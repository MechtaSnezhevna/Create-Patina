package io.github.mechtasnezhevna.createpatina.util;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.capabilities.Capabilities;

public final class OxidizeUtil {
    
    private OxidizeUtil() {}


    /**
     * Replaces a block with another state from the same logical Patina family while preserving
     * shared properties and BlockEntity contents.
     *
     * <p>The old BlockEntity is deliberately unregistered before {@link Level#setBlockAndUpdate}.
     * Create blocks such as the Item Drain drop their held item from {@code onRemove}; removing
     * the BlockEntity first prevents that destructive removal path from seeing data which is
     * about to be migrated.</p>
     */
    public static void replaceWithState(BlockState oldState, BlockState newState, Level level, BlockPos pos) {
        BlockState currentState = level.getBlockState(pos);
        if (currentState != oldState) {
            return;
        }

        BlockState finalState = newState;
        for (Property<?> property : currentState.getProperties()) {
            if (finalState.hasProperty(property)) {
                finalState = copyProperty(currentState, finalState, property);
            }
        }

        if (currentState == finalState) {
            return;
        }

        BlockEntity oldBE = level.getBlockEntity(pos);
        CompoundTag blockEntityData = null;
        if (oldBE != null) {
            if (!finalState.hasBlockEntity()) {
                throw new IllegalArgumentException("Cannot preserve BlockEntity data when replacing "
                        + currentState + " with non-BlockEntity state " + finalState + " at " + pos);
            }

            blockEntityData = oldBE.saveWithoutMetadata(level.registryAccess());

            level.removeBlockEntity(pos);
        }

        if (!level.setBlockAndUpdate(pos, finalState)) {
            if (oldBE != null) {
                restoreOriginalBlockEntity(level, pos, currentState, oldBE, blockEntityData);
            }
            throw new IllegalStateException("Failed to replace " + currentState + " with "
                    + finalState + " at " + pos);
        }

        if (blockEntityData != null) {
            BlockEntity newBE = level.getBlockEntity(pos);
            if (newBE == null) {
                restoreOriginalBlockEntity(level, pos, currentState, oldBE, blockEntityData);
                throw new IllegalStateException("Replacement state " + finalState
                        + " did not create a BlockEntity at " + pos);
            }
            if (newBE.getClass() != oldBE.getClass()) {
                restoreOriginalBlockEntity(level, pos, currentState, oldBE, blockEntityData);
                throw new IllegalStateException("Cannot migrate BlockEntity data between incompatible classes "
                        + oldBE.getClass().getName() + " and " + newBE.getClass().getName()
                        + " at " + pos);
            }

            try {
                newBE.loadWithComponents(blockEntityData.copy(), level.registryAccess());
            } catch (RuntimeException exception) {
                restoreOriginalBlockEntity(level, pos, currentState, oldBE, blockEntityData);
                throw new IllegalStateException("Failed to migrate BlockEntity data from "
                        + currentState + " to " + finalState + " at " + pos, exception);
            }

            newBE.setChanged();
            markBlockEntityForSync(level, pos);
        }

        if (finalState.getBlock() instanceof PatinaBlock patina && level instanceof ServerLevel server) {
            patina.actionWhenReplaced(currentState, finalState, server, pos);
        }
    }

    private static void restoreOriginalBlockEntity(
            Level level, BlockPos pos, BlockState oldState, BlockEntity oldBE, CompoundTag blockEntityData
    ) {
        level.removeBlockEntity(pos);

        if (level.getBlockState(pos) != oldState && !level.setBlockAndUpdate(pos, oldState)) {
            throw new IllegalStateException("Failed to restore original state " + oldState + " at " + pos);
        }

        BlockEntity generatedBE = level.getBlockEntity(pos);
        if (generatedBE != null && generatedBE != oldBE) {
            level.removeBlockEntity(pos);
        }

        BlockEntity restoredBE = oldBE.getType().create(pos, oldState);
        if (restoredBE == null) {
            throw new IllegalStateException("Failed to recreate original BlockEntity "
                    + oldBE.getType() + " at " + pos);
        }

        restoredBE.loadWithComponents(blockEntityData.copy(), level.registryAccess());
        level.setBlockEntity(restoredBE);
        restoredBE.setChanged();
        markBlockEntityForSync(level, pos);
    }

    private static void markBlockEntityForSync(Level level, BlockPos pos) {
        /*
         * Verified against Minecraft 1.21.1 ChunkHolder#broadcastChanges
         * (NeoForge 21.1.228, 2026-07-26): a queued block change sends the final
         * BlockEntity update packet after the block-state packet in the same broadcast.
         */
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(pos);
        }
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }
}

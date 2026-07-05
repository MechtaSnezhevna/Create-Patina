package io.github.mechtasnezhevna.createpatina.util;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class OxidizeUtil {
    
    private OxidizeUtil() {}


    /**
     * 通用的状态保留替换方法
     */
    public static void replaceWithState(BlockState oldState, BlockState newState, Level level, BlockPos pos) {

        BlockState finalState = newState;
        for (Property<?> property : oldState.getProperties()) {
            if (finalState.hasProperty(property)) {
                finalState = copyProperty(oldState, finalState, property);
            }
        }

        BlockEntity oldBE = level.getBlockEntity(pos);
        CompoundTag tag = null;
        if (oldBE != null) {
            tag = oldBE.saveWithFullMetadata(level.registryAccess());
//            level.removeBlockEntity(pos);
//            level.invalidateCapabilities(pos);
        }

        level.setBlockAndUpdate(pos, finalState);

        if (tag != null) {
            BlockEntity newBE = level.getBlockEntity(pos);
            if (newBE != null) {
                newBE.loadWithComponents(tag, level.registryAccess());
//                if (newBE instanceof SyncedBlockEntity synced) {
//                    synced.notifyUpdate();
//                } else {
//                    newBE.setChanged();
//                }
                newBE.setChanged();
            }
        }

        if (finalState.getBlock() instanceof PatinaBlock patina && level instanceof ServerLevel server) {
            patina.actionWhenReplaced(oldState, newState, server, pos);
        }

    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }
}

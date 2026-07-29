package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.PortableStorageInterfaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

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

        BlockState finalState = copySharedProperties(currentState, newState);

        if (currentState == finalState) {
            return;
        }

        BlockEntity oldBE = level.getBlockEntity(pos);
        boolean replacingFluidTank = oldBE instanceof FluidTankBlockEntity;
        AbstractContraptionEntity connectedContraption = null;
        float portableInterfaceDistance = 0;
        int portableInterfaceTransferTimer = 0;
        int portableInterfaceKeepAlive = 0;
        BlockState stateToRestore = currentState;
        CompoundTag blockEntityData = null;
        if (oldBE != null) {
            if (!finalState.hasBlockEntity()) {
                throw new IllegalArgumentException("Cannot preserve BlockEntity data when replacing "
                        + currentState + " with non-BlockEntity state " + finalState + " at " + pos);
            }

            if (oldBE instanceof PortableFluidInterfaceBlockEntity) {
                PortableStorageInterfaceBlockEntityAccessor accessor =
                        (PortableStorageInterfaceBlockEntityAccessor) oldBE;
                if (accessor.createPatina$getConnectedEntity() instanceof AbstractContraptionEntity contraption) {
                    connectedContraption = contraption;
                    portableInterfaceDistance = accessor.createPatina$getDistance();
                    portableInterfaceTransferTimer = accessor.createPatina$getTransferTimer();
                    portableInterfaceKeepAlive =
                            ((PortableFluidInterfaceBlockEntity) oldBE).keepAlive;
                }
            }

            prepareBlockEntityForReplacement(oldBE);

            /*
             * ConnectivityHandler#splitMulti resets a tank's TOP, BOTTOM and SHAPE properties.
             * Copy from that detached state instead of preserving the stale CT properties that
             * belonged to the old multi-block.
             */
            stateToRestore = level.getBlockState(pos);
            finalState = copySharedProperties(stateToRestore, newState);
            blockEntityData = oldBE.saveWithoutMetadata();

            level.removeBlockEntity(pos);
        }

        if (!setReplacementState(level, pos, finalState, replacingFluidTank)) {
            if (oldBE != null) {
                restoreOriginalBlockEntity(
                        level, pos, stateToRestore, oldBE, blockEntityData, replacingFluidTank
                );
            }
            throw new IllegalStateException("Failed to replace " + currentState + " with "
                    + finalState + " at " + pos);
        }

        if (blockEntityData != null) {
            BlockEntity newBE = level.getBlockEntity(pos);
            if (newBE == null) {
                restoreOriginalBlockEntity(
                        level, pos, stateToRestore, oldBE, blockEntityData, replacingFluidTank
                );
                throw new IllegalStateException("Replacement state " + finalState
                        + " did not create a BlockEntity at " + pos);
            }
            if (newBE.getClass() != oldBE.getClass()) {
                restoreOriginalBlockEntity(
                        level, pos, stateToRestore, oldBE, blockEntityData, replacingFluidTank
                );
                throw new IllegalStateException("Cannot migrate BlockEntity data between incompatible classes "
                        + oldBE.getClass().getName() + " and " + newBE.getClass().getName()
                        + " at " + pos);
            }

            try {
                newBE.load(blockEntityData.copy());
            } catch (RuntimeException exception) {
                restoreOriginalBlockEntity(
                        level, pos, stateToRestore, oldBE, blockEntityData, replacingFluidTank
                );
                throw new IllegalStateException("Failed to migrate BlockEntity data from "
                        + currentState + " to " + finalState + " at " + pos, exception);
            }

            if (newBE instanceof PortableFluidInterfaceBlockEntity portableInterface
                    && connectedContraption != null
                    && connectedContraption.isAlive()
                    && connectedContraption.getContraption() != null) {
                /*
                 * Original Create code from
                 * PortableStorageInterfaceMovement#tick:
                 * if (stationaryInterface.connectedEntity == null)
                 *     stationaryInterface.startTransferringTo(context.contraption, stationaryInterface.distance);
                 *
                 * Original Create code from
                 * PortableFluidInterfaceBlockEntity#startTransferringTo:
                 * capability = new InterfaceFluidHandler(contraption.getStorage().getFluids());
                 * invalidateCapability();
                 * super.startTransferringTo(contraption, distance);
                 */
                portableInterface.startTransferringTo(
                        connectedContraption.getContraption(), portableInterfaceDistance
                );
                /*
                 * Original Create code from PortableStorageInterfaceBlockEntity#startTransferringTo:
                 * this.distance = Math.min(2, distance);
                 * connectedEntity = contraption.entity;
                 * startConnecting();
                 * notifyUpdate();
                 *
                 * Original Create code from PortableStorageInterfaceBlockEntity#startConnecting:
                 * transferTimer = 6 + ANIMATION * 2;
                 *
                 * Rebinding must rebuild connectedEntity and the fluid handler, but replacing the
                 * active timeout with the short initial-connection timer makes the contraption
                 * leave before the refreshed pipe network can resume content transfer.
                 */
                PortableStorageInterfaceBlockEntityAccessor accessor =
                        (PortableStorageInterfaceBlockEntityAccessor) portableInterface;
                accessor.createPatina$setTransferTimer(portableInterfaceTransferTimer);
                portableInterface.keepAlive = portableInterfaceKeepAlive;
            }

            newBE.setChanged();
            markBlockEntityForSync(level, pos);
        }

        if (finalState.getBlock() instanceof PatinaBlock patina && level instanceof ServerLevel server) {
            patina.actionWhenReplaced(currentState, finalState, server, pos);
        }
    }

    private static void prepareBlockEntityForReplacement(BlockEntity blockEntity) {
        if (!(blockEntity instanceof FluidTankBlockEntity tankBE)) {
            return;
        }

        /*
         * Original Create code from FluidTankBlock#onRemove:
         * if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
         *     BlockEntity be = world.getBlockEntity(pos);
         *     if (!(be instanceof FluidTankBlockEntity tankBE))
         *         return;
         *     world.removeBlockEntity(pos);
         *     ConnectivityHandler.splitMulti(tankBE);
         * }
         */
        ConnectivityHandler.splitMulti(tankBE);

        /*
         * Original Create code from ItemVaultBlock#onWrenched:
         * if (be instanceof ItemVaultBlockEntity vault) {
         *     ConnectivityHandler.splitMulti(vault);
         *     vault.removeController(true);
         * }
         *
         * splitMulti is a no-op for an existing 1x1 structure. Calling removeController(true)
         * as Create does for a structural reset guarantees that even a single tank is saved
         * with the Uninitialized marker and reconnects on its next BlockEntity tick.
         */
        tankBE.removeController(true);
    }

    private static boolean setReplacementState(
            Level level, BlockPos pos, BlockState state, boolean suppressImmediateTankConnectivity
    ) {
        int flags = Block.UPDATE_ALL;
        if (suppressImmediateTankConnectivity) {
            /*
             * Original Create code from FluidTankBlock#onPlace:
             * if (oldState.getBlock() == state.getBlock())
             *     return;
             * if (moved)
             *     return;
             * withBlockEntityDo(world, pos, FluidTankBlockEntity::updateConnectivity);
             *
             * The replacement BlockEntity does not receive its preserved NBT until setBlock
             * returns. UPDATE_MOVE_BY_PISTON supplies moved=true and prevents Create from
             * forming a multi-block against that empty, not-yet-restored BlockEntity.
             */
            flags |= Block.UPDATE_MOVE_BY_PISTON;
        }
        return level.setBlock(pos, state, flags);
    }

    private static void restoreOriginalBlockEntity(
            Level level, BlockPos pos, BlockState oldState, BlockEntity oldBE,
            CompoundTag blockEntityData, boolean restoringFluidTank
    ) {
        level.removeBlockEntity(pos);

        if (level.getBlockState(pos) != oldState
                && !setReplacementState(level, pos, oldState, restoringFluidTank)) {
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

        restoredBE.load(blockEntityData.copy());
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

    private static BlockState copySharedProperties(BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            if (to.hasProperty(property)) {
                to = copyProperty(from, to, property);
            }
        }
        return to;
    }
}

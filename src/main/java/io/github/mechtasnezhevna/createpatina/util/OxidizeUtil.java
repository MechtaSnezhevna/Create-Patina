package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.PortableStorageInterfaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;
import java.util.function.Function;

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
            blockEntityData = oldBE.saveWithoutMetadata(level.registryAccess());

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
                newBE.loadWithComponents(blockEntityData.copy(), level.registryAccess());
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

    /**
     * Applies a tool-driven weathering-state change to a block, or to the entire fluid tank
     * multiblock when {@code wholeTank} is enabled and the target is part of one.
     *
     * <p>Waxing, de-waxing or scraping one block of a large tank splits its multiblock, because
     * mixed weathering states never connect to each other (see ConnectivityHandlerMixin). When
     * the whole tank is operated on instead, every part shifts through the same transition while
     * the multiblock structure, fluid contents and connected-texture state are left untouched, so
     * the tank never splits apart, flickers, or loses its fluid-level display.</p>
     */
    public static void applyToolWeathering(
            BlockState oldState, BlockState newState, Level level, BlockPos pos, boolean wholeTank
    ) {
        if (!wholeTank) {
            replaceWithState(oldState, newState, level, pos);
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FluidTankBlockEntity tankBE)) {
            replaceWithState(oldState, newState, level, pos);
            return;
        }
        FluidTankBlockEntity controller = tankBE.getControllerBE();
        if (controller == null) {
            replaceWithState(oldState, newState, level, pos);
            return;
        }

        Function<BlockState, Optional<BlockState>> transition = toolTransition(oldState, newState);
        if (transition == null) {
            replaceWithState(oldState, newState, level, pos);
            return;
        }

        /*
         * The whole-tank replacement runs on the server as well as on the client-side prediction,
         * so both sides apply the identical change to the identical blocks.
         */
        weatherWholeFluidTank(controller, level, transition);
    }

    /**
     * Derives the per-part transition a tool applied to the clicked block. All parts of one tank
     * multiblock share the same weathering type, so the same transition applies to every part.
     */
    private static Function<BlockState, Optional<BlockState>> toolTransition(
            BlockState current, BlockState target
    ) {
        if (sameBlock(WaxUtil.getWaxed(current), target)) {
            return WaxUtil::getWaxed;
        }
        if (sameBlock(WeatheringCopper.getPrevious(current), target)) {
            return WeatheringCopper::getPrevious;
        }
        if (sameBlock(WaxUtil.getUnwaxed(current), target)) {
            return WaxUtil::getUnwaxed;
        }
        return null;
    }

    private static boolean sameBlock(Optional<BlockState> candidate, BlockState target) {
        return candidate.isPresent() && candidate.get().getBlock() == target.getBlock();
    }

    /**
     * Moves every part of a fluid tank multiblock through the same weathering transition without
     * splitting the structure: controller position, width and height, fluid contents, BlockEntity
     * data and connected-texture properties are all preserved, only the block type itself changes.
     *
     * <p>Because the multiblock never detaches, no async re-formation on a later BlockEntity tick
     * is needed, and the client never renders a transient per-block split state. Used by tool
     * interactions (axe / honeycomb / sandpaper, by hand or by deployer) and by natural
     * random-tick weathering when whole-tank weathering is enabled.</p>
     */
    public static void weatherWholeFluidTank(
            FluidTankBlockEntity controller, Level level,
            Function<BlockState, Optional<BlockState>> transition
    ) {
        /*
         * Mirrors ConnectivityHandler#splitMultiAndInvalidate: width x width x height starting at
         * the controller. Width, height, axis and origin are captured up front because replacing
         * the controller block swaps its BlockEntity for a fresh instance.
         */
        int width = controller.getWidth();
        int height = controller.getHeight();
        Direction.Axis axis = controller.getMainConnectionAxis();
        BlockPos origin = controller.getBlockPos();
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos partPos = switch (axis) {
                        case X -> origin.offset(yOffset, xOffset, zOffset);
                        case Y -> origin.offset(xOffset, yOffset, zOffset);
                        case Z -> origin.offset(xOffset, zOffset, yOffset);
                    };
                    BlockState partState = level.getBlockState(partPos);
                    if (!(partState.getBlock() instanceof FluidTankBlock)) {
                        continue;
                    }
                    BlockEntity partBE = level.getBlockEntity(partPos);
                    if (!(partBE instanceof FluidTankBlockEntity partTank)
                            || !isPartOfMultiblock(partTank, origin)) {
                        continue;
                    }
                    Optional<BlockState> nextState = transition.apply(partState);
                    if (nextState.isEmpty()) {
                        continue;
                    }
                    replaceTankPartState(level, partPos, partState, nextState.get());
                }
            }
        }
    }

    private static boolean isPartOfMultiblock(FluidTankBlockEntity part, BlockPos controllerPos) {
        return part.isController() ? part.getBlockPos().equals(controllerPos)
                : controllerPos.equals(part.getController());
    }

    /**
     * Swaps the weathering type of one tank part while keeping its BlockEntity data and leaving
     * the multiblock it belongs to connected. Unlike {@link #replaceWithState(BlockState,
     * BlockState, Level, BlockPos)}, the surrounding tank is never split into single blocks.
     */
    private static void replaceTankPartState(
            Level level, BlockPos pos, BlockState oldState, BlockState newState
    ) {
        BlockState currentState = level.getBlockState(pos);
        if (currentState != oldState) {
            return;
        }

        BlockState finalState = copySharedProperties(currentState, newState);
        if (currentState == finalState) {
            return;
        }

        BlockEntity oldBE = level.getBlockEntity(pos);
        CompoundTag blockEntityData = null;
        if (oldBE instanceof FluidTankBlockEntity) {
            if (!finalState.hasBlockEntity()) {
                throw new IllegalArgumentException("Cannot preserve BlockEntity data when replacing "
                        + currentState + " with non-BlockEntity state " + finalState + " at " + pos);
            }

            blockEntityData = oldBE.saveWithoutMetadata(level.registryAccess());
            level.removeBlockEntity(pos);
        }

        if (!setReplacementState(level, pos, finalState, true)) {
            if (blockEntityData != null) {
                restoreOriginalBlockEntity(
                        level, pos, currentState, oldBE, blockEntityData, true
                );
            }
            throw new IllegalStateException("Failed to replace " + currentState + " with "
                    + finalState + " at " + pos);
        }

        if (blockEntityData != null) {
            BlockEntity newBE = level.getBlockEntity(pos);
            if (newBE == null || newBE.getClass() != oldBE.getClass()) {
                restoreOriginalBlockEntity(
                        level, pos, currentState, oldBE, blockEntityData, true
                );
                throw new IllegalStateException("Replacement state " + finalState
                        + " did not create a compatible BlockEntity at " + pos);
            }

            try {
                newBE.loadWithComponents(blockEntityData.copy(), level.registryAccess());
            } catch (RuntimeException exception) {
                restoreOriginalBlockEntity(
                        level, pos, currentState, oldBE, blockEntityData, true
                );
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

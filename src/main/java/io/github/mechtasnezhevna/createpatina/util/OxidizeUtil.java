package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
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

        for (Direction d : Direction.values()) {
            BlockPos neighborPos = pos.relative(d);
            BlockState neighborState = level.getBlockState(neighborPos);
            FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, neighborPos);
            if (pipe != null) {
                // Force remove old connection
                if (pipe.interfaces != null) {
                    pipe.interfaces.remove(d.getOpposite());
                }
                FluidPropagator.propagateChangedPipe(level, neighborPos, neighborState);
                FluidPropagator.resetAffectedFluidNetworks(level, neighborPos, d.getOpposite());
            }

            BlockEntity neighborBE = level.getBlockEntity(neighborPos);
            if (neighborBE instanceof PumpBlockEntity pumpBE) {
                pumpBE.updatePressureChange();
            }
        }

    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }

    public static void changeOverTimeWithState(WeatheringCopper copper, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float f = 0.05688889F;
        if (random.nextFloat() < f) {
            copper.getNextState(state, level, pos, random).ifPresent((next) ->
                    replaceWithState(state, next, level, pos));
        }
    }
}

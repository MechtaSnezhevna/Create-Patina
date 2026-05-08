package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class OxidizeUtil {
    
    private OxidizeUtil() {}

    /**
     * 用于氧化的自动替换
     */
    public static void applyChangeWithState(BlockState state, Level level, BlockPos pos) {
        WeatheringCopper.getNext(state.getBlock()).ifPresent(nextBlock -> {
            replaceWithState(state, nextBlock.defaultBlockState(), level, pos);
        });
    }

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
        }

        level.setBlockAndUpdate(pos, finalState);

        if (tag != null) {
            BlockEntity newBE = level.getBlockEntity(pos);
            if (newBE != null) {
                newBE.loadWithComponents(tag, level.registryAccess());
                newBE.setChanged();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }
}

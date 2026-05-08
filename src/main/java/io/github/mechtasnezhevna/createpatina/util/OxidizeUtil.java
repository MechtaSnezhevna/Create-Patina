package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public final class OxidizeUtil {
    
    private OxidizeUtil() {}

    /**
     * Applies oxidation to a block while preserving its properties (like facing) and BlockEntity data.
     */
    public static void applyChangeWithState(BlockState state, ServerLevel level, BlockPos pos) {
        WeatheringCopper.getNext(state.getBlock()).ifPresent(nextBlock -> {
            BlockState nextState = nextBlock.defaultBlockState();

            for (Property<?> property : state.getProperties()) {
                if (nextState.hasProperty(property)) {
                    nextState = copyProperty(state, nextState, property);
                }
            }

            BlockEntity oldBE = level.getBlockEntity(pos);
            CompoundTag tag = null;
            if (oldBE != null) {
                tag = oldBE.saveWithFullMetadata(level.registryAccess());
            }

            level.setBlockAndUpdate(pos, nextState);

            if (tag != null) {
                BlockEntity newBE = level.getBlockEntity(pos);
                if (newBE != null) {
                    newBE.loadWithComponents(tag, level.registryAccess());
                    newBE.setChanged();
                }
            }
        });
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }

}

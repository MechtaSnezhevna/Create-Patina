package io.github.mechtasnezhevna.createpatina.block;

import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.mechtasnezhevna.createpatina.util.OxidizeUtil.replaceWithState;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface PatinaBlock extends WeatheringCopper {

    WeatheringType getType();

    @Override
    default WeatherState getAge() {
        return this.getType().getWeatherState();
    }

    /**
     * Checks if this block can weather. If the block is waxed, it cannot weather.
     * This method is used to determine if the block should be randomly ticking and if it should change over time.
     * Override this method if you want to disable weathering for a block specifically.
     * @return true if the block can weather, false otherwise
     */
    default boolean isWeatheringEnabled() {
        return !getType().isWaxed();
    }

    /**
     * This method is called after the block being replaced by another block state.
     * Override this method if you want to change something else.
     */
    default void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
    }

    @Override
    default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float f = 0.05688889F;
        if (random.nextFloat() < f) {
            this.getNextState(state, level, pos, random).ifPresent((next) ->
                    replaceWithState(state, next, level, pos));
        }
    }
}

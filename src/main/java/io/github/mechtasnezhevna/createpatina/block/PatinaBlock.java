package io.github.mechtasnezhevna.createpatina.block;

import io.github.mechtasnezhevna.createpatina.PatinaConfig;
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
     * Determines whether natural weathering is permitted for this block.
     * Override this policy hook to exclude blocks that must not weather naturally.
     */
    default boolean allowsNaturalWeathering() {
        return !getType().isWaxed();
    }

    /**
     * Determines whether this block is permitted to weather and still has a later weathering stage.
     */
    default boolean canAdvanceWeathering() {
        return allowsNaturalWeathering() && getType().getNext() != null;
    }

    /**
     * This method is called after the block being replaced by another block state.
     * Override this method if you want to change something else.
     */
    default void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
    }

    @Override
    default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!PatinaConfig.CONFIG.IS_RANDOM_TICK_WEATHERING_ENABLED.get()) {
            return;
        }
        float f = 0.05688889F;
        if (random.nextFloat() < f) {
            this.getNextState(state, level, pos, random).ifPresent((next) ->
                    replaceWithState(state, next, level, pos));
        }
    }
}

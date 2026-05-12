package io.github.mechtasnezhevna.createpatina.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.mechtasnezhevna.createpatina.util.OxidizeUtil.replaceWithState;

public interface PatinaBlock extends WeatheringCopper {

    void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos);

    @Override
    default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float f = 0.05688889F;
        if (random.nextFloat() < f) {
            this.getNextState(state, level, pos, random).ifPresent((next) ->
                    replaceWithState(state, next, level, pos));
        }
    }
}

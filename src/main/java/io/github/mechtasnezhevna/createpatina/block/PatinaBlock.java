package io.github.mechtasnezhevna.createpatina.block;

import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
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
     * Keeps vanilla's 1.20.1 weathering scan and probability calculation, while ignoring
     * non-weathering Patina neighbours and preserving BlockEntity data during replacement.
     */
    @Override
    default void applyChangeOverTime(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        if (!allowsNaturalWeathering()) {
            return;
        }

        int age = getAge().ordinal();
        int sameAgeNeighbours = 0;
        int olderNeighbours = 0;

        /*
         * Original Minecraft 1.20.1 code from ChangeOverTimeBlock#applyChangeOverTime:
         * for (BlockPos blockpos : BlockPos.withinManhattan(pos, 4, 4, 4)) {
         *     int l = blockpos.distManhattan(pos);
         *     if (l > 4) {
         *         break;
         *     }
         *
         *     if (!blockpos.equals(pos)) {
         *         BlockState blockstate = level.getBlockState(blockpos);
         *         Block block = blockstate.getBlock();
         *         if (block instanceof ChangeOverTimeBlock) {
         *             Enum<?> oenum = ((ChangeOverTimeBlock)block).getAge();
         *             if (this.getAge().getClass() == oenum.getClass()) {
         *                 int i1 = oenum.ordinal();
         *                 if (i1 < i) {
         *                     return;
         *                 }
         *
         *                 if (i1 > i) {
         *                     ++k;
         *                 } else {
         *                     ++j;
         *                 }
         *             }
         *         }
         *     }
         * }
         */
        for (BlockPos neighbourPos : BlockPos.withinManhattan(pos, 4, 4, 4)) {
            if (neighbourPos.distManhattan(pos) > 4) {
                break;
            }
            if (neighbourPos.equals(pos)) {
                continue;
            }

            Block block = level.getBlockState(neighbourPos).getBlock();
            if (block instanceof PatinaBlock patinaBlock
                    && !patinaBlock.allowsNaturalWeathering()) {
                continue;
            }
            if (!(block instanceof ChangeOverTimeBlock<?> changeOverTimeBlock)) {
                continue;
            }

            Enum<?> neighbourAge = changeOverTimeBlock.getAge();
            if (getAge().getClass() != neighbourAge.getClass()) {
                continue;
            }

            int neighbourOrdinal = neighbourAge.ordinal();
            if (neighbourOrdinal < age) {
                return;
            }
            if (neighbourOrdinal > age) {
                olderNeighbours++;
            } else {
                sameAgeNeighbours++;
            }
        }

        float ageRatio = (float) (olderNeighbours + 1)
                / (float) (olderNeighbours + sameAgeNeighbours + 1);
        float chance = ageRatio * ageRatio * getChanceModifier();
        if (random.nextFloat() >= chance) {
            return;
        }

        /*
         * Original Minecraft 1.20.1 code from ChangeOverTimeBlock#applyChangeOverTime:
         * this.getNext(state).ifPresent((p_153039_) -> {
         *     level.setBlockAndUpdate(pos, p_153039_);
         * });
         */
        getNext(state).ifPresent(newState ->
                replaceWithState(state, newState, level, pos)
        );
    }

    /**
     * This method is called after the block being replaced by another block state.
     * Override this method if you want to change something else.
     */
    default void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
    }

    // @Override  // best ojng use onRandomTick in 1.20.1
    default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float f = 0.05688889F;
        if (random.nextFloat() < f) {
            applyChangeOverTime(state, level, pos, random);
        }
    }
}

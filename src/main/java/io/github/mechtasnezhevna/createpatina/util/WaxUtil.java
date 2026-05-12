package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.DataMapHooks;

import java.util.Optional;

public class WaxUtil {

    public static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(DataMapHooks.getBlockWaxed(block));
    }

    public static Optional<Block> getUnwaxed(Block block) {
        return Optional.ofNullable(DataMapHooks.getBlockUnwaxed(block));
    }

    public static Optional<BlockState> getWaxed(BlockState state) {
        return getWaxed(state.getBlock()).map((b) -> b.withPropertiesOf(state));
    }

    public static Optional<BlockState> getUnwaxed(BlockState state) {
        return getUnwaxed(state.getBlock()).map((b) -> b.withPropertiesOf(state));
    }
}

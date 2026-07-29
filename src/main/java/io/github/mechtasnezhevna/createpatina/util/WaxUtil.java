package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class WaxUtil {

    public static Optional<Block> getWaxed(Block block) {
        // verified: Minecraft 1.20.1 HoneycombItem WAXABLES/WAX_OFF_BY_BLOCK source, 2026-07-30
        return Optional.ofNullable(HoneycombItem.WAXABLES.get().get(block));
    }

    public static Optional<Block> getUnwaxed(Block block) {
        return Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(block));
    }

    public static Optional<BlockState> getWaxed(BlockState state) {
        return getWaxed(state.getBlock()).map((b) -> b.withPropertiesOf(state));
    }

    public static Optional<BlockState> getUnwaxed(BlockState state) {
        return getUnwaxed(state.getBlock()).map((b) -> b.withPropertiesOf(state));
    }
}

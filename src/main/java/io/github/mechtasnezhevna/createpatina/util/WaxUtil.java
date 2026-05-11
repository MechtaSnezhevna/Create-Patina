package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.DataMapHooks;

import java.util.Optional;

public class WaxUtil {

    public static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(DataMapHooks.getBlockWaxed(block));
    }

    public static Optional<Block> getUnwaxed(Block block) {
        return Optional.ofNullable(DataMapHooks.getBlockUnwaxed(block));
    }
}

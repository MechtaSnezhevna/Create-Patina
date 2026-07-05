package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;

public class PatinaStress {
    /**
     * 设置方块的应力消耗
     */
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return builder -> builder.onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> value));
    }

    /**
     * 设置方块的应力容量
     */
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return builder -> builder.onRegister(block -> BlockStressValues.CAPACITIES.register(block, () -> value));
    }
}

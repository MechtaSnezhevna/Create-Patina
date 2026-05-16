package io.github.mechtasnezhevna.createpatina.registry.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CopperChainBuilder<B extends Block & PatinaBlock, R extends AbstractRegistrate<R>> {

    private final Map<WeatheringType, BlockBuilder<B, R>> builders = new EnumMap<>(WeatheringType.class);

    private BlockEntry<? extends Block> unaffected = null;

    public CopperChainBuilder(R owner, String name, BiFunction<WeatheringType, BlockBehaviour.Properties, B> factory) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) {
                continue;
            }
            String registryName = type.getPrefix() + name;
            BlockBuilder<B, R> builder = owner.block(registryName, p -> factory.apply(type, p));
            builders.put(type, builder);
        }
    }

    public CopperChainBuilder<B, R> configure(Consumer<BlockBuilder<B, R>> consumer) {
        builders.values().forEach(consumer);
        return this;
    }

    public CopperChainBuilder<B, R> unaffected(BlockEntry<? extends Block> unaffected) {
        this.unaffected = unaffected;
        return this;
    }

    public CopperChain<B> register() {
        Map<WeatheringType, BlockEntry<B>> res = new EnumMap<>(WeatheringType.class);
        builders.forEach((type, b) -> res.put(type, b.register()));
        return new CopperChain<>(res, unaffected);
    }
}

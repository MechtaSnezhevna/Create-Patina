package io.github.mechtasnezhevna.createpatina.registry.chain;

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
    private final boolean registerUnaffected;

    public CopperChainBuilder(R owner, String name, BiFunction<WeatheringType, BlockBehaviour.Properties, B> factory, boolean registerUnaffected) {
        this.registerUnaffected = registerUnaffected;
        for (WeatheringType type : WeatheringType.values()) {
            if (!registerUnaffected && type == WeatheringType.UNAFFECTED) {
                continue;
            }
            String registryName = type.getPrefix() + name;
            BlockBuilder<B, R> builder = owner.block(registryName, p -> factory.apply(type, p));
            builders.put(type, builder);
        }
    }

    public CopperChainBuilder(R owner, String name, BiFunction<WeatheringType, BlockBehaviour.Properties, B> factory) {
        this(owner, name, factory, false);
    }

    public CopperChainBuilder<B, R> configure(Consumer<BlockBuilder<B, R>> consumer) {
        builders.values().forEach(consumer);
        return this;
    }

    public CopperChainBuilder<B, R> unaffected(BlockEntry<? extends Block> unaffected) {
        if (registerUnaffected) {
            throw new IllegalStateException("This CopperChainBuilder is configured to auto register an unaffected block.");
        }
        this.unaffected = unaffected;
        return this;
    }

    public CopperChain<B> register() {
        Map<WeatheringType, BlockEntry<B>> res = new EnumMap<>(WeatheringType.class);
        builders.forEach((type, b) -> res.put(type, b.register()));
        if (registerUnaffected) {
            unaffected = res.get(WeatheringType.UNAFFECTED);
        }
        registerWeatheringAndWaxable(res);
        return new CopperChain<>(res, unaffected);
    }

    private void registerWeatheringAndWaxable(Map<WeatheringType, BlockEntry<B>> res) {
        if (unaffected != null) CopperRegistries.addWeathering(unaffected, res.get(WeatheringType.EXPOSED));
        CopperRegistries.addWeathering(res.get(WeatheringType.EXPOSED), res.get(WeatheringType.WEATHERED));
        CopperRegistries.addWeathering(res.get(WeatheringType.WEATHERED), res.get(WeatheringType.OXIDIZED));

        if (unaffected != null) CopperRegistries.addWaxable(unaffected, res.get(WeatheringType.WAXED));
        CopperRegistries.addWaxable(res.get(WeatheringType.EXPOSED), res.get(WeatheringType.WAXED_EXPOSED));
        CopperRegistries.addWaxable(res.get(WeatheringType.WEATHERED), res.get(WeatheringType.WAXED_WEATHERED));
        CopperRegistries.addWaxable(res.get(WeatheringType.OXIDIZED), res.get(WeatheringType.WAXED_OXIDIZED));
    }
}

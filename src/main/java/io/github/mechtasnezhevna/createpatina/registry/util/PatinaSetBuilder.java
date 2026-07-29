package io.github.mechtasnezhevna.createpatina.registry.util;

import com.simibubi.create.foundation.block.CopperRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.util.PatinaMapColor;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class PatinaSetBuilder<B extends Block, R extends AbstractRegistrate<R>> {

    private final Map<WeatheringType, BlockBuilder<B, R>> builders = new EnumMap<>(WeatheringType.class);

    private BlockEntry<? extends Block> unaffectedBlock;

    public PatinaSetBuilder(R owner, String name, Function<BlockBehaviour.Properties, B> factory, boolean buildUnaffectedVariant) {
        for (WeatheringType type : WeatheringType.values()) {
            if (!buildUnaffectedVariant && type == WeatheringType.UNAFFECTED) {
                continue;
            }
            String registryName = type.getPrefix() + name;
            BlockBuilder<B, R> builder = owner.block(registryName, factory::apply);
            builders.put(type, builder);
        }
    }

    public PatinaSetBuilder(R owner, String name, Function<BlockBehaviour.Properties, B> factory) {
        this(owner, name, factory, false);
    }

    public PatinaSetBuilder(R owner, String name, BiFunction<WeatheringType, BlockBehaviour.Properties, B> factory, boolean buildUnaffectedVariant) {
        for (WeatheringType type : WeatheringType.values()) {
            if (!buildUnaffectedVariant && type == WeatheringType.UNAFFECTED) {
                continue;
            }
            String registryName = type.getPrefix() + name;
            BlockBuilder<B, R> builder = owner.block(registryName, p -> factory.apply(type, p));
            builders.put(type, builder);
        }
    }

    public PatinaSetBuilder(R owner, String name, BiFunction<WeatheringType, BlockBehaviour.Properties, B> factory) {
        this(owner, name, factory, false);
    }

    public PatinaSetBuilder<B, R> configure(Consumer<BlockBuilder<B, R>> consumer) {
        builders.values().forEach(consumer);
        return this;
    }

    public PatinaSetBuilder<B, R> configure(BiConsumer<WeatheringType, BlockBuilder<B, R>> consumer) {
        builders.forEach(consumer);
        return this;
    }

    public PatinaSetBuilder<B, R> mapColor() {
        configure((type, builder) ->
                builder.properties(p -> p.mapColor(PatinaMapColor.getMapColorByType(type))));
        return this;
    }

    public PatinaSetBuilder<B, R> unaffected(BlockEntry<? extends Block> unaffected) {
        unaffectedBlock = unaffected;
        return this;
    }

    public PatinaSet register() {
        Map<WeatheringType, BlockEntry<? extends Block>> res = new EnumMap<>(WeatheringType.class);
        builders.forEach((type, b) -> res.put(type, b.register()));
        if (unaffectedBlock != null) {
            res.put(WeatheringType.UNAFFECTED, unaffectedBlock);
        }
        if (res.get(WeatheringType.UNAFFECTED) == null) {
            throw new IllegalStateException("Unaffected variant must be registered or provided.");
        }
        registerWeatheringAndWaxable(res);
        return new PatinaSet(res);
    }

    private void registerWeatheringAndWaxable(Map<WeatheringType, BlockEntry<? extends Block>> res) {
        // verified: Create 6.0.8 CopperRegistries/CopperBlockSet source for Forge 1.20.1, 2026-07-30
        CopperRegistries.addWeathering(
                () -> res.get(WeatheringType.UNAFFECTED).get(),
                () -> res.get(WeatheringType.EXPOSED).get()
        );
        CopperRegistries.addWeathering(
                () -> res.get(WeatheringType.EXPOSED).get(),
                () -> res.get(WeatheringType.WEATHERED).get()
        );
        CopperRegistries.addWeathering(
                () -> res.get(WeatheringType.WEATHERED).get(),
                () -> res.get(WeatheringType.OXIDIZED).get()
        );

        CopperRegistries.addWaxable(
                () -> res.get(WeatheringType.UNAFFECTED).get(),
                () -> res.get(WeatheringType.WAXED).get()
        );
        CopperRegistries.addWaxable(
                () -> res.get(WeatheringType.EXPOSED).get(),
                () -> res.get(WeatheringType.WAXED_EXPOSED).get()
        );
        CopperRegistries.addWaxable(
                () -> res.get(WeatheringType.WEATHERED).get(),
                () -> res.get(WeatheringType.WAXED_WEATHERED).get()
        );
        CopperRegistries.addWaxable(
                () -> res.get(WeatheringType.OXIDIZED).get(),
                () -> res.get(WeatheringType.WAXED_OXIDIZED).get()
        );
    }

    private void addExtraPatinaSettings() {
        builders.forEach((type, b) -> {
            b.properties(p -> p.mapColor(PatinaMapColor.getMapColorByType(type)));
        });
    }
}

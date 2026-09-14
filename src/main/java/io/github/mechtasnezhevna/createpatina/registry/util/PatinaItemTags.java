package io.github.mechtasnezhevna.createpatina.registry.util;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * Item tags that classify copper parts by their weathering stage.
 * Each of the eight {@link WeatheringType}s gets its own tag, e.g. {@code createpatina:oxidized}.
 */
public final class PatinaItemTags {

    private static final Map<WeatheringType, TagKey<Item>> BY_TYPE = new EnumMap<>(WeatheringType.class);

    private PatinaItemTags() {
    }

    public static TagKey<Item> forType(WeatheringType type) {
        return BY_TYPE.computeIfAbsent(type, PatinaItemTags::create);
    }

    private static TagKey<Item> create(WeatheringType type) {
        return TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(CreatePatina.MODID, type.name().toLowerCase(Locale.ROOT)));
    }
}

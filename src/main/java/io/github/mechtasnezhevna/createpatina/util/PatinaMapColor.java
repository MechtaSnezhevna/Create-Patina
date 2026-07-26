package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PatinaMapColor {

    @Contract(pure = true)
    public static @Nullable MapColor getMapColorByPrefix(String prefix){
        if (prefix == "" || prefix == "waxed_")
            return MapColor.COLOR_ORANGE;
        if (prefix == "exposed_" || prefix == "waxed_exposed_")
            return MapColor.TERRACOTTA_LIGHT_GRAY;
        if (prefix == "weathered_" || prefix == "waxed_weathered_")
            return MapColor.WARPED_STEM;
        if (prefix == "oxidized_" || prefix == "waxed_oxidized_")
            return MapColor.WARPED_NYLIUM;
        return null;
    }

    @Contract(pure = true)
    public static @NotNull MapColor getMapColorByType(@NotNull WeatheringType type){
        return switch (type) {
            case UNAFFECTED, WAXED -> MapColor.COLOR_ORANGE;
            case EXPOSED, WAXED_EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY;
            case WEATHERED, WAXED_WEATHERED -> MapColor.WARPED_STEM;
            case OXIDIZED, WAXED_OXIDIZED -> MapColor.WARPED_NYLIUM;
        };
    }

    @Contract(pure = true)
    public static MapColor getMapColorByName(String name) {
        return getMapColorByType(WeatheringType.fromIdString(name));
    }
}

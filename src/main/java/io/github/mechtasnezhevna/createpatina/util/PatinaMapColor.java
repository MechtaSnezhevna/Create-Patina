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
            return MapColor.TERRACOTTA_ORANGE;
        if (prefix == "weathered_" || prefix == "waxed_weathered_")
            return MapColor.COLOR_GREEN;
        if (prefix == "oxidized_" || prefix == "waxed_oxidized_")
            return MapColor.TERRACOTTA_GREEN;
        return null;
    }

    @Contract(pure = true)
    public static @Nullable MapColor getMapColorByType(@NotNull WeatheringType type){
        return getMapColorByPrefix(type.getPrefix());
    }

    @Contract(pure = true)
    public static MapColor getMapColorByName(String name){
        String[] prefixes = {"exposed","weathered","oxidized","waxed_", "waxed_exposed_","waxed_weathered_","waxed_oxidized_"};
        for (String prefix : prefixes) {
            if (name.startsWith(prefix)) {
                return getMapColorByPrefix(prefix);
            }
        }
        return getMapColorByPrefix("");
    }
}

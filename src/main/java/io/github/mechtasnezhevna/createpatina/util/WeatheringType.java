package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.level.block.WeatheringCopper;

public enum WeatheringType {
    UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED, WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED;

    public WeatheringCopper.WeatherState getWeatherState() {
        return switch (this) {
            case EXPOSED, WAXED_EXPOSED -> WeatheringCopper.WeatherState.EXPOSED;
            case WEATHERED, WAXED_WEATHERED -> WeatheringCopper.WeatherState.WEATHERED;
            case OXIDIZED, WAXED_OXIDIZED -> WeatheringCopper.WeatherState.OXIDIZED;
            case UNAFFECTED, WAXED -> WeatheringCopper.WeatherState.UNAFFECTED;
        };
    }

    public Boolean isWaxed() {
        return switch (this) {
            case UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED -> false;
            case WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED -> true;
        };
    }
}

package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.level.block.WeatheringCopper;

public enum WeatheringType {
    UNAFFECTED,
    EXPOSED,
    WEATHERED,
    OXIDIZED,
    WAXED,
    WAXED_EXPOSED,
    WAXED_WEATHERED,
    WAXED_OXIDIZED;

    private final String prefix;

    WeatheringType() {
        this.prefix = this.name().toLowerCase();
    }

    /**
     * Gets the prefix for this weathering type.
     * For example, for EXPOSED it will return "exposed_",
     * specially for UNAFFECTED it will return "".
     * @return the prefix for this weathering type
     */
    public String getPrefix() {
        if (this == UNAFFECTED) {
            return "";
        }
        return this.prefix + "_";
    }

    public WeatheringCopper.WeatherState getWeatherState() {
        return switch (this) {
            case EXPOSED, WAXED_EXPOSED -> WeatheringCopper.WeatherState.EXPOSED;
            case WEATHERED, WAXED_WEATHERED -> WeatheringCopper.WeatherState.WEATHERED;
            case OXIDIZED, WAXED_OXIDIZED -> WeatheringCopper.WeatherState.OXIDIZED;
            case UNAFFECTED, WAXED -> WeatheringCopper.WeatherState.UNAFFECTED;
        };
    }

    public WeatheringType getNext() {
        return switch (this) {
            case UNAFFECTED -> EXPOSED;
            case EXPOSED -> WEATHERED;
            case WEATHERED -> OXIDIZED;
            case WAXED -> WAXED_EXPOSED;
            case WAXED_EXPOSED -> WAXED_WEATHERED;
            case WAXED_WEATHERED -> WAXED_OXIDIZED;
            case OXIDIZED, WAXED_OXIDIZED -> null;
        };
    }

    public WeatheringType getPrev() {
        return switch (this) {
            case UNAFFECTED, WAXED -> null;
            case EXPOSED -> UNAFFECTED;
            case WEATHERED -> EXPOSED;
            case OXIDIZED -> WEATHERED;
            case WAXED_EXPOSED -> WAXED;
            case WAXED_WEATHERED -> WAXED_EXPOSED;
            case WAXED_OXIDIZED -> WAXED_WEATHERED;
        };
    }

    public WeatheringType getFirst() {
        return switch (this) {
            case UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED -> UNAFFECTED;
            case WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED -> WAXED;
        };
    }

    public Boolean isWaxed() {
        return switch (this) {
            case UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED -> false;
            case WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED -> true;
        };
    }

    public WeatheringType getWaxed() {
        return switch (this) {
            case UNAFFECTED -> WAXED;
            case EXPOSED -> WAXED_EXPOSED;
            case WEATHERED -> WAXED_WEATHERED;
            case OXIDIZED -> WAXED_OXIDIZED;
            case WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED -> null;
        };
    }

    public WeatheringType getUnWaxed() {
        return switch (this) {
            case WAXED -> UNAFFECTED;
            case WAXED_EXPOSED -> EXPOSED;
            case WAXED_WEATHERED -> WEATHERED;
            case WAXED_OXIDIZED -> OXIDIZED;
            case UNAFFECTED, EXPOSED, WEATHERED, OXIDIZED -> null;
        };
    }
}

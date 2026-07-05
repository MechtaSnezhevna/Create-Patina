package io.github.mechtasnezhevna.createpatina.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

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

    public String getPrefixWithoutWaxed(){
        if (this == WAXED) return "";
        return this.prefix.replace("waxed_","") + "_";
    }

    public WeatheringCopper.WeatherState getWeatherState() {
        return switch (this) {
            case EXPOSED, WAXED_EXPOSED -> WeatheringCopper.WeatherState.EXPOSED;
            case WEATHERED, WAXED_WEATHERED -> WeatheringCopper.WeatherState.WEATHERED;
            case OXIDIZED, WAXED_OXIDIZED -> WeatheringCopper.WeatherState.OXIDIZED;
            case UNAFFECTED, WAXED -> WeatheringCopper.WeatherState.UNAFFECTED;
        };
    }

    @Contract(pure = true)
    public static String getPrefixByName(String name){
        String[] prefixes = {"exposed_","weathered_","oxidized_","waxed_", "waxed_exposed_","waxed_weathered_","waxed_oxidized_"};
        for (String prefix : prefixes) {
            if (name.startsWith(prefix)) {
                return prefix;
            }
        }
        return "";
    }

    @Contract(pure = true)
    public static String getPrefixWithoutWaxedByName(String name){
        return getPrefixByName(name.replace("waxed_",""));
    }


    public static WeatheringType getFromPrefix(String name) {
        return switch (name) {
            case "exposed_" -> EXPOSED;
            case "weathered_" -> WEATHERED;
            case "oxidized_" -> OXIDIZED;
            case "waxed_" -> WAXED;
            case "waxed_exposed_" -> WAXED_EXPOSED;
            case "waxed_weathered_" -> WAXED_WEATHERED;
            case "waxed_oxidized_" -> WAXED_OXIDIZED;
            default -> UNAFFECTED;
        };
    }

    public static WeatheringType fromBlock(Block block) {
        return fromId(BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    public static WeatheringType fromBlockEntry(BlockEntry<?> entry) {
        return fromId(entry.getId().getPath());
    }

    public static WeatheringType fromId(String id) {
        if (id.startsWith("waxed_exposed_")) return WAXED_EXPOSED;
        else if (id.startsWith("waxed_weathered_")) return WAXED_WEATHERED;
        else if (id.startsWith("waxed_oxidized_")) return WAXED_OXIDIZED;
        else if (id.startsWith("exposed_")) return EXPOSED;
        else if (id.startsWith("weathered_")) return WEATHERED;
        else if (id.startsWith("oxidized_")) return OXIDIZED;
        else if (id.startsWith("waxed_")) return WAXED;
        else return UNAFFECTED;
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

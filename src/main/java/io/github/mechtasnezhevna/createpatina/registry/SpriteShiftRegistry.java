package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;

import java.util.EnumMap;
import java.util.Map;

public class SpriteShiftRegistry {

    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_COPPER_CASINGS = new EnumMap<>(WeatheringType.class);

    static {
        WEATHERING_COPPER_CASINGS.put(WeatheringType.UNAFFECTED,      AllSpriteShifts.COPPER_CASING);
        WEATHERING_COPPER_CASINGS.put(WeatheringType.WAXED,           AllSpriteShifts.COPPER_CASING);

        WEATHERING_COPPER_CASINGS.put(WeatheringType.EXPOSED,         create("exposed_copper_casing"));
        WEATHERING_COPPER_CASINGS.put(WeatheringType.WEATHERED,       create("weathered_copper_casing"));
        WEATHERING_COPPER_CASINGS.put(WeatheringType.OXIDIZED,        create("oxidized_copper_casing"));

        WEATHERING_COPPER_CASINGS.put(WeatheringType.WAXED_EXPOSED,   create("waxed_exposed_copper_casing"));
        WEATHERING_COPPER_CASINGS.put(WeatheringType.WAXED_WEATHERED, create("waxed_weathered_copper_casing"));
        WEATHERING_COPPER_CASINGS.put(WeatheringType.WAXED_OXIDIZED,  create("waxed_oxidized_copper_casing"));
    }

    private static CTSpriteShiftEntry create(String name) {
        return CTSpriteShifter.getCT(
                AllCTTypes.OMNIDIRECTIONAL,
                CreatePatina.asResource("block/" + name),
                CreatePatina.asResource("block/" + name + "_connected")
        );
    }
}
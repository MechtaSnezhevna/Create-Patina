package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;

import java.util.Map;

public class SpriteShiftRegistry {

    private static void register(Map<WeatheringType, CTSpriteShiftEntry> map, CTType ctType, String baseName, CTSpriteShiftEntry defaultEntry) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED || type == WeatheringType.WAXED) {
                map.put(type, defaultEntry);
            }
            else{
                map.put(type, create(ctType, type.getPrefixWithoutWaxed() + baseName));
            }
        }
    }

    private static CTSpriteShiftEntry create(CTType ctType, String name) {
        return CTSpriteShifter.getCT(
                ctType,
                CreatePatina.asResource("block/" + name),
                CreatePatina.asResource("block/" + name + "_connected")
        );
    }
}
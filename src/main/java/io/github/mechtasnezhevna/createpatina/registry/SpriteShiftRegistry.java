package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;

import java.util.EnumMap;
import java.util.Map;

public class SpriteShiftRegistry {

    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_COPPER_CASINGS = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_COPPER_SCAFFOLDS = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_COPPER_SCAFFOLD_INSIDES = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_FLUID_TANK = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_FLUID_TANK_TOP = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, CTSpriteShiftEntry> WEATHERING_FLUID_TANK_INNER = new EnumMap<>(WeatheringType.class);

    static {
        register(WEATHERING_COPPER_CASINGS, AllCTTypes.OMNIDIRECTIONAL, "casing/", "copper_casing", AllSpriteShifts.COPPER_CASING);
        register(WEATHERING_COPPER_SCAFFOLDS, AllCTTypes.HORIZONTAL, "scaffold/", "copper_scaffold", AllSpriteShifts.COPPER_SCAFFOLD);
        register(WEATHERING_COPPER_SCAFFOLD_INSIDES, AllCTTypes.HORIZONTAL, "scaffold/", "copper_scaffold_inside", AllSpriteShifts.COPPER_SCAFFOLD_INSIDE);
        register(WEATHERING_FLUID_TANK, AllCTTypes.RECTANGLE, "fluid_tank/", "fluid_tank", AllSpriteShifts.FLUID_TANK);
        register(WEATHERING_FLUID_TANK_TOP, AllCTTypes.RECTANGLE, "fluid_tank/", "fluid_tank_top", AllSpriteShifts.FLUID_TANK_TOP);
        register(WEATHERING_FLUID_TANK_INNER, AllCTTypes.RECTANGLE, "fluid_tank/", "fluid_tank_inner", AllSpriteShifts.FLUID_TANK_INNER);
    }

    private static void register(Map<WeatheringType, CTSpriteShiftEntry> map, CTType ctType,String dirName, String baseName, CTSpriteShiftEntry defaultEntry) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) {
                map.put(type, defaultEntry);
            }
            else{
                map.put(type, create(ctType, dirName + type.getPrefixWithoutWaxed() + baseName));
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
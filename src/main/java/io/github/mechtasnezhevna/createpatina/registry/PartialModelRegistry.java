package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class PartialModelRegistry {

    public static final Map<WeatheringType, PartialModel> WEATHERING_FLUID_PIPE_CASINGS =
            new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType,
            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>>> WEATHERING_PIPE_ATTACHMENTS =
            new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_VALVE_HANDLES = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_DOOR_LEFT = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel> WEATHERING_DOOR_RIGHT = new EnumMap<>(WeatheringType.class);

    static {

        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) // The base type should be defined in Create.
                continue;

            WEATHERING_FLUID_PIPE_CASINGS.put(type, block("fluid_pipe/" + type.getPrefix() + "fluid_pipe/casing"));

            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>> attachmentMap = new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);
            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
                Map<Direction, PartialModel> map = new HashMap<>();
                for (Direction d : Iterate.directions) {
                    String asId = Lang.asId(partial.name());
                    map.put(d, block("fluid_pipe/" + type.getPrefix() + "fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
                }
                attachmentMap.put(partial, map);
            }
            WEATHERING_PIPE_ATTACHMENTS.put(type, attachmentMap);
        }

        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) {
                continue;
            }
            WEATHERING_VALVE_HANDLES.put(type, block(type.getPrefix() + "copper_valve_handle"));
        }

        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) {
                continue;
            }
            WEATHERING_DOOR_LEFT.put(type, block("copper_door/" + type.getPrefix() + "copper_door/fold_left"));
            WEATHERING_DOOR_RIGHT.put(type, block("copper_door/" + type.getPrefix() + "copper_door/fold_right"));
        }
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreatePatina.asResource("block/" + path));
    }

    public static void init() {
        // Static initializer does the work
    }
}

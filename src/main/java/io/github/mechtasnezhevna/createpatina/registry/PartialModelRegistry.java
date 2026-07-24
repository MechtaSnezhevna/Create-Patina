package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class PartialModelRegistry {

    public static final Map<WeatheringType, PartialModel> WEATHERING_FLUID_PIPE_CASINGS = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType,
            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>>> WEATHERING_PIPE_ATTACHMENTS =
            new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_VALVE_HANDLES = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel> WEATHERING_VALVE_POINTER = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_DOOR_LEFT = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel> WEATHERING_DOOR_RIGHT = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel>
            WEATHERING_PORTABLE_FLUID_INTERFACE_TOP = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel>
            WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel>
            WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE_POWERED = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_SPOUT_BOTTOM = new EnumMap<>(WeatheringType.class);

    public static final Map<WeatheringType, PartialModel> WEATHERING_PULLEY_MAGNET = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, PartialModel> WEATHERING_HALF_MAGNET = new EnumMap<>(WeatheringType.class);

    static {
        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) // The base type should be defined in Create.
                continue;
            String prefix = type.getPrefix();

            WEATHERING_FLUID_PIPE_CASINGS.put(type, block("fluid_pipe/" + prefix + "fluid_pipe/casing"));
            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>> attachmentMap = new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);
            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
                Map<Direction, PartialModel> map = new HashMap<>();
                for (Direction d : Iterate.directions) {
                    String asId = Lang.asId(partial.name());
                    map.put(d, block("fluid_pipe/" + prefix + "fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
                }
                attachmentMap.put(partial, map);
            }
            WEATHERING_PIPE_ATTACHMENTS.put(type, attachmentMap);
            WEATHERING_VALVE_HANDLES.put(type, block("copper_valve_handle/" + prefix + "copper_valve_handle"));
            WEATHERING_VALVE_POINTER.put(type, block("fluid_valve/" + prefix + "fluid_valve_pointer"));
            WEATHERING_DOOR_LEFT.put(type, block("copper_door/" + prefix + "copper_door/fold_left"));
            WEATHERING_DOOR_RIGHT.put(type, block("copper_door/" + prefix + "copper_door/fold_right"));
            WEATHERING_PORTABLE_FLUID_INTERFACE_TOP.put(type, block("portable_fluid_interface/" + prefix + "portable_fluid_interface/block_top"));
            WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE.put(type, block("portable_fluid_interface/" + prefix + "portable_fluid_interface/block_middle"));
            WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE_POWERED.put(type, block("portable_fluid_interface/" + prefix + "portable_fluid_interface/block_middle_powered"));
            WEATHERING_SPOUT_BOTTOM.put(type, block("spout/" + prefix + "spout/bottom"));
            WEATHERING_PULLEY_MAGNET.put(type, block("hose_pulley/" + prefix + "hose_pulley/pulley_magnet"));
            WEATHERING_HALF_MAGNET.put(type, block("hose_pulley/" + prefix + "hose_pulley/rope_half_magnet"));
        }
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreatePatina.asResource("block/" + path));
    }

    public static void init() {
        // Static initializer does the work
    }
}

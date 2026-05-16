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

    public static final Map<WeatheringType,
            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>>> WEATHERING_PIPE_ATTACHMENTS =
            new EnumMap<>(WeatheringType.class);

    static {

        for (WeatheringType type : WeatheringType.values()) {
            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials,
                    Map<Direction, PartialModel>> attachmentMap = new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);
            String typeId = Lang.asId(type.name());
            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
                Map<Direction, PartialModel> map = new HashMap<>();
                for (Direction d : Iterate.directions) {
                    String asId = Lang.asId(partial.name());
                    map.put(d, block(typeId + "_fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
                }
                attachmentMap.put(partial, map);
            }
            WEATHERING_PIPE_ATTACHMENTS.put(type, attachmentMap);
        }
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreatePatina.asResource("block/" + path));
    }

    public static void init() {
        // Static initializer does the work
    }
}

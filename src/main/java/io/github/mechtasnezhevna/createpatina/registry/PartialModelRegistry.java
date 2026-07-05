package io.github.mechtasnezhevna.createpatina.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.CreatePatina;


public class PartialModelRegistry {

    private static PartialModel block(String path) {
        return PartialModel.of(CreatePatina.asResource("block/" + path));
    }

    public static void init() {
        // Static initializer does the work
    }
}

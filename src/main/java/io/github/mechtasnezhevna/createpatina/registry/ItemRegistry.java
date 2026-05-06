package io.github.mechtasnezhevna.createpatina.registry;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = CreatePatina.ITEMS;

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

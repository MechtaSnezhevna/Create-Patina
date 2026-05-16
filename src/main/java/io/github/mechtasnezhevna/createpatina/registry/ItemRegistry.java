package io.github.mechtasnezhevna.createpatina.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class ItemRegistry
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

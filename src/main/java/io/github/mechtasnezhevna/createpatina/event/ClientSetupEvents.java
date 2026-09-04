package io.github.mechtasnezhevna.createpatina.event;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

import io.github.mechtasnezhevna.createpatina.ponder.PatinaPonderPlugin;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientSetupEvents {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PatinaPonderPlugin());
    }
}

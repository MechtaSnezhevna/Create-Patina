package io.github.mechtasnezhevna.createpatina;

import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = CreatePatina.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class CreatePatinaClient {
    private CreatePatinaClient() {
    }

    @SubscribeEvent
    public static void clientInit(final FMLClientSetupEvent event) {
        PartialModelRegistry.init();
    }
}

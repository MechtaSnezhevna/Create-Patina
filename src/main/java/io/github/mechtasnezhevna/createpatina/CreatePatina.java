package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.mechtasnezhevna.createpatina.event.CommonEvents;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.registry.CreativeModeTabRegistry;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(CreatePatina.MODID)
public class CreatePatina {

    public static final String MODID = "createpatina";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreatePatina(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        REGISTRATE.registerEventListeners(modEventBus);

        ItemRegistry.register(modEventBus);
        BlockEntityRegistry.register();
        BlockRegistry.register();
        CreativeModeTabRegistry.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onUseHoneycomb);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onUseAxe);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Patina common setup");
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}

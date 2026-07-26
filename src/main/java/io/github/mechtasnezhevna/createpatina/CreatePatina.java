package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.mechtasnezhevna.createpatina.event.CommonEvents;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.network.PatinaClockActionPayload;
import io.github.mechtasnezhevna.createpatina.registry.*;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.PatinaDataGen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(CreatePatina.MODID)
public class CreatePatina {

    public static final String MODID = "createpatina";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.defaultCreativeTab(CreativeModeTabRegistry.CREATEPATINA_TAB, "createpatina_tab");
    }

    public CreatePatina(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.SERVER, PatinaConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        CreativeModeTabRegistry.register(modEventBus);

        ItemRegistry.register(modEventBus);
        BlockEntityRegistry.register();
        BlockRegistry.register();

        modEventBus.addListener(CreativeModeTabRegistry::editPatinaTab);
        modEventBus.addListener(EventPriority.LOWEST, PatinaDataGen::gatherData);
        modEventBus.addListener(PatinaClockActionPayload::register);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onUseHoneycomb);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onUseAxe);
        NeoForge.EVENT_BUS.addListener(PatinaClockItem::suppressImmediateServerInteraction);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Patina common setup");
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

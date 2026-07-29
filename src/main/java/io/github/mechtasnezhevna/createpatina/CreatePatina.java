package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.mechtasnezhevna.createpatina.event.CommonEvents;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.network.PatinaClockActionPayload;
import io.github.mechtasnezhevna.createpatina.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

    public CreatePatina() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PatinaConfig.SPEC);
        REGISTRATE.registerEventListeners(modEventBus);
        CreativeModeTabRegistry.register(modEventBus);

        BlockEntityRegistry.register();
        BlockRegistry.register();
        ItemRegistry.register();

        modEventBus.addListener(CreativeModeTabRegistry::editPatinaTab);
        PatinaClockActionPayload.register();
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onUseHoneycomb);
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onUseAxe);
        MinecraftForge.EVENT_BUS.addListener(PatinaClockItem::suppressImmediateServerInteraction);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Patina common setup");
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}

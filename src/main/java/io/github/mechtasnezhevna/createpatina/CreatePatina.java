package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.client.ConfigScreenRegistration;
import io.github.mechtasnezhevna.createpatina.network.PatinaClockActionPayload;
import io.github.mechtasnezhevna.createpatina.registry.*;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.PatinaDataGen;
import io.github.mechtasnezhevna.createpatina.registry.util.DefaultPatinaSets;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(CreatePatina.MODID)
public class CreatePatina {

    public static final String MODID = "createpatina";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.defaultCreativeTab(CreativeModeTabRegistry.CREATEPATINA_TAB, "createpatina_tab");
        REGISTRATE.setTooltipModifierFactory(item ->
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CreatePatina() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PatinaConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PatinaConfig.CLIENT_SPEC);
        if (FMLEnvironment.dist.isClient()) {
            ConfigScreenRegistration.register();
        }
        REGISTRATE.registerEventListeners(modEventBus);
        CreativeModeTabRegistry.register(modEventBus);

        ItemRegistry.register(modEventBus);
        PatinaRecipeTypes.register(modEventBus);
        PatinaFanProcessingTypes.register(modEventBus);
        BlockEntityRegistry.register();
        BlockRegistry.register();
        DefaultPatinaSets.register();

        modEventBus.addListener(CreativeModeTabRegistry::editPatinaTab);
        modEventBus.addListener(PatinaDataGen::gatherData);
        PatinaClockActionPayload.register();
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

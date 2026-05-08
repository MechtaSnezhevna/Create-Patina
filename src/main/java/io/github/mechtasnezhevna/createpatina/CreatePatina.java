package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.mechtasnezhevna.createpatina.event.CommonEvents;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(CreatePatina.MODID)
public class CreatePatina {

    public static final String MODID = "createpatina";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreatePatina(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        REGISTRATE.registerEventListeners(modEventBus);

        BlockEntityRegistry.register();
        BlockRegistry.register();
        CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onUseHoneycomb);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Patina common setup");
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}

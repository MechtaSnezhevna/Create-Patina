package io.github.mechtasnezhevna.createpatina.client;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Adds the mod's configuration screen to the Mods list.
 * Client-only: never loaded on a dedicated server, see CreatePatina#CreatePatina.
 */
public final class ConfigScreenRegistration {

    private ConfigScreenRegistration() {
    }

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> new BaseConfigScreen(parent, CreatePatina.MODID)));
    }
}

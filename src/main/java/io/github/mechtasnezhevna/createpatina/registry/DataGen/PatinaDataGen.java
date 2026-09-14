package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class PatinaDataGen {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new PatinaWeatheringItemTagProvider(packOutput, lookupProvider));
        PatinaRecipeProvider.registerAllProcessing(
                generator, packOutput, lookupProvider, event.includeServer()
        );
    }
}

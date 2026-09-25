package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import io.github.mechtasnezhevna.createpatina.registry.PatinaTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class PatinaDataGen {

    public static void gatherDataHighPriority(GatherDataEvent event) {
        PatinaTagRegistry.addGenerators();
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        PatinaRecipeProvider.registerAllProcessing(
                generator, packOutput, lookupProvider, event.includeServer()
        );
    }
}

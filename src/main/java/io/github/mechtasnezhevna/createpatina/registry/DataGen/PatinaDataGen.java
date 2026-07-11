package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaDataMapProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class PatinaDataGen {

    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(CreatePatina.MODID))
            return;

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new PatinaDataMapProvider(packOutput, lookupProvider));
    }
}

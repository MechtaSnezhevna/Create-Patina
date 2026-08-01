package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe.PatinaDeployingRecipeGen;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe.PatinaFillingRecipeGen;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe.PatinaHoneyingRecipeGen;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe.PatinaWashingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public final class PatinaRecipeProvider {

    private PatinaRecipeProvider() {
    }

    public static void registerAllProcessing(DataGenerator generator, PackOutput output,
                                             CompletableFuture<HolderLookup.Provider> registries,
                                             boolean includeServer) {
        generator.addProvider(includeServer, new PatinaDeployingRecipeGen(output, registries));
        generator.addProvider(includeServer, new PatinaFillingRecipeGen(output, registries));
        generator.addProvider(includeServer, new PatinaHoneyingRecipeGen(output, registries));
        generator.addProvider(includeServer, new PatinaWashingRecipeGen(output, registries));
    }
}

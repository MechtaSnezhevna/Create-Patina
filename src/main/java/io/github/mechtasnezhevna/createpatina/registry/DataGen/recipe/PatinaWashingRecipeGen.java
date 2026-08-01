package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PatinaWashingRecipeGen extends WashingRecipeGen {

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaWashingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(this::addOxidizingRecipes);
    }

    private void addOxidizingRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed() || type == WeatheringType.OXIDIZED) {
                continue;
            }

            Block input = set.get(type);
            Block output = set.get(type.getNext());
            if (input.asItem() == Items.AIR || output.asItem() == Items.AIR) {
                continue;
            }

            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
            ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
            ResourceLocation recipeId = CreatePatina.asResource(
                    outputId.getNamespace() + "/" + inputId.getPath() + "_oxidize"
            );
            if (!generatedRecipeIds.add(recipeId)) {
                continue;
            }

            // verified: Create 6.0.10 WashingRecipeGen/ProcessingRecipeBuilder, 2026-08-01
            create(recipeId, builder -> builder
                    .require(input)
                    .output(output));
        }
    }
}

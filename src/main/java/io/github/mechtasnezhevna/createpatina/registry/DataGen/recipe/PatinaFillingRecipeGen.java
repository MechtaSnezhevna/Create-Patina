package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PatinaFillingRecipeGen extends FillingRecipeGen {

    private static final int WATER_AMOUNT = 250;
    private static final int HONEY_AMOUNT = 100;

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(set -> {
            addWaterRecipes(set);
            addHoneyRecipes(set);
        });
    }

    private void addWaterRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed() || type == WeatheringType.OXIDIZED) {
                continue;
            }

            Block input = set.get(type);
            Block output = set.get(type.getNext());
            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());

            addWaterRecipe(inputId.getPath() + "_oxidize", input, output);
        }
    }

    private void addHoneyRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                continue;
            }

            Block input = set.get(type);
            Block output = set.get(type.getWaxed());
            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());

            addHoneyRecipe(inputId.getPath() + "_wax_on", input, output);
        }
    }

    private void addWaterRecipe(String name, Block input, Block output) {
        if (!canGenerate(input, output)) {
            return;
        }

        ResourceLocation recipeId = recipeId("water", name, output);
        if (!generatedRecipeIds.add(recipeId)) {
            return;
        }

        // verified: Create 6.0.10 FillingRecipeGen/ProcessingRecipeBuilder, 2026-08-01
        create(recipeId, builder -> builder
                .require(Fluids.WATER, WATER_AMOUNT)
                .require(input)
                .output(output));
    }

    private void addHoneyRecipe(String name, Block input, Block output) {
        if (!canGenerate(input, output)) {
            return;
        }

        ResourceLocation recipeId = recipeId("honey", name, output);
        if (!generatedRecipeIds.add(recipeId)) {
            return;
        }

        // verified: Create 6.0.10 CreateFillingRecipeGen uses NeoForge's honey fluid tag, 2026-08-01
        create(recipeId, builder -> builder
                .require(Tags.Fluids.HONEY, HONEY_AMOUNT)
                .require(input)
                .output(output));
    }

    private boolean canGenerate(Block input, Block output) {
        return input.asItem() != Items.AIR && output.asItem() != Items.AIR;
    }

    private ResourceLocation recipeId(String fluid, String name, Block output) {
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
        return CreatePatina.asResource(fluid + "/" + outputId.getNamespace() + "/" + name);
    }
}

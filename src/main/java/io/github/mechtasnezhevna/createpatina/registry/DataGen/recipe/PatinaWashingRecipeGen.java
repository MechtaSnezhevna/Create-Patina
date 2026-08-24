package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PatinaWashingRecipeGen extends WashingRecipeGen {

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaWashingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(this::addOxidizingRecipes);
        addArmorBacktankRecipes();
    }

    private void addOxidizingRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed() || type == WeatheringType.OXIDIZED) {
                continue;
            }

            Block input = set.get(type);
            Block output = set.get(type.getNext());
            addWashingRecipe(input, output);
        }
    }

    private void addArmorBacktankRecipes() {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed() || type == WeatheringType.OXIDIZED) {
                continue;
            }

            ItemLike input = backtankItem(type);
            ItemLike output = backtankItem(type.getNext());
            addWashingRecipe(input, output);
        }
    }

    private void addWashingRecipe(ItemLike input, ItemLike output) {
        if (input.asItem() == Items.AIR || output.asItem() == Items.AIR) {
            return;
        }

        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
        if (inputId.getPath().endsWith("_placeable")) {
            return;
        }
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
        ResourceLocation recipeId = CreatePatina.asResource(
                outputId.getNamespace() + "/" + inputId.getPath() + "_oxidize"
        );
        if (!generatedRecipeIds.add(recipeId)) {
            return;
        }

        // verified: Create 6.0.10 WashingRecipeGen/ProcessingRecipeBuilder, 2026-08-01
        create(recipeId, builder -> builder
                .require(input)
                .output(output));
    }

    private static ItemLike backtankItem(WeatheringType type) {
        return type == WeatheringType.UNAFFECTED
                ? AllItems.COPPER_BACKTANK.get()
                : ItemRegistry.ARMOR_BACKTANKS.get(type).asItem();
    }
}
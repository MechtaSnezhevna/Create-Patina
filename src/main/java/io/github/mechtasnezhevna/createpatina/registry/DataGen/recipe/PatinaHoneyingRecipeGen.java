package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import io.github.mechtasnezhevna.createpatina.recipe.HoneyingRecipe;
import io.github.mechtasnezhevna.createpatina.registry.PatinaRecipeTypes;
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

public final class PatinaHoneyingRecipeGen extends StandardProcessingRecipeGen<HoneyingRecipe> {

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaHoneyingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(this::addHoneyingRecipes);
        addArmorBacktankRecipes();
    }

    private void addHoneyingRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                continue;
            }

            Block input = set.get(type);
            Block output = set.get(type.getWaxed());
            if (input.asItem() == Items.AIR || output.asItem() == Items.AIR) {
                continue;
            }

            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
            if (inputId.getPath().endsWith("_placeable")) {
                continue;
            }
            ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
            ResourceLocation recipeId = CreatePatina.asResource(
                    outputId.getNamespace() + "/" + inputId.getPath() + "_wax_on"
            );
            if (!generatedRecipeIds.add(recipeId)) {
                continue;
            }

            // verified: Create 6.0.10 StandardProcessingRecipeGen/ProcessingRecipeBuilder, 2026-08-01
            create(recipeId, builder -> builder
                    .require(input)
                    .output(output));
        }
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return PatinaRecipeTypes.HONEYING;
    }

    private void addArmorBacktankRecipes() {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                continue;
            }

            ItemLike input = backtankItem(type);
            ItemLike output = backtankItem(type.getWaxed());
            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
            ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
            ResourceLocation recipeId = CreatePatina.asResource(
                    outputId.getNamespace() + "/" + inputId.getPath() + "_wax_on"
            );
            if (!generatedRecipeIds.add(recipeId)) {
                continue;
            }

            // verified: Create 6.0.10 StandardProcessingRecipeGen/ProcessingRecipeBuilder, 2026-08-01
            create(recipeId, builder -> builder
                    .require(input)
                    .output(output));
        }
    }

    private static ItemLike backtankItem(WeatheringType type) {
        return type == WeatheringType.UNAFFECTED
                ? AllItems.COPPER_BACKTANK.get()
                : ItemRegistry.ARMOR_BACKTANKS.get(type).asItem();
    }
}

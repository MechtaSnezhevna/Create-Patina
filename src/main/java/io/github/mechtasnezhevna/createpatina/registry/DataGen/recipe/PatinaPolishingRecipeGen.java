package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.PolishingRecipeGen;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PatinaPolishingRecipeGen extends PolishingRecipeGen {

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaPolishingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(this::addPolishingRecipes);
        addArmorBacktankRecipes();
    }

    private void addPolishingRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                addPolishingRecipe(set, type, type.getUnWaxed(), "_wax_off");
            } else if (type != WeatheringType.UNAFFECTED) {
                addPolishingRecipe(set, type, type.getPrev(), "_scrape");
            }
        }
    }

    private void addPolishingRecipe(PatinaSet set, WeatheringType inputType, WeatheringType outputType, String suffix) {
        if (outputType == null) {
            return;
        }

        Block input = set.get(inputType);
        Block output = set.get(outputType);
        if (input.asItem() == Items.AIR || output.asItem() == Items.AIR) {
            return;
        }

        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
        if (inputId.getPath().endsWith("_placeable")) {
            return;
        }
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
        ResourceLocation recipeId = CreatePatina.asResource(
                outputId.getNamespace() + "/" + inputId.getPath() + suffix
        );
        if (!generatedRecipeIds.add(recipeId)) {
            return;
        }

        // verified: Create 6.0.10 PolishingRecipeGen/SandPaperPolishingRecipe, 2026-08-16
        create(recipeId, builder -> builder
                .require(input)
                .output(output));
    }

    private void addArmorBacktankRecipes() {
        for (WeatheringType type : WeatheringType.values()) {
            if (type == WeatheringType.UNAFFECTED) {
                continue;
            }

            WeatheringType outputType = type.isWaxed() ? type.getUnWaxed() : type.getPrev();
            if (outputType == null) {
                continue;
            }

            Item input = ItemRegistry.ARMOR_BACKTANKS.get(type).asItem();
            Item output = outputType == WeatheringType.UNAFFECTED
                    ? AllItems.COPPER_BACKTANK.get()
                    : ItemRegistry.ARMOR_BACKTANKS.get(outputType).asItem();
            String suffix = type.isWaxed() ? "_wax_off" : "_scrape";

            ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input);
            ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output);
            ResourceLocation recipeId = CreatePatina.asResource(
                    outputId.getNamespace() + "/" + inputId.getPath() + suffix
            );
            if (!generatedRecipeIds.add(recipeId)) {
                continue;
            }

            // verified: Create 6.0.10 PolishingRecipeGen/SandPaperPolishingRecipe, 2026-08-16
            create(recipeId, builder -> builder
                    .require(input)
                    .output(output));
        }
    }
}

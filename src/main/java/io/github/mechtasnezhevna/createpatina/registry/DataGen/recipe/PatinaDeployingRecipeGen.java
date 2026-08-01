package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.DefaultPatinaSets;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PatinaDeployingRecipeGen extends DeployingRecipeGen {

    private final Set<ResourceLocation> generatedRecipeIds = new HashSet<>();

    public PatinaDeployingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
        PatinaSet.all().forEach(set -> {
            // verified: Create 6.0.10 CreateDeployingRecipeGen already covers every DefaultPatinaSets entry, 2026-08-01
            boolean createAlreadyCovers = DefaultPatinaSets.contains(set);
            addWaxingRecipes(set, !createAlreadyCovers);
            if (!createAlreadyCovers) {
                addScrapingRecipes(set);
            }
            addSandpaperRecipes(set);
        });
    }

    private void addWaxingRecipes(PatinaSet set, boolean includeNonHoneycombRecipes) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                continue;
            }

            WeatheringType waxedType = type.getWaxed();
            Block nonWaxedBlock = set.get(type);
            Block waxedBlock = set.get(waxedType);
            ResourceLocation nonWaxedId = BuiltInRegistries.ITEM.getKey(nonWaxedBlock.asItem());

            addDeployingRecipe(
                    "waxing", nonWaxedId.getPath() + "_wax_on",
                    nonWaxedBlock, Ingredient.of(Items.HONEYCOMB), waxedBlock, false
            );
            if (includeNonHoneycombRecipes) {
                addDeployingRecipe(
                        "waxing_block", nonWaxedId.getPath() + "_wax_on",
                        nonWaxedBlock, Ingredient.of(Items.HONEYCOMB_BLOCK), waxedBlock, true
                );
                addDeployingRecipe(
                        "waxing", nonWaxedId.getPath() + "_wax_off",
                        waxedBlock, Ingredient.of(ItemTags.AXES), nonWaxedBlock, false
                );
            }
        }
    }

    private void addScrapingRecipes(PatinaSet set) {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed() || type == WeatheringType.UNAFFECTED) {
                continue;
            }

            Block oxidizedBlock = set.get(type);
            Block previousBlock = set.get(type.getPrev());
            ResourceLocation oxidizedId = BuiltInRegistries.ITEM.getKey(oxidizedBlock.asItem());

            addDeployingRecipe(
                    "scraping", oxidizedId.getPath() + "_scrape",
                    oxidizedBlock, Ingredient.of(ItemTags.AXES), previousBlock, false
            );
        }
    }

    private void addSandpaperRecipes(PatinaSet set) {
        Ingredient sandpaper = Ingredient.of(AllItemTags.SANDPAPER.tag);

        for (WeatheringType type : WeatheringType.values()) {
            if (type.isWaxed()) {
                continue;
            }

            Block nonWaxedBlock = set.get(type);
            Block waxedBlock = set.get(type.getWaxed());
            ResourceLocation nonWaxedId = BuiltInRegistries.ITEM.getKey(nonWaxedBlock.asItem());

            addDeployingRecipe(
                    "sanding", nonWaxedId.getPath() + "_wax_off",
                    waxedBlock, sandpaper, nonWaxedBlock, false
            );

            if (type != WeatheringType.UNAFFECTED) {
                Block previousBlock = set.get(type.getPrev());

                addDeployingRecipe(
                        "sanding", nonWaxedId.getPath() + "_scrape",
                        nonWaxedBlock, sandpaper, previousBlock, false
                );
            }
        }
    }

    private void addDeployingRecipe(
            String operation, String name, Block input, Ingredient heldItem, Block output, boolean keepHeldItem
    ) {
        if (input.asItem() == Items.AIR || output.asItem() == Items.AIR) {
            return;
        }

        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.asItem());
        ResourceLocation recipeId = CreatePatina.asResource(
                operation + "/" + outputId.getNamespace() + "/" + name
        );
        if (!generatedRecipeIds.add(recipeId)) {
            return;
        }

        // verified: Create 6.0.10 DeployingRecipeGen/BeltDeployerCallbacks, 2026-08-01
        create(recipeId, builder -> {
            builder.require(input)
                    .require(heldItem)
                    .output(output);
            if (keepHeldItem) {
                builder.toolNotConsumed();
            }
            return builder;
        });
    }
}

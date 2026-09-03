package io.github.mechtasnezhevna.createpatina.registry.DataGen.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

/**
 * Survival crafting for the Patina Clock: a Deployer sequentially installs a
 * Precision Mechanism, Copper Sheet and Honeycomb onto a Clock.
 */
public final class PatinaSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {

    GeneratedRecipe PATINA_CLOCK = create("patina_clock", b -> b
            .require(Items.CLOCK)
            .transitionTo(ItemRegistry.INCOMPLETE_PATINA_CLOCK)
            .addOutput(ItemRegistry.PATINA_CLOCK, 1)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new,
                    rb -> rb.require(Ingredient.of(AllItems.PRECISION_MECHANISM.get())))
            .addStep(DeployerApplicationRecipe::new,
                    rb -> rb.require(Ingredient.of(AllItems.COPPER_SHEET.get())))
            .addStep(DeployerApplicationRecipe::new,
                    rb -> rb.require(Ingredient.of(Items.HONEYCOMB))));

    public PatinaSequencedAssemblyRecipeGen(PackOutput output,
                                            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreatePatina.MODID);
    }
}

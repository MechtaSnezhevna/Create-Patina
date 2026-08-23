package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Belt deployers apply polishing recipes through RecipeApplier#applyRecipeOn,
 * which rolls fresh output stacks and drops the backtank stored air.
 */
@Mixin(RecipeApplier.class)
public abstract class RecipeApplierMixin {

    @Inject(
            method = "applyRecipeOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/Recipe;Z)Ljava/util/List;",
            at = @At("TAIL")
    )
    private static void createpatina$preserveBacktankAir(
            Level level, ItemStack stackIn, Recipe<?> recipe, boolean returnProcessingRemainder,
            CallbackInfoReturnable<List<ItemStack>> cir
    ) {
        if (!(recipe instanceof SandPaperPolishingRecipe) || !stackIn.has(AllDataComponents.BACKTANK_AIR)) {
            return;
        }
        int air = stackIn.get(AllDataComponents.BACKTANK_AIR);
        for (ItemStack stack : cir.getReturnValue()) {
            if (!stack.isEmpty()) {
                stack.set(AllDataComponents.BACKTANK_AIR, air);
            }
        }
    }
}

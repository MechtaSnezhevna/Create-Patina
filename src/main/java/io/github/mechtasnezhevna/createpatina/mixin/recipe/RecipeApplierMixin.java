package io.github.mechtasnezhevna.createpatina.mixin.recipe;

import com.simibubi.create.foundation.recipe.RecipeApplier;
import io.github.mechtasnezhevna.createpatina.util.BacktankAir;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Belt/depot deployers and fan processing apply recipes through
 * RecipeApplier#applyRecipeOn, which rolls fresh output stacks and drops the
 * backtank stored air NBT tag.
 */
@Mixin(value = RecipeApplier.class, remap = false)
public abstract class RecipeApplierMixin {

    @Inject(
            method = "applyRecipeOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/Recipe;Z)Ljava/util/List;",
            at = @At("TAIL")
    )
    private static void createpatina$preserveBacktankAir(
            Level level, ItemStack stackIn, Recipe<?> recipe, boolean returnProcessingRemainder,
            CallbackInfoReturnable<List<ItemStack>> cir
    ) {
        if (!BacktankAir.has(stackIn)) {
            return;
        }
        float air = BacktankAir.get(stackIn);
        for (ItemStack stack : cir.getReturnValue()) {
            if (!stack.isEmpty()) {
                BacktankAir.set(stack, air);
            }
        }
    }
}

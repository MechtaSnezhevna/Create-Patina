package io.github.mechtasnezhevna.createpatina.mixin.recipe;

import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import io.github.mechtasnezhevna.createpatina.util.BacktankAir;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * SandPaperPolishingRecipe assembles a fresh output stack, dropping item data
 * such as the backtank stored air NBT tag. Restore it from the input so
 * polishing a tank by hand or with a deployer keeps its remaining air.
 */
@Mixin(value = SandPaperPolishingRecipe.class, remap = false)
public abstract class SandPaperPolishingRecipeMixin {

    @Inject(
            method = "applyPolish(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN")
    )
    private static void createpatina$preserveBacktankAir(
            Level level, Vec3 position, ItemStack input, ItemStack sandPaperStack,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!BacktankAir.has(input)) {
            return;
        }
        ItemStack result = cir.getReturnValue();
        if (!result.isEmpty()) {
            BacktankAir.set(result, BacktankAir.get(input));
        }
    }
}

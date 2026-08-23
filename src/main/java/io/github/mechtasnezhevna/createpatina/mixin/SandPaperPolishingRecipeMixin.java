package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * SandPaperPolishingRecipe assembles a fresh output stack, dropping item data
 * such as the backtank stored air. Restore BACKTANK_AIR from the input so
 * polishing a tank by hand or with a deployer keeps its remaining air.
 */
@Mixin(SandPaperPolishingRecipe.class)
public abstract class SandPaperPolishingRecipeMixin {

    @Inject(
            method = "applyPolish(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN")
    )
    private static void createpatina$preserveBacktankAir(
            Level level, Vec3 position, ItemStack input, ItemStack sandPaperStack,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!input.has(AllDataComponents.BACKTANK_AIR)) {
            return;
        }
        ItemStack result = cir.getReturnValue();
        if (!result.isEmpty()) {
            result.set(AllDataComponents.BACKTANK_AIR, input.get(AllDataComponents.BACKTANK_AIR));
        }
    }
}

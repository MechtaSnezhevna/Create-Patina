package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The spout rolls a fresh output stack for filling recipes, dropping the
 * backtank stored air. Restore BACKTANK_AIR from the input so water/honey
 * filling keeps the tank remaining air.
 */
@Mixin(FillingBySpout.class)
public abstract class FillingBySpoutMixin {

    @Inject(
            method = "fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/neoforged/neoforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN")
    )
    private static void createpatina$preserveBacktankAir(
            Level level, int requiredAmount, ItemStack input, FluidStack availableFluid,
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

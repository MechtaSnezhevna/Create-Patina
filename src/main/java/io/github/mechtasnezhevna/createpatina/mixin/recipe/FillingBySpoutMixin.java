package io.github.mechtasnezhevna.createpatina.mixin.recipe;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import io.github.mechtasnezhevna.createpatina.util.BacktankAir;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The spout rolls a fresh output stack for filling recipes, dropping the
 * backtank stored air. fillItem consumes (shrinks) the input stack before it
 * returns, so the air is captured at entry and copied onto the rolled output
 * instead of being read back from the consumed input. Restores the backtank
 * air NBT tag so water/honey filling keeps the tank remaining air.
 */
@Mixin(value = FillingBySpout.class, remap = false)
public abstract class FillingBySpoutMixin {

    @Unique
    private static final ThreadLocal<Float> CAPTURED_BACKTANK_AIR = new ThreadLocal<>();

    @Inject(
            method = "fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD")
    )
    private static void createpatina$captureBacktankAir(
            Level level, int requiredAmount, ItemStack input, FluidStack availableFluid,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        CAPTURED_BACKTANK_AIR.remove();
        if (BacktankAir.has(input)) {
            CAPTURED_BACKTANK_AIR.set(BacktankAir.get(input));
        }
    }

    @Inject(
            method = "fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN")
    )
    private static void createpatina$restoreBacktankAir(
            Level level, int requiredAmount, ItemStack input, FluidStack availableFluid,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        Float air = CAPTURED_BACKTANK_AIR.get();
        if (air == null) {
            return;
        }
        CAPTURED_BACKTANK_AIR.remove();
        ItemStack result = cir.getReturnValue();
        if (!result.isEmpty()) {
            BacktankAir.set(result, air);
        }
    }
}

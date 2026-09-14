package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import io.github.mechtasnezhevna.createpatina.util.BacktankAir;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Safety net for the spout delivery point: whatever path the filling recipe
 * took, the item handed back to the depot/belt carries the input tank's
 * remaining air. The air must be captured before fillItem runs, because a
 * matching filling recipe consumes (shrinks) the input stack before the
 * output is returned.
 */
@Mixin(value = SpoutBlockEntity.class, remap = false)
public abstract class SpoutBlockEntityMixin {

    @Redirect(
            method = "whenItemHeld(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;)Lcom/simibubi/create/content/kinetics/belt/behaviour/BeltProcessingBehaviour$ProcessingResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/fluids/spout/FillingBySpout;fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack createpatina$fillItemPreservingBacktankAir(
            Level level, int requiredAmount, ItemStack input, FluidStack availableFluid
    ) {
        float air = BacktankAir.has(input)
                ? BacktankAir.get(input)
                : -1;
        ItemStack result = FillingBySpout.fillItem(level, requiredAmount, input, availableFluid);
        if (air >= 0 && !result.isEmpty()) {
            BacktankAir.set(result, air);
        }
        return result;
    }
}

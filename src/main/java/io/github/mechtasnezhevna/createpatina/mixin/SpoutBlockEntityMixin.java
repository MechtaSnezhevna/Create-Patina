package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
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
@Mixin(SpoutBlockEntity.class)
public abstract class SpoutBlockEntityMixin {

    @Redirect(
            method = "whenItemHeld(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;)Lcom/simibubi/create/content/kinetics/belt/behaviour/BeltProcessingBehaviour$ProcessingResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/fluids/spout/FillingBySpout;fillItem(Lnet/minecraft/world/level/Level;ILnet/minecraft/world/item/ItemStack;Lnet/neoforged/neoforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack createpatina$fillItemPreservingBacktankAir(
            Level level, int requiredAmount, ItemStack input, FluidStack availableFluid
    ) {
        int air = input.has(AllDataComponents.BACKTANK_AIR)
                ? input.get(AllDataComponents.BACKTANK_AIR)
                : -1;
        ItemStack result = FillingBySpout.fillItem(level, requiredAmount, input, availableFluid);
        if (air >= 0 && !result.isEmpty()) {
            result.set(AllDataComponents.BACKTANK_AIR, air);
        }
        return result;
    }
}

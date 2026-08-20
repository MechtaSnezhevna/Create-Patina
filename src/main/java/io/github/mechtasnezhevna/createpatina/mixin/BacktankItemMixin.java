package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemComponent;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * BacktankItem inherits the equip-on-right-click behaviour from ArmorItem#use.
 * When the other hand holds sandpaper and the held tank can be polished, hand
 * the tank over to the sandpaper's polishing flow instead of equipping it.
 */
@Mixin(ArmorItem.class)
public abstract class BacktankItemMixin {

    @Inject(
            method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void createpatina$polishWithSandpaperInsteadOfEquipping(
            Level level, Player player, InteractionHand hand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        if (!((Object) this instanceof BacktankItem)) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() || !SandPaperPolishingRecipe.canPolish(level, stack)) {
            return;
        }

        InteractionHand sandpaperHand = hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack sandpaper = player.getItemInHand(sandpaperHand);
        if (!sandpaper.is(AllItemTags.SANDPAPER.tag)) {
            return;
        }

        // Mirror SandPaperItem#use: park one tank on the sandpaper and start polishing.
        ItemStack toPolish = stack.split(1);
        sandpaper.set(AllDataComponents.SAND_PAPER_POLISHING, new SandPaperItemComponent(toPolish));
        player.startUsingItem(sandpaperHand);
        cir.setReturnValue(InteractionResultHolder.sidedSuccess(stack, level.isClientSide));
    }
}

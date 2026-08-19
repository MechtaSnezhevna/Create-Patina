package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Create's ItemDrainBlock#useItemOn consumes right-clicks with every non-BlockItem stack,
 * returning SUCCESS before AxeItem/SandPaperItem#useOn or HoneycombItem#useOn can run.
 * Let scraping, wax-off and waxing tools take over when the clicked drain can actually
 * be de-oxidized, de-waxed or waxed.
 */
@Mixin(ItemDrainBlock.class)
public abstract class ItemDrainToolInteractionMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void createpatina$allowScrapeWaxOffAndWax(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult,
            CallbackInfoReturnable<ItemInteractionResult> cir
    ) {
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() instanceof HoneycombItem) {
            if (HoneycombItem.getWaxed(state).isPresent()) {
                cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
            }
            return;
        }
        if (!stack.canPerformAction(ItemAbilities.AXE_SCRAPE)
                && !stack.canPerformAction(ItemAbilities.AXE_WAX_OFF)) {
            return;
        }

        UseOnContext context = new UseOnContext(player, hand, hitResult);
        boolean canScrape = state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, true) != null;
        boolean canWaxOff = !canScrape
                && state.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, true) != null;
        if (canScrape || canWaxOff) {
            cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
    }
}



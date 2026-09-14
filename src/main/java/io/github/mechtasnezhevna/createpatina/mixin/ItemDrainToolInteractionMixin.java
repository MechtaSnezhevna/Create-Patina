package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Create's ItemDrainBlock#use consumes right-clicks with every non-BlockItem stack,
 * returning SUCCESS before AxeItem/SandPaperItem#useOn or HoneycombItem#useOn can run.
 * Let scraping, wax-off and waxing tools take over when the clicked drain can actually
 * be de-oxidized, de-waxed or waxed.
 */
@Mixin(ItemDrainBlock.class)
public abstract class ItemDrainToolInteractionMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void createpatina$allowScrapeWaxOffAndWax(
            BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return;
        }
        if (stack.getItem() instanceof HoneycombItem) {
            if (HoneycombItem.getWaxed(state).isPresent()) {
                cir.setReturnValue(InteractionResult.PASS);
            }
            return;
        }
        if (!stack.canPerformAction(ToolActions.AXE_SCRAPE)
                && !stack.canPerformAction(ToolActions.AXE_WAX_OFF)) {
            return;
        }

        UseOnContext context = new UseOnContext(player, hand, hitResult);
        boolean canScrape = state.getToolModifiedState(context, ToolActions.AXE_SCRAPE, true) != null;
        boolean canWaxOff = !canScrape
                && state.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, true) != null;
        if (canScrape || canWaxOff) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}

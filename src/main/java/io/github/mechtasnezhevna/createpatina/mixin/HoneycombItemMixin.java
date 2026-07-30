package io.github.mechtasnezhevna.createpatina.mixin;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WaxUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HoneycombItem.class)
public abstract class HoneycombItemMixin {

    @Inject(
            method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void createpatina$waxPatinaBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof PatinaBlock)) return;

        Optional<BlockState> waxedState = WaxUtil.getWaxed(state);
        if (waxedState.isEmpty()) return;

        if (!level.isClientSide) {
            OxidizeUtil.replaceWithState(state, waxedState.get(), level, pos);

            Player player = context.getPlayer();
            ItemStack stack = context.getItemInHand();
            if (player instanceof ServerPlayer sp) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
            }

            level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_WAX_ON, pos, 0);
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player != null && !player.isCreative()) {
            stack.shrink(1);
        }

        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
    }
}

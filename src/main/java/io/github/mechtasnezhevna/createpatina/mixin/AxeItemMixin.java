package io.github.mechtasnezhevna.createpatina.mixin;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WaxUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.DataMapHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {

    @Inject(
            method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void createpatina$scrapePatinaBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        if (player != null
                && context.getHand().equals(InteractionHand.MAIN_HAND)
                && player.getOffhandItem().is(Items.SHIELD)
                && !player.isSecondaryUseActive()) {
            return;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof PatinaBlock)) return;

        Optional<BlockState> unwaxedState = WaxUtil.getUnwaxed(state);
        if (unwaxedState.isPresent()) {
            if (!level.isClientSide) {
                OxidizeUtil.replaceWithState(state, unwaxedState.get(), level, pos);
                level.levelEvent(LevelEvent.PARTICLES_WAX_OFF, pos, 0);
                level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                triggerCriteriaAndDamage(context);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }

        BlockState previousState = Optional
                .ofNullable(DataMapHooks.getPreviousOxidizedStage(state.getBlock()))
                .map(block -> block.withPropertiesOf(state))
                .orElse(null);

        if (previousState != null) {
            if (!level.isClientSide) {
                OxidizeUtil.replaceWithState(state, previousState, level, pos);
                level.levelEvent(LevelEvent.PARTICLES_SCRAPE, pos, 0);
                level.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                triggerCriteriaAndDamage(context);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }

    private static void triggerCriteriaAndDamage(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player instanceof ServerPlayer sp) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
        }
        if (player != null) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
        }
    }
}

package io.github.mechtasnezhevna.createpatina.event;

import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WaxUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

import java.util.Optional;

public class CommonEvents {

    /**
     * 处理涂蜡
     */
    @SubscribeEvent
    public static void onUseHoneycomb(UseItemOnBlockEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.HONEYCOMB)) return;

        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        Optional<BlockState> waxedState = WaxUtil.getWaxed(state);
        if (waxedState.isEmpty()) return;

        if (!level.isClientSide) {

            OxidizeUtil.replaceWithState(state, waxedState.get(), level, pos);

            if (event.getPlayer() instanceof ServerPlayer sp) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
            }

            // 播放特效
            level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_WAX_ON, pos, 0);
        }

        if (event.getPlayer() != null && !event.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        event.setCanceled(true);
        event.setCancellationResult(ItemInteractionResult.SUCCESS);
    }

    /**
     * 处理斧头脱蜡和除锈
     */
    @SubscribeEvent
    public static void onUseAxe(UseItemOnBlockEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.canPerformAction(ItemAbilities.AXE_SCRAPE)) return;

        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        // 优先尝试脱蜡
        Optional<BlockState> unwaxedState = WaxUtil.getUnwaxed(state);

        if (unwaxedState.isPresent()) {
            if (!level.isClientSide) {
                OxidizeUtil.replaceWithState(state, unwaxedState.get(), level, pos);
                level.levelEvent(LevelEvent.PARTICLES_WAX_OFF, pos, 0);
                level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                triggerCriteriaAndDamage(event);
            }
            event.setCanceled(true);
            event.setCancellationResult(ItemInteractionResult.SUCCESS);
            return;
        }

        // 尝试除锈
        Optional<BlockState> previousStage = WeatheringCopper.getPrevious(state);

        if (previousStage.isPresent()) {
            if (!level.isClientSide) {
                OxidizeUtil.replaceWithState(state, previousStage.get(), level, pos);
                level.levelEvent(LevelEvent.PARTICLES_SCRAPE, pos, 0);
                level.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                triggerCriteriaAndDamage(event);
            }
            event.setCanceled(true);
            event.setCancellationResult(ItemInteractionResult.SUCCESS);
        }
    }

    private static void triggerCriteriaAndDamage(UseItemOnBlockEvent event) {
        if (event.getPlayer() instanceof ServerPlayer sp) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, event.getPos(), event.getItemStack());
        }
        if (event.getPlayer() != null) {
            ItemStack stack = event.getItemStack();
            stack.hurtAndBreak(1, event.getPlayer(), event.getPlayer().getEquipmentSlotForItem(stack));
        }
    }
}

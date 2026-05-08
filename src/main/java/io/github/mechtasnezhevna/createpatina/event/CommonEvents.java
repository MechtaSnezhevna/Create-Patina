package io.github.mechtasnezhevna.createpatina.event;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

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

        Waxable waxData = state.getBlockHolder().getData(NeoForgeDataMaps.WAXABLES);
        if (waxData == null) return;

        if (!(state.getBlock() instanceof ItemDrainBlock)) return;

        if (!level.isClientSide) {
            OxidizeUtil.replaceWithState(state, waxData.waxed().defaultBlockState(), level, pos);

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
}

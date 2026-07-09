package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.block.WeatheringWhistleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WhistleExtenderBlock.class, remap = false)
public class WhistleExtenderBlockMixin {

    @Redirect(
            method = "canSurvive",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHasCanSurvive(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return state.getBlock() == AllBlocks.STEAM_WHISTLE.get()
                    || state.getBlock() instanceof WeatheringWhistleBlock;
        }
        return entry.has(state);
    }

    @Redirect(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            remap = false
    )
    private boolean redirectIsIn(BlockEntry<?> entry, ItemStack stack) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            Block block = Block.byItem(stack.getItem());
            return block == AllBlocks.STEAM_WHISTLE.get()
                    || block instanceof WeatheringWhistleBlock;
        }
        return entry.isIn(stack);
    }

    @Redirect(
            method = "hidesNeighborFace",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHidesNeighborFace(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return state.getBlock() == AllBlocks.STEAM_WHISTLE.get()
                    || state.getBlock() instanceof WeatheringWhistleBlock;
        }
        return entry.has(state);
    }

    /**
     * @author Create Patina
     * @reason match extender with its bottom block
     */
    @Overwrite
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockPos rootPos = WhistleExtenderBlock.findRoot((LevelAccessor) level, pos);
        BlockState rootState = level.getBlockState(rootPos);
        if (rootState.getBlock() instanceof WhistleExtenderBlock || rootState.isAir()) {
            return AllBlocks.STEAM_WHISTLE.asStack();
        }
        return rootState.getCloneItemStack(target, level, pos, player);
    }
}
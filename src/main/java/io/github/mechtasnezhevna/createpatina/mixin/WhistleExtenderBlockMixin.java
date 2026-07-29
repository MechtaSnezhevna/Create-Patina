package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
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
            return BlockRegistry.STEAM_WHISTLE_SET.has(state);
        }
        return entry.has(state);
    }

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            remap = false
    )
    private boolean redirectIsIn(BlockEntry<?> entry, ItemStack stack) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return BlockRegistry.STEAM_WHISTLE_SET.isIn(stack);
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
            return BlockRegistry.STEAM_WHISTLE_SET.has(state);
        }
        return entry.has(state);
    }

    @ModifyReturnValue(method = "getCloneItemStack", at = @At("RETURN"))
    private ItemStack createpatina$matchCloneStackWithRoot(
            ItemStack original, BlockState state, HitResult target,
            LevelReader level, BlockPos pos, Player player
    ) {
        BlockPos rootPos = WhistleExtenderBlock.findRoot((LevelAccessor) level, pos);
        BlockState rootState = level.getBlockState(rootPos);
        if (rootState.getBlock() instanceof WhistleExtenderBlock || rootState.isAir()) {
            return original;
        }
        return rootState.getCloneItemStack(target, level, pos, player);
    }
}

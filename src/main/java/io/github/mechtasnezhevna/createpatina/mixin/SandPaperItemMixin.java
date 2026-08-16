package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SandPaperItem.class)
public abstract class SandPaperItemMixin {

    @WrapOperation(
            method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private boolean createpatina$preserveBlockEntity(
            Level level, BlockPos pos, BlockState newState, Operation<Boolean> original
    ) {
        BlockState oldState = level.getBlockState(pos);
        if (oldState.getBlock() instanceof PatinaBlock
                || newState.getBlock() instanceof PatinaBlock) {
            OxidizeUtil.replaceWithState(oldState, newState, level, pos);
            return true;
        }
        return original.call(level, pos, newState);
    }
}

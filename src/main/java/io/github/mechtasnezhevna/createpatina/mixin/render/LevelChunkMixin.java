package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {

    /**
     * On the client, replacing a block with a different block unconditionally destroys the
     * block entity and recreates it empty. For swaps between patina variants (oxidation,
     * waxing, lightning cleaning) this resets client-only render state such as the fluid
     * level's LerpedFloat, so contained fluids visibly flicker on every stage change.
     * When the existing block entity is valid for the new patina variant, keep it; the
     * reuse path in setBlockState then only updates its block state.
     * ordinal = 0: the first removeBlockEntity call site is the client-side branch.
     */
    @WrapOperation(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunk;removeBlockEntity(Lnet/minecraft/core/BlockPos;)V",
                    ordinal = 0
            )
    )
    private void createpatina$keepBlockEntityOnPatinaSwap(
            LevelChunk chunk, BlockPos pos, Operation<Void> original,
            @Local(argsOnly = true) BlockState newState
    ) {
        if (newState.getBlock() instanceof PatinaBlock) {
            BlockEntity blockEntity = chunk.getBlockEntity(pos);
            if (blockEntity != null && blockEntity.isValidBlockState(newState)) {
                return;
            }
        }
        original.call(chunk, pos);
    }
}

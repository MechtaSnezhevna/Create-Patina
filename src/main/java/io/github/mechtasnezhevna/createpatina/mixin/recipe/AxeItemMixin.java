package io.github.mechtasnezhevna.createpatina.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {

    @WrapOperation(
            method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            )
    )
    private boolean createpatina$preserveBlockEntity(
            Level level, BlockPos pos, BlockState newState, int flags, Operation<Boolean> original
    ) {
        BlockState oldState = level.getBlockState(pos);
        if (oldState.getBlock() instanceof PatinaBlock
                || newState.getBlock() instanceof PatinaBlock) {
            OxidizeUtil.applyToolWeathering(oldState, newState, level, pos,
                    PatinaConfig.CONFIG.WEATHER_WHOLE_FLUID_TANK_WITH_TOOLS.get());
            return true;
        }
        return original.call(level, pos, newState, flags);
    }
}

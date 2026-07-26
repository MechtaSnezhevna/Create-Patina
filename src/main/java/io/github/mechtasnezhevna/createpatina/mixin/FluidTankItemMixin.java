package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidTankItem.class)
public abstract class FluidTankItemMixin {

    @WrapOperation(
            method = "tryMultiPlace",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/api/connectivity/ConnectivityHandler;partAt(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"
            )
    )
    private BlockEntity createPatina$findMatchingTank(
            BlockEntityType<?> originalType, BlockGetter level, BlockPos pos, Operation<BlockEntity> original
    ) {
        BlockState placedOnState = level.getBlockState(pos);
        Block placedOnBlock = placedOnState.getBlock();
        Block currentBlock = ((BlockItem) (Object) this).getBlock();
        if (currentBlock != placedOnBlock) {
            return null;
        }

        BlockEntityType<?> actualType = currentBlock instanceof FluidTankBlock tank
                ? tank.getBlockEntityType()
                : originalType;
        return original.call(actualType, level, pos);
    }
}

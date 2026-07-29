package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = ConnectivityHandler.class, remap = false)
public abstract class ConnectivityHandlerMixin {

    /*
     * Original Create code from ConnectivityHandler#tryToFormNewMultiOfWidth:
     * Optional<T> part = cache.getOrCache(type, level, pos);
     * if (part.isEmpty())
     *     break Search;
     */
    @ModifyExpressionValue(
            method = "tryToFormNewMultiOfWidth",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/api/connectivity/ConnectivityHandler$SearchCache;"
                            + "getOrCache(Lnet/minecraft/world/level/block/entity/BlockEntityType;"
                            + "Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)"
                            + "Ljava/util/Optional;"
            )
    )
    private static Optional<?> createPatina$separateWeatheringTankStates(
            Optional<?> original,
            @Local(argsOnly = true, ordinal = 0) BlockEntity source,
            @Local(ordinal = 1) BlockPos candidatePos
    ) {
        if (original.isEmpty()) {
            return original;
        }

        Level level = source.getLevel();
        if (level == null) {
            return original;
        }

        Block sourceBlock = source.getBlockState().getBlock();
        Block candidateBlock = level.getBlockState(candidatePos).getBlock();
        if (sourceBlock instanceof FluidTankBlock
                && candidateBlock instanceof FluidTankBlock
                && WeatheringType.fromBlock(sourceBlock) != WeatheringType.fromBlock(candidateBlock)) {
            return Optional.empty();
        }

        return original;
    }
}

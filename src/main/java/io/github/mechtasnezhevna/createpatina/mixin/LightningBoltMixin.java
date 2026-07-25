package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @WrapOperation(
            method = "clearCopperOnLightningStrike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private static boolean createpatina$redirectSetBlockAndUpdate(
            Level level, BlockPos pos, BlockState state,
            Operation<Boolean> original, @Local(ordinal = 1) BlockState oldState
    ) {
        Block block = state.getBlock();
        if (block instanceof WeatheringCopper) {
            OxidizeUtil.replaceWithState(oldState, WeatheringCopper.getFirst(oldState), level, pos);
            return true;
        }
        return level.setBlockAndUpdate(pos, state);
    }

    @WrapOperation(
            method = "randomStepCleaningCopper",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
            )
    )
    private static void createpatina$redirectSetBlockAndUpdateRandomStep(
            Optional<BlockState> optional, Consumer<BlockState> consumer,
            Operation<Void> original, @Local(ordinal = 0) BlockState oldState,
            @Local(ordinal = 1) BlockPos pos, @Local(ordinal = 0, argsOnly = true) Level level
    ) {
        optional.ifPresent(state -> {
            Block block = state.getBlock();
            if (block instanceof WeatheringCopper) {
                OxidizeUtil.replaceWithState(oldState, state, level, pos);
            } else {
                consumer.accept(state);
            }
        });
    }
}

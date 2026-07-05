package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.holder.HolderEnum;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChangeOverTimeBlock.class)
public interface ChangeOverTimeBlockMixin {
    @WrapOperation(
            method = "getNextState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/ChangeOverTimeBlock;getAge()Ljava/lang/Enum;"
            )
    )
    private static Enum<?> skipWaxedNeighbors(ChangeOverTimeBlock<?> instance, Operation<Enum<?>> original) {
        if (instance instanceof PatinaBlock patina && !patina.isWeatheringEnabled()) {
            return HolderEnum.NONE;
        }
        return original.call(instance);
    }
}

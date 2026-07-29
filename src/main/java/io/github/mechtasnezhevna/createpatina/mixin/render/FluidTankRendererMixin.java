package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidTankRenderer.class, remap = false)
public class FluidTankRendererMixin {

    @WrapOperation(
            method = "renderAsBoiler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/render/CachedBuffers;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/createmod/catnip/render/SuperByteBuffer;",
                    ordinal = 0
            )
    )
    public SuperByteBuffer renderAsBoiler$replaceBoilerGauge(PartialModel partial,
                                          BlockState referenceState,
                                          Operation<SuperByteBuffer> original) {
        if (referenceState.getBlock() == AllBlocks.FLUID_TANK.get()) {
            return original.call(partial, referenceState);
        }
        WeatheringType type = WeatheringType.fromBlock(referenceState.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return original.call(PartialModelRegistry.WEATHERING_BOILER_GAUGE.get(type), referenceState);
        }
        return original.call(partial, referenceState);
    }

    @WrapOperation(
            method = "renderAsBoiler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/render/CachedBuffers;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/createmod/catnip/render/SuperByteBuffer;",
                    ordinal = 1
            )
    )
    public SuperByteBuffer renderAsBoiler$replaceBoilerGaugeDial(PartialModel partial,
                                                             BlockState referenceState,
                                                             Operation<SuperByteBuffer> original) {
        if (referenceState.getBlock() == AllBlocks.FLUID_TANK.get()) {
            return original.call(partial, referenceState);
        }
        WeatheringType type = WeatheringType.fromBlock(referenceState.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return original.call(PartialModelRegistry.WEATHERING_BOILER_GAUGE_DIAL.get(type), referenceState);
        }
        return original.call(partial, referenceState);
    }

}

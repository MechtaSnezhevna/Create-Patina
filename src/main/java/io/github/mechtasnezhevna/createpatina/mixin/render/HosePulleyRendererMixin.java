package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HosePulleyRenderer.class)
public abstract class HosePulleyRendererMixin {

    @WrapOperation(
            method = "renderMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/render/CachedBuffers;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/createmod/catnip/render/SuperByteBuffer;"
            )
    )
    private SuperByteBuffer wrapPartial(PartialModel model, BlockState state, Operation<SuperByteBuffer> original) {
        if (model == AllPartialModels.HOSE_MAGNET) {
            WeatheringType type = WeatheringType.fromBlock(state.getBlock());
            if (type != WeatheringType.UNAFFECTED) {
                model = PartialModelRegistry.WEATHERING_PULLEY_MAGNET.get(type);
            }
        }
        return original.call(model, state);
    }
}
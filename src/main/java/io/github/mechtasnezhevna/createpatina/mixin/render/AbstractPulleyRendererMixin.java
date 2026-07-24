package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.contraptions.pulley.AbstractPulleyRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractPulleyRenderer.class)
public abstract class AbstractPulleyRendererMixin<T extends KineticBlockEntity> {

    @ModifyExpressionValue(
            method = "renderSafe",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/contraptions/pulley/AbstractPulleyRenderer;halfMagnet:Ldev/engine_room/flywheel/lib/model/baked/PartialModel;",
                    ordinal = 0
            )
    )
    private PartialModel createpatina$modifyHalfMagnet(PartialModel original, T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        WeatheringType type = WeatheringType.fromBlock(blockState.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return PartialModelRegistry.WEATHERING_HALF_MAGNET.get(type);
        }
        return original;
    }
}

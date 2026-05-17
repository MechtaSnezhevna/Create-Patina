package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PipeAttachmentModel.class)
public class PipeAttachmentModelMixin {

    @WrapOperation(
            method = "getRenderTypes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderTypes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;)Lnet/neoforged/neoforge/client/ChunkRenderTypeSet;",
                    ordinal = 0
            )
    )
    private ChunkRenderTypeSet wrapGetRenderTypes_casing(
            BakedModel instance,
            BlockState blockState,
            RandomSource rand,
            ModelData data,
            Operation<ChunkRenderTypeSet> original
    ) {
        if (blockState.getBlock() instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED) {
            return PartialModelRegistry.WEATHERING_FLUID_PIPE_CASINGS.get(patina.getType())
                    .get().getRenderTypes(blockState, rand, data);
        } else {
            return original.call(instance, blockState, rand, data);
        }
    }

    @WrapOperation(
            method = "getRenderTypes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getRenderTypes(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;)Lnet/neoforged/neoforge/client/ChunkRenderTypeSet;",
                    ordinal = 1
            )
    )
    private ChunkRenderTypeSet wrapGetRenderTypes_normal(
            BakedModel instance,
            BlockState blockState,
            RandomSource rand,
            ModelData data,
            Operation<ChunkRenderTypeSet> original,
            @Local(name = "partial") FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial,
            @Local(name = "d") Direction d
    ) {
        if (blockState.getBlock() instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED) {
            return PartialModelRegistry.WEATHERING_PIPE_ATTACHMENTS.get(patina.getType())
                    .get(partial).get(d).get().getRenderTypes(blockState, rand, data);
        } else {
            return original.call(instance, blockState, rand, data);
        }
    }

    @WrapOperation(
            method = "addQuads",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/engine_room/flywheel/lib/model/baked/PartialModel;get()Lnet/minecraft/client/resources/model/BakedModel;",
                    ordinal = 0
            )
    )
    private BakedModel wrapAddQuads_normal(
            PartialModel instance,
            Operation<BakedModel> original,
            @Local(name = "partial") FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial,
            @Local(name = "d") Direction d,
            @Local(name = "state") BlockState blockState
    ) {
        if (blockState.getBlock() instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED) {
            return PartialModelRegistry.WEATHERING_PIPE_ATTACHMENTS.get(patina.getType())
                    .get(partial).get(d).get();
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(
            method = "addQuads",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/engine_room/flywheel/lib/model/baked/PartialModel;get()Lnet/minecraft/client/resources/model/BakedModel;",
                    ordinal = 1
            )
    )
    private BakedModel wrapAddQuads_casing(
            PartialModel instance,
            Operation<BakedModel> original,
            @Local(name = "state") BlockState blockState
    ) {
        if (blockState.getBlock() instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED) {
            return PartialModelRegistry.WEATHERING_FLUID_PIPE_CASINGS.get(patina.getType()).get();
        } else {
            return original.call(instance);
        }
    }

}

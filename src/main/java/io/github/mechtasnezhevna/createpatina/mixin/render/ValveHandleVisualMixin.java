package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleVisual;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ValveHandleVisual.class, remap = false)
public class ValveHandleVisualMixin {

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/engine_room/flywheel/lib/model/Models;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;)Ldev/engine_room/flywheel/api/model/Model;"
            )
    )
    private Model wrapModelsPartial(
            PartialModel originalPartial,
            Operation<Model> original,
            @Local(argsOnly = true) HandCrankBlockEntity blockEntity
    ) {
        BlockState state = blockEntity.getBlockState();
        DyeColor color = null;
        if (state != null && state.getBlock() instanceof ValveHandleBlock vhb) {
            color = vhb.color;
        }

        if (color != null) {
            return original.call(originalPartial);
        }

        if (state != null && state.getBlock() instanceof PatinaBlock patina) {
            WeatheringType type = patina.getType();
            if (type != WeatheringType.UNAFFECTED) {
                PartialModel customPartial = PartialModelRegistry.WEATHERING_VALVE_HANDLES.get(type);
                if (customPartial != null) {
                    return original.call(customPartial);
                }
            }
        }

        return original.call(originalPartial);
    }
}
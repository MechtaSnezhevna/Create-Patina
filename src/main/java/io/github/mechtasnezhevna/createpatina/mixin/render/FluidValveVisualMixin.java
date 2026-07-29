package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveVisual;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidValveVisual.class, remap = false)
public abstract class FluidValveVisualMixin {


    @WrapOperation(method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/engine_room/flywheel/lib/model/Models;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;)Ldev/engine_room/flywheel/api/model/Model;"
            )
    )
    private Model redirectPartial(PartialModel partial, Operation<Model> original,
                                  @Local(name = "blockEntity") FluidValveBlockEntity entity) {
        BlockState state = entity.getBlockState();
        WeatheringType type = WeatheringType.fromBlock(state.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return Models.partial(PartialModelRegistry.WEATHERING_VALVE_POINTER.get(type));
        }
        return original.call(partial);
    }
}

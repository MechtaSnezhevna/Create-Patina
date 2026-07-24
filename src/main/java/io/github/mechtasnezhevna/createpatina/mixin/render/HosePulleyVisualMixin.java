package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.pulley.HosePulleyVisual;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.AbstractBlockEntityVisualAccessor;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HosePulleyVisual.class)
public abstract class HosePulleyVisualMixin {

    @WrapOperation(method = "getMagnetModel", at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/lib/model/Models;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;)Ldev/engine_room/flywheel/api/model/Model;"))
    private Model redirectMagnetPartial(PartialModel partial, Operation<Model> original) {
        BlockState state = ((AbstractBlockEntityVisualAccessor) this).getBlockState();
        WeatheringType type = WeatheringType.fromBlock(state.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return Models.partial(PartialModelRegistry.WEATHERING_PULLEY_MAGNET.get(type));
        }
        return original.call(partial);
    }

    @WrapOperation(method = "getHalfMagnetModel", at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/lib/model/Models;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;)Ldev/engine_room/flywheel/api/model/Model;"))
    private Model redirectHalfMagnetPartial(PartialModel partial, Operation<Model> original) {
        BlockState state = ((AbstractBlockEntityVisualAccessor) this).getBlockState();
        WeatheringType type = WeatheringType.fromBlock(state.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            return Models.partial(PartialModelRegistry.WEATHERING_HALF_MAGNET.get(type));
        }
        return original.call(partial);
    }
}

package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(SlidingDoorRenderer.class)
public class SlidingDoorRendererMixin {

    @Redirect(
            method = "renderSafe",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private Object getCustomDoorPartial(Map<ResourceLocation, Couple<PartialModel>> map, Object key) {
        ResourceLocation loc = (ResourceLocation) key;
        Block block = BuiltInRegistries.BLOCK.get(loc);

        if (block instanceof PatinaBlock patina) {
            WeatheringType type = patina.getType();
            if (type != WeatheringType.UNAFFECTED) {
                PartialModel left = PartialModelRegistry.WEATHERING_DOOR_LEFT.get(type);
                PartialModel right = PartialModelRegistry.WEATHERING_DOOR_RIGHT.get(type);

                if (left != null && right != null) {
                    return Couple.create(left, right);
                }
            }
        }
        return map.get(key);
    }
}
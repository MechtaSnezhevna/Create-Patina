package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.fluids.tank.FluidTankCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import io.github.mechtasnezhevna.createpatina.registry.SpriteShiftRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidTankCTBehaviour.class)
public class FluidTankCTBehaviourMixin {
    @Inject(method = "getShift", at = @At("HEAD"), cancellable = true)
    private void createpatina$getShiftHead(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite, CallbackInfoReturnable<CTSpriteShiftEntry> cir) {
        WeatheringType type = WeatheringType.fromBlock(state.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            CTSpriteShiftEntry innerShift = SpriteShiftRegistry.WEATHERING_FLUID_TANK_INNER.get(type);
            if (sprite != null && direction.getAxis() == Direction.Axis.Y && innerShift.getOriginal() == sprite) {
                cir.setReturnValue(innerShift);
            }
        }
    }

    @ModifyReturnValue(method = "getShift", at = @At("RETURN"))
    private CTSpriteShiftEntry createpatina$getShift(CTSpriteShiftEntry original, BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        WeatheringType type = WeatheringType.fromBlock(state.getBlock());
        if (type != WeatheringType.UNAFFECTED) {
            if (original == AllSpriteShifts.FLUID_TANK)
                return SpriteShiftRegistry.WEATHERING_FLUID_TANK.get(type);
            if (original == AllSpriteShifts.FLUID_TANK_TOP)
                return SpriteShiftRegistry.WEATHERING_FLUID_TANK_TOP.get(type);
            if (original == AllSpriteShifts.FLUID_TANK_INNER)
                return SpriteShiftRegistry.WEATHERING_FLUID_TANK_INNER.get(type);
        }
        return original;
    }
}
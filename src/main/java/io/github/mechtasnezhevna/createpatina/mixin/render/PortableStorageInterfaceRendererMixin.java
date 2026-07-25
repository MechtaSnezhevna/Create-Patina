package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPortableStorageInterfaceBlock;
import io.github.mechtasnezhevna.createpatina.registry.PartialModelRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PortableStorageInterfaceRenderer.class)
public class PortableStorageInterfaceRendererMixin {

    @ModifyReturnValue(method = "getMiddleForState", at = @At("RETURN"))
    private static PartialModel createpatina$getMiddleForState(
            PartialModel original, BlockState state, boolean lit
    ) {
        Block block = state.getBlock();
        if (block instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED && isFluidInterface(state)) {
            return lit
                    ? PartialModelRegistry.WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE_POWERED.get(patina.getType())
                    : PartialModelRegistry.WEATHERING_PORTABLE_FLUID_INTERFACE_MIDDLE.get(patina.getType());
        }
        return original;
    }

    @ModifyReturnValue(method = "getTopForState", at = @At("RETURN"))
    private static PartialModel createpatina$getTopForState(PartialModel original, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof PatinaBlock patina && patina.getType() != WeatheringType.UNAFFECTED && isFluidInterface(state)) {
            return PartialModelRegistry.WEATHERING_PORTABLE_FLUID_INTERFACE_TOP.get(patina.getType());
        }
        return original;
    }

    private static boolean isFluidInterface(BlockState state) {
        Block block = state.getBlock();
        if (block == AllBlocks.PORTABLE_FLUID_INTERFACE.get()) {
            return true;
        }
        if (block instanceof WeatheringPortableStorageInterfaceBlock wpb) {
            return wpb.getPortableStorageInterfaceBlock() == AllBlocks.PORTABLE_FLUID_INTERFACE.get();
        }
        return false;
    }
}

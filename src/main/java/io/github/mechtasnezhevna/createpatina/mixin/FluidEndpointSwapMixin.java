package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.FluidPropagator;
import io.github.mechtasnezhevna.createpatina.util.FluidEndpointSwap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidPropagator.class)
public abstract class FluidEndpointSwapMixin {
    /*
     * Original Create code from FluidPropagator#validateNeighbourChange:
     * for (Direction d : Iterate.directions) {
     *     if (!pos.relative(d)
     *         .equals(neighborPos))
     *         continue;
     *     return d;
     * }
     * return null;
     *
     * A verified equivalent endpoint swap does not change pipe topology. Suppress
     * only its recorded neighbor notifications, before they schedule a pipe tick.
     */
    @Inject(method = "validateNeighbourChange", at = @At("HEAD"), cancellable = true)
    private static void createpatina$preserveEndpointConnection(
            BlockState state, Level world, BlockPos pos, Block otherBlock,
            BlockPos neighborPos, boolean isMoving, CallbackInfoReturnable<Direction> cir) {
        if (FluidEndpointSwap.suppressesNeighbor(world, pos, neighborPos)) {
            cir.setReturnValue(null);
        }
    }
}

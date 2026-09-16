package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Create 1.20.1 recognizes its own pumps and encased pipes by registry identity
 * ({@code AllBlocks.MECHANICAL_PUMP.has(state)} and {@code AllBlocks.ENCASED_FLUID_PIPE.has(state)}).
 * Patina's weathering variants are separate blocks, so both tests reject them and the affected
 * parts silently drop out of Create's fluid propagation.
 *
 * <p>Create 1.21.1 replaced the two identity tests with {@code instanceof} checks. These redirects
 * restore the same behaviour on the 1.20.1 code base, so weathering variants take part in the
 * fluid network exactly like the blocks they were derived from.</p>
 */
@Mixin(value = FluidPropagator.class, remap = false)
public abstract class FluidPropagatorMixin {

    /*
     * Original Create 1.20.1 code from FluidPropagator#propagateChangedPipe:
     * if (!AllBlocks.MECHANICAL_PUMP.has(targetState) || targetState.getValue(PumpBlock.FACING)
     *     .getAxis() != direction.getAxis())
     *     continue;
     * discoveredPumps.add(Pair.of((PumpBlockEntity) blockEntity, direction.getOpposite()));
     *
     * Pumps are only rediscovered through this branch. When it rejects a weathering pump
     * variant, the network it belongs to is wiped and never receives pressure again.
     */
    @Redirect(
            method = "propagateChangedPipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private static boolean createpatina$recognizeMechanicalPumpVariants(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.MECHANICAL_PUMP) {
            return state.getBlock() instanceof PumpBlock;
        }
        return entry.has(state);
    }

    /*
     * Original Create 1.20.1 code from FluidPropagator#validateNeighbourChange:
     * if (getStraightPipeAxis(state) == null && !AllBlocks.ENCASED_FLUID_PIPE.has(state))
     *     return null;
     *
     * Returning null makes EncasedPipeBlock#neighborChanged discard the update, so a weathering
     * encased pipe would never schedule the tick that lets Create rebuild its network pressure.
     */
    @Redirect(
            method = "validateNeighbourChange",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private static boolean createpatina$recognizeEncasedPipeVariants(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.ENCASED_FLUID_PIPE) {
            return state.getBlock() instanceof EncasedPipeBlock;
        }
        return entry.has(state);
    }
}

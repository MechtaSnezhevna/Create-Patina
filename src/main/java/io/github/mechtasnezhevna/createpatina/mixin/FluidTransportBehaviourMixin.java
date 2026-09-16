package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Create 1.20.1 picks the model a pipe shows towards its neighbours with registry identity
 * checks. Pipes may only display the drain attachment when the block they face is an actual
 * fluid handler, and Create carves its own hose pulley out of that rule. A weathering hose
 * pulley is a different block, so the exception never applied to it.
 */
@Mixin(value = FluidTransportBehaviour.class, remap = false)
public abstract class FluidTransportBehaviourMixin {

    /*
     * Original Create 1.20.1 code from FluidTransportBehaviour#getRenderedRimAttachment:
     * if (FluidPropagator.hasFluidCapability(world, offsetPos, direction.getOpposite())
     *     && !AllBlocks.HOSE_PULLEY.has(facingState))
     *     return AttachmentTypes.DRAIN;
     *
     * HosePulleyBlockEntity exposes its IFluidHandler on the side the pull rope hangs from, so
     * without this a pipe attached to a weathering hose pulley rendered a drain instead of the
     * rim its unweathered counterpart shows.
     *
     * The neighbouring AllBlocks.ENCASED_FLUID_PIPE.has(facingState) check of the same method
     * is deliberately passed through unchanged: encased pipe BlockEntities expose no fluid
     * handler, so both of its branches already resolve to AttachmentTypes.RIM for every
     * weathering encased pipe.
     */
    @Redirect(
            method = "getRenderedRimAttachment",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean createpatina$recognizeHosePulleyVariants(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.HOSE_PULLEY) {
            return state.getBlock() instanceof HosePulleyBlock;
        }
        return entry.has(state);
    }
}

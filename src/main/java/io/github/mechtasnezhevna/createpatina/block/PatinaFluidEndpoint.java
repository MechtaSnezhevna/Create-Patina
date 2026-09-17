package io.github.mechtasnezhevna.createpatina.block;

import io.github.mechtasnezhevna.createpatina.util.FluidEndpointSwap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A Patina block whose BlockEntity is exposed to Create fluid networks as a fluid capability.
 */
public interface PatinaFluidEndpoint extends PatinaBlock {

    @Override
    default void actionWhenReplaced(
            BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos
    ) {
        if (!FluidEndpointSwap.defersRefresh(level, pos)) {
            FluidEndpointSwap.refreshEndpointConnections(level, pos);
        }
    }
}

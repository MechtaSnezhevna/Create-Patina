package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class FluidNetworkUtil {

    private FluidNetworkUtil() {
    }

    /**
     * Discards only the neighboring connections which cached the replaced endpoint's
     * old fluid capability, then lets Create rebuild pressure for the affected side.
     */
    public static void refreshEndpointConnections(ServerLevel level, BlockPos endpointPos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighbourPos = endpointPos.relative(direction);
            FluidTransportBehaviour transport = FluidPropagator.getPipe(level, neighbourPos);
            Direction sideTowardEndpoint = direction.getOpposite();

            if (transport == null
                    || transport.interfaces == null
                    || transport.interfaces.remove(sideTowardEndpoint) == null) {
                continue;
            }

            BlockEntity neighbourBE = level.getBlockEntity(neighbourPos);
            if (neighbourBE instanceof PumpBlockEntity pump) {
                pump.updatePipesOnSide(sideTowardEndpoint);
                continue;
            }

            BlockState neighbourState = level.getBlockState(neighbourPos);
            FluidPropagator.propagateChangedPipe(level, neighbourPos, neighbourState);
        }
    }
}

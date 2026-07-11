package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConnectFuncs {



    public static void reconnect(ServerLevel level, BlockPos pos) {
        for (Direction d : Direction.values()) {
            BlockPos neighborPos = pos.relative(d);
            BlockState neighborState = level.getBlockState(neighborPos);
            FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, neighborPos);
            if (pipe != null) {
                if (pipe.interfaces != null) {
                    // Force remove the old connections
                    pipe.interfaces.remove(d.getOpposite());
                }
                FluidPropagator.propagateChangedPipe(level, neighborPos, neighborState);
                FluidPropagator.resetAffectedFluidNetworks(level, neighborPos, d.getOpposite());
            }

            BlockEntity neighborBE = level.getBlockEntity(neighborPos);
            if (neighborBE instanceof PumpBlockEntity pumpBE) {
                pumpBE.updatePressureChange();
            }
        }
    }
}

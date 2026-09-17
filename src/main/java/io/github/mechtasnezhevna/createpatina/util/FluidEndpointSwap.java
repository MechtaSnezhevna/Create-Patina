package io.github.mechtasnezhevna.createpatina.util;

import com.simibubi.create.content.fluids.FlowSource;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaFluidEndpoint;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.FlowSourceHandlerAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.FluidNetworkAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.PipeConnectionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;

import java.util.ArrayList;
import java.util.List;

/** Synchronous preservation of simple endpoint swaps; never captures a whole pipe network. */
public final class FluidEndpointSwap {
    private static final ThreadLocal<Context> ACTIVE = new ThreadLocal<>();

    private FluidEndpointSwap() {}

    /** Returns true only when the replacement completed with the original neighboring connections. */
    public static boolean replace(ServerLevel level, BlockPos pos, BlockState oldState, BlockState newState) {
        if (ACTIVE.get() != null) {
            throw new IllegalStateException("Nested fluid endpoint swaps are not supported");
        }
        if (level.getBlockState(pos) != oldState || oldState == newState) {
            return false;
        }
        Context context = capture(level, pos, oldState, newState);
        if (context == null) {
            OxidizeUtil.replaceWithStateInternal(oldState, newState, level, pos);
            return false;
        }
        boolean preserved = false;
        ACTIVE.set(context);
        try {
            OxidizeUtil.replaceWithStateInternal(oldState, newState, level, pos);
            if (level.getBlockState(pos).getBlock() != newState.getBlock()
                    || level.getBlockEntity(pos) == null
                    || level.getBlockEntity(pos).getClass() != context.entityClass()) {
                return false;
            }
            // Validate every side before changing any cached provider.
            for (Connection saved : context.connections()) {
                FluidTransportBehaviour current = FluidPropagator.getPipe(level, saved.pos());
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK,
                        pos, saved.side().getOpposite());
                if (current != saved.transport() || current.blockEntity.isRemoved()
                        || level.getBlockState(saved.pos()) != saved.state()
                        || current.phase != FluidTransportBehaviour.UpdatePhase.IDLE
                        || current.getConnection(saved.side()) != saved.connection()
                        || ((PipeConnectionAccessor) saved.connection()).createpatina$getSource().orElse(null)
                                != saved.source()
                        || level.getBlockTicks().hasScheduledTick(saved.pos(), saved.state().getBlock())
                        || !saved.matches(handler)) {
                    return false;
                }
            }
            for (Connection saved : context.connections()) {

                ((FlowSourceHandlerAccessor) saved.source()).createpatina$setHandlerCache(null);
                saved.source().manageSource(level, saved.transport().blockEntity);

                ((PipeConnectionAccessor) saved.connection()).createpatina$getNetwork()
                        .ifPresent(network -> ((FluidNetworkAccessor) network)
                                .createpatina$setSource(saved.source().provideHandler()));
            }
            preserved = true;
            return true;
        } finally {
            ACTIVE.remove();
            if (!preserved) {
                refreshEndpointConnections(level, pos);
            }
        }
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

    public static boolean defersRefresh(Level level, BlockPos endpoint) {
        Context context = ACTIVE.get();
        return context != null && context.level() == level && context.pos().equals(endpoint);
    }

    public static boolean suppressesNeighbor(Level level, BlockPos pipe, BlockPos endpoint) {
        Context context = ACTIVE.get();
        return defersRefresh(level, endpoint) && context.connections().stream()
                .anyMatch(saved -> saved.pos().equals(pipe));
    }

    private static Context capture(ServerLevel level, BlockPos pos, BlockState oldState, BlockState newState) {
        BlockEntity entity = level.getBlockEntity(pos);
        // Explicit allowlist: tanks, pulleys and moving interfaces require separate policies.
        if (!(entity instanceof ItemDrainBlockEntity || entity instanceof SpoutBlockEntity)
                || !(oldState.getBlock() instanceof PatinaFluidEndpoint)
                || !(newState.getBlock() instanceof PatinaFluidEndpoint)) {
            return null;
        }
        List<Connection> connections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos adjacent = pos.relative(direction);
            if (!level.isLoaded(adjacent)) return null;
            FluidTransportBehaviour transport = FluidPropagator.getPipe(level, adjacent);
            if (transport == null)
                continue;
            Direction side = direction.getOpposite();
            PipeConnection connection = transport.getConnection(side);
            if (connection == null)
                continue;
            // Do not promise continuity while an earlier update is already waiting to rebuild.
            if (transport.phase != FluidTransportBehaviour.UpdatePhase.IDLE
                    || level.getBlockTicks().hasScheduledTick(adjacent, level.getBlockState(adjacent).getBlock())) {
                return null;
            }
            FlowSource source = ((PipeConnectionAccessor) connection).createpatina$getSource().orElse(null);
            IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
            if (!(source instanceof FlowSource.FluidHandler fluidSource) || handler == null)
                return null;
            List<FluidStack> fluids = new ArrayList<>();
            List<Integer> capacities = new ArrayList<>();
            for (int tank = 0; tank < handler.getTanks(); tank++) {
                fluids.add(handler.getFluidInTank(tank).copy());
                capacities.add(handler.getTankCapacity(tank));
            }
            connections.add(new Connection(adjacent, level.getBlockState(adjacent), side,
                    transport, connection, fluidSource, fluids, capacities));
        }
        return new Context(level, pos.immutable(), entity.getClass(), connections);
    }

    private record Context(ServerLevel level, BlockPos pos, Class<?> entityClass, List<Connection> connections) {}

    private record Connection(BlockPos pos, BlockState state, Direction side, FluidTransportBehaviour transport,
                              PipeConnection connection, FlowSource.FluidHandler source,
                              List<FluidStack> fluids, List<Integer> capacities) {
        boolean matches(IFluidHandler handler) {
            if (handler == null || handler.getTanks() != fluids.size()) return false;
            for (int tank = 0; tank < fluids.size(); tank++) {
                FluidStack actual = handler.getFluidInTank(tank);
                FluidStack expected = fluids.get(tank);
                if (handler.getTankCapacity(tank) != capacities.get(tank)
                        || actual.getAmount() != expected.getAmount()
                        || (!expected.isEmpty() && !FluidStack.isSameFluidSameComponents(expected, actual))) {
                    return false;
                }
            }
            return true;
        }
    }
}

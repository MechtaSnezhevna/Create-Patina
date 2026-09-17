package io.github.mechtasnezhevna.createpatina;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.ItemDrainBlockEntityAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.PipeConnectionAccessor;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * test FluidEndpointSwap functions properly tick by tick
 */
@EventBusSubscriber(modid = "createpatina")
public final class FluidSwapTickProbe {
    private static final List<FluidSwapTickProbe> ACTIVE = new ArrayList<>();
    private final ServerLevel level;
    private final BlockPos drain, pump, target;
    final List<String> samples = new ArrayList<>();
    final List<String> failures = new ArrayList<>();
    private int step, sourceBefore, targetBefore, baseline;

    FluidSwapTickProbe(ServerLevel level, BlockPos drain, BlockPos pump, BlockPos target) {
        this.level = level;
        this.drain = drain;
        this.pump = pump;
        this.target = target;
        ACTIVE.add(this);
    }

    @SubscribeEvent
    public static void beforeLevelTick(LevelTickEvent.Pre event) {
        for (var probe : ACTIVE) {
            if (event.getLevel() != probe.level) continue;
            try {
                probe.sourceBefore = probe.sourceAmount();
                probe.targetBefore = probe.targetAmount();
                if (probe.step == 3 || probe.step == 6) probe.swap();
            } catch (RuntimeException exception) {
                probe.failures.add("Pre step " + probe.step + ": " + exception);
            }
        }
    }

    @SubscribeEvent
    public static void afterLevelTick(LevelTickEvent.Post event) {
        var iterator = ACTIVE.iterator();
        while (iterator.hasNext()) {
            var probe = iterator.next();
            if (event.getLevel() != probe.level) continue;
            try {
                int source = probe.sourceAmount();
                int target = probe.targetAmount();
                int drained = probe.sourceBefore - source;
                int filled = target - probe.targetBefore;
                probe.samples.add("step=" + probe.step + " drained=" + drained + " filled=" + filled);
                if (probe.step == 0) probe.baseline = drained;
                if (probe.baseline <= 0 || drained != probe.baseline || filled != probe.baseline
                        || source + target != 1500) {
                    probe.failures.add("Unexpected transfer: " + probe.samples.getLast()
                            + " baseline=" + probe.baseline + " total=" + (source + target));
                }
            } catch (RuntimeException exception) {
                probe.failures.add("Post step " + probe.step + ": " + exception);
            }
            if (++probe.step == 9) {
                LogUtils.getLogger().info("Fluid swap per-tick samples (swaps at 3 and 6): {}", probe.samples);
                iterator.remove();
            }
        }
    }

    private int sourceAmount() {
        return ((ItemDrainBlockEntityAccessor) level.getBlockEntity(drain)).getInternalTank()
                .getPrimaryHandler().getFluidAmount();
    }

    private int targetAmount() {
        return level.getCapability(Capabilities.FluidHandler.BLOCK, target, Direction.WEST)
                .getFluidInTank(0).getAmount();
    }

    private void swap() {
        var transport = ((PumpBlockEntity) level.getBlockEntity(pump)).getBehaviour(FluidTransportBehaviour.TYPE);
        var connection = transport.getConnection(Direction.WEST);
        var network = ((PipeConnectionAccessor) connection).createpatina$getNetwork().orElseThrow();
        var next = step == 3
                ? BlockRegistry.ITEM_DRAIN_SET.getEntry(WeatheringType.EXPOSED, ItemDrainBlock.class).getDefaultState()
                : AllBlocks.ITEM_DRAIN.getDefaultState();
        OxidizeUtil.applySelectionWeathering(level.getBlockState(drain), next, level, drain, false);
        if (transport.getConnection(Direction.WEST) != connection
                || ((PipeConnectionAccessor) connection).createpatina$getNetwork().orElse(null) != network) {
            failures.add("Connection/network changed at step " + step);
        }
    }
}

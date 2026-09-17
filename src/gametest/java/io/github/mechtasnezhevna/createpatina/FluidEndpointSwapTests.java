package io.github.mechtasnezhevna.createpatina;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.ItemDrainBlockEntityAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.accessor.PipeConnectionAccessor;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("createpatina")
@PrefixGameTestTemplate(false)
public class FluidEndpointSwapTests {
    @GameTest(template = "fluid_swap_empty", timeoutTicks = 160)
    public static void drainSwapHasNoMissingTick(GameTestHelper helper) {
        prepare(helper);
        helper.runAtTickTime(50, () -> {
            var probe = new FluidSwapTickProbe(helper.getLevel(), helper.absolutePos(new BlockPos(1, 2, 1)),
                    helper.absolutePos(new BlockPos(2, 2, 1)), helper.absolutePos(new BlockPos(3, 2, 1)));
            helper.runAtTickTime(65, () -> {
                helper.assertTrue(probe.samples.size() == 9, "Missing tick samples: " + probe.samples);
                helper.assertTrue(probe.failures.isEmpty(), "Per-tick continuity failed: " + probe.failures);
                helper.succeed();
            });
        });
    }

    @GameTest(template = "fluid_swap_empty", timeoutTicks = 160)
    public static void drainDirectlyOnPump(GameTestHelper helper) {
        run(helper, false);
    }

    @GameTest(template = "fluid_swap_empty", timeoutTicks = 160)
    public static void legacyDrainStillTransfers(GameTestHelper helper) {
        run(helper, true);
    }

    private static void prepare(GameTestHelper helper) {
        var level = helper.getLevel();
        BlockPos drain = helper.absolutePos(new BlockPos(1, 2, 1));
        helper.setBlock(1, 2, 1, AllBlocks.ITEM_DRAIN.getDefaultState());
        helper.setBlock(2, 2, 1, AllBlocks.MECHANICAL_PUMP.getDefaultState().setValue(PumpBlock.FACING, Direction.EAST));
        helper.setBlock(3, 2, 1, AllBlocks.FLUID_TANK.getDefaultState());
        helper.setBlock(2, 3, 1, AllBlocks.COGWHEEL.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        helper.setBlock(1, 3, 1, AllBlocks.CREATIVE_MOTOR.getDefaultState().setValue(CreativeMotorBlock.FACING, Direction.EAST));
        helper.runAtTickTime(5, () -> {
            ((ItemDrainBlockEntityAccessor) level.getBlockEntity(drain)).getInternalTank()
                    .getPrimaryHandler().setFluid(new FluidStack(Fluids.WATER, 1500));
        });
    }

    private static void run(GameTestHelper helper, boolean legacy) {
        prepare(helper);
        var level = helper.getLevel();
        BlockPos drain = helper.absolutePos(new BlockPos(1, 2, 1));
        BlockPos pumpPos = helper.absolutePos(new BlockPos(2, 2, 1));
        BlockPos target = helper.absolutePos(new BlockPos(3, 2, 1));
        helper.runAtTickTime(50, () -> {
            var handler = level.getCapability(Capabilities.FluidHandler.BLOCK, target, Direction.WEST);
            helper.assertTrue(handler != null && handler.getFluidInTank(0).getAmount() > 0,
                    "Pump must transfer before replacement");
            int before = handler.getFluidInTank(0).getAmount();
            int[] lastSeen = {before};
            PumpBlockEntity pump = (PumpBlockEntity) level.getBlockEntity(pumpPos);
            var transport = pump.getBehaviour(FluidTransportBehaviour.TYPE);
            var connection = transport.getConnection(Direction.WEST);
            var network = ((PipeConnectionAccessor) connection).createpatina$getNetwork().orElseThrow();
            var next = BlockRegistry.ITEM_DRAIN_SET.getEntry(WeatheringType.EXPOSED, ItemDrainBlock.class).getDefaultState();
            if (legacy) {
                OxidizeUtil.replaceWithStateInternal(level.getBlockState(drain), next, level, drain);
            } else {
                // Same dispatcher used by right-click weathering, not the enhanced helper directly.
                OxidizeUtil.applySelectionWeathering(level.getBlockState(drain), next, level, drain, false);
                helper.assertTrue(transport.getConnection(Direction.WEST) == connection, "Connection was rebuilt");
                helper.assertTrue(((PipeConnectionAccessor) connection).createpatina$getNetwork().orElse(null) == network,
                        "Network was rebuilt");
            }
            if (!legacy) {
                helper.runAtTickTime(65, () -> {
                    lastSeen[0] = level.getCapability(Capabilities.FluidHandler.BLOCK, target, Direction.WEST)
                            .getFluidInTank(0).getAmount();
                    helper.assertTrue(lastSeen[0] > before, "First swap stopped transfer");
                    OxidizeUtil.applySelectionWeathering(level.getBlockState(drain),
                            AllBlocks.ITEM_DRAIN.getDefaultState(), level, drain, false);
                    helper.assertTrue(transport.getConnection(Direction.WEST) == connection,
                            "Reverse swap rebuilt connection");
                    helper.assertTrue(((PipeConnectionAccessor) connection).createpatina$getNetwork().orElse(null) == network,
                            "Reverse swap rebuilt network");
                });
            }
            helper.runAtTickTime(80, () -> {
                int after = level.getCapability(Capabilities.FluidHandler.BLOCK, target, Direction.WEST)
                        .getFluidInTank(0).getAmount();
                int remaining = ((ItemDrainBlockEntityAccessor) level.getBlockEntity(drain))
                        .getInternalTank().getPrimaryHandler().getFluidAmount();
                var currentConnection = transport.getConnection(Direction.WEST);
                var currentSource = ((PipeConnectionAccessor) currentConnection).createpatina$getSource().orElse(null);
                helper.assertTrue(after > lastSeen[0], "Transfer stopped: before=" + lastSeen[0] + " after=" + after
                        + " remaining=" + remaining + " speed=" + pump.getSpeed()
                        + " pressure=" + currentConnection.getPressure()
                        + " flow=" + currentConnection.hasFlow()
                        + " source=" + currentSource
                        + " handler=" + (currentSource == null ? null : currentSource.provideHandler()));
                helper.assertTrue(after + remaining == 1500, "Fluid was lost or duplicated");
                helper.succeed();
            });
        });
    }
}

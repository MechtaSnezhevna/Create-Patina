package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.data.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WeatheringFluidPipeBlock extends FluidPipeBlock implements PatinaBlock {

    private final WeatheringType type;

    public WeatheringFluidPipeBlock(WeatheringType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (tryRemoveBracket(context))
            return InteractionResult.SUCCESS;

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        Direction.Axis axis = FluidPropagator.getStraightPipeAxis(state);
        if (axis == null) {
            Vec3 clickLocation = context.getClickLocation()
                    .subtract(pos.getX(), pos.getY(), pos.getZ());
            double closest = Float.MAX_VALUE;
            Direction argClosest = Direction.UP;
            for (Direction direction : Iterate.directions) {
                if (clickedFace.getAxis() == direction.getAxis())
                    continue;
                Vec3 centerOf = Vec3.atCenterOf(direction.getNormal());
                double distance = centerOf.distanceToSqr(clickLocation);
                if (distance < closest) {
                    closest = distance;
                    argClosest = direction;
                }
            }
            axis = argClosest.getAxis();
        }

        if (clickedFace.getAxis() == axis)
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            withBlockEntityDo(world, pos, fpte -> fpte.getBehaviour(FluidTransportBehaviour.TYPE).interfaces.values()
                    .stream()
                    .filter(pc -> pc != null && pc.hasFlow())
                    .findAny()
                    .ifPresent($ -> AllAdvancements.GLASS_PIPE.awardTo(context.getPlayer())));

            FluidTransportBehaviour.cacheFlows(world, pos);
            world.setBlockAndUpdate(pos, getGlassVariant().getDefaultState()
                    .setValue(GlassFluidPipeBlock.AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            FluidTransportBehaviour.loadFlows(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    private BlockEntry<WeatheringGlassFluidPipeBlock> getGlassVariant() {
        assert type != WeatheringType.UNAFFECTED : "Unaffected blocks should not have a glass variant";
        return BlockRegistry.GLASS_FLUID_PIPES.getEntries().get(type);
    }

    @Override
    public WeatheringType getType() {
        return this.type;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        reconnect(level, pos);
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHERING_FLUID_PIPE.get();
    }

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

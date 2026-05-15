package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringFluidPipeBlock extends FluidPipeBlock implements PatinaBlock {

    private final WeatherState weatherState;

    private final Type type;

    public WeatheringFluidPipeBlock(Type type, Properties properties) {
        super(properties);
        this.type = type;
        this.weatherState = type.getWeatherState();
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    public Type getType() {
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

    public enum Type {
        EXPOSED, WEATHERED, OXIDIZED, WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED;

        WeatherState getWeatherState() {
            return switch (this) {
                case EXPOSED, WAXED_EXPOSED -> WeatherState.EXPOSED;
                case WEATHERED, WAXED_WEATHERED -> WeatherState.WEATHERED;
                case OXIDIZED, WAXED_OXIDIZED -> WeatherState.OXIDIZED;
                case WAXED -> WeatherState.UNAFFECTED;
            };
        }

        Boolean isWaxed() {
            return switch (this) {
                case EXPOSED, WEATHERED, OXIDIZED -> false;
                case WAXED, WAXED_EXPOSED, WAXED_WEATHERED, WAXED_OXIDIZED -> true;
            };
        }
    }
}

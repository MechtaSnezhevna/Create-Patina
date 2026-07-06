package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringFluidValveBlock extends FluidValveBlock implements PatinaBlock {

    private final WeatheringType type;

    public WeatheringFluidValveBlock(WeatheringType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public WeatheringType getType() {
        return type;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) ||
                (isWeatheringEnabled() && type != WeatheringType.OXIDIZED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        //remain empty as no special reconnect action needed
    }

    @Override
    public BlockEntityType<? extends FluidValveBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHERING_FLUID_VALVE.get();
    }
}

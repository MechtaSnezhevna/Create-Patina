package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringFluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidPipeBlock.class)
public abstract class FluidPipeOxidizationMixin extends Block implements PatinaBlock {
    public FluidPipeOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        WeatheringFluidPipeBlock.reconnect(level, pos);
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}

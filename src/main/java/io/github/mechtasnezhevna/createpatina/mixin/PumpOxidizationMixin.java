package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.pump.PumpBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPumpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PumpBlock.class)
public abstract class PumpOxidizationMixin extends Block implements PatinaBlock {
    public PumpOxidizationMixin(Properties properties) {
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
        WeatheringPumpBlock.reconnect(level, pos);
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}

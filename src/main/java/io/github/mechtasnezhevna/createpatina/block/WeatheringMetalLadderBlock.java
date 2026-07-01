package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.content.decoration.MetalLadderBlock;
import io.github.mechtasnezhevna.createpatina.util.PatinaMapColor;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WeatheringMetalLadderBlock extends MetalLadderBlock implements PatinaBlock {

    private final WeatheringType type;

    public WeatheringMetalLadderBlock(WeatheringType type, Properties p_54346_) {
        super(p_54346_);
        this.type = type;
    }

    @Override
    public WeatheringType getType() {
        return this.type;
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
}

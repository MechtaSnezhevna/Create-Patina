package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(ValveHandleBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopperValveHandleOxidizationMixin extends Block implements PatinaBlock{
    public CopperValveHandleOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getBlock().getDescriptionId().equals("block.create.copper_valve_handle");
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.UNAFFECTED;
    }
}

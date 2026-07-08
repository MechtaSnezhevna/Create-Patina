package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
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

@Mixin(MetalScaffoldingBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CopperScaffoldOxidizationMixin extends Block implements PatinaBlock {
    public CopperScaffoldOxidizationMixin(Properties p_54345_) {
        super(p_54345_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getBlock() == AllBlocks.COPPER_SCAFFOLD.get();
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

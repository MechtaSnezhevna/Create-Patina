package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

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
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(MetalScaffoldingBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CopperScaffoldOxidizationMixin extends Block implements PatinaBlock {
    public CopperScaffoldOxidizationMixin(Properties p_54345_) {
        super(p_54345_);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperScaffold;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperScaffold == null) {
            patina$isCopperScaffold = state.getBlock().getDescriptionId()
                    .contains("copper_scaffold");
        }
        return super.isRandomlyTicking(state) || canAdvanceWeathering();
    }

    @Override
    public boolean allowsNaturalWeathering() {
        if (patina$isCopperScaffold == null) {
            return false;
        }
        return patina$isCopperScaffold && !getType().isWaxed();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringType getType() {
        if (patina$type == null) {
            patina$type = WeatheringType.fromBlock(this);
        }
        return patina$type;
    }
}

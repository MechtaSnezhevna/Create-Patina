package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(IronBarsBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CopperBarsOxidizationMixin extends Block implements PatinaBlock {
    public CopperBarsOxidizationMixin(Properties p_54345_) {
        super(p_54345_);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperBars;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperBars == null) {
            patina$isCopperBars = state.getBlock().getDescriptionId()
                    .contains("copper_bars");
        }
        return super.isRandomlyTicking(state) || canAdvanceWeathering();
    }

    @Override
    public boolean allowsNaturalWeathering() {
        if (patina$isCopperBars == null) {
            return false;
        }
        return patina$isCopperBars && !getType().isWaxed();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
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

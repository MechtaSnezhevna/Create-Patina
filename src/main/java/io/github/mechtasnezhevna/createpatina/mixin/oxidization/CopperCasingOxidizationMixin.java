package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
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

@Mixin(CasingBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CopperCasingOxidizationMixin extends Block implements PatinaBlock {
    public CopperCasingOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperCasing;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperCasing == null) {
            patina$isCopperCasing = state.getBlock().getDescriptionId()
                    .contains("copper_casing");
        }
        return super.isRandomlyTicking(state) ||
                (patina$isCopperCasing && this.isWeatheringEnabled()
                        && getType() != WeatheringType.OXIDIZED);
    }

    @Override
    public boolean isWeatheringEnabled() {
        if (patina$isCopperCasing == null) {
            return false;
        }
        return patina$isCopperCasing && !getType().isWaxed();
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

package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BacktankBlock.class)
public abstract class CopperBacktankOxidizationMixin extends Block implements PatinaBlock {

    public CopperBacktankOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperBackTank;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperBackTank == null) {
            patina$isCopperBackTank = state.getBlock().getDescriptionId()
                    .contains("copper_backtank");
        }
        return super.isRandomlyTicking(state) ||
                (patina$isCopperBackTank && this.isWeatheringEnabled()
                        && getType() != WeatheringType.OXIDIZED);
    }

    @Override
    public boolean isWeatheringEnabled() {
        if (patina$isCopperBackTank == null) {
            return false;
        }
        return patina$isCopperBackTank && !getType().isWaxed();
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

    @ModifyReturnValue(method = "getBlockEntityType", at = @At("RETURN"))
    public BlockEntityType<? extends BacktankBlockEntity> getBlockEntityType(BlockEntityType<?> original) {
        if(patina$type == WeatheringType.UNAFFECTED) {
            return AllBlockEntityTypes.BACKTANK.get();
        }
        return BlockEntityRegistry.WEATHERING_COPPER_BACKTANK.get();
    }
}

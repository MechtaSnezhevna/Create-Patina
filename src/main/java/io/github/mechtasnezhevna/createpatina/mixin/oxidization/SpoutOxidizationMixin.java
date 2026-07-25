package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaFluidEndpoint;
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

@Mixin(SpoutBlock.class)
public class SpoutOxidizationMixin extends Block implements PatinaFluidEndpoint {
    public SpoutOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) || canAdvanceWeathering();
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
    public BlockEntityType<? extends SpoutBlockEntity> getBlockEntityType(BlockEntityType<?> type) {
        if(getType() == WeatheringType.UNAFFECTED){
            return BlockEntityRegistry.WEATHERING_SPOUT.get();
        }
        return BlockEntityRegistry.WEATHERING_SPOUT.get();
    }
}

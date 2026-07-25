package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WhistleBlock.class)
public class SteamWhistleOxidizationMixin extends Block implements PatinaBlock {
    public SteamWhistleOxidizationMixin(BlockBehaviour.Properties properties) {
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
    public BlockEntityType<? extends WhistleBlockEntity> getBlockEntityType(BlockEntityType<?> original) {
        if(getType() == WeatheringType.UNAFFECTED){
            return AllBlockEntityTypes.STEAM_WHISTLE.get();
        }
        return BlockEntityRegistry.WEATHERING_STEAM_WHISTLE.get();
    }
}

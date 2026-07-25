package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(SteamEngineBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamEngineOxidizationMixin extends Block implements PatinaBlock {
    public SteamEngineOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
//        if(state.getBlock().getDescriptionId().equals("block.create.steam_engine"))
//            return true;
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
    public BlockEntityType<? extends SteamEngineBlockEntity> getBlockEntityType(BlockEntityType<?> original) {
        if(getType() == WeatheringType.UNAFFECTED){
            return AllBlockEntityTypes.STEAM_ENGINE.get();
        }
        return BlockEntityRegistry.WEATHERING_STEAM_ENGINE.get();
    }
}

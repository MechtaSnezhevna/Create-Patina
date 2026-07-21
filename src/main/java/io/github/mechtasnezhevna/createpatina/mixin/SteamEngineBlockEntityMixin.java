package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SteamEngineBlockEntity.class)
public abstract class SteamEngineBlockEntityMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void createpatina$checkFluidTankInstance(CallbackInfoReturnable<Boolean> cir) {
        SteamEngineBlockEntity self = (SteamEngineBlockEntity) (Object) this;
        Direction dir = SteamEngineBlock.getConnectedDirection(self.getBlockState()).getOpposite();
        Level level = self.getLevel();
        if (level == null) {
            cir.setReturnValue(false);
            return;
        }
        Block block = level.getBlockState(self.getBlockPos().relative(dir)).getBlock();
        if (block == AllBlocks.FLUID_TANK.get()){
            cir.setReturnValue(true);
            return;
        }
        else {
            for (WeatheringType type : WeatheringType.values()) {
                if (block == BlockRegistry.FLUID_TANK_SET.get(type)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
        cir.setReturnValue(false);
    }
}
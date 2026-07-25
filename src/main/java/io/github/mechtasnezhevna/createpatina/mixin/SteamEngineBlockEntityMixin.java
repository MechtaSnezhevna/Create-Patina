package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SteamEngineBlockEntity.class)
public abstract class SteamEngineBlockEntityMixin {

    @ModifyReturnValue(method = "isValid", at = @At("RETURN"))
    private boolean createpatina$acceptPatinaFluidTank(boolean original) {
        if (original) {
            return true;
        }

        SteamEngineBlockEntity self = (SteamEngineBlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) {
            return false;
        }

        Direction direction = SteamEngineBlock.getConnectedDirection(self.getBlockState()).getOpposite();
        return BlockRegistry.FLUID_TANK_SET.has(
                level.getBlockState(self.getBlockPos().relative(direction))
        );
    }
}

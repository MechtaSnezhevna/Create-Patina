package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SteamEngineBlockEntity.class, remap = false)
public abstract class SteamEngineBlockEntityMixin {

    /*
     * Original Create 1.20.1 code from SteamEngineBlockEntity#tick:
     * BlockState blockState = getBlockState();
     * if (!AllBlocks.STEAM_ENGINE.has(blockState))
     *     return;
     *
     * The weathering variants are different blocks, so without this a weathered steam engine
     * bails out before it can drive its shaft. The redirect lives here instead of next to the
     * getTargetAngle one because tick runs on both sides.
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean createpatina$recognizeSteamEngineVariants(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_ENGINE) {
            return BlockRegistry.STEAM_ENGINE_SET.has(state);
        }
        return entry.has(state);
    }

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

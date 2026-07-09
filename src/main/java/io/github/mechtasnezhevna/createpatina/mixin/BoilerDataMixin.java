package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BoilerData.class, remap = false)
public abstract class BoilerDataMixin {

    @Redirect(
            method = "evaluate",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHas(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_ENGINE) {
            return state.getBlock() == AllBlocks.STEAM_ENGINE.get() || state.getBlock() instanceof SteamEngineBlock;
        }
        return entry.has(state);
    }
}
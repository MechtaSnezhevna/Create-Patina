package io.github.mechtasnezhevna.createpatina.mixin.render;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SteamEngineBlockEntity.class, remap = false)
public class SteamEngineBlockEntityMixin {

    @Redirect(
            method = "getTargetAngle",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHasInGetTargetAngle(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_ENGINE) {
            return BlockRegistry.STEAM_ENGINE_SET.has(state);
        }
        return entry.has(state);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHasInTick(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_ENGINE) {
            return BlockRegistry.STEAM_ENGINE_SET.has(state);
        }
        return entry.has(state);
    }
}
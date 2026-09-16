package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BoilerData.class, remap = false)
public abstract class BoilerDataMixin {

    /*
     * Original Create 1.20.1 code from BoilerData#evaluate:
     * if (AllBlocks.STEAM_ENGINE.has(attachedState) && SteamEngineBlock.getFacing(attachedState) == d)
     *     attachedEngines++;
     * if (AllBlocks.STEAM_WHISTLE.has(attachedState) && WhistleBlock.getAttachedDirection(...) == d)
     *     attachedWhistles++;
     */
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
            return BlockRegistry.STEAM_ENGINE_SET.has(state);
        }
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return BlockRegistry.STEAM_WHISTLE_SET.has(state);
        }
        return entry.has(state);
    }

    /*
     * Original Create 1.20.1 code from BoilerData#checkPipeOrganAdvancement:
     * if (AllBlocks.STEAM_WHISTLE.has(attachedState)
     *     && WhistleBlock.getAttachedDirection(attachedState).getOpposite() == d)
     *
     * The same identity test as in evaluate, this time while collecting the whistle pitches, so
     * a boiler carrying weathering whistles never awarded the pipe organ advancement.
     */
    @Redirect(
            method = "checkPipeOrganAdvancement",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            remap = false
    )
    private boolean redirectHasPipeOrgan(BlockEntry<?> entry, BlockState state) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return BlockRegistry.STEAM_WHISTLE_SET.has(state);
        }
        return entry.has(state);
    }
}

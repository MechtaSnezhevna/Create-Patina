package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PortableStorageInterfaceMovement.class)
public class PortableStorageInterfaceMovementMixin {

    @Redirect(
            method = "getStationaryInterfaceAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",
                    ordinal = 0
            )
    )
    private Block redirectTargetBlock(BlockState instance) {
        if (BlockRegistry.PORTABLE_FLUID_INTERFACE_SET.has(instance)) {
            return AllBlocks.PORTABLE_FLUID_INTERFACE.get();
        }
        return instance.getBlock();
    }

    @Redirect(
            method = "getStationaryInterfaceAt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",
                    ordinal = 1
            )
    )
    private Block redirectOriginalBlock(BlockState instance) {
        if (BlockRegistry.PORTABLE_FLUID_INTERFACE_SET.has(instance)) {
            return AllBlocks.PORTABLE_FLUID_INTERFACE.get();
        }
        return instance.getBlock();
    }

}

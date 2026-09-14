package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WhistleBlock.class, remap = false)
public class WhistleBlockUsageMixin {

    @Redirect(
            // verified: Create 6.0.8 WhistleBlock#use source for Minecraft 1.20.1, 2026-09-14
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z",
                    remap = false
            ),
            remap = true
    )
    private boolean redirectWhistleIsIn(BlockEntry<?> entry, ItemStack stack) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            return BlockRegistry.STEAM_WHISTLE_SET.isIn(stack);
        }
        return entry.isIn(stack);
    }
}

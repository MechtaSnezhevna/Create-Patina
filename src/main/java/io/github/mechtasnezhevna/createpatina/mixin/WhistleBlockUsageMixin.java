package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.block.WeatheringWhistleBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WhistleBlock.class)
public class WhistleBlockUsageMixin {

    @Redirect(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            remap = false
    )
    private boolean redirectWhistleIsIn(BlockEntry<?> entry, ItemStack stack) {
        if (entry == AllBlocks.STEAM_WHISTLE) {
            Block block = Block.byItem(stack.getItem());
            return block == AllBlocks.STEAM_WHISTLE.get()
                    || block instanceof WeatheringWhistleBlock;
        }
        return entry.isIn(stack);
    }
}
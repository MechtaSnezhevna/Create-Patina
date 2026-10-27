package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.logistics.packager.PackagerBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackagerBlock.class)
public class PackagerBlockMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void onUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                             Player player, InteractionHand hand, BlockHitResult hitResult,
                             CallbackInfoReturnable<ItemInteractionResult> cir) {
        for(WeatheringType type : WeatheringType.values()) {
            if(type == WeatheringType.UNAFFECTED)
                continue;
            if (BlockRegistry.STOCK_LINK_SET.getEntry(type).get().asItem().equals(stack.getItem())) {
                if (!(state.hasProperty(PackagerBlock.LINKED) && state.getValue(PackagerBlock.LINKED))) {
                    cir.setReturnValue(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
                }
            }
        }
    }
}

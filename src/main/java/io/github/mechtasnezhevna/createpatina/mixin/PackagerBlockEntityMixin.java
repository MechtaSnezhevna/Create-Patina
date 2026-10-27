package io.github.mechtasnezhevna.createpatina.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PackagerBlockEntity.class)
public abstract class PackagerBlockEntityMixin {

    @Shadow public abstract void triggerStockCheck();

    @ModifyExpressionValue(
            method = {
                    "submitNewArrivals",
                    "getLinkPos",
                    "flashLink"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            require = 3
    )
    private boolean createpatina$recognizePatinaBlock(
            boolean original,
            @Local BlockState adjacentState
    ) {
        if(original)
            return true;
        for(WeatheringType type: WeatheringType.values()) {
            if(type == WeatheringType.UNAFFECTED)
                continue;
            if(BlockRegistry.STOCK_LINK_SET.getEntry(type).has(adjacentState))
                return true;
        }
        return false;
    }
}

package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlockEntityVisual.class)
public interface AbstractBlockEntityVisualAccessor {

    @Accessor("blockState")
    BlockState getBlockState();

}

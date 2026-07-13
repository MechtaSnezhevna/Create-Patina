package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HosePulleyBlockEntity.class)
public interface HosePulleyBlockEntityAccessor {

    @Accessor("handler")
    HosePulleyFluidHandler getHandler();
}

package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.FlowSource;
import com.simibubi.create.foundation.ICapabilityProvider;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlowSource.FluidHandler.class)
public interface FlowSourceHandlerAccessor {
    @Accessor("fluidHandlerCache")
    void createpatina$setHandlerCache(ICapabilityProvider<IFluidHandler> provider);
}

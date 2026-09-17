package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.foundation.ICapabilityProvider;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidNetwork.class)
public interface FluidNetworkAccessor {
    @Accessor("source")
    void createpatina$setSource(ICapabilityProvider<IFluidHandler> provider);
}

package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.FlowSource;
import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.content.fluids.PipeConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.Optional;

@Mixin(PipeConnection.class)
public interface PipeConnectionAccessor {
    @Accessor("source") Optional<FlowSource> createpatina$getSource();
    @Accessor("network") Optional<FluidNetwork> createpatina$getNetwork();
}

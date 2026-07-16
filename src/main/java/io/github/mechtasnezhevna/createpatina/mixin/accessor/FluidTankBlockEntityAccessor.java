package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTankBlockEntity.class)
public interface FluidTankBlockEntityAccessor {

    @Accessor("fluidCapability")
    IFluidHandler getFluidCapability();

}

package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidTankBlockEntity.class)
public interface FluidTankBlockEntityInvoker {

    @Invoker("refreshCapability")
    void createpatina$callRefreshCapability();

}

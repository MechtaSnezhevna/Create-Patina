package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PortableFluidInterfaceBlockEntity.class)
public interface PortableFluidInterfaceBlockEntityAccessor {

    @Accessor("capability")
    IFluidHandler getCapability();

}

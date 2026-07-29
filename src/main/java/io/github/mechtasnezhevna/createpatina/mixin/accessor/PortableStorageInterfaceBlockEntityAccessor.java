package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PortableStorageInterfaceBlockEntity.class, remap = false)
public interface PortableStorageInterfaceBlockEntityAccessor {

    @Accessor("connectedEntity")
    Entity createPatina$getConnectedEntity();

    @Accessor("transferTimer")
    int createPatina$getTransferTimer();

    @Accessor("transferTimer")
    void createPatina$setTransferTimer(int transferTimer);

    @Accessor("distance")
    float createPatina$getDistance();
}

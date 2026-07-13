package io.github.mechtasnezhevna.createpatina.mixin.accessor;

import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainItemHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemDrainBlockEntity.class)
public interface ItemDrainBlockEntityAccessor {

    @Accessor("itemHandlers")
    Map<Direction, ItemDrainItemHandler> getItemHandlers();

    @Accessor("internalTank")
    SmartFluidTankBehaviour getInternalTank();
}

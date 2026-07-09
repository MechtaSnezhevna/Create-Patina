package io.github.mechtasnezhevna.createpatina.event;

import com.simibubi.create.AllBlockEntityTypes;
import io.github.mechtasnezhevna.createpatina.mixin.ItemDrainBlockEntityAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.SpoutBlockEntityAccessor;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

@EventBusSubscriber(modid = MODID)
public class CapabilityEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.WEATHERING_ITEM_DRAIN.get(),
                (be, context) -> {
                    if (context != null && context.getAxis().isHorizontal()) {
                        return ((ItemDrainBlockEntityAccessor) be).getItemHandlers().get(context);
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.WEATHERING_ITEM_DRAIN.get(),
                (be, context) -> {
                    if (context != Direction.UP) {
                        return ((ItemDrainBlockEntityAccessor) be).getInternalTank().getCapability();
                    }
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.WEATHERING_SPOUT.get(),
                (be, context) -> {
                    if (context != Direction.DOWN)
                        return ((SpoutBlockEntityAccessor) be).getTank().getCapability();
                    return null;
                }
        );
    }
}

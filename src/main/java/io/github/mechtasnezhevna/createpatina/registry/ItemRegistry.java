package io.github.mechtasnezhevna.createpatina.registry;

import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class ItemRegistry
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<BlockItem> EXPOSED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("exposed_item_drain",
            BlockRegistry.EXPOSED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> WEATHERED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("weathered_item_drain",
            BlockRegistry.WEATHERED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> OXIDIZED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("oxidized_item_drain",
            BlockRegistry.OXIDIZED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> WAXED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("waxed_item_drain",
            BlockRegistry.WAXED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> WAXED_EXPOSED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("waxed_exposed_item_drain",
            BlockRegistry.WAXED_EXPOSED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> WAXED_WEATHERED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("waxed_weathered_item_drain",
            BlockRegistry.WAXED_WEATHERED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> WAXED_OXIDIZED_ITEM_DRAIN = ITEMS.registerSimpleBlockItem("waxed_oxidized_item_drain",
            BlockRegistry.WAXED_OXIDIZED_ITEM_DRAIN);

    public static final DeferredItem<BlockItem> EXPOSED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("exposed_mechanical_pump",
            BlockRegistry.EXPOSED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> WEATHERED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("weathered_mechanical_pump",
            BlockRegistry.WEATHERED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> OXIDIZED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("oxidized_mechanical_pump",
            BlockRegistry.OXIDIZED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> WAXED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("waxed_mechanical_pump",
            BlockRegistry.WAXED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> WAXED_EXPOSED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("waxed_exposed_mechanical_pump",
            BlockRegistry.WAXED_EXPOSED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> WAXED_WEATHERED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("waxed_weathered_mechanical_pump",
            BlockRegistry.WAXED_WEATHERED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> WAXED_OXIDIZED_MECHANICAL_PUMP = ITEMS.registerSimpleBlockItem("waxed_oxidized_mechanical_pump",
            BlockRegistry.WAXED_OXIDIZED_MECHANICAL_PUMP);

    public static final DeferredItem<BlockItem> EXPOSED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("exposed_fluid_pipe",
            BlockRegistry.EXPOSED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> WEATHERED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("weathered_fluid_pipe",
            BlockRegistry.WEATHERED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> OXIDIZED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("oxidized_fluid_pipe",
            BlockRegistry.OXIDIZED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> WAXED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("waxed_fluid_pipe",
            BlockRegistry.WAXED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> WAXED_EXPOSED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("waxed_exposed_fluid_pipe",
            BlockRegistry.WAXED_EXPOSED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> WAXED_WEATHERED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("waxed_weathered_fluid_pipe",
            BlockRegistry.WAXED_WEATHERED_FLUID_PIPE);

    public static final DeferredItem<BlockItem> WAXED_OXIDIZED_FLUID_PIPE = ITEMS.registerSimpleBlockItem("waxed_oxidized_fluid_pipe",
            BlockRegistry.WAXED_OXIDIZED_FLUID_PIPE);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

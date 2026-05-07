package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.simibubi.create.AllBlocks.ITEM_DRAIN;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = CreatePatina.BLOCKS;

    public static final DeferredBlock<Block> EXAMPLE_COPPER_BLOCK = BLOCKS.register("example_copper_block", () -> new WeatheringCopperFullBlock(
            WeatheringCopper.WeatherState.UNAFFECTED,
            BlockBehaviour.Properties.of()
                    .strength(3.0f, 6.0f)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops()
                    .randomTicks()
    ));

    public static final DeferredBlock<Block> EXAMPLE_EXPOSED_COPPER_BLOCK = BLOCKS.register("example_exposed_copper_block", () -> new WeatheringCopperFullBlock(
            WeatheringCopper.WeatherState.EXPOSED,
            BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())
    ));

    public static final DeferredBlock<Block> EXAMPLE_WEATHERED_COPPER_BLOCK = BLOCKS.register("example_weathered_copper_block", () -> new WeatheringCopperFullBlock(
            WeatheringCopper.WeatherState.WEATHERED,
            BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())
    ));

    public static final DeferredBlock<Block> EXAMPLE_OXIDIZED_COPPER_BLOCK = BLOCKS.register("example_oxidized_copper_block", () -> new WeatheringCopperFullBlock(
            WeatheringCopper.WeatherState.OXIDIZED,
            BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())
    ));

    public static final DeferredBlock<Block> WAXED_EXAMPLE_COPPER_BLOCK = BLOCKS.register("waxed_example_copper_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())) );

    public static final DeferredBlock<Block> WAXED_EXAMPLE_EXPOSED_COPPER_BLOCK = BLOCKS.register("waxed_example_exposed_copper_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())) );

    public static final DeferredBlock<Block> WAXED_EXAMPLE_WEATHERED_COPPER_BLOCK = BLOCKS.register("waxed_example_weathered_copper_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())) );

    public static final DeferredBlock<Block> WAXED_EXAMPLE_OXIDIZED_COPPER_BLOCK = BLOCKS.register("waxed_example_oxidized_copper_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(EXAMPLE_COPPER_BLOCK.get())) );
/*
    public static final DeferredBlock<Block> EXPOSED_ITEM_DRAIN = BLOCKS.register("exposed_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> WEATHERED_ITEM_DRAIN = BLOCKS.register("weathered_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> OXIDIZED_ITEM_DRAIN = BLOCKS.register("oxidized_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> WAXED_ITEM_DRAIN = BLOCKS.register("waxed_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> WAXED_EXPOSED_ITEM_DRAIN = BLOCKS.register("waxed_exposed_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> WAXED_WEATHERED_ITEM_DRAIN = BLOCKS.register("waxed_weathered_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));

    public static final DeferredBlock<Block> WAXED_OXIDIZED_ITEM_DRAIN = BLOCKS.register("waxed_oxidized_item_drain",
            () -> new ItemDrainBlock(BlockBehaviour.Properties.ofFullCopy(ITEM_DRAIN.get())));
*/
    public static void registerBlockItems() {
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_copper_block", EXAMPLE_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_exposed_copper_block", EXAMPLE_EXPOSED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_weathered_copper_block", EXAMPLE_WEATHERED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_oxidized_copper_block", EXAMPLE_OXIDIZED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_example_copper_block", WAXED_EXAMPLE_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_example_exposed_copper_block", WAXED_EXAMPLE_EXPOSED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_example_weathered_copper_block", WAXED_EXAMPLE_WEATHERED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_example_oxidized_copper_block", WAXED_EXAMPLE_OXIDIZED_COPPER_BLOCK);
        /*
        ItemRegistry.ITEMS.registerSimpleBlockItem("exposed_item_drain", EXPOSED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("weathered_item_drain", WEATHERED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("oxidized_item_drain", OXIDIZED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_item_drain", WAXED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_exposed_item_drain", WAXED_EXPOSED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_weathered_item_drain", WAXED_WEATHERED_ITEM_DRAIN);
        ItemRegistry.ITEMS.registerSimpleBlockItem("waxed_oxidized_item_drain", WAXED_OXIDIZED_ITEM_DRAIN);
    */
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        registerBlockItems();
    }
}

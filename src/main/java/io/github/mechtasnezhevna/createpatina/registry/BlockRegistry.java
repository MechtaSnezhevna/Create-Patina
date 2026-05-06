package io.github.mechtasnezhevna.createpatina.registry;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

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


    public static void registerBlockItems() {
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_copper_block", EXAMPLE_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_exposed_copper_block", EXAMPLE_EXPOSED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_weathered_copper_block", EXAMPLE_WEATHERED_COPPER_BLOCK);
        ItemRegistry.ITEMS.registerSimpleBlockItem("example_oxidized_copper_block", EXAMPLE_OXIDIZED_COPPER_BLOCK);
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        registerBlockItems();
    }
}

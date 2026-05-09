package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.WeatheringItemDrainBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BlockRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final BlockEntry<WeatheringItemDrainBlock> EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.EXPOSED, properties))
            .initialProperties(AllBlocks.ITEM_DRAIN)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.WEATHERED, properties))
            .initialProperties(AllBlocks.ITEM_DRAIN)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(AllBlocks.ITEM_DRAIN)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_ITEM_DRAIN = REGISTRATE
            .block("waxed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.UNAFFECTED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("waxed_exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("waxed_weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("waxed_oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .register();


    public static void register() {
    }
}

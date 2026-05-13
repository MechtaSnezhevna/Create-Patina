package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.WeatheringItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPumpBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BlockRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final BlockEntry<WeatheringItemDrainBlock> EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_ITEM_DRAIN = REGISTRATE
            .block("waxed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.UNAFFECTED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("waxed_exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("waxed_weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("waxed_oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringItemDrainBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .register();

    public static final BlockEntry<WeatheringPumpBlock> EXPOSED_MECHANICAL_PUMP = REGISTRATE
            .block("exposed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})

            .register();

    public static final BlockEntry<WeatheringPumpBlock> WEATHERED_MECHANICAL_PUMP = REGISTRATE
            .block("weathered_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static final BlockEntry<WeatheringPumpBlock> OXIDIZED_MECHANICAL_PUMP = REGISTRATE
            .block("oxidized_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.UNAFFECTED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_EXPOSED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_exposed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_WEATHERED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_weathered_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_OXIDIZED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_oxidized_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringPumpBlock.WeatherState.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {})
            .register();

    public static void register() {
    }
}

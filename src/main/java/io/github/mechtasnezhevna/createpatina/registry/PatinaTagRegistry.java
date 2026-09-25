package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

import java.util.EnumMap;
import java.util.Map;

import static io.github.mechtasnezhevna.createpatina.registry.util.PatinaTags.*;

public class PatinaTagRegistry {
    private static final CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static void addGenerators() {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, PatinaTagRegistry::genBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, PatinaTagRegistry::genItemTags);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, PatinaTagRegistry::genFluidTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(PatinaBlockTags.FAN_PROCESSING_CATALYSTS_HONEYING.tag)
                .add(Blocks.HONEY_BLOCK);

        prov.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
                .add(Blocks.HONEY_BLOCK);
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        Map<WeatheringType, TagGen.CreateTagAppender<Item>> m =  new EnumMap<>(WeatheringType.class);
        for (WeatheringType type: WeatheringType.values()) {
            m.put(type, prov.tag(BY_TYPE.get(type).tag));
        }

        for (PatinaSet set: PatinaSet.all()) {
            for (WeatheringType type: WeatheringType.values()) {
                Item item = set.get(type).asItem();
                if (item != Items.AIR) {
                    m.get(type).add(item);
                }
            }
        }

        ItemRegistry.ARMOR_BACKTANKS.forEach((t, e) ->
                m.get(t).add(e.asItem())
        );

    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
        TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);

        prov.tag(PatinaFluidTags.FAN_PROCESSING_CATALYSTS_HONEYING.tag)
                .addTag(Tags.Fluids.HONEY);
    }
}

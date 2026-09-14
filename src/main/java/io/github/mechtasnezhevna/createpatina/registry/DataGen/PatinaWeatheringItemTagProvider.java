package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaItemTags;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the eight weathering-stage item tags under {@code data/createpatina/tags/item/}.
 * Every weathering copper part belongs to the tag of its current stage, vanilla and Create items included.
 */
public class PatinaWeatheringItemTagProvider extends ItemTagsProvider {

    public PatinaWeatheringItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<WeatheringType, Set<ResourceLocation>> itemsByType = new EnumMap<>(WeatheringType.class);
        for (WeatheringType type : WeatheringType.values()) {
            itemsByType.put(type, new LinkedHashSet<>());
        }

        for (PatinaSet set : PatinaSet.all()) {
            for (WeatheringType type : WeatheringType.values()) {
                Item item = set.get(type).asItem();
                if (item != Items.AIR) {
                    itemsByType.get(type).add(BuiltInRegistries.ITEM.getKey(item));
                }
            }
        }

        // Wearable backtanks are separate items from the blocks they place.
        ItemRegistry.ARMOR_BACKTANKS.forEach((type, backtank) ->
                itemsByType.get(type).add(BuiltInRegistries.ITEM.getKey(backtank.get())));

        for (WeatheringType type : WeatheringType.values()) {
            itemsByType.get(type).forEach(location ->
                    tag(PatinaItemTags.forType(type)).add(ResourceKey.create(Registries.ITEM, location)));
        }
    }
}

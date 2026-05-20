package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class CreativeModeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> CREATEPATINA_TAB = CREATIVE_MODE_TABS.register("createpatina_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.createpatina"))
                    .icon(BlockRegistry.ITEM_DRAINS.getEntry(WeatheringType.WEATHERED)::asStack)
                    .build()
    );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }

    public static void putOriginalItemsBefore(BuildCreativeModeTabContentsEvent e) {
        if (e.getTab() == CREATEPATINA_TAB.get()) {
            insertBefore(e, BlockRegistry.ITEM_DRAINS.getEntry(WeatheringType.EXPOSED), AllBlocks.ITEM_DRAIN);
            insertBefore(e, BlockRegistry.MECHANICAL_PUMPS.getEntry(WeatheringType.EXPOSED), AllBlocks.MECHANICAL_PUMP);
            insertBefore(e, BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.EXPOSED), AllBlocks.FLUID_PIPE);
            insertBefore(e, BlockRegistry.COPPER_CASINGS.getEntry(WeatheringType.EXPOSED), AllBlocks.COPPER_CASING);
        }
    }



    private static void insertBefore(BuildCreativeModeTabContentsEvent e, ItemProviderEntry<?, ?> existingEntry, ItemProviderEntry<?, ?> newEntry, CreativeModeTab.TabVisibility visibility) {
        e.insertBefore(existingEntry.asStack(), newEntry.asStack(), visibility);
    }

    private static void insertBefore(BuildCreativeModeTabContentsEvent e, ItemProviderEntry<?, ?> existingEntry, ItemProviderEntry<?, ?> newEntry) {
        insertBefore(e, existingEntry, newEntry, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void insertAfter(BuildCreativeModeTabContentsEvent e, ItemProviderEntry<?, ?> existingEntry, ItemProviderEntry<?, ?> newEntry, CreativeModeTab.TabVisibility visibility) {
        e.insertAfter(existingEntry.asStack(), newEntry.asStack(), visibility);
    }

    private static void insertAfter(BuildCreativeModeTabContentsEvent e, ItemProviderEntry<?, ?> existingEntry, ItemProviderEntry<?, ?> newEntry) {
        insertAfter(e, existingEntry, newEntry, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

}

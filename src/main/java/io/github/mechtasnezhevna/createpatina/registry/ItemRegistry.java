package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class ItemRegistry
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    private static final CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final Map<WeatheringType, ItemEntry<BacktankItem.BacktankBlockItem>> PLACEABLE_BACKTANKS = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, ItemEntry<? extends BacktankItem>> ARMOR_BACKTANKS = new EnumMap<>(WeatheringType.class);

    static {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.equals(WeatheringType.UNAFFECTED)) continue;

            String prefix = type.getPrefixWithoutWaxed();
            String baseName = type.getPrefix() + "copper_backtank";
            // Weathering variants behave exactly like the original Copper Backtank, so once
            // registered they reuse its tooltip text instead of duplicating it.
            PLACEABLE_BACKTANKS.put(type, REGISTRATE
                    .item(baseName + "_placeable", p -> new BacktankItem.BacktankBlockItem(
                            BlockRegistry.COPPER_BACKTANK_SET.get(type),
                            () -> ARMOR_BACKTANKS.get(type).get(), p))
                    .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.create.copper_backtank"))
                    .register());
            ARMOR_BACKTANKS.put(type, REGISTRATE
                    .item(baseName, p -> new BacktankItem(
                            AllArmorMaterials.COPPER, p,
                            ResourceLocation.fromNamespaceAndPath(MODID,prefix + "copper_diving"),
                            PLACEABLE_BACKTANKS.get(type)))
                    .model((c, p) ->
                            p.withExistingParent(baseName, Create.asResource("block/copper_backtank/item"))
                             .texture("0", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank"))
                             .texture("particle", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank")))
                    .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
                    .tag(ItemTags.CHEST_ARMOR)
                    .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.create.copper_backtank"))
                    .register());
        }
    }

    public static final ItemEntry<PatinaClockItem> PATINA_CLOCK = REGISTRATE
            .item("patina_clock", PatinaClockItem::new)
            .properties(p -> p.stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .durability(256)
            )
//            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_PATINA_CLOCK = REGISTRATE
            .item("incomplete_patina_clock", SequencedAssemblyItem::new)
            .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/generated"))
                    .texture("layer0", p.modLoc("item/patina_clock")))
            .register();

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

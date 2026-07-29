package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.EnumMap;
import java.util.Map;

public class ItemRegistry
{
    private static final CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final Map<WeatheringType, ItemEntry<BacktankItem.BacktankBlockItem>> PLACEABLE_BACKTANKS = new EnumMap<>(WeatheringType.class);
    public static final Map<WeatheringType, ItemEntry<? extends BacktankItem>> ARMOR_BACKTANKS = new EnumMap<>(WeatheringType.class);

    static {
        for (WeatheringType type : WeatheringType.values()) {
            if (type.equals(WeatheringType.UNAFFECTED)) continue;

            String prefix = type.getPrefixWithoutWaxed();
            String baseName = type.getPrefix() + "copper_backtank";
            PLACEABLE_BACKTANKS.put(type, REGISTRATE
                    .item(baseName + "_placeable", p -> new BacktankItem.BacktankBlockItem(
                            BlockRegistry.COPPER_BACKTANK_SET.get(type),
                            () -> ARMOR_BACKTANKS.get(type).get(), p))
                    .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
                    .register());
            ARMOR_BACKTANKS.put(type, REGISTRATE
                    .item(baseName, p -> new BacktankItem(
                            AllArmorMaterials.COPPER, p,
                            new ResourceLocation(CreatePatina.MODID, prefix + "copper_diving"),
                            PLACEABLE_BACKTANKS.get(type)))
                    .model((c, p) ->
                            p.withExistingParent(baseName, Create.asResource("block/copper_backtank/item"))
                             .texture("0", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank"))
                             .texture("particle", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank")))
                    .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
                    .tag(AllTags.AllItemTags.CHESTPLATE_ARMORS.tag)
                    .register());
        }
    }

    public static final ItemEntry<PatinaClockItem> PATINA_CLOCK = REGISTRATE
            .item("patina_clock", PatinaClockItem::new)
            .properties(p -> p.stacksTo(1)
                    .rarity(Rarity.EPIC)
            )
//            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static void register() {
    }
}

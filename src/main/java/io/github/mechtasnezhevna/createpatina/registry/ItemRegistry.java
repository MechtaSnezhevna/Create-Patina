package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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
            PLACEABLE_BACKTANKS.put(type, REGISTRATE
                    .item(baseName + "_placeable", p -> new BacktankItem.BacktankBlockItem(
                            BlockRegistry.COPPER_BACKTANK_SET.get(type),
                            () -> ARMOR_BACKTANKS.get(type).get(), p))
                    .model((c, p) -> p.withExistingParent(c.getName(), p.mcLoc("item/barrier")))
                    .register());
            ARMOR_BACKTANKS.put(type, REGISTRATE
                    .item(baseName, p -> new BacktankItem(
                            AllArmorMaterials.COPPER, p,
                            ResourceLocation.fromNamespaceAndPath(MODID,prefix + "copper_diving"),
                            PLACEABLE_BACKTANKS.get(type)))
                    .model((c, p) ->
                            p.withExistingParent(baseName, p.modLoc("block/copper_backtank/item"))
                             .texture("0", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank"))
                             .texture("particle", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank")))
                    .tag(AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
                    .tag(ItemTags.CHEST_ARMOR)
                    .register());
        }
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

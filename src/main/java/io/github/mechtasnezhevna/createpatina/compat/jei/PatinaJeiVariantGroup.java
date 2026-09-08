package io.github.mechtasnezhevna.createpatina.compat.jei;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import io.github.mechtasnezhevna.createpatina.registry.ItemRegistry;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * One weather-collapsible group shown in JEI while
 * {@code COLLAPSE_PATINA_SETS_IN_JEI} is enabled: the unaffected representative
 * plus the variant stacks hidden behind it.
 */
record PatinaJeiVariantGroup(ItemStack representative, List<ItemStack> variants) {

    /**
     * Collects every group that JEI should collapse: one per block {@link PatinaSet}, plus the
     * wearable backtank items, which are standalone items rather than block items and therefore
     * are not covered by the COPPER_BACKTANK block set.
     */
    static List<PatinaJeiVariantGroup> all() {
        List<PatinaJeiVariantGroup> groups = new ArrayList<>();

        for (PatinaSet set : PatinaSet.all()) {
            addGroup(groups, set.get(WeatheringType.UNAFFECTED).asItem().getDefaultInstance(),
                    variantsOf(type -> set.get(type).asItem().getDefaultInstance()));
        }

        addGroup(groups, fullBacktank(AllItems.COPPER_BACKTANK.get()),
                variantsOf(type -> fullBacktank(ItemRegistry.ARMOR_BACKTANKS.get(type).get())));

        return List.copyOf(groups);
    }

    /**
     * JEI shows the wearable backtanks with a full tank, matching the creative tab.
     */
    private static ItemStack fullBacktank(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.set(AllDataComponents.BACKTANK_AIR, BacktankUtil.maxAirWithoutEnchants());
        return stack;
    }

    private static List<ItemStack> variantsOf(StackProvider provider) {
        List<ItemStack> variants = new ArrayList<>();
        for (WeatheringType type : WeatheringType.values()) {
            if (type != WeatheringType.UNAFFECTED) {
                ItemStack variant = provider.stackFor(type);
                if (!variant.isEmpty()) {
                    variants.add(variant);
                }
            }
        }
        return variants;
    }

    private static void addGroup(List<PatinaJeiVariantGroup> groups, ItemStack representative, List<ItemStack> variants) {
        if (!representative.isEmpty() && !variants.isEmpty()) {
            groups.add(new PatinaJeiVariantGroup(representative, List.copyOf(variants)));
        }
    }

    @FunctionalInterface
    private interface StackProvider {
        ItemStack stackFor(WeatheringType type);
    }
}

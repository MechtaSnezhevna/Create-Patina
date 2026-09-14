package io.github.mechtasnezhevna.createpatina.registry.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class PatinaSet {

    private static final List<PatinaSet> ALL = new ArrayList<>();

    private final Map<WeatheringType, NonNullSupplier<? extends Block>> entries;

    public PatinaSet(Map<WeatheringType, ? extends NonNullSupplier<? extends Block>> entries) {
        EnumMap<WeatheringType, NonNullSupplier<? extends Block>> copy = new EnumMap<>(WeatheringType.class);
        copy.putAll(entries);
        if (copy.size() != WeatheringType.values().length) {
            throw new IllegalArgumentException("A PatinaSet must contain every weathering and waxed variant");
        }
        this.entries = Collections.unmodifiableMap(copy);
        ALL.add(this);
    }

    public static List<PatinaSet> all() {
        return List.copyOf(ALL);
    }

    public Map<WeatheringType, NonNullSupplier<? extends Block>> entries() {
        return entries;
    }

    public Block get(WeatheringType type) {
        return getSupplier(type).get();
    }

    public NonNullSupplier<? extends Block> getSupplier(WeatheringType type) {
        NonNullSupplier<? extends Block> supplier = entries.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Cannot find block for type " + type);
        }
        return supplier;
    }

    /**
     * Returns the Registrate entry backing a mod-owned patina set.
     * Vanilla entries in {@link DefaultPatinaSets} are available through {@link #getSupplier(WeatheringType)}
     * or {@link #get(WeatheringType)} instead.
     */
    public BlockEntry<? extends Block> getEntry(WeatheringType type) {
        NonNullSupplier<? extends Block> supplier = getSupplier(type);
        if (supplier instanceof BlockEntry<?> entry) {
            return entry;
        }
        throw new IllegalStateException("The " + type + " variant is not backed by a Registrate BlockEntry");
    }

    @SuppressWarnings("unchecked")
    public <B extends Block> BlockEntry<B> getEntry(WeatheringType type, Class<B> clazz) {
        BlockEntry<? extends Block> entry = getEntry(type);
        if (clazz.isInstance(entry.get())) {
            return (BlockEntry<B>) entry;
        }
        throw new IllegalArgumentException("Cannot find block entry for type " + type + ". Type mismatch");
    }

    @SuppressWarnings("unchecked")
    public NonNullSupplier<? extends Block>[] getAllEntries() {
        return entries.values().toArray(size -> (NonNullSupplier<? extends Block>[]) new NonNullSupplier[size]);
    }

    public boolean has(BlockEntry<? extends Block> entry) {
        return entries.containsValue(entry);
    }

    public boolean has(BlockState state) {
        return entries.values().stream().anyMatch(entry -> entry.get() == state.getBlock());
    }

    public boolean isIn(ItemStack stack) {
        return entries.values().stream().anyMatch(entry -> stack.is(entry.get().asItem()));
    }
}

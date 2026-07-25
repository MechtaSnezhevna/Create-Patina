package io.github.mechtasnezhevna.createpatina.registry.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public record PatinaSet (
        Map<WeatheringType, BlockEntry<? extends Block>> entries
) {

    public Block get(WeatheringType type) {
        return getEntry(type).get();
    }

    public BlockEntry<? extends Block> getEntry(WeatheringType type) {
        return entries.get(type);
    }

    @SuppressWarnings("unchecked")
    public <B extends Block> BlockEntry<B> getEntry(WeatheringType type, Class<B> clazz) {
        if (clazz.isInstance(entries.get(type).get())) {
            return (BlockEntry<B>) (entries.get(type));
        }
        throw new IllegalArgumentException("Cannot find block entry for type " + type + ". Type Mismatch");
    }

    @SuppressWarnings("unchecked")
    public NonNullSupplier<? extends Block>[] getAllEntries() {
        return entries.values().stream()
                .map(entry -> (NonNullSupplier<? extends Block>) entry)
                .toArray(size -> (NonNullSupplier<? extends Block>[]) new NonNullSupplier[size]);
    }

    public boolean has(BlockEntry<? extends Block> entry) {
        return entries.containsValue(entry);
    }

    public boolean has(BlockState state) {
        for (BlockEntry<? extends Block> entry : entries.values()) {
            if (entry.has(state)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIn(ItemStack stack) {
        for (BlockEntry<? extends Block> entry : entries.values()) {
            if (entry.isIn(stack)) {
                return true;
            }
        }
        return false;
    }

}
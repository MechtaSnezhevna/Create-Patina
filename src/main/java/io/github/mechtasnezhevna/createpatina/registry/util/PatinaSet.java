package io.github.mechtasnezhevna.createpatina.registry.util;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public record PatinaSet<B extends Block> (
        Map<WeatheringType, BlockEntry<B>> entries
) {

    public Block get(WeatheringType type) {
        return getEntry(type).get();
    }

    public BlockEntry<? extends Block> getEntry(WeatheringType type) {
        return entries.get(type);
    }

    @SuppressWarnings("unchecked")
    public NonNullSupplier<? extends Block>[] getAllEntries() {
        return entries.values().stream()
                .map(entry -> (NonNullSupplier<? extends Block>) entry)
                .toArray(size -> (NonNullSupplier<? extends Block>[]) new NonNullSupplier[size]);
    }
}

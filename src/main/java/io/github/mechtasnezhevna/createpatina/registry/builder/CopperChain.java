package io.github.mechtasnezhevna.createpatina.registry.builder;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class CopperChain<B extends Block & PatinaBlock> {

    private final Map<WeatheringType, BlockEntry<B>> entries;

    private final BlockEntry<? extends Block> unaffected;

    public CopperChain(Map<WeatheringType, BlockEntry<B>> entries, BlockEntry<? extends Block> unaffected) {
        this.unaffected = unaffected;
        this.entries = entries;
    }

    public Block get(WeatheringType type) {
        return getEntry(type).get();
    }

    public BlockEntry<? extends Block> getEntry(WeatheringType type) {
        if (type == WeatheringType.UNAFFECTED) {
            return unaffected;
        }
        return entries.get(type);
    }

    @SuppressWarnings("unchecked")
    public NonNullSupplier<? extends Block>[] getAllEntries() {
        return entries.values().stream()
                .map(entry -> (NonNullSupplier<? extends Block>) entry)
                .toArray(size -> (NonNullSupplier<? extends Block>[]) new NonNullSupplier[size]);
    }
}

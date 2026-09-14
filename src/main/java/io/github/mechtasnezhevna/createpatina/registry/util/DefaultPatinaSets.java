package io.github.mechtasnezhevna.createpatina.registry.util;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.block.CopperBlockSet;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class DefaultPatinaSets {

    /*
     * Minecraft 1.20.1 only ships the copper block family that was introduced in 1.17: the full
     * block and the cut copper block, stairs and slab. Chiseled copper, copper grates, bulbs,
     * doors and trapdoors were added in 1.21, so those vanilla sets do not exist on this branch.
     */
    public static final PatinaSet COPPER_FULL_BLOCK = vanilla(
            Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER,
            Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER
    );
    public static final PatinaSet CUT_COPPER = vanilla(
            Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER,
            Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER
    );
    public static final PatinaSet CUT_COPPER_STAIRS = vanilla(
            Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
            Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS
    );
    public static final PatinaSet CUT_COPPER_SLAB = vanilla(
            Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB,
            Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
    );

    public static final PatinaSet COPPER_SHINGLES = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.BlockVariant.INSTANCE);
    public static final PatinaSet COPPER_SHINGLE_STAIRS = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.StairVariant.INSTANCE);
    public static final PatinaSet COPPER_SHINGLE_SLAB = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.SlabVariant.INSTANCE);
    public static final PatinaSet COPPER_TILES = create(AllBlocks.COPPER_TILES, CopperBlockSet.BlockVariant.INSTANCE);
    public static final PatinaSet COPPER_TILE_STAIRS = create(AllBlocks.COPPER_TILES, CopperBlockSet.StairVariant.INSTANCE);
    public static final PatinaSet COPPER_TILE_SLAB = create(AllBlocks.COPPER_TILES, CopperBlockSet.SlabVariant.INSTANCE);

    private static final List<PatinaSet> ALL = List.of(
            COPPER_FULL_BLOCK, CUT_COPPER, CUT_COPPER_STAIRS, CUT_COPPER_SLAB,
            COPPER_SHINGLES, COPPER_SHINGLE_STAIRS, COPPER_SHINGLE_SLAB,
            COPPER_TILES, COPPER_TILE_STAIRS, COPPER_TILE_SLAB
    );

    private DefaultPatinaSets() {
    }

    public static void register() {
        // Triggers class initialization and PatinaSet registration.
    }

    public static boolean contains(PatinaSet set) {
        return ALL.contains(set);
    }

    private static PatinaSet vanilla(Block unaffected, Block exposed, Block weathered, Block oxidized,
                                     Block waxed, Block waxedExposed, Block waxedWeathered, Block waxedOxidized) {
        Map<WeatheringType, NonNullSupplier<? extends Block>> entries = new EnumMap<>(WeatheringType.class);
        entries.put(WeatheringType.UNAFFECTED, () -> unaffected);
        entries.put(WeatheringType.EXPOSED, () -> exposed);
        entries.put(WeatheringType.WEATHERED, () -> weathered);
        entries.put(WeatheringType.OXIDIZED, () -> oxidized);
        entries.put(WeatheringType.WAXED, () -> waxed);
        entries.put(WeatheringType.WAXED_EXPOSED, () -> waxedExposed);
        entries.put(WeatheringType.WAXED_WEATHERED, () -> waxedWeathered);
        entries.put(WeatheringType.WAXED_OXIDIZED, () -> waxedOxidized);
        return new PatinaSet(entries);
    }

    private static PatinaSet create(CopperBlockSet set, CopperBlockSet.Variant<?> variant) {
        Map<WeatheringType, NonNullSupplier<? extends Block>> entries = new EnumMap<>(WeatheringType.class);
        for (WeatheringType type : WeatheringType.values()) {
            entries.put(type, set.get(variant, type.getWeatherState(), type.isWaxed()));
        }
        return new PatinaSet(entries);
    }
}
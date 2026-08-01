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
    public static final PatinaSet CHISELED_COPPER = vanilla(
            Blocks.CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER,
            Blocks.WAXED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER
    );
    public static final PatinaSet COPPER_GRATE = vanilla(
            Blocks.COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE, Blocks.OXIDIZED_COPPER_GRATE,
            Blocks.WAXED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER_GRATE
    );
    public static final PatinaSet COPPER_BULB = vanilla(
            Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB,
            Blocks.WAXED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB
    );
    public static final PatinaSet COPPER_DOOR = vanilla(
            Blocks.COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_DOOR,
            Blocks.WAXED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_DOOR
    );
    public static final PatinaSet COPPER_TRAPDOOR = vanilla(
            Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR,
            Blocks.WAXED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR
    );

    public static final PatinaSet COPPER_SHINGLES = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.BlockVariant.INSTANCE);
    public static final PatinaSet COPPER_SHINGLE_STAIRS = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.StairVariant.INSTANCE);
    public static final PatinaSet COPPER_SHINGLE_SLAB = create(AllBlocks.COPPER_SHINGLES, CopperBlockSet.SlabVariant.INSTANCE);
    public static final PatinaSet COPPER_TILES = create(AllBlocks.COPPER_TILES, CopperBlockSet.BlockVariant.INSTANCE);
    public static final PatinaSet COPPER_TILE_STAIRS = create(AllBlocks.COPPER_TILES, CopperBlockSet.StairVariant.INSTANCE);
    public static final PatinaSet COPPER_TILE_SLAB = create(AllBlocks.COPPER_TILES, CopperBlockSet.SlabVariant.INSTANCE);

    private static final List<PatinaSet> ALL = List.of(
            COPPER_FULL_BLOCK, CUT_COPPER, CUT_COPPER_STAIRS, CUT_COPPER_SLAB,
            CHISELED_COPPER, COPPER_GRATE, COPPER_BULB, COPPER_DOOR, COPPER_TRAPDOOR,
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

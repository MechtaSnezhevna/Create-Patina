package io.github.mechtasnezhevna.createpatina.registry.util;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/**
 * Fan processing catalyst tags, mirroring Create's {@code create:fan_processing_catalysts/*}
 * convention so that both fluids and ordinary blocks can act as catalysts.
 */
public final class PatinaFanProcessingCatalystTags {

    private static final String HONEYING_PATH = "fan_processing_catalysts/honeying";

    public static final TagKey<Fluid> HONEYING_FLUID = TagKey.create(Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath(CreatePatina.MODID, HONEYING_PATH));

    public static final TagKey<Block> HONEYING_BLOCK = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(CreatePatina.MODID, HONEYING_PATH));

    private PatinaFanProcessingCatalystTags() {
    }
}

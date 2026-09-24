package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaFanProcessingCatalystTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

/**
 * Generates the fan processing catalyst tags, mirroring Create's {@code create:fan_processing_catalysts/*}
 * convention so that ordinary blocks can act as catalysts in addition to fluids.
 */
public final class PatinaFanProcessingCatalystTagGen {

    private PatinaFanProcessingCatalystTagGen() {
    }

    public static void addGenerators() {
        CreatePatina.registrate()
                .addDataGenerator(ProviderType.BLOCK_TAGS, PatinaFanProcessingCatalystTagGen::genBlockTags);
        CreatePatina.registrate()
                .addDataGenerator(ProviderType.FLUID_TAGS, PatinaFanProcessingCatalystTagGen::genFluidTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        provIn.addTag(PatinaFanProcessingCatalystTags.HONEYING_BLOCK)
                .add(Blocks.HONEY_BLOCK.builtInRegistryHolder().key());
        // A solid catalyst block must also let the fan's air stream pass through.
        provIn.addTag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
                .add(Blocks.HONEY_BLOCK.builtInRegistryHolder().key());
    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
        provIn.addTag(PatinaFanProcessingCatalystTags.HONEYING_FLUID)
                .addTag(Tags.Fluids.HONEY);
    }
}

package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.block.WeatheringEncasedPipeBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;

public class WeatheringEncasedPipeGenerator {

    public static void genModel(DataGenContext<Block, WeatheringEncasedPipeBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType casingType,
                                WeatheringType pipeType,
                                String familyName) {
        String name = ctx.getName();
        String casingPrefix = casingType.getPrefixWithoutWaxed();
        String pipePrefix = pipeType.getPrefixWithoutWaxed();
        String modelPath = "block/encased_pipe/" + familyName + "/" + name;

        genFlatModel(prov, modelPath, casingPrefix);
        genOpenModel(prov, modelPath, casingPrefix, pipePrefix);

        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(
                NonNullSupplier.of(ctx::getEntry), modelPath.substring("block/".length()), ctx.getId());
        BlockStateGen.encasedPipe().accept(cast, prov);
    }

    private static void genFlatModel(RegistrateBlockstateProvider prov,
                                     String modelPath,
                                     String casingPrefix) {
        prov.models().withExistingParent(modelPath + "/block_flat",
                        Create.asResource("block/encased_fluid_pipe/block_flat"))
                .texture("0", prov.modLoc("block/casing/" + casingPrefix + "copper_casing"))
                .texture("particle", prov.modLoc("block/casing/" + casingPrefix + "copper_casing"));
    }

    private static void genOpenModel(RegistrateBlockstateProvider prov,
                                     String modelPath,
                                     String casingPrefix,
                                     String pipePrefix) {
        prov.models().withExistingParent(modelPath + "/block_open",
                        Create.asResource("block/encased_fluid_pipe/block_open"))
                .texture("0", prov.modLoc("block/encased_pipe/" + casingPrefix + "encased_" + pipePrefix + "pipe"))
                .texture("particle", prov.modLoc("block/encased_pipe/" + casingPrefix + "encased_" + pipePrefix + "pipe"));
    }
}

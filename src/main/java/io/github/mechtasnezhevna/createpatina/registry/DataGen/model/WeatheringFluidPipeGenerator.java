package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.block.WeatheringFluidPipeBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;

public class WeatheringFluidPipeGenerator {

    public static void genModel(DataGenContext<Block, WeatheringFluidPipeBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();
        String copperBlockPath = "block/" + prefix + "copper" + (prefix.isEmpty() ? "_block" : "");

        DataGenContext<Block, FluidPipeBlock> cast = new DataGenContext<>(
                NonNullSupplier.of(ctx::getEntry), "fluid_pipe/" + name, ctx.getId());

        genCoreModels(prov, name, prefix, copperBlockPath);
        genConnectionModels(prov, name, prefix);
        genCasingModel(prov, name, prefix, copperBlockPath);
        genBlockItemModel(prov, name, prefix, copperBlockPath);
        genWindowModel(prov, name, prefix, copperBlockPath);

        BlockStateGen.pipe().accept(cast, prov);
    }

    private static void genCoreModels(RegistrateBlockstateProvider prov,
                                      String name,
                                      String prefix,
                                      String copperBlockPath) {
        for (String axis : new String[]{"x", "y", "z"}) {
            prov.models().withExistingParent("block/fluid_pipe/" + name + "/core_" + axis,
                            Create.asResource("block/fluid_pipe/core_" + axis))
                    .texture("0", prov.modLoc("block/fluid_pipe/" + prefix + "pipes_connected"))
                    .texture("particle", prov.mcLoc(copperBlockPath));
        }
    }

    private static void genConnectionModels(RegistrateBlockstateProvider prov,
                                            String name,
                                            String prefix) {
        for (String part : new String[]{"connection/", "drain/", "rim/", "rim_connector/"}) {
            for (String direction : new String[]{"down", "up", "north", "south", "west", "east"}) {
                prov.models().withExistingParent("block/fluid_pipe/" + name + "/" + part + direction,
                                Create.asResource("block/fluid_pipe/" + part + direction))
                        .texture("0", prov.modLoc("block/fluid_pipe/" + prefix + "pipes"))
                        .texture("particle", prov.modLoc("block/fluid_pipe/" + prefix + "pipes"));
            }
        }
    }

    private static void genCasingModel(RegistrateBlockstateProvider prov,
                                       String name,
                                       String prefix,
                                       String copperBlockPath) {
        prov.models().withExistingParent("block/fluid_pipe/" + name + "/casing",
                        Create.asResource("block/fluid_pipe/casing"))
                .texture("0", prov.modLoc("block/fluid_pipe/" + prefix + "pipes"))
                .texture("particle", prov.mcLoc(copperBlockPath));
    }

    private static void genBlockItemModel(RegistrateBlockstateProvider prov,
                                          String name,
                                          String prefix,
                                          String copperBlockPath) {
        prov.models().withExistingParent("block/fluid_pipe/" + name + "/item",
                        Create.asResource("block/fluid_pipe/item"))
                .texture("1", prov.modLoc("block/fluid_pipe/" + prefix + "pipes"))
                .texture("particle", prov.mcLoc(copperBlockPath));
    }

    private static void genWindowModel(RegistrateBlockstateProvider prov,
                                       String name,
                                       String prefix,
                                       String copperBlockPath) {
        prov.models().withExistingParent("block/fluid_pipe/" + name + "/window",
                        Create.asResource("block/fluid_pipe/window"))
                .texture("0", prov.modLoc("block/fluid_pipe/" + prefix + "glass_fluid_pipe"))
                .texture("particle", prov.mcLoc(copperBlockPath));
    }
}

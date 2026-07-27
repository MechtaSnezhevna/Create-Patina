package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.EAST;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.NORTH;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.SOUTH;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WEST;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WeatheringBarsGenerator {

    public static <P extends IronBarsBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> barsBlockState(
            String name, boolean specialEdge) {
        return (c, p) -> {
            ModelFile post_ends = barsSubModel(p, name, "post_ends", specialEdge);
            ModelFile post = barsSubModel(p, name, "post", specialEdge);
            ModelFile cap = barsSubModel(p, name, "cap", specialEdge);
            ModelFile cap_alt = barsSubModel(p, name, "cap_alt", specialEdge);
            ModelFile side = barsSubModel(p, name, "side", specialEdge);
            ModelFile side_alt = barsSubModel(p, name, "side_alt", specialEdge);
            p.getMultipartBuilder(c.get())
                    .part()
                    .modelFile(post_ends)
                    .addModel()
                    .end()
                    .part()
                    .modelFile(post)
                    .addModel()
                    .condition(NORTH, false)
                    .condition(EAST, false)
                    .condition(SOUTH, false)
                    .condition(WEST, false)
                    .end()
                    .part()
                    .modelFile(cap)
                    .addModel()
                    .condition(NORTH, true)
                    .condition(EAST, false)
                    .condition(SOUTH, false)
                    .condition(WEST, false)
                    .end()
                    .part()
                    .modelFile(cap)
                    .rotationY(90)
                    .addModel()
                    .condition(NORTH, false)
                    .condition(EAST, true)
                    .condition(SOUTH, false)
                    .condition(WEST, false)
                    .end()
                    .part()
                    .modelFile(cap_alt)
                    .addModel()
                    .condition(NORTH, false)
                    .condition(EAST, false)
                    .condition(SOUTH, true)
                    .condition(WEST, false)
                    .end()
                    .part()
                    .modelFile(cap_alt)
                    .rotationY(90)
                    .addModel()
                    .condition(NORTH, false)
                    .condition(EAST, false)
                    .condition(SOUTH, false)
                    .condition(WEST, true)
                    .end()
                    .part()
                    .modelFile(side)
                    .addModel()
                    .condition(NORTH, true)
                    .end()
                    .part()
                    .modelFile(side)
                    .rotationY(90)
                    .addModel()
                    .condition(EAST, true)
                    .end()
                    .part()
                    .modelFile(side_alt)
                    .addModel()
                    .condition(SOUTH, true)
                    .end()
                    .part()
                    .modelFile(side_alt)
                    .rotationY(90)
                    .addModel()
                    .condition(WEST, true)
                    .end();
        };
    }

    private static ModelFile barsSubModel(RegistrateBlockstateProvider p, String name, String suffix,
                                          boolean specialEdge) {
        String tName = name.replace("waxed_","");
        ResourceLocation barsTexture = p.modLoc("block/bars/" + tName + "_bars");
        ResourceLocation edgeTexture = specialEdge ? p.modLoc("block/bars/" + tName + "_bars_edge") : barsTexture;

        ResourceLocation parentModel = Create.asResource("block/bars/" + suffix);

        return p.models()
                .withExistingParent("block/bars/" + name + "_" + suffix, parentModel)
                .texture("bars", barsTexture)
                .texture("particle", barsTexture)
                .texture("edge", edgeTexture);
    }
}
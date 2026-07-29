package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringCopperScaffoldingGenerator {

    public static void genModel(DataGenContext<Block, MetalScaffoldingBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        prov.getVariantBuilder(ctx.get())
                .forAllStatesExcept(state -> {
                    String suffix = state.getValue(MetalScaffoldingBlock.BOTTOM) ? "_horizontal" : "";
                    return ConfiguredModel.builder()
                            .modelFile(genStateModel(prov, ctx.getName(), type.getPrefixWithoutWaxed(), suffix))
                            .build();
                }, MetalScaffoldingBlock.WATERLOGGED, MetalScaffoldingBlock.DISTANCE);
    }

    private static ModelFile genStateModel(RegistrateBlockstateProvider prov,
                                           String name,
                                           String prefix,
                                           String suffix) {
        return prov.models()
                .withExistingParent("block/copper_scaffold/" + name + suffix,
                        Create.asResource("block/scaffold/block" + suffix))
                .texture("top", prov.modLoc("block/scaffold/" + prefix + "copper_funnel_frame"))
                .texture("inside", prov.modLoc("block/scaffold/" + prefix + "copper_scaffold_inside"))
                .texture("side", prov.modLoc("block/scaffold/" + prefix + "copper_scaffold"))
                .texture("casing", prov.modLoc("block/casing/" + prefix + "copper_casing"))
                .texture("particle", prov.modLoc("block/scaffold/" + prefix + "copper_scaffold"));
    }
}

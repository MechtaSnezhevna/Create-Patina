package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringSpoutGenerator {

    public static void genModel(DataGenContext<Block, SpoutBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();

        prov.simpleBlock(ctx.get(), genBaseModel(prov, name, prefix));
        genBottomModel(prov, name, prefix);
    }

    private static ModelFile genBaseModel(RegistrateBlockstateProvider prov,
                                          String name,
                                          String prefix) {
        return prov.models()
                .withExistingParent("block/spout/" + name + "/block", Create.asResource("block/spout/block"))
                .texture("particle", prov.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("0", prov.modLoc("block/spout/" + prefix + "spout"))
                .texture("3", prov.modLoc("block/encased_pipe/" + prefix + "encased_" + prefix + "pipe"));
    }

    private static void genBottomModel(RegistrateBlockstateProvider prov,
                                       String name,
                                       String prefix) {
        prov.models().withExistingParent("block/spout/" + name + "/bottom", Create.asResource("block/spout/bottom"))
                .texture("2", prov.modLoc("block/spout/" + prefix + "spout_nozzle"));
    }

    public static void genItemModel(DataGenContext<Item, ? extends BlockItem> ctx,
                                    RegistrateItemModelProvider prov,
                                    WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();
        prov.withExistingParent(name, Create.asResource("block/spout/item"))
                .texture("particle", prov.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("0", prov.modLoc("block/spout/" + prefix + "spout"))
                .texture("4", prov.modLoc("block/encased_pipe/" + prefix + "encased_" + prefix + "pipe"))
                .texture("3", prov.modLoc("block/spout/" + prefix + "spout_nozzle"));
    }
}

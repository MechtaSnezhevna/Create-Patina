package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WeatheringCopperDoorGenerator {

    public static void genModel(DataGenContext<Block, SlidingDoorBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();

        ModelFile bottom = genDoorHalfModel(prov, name, prefix, "bottom");
        ModelFile top = genDoorHalfModel(prov, name, prefix, "top");
        prov.doorBlock(ctx.get(), bottom, bottom, bottom, bottom, top, top, top, top);

        genFoldModel(prov, name, prefix, "left");
        genFoldModel(prov, name, prefix, "right");
    }

    private static ModelFile genDoorHalfModel(RegistrateBlockstateProvider prov,
                                              String name,
                                              String prefix,
                                              String half) {
        return prov.models().withExistingParent("block/copper_door/" + name + "/block_" + half,
                        Create.asResource("block/copper_door/block_" + half))
                .texture("0", prov.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                .texture("2", prov.modLoc("block/copper_door/" + prefix + "copper_door_" + half))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }

    private static void genFoldModel(RegistrateBlockstateProvider prov,
                                     String name,
                                     String prefix,
                                     String side) {
        prov.models().withExistingParent("block/copper_door/" + name + "/fold_" + side,
                        Create.asResource("block/copper_door/fold_" + side))
                .texture("0", prov.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                .texture("3", prov.modLoc("block/copper_door/" + prefix + "copper_door_bottom"))
                .texture("2", prov.modLoc("block/copper_door/" + prefix + "copper_door_top"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }
}

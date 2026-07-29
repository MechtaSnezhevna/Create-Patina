package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringHosePulleyGenerator {

    public static void genModel(DataGenContext<Block, HosePulleyBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();

        prov.horizontalBlock(ctx.get(), genBaseModel(prov, name, prefix));
        genPulleyMagnetModel(prov, name, prefix);
        genRopeHalfMagnetModel(prov, name, prefix);
    }

    private static ModelFile genBaseModel(RegistrateBlockstateProvider prov,
                                          String name,
                                          String prefix) {
        return prov.models()
                .withExistingParent("block/hose_pulley/" + name + "/block",
                        Create.asResource("block/hose_pulley/block"))
                .texture("1", prov.modLoc("block/hose_pulley/" + prefix + "hose_pulley"))
                .texture("3", prov.modLoc("block/pump/" + prefix + "pump"))
                .texture("partical", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_inner"));
    }

    private static void genPulleyMagnetModel(RegistrateBlockstateProvider prov,
                                             String name,
                                             String prefix) {
        prov.models().withExistingParent("block/hose_pulley/" + name + "/pulley_magnet",
                        Create.asResource("block/hose_pulley/pulley_magnet"))
                .texture("0", prov.modLoc("block/hose_pulley/" + prefix + "hose"))
                .texture("particle", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_inner"));
    }

    private static void genRopeHalfMagnetModel(RegistrateBlockstateProvider prov,
                                               String name,
                                               String prefix) {
        prov.models().withExistingParent("block/hose_pulley/" + name + "/rope_half_magnet",
                        Create.asResource("block/hose_pulley/rope_half_magnet"))
                .texture("0", prov.modLoc("block/hose_pulley/" + prefix + "hose"))
                .texture("particle", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_inner"));
    }

    public static void genItemModel(DataGenContext<Item, BlockItem> ctx,
                                    RegistrateItemModelProvider prov,
                                    WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();
        prov.withExistingParent(name, Create.asResource("block/hose_pulley/item"))
                .texture("1", prov.modLoc("block/hose_pulley/" + prefix + "hose_pulley"))
                .texture("3", prov.modLoc("block/pump/" + prefix + "pump"))
                .texture("partical", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_inner"));
    }
}

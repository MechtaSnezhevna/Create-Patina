package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPortableStorageInterfaceBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WeatheringPortableFluidInterfaceGenerator {

    public static void genModel(DataGenContext<Block, WeatheringPortableStorageInterfaceBlock> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();

        prov.directionalBlock(ctx.get(), genBaseModel(prov, name, prefix));
        genTopModel(prov, name, prefix);
        genMiddleModel(prov, name, prefix);
        genPoweredMiddleModel(prov, name, prefix);
    }

    private static ModelFile genBaseModel(RegistrateBlockstateProvider prov,
                                          String name,
                                          String prefix) {
        return prov.models()
                .withExistingParent("block/portable_fluid_interface/" + name + "/block",
                        Create.asResource("block/portable_fluid_interface/block"))
                .texture("0", prov.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                .texture("2", prov.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }

    private static void genTopModel(RegistrateBlockstateProvider prov,
                                    String name,
                                    String prefix) {
        prov.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_top",
                        Create.asResource("block/portable_fluid_interface/block_top"))
                .texture("0", prov.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }

    private static void genMiddleModel(RegistrateBlockstateProvider prov,
                                       String name,
                                       String prefix) {
        prov.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_middle",
                        Create.asResource("block/portable_fluid_interface/block_middle"))
                .texture("2", prov.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }

    private static void genPoweredMiddleModel(RegistrateBlockstateProvider prov,
                                              String name,
                                              String prefix) {
        prov.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_middle_powered",
                        Create.asResource("block/portable_fluid_interface/block_middle_powered"))
                .texture("0", prov.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }

    public static void genItemModel(DataGenContext<Item, BlockItem> ctx,
                                    RegistrateItemModelProvider prov,
                                    WeatheringType type) {
        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();
        prov.withExistingParent(name, Create.asResource("block/portable_fluid_interface/item"))
                .texture("0", prov.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                .texture("2", prov.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("particle", prov.modLoc("block/casing/" + prefix + "copper_casing"));
    }
}

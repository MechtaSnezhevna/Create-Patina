package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringSteamEngineGenerator {

    public static void genModel(DataGenContext<Block, ?> ctx,
                                RegistrateBlockstateProvider prov,
                                WeatheringType type) {

        String name = ctx.getName();
        String prefix = type.getPrefixWithoutWaxed();

        prov.horizontalFaceBlock(ctx.get(), genBaseModel(prov, name, prefix));
        genBoilerGauge(prov, name, prefix);
        genBoilerGaugeDial(prov, name, prefix);
    }

    private static ModelFile genBaseModel(RegistrateBlockstateProvider prov,
                                          String name,
                                          String prefix) {
        return prov.models()
                .withExistingParent("block/steam_engine/" + name + "/block",
                        Create.asResource("block/steam_engine/block"))
                .texture("particle", prov.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("1", prov.modLoc("block/steam_engine/" + prefix + "engine"));
    }

    public static void genItemModel(DataGenContext<Item, BlockItem> c,
                                    RegistrateItemModelProvider p,
                                    WeatheringType type) {
        String name = c.getName();
        String prefix = type.getPrefixWithoutWaxed();
        p.withExistingParent(name, Create.asResource("block/steam_engine/item"))
                .texture("particle", p.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("1", p.modLoc("block/steam_engine/" + prefix + "engine"));

    }


    private static void genBoilerGauge(RegistrateBlockstateProvider prov,
                                       String name, String prefix) {
        prov.models()
                .withExistingParent("block/steam_engine/" + name + "/gauge",
                        Create.asResource("block/steam_engine/gauge"))
                .texture("0", prov.modLoc("block/steam_engine/" + prefix + "boiler_gauge"))
                .texture("particle", prov.modLoc("block/steam_engine/" + prefix + "boiler_gauge"));

    }

    private static void genBoilerGaugeDial(RegistrateBlockstateProvider prov,
                                       String name, String prefix) {
        prov.models()
                .withExistingParent("block/steam_engine/" + name + "/gauge_dial",
                        Create.asResource("block/steam_engine/gauge_dial"))
                .texture("0", prov.modLoc("block/steam_engine/" + prefix + "boiler_gauge"))
                .texture("particle", prov.modLoc("block/steam_engine/" + prefix + "boiler_gauge"));

    }
}

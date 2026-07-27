package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;


import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.HashMap;
import java.util.Map;

public class WeatheringFluidValveGenerator {

    public static void genModel(DataGenContext<Block, FluidValveBlock> c,
                                RegistrateBlockstateProvider p,
                                WeatheringType type) {
        String prefix = type.getPrefixWithoutWaxed();
        Map<String, ModelFile> modelMap = new HashMap<>();

        genValveModels(c, p, prefix, modelMap);
        genBlockState(c, p, modelMap);
        genPointerModel(c, p, prefix);
    }

    private static void genValveModels(DataGenContext<Block, FluidValveBlock> c,
                                       RegistrateBlockstateProvider p,
                                       String prefix,
                                       Map<String, ModelFile> modelMap) {
        for (String dir : new String[]{"vertical", "horizontal"}) {
            for (String state : new String[]{"open", "closed"}) {
                String modelName = c.getName() + "_" + dir + "_" + state;
                ModelFile model = p.models().withExistingParent("block/fluid_valve/" + modelName,
                                Create.asResource("block/fluid_valve/block_" + dir + "_" + state))
                        .texture("2", p.modLoc("block/valve/" + prefix + "fluid_valve"))
                        .texture("4", p.modLoc("block/valve/" + prefix + "valve_" + state))
                        .texture("3", p.modLoc("block/valve/" + prefix + "valve_" + state))
                        .texture("particle", p.modLoc("block/general/" + prefix + "copper_underside"));
                modelMap.put(modelName, model);
            }
        }
    }

    private static void genBlockState(DataGenContext<Block, FluidValveBlock> c,
                                      RegistrateBlockstateProvider p,
                                      Map<String, ModelFile> modelMap) {
        BlockStateGen.directionalAxisBlock(c, p, (state, vertical) -> {
            boolean enabled = state.getValue(FluidValveBlock.ENABLED);
            String dir = vertical ? "vertical" : "horizontal";
            return modelMap.get(c.getName() + "_" + dir + "_" + (enabled ? "open" : "closed"));
        });
    }

    private static void genPointerModel(DataGenContext<Block, FluidValveBlock> c,
                                        RegistrateBlockstateProvider p,
                                        String prefix) {
        p.models().withExistingParent("block/fluid_valve/" + c.getName() + "_pointer",
                        Create.asResource("block/fluid_valve/pointer"))
                .texture("particle", p.modLoc("block/general/" + prefix + "copper_underside"))
                .texture("2", p.modLoc("block/valve/" + prefix + "fluid_valve"));
    }

    public static void genItemModel(DataGenContext<Item, BlockItem> c,
                                    RegistrateItemModelProvider p,
                                    WeatheringType type) {
        String name = c.getName();
        String prefix = type.getPrefixWithoutWaxed();
        p.withExistingParent(name, Create.asResource("block/fluid_valve/item"))
                .texture("2", p.modLoc("block/valve/" + prefix + "fluid_valve"))
                .texture("4", p.modLoc("block/valve/" + prefix + "valve_open"))
                .texture("3", p.modLoc("block/valve/" + prefix + "valve_closed"))
                .texture("particle", p.modLoc("block/valve/" + prefix + "valve_closed"));
    }
}

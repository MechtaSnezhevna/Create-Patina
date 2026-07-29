package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankGenerator;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringFluidTankGenerator extends FluidTankGenerator {

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        String name = ctx.getName();
        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);

        Boolean top = state.getValue(FluidTankBlock.TOP);
        Boolean bottom = state.getValue(FluidTankBlock.BOTTOM);
        FluidTankBlock.Shape shape = state.getValue(FluidTankBlock.SHAPE);

        String shapeName = "middle";
        if (top && bottom)
            shapeName = "single";
        else if (top)
            shapeName = "top";
        else if (bottom)
            shapeName = "bottom";

        String modelName = shapeName + (shape == FluidTankBlock.Shape.PLAIN ? "" : "_" + shape.getSerializedName());

        return prov.models()
                .withExistingParent( "block/fluid_tank/" + name + "/block_" + modelName,
                        Create.asResource("block/fluid_tank/block_" + modelName))
                .texture("0", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_top"))
                .texture("1", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank"))
                .texture("3", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_window"))
                .texture("4", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_inner"))
                .texture("5", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank_window_single"))
                .texture("particle", prov.modLoc("block/fluid_tank/" + prefix + "fluid_tank"));
    }

}

package io.github.mechtasnezhevna.createpatina.registry.DataGen;

import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WeatheringSmartFluidPipeGenerator extends SmartFluidPipeGenerator {

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String prefix = WeatheringType.getPrefixWithoutWaxedByName(ctx.getName());
        return prov.models()
                .withExistingParent("block/smart_fluid_pipe/" + ctx.getName(), prov.modLoc("block/smart_fluid_pipe/block"))
                .texture("2", prov.modLoc("block/" + prefix + "smart_pipe_1"))
                .texture("3", prov.modLoc("block/" + prefix + "smart_pipe_2"))
                .texture("4", prov.modLoc("block/" + prefix + "pipes"))
                .texture("5", prov.modLoc("block/" + prefix + "smart_pipe_3"))
                .texture("particle", prov.mcLoc("block/" + prefix + "copper" + (prefix.isEmpty() ? "_block" : "")));
    }
}
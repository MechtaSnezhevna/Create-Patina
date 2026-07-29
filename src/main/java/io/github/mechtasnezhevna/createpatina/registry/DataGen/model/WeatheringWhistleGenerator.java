package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleGenerator;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class WeatheringWhistleGenerator extends WhistleGenerator {

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String name = ctx.getName();
        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
        String wall = state.getValue(WhistleBlock.WALL) ? "wall" : "floor";
        String size = state.getValue(WhistleBlock.SIZE).getSerializedName();
        boolean powered = state.getValue(WhistleBlock.POWERED);
        ModelFile baseModel = prov.models()
                .withExistingParent("block/steam_whistle/" + name + "/block_" + size + "_" + wall,
                        Create.asResource("block/steam_whistle/block_" + size + "_" + wall))
                .texture("1", prov.modLoc("block/steam_engine/" + prefix + "engine"))
                .texture("2", prov.modLoc("block/general/" + prefix + "copper_redstone_plate"));
        if (!powered) {
            return baseModel;
        }
        return prov.models()
                .withExistingParent("block/steam_whistle/" + name + "/block_" + size + "_" + wall + "_powered",
                        baseModel.getLocation())
                .texture("2", prov.modLoc("block/general/" + prefix + "copper_redstone_plate_powered"));
    }

}

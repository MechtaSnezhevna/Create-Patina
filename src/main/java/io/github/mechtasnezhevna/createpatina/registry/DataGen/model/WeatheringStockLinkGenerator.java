package io.github.mechtasnezhevna.createpatina.registry.DataGen.model;

import com.simibubi.create.content.logistics.packagerLink.PackagerLinkBlock;
import com.simibubi.create.content.logistics.packagerLink.PackagerLinkGenerator;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class WeatheringStockLinkGenerator extends PackagerLinkGenerator {

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                BlockState state) {
        String name = ctx.getName();
        String variant =
                state.getValue(PackagerLinkBlock.FACE) == AttachFace.WALL ? "block_horizontal" : "block_vertical";
        if (state.getValue(PackagerLinkBlock.POWERED))
            variant += "_powered";
        return prov.models()
                .getExistingFile(prov.modLoc("block/stock_link/" + name + "/" + variant));
    }

}

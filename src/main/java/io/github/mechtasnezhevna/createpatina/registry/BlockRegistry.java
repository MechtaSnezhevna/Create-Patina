package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.WeatheringFluidPipeBlock;
import io.github.mechtasnezhevna.createpatina.registry.builder.CopperChain;
import io.github.mechtasnezhevna.createpatina.registry.builder.CopperChainBuilder;
import io.github.mechtasnezhevna.createpatina.util.PatinaStress;
import io.github.mechtasnezhevna.createpatina.block.WeatheringItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPumpBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("deprecation")
public class BlockRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final CopperChain<WeatheringItemDrainBlock> ITEM_DRAINS = new CopperChainBuilder<>(
            REGISTRATE, "item_drain", WeatheringItemDrainBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::randomTicks)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        var modelPath = p.modLoc("block/item_drain/" + c.getName());
                        p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
                    })
                    .simpleItem()
            ).unaffected(AllBlocks.ITEM_DRAIN)
            .register();

    private static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pumpBlockState() {
        return (c, p) ->
                BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, $ ->
                        p.models().getExistingFile(
                                p.modLoc("block/mechanical_pump/" + c.getName() + "/block")
                        )
                );
    }

    public static final CopperChain<WeatheringPumpBlock> MECHANICAL_PUMPS = new CopperChainBuilder<>(
            REGISTRATE, "mechanical_pump", WeatheringPumpBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.mapColor(MapColor.STONE))
                    .properties(BlockBehaviour.Properties::randomTicks)
                    .transform(pickaxeOnly())
                    .blockstate(pumpBlockState())
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .transform(PatinaStress.setImpact(4.0d))
                    .item()
                    .transform(customItemModel("mechanical_pump", "_", "item"))
            ).unaffected(AllBlocks.MECHANICAL_PUMP)
            .register();

    private static <P extends FluidPipeBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> pipeBlockState(String prefix) {
        return (c, p) -> {
            DataGenContext<Block, P> proxy = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                    prefix + c.getName(), c.getId());

            @SuppressWarnings("unchecked")
            DataGenContext<Block, FluidPipeBlock> cast = (DataGenContext<Block, FluidPipeBlock>) proxy;

            BlockStateGen.pipe().accept(cast, p);
        };
    }

    public static final CopperChain<WeatheringFluidPipeBlock> FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "fluid_pipe", WeatheringFluidPipeBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::forceSolidOff)
                    .transform(pickaxeOnly())
                    .blockstate(pipeBlockState("fluid_pipe/"))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .item()
                    .transform(customItemModel("fluid_pipe", "_", "item"))
            ).unaffected(AllBlocks.FLUID_PIPE)
            .register();

    public static void register() {
    }
}

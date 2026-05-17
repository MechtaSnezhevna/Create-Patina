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
import io.github.mechtasnezhevna.createpatina.registry.chain.CopperChain;
import io.github.mechtasnezhevna.createpatina.registry.chain.CopperChainBuilder;
import io.github.mechtasnezhevna.createpatina.block.WeatheringGlassFluidPipeBlock;
import io.github.mechtasnezhevna.createpatina.util.PatinaStress;
import io.github.mechtasnezhevna.createpatina.block.WeatheringItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringPumpBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

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

    public static final CopperChain<WeatheringPumpBlock> MECHANICAL_PUMPS = new CopperChainBuilder<>(
            REGISTRATE, "mechanical_pump", WeatheringPumpBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.mapColor(MapColor.STONE))
                    .properties(BlockBehaviour.Properties::randomTicks)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) ->
                            BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, $ ->
                                    p.models().getExistingFile(
                                            p.modLoc("block/mechanical_pump/" + c.getName() + "/block")
                                    )
                            ))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .transform(PatinaStress.setImpact(4.0d))
                    .item()
                    .transform(customItemModel("mechanical_pump", "_", "item"))
            ).unaffected(AllBlocks.MECHANICAL_PUMP)
            .register();


    public static final CopperChain<WeatheringFluidPipeBlock> FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "fluid_pipe", WeatheringFluidPipeBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::forceSolidOff)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, FluidPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.pipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .item()
                    .transform(customItemModel("fluid_pipe", "_", "item"))
            ).unaffected(AllBlocks.FLUID_PIPE)
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> EXPOSED_GLASS_FLUID_PIPE = REGISTRATE
            .block("exposed_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/exposed_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b) -> p.dropOther(b, EXPOSED_FLUID_PIPE.get()))
                    .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> WEATHERED_GLASS_FLUID_PIPE = REGISTRATE
            .block("weathered_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/weathered_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, WEATHERED_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> OXIDIZED_GLASS_FLUID_PIPE = REGISTRATE
            .block("oxidized_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/oxidized_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, OXIDIZED_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> WAXED_GLASS_FLUID_PIPE = REGISTRATE
            .block("waxed_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.WAXED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/waxed_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, WAXED_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> WAXED_EXPOSED_GLASS_FLUID_PIPE = REGISTRATE
            .block("waxed_exposed_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.WAXED_EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/waxed_exposed_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, WAXED_EXPOSED_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> WAXED_WEATHERED_GLASS_FLUID_PIPE = REGISTRATE
            .block("waxed_weathered_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.WAXED_WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/waxed_weathered_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, WAXED_WEATHERED_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<WeatheringGlassFluidPipeBlock> WAXED_OXIDIZED_GLASS_FLUID_PIPE = REGISTRATE
            .block("waxed_oxidized_glass_fluid_pipe", (properties) -> new WeatheringGlassFluidPipeBlock(WeatheringType.WAXED_OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/fluid_pipe/waxed_oxidized_fluid_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, WAXED_OXIDIZED_FLUID_PIPE.get()))
            .register();

    public static void register() {
    }
}

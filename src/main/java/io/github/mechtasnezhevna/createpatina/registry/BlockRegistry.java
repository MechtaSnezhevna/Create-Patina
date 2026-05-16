package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.WeatheringFluidPipeBlock;
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
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("deprecation")
public class BlockRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final BlockEntry<WeatheringItemDrainBlock> EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_ITEM_DRAIN = REGISTRATE
            .block("waxed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.WAXED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_EXPOSED_ITEM_DRAIN = REGISTRATE
            .block("waxed_exposed_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.WAXED_EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_WEATHERED_ITEM_DRAIN = REGISTRATE
            .block("waxed_weathered_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.WAXED_WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();

    public static final BlockEntry<WeatheringItemDrainBlock> WAXED_OXIDIZED_ITEM_DRAIN = REGISTRATE
            .block("waxed_oxidized_item_drain", (properties) -> new WeatheringItemDrainBlock(WeatheringType.WAXED_OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                var modelPath = p.modLoc("block/item_drain/" + c.getName());
                p.simpleBlock(c.get(), new ModelFile.UncheckedModelFile(modelPath));
            })
            .simpleItem()
            .register();


    private static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pumpBlockState() {
        return (c, p) ->
                BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, $ ->
                        p.models().getExistingFile(
                                p.modLoc("block/mechanical_pump/" + c.getName() + "/block")
                        )
                );
    }

    public static final BlockEntry<WeatheringPumpBlock> EXPOSED_MECHANICAL_PUMP = REGISTRATE
            .block("exposed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WEATHERED_MECHANICAL_PUMP = REGISTRATE
            .block("weathered_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> OXIDIZED_MECHANICAL_PUMP = REGISTRATE
            .block("oxidized_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.WAXED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_EXPOSED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_exposed_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.WAXED_EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_WEATHERED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_weathered_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.WAXED_WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringPumpBlock> WAXED_OXIDIZED_MECHANICAL_PUMP = REGISTRATE
            .block("waxed_oxidized_mechanical_pump", (properties) -> new WeatheringPumpBlock(WeatheringType.WAXED_OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.STONE))
            .properties(BlockBehaviour.Properties::randomTicks)
            .transform(pickaxeOnly())
            .blockstate(pumpBlockState())
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(PatinaStress.setImpact(4.0d))
            .item()
            .transform(customItemModel("mechanical_pump", "_", "item"))
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

    public static final BlockEntry<WeatheringFluidPipeBlock> EXPOSED_FLUID_PIPE = REGISTRATE
            .block("exposed_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> WEATHERED_FLUID_PIPE = REGISTRATE
            .block("weathered_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> OXIDIZED_FLUID_PIPE = REGISTRATE
            .block("oxidized_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> WAXED_FLUID_PIPE = REGISTRATE
            .block("waxed_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.WAXED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> WAXED_EXPOSED_FLUID_PIPE = REGISTRATE
            .block("waxed_exposed_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.WAXED_EXPOSED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> WAXED_WEATHERED_FLUID_PIPE = REGISTRATE
            .block("waxed_weathered_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.WAXED_WEATHERED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
            .register();

    public static final BlockEntry<WeatheringFluidPipeBlock> WAXED_OXIDIZED_FLUID_PIPE = REGISTRATE
            .block("waxed_oxidized_fluid_pipe", (properties) -> new WeatheringFluidPipeBlock(WeatheringType.WAXED_OXIDIZED, properties))
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::forceSolidOff)
            .transform(pickaxeOnly())
            .blockstate(pipeBlockState("fluid_pipe/"))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel("fluid_pipe", "_", "item"))
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

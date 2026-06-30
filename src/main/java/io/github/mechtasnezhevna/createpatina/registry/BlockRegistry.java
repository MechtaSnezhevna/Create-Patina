package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.*;
import io.github.mechtasnezhevna.createpatina.registry.chain.CopperChain;
import io.github.mechtasnezhevna.createpatina.registry.chain.CopperChainBuilder;
import io.github.mechtasnezhevna.createpatina.util.PatinaStress;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings({"deprecation", "removal"})
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

    public static final CopperChain<WeatheringGlassFluidPipeBlock> GLASS_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "glass_fluid_pipe", WeatheringGlassFluidPipeBlock::new)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.getVariantBuilder(c.getEntry())
                            .forAllStatesExcept(state -> {
                                Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                return ConfiguredModel.builder()
                                        .modelFile(p.models()
                                                .getExistingFile(p.modLoc("block/fluid_pipe/" + c.get().getType().getPrefix() + "fluid_pipe/window")))
                                        .uvLock(false)
                                        .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                        .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                        .build();
                            }, BlockStateProperties.WATERLOGGED))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, block) -> p.dropOther(block, FLUID_PIPES.get(block.getType())))
            ).unaffected(AllBlocks.GLASS_FLUID_PIPE)
            .register();

    public static final CopperChain<WeatheringCasingBlock> COPPER_CASINGS = new CopperChainBuilder<>(
            REGISTRATE, "copper_casing", WeatheringCasingBlock:: new)
            .configure(b -> b
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .sound(SoundType.COPPER))
                    .transform(BuilderTransformers.casing(() -> SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType())))
            ).unaffected(AllBlocks.COPPER_CASING)
            .register();

    private static Supplier<Block> getCasingByType(WeatheringType type) {
        if (type == WeatheringType.UNAFFECTED) {
            return AllBlocks.COPPER_CASING::get;
        }
        return BlockRegistry.COPPER_CASINGS.getEntry(type)::get;
    }

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), AllBlocks.FLUID_PIPE))
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, AllBlocks.FLUID_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.FLUID_PIPE))
            ).unaffected(AllBlocks.ENCASED_FLUID_PIPE)
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_EXPOSED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_exposed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.EXPOSED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_exposed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.EXPOSED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.EXPOSED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_OXIDIZED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_oxidized_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.OXIDIZED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_oxidized_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.OXIDIZED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.OXIDIZED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_WEATHERED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_weathered_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WEATHERED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_weathered_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.WEATHERED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WEATHERED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_WAXED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_waxed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.WAXED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_WAXED_EXPOSED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_waxed_exposed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_EXPOSED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_exposed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.WAXED_EXPOSED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_EXPOSED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_WAXED_OXIDIZED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_waxed_oxidized_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_OXIDIZED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_oxidized_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.WAXED_OXIDIZED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_OXIDIZED)))
            )
            .register();

    public static final CopperChain<WeatheringEncasedPipeBlock> ENCASED_WAXED_WEATHERED_FLUID_PIPES = new CopperChainBuilder<>(
            REGISTRATE, "encased_waxed_weathered_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(),(BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_WEATHERED)),true)
            .configure(b -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_weathered_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(b.get().get().getType()),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPES.get(WeatheringType.WAXED_WEATHERED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPES.getEntry(WeatheringType.WAXED_WEATHERED)))
            )
            .register();

    @Contract(pure = true)
    public static @Nullable CopperChain<WeatheringEncasedPipeBlock> ENCASED_WHAT_FLUID_PIPE(WeatheringType pipeType) {
        if (pipeType == WeatheringType.UNAFFECTED) return ENCASED_FLUID_PIPES;
        if (pipeType == WeatheringType.EXPOSED) return  ENCASED_EXPOSED_FLUID_PIPES;
        if (pipeType == WeatheringType.WEATHERED) return ENCASED_WEATHERED_FLUID_PIPES;
        if (pipeType == WeatheringType.OXIDIZED) return ENCASED_OXIDIZED_FLUID_PIPES;
        if (pipeType == WeatheringType.WAXED) return ENCASED_WAXED_FLUID_PIPES;
        if (pipeType == WeatheringType.WAXED_EXPOSED) return ENCASED_WAXED_EXPOSED_FLUID_PIPES;
        if (pipeType == WeatheringType.WAXED_WEATHERED) return ENCASED_WAXED_WEATHERED_FLUID_PIPES;
        if (pipeType == WeatheringType.WAXED_OXIDIZED) return ENCASED_WAXED_OXIDIZED_FLUID_PIPES;
        return null;
    }

    public static void register() {
    }
}

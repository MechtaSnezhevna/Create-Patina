package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlockItem;
import com.simibubi.create.content.decoration.MetalScaffoldingCTBehaviour;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.equipment.armor.BacktankBlock;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlockItem;
import com.simibubi.create.content.logistics.tableCloth.TableClothModel;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.*;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.WeatheringSmartFluidPipeGenerator;
import io.github.mechtasnezhevna.createpatina.registry.DataGen.WeatheringWhistleGenerator;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSetBuilder;
import io.github.mechtasnezhevna.createpatina.util.PatinaMapColor;
import io.github.mechtasnezhevna.createpatina.util.PatinaStress;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.MetalBarsGen.barsBlockState;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings({"deprecation", "removal"})
public class BlockRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final PatinaSet ITEM_DRAIN_SET = new PatinaSetBuilder<>(
            REGISTRATE, "item_drain", ItemDrainBlock::new)
            .configure((type, b) -> b
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

    public static final PatinaSet MECHANICAL_PUMP_SET = new PatinaSetBuilder<>(
            REGISTRATE, "mechanical_pump", PumpBlock::new)
            .configure((type, b) -> b
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

    public static final PatinaSet FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "fluid_pipe", WeatheringFluidPipeBlock::new)
            .configure((type, b) -> b
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

    public static final PatinaSet GLASS_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "glass_fluid_pipe", WeatheringGlassFluidPipeBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.getVariantBuilder(c.getEntry())
                            .forAllStatesExcept(state -> {
                                Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                return ConfiguredModel.builder()
                                        .modelFile(p.models()
                                                .getExistingFile(p.modLoc("block/fluid_pipe/" + type.getPrefix() + "fluid_pipe/window")))
                                        .uvLock(false)
                                        .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                        .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                        .build();
                            }, BlockStateProperties.WATERLOGGED))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, block) -> p.dropOther(block, FLUID_PIPE_SET.get(type)))
            ).unaffected(AllBlocks.GLASS_FLUID_PIPE)
            .register();

    public static final PatinaSet COPPER_CASING_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_casing", CasingBlock:: new)
            .configure((type, b) -> b
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .sound(SoundType.COPPER))
                    .transform(BuilderTransformers.casing(() -> SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type)))
            ).unaffected(AllBlocks.COPPER_CASING)
            .register();

    private static Supplier<Block> getCasingByType(WeatheringType type) {
        if (type == WeatheringType.UNAFFECTED) {
            return AllBlocks.COPPER_CASING::get;
        }
        return BlockRegistry.COPPER_CASING_SET.getEntry(type)::get;
    }

    public static final PatinaSet ENCASED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), AllBlocks.FLUID_PIPE))
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, AllBlocks.FLUID_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.FLUID_PIPE))
            ).unaffected(AllBlocks.ENCASED_FLUID_PIPE)
            .register();

    public static final PatinaSet ENCASED_EXPOSED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_exposed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.EXPOSED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_exposed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.EXPOSED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.EXPOSED)))
            )
            .register();

    public static final PatinaSet ENCASED_OXIDIZED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_oxidized_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.OXIDIZED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_oxidized_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.OXIDIZED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.OXIDIZED)))
            )
            .register();

    public static final PatinaSet ENCASED_WEATHERED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_weathered_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WEATHERED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_weathered_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.WEATHERED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WEATHERED)))
            )
            .register();

    public static final PatinaSet ENCASED_WAXED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_waxed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.WAXED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED)))
            )
            .register();

    public static final PatinaSet ENCASED_WAXED_EXPOSED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_waxed_exposed_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_EXPOSED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_exposed_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.WAXED_EXPOSED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_EXPOSED)))
            )
            .register();

    public static final PatinaSet ENCASED_WAXED_OXIDIZED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_waxed_oxidized_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_OXIDIZED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_oxidized_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.WAXED_OXIDIZED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_OXIDIZED)))
            )
            .register();

    public static final PatinaSet ENCASED_WAXED_WEATHERED_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "encased_waxed_weathered_fluid_pipe", (type, props) -> new WeatheringEncasedPipeBlock(type, props, () -> getCasingByType(type).get(), (BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_WEATHERED)),true)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        DataGenContext<Block, EncasedPipeBlock> cast = new DataGenContext<>(NonNullSupplier.of(c::getEntry),
                                "encased_waxed_weathered_fluid_pipe/" + c.getName(), c.getId());
                        BlockStateGen.encasedPipe().accept(cast, p);
                    })
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type),
                            (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .loot((p, b2) -> p.dropOther(b2, BlockRegistry.FLUID_PIPE_SET.get(WeatheringType.WAXED_WEATHERED)))
                    .transform(EncasingRegistry.addVariantTo((BlockEntry<? extends FluidPipeBlock>) BlockRegistry.FLUID_PIPE_SET.getEntry(WeatheringType.WAXED_WEATHERED)))
            )
            .register();

    @Contract(pure = true)
    public static @Nullable PatinaSet ENCASED_WHAT_FLUID_PIPES(WeatheringType pipeType) {
        return switch (pipeType) {
            case UNAFFECTED -> ENCASED_FLUID_PIPE_SET;
            case EXPOSED -> ENCASED_EXPOSED_FLUID_PIPE_SET;
            case WEATHERED -> ENCASED_WEATHERED_FLUID_PIPE_SET;
            case OXIDIZED -> ENCASED_OXIDIZED_FLUID_PIPE_SET;
            case WAXED -> ENCASED_WAXED_FLUID_PIPE_SET;
            case WAXED_EXPOSED -> ENCASED_WAXED_EXPOSED_FLUID_PIPE_SET;
            case WAXED_WEATHERED -> ENCASED_WAXED_WEATHERED_FLUID_PIPE_SET;
            case WAXED_OXIDIZED -> ENCASED_WAXED_OXIDIZED_FLUID_PIPE_SET;
        };
    }

    public static final PatinaSet SPOUT_SET = new PatinaSetBuilder<>(
        REGISTRATE, "spout", SpoutBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        ModelFile baseModel = p.models()
                                .withExistingParent("block/spout/" + name + "/block", p.modLoc("block/spout/block"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("0", p.modLoc("block/spout/" + prefix + "spout"))
                                .texture("3", p.modLoc("block/" + prefix + "encased_pipe"));
                        p.simpleBlock(c.get(), baseModel);
                    })
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AssemblyOperatorBlockItem::new)
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/spout/item"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("0", p.modLoc("block/spout/" + prefix + "spout"))
                                .texture("4", p.modLoc("block/" + prefix + "encased_pipe"))
                                .texture("3", p.modLoc("block/spout/" + prefix + "spout_nozzle"));
                    })
                    .build()
            ).unaffected(AllBlocks.SPOUT)
            .register();

    public static final PatinaSet STEAM_ENGINE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "steam_engine", SteamEngineBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        ModelFile baseModel = p.models()
                                .withExistingParent("block/steam_engine/" + name + "/block", p.modLoc("block/steam_engine/block"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("1", p.modLoc("block/steam_engine/" + prefix + "engine"));
                        p.horizontalFaceBlock(c.get(), baseModel);
                    })
                    .transform(PatinaStress.setCapacity(1024.0))
                    .onRegister(BlockStressValues.setGeneratorSpeed(64, true))
                    .item()
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/steam_engine/item"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("1", p.modLoc("block/steam_engine/" + prefix + "engine"));
                    })
                    .build()
            ).unaffected(AllBlocks.STEAM_ENGINE)
            .register();

    public static final PatinaSet STEAM_WHISTLE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "steam_whistle", WhistleBlock::new)
            .configure(b ->b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.mapColor(MapColor.GOLD))
                    .transform(pickaxeOnly())
                    .blockstate(new WeatheringWhistleGenerator()::generate)
                    .item()
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/steam_whistle/item"))
                                .texture("1", p.modLoc("block/steam_engine/" + prefix + "engine"))
                                .texture("2", p.modLoc("block/" + prefix + "copper_redstone_plate"));
                    })
                    .build()
            ).unaffected(AllBlocks.STEAM_WHISTLE)
            .register();

    public static final PatinaSet SMART_FLUID_PIPE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "smart_fluid_pipe", SmartFluidPipeBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_YELLOW))
                    .transform(pickaxeOnly())
                    .blockstate(new WeatheringSmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .item()
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/smart_fluid_pipe/item"))
                                .texture("2", p.modLoc("block/" + prefix + "smart_pipe_1"))
                                .texture("3", p.modLoc("block/" + prefix + "smart_pipe_2"))
                                .texture("1", p.modLoc("block/" + prefix + "pipes"))
                                .texture("4", p.modLoc("block/" + prefix + "smart_pipe_3"))
                                .texture("particle", p.modLoc("block/" + prefix + "smart_pipe_3"));
                    })
                    .build()
            ).unaffected(AllBlocks.SMART_FLUID_PIPE)
            .register();

    public static final PatinaSet FLUID_VALVE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "fluid_valve", FluidValveBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> {
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(c.getName());
                        Map<String, ModelFile> modelMap = new HashMap<>();
                        for (String dir : new String[]{"vertical", "horizontal"}) {
                            for (String state : new String[]{"open", "closed"}) {
                                String modelName = c.getName() + "_" + dir + "_" + state;
                                ModelFile model = p.models().withExistingParent(modelName,
                                                p.modLoc("block/fluid_valve/block_" + dir + "_" + state))
                                        .texture("2", p.modLoc("block/" + prefix + "fluid_valve"))
                                        .texture("4", p.modLoc("block/" + prefix + "valve_" + state))
                                        .texture("3", p.modLoc("block/" + prefix + "valve_" + state))
                                        .texture("particle", p.modLoc("block/" + prefix + "copper_underside"));
                                modelMap.put(modelName, model);
                            }
                        }
                        BlockStateGen.directionalAxisBlock(c, p, (state, vertical) -> {
                            boolean enabled = state.getValue(FluidValveBlock.ENABLED);
                            String dir = vertical ? "vertical" : "horizontal";
                            return modelMap.get(c.getName() + "_" + dir + "_" + (enabled ? "open" : "closed"));
                        });
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
                    .item()
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/fluid_valve/item"))
                                .texture("2", p.modLoc("block/" + prefix + "fluid_valve"))
                                .texture("4", p.modLoc("block/" + prefix + "valve_open"))
                                .texture("3", p.modLoc("block/" + prefix + "valve_closed"))
                                .texture("particle", p.modLoc("block/" + prefix + "valve_closed"));
                    })
                    .build()
            ).unaffected(AllBlocks.FLUID_VALVE)
            .register();

    public static final PatinaSet COPPER_VALVE_HANDLE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_valve_handle", WeatheringValveHandleBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.directionalBlock(c.get(), p.models()
                            .withExistingParent(c.getName(), p.modLoc("block/copper_valve_handle"))
                            .texture("3", p.modLoc("block/" + WeatheringType.getPrefixWithoutWaxedByName(c.getName()) + "valve_handle_copper"))))
                    .tag(AllTags.AllBlockTags.BRITTLE.tag, AllTags.AllBlockTags.VALVE_HANDLES.tag)
                    .onRegister(BlockStressValues.setGeneratorSpeed(32))
                    .onRegister(ItemUseOverrides::addBlock)
                    .transform(PatinaStress.setCapacity(8.0))
                    .item()
                    //.tag(AllTags.AllItemTags.VALVE_HANDLES.tag)
                    // removed to prevent recipe to an unaffected handle
                    .build()
            ).unaffected(AllBlocks.COPPER_VALVE_HANDLE)
            .register();

    public static final PatinaSet PORTABLE_FLUID_INTERFACE_SET = new PatinaSetBuilder<>(
            REGISTRATE, "portable_fluid_interface", WeatheringPortableStorageInterfaceBlock::new)
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .transform(axeOrPickaxe())
                    .blockstate((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.directionalBlock(c.get(), p.models()
                                .withExistingParent("block/portable_fluid_interface/" + name + "/block",
                                        p.modLoc("block/portable_fluid_interface/block"))
                                .texture("0", p.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                                .texture("2", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"))
                        );
                        p.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_top",
                                        p.modLoc("block/portable_fluid_interface/block_top"))
                                .texture("0", p.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                        p.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_middle",
                                        p.modLoc("block/portable_fluid_interface/block_middle"))
                                .texture("2", p.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                        p.models().withExistingParent("block/portable_fluid_interface/" + name + "/block_middle_powered",
                                        p.modLoc("block/portable_fluid_interface/block_middle_powered"))
                                .texture("0", p.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                    })
                    .onRegister(movementBehaviour(new PortableStorageInterfaceMovement()))
                    .item()
                    .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                    .model((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        p.withExistingParent(name, p.modLoc("block/portable_fluid_interface/item"))
                                .texture("0", p.modLoc("block/portable_fluid_interface/" + prefix + "portable_fluid_interface"))
                                .texture("2", p.modLoc("block/" + prefix + "copper_underside"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                    })
                    .build()
            ).unaffected(AllBlocks.PORTABLE_FLUID_INTERFACE)
            .register();

    public static final PatinaSet COPPER_BACKTANK_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_backtank", BacktankBlock:: new)
            .configure((type, builder) -> {
                String baseName = builder.getName();
                String prefix = WeatheringType.getPrefixWithoutWaxedByName(baseName);
                builder.initialProperties(SharedProperties::copperMetal)
                        .blockstate((c, p) -> p.horizontalBlock(c.get(),
                                p.models().withExistingParent("block/copper_backtank/" + c.getName(), p.modLoc("block/copper_backtank/block"))
                                        .texture("0", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank"))
                                        .texture("particle", p.modLoc("block/copper_backtank/" + prefix + "copper_backtank"))
                        ))
                        .transform(pickaxeOnly())
                        .addLayer(() -> RenderType::cutoutMipped)
                        .transform(PatinaStress.setImpact(4.0))
                        .loot((lt, block) -> {
                            LootTable.Builder lb = LootTable.lootTable();
                            LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
                            lt.add(block, lb.withPool(LootPool.lootPool()
                                .when(survivesExplosion)
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ItemRegistry.ARMOR_BACKTANKS.get(type).get())
                                    .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                        .include(AllDataComponents.BACKTANK_AIR)))));
                        });
            }).unaffected(AllBlocks.COPPER_BACKTANK)
            .register();

    public static final PatinaSet COPPER_TABLE_CLOTH_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_table_cloth", properties -> new TableClothBlock(properties, "copper"))
            .configure((type, b) -> b
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    //.lang("Copper Table Cover")
                    // removed so that DataGen lang file uses 'Cloth' in name
                    .blockstate((c, p) -> {
                        String name = c.getName();
                        p.simpleBlock(c.get(), p.models()
                         .withExistingParent("block/copper_table_cloth/" + name, p.modLoc("block/table_cloth/block"))
                         .texture("0", p.modLoc("block/table_cloth/" + WeatheringType.getPrefixWithoutWaxedByName(name) + "copper")));
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> TableClothModel::new))
                    .tag(AllTags.AllBlockTags.TABLE_CLOTHS.tag, BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                    //.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.create.table_cloth"))
                    // removed as temporarily no special description needed
                    .item(TableClothBlockItem::new)
                    .model((c, p) -> {
                        String name = c.getName();
                        p.withExistingParent(name, p.modLoc("block/table_cloth/item"))
                         .texture("0", p.modLoc("block/table_cloth/" + WeatheringType.getPrefixWithoutWaxedByName(name) + "copper"));
                    })
                    .tag(AllTags.AllItemTags.TABLE_CLOTHS.tag)
                    .build()
            ).unaffected(AllBlocks.COPPER_TABLE_CLOTH)
            .register();

    public static final PatinaSet COPPER_DOOR_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_door", properties -> SlidingDoorBlock.stone(properties, true))
            .configure((type, b) -> b
                    .initialProperties(() -> Blocks.IRON_DOOR)
                    .properties(p -> p.requiresCorrectToolForDrops()
                            .mapColor(PatinaMapColor.getMapColorByName(b.getName()))
                            .noOcclusion()
                            .strength(3.0F, 6.0F))
                    .blockstate((c, p) -> {
                        String name = c.getName();
                        String prefix = WeatheringType.getPrefixWithoutWaxedByName(name);
                        ModelFile bottom = p.models().withExistingParent("block/copper_door/" + name + "/block_bottom",
                                        p.modLoc("block/copper_door/block_bottom"))
                                .texture("0", p.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                                .texture("2", p.modLoc("block/copper_door/" + prefix + "copper_door_bottom"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                        ModelFile top = p.models().withExistingParent("block/copper_door/" + name + "/block_top",
                                        p.modLoc("block/copper_door/block_top"))
                                .texture("0", p.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                                .texture("2", p.modLoc("block/copper_door/" + prefix + "copper_door_top"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                        p.doorBlock(c.get(), bottom, bottom, bottom, bottom, top, top, top, top);

                        p.models().withExistingParent("block/copper_door/" + name + "/fold_left",
                                        p.modLoc("block/copper_door/fold_left"))
                                .texture("0", p.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                                .texture("3", p.modLoc("block/copper_door/" + prefix + "copper_door_bottom"))
                                .texture("2", p.modLoc("block/copper_door/" + prefix + "copper_door_top"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                        p.models().withExistingParent("block/copper_door/" + name + "/fold_right",
                                        p.modLoc("block/copper_door/fold_right"))
                                .texture("0", p.modLoc("block/copper_door/" + prefix + "copper_door_side"))
                                .texture("3", p.modLoc("block/copper_door/" + prefix + "copper_door_bottom"))
                                .texture("2", p.modLoc("block/copper_door/" + prefix + "copper_door_top"))
                                .texture("particle", p.modLoc("block/" + prefix + "copper_casing"));
                    })
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                    .onRegister(movementBehaviour(new SlidingDoorMovementBehaviour()))
                    .tag(BlockTags.DOORS)
                    .tag(BlockTags.WOODEN_DOORS) // for villager AI
                    .tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
                    .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)))
                    .item()
                    .tag(ItemTags.DOORS)
                    .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                    .model((c, p) ->
                            p.blockSprite(c, p.modLoc("item/copper_door/" + WeatheringType.getPrefixWithoutWaxedByName(b.getName()) + "copper_door")))
                    .build()
            ).unaffected(AllBlocks.COPPER_DOOR)
            .register();

    public static final PatinaSet COPPER_SCAFFOLD_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_scaffolding", MetalScaffoldingBlock:: new)
            .configure((type, b) -> b
                    .initialProperties(() -> Blocks.SCAFFOLDING)
                    .properties(p -> p.sound(SoundType.COPPER)
                            .mapColor(PatinaMapColor.getMapColorByName(b.getName())))
                    .addLayer(() -> RenderType::cutout)
                    .blockstate((c, p) -> p.getVariantBuilder(c.get())
                            .forAllStatesExcept(s -> {
                                String suffix = s.getValue(MetalScaffoldingBlock.BOTTOM) ? "_horizontal" : "";
                                return ConfiguredModel.builder()
                                        .modelFile(p.models()
                                                .withExistingParent(c.getName() + suffix, p.modLoc("block/copper_scaffold/block" + suffix))
                                                .texture("top", p.modLoc("block/" + type.getPrefixWithoutWaxed() + "copper_funnel_frame"))
                                                .texture("inside", p.modLoc("block/" + type.getPrefixWithoutWaxed() + "copper_scaffold_inside"))
                                                .texture("side", p.modLoc("block/" + type.getPrefixWithoutWaxed() + "copper_scaffold"))
                                                .texture("casing", p.modLoc("block/" + type.getPrefixWithoutWaxed() + "copper_casing"))
                                                .texture("particle", p.modLoc("block/" + type.getPrefixWithoutWaxed() + "copper_scaffold")))
                                        .build();
                            }, MetalScaffoldingBlock.WATERLOGGED, MetalScaffoldingBlock.DISTANCE))
                    .onRegister(connectedTextures(
                            () -> new MetalScaffoldingCTBehaviour(
                                    SpriteShiftRegistry.WEATHERING_COPPER_SCAFFOLDS.get(type),
                                    SpriteShiftRegistry.WEATHERING_COPPER_SCAFFOLD_INSIDES.get(type),
                                    SpriteShiftRegistry.WEATHERING_COPPER_CASINGS.get(type))))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.CLIMBABLE)
                    .item(MetalScaffoldingBlockItem::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + c.getName())))
                    .build()
            ).unaffected(AllBlocks.COPPER_SCAFFOLD)
            .register();

    public static final PatinaSet COPPER_LADDER_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_ladder", MetalLadderBlock:: new)
            .configure((type, b) -> b
                    .properties(p -> p.mapColor(PatinaMapColor.getMapColorByName(b.getName())))
                    .addLayer(() -> RenderType::cutout)
                    .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                            .withExistingParent(c.getName(), p.modLoc("block/copper_ladder"))
                            .texture("0", p.modLoc("block/"+type.getPrefixWithoutWaxed()+"ladder_copper_hoop"))
                            .texture("1", p.modLoc("block/"+type.getPrefixWithoutWaxed()+"ladder_copper"))
                            .texture("particle", p.modLoc("block/"+type.getPrefixWithoutWaxed()+"ladder_copper"))))
                    .properties(p -> p.sound(SoundType.COPPER))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.CLIMBABLE)
                    .item()
                    .model((c, p) -> p.blockSprite(c::get, p.modLoc("block/"+WeatheringType.getPrefixWithoutWaxedByName(b.getName())+"ladder_copper")))
                    .build()
            ).unaffected(AllBlocks.COPPER_LADDER)
            .register();

    public static final PatinaSet COPPER_BARS_SET = new PatinaSetBuilder<>(
            REGISTRATE, "copper_bars", IronBarsBlock:: new)
            .configure((type, b) -> b
                    .addLayer(() -> RenderType::cutoutMipped)
                    .initialProperties(() -> Blocks.IRON_BARS)
                    .properties(p -> p.sound(SoundType.COPPER)
                            .mapColor(PatinaMapColor.getMapColorByName(b.getName())))
                    .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
                    .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
                    .transform(TagGen.pickaxeOnly())
                    .blockstate(barsBlockState(WeatheringType.getPrefixByName(b.getName()) + "copper", true))
                    .item()
                    .model((c, p) -> {
                        ResourceLocation barsTexture = p.modLoc("block/bars/" + b.getName());
                        p.generated(c, barsTexture);
                    })
                    .build()
            ).unaffected(AllBlocks.COPPER_BARS)
            .register();

    public static void register() {
    }
}

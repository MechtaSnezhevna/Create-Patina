package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import com.simibubi.create.content.decoration.steamWhistle.WhistleRenderer;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankRenderer;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainRenderer;
import com.simibubi.create.content.fluids.pipes.*;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveVisual;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.crank.HandCrankRenderer;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleVisual;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineRenderer;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineVisual;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlockEntity;
import com.simibubi.create.content.logistics.tableCloth.TableClothRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;

public class BlockEntityRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final BlockEntityEntry<ItemDrainBlockEntity> WEATHERING_ITEM_DRAIN = REGISTRATE
            .blockEntity("item_drain", ItemDrainBlockEntity::new)
            .validBlocks(BlockRegistry.ITEM_DRAINS.getAllEntries())
            .renderer(() -> ItemDrainRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpBlockEntity> WEATHERING_PUMP = REGISTRATE
            .blockEntity("pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(AllPartialModels.MECHANICAL_PUMP_COG))
            .validBlocks(BlockRegistry.MECHANICAL_PUMPS.getAllEntries())
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> WEATHERING_FLUID_PIPE = REGISTRATE
            .blockEntity("fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.FLUID_PIPES.getAllEntries())
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> WEATHERING_GLASS_FLUID_PIPE = REGISTRATE
            .blockEntity("glass_fluid_pipe", StraightPipeBlockEntity::new)
            .visual(() -> GlassPipeVisual::new, false)
            .validBlocks(BlockRegistry.GLASS_FLUID_PIPES.getAllEntries())
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> WEATHERING_ENCASED_FLUID_PIPE = REGISTRATE
            .blockEntity("encased_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.ENCASED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_EXPOSED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_OXIDIZED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WEATHERED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_EXPOSED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_OXIDIZED_FLUID_PIPES.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_WEATHERED_FLUID_PIPES.getAllEntries())
            .register();

    public static final BlockEntityEntry<ValveHandleBlockEntity> WEATHERING_VALVE_HANDLE = REGISTRATE
            .blockEntity("copper_valve_handle", ValveHandleBlockEntity::new)
            .visual(() -> ValveHandleVisual::new)
            .validBlocks(BlockRegistry.COPPER_VALVE_HANDLES.getAllEntries())
            .renderer(() -> HandCrankRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> WEATHERING_FLUID_VALVE = REGISTRATE
            .blockEntity("fluid_valve", FluidValveBlockEntity::new)
            .visual(() -> FluidValveVisual::new)
            .validBlocks(BlockRegistry.FLUID_VALVES.getAllEntries())
            .renderer(() -> FluidValveRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> WEATHERING_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.SMART_FLUID_PIPES.getAllEntries())
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<TableClothBlockEntity> WEATHERING_TABLE_CLOTH = REGISTRATE
            .blockEntity("table_cloth", TableClothBlockEntity::new)
            .validBlocks(BlockRegistry.COPPER_TABLE_CLOTHS.getAllEntries())
            .renderer(() -> TableClothRenderer::new)
            .register();

    public static final BlockEntityEntry<SlidingDoorBlockEntity> WEATHERING_COPPER_DOOR = REGISTRATE
            .blockEntity("copper_door", SlidingDoorBlockEntity::new)
            .renderer(() -> SlidingDoorRenderer::new)
            .validBlocks(BlockRegistry.COPPER_DOORS.getAllEntries())
            .register();

    public static final BlockEntityEntry<BacktankBlockEntity> WEATHERING_COPPER_BACKTANK = REGISTRATE
            .blockEntity("copper_backtank", BacktankBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::backtank)
            .validBlocks(BlockRegistry.COPPER_BACKTANKS.getAllEntries())
            .renderer(() -> BacktankRenderer::new)
            .register();

    public static final BlockEntityEntry<SteamEngineBlockEntity> WEATHERING_STEAM_ENGINE = REGISTRATE
            .blockEntity("steam_engine", SteamEngineBlockEntity::new)
            .visual(() -> SteamEngineVisual::new, false)
            .validBlocks(BlockRegistry.STEAM_ENGINES.getAllEntries())
            .renderer(() -> SteamEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<WhistleBlockEntity> WEATHERING_STEAM_WHISTLE = REGISTRATE
            .blockEntity("steam_whistle", WhistleBlockEntity::new)
            .validBlocks(BlockRegistry.STEAM_WHISTLES.getAllEntries())
            .renderer(() -> WhistleRenderer::new)
            .register();

    public static final BlockEntityEntry<SpoutBlockEntity> WEATHERING_SPOUT = REGISTRATE
            .blockEntity("spout", SpoutBlockEntity::new)
            .validBlocks(BlockRegistry.SPOUTS.getAllEntries())
            .renderer(() -> SpoutRenderer::new)
            .register();

    public static void register() {
    }
}

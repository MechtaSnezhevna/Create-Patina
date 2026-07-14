package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.actors.psi.PSIVisual;
import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceRenderer;
import com.simibubi.create.content.contraptions.pulley.HosePulleyVisual;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlockEntity;
import com.simibubi.create.content.decoration.steamWhistle.WhistleRenderer;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankRenderer;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainRenderer;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyRenderer;
import com.simibubi.create.content.fluids.pipes.*;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveVisual;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutRenderer;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankRenderer;
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
            .validBlocks(BlockRegistry.ITEM_DRAIN_SET.getAllEntries())
            .renderer(() -> ItemDrainRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpBlockEntity> WEATHERING_PUMP = REGISTRATE
            .blockEntity("pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(AllPartialModels.MECHANICAL_PUMP_COG))
            .validBlocks(BlockRegistry.MECHANICAL_PUMP_SET.getAllEntries())
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> WEATHERING_FLUID_PIPE = REGISTRATE
            .blockEntity("fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.FLUID_PIPE_SET.getAllEntries())
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> WEATHERING_GLASS_FLUID_PIPE = REGISTRATE
            .blockEntity("glass_fluid_pipe", StraightPipeBlockEntity::new)
            .visual(() -> GlassPipeVisual::new, false)
            .validBlocks(BlockRegistry.GLASS_FLUID_PIPE_SET.getAllEntries())
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> WEATHERING_ENCASED_FLUID_PIPE = REGISTRATE
            .blockEntity("encased_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.ENCASED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_EXPOSED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_OXIDIZED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WEATHERED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_EXPOSED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_OXIDIZED_FLUID_PIPE_SET.getAllEntries())
            .validBlocks(BlockRegistry.ENCASED_WAXED_WEATHERED_FLUID_PIPE_SET.getAllEntries())
            .register();

    public static final BlockEntityEntry<ValveHandleBlockEntity> WEATHERING_VALVE_HANDLE = REGISTRATE
            .blockEntity("copper_valve_handle", ValveHandleBlockEntity::new)
            .visual(() -> ValveHandleVisual::new)
            .validBlocks(BlockRegistry.COPPER_VALVE_HANDLE_SET.getAllEntries())
            .renderer(() -> HandCrankRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> WEATHERING_FLUID_VALVE = REGISTRATE
            .blockEntity("fluid_valve", FluidValveBlockEntity::new)
            .visual(() -> FluidValveVisual::new)
            .validBlocks(BlockRegistry.FLUID_VALVE_SET.getAllEntries())
            .renderer(() -> FluidValveRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> WEATHERING_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(BlockRegistry.SMART_FLUID_PIPE_SET.getAllEntries())
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<TableClothBlockEntity> WEATHERING_TABLE_CLOTH = REGISTRATE
            .blockEntity("table_cloth", TableClothBlockEntity::new)
            .validBlocks(BlockRegistry.COPPER_TABLE_CLOTH_SET.getAllEntries())
            .renderer(() -> TableClothRenderer::new)
            .register();

    public static final BlockEntityEntry<SlidingDoorBlockEntity> WEATHERING_COPPER_DOOR = REGISTRATE
            .blockEntity("copper_door", SlidingDoorBlockEntity::new)
            .renderer(() -> SlidingDoorRenderer::new)
            .validBlocks(BlockRegistry.COPPER_DOOR_SET.getAllEntries())
            .register();

    public static final BlockEntityEntry<BacktankBlockEntity> WEATHERING_COPPER_BACKTANK = REGISTRATE
            .blockEntity("copper_backtank", BacktankBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::backtank)
            .validBlocks(BlockRegistry.COPPER_BACKTANK_SET.getAllEntries())
            .renderer(() -> BacktankRenderer::new)
            .register();

    public static final BlockEntityEntry<SteamEngineBlockEntity> WEATHERING_STEAM_ENGINE = REGISTRATE
            .blockEntity("steam_engine", SteamEngineBlockEntity::new)
            .visual(() -> SteamEngineVisual::new, false)
            .validBlocks(BlockRegistry.STEAM_ENGINE_SET.getAllEntries())
            .renderer(() -> SteamEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<WhistleBlockEntity> WEATHERING_STEAM_WHISTLE = REGISTRATE
            .blockEntity("steam_whistle", WhistleBlockEntity::new)
            .validBlocks(BlockRegistry.STEAM_WHISTLE_SET.getAllEntries())
            .renderer(() -> WhistleRenderer::new)
            .register();

    public static final BlockEntityEntry<SpoutBlockEntity> WEATHERING_SPOUT = REGISTRATE
            .blockEntity("spout", SpoutBlockEntity::new)
            .validBlocks(BlockRegistry.SPOUT_SET.getAllEntries())
            .renderer(() -> SpoutRenderer::new)
            .register();

    public static final BlockEntityEntry<PortableFluidInterfaceBlockEntity> WEATHERING_PORTABLE_FLUID_INTERFACE = REGISTRATE
            .blockEntity("portable_fluid_interface", PortableFluidInterfaceBlockEntity::new)
            .visual(() -> PSIVisual::new)
            .validBlocks(BlockRegistry.PORTABLE_FLUID_INTERFACE_SET.getAllEntries())
            .renderer(() -> PortableStorageInterfaceRenderer::new)
            .register();

    public static final BlockEntityEntry<HosePulleyBlockEntity> WEATHERING_HOSE_PULLEY = REGISTRATE
            .blockEntity("hose_pulley", HosePulleyBlockEntity::new)
            .visual(() -> HosePulleyVisual::new)
            .validBlocks(BlockRegistry.HOSE_PULLEY_SET.getAllEntries())
            .renderer(() -> HosePulleyRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidTankBlockEntity> WEATHERING_FLUID_TANK = REGISTRATE
            .blockEntity("fluid_tank", FluidTankBlockEntity::new)
            .validBlocks(BlockRegistry.FLUID_TANK_SET.getAllEntries())
            .renderer(() -> FluidTankRenderer::new)
            .register();

    public static void register() {
    }
}

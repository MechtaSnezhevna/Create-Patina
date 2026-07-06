package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainRenderer;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassPipeVisual;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.crank.HandCrankRenderer;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleVisual;
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

    public static final BlockEntityEntry<ValveHandleBlockEntity> WEATHEING_VALVE_HANDLE = REGISTRATE
            .blockEntity("copper_valve_handle", ValveHandleBlockEntity::new)
            .visual(() -> ValveHandleVisual::new)
            .validBlocks(BlockRegistry.COPPER_VALVE_HANDLES.getAllEntries())
            .renderer(() -> HandCrankRenderer::new)
            .register();

    public static void register() {
    }
}

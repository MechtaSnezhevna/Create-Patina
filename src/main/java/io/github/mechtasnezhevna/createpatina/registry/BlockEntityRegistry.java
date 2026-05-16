package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainRenderer;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.mechtasnezhevna.createpatina.CreatePatina;

public class BlockEntityRegistry {

    public static CreateRegistrate REGISTRATE = CreatePatina.registrate();

    public static final BlockEntityEntry<ItemDrainBlockEntity> WEATHERING_ITEM_DRAIN = REGISTRATE
            .blockEntity("item_drain", ItemDrainBlockEntity::new)
            .validBlocks(
                    BlockRegistry.EXPOSED_ITEM_DRAIN,
                    BlockRegistry.WEATHERED_ITEM_DRAIN,
                    BlockRegistry.OXIDIZED_ITEM_DRAIN,
                    BlockRegistry.WAXED_ITEM_DRAIN,
                    BlockRegistry.WAXED_EXPOSED_ITEM_DRAIN,
                    BlockRegistry.WAXED_WEATHERED_ITEM_DRAIN,
                    BlockRegistry.WAXED_OXIDIZED_ITEM_DRAIN
            )
            .renderer(() -> ItemDrainRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpBlockEntity> WEATHERING_PUMP = REGISTRATE
            .blockEntity("pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(AllPartialModels.MECHANICAL_PUMP_COG))
            .validBlocks(
                    BlockRegistry.EXPOSED_MECHANICAL_PUMP,
                    BlockRegistry.WEATHERED_MECHANICAL_PUMP,
                    BlockRegistry.OXIDIZED_MECHANICAL_PUMP,
                    BlockRegistry.WAXED_MECHANICAL_PUMP,
                    BlockRegistry.WAXED_EXPOSED_MECHANICAL_PUMP,
                    BlockRegistry.WAXED_WEATHERED_MECHANICAL_PUMP,
                    BlockRegistry.WAXED_OXIDIZED_MECHANICAL_PUMP
            )
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> WEATHERING_FLUID_PIPE = REGISTRATE
            .blockEntity("fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(
                    BlockRegistry.EXPOSED_FLUID_PIPE,
                    BlockRegistry.WEATHERED_FLUID_PIPE,
                    BlockRegistry.OXIDIZED_FLUID_PIPE,
                    BlockRegistry.WAXED_FLUID_PIPE,
                    BlockRegistry.WAXED_EXPOSED_FLUID_PIPE,
                    BlockRegistry.WAXED_WEATHERED_FLUID_PIPE,
                    BlockRegistry.WAXED_OXIDIZED_FLUID_PIPE
            )
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> WEATHERING_GLASS_FLUID_PIPE = REGISTRATE
            .blockEntity("glass_fluid_pipe", StraightPipeBlockEntity::new)
            .validBlocks(
                    BlockRegistry.EXPOSED_GLASS_FLUID_PIPE,
                    BlockRegistry.WEATHERED_GLASS_FLUID_PIPE,
                    BlockRegistry.OXIDIZED_GLASS_FLUID_PIPE,
                    BlockRegistry.WAXED_GLASS_FLUID_PIPE,
                    BlockRegistry.WAXED_EXPOSED_GLASS_FLUID_PIPE,
                    BlockRegistry.WAXED_WEATHERED_GLASS_FLUID_PIPE,
                    BlockRegistry.WAXED_OXIDIZED_GLASS_FLUID_PIPE
            )
            .register();

    public static void register() {
    }
}

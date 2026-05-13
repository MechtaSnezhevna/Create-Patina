package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.drain.ItemDrainRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
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

    public static void register() {
    }
}

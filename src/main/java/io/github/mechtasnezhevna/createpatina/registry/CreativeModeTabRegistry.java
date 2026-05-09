package io.github.mechtasnezhevna.createpatina.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.simibubi.create.AllBlocks.ITEM_DRAIN;
import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class CreativeModeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> CREATEPATINA_TAB = CREATIVE_MODE_TABS.register("createpatina_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.createpatina"))
                    .icon(() -> new ItemStack(BlockRegistry.WEATHERED_ITEM_DRAIN.get()))
                    .displayItems((params, output) -> {
                        output.accept(ITEM_DRAIN.get());
                        output.accept(BlockRegistry.EXPOSED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.WEATHERED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.OXIDIZED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.WAXED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.WAXED_EXPOSED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.WAXED_WEATHERED_ITEM_DRAIN.get());
                        output.accept(BlockRegistry.WAXED_OXIDIZED_ITEM_DRAIN.get());
                    })
                    .build()
    );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }

}

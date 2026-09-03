package io.github.mechtasnezhevna.createpatina.item;

import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.util.CopperRegistries;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Optional;

public class PatinaClockItem extends Item {

    private static final WeatheringType[][] SETTINGS = {
            {
                    WeatheringType.UNAFFECTED,
                    WeatheringType.EXPOSED,
                    WeatheringType.WEATHERED,
                    WeatheringType.OXIDIZED
            },
            {
                    WeatheringType.WAXED,
                    WeatheringType.WAXED_EXPOSED,
                    WeatheringType.WAXED_WEATHERED,
                    WeatheringType.WAXED_OXIDIZED
            }
    };

    public PatinaClockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return canInteractWith(state)
                ? InteractionResult.sidedSuccess(context.getLevel().isClientSide)
                : InteractionResult.PASS;
    }

    public static boolean canInteractWith(BlockState state) {
        return canAdjustState(state) || getNext(state).isPresent();
    }

    // verified: NeoForge 21.1.228 PlayerInteractEvent.RightClickBlock source, 2026-07-26
    public static void suppressImmediateServerInteraction(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide
                || !(event.getItemStack().getItem() instanceof PatinaClockItem)
                || !canInteractWith(event.getLevel().getBlockState(event.getPos()))) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    public static boolean canAdjustState(BlockState state) {
        if (!(state.getBlock() instanceof PatinaBlock)) {
            return false;
        }

        Holder<Block> holder = state.getBlock().builtInRegistryHolder();
        return CopperRegistries.getWeatheringView().containsKey(holder)
                || CopperRegistries.getWeatheringView().containsValue(holder)
                || CopperRegistries.getWaxableView().containsKey(holder)
                || CopperRegistries.getWaxableView().containsValue(holder);
    }

    public static void applyShortAction(ServerPlayer player, BlockPos pos, ItemStack itemStack) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        getNext(state).ifPresent(nextState -> {
            replaceState(player, itemStack, pos, state, nextState);
            damageClock(player, itemStack);
        });
    }

    public static void applySelectedState(ServerPlayer player, BlockPos pos, ItemStack itemStack, int row, int value) {
        if (row < 0 || row >= SETTINGS.length || value < 0 || value >= SETTINGS[row].length) {
            return;
        }

        Level level = player.level();
        BlockState oldState = level.getBlockState(pos);
        if (!canAdjustState(oldState)) {
            return;
        }

        findStateForType(oldState, SETTINGS[row][value])
                .filter(newState -> !newState.is(oldState.getBlock()))
                .ifPresent(newState -> {
                    replaceState(player, itemStack, pos, oldState, newState);
                    damageClock(player, itemStack);
                });
    }

    public static int rowFor(WeatheringType type) {
        return type.isWaxed() ? 1 : 0;
    }

    public static int valueFor(WeatheringType type) {
        return switch (type) {
            case UNAFFECTED, WAXED -> 0;
            case EXPOSED, WAXED_EXPOSED -> 1;
            case WEATHERED, WAXED_WEATHERED -> 2;
            case OXIDIZED, WAXED_OXIDIZED -> 3;
        };
    }

    public static WeatheringType typeFor(int row, int value) {
        if (row < 0 || row >= SETTINGS.length || value < 0 || value >= SETTINGS[row].length) {
            return WeatheringType.UNAFFECTED;
        }
        return SETTINGS[row][value];
    }

    private static void replaceState(
            ServerPlayer player, ItemStack itemStack, BlockPos pos, BlockState oldState, BlockState newState
    ) {
        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, itemStack);
        OxidizeUtil.applySelectionWeathering(oldState, newState, player.level(), pos,
                PatinaConfig.CONFIG.WEATHER_WHOLE_FLUID_TANK_WITH_TOOLS.get());
        player.level().gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
    }

    private static void damageClock(ServerPlayer player, ItemStack itemStack) {
        itemStack.hurtAndBreak(1, player.serverLevel(), player,
                item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
    }

    private static Optional<BlockState> findStateForType(BlockState state, WeatheringType targetType) {
        PatinaBlock patinaBlock = (PatinaBlock) state.getBlock();
        WeatheringType currentType = patinaBlock.getType();
        Holder<Block> current = state.getBlock().builtInRegistryHolder();

        if (currentType.isWaxed()) {
            Optional<Holder<Block>> unwaxed = findKeyByValue(CopperRegistries.getWaxableView(), current);
            if (unwaxed.isEmpty()) {
                return Optional.empty();
            }
            current = unwaxed.get();
        }

        int currentStage = valueFor(currentType);
        for (int i = 0; i < currentStage; i++) {
            Optional<Holder<Block>> previous = findKeyByValue(CopperRegistries.getWeatheringView(), current);
            if (previous.isEmpty()) {
                return Optional.empty();
            }
            current = previous.get();
        }

        int targetStage = valueFor(targetType);
        for (int i = 0; i < targetStage; i++) {
            Holder<Block> next = CopperRegistries.getWeatheringView().get(current);
            if (next == null) {
                return Optional.empty();
            }
            current = next;
        }

        if (targetType.isWaxed()) {
            Holder<Block> waxed = CopperRegistries.getWaxableView().get(current);
            if (waxed == null) {
                return Optional.empty();
            }
            current = waxed;
        }

        return Optional.of(current.value().defaultBlockState());
    }

    private static Optional<Holder<Block>> findKeyByValue(
            Map<Holder<Block>, Holder<Block>> map, Holder<Block> value
    ) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private static Optional<BlockState> getNext(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).map((p_154896_) -> p_154896_.withPropertiesOf(state));
    }
}

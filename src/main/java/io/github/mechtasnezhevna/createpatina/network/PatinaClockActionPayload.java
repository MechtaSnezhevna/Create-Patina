package io.github.mechtasnezhevna.createpatina.network;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PatinaClockActionPayload(BlockPos pos, int row, int value, Direction face)
        implements CustomPacketPayload {

    public static final int SHORT_ACTION_ROW = -1;
    public static final int PLACE_ON_TABLE_ROW = -2;

    public static final Type<PatinaClockActionPayload> TYPE =
            new Type<>(CreatePatina.asResource("patina_clock_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PatinaClockActionPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, PatinaClockActionPayload::pos,
                    ByteBufCodecs.VAR_INT, PatinaClockActionPayload::row,
                    ByteBufCodecs.VAR_INT, PatinaClockActionPayload::value,
                    Direction.STREAM_CODEC, PatinaClockActionPayload::face,
                    PatinaClockActionPayload::new
            );

    // verified: NeoForge 21.1.228 RegisterPayloadHandlersEvent/PayloadRegistrar source, 2026-07-26
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(TYPE, STREAM_CODEC, PatinaClockActionPayload::handle);
    }

    private static void handle(PatinaClockActionPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack itemStack = findHeldClock(player);
        if (itemStack.isEmpty()
                || !player.level().isLoaded(payload.pos())
                || !player.level().mayInteract(player, payload.pos())
                || !player.mayUseItemAt(payload.pos(), payload.face(), itemStack)
                || player.distanceToSqr(Vec3.atCenterOf(payload.pos())) > 64.0D) {
            return;
        }

        if (payload.row() == SHORT_ACTION_ROW) {
            PatinaClockItem.applyShortAction(player, payload.pos(), itemStack);
            return;
        }

        if (payload.row() == PLACE_ON_TABLE_ROW) {
            placeClockOnTable(player, payload.pos(), payload.face(), itemStack);
            return;
        }

        PatinaClockItem.applySelectedState(
                player, payload.pos(), itemStack, payload.row(), payload.value()
        );
    }

    /**
     * Places the held Patina Clock onto a copper table cloth, mirroring the table cloth's own
     * right-click placement so a plain right-click no longer runs the clock's weathering actions.
     */
    private static void placeClockOnTable(ServerPlayer player, BlockPos pos, Direction face, ItemStack clockStack) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        if (face == Direction.DOWN || !PatinaClockItem.isCopperTableCloth(state)) {
            return;
        }

        InteractionHand hand = player.getMainHandItem().is(clockStack.getItem())
                ? InteractionHand.MAIN_HAND
                : InteractionHand.OFF_HAND;
        state.useItemOn(clockStack, level, player, hand,
                new BlockHitResult(Vec3.atCenterOf(pos), face, pos, false));
    }

    private static ItemStack findHeldClock(ServerPlayer player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof PatinaClockItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

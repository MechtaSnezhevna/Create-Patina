package io.github.mechtasnezhevna.createpatina.network;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public record PatinaClockActionPayload(BlockPos pos, int row, int value, Direction face) {

    public static final int SHORT_ACTION_ROW = -1;

    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            CreatePatina.asResource("patina_clock_action"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // verified: Forge 1.20.1-47.1.33 SimpleChannel.MessageBuilder source, 2026-07-30
    public static void register() {
        CHANNEL.messageBuilder(PatinaClockActionPayload.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PatinaClockActionPayload::encode)
                .decoder(PatinaClockActionPayload::decode)
                .consumerMainThread(PatinaClockActionPayload::handle)
                .add();
    }

    public static void sendToServer(PatinaClockActionPayload payload) {
        CHANNEL.sendToServer(payload);
    }

    private static void encode(PatinaClockActionPayload payload, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(payload.pos());
        buffer.writeVarInt(payload.row());
        buffer.writeVarInt(payload.value());
        buffer.writeEnum(payload.face());
    }

    private static PatinaClockActionPayload decode(FriendlyByteBuf buffer) {
        return new PatinaClockActionPayload(
                buffer.readBlockPos(),
                buffer.readVarInt(),
                buffer.readVarInt(),
                buffer.readEnum(Direction.class)
        );
    }

    private static void handle(
            PatinaClockActionPayload payload,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) {
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

        PatinaClockItem.applySelectedState(
                player, payload.pos(), itemStack, payload.row(), payload.value()
        );
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
}

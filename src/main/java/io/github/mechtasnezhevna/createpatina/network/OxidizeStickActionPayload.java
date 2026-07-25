package io.github.mechtasnezhevna.createpatina.network;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.item.OxidizeStickItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OxidizeStickActionPayload(BlockPos pos, int row, int value, Direction face)
        implements CustomPacketPayload {

    public static final int SHORT_ACTION_ROW = -1;

    public static final Type<OxidizeStickActionPayload> TYPE =
            new Type<>(CreatePatina.asResource("oxidize_stick_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OxidizeStickActionPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, OxidizeStickActionPayload::pos,
                    ByteBufCodecs.VAR_INT, OxidizeStickActionPayload::row,
                    ByteBufCodecs.VAR_INT, OxidizeStickActionPayload::value,
                    Direction.STREAM_CODEC, OxidizeStickActionPayload::face,
                    OxidizeStickActionPayload::new
            );

    // verified: NeoForge 21.1.228 RegisterPayloadHandlersEvent/PayloadRegistrar source, 2026-07-26
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(TYPE, STREAM_CODEC, OxidizeStickActionPayload::handle);
    }

    private static void handle(OxidizeStickActionPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack itemStack = findHeldStick(player);
        if (itemStack.isEmpty()
                || !player.level().isLoaded(payload.pos())
                || !player.level().mayInteract(player, payload.pos())
                || !player.mayUseItemAt(payload.pos(), payload.face(), itemStack)
                || player.distanceToSqr(Vec3.atCenterOf(payload.pos())) > 64.0D) {
            return;
        }

        if (payload.row() == SHORT_ACTION_ROW) {
            OxidizeStickItem.applyShortAction(player, payload.pos(), itemStack);
            return;
        }

        OxidizeStickItem.applySelectedState(
                player, payload.pos(), itemStack, payload.row(), payload.value()
        );
    }

    private static ItemStack findHeldStick(ServerPlayer player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof OxidizeStickItem) {
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

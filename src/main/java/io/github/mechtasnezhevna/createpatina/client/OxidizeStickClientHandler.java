package io.github.mechtasnezhevna.createpatina.client;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.item.OxidizeStickItem;
import io.github.mechtasnezhevna.createpatina.network.OxidizeStickActionPayload;
import net.createmod.catnip.gui.ScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class OxidizeStickClientHandler {

    private static final int LONG_PRESS_TICKS = 5;

    private static int heldTicks = -1;
    private static BlockPos heldPos;
    private static Direction heldFace;
    private static InteractionHand heldHand;

    private OxidizeStickClientHandler() {
    }

    // verified: NeoForge 21.1.228 PlayerInteractEvent.RightClickBlock source, 2026-07-26
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide
                || !(event.getItemStack().getItem() instanceof OxidizeStickItem)
                || !OxidizeStickItem.canInteractWith(event.getLevel().getBlockState(event.getPos()))) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (heldTicks != -1 && event.getPos().equals(heldPos)) {
            return;
        }

        heldTicks = 0;
        heldPos = event.getPos().immutable();
        heldFace = event.getHitVec().getDirection();
        heldHand = event.getHand();
    }

    // verified: NeoForge 21.1.228 ClientTickEvent.Post source, 2026-07-26
    public static void onClientTick(ClientTickEvent.Post event) {
        if (heldTicks == -1) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.screen != null) {
            cancel();
            return;
        }

        ItemStack heldStack = minecraft.player.getItemInHand(heldHand);
        if (!(heldStack.getItem() instanceof OxidizeStickItem)
                || !(minecraft.hitResult instanceof BlockHitResult hitResult)
                || !hitResult.getBlockPos().equals(heldPos)) {
            cancel();
            return;
        }

        if (!minecraft.options.keyUse.isDown()) {
            // verified: NeoForge 21.1.228 PacketDistributor source, 2026-07-26
            PacketDistributor.sendToServer(new OxidizeStickActionPayload(
                    heldPos, OxidizeStickActionPayload.SHORT_ACTION_ROW, 0, heldFace
            ));
            cancel();
            return;
        }

        BlockState state = minecraft.level.getBlockState(heldPos);
        if (++heldTicks < LONG_PRESS_TICKS
                || !OxidizeStickItem.canAdjustState(state)
                || !(state.getBlock() instanceof PatinaBlock patinaBlock)) {
            return;
        }

        ScreenOpener.open(new OxidizeStickValueSettingsScreen(
                heldPos, heldFace, patinaBlock.getType()
        ));
        cancel();
    }

    private static void cancel() {
        heldTicks = -1;
        heldPos = null;
        heldFace = null;
        heldHand = null;
    }
}

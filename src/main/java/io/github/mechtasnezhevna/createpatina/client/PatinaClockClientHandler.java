package io.github.mechtasnezhevna.createpatina.client;

import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.network.PatinaClockActionPayload;
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

public final class PatinaClockClientHandler {

    private static final int LONG_PRESS_TICKS = 5;
    private static final int TABLE_LONG_PRESS_TICKS = 10;

    private static int heldTicks = -1;
    private static BlockPos heldPos;
    private static Direction heldFace;
    private static InteractionHand heldHand;
    private static boolean heldShift;
    private static boolean heldIsCopperTableCloth;

    private PatinaClockClientHandler() {
    }

    // verified: NeoForge 21.1.228 PlayerInteractEvent.RightClickBlock source, 2026-07-26
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide
                || !(event.getItemStack().getItem() instanceof PatinaClockItem)
                || !PatinaClockItem.canInteractWith(event.getLevel().getBlockState(event.getPos()))) {
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
        heldShift = event.getEntity().isShiftKeyDown();
        heldIsCopperTableCloth = PatinaClockItem.isCopperTableCloth(event.getLevel().getBlockState(event.getPos()));
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
        if (!(heldStack.getItem() instanceof PatinaClockItem)
                || !(minecraft.hitResult instanceof BlockHitResult hitResult)
                || !hitResult.getBlockPos().equals(heldPos)) {
            cancel();
            return;
        }

        if (!minecraft.options.keyUse.isDown()) {
            int row = heldIsCopperTableCloth && !heldShift
                    ? PatinaClockActionPayload.PLACE_ON_TABLE_ROW
                    : PatinaClockActionPayload.SHORT_ACTION_ROW;
            // verified: NeoForge 21.1.228 PacketDistributor source, 2026-07-26
            PacketDistributor.sendToServer(new PatinaClockActionPayload(
                    heldPos, row, 0, heldFace
            ));
            cancel();
            return;
        }

        BlockState state = minecraft.level.getBlockState(heldPos);
        int longPressTicks = heldIsCopperTableCloth ? TABLE_LONG_PRESS_TICKS : LONG_PRESS_TICKS;
        if (++heldTicks < longPressTicks
                || !PatinaClockItem.canAdjustState(state)
                || !(state.getBlock() instanceof PatinaBlock patinaBlock)) {
            return;
        }

        ScreenOpener.open(new PatinaClockValueSettingsScreen(
                heldPos, heldFace, patinaBlock.getType()
        ));
        cancel();
    }

    private static void cancel() {
        heldTicks = -1;
        heldPos = null;
        heldFace = null;
        heldHand = null;
        heldShift = false;
        heldIsCopperTableCloth = false;
    }
}

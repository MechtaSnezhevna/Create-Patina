package io.github.mechtasnezhevna.createpatina.client;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = CreatePatina.MODID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class PatinaClockClientHandler {

    private static final int LONG_PRESS_TICKS = 5;

    private static int heldTicks = -1;
    private static BlockPos heldPos;
    private static Direction heldFace;
    private static InteractionHand heldHand;

    private PatinaClockClientHandler() {
    }

    // verified: Forge 1.20.1-47.1.33 PlayerInteractEvent.RightClickBlock source, 2026-07-30
    @SubscribeEvent
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
    }

    // verified: Forge 1.20.1-47.1.33 TickEvent.ClientTickEvent source, 2026-07-30
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || heldTicks == -1) {
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
            PatinaClockActionPayload.sendToServer(new PatinaClockActionPayload(
                    heldPos, PatinaClockActionPayload.SHORT_ACTION_ROW, 0, heldFace
            ));
            cancel();
            return;
        }

        BlockState state = minecraft.level.getBlockState(heldPos);
        if (++heldTicks < LONG_PRESS_TICKS
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
    }
}

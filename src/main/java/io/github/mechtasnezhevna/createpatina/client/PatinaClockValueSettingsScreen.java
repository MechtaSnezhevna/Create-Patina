package io.github.mechtasnezhevna.createpatina.client;

import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.mechtasnezhevna.createpatina.item.PatinaClockItem;
import io.github.mechtasnezhevna.createpatina.network.PatinaClockActionPayload;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class PatinaClockValueSettingsScreen extends AbstractSimiScreen {

    private static final int STATE_COUNT = 8;
    private static final int STATES_PER_ROW = 4;
    private static final int FACE_RADIUS = 72;
    private static final int SLOT_RADIUS = 58;
    private static final int INNER_DEAD_ZONE_SQUARED = 20 * 20;
    private static final int OUTER_HOVER_RADIUS_SQUARED = 92 * 92;

    private static final int FACE_BORDER_COLOR = 0xFF9B6A32;
    private static final int FACE_COLOR = 0xED201A17;
    private static final int HAND_COLOR = 0xFFFBDC7D;
    private static final int UNWAXED_COLOR = 0xFFD98A48;
    private static final int WAXED_COLOR = 0xFF63B6AE;

    private final BlockPos targetPos;
    private final Direction clickedFace;
    private int selectedState;
    private int ticksOpen;
    private int soundCoolDown;

    public PatinaClockValueSettingsScreen(BlockPos pos, Direction clickedFace, WeatheringType initialType) {
        this.targetPos = pos;
        this.clickedFace = clickedFace;
        this.selectedState = PatinaClockItem.rowFor(initialType) * STATES_PER_ROW
                + PatinaClockItem.valueFor(initialType);
    }

    @Override
    protected void init() {
        super.init();
        setCursorToState(selectedState);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float fade = Mth.clamp(
                (ticksOpen + AnimationTickHolder.getPartialTicks()) / 8f,
                1 / 512f,
                1
        );
        int centerX = width / 2;
        int centerY = height / 2 - 8;

        updateHoveredState(mouseX, mouseY);
        drawClockFace(graphics, centerX, centerY, fade);
        drawClockHand(graphics, centerX, centerY, selectedState);
        drawStateSlots(graphics, centerX, centerY);
        drawClockText(graphics, centerX, centerY, fade);
    }

    private void drawClockFace(GuiGraphics graphics, int centerX, int centerY, float fade) {
        int borderColor = withFade(FACE_BORDER_COLOR, fade);
        int faceColor = withFade(FACE_COLOR, fade);
        drawFilledCircle(graphics, centerX, centerY, FACE_RADIUS, borderColor);
        drawFilledCircle(graphics, centerX, centerY, FACE_RADIUS - 3, faceColor);

        for (int state = 0; state < STATE_COUNT; state++) {
            double angle = angleForState(state);
            int innerX = centerX + (int) Math.round(Math.sin(angle) * 39);
            int innerY = centerY - (int) Math.round(Math.cos(angle) * 39);
            int outerX = centerX + (int) Math.round(Math.sin(angle) * 47);
            int outerY = centerY - (int) Math.round(Math.cos(angle) * 47);
            drawLine(graphics, innerX, innerY, outerX, outerY, withFade(0xFFA77945, fade), 1);
        }
    }

    private void drawClockHand(GuiGraphics graphics, int centerX, int centerY, int state) {
        double angle = angleForState(state);
        int handX = centerX + (int) Math.round(Math.sin(angle) * 39);
        int handY = centerY - (int) Math.round(Math.cos(angle) * 39);
        drawLine(graphics, centerX, centerY, handX, handY, HAND_COLOR, 2);
        graphics.fill(centerX - 3, centerY - 3, centerX + 4, centerY + 4, FACE_BORDER_COLOR);
        graphics.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, HAND_COLOR);
    }

    private void drawStateSlots(GuiGraphics graphics, int centerX, int centerY) {
        for (int state = 0; state < STATE_COUNT; state++) {
            double angle = angleForState(state);
            int slotX = centerX + (int) Math.round(Math.sin(angle) * SLOT_RADIUS);
            int slotY = centerY - (int) Math.round(Math.cos(angle) * SLOT_RADIUS);

            AllGuiTextures.TOOLBELT_SLOT.render(graphics, slotX - 11, slotY - 11);
            if (state == selectedState) {
                AllGuiTextures.TOOLBELT_SLOT_HIGHLIGHT.render(graphics, slotX - 12, slotY - 12);
            }

            Component number = Component.literal(Integer.toString(state % STATES_PER_ROW + 1));
            int color = state < STATES_PER_ROW ? UNWAXED_COLOR : WAXED_COLOR;
            graphics.drawCenteredString(font, number, slotX, slotY - 4, color);
        }
    }

    private void drawClockText(GuiGraphics graphics, int centerX, int centerY, float fade) {
        int textColor = withFade(0xFFDDDDDD, fade);
        int selectedColor = withFade(HAND_COLOR, fade);
        int row = selectedState / STATES_PER_ROW;
        int value = selectedState % STATES_PER_ROW;
        WeatheringType type = PatinaClockItem.typeFor(row, value);

        Component title = Component.translatable("gui.createpatina.patina_clock.title");
        Component stateName = Component.translatable(
                "gui.createpatina.weathering_type." + type.name().toLowerCase()
        );
        Component waxState = Component.translatable(
                row == 0
                        ? "gui.createpatina.patina_clock.unwaxed"
                        : "gui.createpatina.patina_clock.waxed"
        );
        Component tip = CreateLang.translateDirect(
                "gui.value_settings.release_to_confirm",
                Component.keybind("key.use")
        );

        graphics.drawCenteredString(font, title, centerX, centerY - FACE_RADIUS - 25, textColor);
        graphics.drawCenteredString(font, stateName, centerX, centerY - 10, selectedColor);
        graphics.drawCenteredString(
                font,
                waxState,
                centerX,
                centerY + 5,
                row == 0 ? withFade(UNWAXED_COLOR, fade) : withFade(WAXED_COLOR, fade)
        );
        graphics.drawCenteredString(font, tip, centerX, centerY + FACE_RADIUS + 17, textColor);
    }

    /*
     * Original Create code from RadialToolboxMenu#renderWindow:
     * float hoveredX = mouseX - window.getGuiScaledWidth() / 2;
     * float hoveredY = mouseY - window.getGuiScaledHeight() / 2;
     *
     * float distance = hoveredX * hoveredX + hoveredY * hoveredY;
     * if (distance > 25 && distance < 10000)
     *     hoveredSlot =
     *         (Mth.floor((AngleHelper.deg(Mth.atan2(hoveredY, hoveredX)) + 360 + 180 - 22.5f)) % 360)
     *             / 45;
     */
    private void updateHoveredState(double mouseX, double mouseY) {
        double hoveredX = mouseX - width / 2.0;
        double hoveredY = mouseY - (height / 2.0 - 8);
        double distanceSquared = hoveredX * hoveredX + hoveredY * hoveredY;
        if (distanceSquared <= INNER_DEAD_ZONE_SQUARED
                || distanceSquared >= OUTER_HOVER_RADIUS_SQUARED) {
            return;
        }

        double clockwiseDegrees = (Math.toDegrees(Math.atan2(hoveredY, hoveredX)) + 450) % 360;
        int hoveredState = Mth.floor((clockwiseDegrees + 22.5) / 45.0) % STATE_COUNT;
        setSelectedState(hoveredState);
    }

    private void setSelectedState(int state) {
        int wrappedState = Math.floorMod(state, STATE_COUNT);
        if (wrappedState == selectedState) {
            return;
        }

        selectedState = wrappedState;
        if (ticksOpen > 0 && soundCoolDown == 0) {
            float pitch = Mth.lerp(selectedState / 7f, 1.15f, 1.5f);
            minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(AllSoundEvents.SCROLL_VALUE.getMainEvent(), pitch, 0.25F)
            );
            soundCoolDown = 1;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta == 0) {
            return false;
        }

        setSelectedState(selectedState + (int) Math.signum(delta));
        setCursorToState(selectedState);
        return true;
    }

    private void setCursorToState(int state) {
        double angle = angleForState(state);
        double guiX = width / 2.0 + Math.sin(angle) * SLOT_RADIUS;
        double guiY = height / 2.0 - 8 - Math.cos(angle) * SLOT_RADIUS;

        // verified: Minecraft 1.20.1 Window/GLFW usage in Create ValueSettingsScreen, 2026-07-30
        Window window = minecraft.getWindow();
        double guiScale = window.getGuiScale();
        GLFW.glfwSetCursorPos(window.getWindow(), guiX * guiScale, guiY * guiScale);
    }

    /*
     * Original Create code from ValueSettingsScreen#keyReleased:
     * if (minecraft.options.keyUse.matches(pKeyCode, pScanCode)) {
     *     Window window = minecraft.getWindow();
     *     double x = minecraft.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
     *     double y = minecraft.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
     *     saveAndClose(x, y);
     *     return true;
     * }
     * return super.keyReleased(pKeyCode, pScanCode, pModifiers);
     */
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (minecraft.options.keyUse.matches(keyCode, scanCode)) {
            Window window = minecraft.getWindow();
            double mouseX = minecraft.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
            double mouseY = minecraft.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
            updateHoveredState(mouseX, mouseY);
            saveAndClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    /*
     * Original Create code from ValueSettingsScreen#mouseReleased:
     * if (minecraft.options.keyUse.matchesMouse(pButton)) {
     *     saveAndClose(pMouseX, pMouseY);
     *     return true;
     * }
     * return super.mouseReleased(pMouseX, pMouseY, pButton);
     */
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (minecraft.options.keyUse.matchesMouse(button)) {
            updateHoveredState(mouseX, mouseY);
            saveAndClose();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void saveAndClose() {
        int row = selectedState / STATES_PER_ROW;
        int value = selectedState % STATES_PER_ROW;
        PatinaClockActionPayload.sendToServer(new PatinaClockActionPayload(
                targetPos, row, value, clickedFace
        ));
        onClose();
    }

    /*
     * Original Create code from ValueSettingsScreen#renderBackground:
     * int a = ((int) (0x50 * Math.min(1, (ticksOpen + AnimationTickHolder.getPartialTicks()) / 20f))) << 24;
     * graphics.fillGradient(0, 0, this.width, this.height, 0x101010 | a, 0x101010 | a);
     */
    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
        int alpha = (int) (0x70 * Math.min(
                1,
                (ticksOpen + AnimationTickHolder.getPartialTicks()) / 20f
        )) << 24;
        graphics.fillGradient(0, 0, width, height, 0x101010 | alpha, 0x101010 | alpha);
    }

    @Override
    public void tick() {
        ticksOpen++;
        if (soundCoolDown > 0) {
            soundCoolDown--;
        }
        super.tick();
    }

    private static double angleForState(int state) {
        return Math.toRadians(state * 45.0);
    }

    private static int withFade(int color, float fade) {
        int alpha = (int) (((color >>> 24) & 0xFF) * fade);
        return color & 0x00FFFFFF | alpha << 24;
    }

    private static void drawFilledCircle(
            GuiGraphics graphics,
            int centerX,
            int centerY,
            int radius,
            int color
    ) {
        for (int y = -radius; y <= radius; y++) {
            int halfWidth = (int) Math.sqrt(radius * radius - y * y);
            graphics.fill(centerX - halfWidth, centerY + y, centerX + halfWidth + 1, centerY + y + 1, color);
        }
    }

    private static void drawLine(
            GuiGraphics graphics,
            int startX,
            int startY,
            int endX,
            int endY,
            int color,
            int thickness
    ) {
        int dx = endX - startX;
        int dy = endY - startY;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        for (int step = 0; step <= steps; step++) {
            float progress = steps == 0 ? 0 : step / (float) steps;
            int x = Math.round(Mth.lerp(progress, startX, endX));
            int y = Math.round(Mth.lerp(progress, startY, endY));
            graphics.fill(x - thickness / 2, y - thickness / 2, x + (thickness + 1) / 2, y + (thickness + 1) / 2, color);
        }
    }
}

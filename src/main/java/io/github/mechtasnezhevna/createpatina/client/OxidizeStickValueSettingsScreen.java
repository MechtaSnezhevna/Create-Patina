package io.github.mechtasnezhevna.createpatina.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour.ValueSettings;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsScreen;
import io.github.mechtasnezhevna.createpatina.item.OxidizeStickItem;
import io.github.mechtasnezhevna.createpatina.network.OxidizeStickActionPayload;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class OxidizeStickValueSettingsScreen extends ValueSettingsScreen {

    private static final int LOGICAL_MAX_VALUE = 3;
    private static final int COLUMN_STRIDE = 16;

    private final BlockPos targetPos;
    private final Direction clickedFace;

    public OxidizeStickValueSettingsScreen(BlockPos pos, Direction clickedFace, WeatheringType initialType) {
        super(
                pos,
                createBoard(),
                new ValueSettings(
                        OxidizeStickItem.rowFor(initialType),
                        OxidizeStickItem.valueFor(initialType)
                ),
                ignored -> {
                },
                0
        );
        this.targetPos = pos;
        this.clickedFace = clickedFace;
    }

    private static ValueSettingsBoard createBoard() {
        return new ValueSettingsBoard(
                Component.translatable("gui.createpatina.oxidize_stick.title"),
                LOGICAL_MAX_VALUE * COLUMN_STRIDE,
                COLUMN_STRIDE,
                ImmutableList.of(
                        Component.translatable("gui.createpatina.oxidize_stick.unwaxed"),
                        Component.translatable("gui.createpatina.oxidize_stick.waxed")
                ),
                new ValueSettingsFormatter(OxidizeStickValueSettingsScreen::formatSetting)
        );
    }

    private static MutableComponent formatSetting(ValueSettings settings) {
        WeatheringType type = OxidizeStickItem.typeFor(settings.row(), settings.value());
        return Component.translatable("gui.createpatina.weathering_type." + type.name().toLowerCase());
    }

    /*
     * Original Create code from ValueSettingsScreen#getClosestCoordinate:
     * int row = 0;
     * int column = 0;
     * boolean milestonesOnly = hasShiftDown();
     *
     * double bestDiff = Double.MAX_VALUE;
     * for (; row < board.rows().size(); row++) {
     *     Vec2 coord = getCoordinateOfValue(row, 0);
     *     double diff = Math.abs(coord.y - mouseY);
     *     if (bestDiff < diff)
     *         break;
     *     bestDiff = diff;
     * }
     * row -= 1;
     *
     * bestDiff = Double.MAX_VALUE;
     * for (; column <= board.maxValue(); column++) {
     *     Vec2 coord = getCoordinateOfValue(row, milestonesOnly ? column * board.milestoneInterval() : column);
     *     double diff = Math.abs(coord.x - mouseX);
     *     if (bestDiff < diff)
     *         break;
     *     bestDiff = diff;
     * }
     * column -= 1;
     *
     * return new ValueSettings(row,
     *     milestonesOnly ? Math.min(column * board.milestoneInterval(), board.maxValue()) : column);
     */
    @Override
    public ValueSettings getClosestCoordinate(int mouseX, int mouseY) {
        int closestRow = 0;
        double closestRowDistance = Double.MAX_VALUE;
        for (int row = 0; row < 2; row++) {
            Vec2 coordinate = getCoordinateOfValue(row, 0);
            double distance = Math.abs(coordinate.y - mouseY);
            if (distance < closestRowDistance) {
                closestRowDistance = distance;
                closestRow = row;
            }
        }

        int closestValue = 0;
        double closestValueDistance = Double.MAX_VALUE;
        for (int value = 0; value <= LOGICAL_MAX_VALUE; value++) {
            Vec2 coordinate = getCoordinateOfValue(closestRow, value);
            double distance = Math.abs(coordinate.x - mouseX);
            if (distance < closestValueDistance) {
                closestValueDistance = distance;
                closestValue = value;
            }
        }

        return new ValueSettings(closestRow, closestValue);
    }

    /*
     * Original Create code from ValueSettingsScreen#getCoordinateOfValue:
     * int scale = board.maxValue() > 128 ? 1 : 2;
     * float xOut =
     *     guiLeft + ((Math.max(1, column) - 1) / board.milestoneInterval()) * milestoneSize + column * scale + 1.5f;
     * xOut += maxLabelWidth + 14 + 4;
     *
     * if (column % board.milestoneInterval() == 0)
     *     xOut += milestoneSize / 2;
     * if (column > 0)
     *     xOut += milestoneSize;
     *
     * float yOut = guiTop + (row + .5f) * 11 - .5f;
     * return new Vec2(xOut, yOut);
     */
    @Override
    public Vec2 getCoordinateOfValue(int row, int column) {
        return super.getCoordinateOfValue(row, column * COLUMN_STRIDE);
    }

    /*
     * Original Create code from ValueSettingsScreen#mouseScrolled:
     * ValueSettings closest = getClosestCoordinate((int) pMouseX, (int) pMouseY);
     * int column = closest.value() + ((int) Math.signum(pScrollY)) * (hasShiftDown() ? board.milestoneInterval() : 1);
     * column = Mth.clamp(column, 0, board.maxValue());
     * if (column == closest.value())
     *     return false;
     * setCursor(getCoordinateOfValue(closest.row(), column));
     * return true;
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        ValueSettings closest = getClosestCoordinate((int) mouseX, (int) mouseY);
        int value = Mth.clamp(
                closest.value() + (int) Math.signum(scrollY),
                0,
                LOGICAL_MAX_VALUE
        );
        if (value == closest.value()) {
            return false;
        }

        setCursor(getCoordinateOfValue(closest.row(), value));
        return true;
    }

    private void setCursor(Vec2 coordinate) {
        // verified: Minecraft 1.21.1 Window/GLFW usage in Create ValueSettingsScreen, 2026-07-26
        Window window = minecraft.getWindow();
        double guiScale = window.getGuiScale();
        GLFW.glfwSetCursorPos(window.getWindow(), coordinate.x * guiScale, coordinate.y * guiScale);
    }

    @Override
    protected void saveAndClose(double mouseX, double mouseY) {
        ValueSettings closest = getClosestCoordinate((int) mouseX, (int) mouseY);
        // verified: NeoForge 21.1.228 PacketDistributor source, 2026-07-26
        PacketDistributor.sendToServer(new OxidizeStickActionPayload(
                targetPos, closest.row(), closest.value(), clickedFace
        ));
        onClose();
    }
}

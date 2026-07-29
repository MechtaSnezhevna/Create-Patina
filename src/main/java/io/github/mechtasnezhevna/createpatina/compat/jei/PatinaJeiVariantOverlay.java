package io.github.mechtasnezhevna.createpatina.compat.jei;

import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.mixin.compat.jei.IngredientGridWithNavigationAccessor;
import io.github.mechtasnezhevna.createpatina.mixin.compat.jei.IngredientListOverlayAccessor;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class PatinaJeiVariantOverlay {

    private static final int CELL_SIZE = 18;
    private static final int COLUMNS = 4;
    private static final int PADDING = 3;
    private static final int PANEL_WIDTH = PADDING * 2 + COLUMNS * CELL_SIZE;
    private static final int PANEL_HEIGHT = PADDING * 2 + 2 * CELL_SIZE;
    private static final int SCREEN_MARGIN = 4;
    private static final float PANEL_Z = 500.0F;

    private static final int BACKGROUND_COLOR = 0xFF101010;
    private static final int BORDER_COLOR = 0xFFA0A0A0;
    private static final int HOVER_COLOR = 0x60FFFFFF;

    private final Map<Item, PatinaSet> setsByRepresentative = new LinkedHashMap<>();

    @Nullable
    private IJeiRuntime runtime;
    @Nullable
    private PatinaSet activeSet;
    private List<ItemStack> activeVariants = List.of();
    @Nullable
    private Bounds panelBounds;
    private boolean renderingVariantTooltip;

    void setRuntime(@Nullable IJeiRuntime runtime) {
        this.runtime = runtime;
        if (runtime != null) {
            rebuildSetIndex();
        } else {
            clear();
        }
    }

    private void rebuildSetIndex() {
        setsByRepresentative.clear();
        for (PatinaSet set : PatinaSet.all()) {
            ItemStack representative = set.get(WeatheringType.UNAFFECTED).asItem().getDefaultInstance();
            if (!representative.isEmpty()) {
                setsByRepresentative.put(representative.getItem(), set);
            }
        }
    }

    void renderAfterJei(Screen screen, GuiGraphics graphics, int mouseX, int mouseY) {
        IJeiRuntime currentRuntime = runtime;
        if (currentRuntime == null || !PatinaConfig.CLIENT.COLLAPSE_PATINA_SETS_IN_JEI.get()) {
            clear();
            return;
        }

        IIngredientListOverlay ingredientOverlay = currentRuntime.getIngredientListOverlay();
        if (!ingredientOverlay.isListDisplayed()) {
            clear();
            return;
        }

        Bounds currentPanelBounds = panelBounds;
        if (currentPanelBounds != null && currentPanelBounds.contains(mouseX, mouseY)) {
            renderPanel(graphics, mouseX, mouseY);
            return;
        }

        HoveredSource hoveredSource = findHoveredSource(ingredientOverlay, mouseX, mouseY);
        if (hoveredSource != null) {
            activate(hoveredSource.set(), hoveredSource.bounds(), mouseY, screen.width, screen.height);
        } else {
            clear();
            return;
        }

        renderPanel(graphics, mouseX, mouseY);
    }

    void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        // verified: NeoForge 21.1.228 ScreenEvent.MouseButtonPressed.Pre source, 2026-07-28
        IJeiRuntime currentRuntime = runtime;
        if (currentRuntime == null || !PatinaConfig.CLIENT.COLLAPSE_PATINA_SETS_IN_JEI.get()) {
            return;
        }

        Bounds bounds = panelBounds;
        if (bounds == null || !bounds.contains(event.getMouseX(), event.getMouseY())) {
            return;
        }

        ItemStack clicked = getVariantAt(event.getMouseX(), event.getMouseY());
        if (!clicked.isEmpty() && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            showRecipes(currentRuntime, clicked);
            clear();
        } else if (!clicked.isEmpty() && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            showUses(currentRuntime, clicked);
            clear();
        }

        // The complete panel is an input surface, including its padding and empty cell.
        event.setCanceled(true);
    }

    void onRenderTooltipPre(RenderTooltipEvent.Pre event) {
        Bounds bounds = panelBounds;
        if (!renderingVariantTooltip && bounds != null && bounds.contains(event.getX(), event.getY())) {
            // Prevent container, JEI, and other underlying slots from rendering through the panel.
            event.setCanceled(true);
        }
    }

    boolean isMouseOverPanel(double mouseX, double mouseY) {
        Bounds bounds = panelBounds;
        return bounds != null && bounds.contains(mouseX, mouseY);
    }

    @Nullable
    private HoveredSource findHoveredSource(IIngredientListOverlay ingredientOverlay, int mouseX, int mouseY) {
        if (!(ingredientOverlay instanceof IngredientListOverlay concreteOverlay)) {
            return null;
        }

        IngredientGridWithNavigation contents =
                ((IngredientListOverlayAccessor) concreteOverlay).createpatina$getContents();
        IngredientGrid ingredientGrid =
                ((IngredientGridWithNavigationAccessor) contents).createpatina$getIngredientGrid();

        return ingredientGrid.getSlots()
                .filter(slot -> slot.isMouseOver(mouseX, mouseY))
                .flatMap(slot -> slot.getOptionalElement().stream()
                        .flatMap(element -> element.getTypedIngredient()
                                .getIngredient(VanillaTypes.ITEM_STACK).stream())
                        .map(stack -> {
                            PatinaSet set = setsByRepresentative.get(stack.getItem());
                            if (set == null) {
                                return null;
                            }
                            ImmutableRect2i area = slot.getArea();
                            return new HoveredSource(set,
                                    new Bounds(area.getX(), area.getY(), area.getWidth(), area.getHeight()));
                        }))
                .filter(source -> source != null)
                .findFirst()
                .orElse(null);
    }

    private void activate(PatinaSet set, Bounds sourceBounds, int mouseY, int screenWidth, int screenHeight) {
        if (activeSet != set) {
            activeSet = set;
            EnumMap<WeatheringType, ItemStack> variants = new EnumMap<>(WeatheringType.class);
            set.entries().forEach((type, entry) -> {
                if (type != WeatheringType.UNAFFECTED) {
                    ItemStack stack = entry.asStack();
                    if (!stack.isEmpty()) {
                        variants.put(type, stack);
                    }
                }
            });
            activeVariants = variants.values().stream().toList();
        }

        int panelX;
        if (sourceBounds.x - PANEL_WIDTH >= SCREEN_MARGIN) {
            panelX = sourceBounds.x - PANEL_WIDTH;
        } else {
            panelX = Math.min(screenWidth - SCREEN_MARGIN - PANEL_WIDTH, sourceBounds.right());
        }
        int panelY = clamp(mouseY - PANEL_HEIGHT / 2, SCREEN_MARGIN,
                Math.max(SCREEN_MARGIN, screenHeight - SCREEN_MARGIN - PANEL_HEIGHT));

        panelBounds = new Bounds(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private void renderPanel(GuiGraphics graphics, int mouseX, int mouseY) {
        Bounds bounds = panelBounds;
        if (bounds == null) {
            return;
        }

        /*
         * Minecraft 1.21.1 GuiGraphics#renderItem renders at a base Z of 150,
         * while GuiGraphics#renderTooltipInternal renders at Z 400.
         * Use a higher base for the complete Patina overlay so previously rendered
         * 3D item models and JEI tooltips cannot remain in front through depth testing.
         * verified: NeoForge-patched Minecraft 1.21.1 GuiGraphics source, 2026-07-28
         */
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, PANEL_Z);
        try {
            graphics.fill(bounds.x, bounds.y, bounds.right(), bounds.bottom(), BACKGROUND_COLOR);
            drawBorder(graphics, bounds);

            ItemStack hovered = ItemStack.EMPTY;
            for (int index = 0; index < activeVariants.size(); index++) {
                int x = bounds.x + PADDING + index % COLUMNS * CELL_SIZE;
                int y = bounds.y + PADDING + index / COLUMNS * CELL_SIZE;
                if (containsCell(x, y, mouseX, mouseY)) {
                    graphics.fill(x, y, x + CELL_SIZE, y + CELL_SIZE, HOVER_COLOR);
                    hovered = activeVariants.get(index);
                }
                ItemStack stack = activeVariants.get(index);
                graphics.renderItem(stack, x + 1, y + 1);
                graphics.renderItemDecorations(Minecraft.getInstance().font, stack, x + 1, y + 1);
            }

            if (!hovered.isEmpty()) {
                renderingVariantTooltip = true;
                try {
                    graphics.renderTooltip(Minecraft.getInstance().font, hovered, mouseX, mouseY);
                } finally {
                    renderingVariantTooltip = false;
                }
            }
        } finally {
            graphics.pose().popPose();
        }
    }

    private static void drawBorder(GuiGraphics graphics, Bounds bounds) {
        graphics.fill(bounds.x, bounds.y, bounds.right(), bounds.y + 1, BORDER_COLOR);
        graphics.fill(bounds.x, bounds.bottom() - 1, bounds.right(), bounds.bottom(), BORDER_COLOR);
        graphics.fill(bounds.x, bounds.y, bounds.x + 1, bounds.bottom(), BORDER_COLOR);
        graphics.fill(bounds.right() - 1, bounds.y, bounds.right(), bounds.bottom(), BORDER_COLOR);
    }

    private ItemStack getVariantAt(double mouseX, double mouseY) {
        Bounds bounds = panelBounds;
        if (bounds == null || !bounds.contains(mouseX, mouseY)) {
            return ItemStack.EMPTY;
        }

        for (int index = 0; index < activeVariants.size(); index++) {
            int x = bounds.x + PADDING + index % COLUMNS * CELL_SIZE;
            int y = bounds.y + PADDING + index / COLUMNS * CELL_SIZE;
            if (containsCell(x, y, mouseX, mouseY)) {
                return activeVariants.get(index);
            }
        }
        return ItemStack.EMPTY;
    }

    private static void showRecipes(IJeiRuntime runtime, ItemStack stack) {
        IFocusFactory focusFactory = runtime.getJeiHelpers().getFocusFactory();
        IFocus<ItemStack> output = focusFactory.createFocus(
                RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, stack);
        // verified: JEI 19.27.0.340 IRecipesGui and IFocusFactory sources, 2026-07-28
        runtime.getRecipesGui().show(output);
    }

    private static void showUses(IJeiRuntime runtime, ItemStack stack) {
        IFocusFactory focusFactory = runtime.getJeiHelpers().getFocusFactory();
        IFocus<ItemStack> input = focusFactory.createFocus(
                RecipeIngredientRole.INPUT, VanillaTypes.ITEM_STACK, stack);
        IFocus<ItemStack> catalyst = focusFactory.createFocus(
                RecipeIngredientRole.CATALYST, VanillaTypes.ITEM_STACK, stack);
        // Match JEI's own "show uses" behavior by checking input and catalyst roles.
        runtime.getRecipesGui().show(List.of(input, catalyst));
    }

    private void clear() {
        activeSet = null;
        activeVariants = List.of();
        panelBounds = null;
        renderingVariantTooltip = false;
    }

    private static boolean containsCell(int x, int y, double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + CELL_SIZE && mouseY >= y && mouseY < y + CELL_SIZE;
    }

    private static int clamp(int value, int minimum, int maximum) {
        return Math.max(minimum, Math.min(value, maximum));
    }

    private record Bounds(int x, int y, int width, int height) {
        int right() {
            return x + width;
        }

        int bottom() {
            return y + height;
        }

        boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX < right() && mouseY >= y && mouseY < bottom();
        }
    }

    private record HoveredSource(PatinaSet set, Bounds bounds) {
    }
}

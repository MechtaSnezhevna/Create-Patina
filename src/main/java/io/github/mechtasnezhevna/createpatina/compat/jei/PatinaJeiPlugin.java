package io.github.mechtasnezhevna.createpatina.compat.jei;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JeiPlugin
public class PatinaJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = CreatePatina.asResource("jei_plugin");
    private static final PatinaJeiVariantOverlay VARIANT_OVERLAY = new PatinaJeiVariantOverlay();
    private static boolean eventListenersRegistered;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        if (!PatinaConfig.CLIENT.COLLAPSE_PATINA_SETS_IN_JEI.get()) {
            return;
        }

        for (PatinaSet set : PatinaSet.all()) {
            ItemStack representative = set.get(WeatheringType.UNAFFECTED).asItem().getDefaultInstance();
            if (representative.isEmpty()) {
                continue;
            }
            Set<String> aliases = new LinkedHashSet<>();
            for (var entry : set.entries().entrySet()) {
                if (entry.getKey() != WeatheringType.UNAFFECTED) {
                    ItemStack variant = entry.getValue().asStack();
                    if (!variant.isEmpty()) {
                        aliases.add(variant.getDescriptionId());
                    }
                }
            }
            if (!aliases.isEmpty()) {
                // verified: JEI 19.27.0.340 IIngredientAliasRegistration source, 2026-07-28
                registration.addAliases(VanillaTypes.ITEM_STACK, representative, aliases);
            }
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!PatinaConfig.CLIENT.COLLAPSE_PATINA_SETS_IN_JEI.get()) {
            return;
        }

        Map<Item, ItemStack> hiddenVariants = new LinkedHashMap<>();
        for (PatinaSet set : PatinaSet.all()) {
            for (var entry : set.entries().entrySet()) {
                if (entry.getKey() != WeatheringType.UNAFFECTED) {
                    ItemStack stack = entry.getValue().asStack();
                    if (!stack.isEmpty()) {
                        hiddenVariants.putIfAbsent(stack.getItem(), stack);
                    }
                }
            }
        }

        if (!hiddenVariants.isEmpty()) {
            // verified: JEI 19.27.0.340 IIngredientManager source and JEI hiding guide, 2026-07-28
            registration.getIngredientManager()
                    .removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, new ArrayList<>(hiddenVariants.values()));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        VARIANT_OVERLAY.setRuntime(jeiRuntime);
        if (!eventListenersRegistered) {
            eventListenersRegistered = true;
            // verified: NeoForge 21.1.228 ScreenEvent source, 2026-07-28
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, VARIANT_OVERLAY::onMouseClicked);
            // verified: NeoForge 21.1.228 RenderTooltipEvent.Pre source, 2026-07-28
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, VARIANT_OVERLAY::onRenderTooltipPre);
        }
    }

    @Override
    public void onRuntimeUnavailable() {
        VARIANT_OVERLAY.setRuntime(null);
    }

    public static void renderAfterJei(Screen screen, GuiGraphics graphics, int mouseX, int mouseY) {
        VARIANT_OVERLAY.renderAfterJei(screen, graphics, mouseX, mouseY);
    }

    public static boolean isMouseOverVariantPanel(double mouseX, double mouseY) {
        return VARIANT_OVERLAY.isMouseOverPanel(mouseX, mouseY);
    }
}

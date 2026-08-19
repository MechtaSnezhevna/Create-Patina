package io.github.mechtasnezhevna.createpatina.compat.jei;

import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.recipe.HoneyingRecipe;
import io.github.mechtasnezhevna.createpatina.registry.PatinaRecipeTypes;
import io.github.mechtasnezhevna.createpatina.registry.util.PatinaSet;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@JeiPlugin
public class PatinaJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = CreatePatina.asResource("jei_plugin");
    private static final PatinaJeiVariantOverlay VARIANT_OVERLAY = new PatinaJeiVariantOverlay();
    private static boolean eventListenersRegistered;
    private CreateRecipeCategory<HoneyingRecipe> honeyingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(getHoneyingCategory());
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
                    ItemStack variant = entry.getValue().get().asItem().getDefaultInstance();
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
        getHoneyingCategory().registerRecipes(registration);

        if (!PatinaConfig.CLIENT.COLLAPSE_PATINA_SETS_IN_JEI.get()) {
            return;
        }

        Map<Item, ItemStack> hiddenVariants = new LinkedHashMap<>();
        for (PatinaSet set : PatinaSet.all()) {
            for (var entry : set.entries().entrySet()) {
                if (entry.getKey() != WeatheringType.UNAFFECTED) {
                    ItemStack stack = entry.getValue().get().asItem().getDefaultInstance();
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
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        getHoneyingCategory().registerCatalysts(registration);
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

    private CreateRecipeCategory<HoneyingRecipe> getHoneyingCategory() {
        if (honeyingCategory == null) {
            honeyingCategory = new CreateRecipeCategory.Builder<>(HoneyingRecipe.class)
                    .addTypedRecipes(PatinaRecipeTypes.HONEYING)
                    .catalystStack(ProcessingViaFanCategory.getFan("fan_honeying"))
                    .doubleItemIcon(AllItems.PROPELLER.get(), Items.HONEY_BOTTLE)
                    .emptyBackground(178, 72)
                    .build(CreatePatina.asResource("fan_honeying"), FanHoneyingCategory::new);
        }
        return honeyingCategory;
    }
}

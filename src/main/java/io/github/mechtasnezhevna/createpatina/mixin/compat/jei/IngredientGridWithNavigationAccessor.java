package io.github.mechtasnezhevna.createpatina.mixin.compat.jei;

import mezz.jei.gui.overlay.IngredientGrid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation", remap = false)
public interface IngredientGridWithNavigationAccessor {

    // verified: JEI 19.27.0.340 IngredientGridWithNavigation#ingredientGrid source, 2026-07-28
    @Accessor(value = "ingredientGrid", remap = false)
    IngredientGrid createpatina$getIngredientGrid();
}

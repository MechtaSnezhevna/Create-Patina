package io.github.mechtasnezhevna.createpatina.mixin.compat.jei;

import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "mezz.jei.gui.overlay.IngredientListOverlay", remap = false)
public interface IngredientListOverlayAccessor {

    // verified: JEI 15.20.0.106 IngredientListOverlay#contents is an IngredientGridWithNavigation, 2026-09-14
    @Accessor(value = "contents", remap = false)
    IngredientGridWithNavigation createpatina$getContents();
}

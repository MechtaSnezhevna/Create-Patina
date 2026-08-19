package io.github.mechtasnezhevna.createpatina.mixin.compat.jei;

import mezz.jei.gui.overlay.IIngredientListOverlayContents;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = "mezz.jei.gui.overlay.IngredientListOverlay", remap = false)
public interface IngredientListOverlayAccessor {

    // verified: JEI 19.27.0.340 IngredientListOverlay#contents source, 2026-07-28
    @Accessor(value = "contents", remap = false)
    IIngredientListOverlayContents createpatina$getContents();
}

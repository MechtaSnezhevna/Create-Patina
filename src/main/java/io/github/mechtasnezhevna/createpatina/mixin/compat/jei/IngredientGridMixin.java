package io.github.mechtasnezhevna.createpatina.mixin.compat.jei;

import io.github.mechtasnezhevna.createpatina.compat.jei.PatinaJeiPlugin;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.MouseUtil;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.overlay.IngredientGrid", remap = false)
public abstract class IngredientGridMixin {

    /*
     * Original JEI 19.27.0.340 code from IngredientGrid#drawHighlight:
     * guiGraphics.fillGradient(
     *     RenderType.guiOverlay(),
     *     area.getX(),
     *     area.getY(),
     *     area.getX() + area.getWidth(),
     *     area.getY() + area.getHeight(),
     *     0x80FFFFFF,
     *     0x80FFFFFF,
     *     0
     * );
     */
    @Inject(
            method = "drawHighlight(Lnet/minecraft/client/gui/GuiGraphics;Lmezz/jei/common/util/ImmutableRect2i;)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    private static void createpatina$hideHighlightBehindVariantPanel(
            GuiGraphics graphics, ImmutableRect2i area, CallbackInfo ci) {
        if (PatinaJeiPlugin.isMouseOverVariantPanel(MouseUtil.getX(), MouseUtil.getY())) {
            ci.cancel();
        }
    }
}

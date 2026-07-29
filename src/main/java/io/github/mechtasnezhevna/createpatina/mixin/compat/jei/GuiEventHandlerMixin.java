package io.github.mechtasnezhevna.createpatina.mixin.compat.jei;

import io.github.mechtasnezhevna.createpatina.compat.jei.PatinaJeiPlugin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.events.GuiEventHandler", remap = false)
public abstract class GuiEventHandlerMixin {

    /*
     * Original JEI 19.27.0.340 code from GuiEventHandler#onDrawScreenPost:
     * ingredientListOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
     * bookmarkOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
     *
     * Injecting at TAIL keeps the Patina panel and its tooltip above JEI's own tooltips.
     */
    @Inject(
            method = "onDrawScreenPost(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/GuiGraphics;II)V",
            at = @At("TAIL"),
            require = 0,
            remap = false
    )
    private void createpatina$renderVariantOverlayLast(
            Screen screen, GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        PatinaJeiPlugin.renderAfterJei(screen, graphics, mouseX, mouseY);
    }
}

package io.github.mechtasnezhevna.createpatina.compat.jei;

import com.simibubi.create.AllFluids;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import io.github.mechtasnezhevna.createpatina.recipe.HoneyingRecipe;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public final class FanHoneyingCategory extends ProcessingViaFanCategory.MultiOutput<HoneyingRecipe> {

    public FanHoneyingCategory(Info<HoneyingRecipe> info) {
        super(info);
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(AllFluids.HONEY.get())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
    }
}

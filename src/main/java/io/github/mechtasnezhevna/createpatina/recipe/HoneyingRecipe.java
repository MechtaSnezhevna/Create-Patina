package io.github.mechtasnezhevna.createpatina.recipe;

import com.simibubi.create.content.kinetics.fan.processing.SplashingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.mechtasnezhevna.createpatina.registry.PatinaRecipeTypes;
import net.minecraft.world.level.Level;

public final class HoneyingRecipe extends ProcessingRecipe<SplashingRecipe.SplashingWrapper> {

    public HoneyingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(PatinaRecipeTypes.HONEYING, params);
    }

    @Override
    public boolean matches(SplashingRecipe.SplashingWrapper input, Level level) {
        if (input.isEmpty()) {
            return false;
        }
        return ingredients.get(0).test(input.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }
}

package io.github.mechtasnezhevna.createpatina.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import io.github.mechtasnezhevna.createpatina.registry.PatinaRecipeTypes;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class HoneyingRecipe extends StandardProcessingRecipe<SingleRecipeInput> {

    public HoneyingRecipe(ProcessingRecipeParams params) {
        super(PatinaRecipeTypes.HONEYING, params);
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return !input.isEmpty() && ingredients.getFirst().test(input.getItem(0));
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

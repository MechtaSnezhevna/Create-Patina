package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.recipe.HoneyingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Optional;

public enum PatinaRecipeTypes implements IRecipeTypeInfo {
    HONEYING(HoneyingRecipe::new);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializer;
    private final RegistryObject<RecipeType<?>> type;

    PatinaRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> factory) {
        String name = name().toLowerCase(Locale.ROOT);
        id = CreatePatina.asResource(name);
        serializer = Registers.SERIALIZERS.register(name, () -> new ProcessingRecipeSerializer<>(factory));
        type = Registers.TYPES.register(name, () -> RecipeType.simple(id));
    }

    public static void register(IEventBus modEventBus) {
        Registers.SERIALIZERS.register(modEventBus);
        Registers.TYPES.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializer.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    @SuppressWarnings("unchecked")
    public <C extends Container, T extends Recipe<C>> Optional<T> find(C input, Level level) {
        return level.getRecipeManager().getRecipeFor((RecipeType<T>) getType(), input, level);
    }

    private static final class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
                DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreatePatina.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPES =
                DeferredRegister.create(Registries.RECIPE_TYPE, CreatePatina.MODID);
    }
}

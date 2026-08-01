package io.github.mechtasnezhevna.createpatina.registry;

import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public final class PatinaFanProcessingTypes {

    private static final DeferredRegister<FanProcessingType> TYPES =
            DeferredRegister.create(CreateRegistries.FAN_PROCESSING_TYPE, CreatePatina.MODID);

    public static final DeferredHolder<FanProcessingType, FanProcessingType> HONEYING =
            TYPES.register("honeying", HoneyingType::new);

    private PatinaFanProcessingTypes() {
    }

    public static void register(IEventBus modEventBus) {
        TYPES.register(modEventBus);
    }

    private static final class HoneyingType implements FanProcessingType {

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            return level.getFluidState(pos).is(Tags.Fluids.HONEY);
        }

        @Override
        public int getPriority() {
            return 500;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            return PatinaRecipeTypes.HONEYING.find(new SingleRecipeInput(stack), level).isPresent();
        }

        @Override
        @Nullable
        public List<ItemStack> process(ItemStack stack, Level level) {
            return PatinaRecipeTypes.HONEYING.find(new SingleRecipeInput(stack), level)
                    .map(RecipeHolder::value)
                    .map(recipe -> RecipeApplier.applyRecipeOn(level, stack, recipe, true))
                    .orElse(null);
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) != 0) {
                return;
            }

            Vector3f color = new Color(0xF2A900).asVectorF();
            level.addParticle(
                    new DustParticleOptions(color, 1),
                    pos.x + (level.random.nextFloat() - .5f) * .5f,
                    pos.y + .5f,
                    pos.z + (level.random.nextFloat() - .5f) * .5f,
                    0, 1 / 16f, 0
            );
            level.addParticle(ParticleTypes.FALLING_HONEY, pos.x, pos.y + .5f, pos.z, 0, 0, 0);
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0xD98E04, 0xFFD45C, random.nextFloat()));
            particleAccess.setAlpha(1f);
            if (random.nextFloat() < 1 / 32f) {
                particleAccess.spawnExtraParticle(ParticleTypes.FALLING_HONEY, .125f);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
        }
    }
}

package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.mechtasnezhevna.createpatina.PatinaConfig;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaFluidEndpoint;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import io.github.mechtasnezhevna.createpatina.util.TankWeatherGuard;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(FluidTankBlock.class)
public class FluidTankOxidizationMixin extends Block implements PatinaFluidEndpoint {
    public FluidTankOxidizationMixin(Properties properties) {
        super(properties);
    }

    private Boolean isRegularTank() {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(this);
        return "create".equals(id.getNamespace()) && "fluid_tank".equals(id.getPath());
    }

    private Boolean isPatinaTank() {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(this);
        return "createpatina".equals(id.getNamespace());
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) || canAdvanceWeathering();
    }

    @Override
    public boolean allowsNaturalWeathering() {
        return !getType().isWaxed() && (isRegularTank() || isPatinaTank());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!PatinaConfig.CONFIG.IS_RANDOM_TICK_WEATHERING_ENABLED.get()) {
            return;
        }
        if (!PatinaConfig.CONFIG.OXIDIZE_WHOLE_FLUID_TANK.get()) {
            /*
             * Mirrors PatinaBlock#changeOverTime exactly so the default behaviour is unchanged
             * while the config is disabled.
             */
            float weatheringSpeed = 0.05688889F;
            if (random.nextFloat() < weatheringSpeed) {
                this.getNextState(state, level, pos, random)
                        .ifPresent(next -> OxidizeUtil.replaceWithState(state, next, level, pos));
            }
            return;
        }
        weatherWholeTank(state, level, pos, random);
    }

    private void weatherWholeTank(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canAdvanceWeathering()) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FluidTankBlockEntity tankBE)) {
            return;
        }
        FluidTankBlockEntity controller = tankBE.getControllerBE();
        if (controller == null) {
            return;
        }

        /*
         * Same probability path as PatinaBlock#changeOverTime for the ticked block: the base
         * weathering speed roll, then the neighbour-scan roll from ChangeOverTimeBlock#getNextState.
         */
        float weatheringSpeed = 0.05688889F;
        if (random.nextFloat() >= weatheringSpeed) {
            return;
        }
        if (this.getNextState(state, level, pos, random).isEmpty()) {
            return;
        }

        BlockPos controllerPos = controller.getBlockPos();
        if (!TankWeatherGuard.tryAcquire(level, controllerPos)) {
            return;
        }

        /*
         * Every part of the multiblock must advance to the same next weathering stage, otherwise
         * the mixed states would split the structure (see ConnectivityHandlerMixin). The swap is
         * structure-preserving: the multiblock never detaches, so no async re-formation or extra
         * block updates are needed afterwards.
         */
        OxidizeUtil.weatherWholeFluidTank(controller, level, partState -> {
            if (!(partState.getBlock() instanceof PatinaBlock patina) || !patina.canAdvanceWeathering()) {
                return Optional.empty();
            }
            return WeatheringCopper.getNext(partState.getBlock())
                    .map(nextBlock -> nextBlock.defaultBlockState());
        });
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.fromBlock(this);
    }

    @ModifyReturnValue(method = "getBlockEntityType", at = @At("RETURN"))
    private BlockEntityType<? extends FluidTankBlockEntity> patina$getBlockEntityType(BlockEntityType<? extends FluidTankBlockEntity> original) {
        if (original == AllBlockEntityTypes.FLUID_TANK.get()) {
            if(isRegularTank()) {
                return AllBlockEntityTypes.FLUID_TANK.get();
            }
            return BlockEntityRegistry.WEATHERING_FLUID_TANK.get();
        }
        return original;
    }
}

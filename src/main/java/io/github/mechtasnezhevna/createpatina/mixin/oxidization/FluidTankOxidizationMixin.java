package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaFluidEndpoint;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidTankBlock.class, remap = false)
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
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
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

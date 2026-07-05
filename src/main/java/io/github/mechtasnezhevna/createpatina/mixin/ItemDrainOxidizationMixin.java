package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(ItemDrainBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ItemDrainOxidizationMixin extends Block implements PatinaBlock {
    public ItemDrainOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getBlockEntityType", at = @At("RETURN"), cancellable = true)
    private void getBlockEntityType(CallbackInfoReturnable<BlockEntityType<?>> cir) {
        cir.setReturnValue(BlockEntityRegistry.WEATHERING_ITEM_DRAIN.get());
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) ||
                (!getType().isWaxed() && getType() != WeatheringType.OXIDIZED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        reconnect(level, pos);
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.fromBlock(this);
    }

    @Unique
    private static void reconnect(ServerLevel level, BlockPos pos) {
        for (Direction d : Direction.values()) {
            BlockPos neighborPos = pos.relative(d);
            BlockState neighborState = level.getBlockState(neighborPos);
            FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, neighborPos);
            if (pipe != null) {
                if (pipe.interfaces != null) {
                    // Force remove the old connections
                    pipe.interfaces.remove(d.getOpposite());
                }
                FluidPropagator.propagateChangedPipe(level, neighborPos, neighborState);
                FluidPropagator.resetAffectedFluidNetworks(level, neighborPos, d.getOpposite());
            }

            BlockEntity neighborBE = level.getBlockEntity(neighborPos);
            if (neighborBE instanceof PumpBlockEntity pumpBE) {
                pumpBE.updatePressureChange();
            }
        }
    }
}

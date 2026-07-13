package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlidingDoorBlock.class)
public abstract class CopperDoorOxidizationMixin extends Block implements PatinaBlock {
    public CopperDoorOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperDoor;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperDoor == null) {
            patina$isCopperDoor = state.getBlock().getDescriptionId()
                    .contains("copper_door");
        }
        return super.isRandomlyTicking(state) ||
                (patina$isCopperDoor && this.isWeatheringEnabled()
                        && getType() != WeatheringType.OXIDIZED);
    }

    @Override
    public boolean isWeatheringEnabled() {
        if (patina$isCopperDoor == null) {
            return false;
        }
        return patina$isCopperDoor && !getType().isWaxed();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (state.getValue(SlidingDoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            this.changeOverTime(state, level, pos, random);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean isOldDoor = state.getBlock().getDescriptionId().equals("block.create.copper_door") || state.getBlock() instanceof PatinaBlock;
        boolean isNewDoor = newState.getBlock().getDescriptionId().equals("block.create.copper_door") || newState.getBlock() instanceof PatinaBlock;
        if (isOldDoor && isNewDoor) {
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                this.actionWhenReplaced(state, newState, serverLevel, pos);
            }
            if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
                level.removeBlockEntity(pos);
            }
            return;
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        DoubleBlockHalf half = oldState.getValue(SlidingDoorBlock.HALF);
        BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);
        if (otherState.is(oldState.getBlock())) {
            Block nextBlock = newState.getBlock();
            BlockState nextOtherState = nextBlock.defaultBlockState();
            for (Property<?> property : otherState.getProperties()) {
                if (nextOtherState.hasProperty(property)) {
                    nextOtherState = createpatina$copyProperty(otherState, nextOtherState, (Property) property);
                }
            }
            nextOtherState = nextOtherState.setValue(SlidingDoorBlock.HALF, half == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
            level.setBlock(otherPos, nextOtherState, 3);
        }
    }

    @Unique
    private <T extends Comparable<T>> BlockState createpatina$copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }

    @Override
    public WeatheringType getType() {
        if (patina$type == null) {
            patina$type = WeatheringType.fromBlock(this);
        }
        return patina$type;
    }

    @ModifyReturnValue(method = "getBlockEntityType", at = @At("RETURN"))
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType(BlockEntityType<?> original) {
        if(patina$type == WeatheringType.UNAFFECTED){
            return AllBlockEntityTypes.SLIDING_DOOR.get();
        }
        return BlockEntityRegistry.WEATHERING_COPPER_DOOR.get();
    }
}

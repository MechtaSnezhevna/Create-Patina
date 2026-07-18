package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.ConnectFuncs;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.data.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WeatheringGlassFluidPipeBlock extends GlassFluidPipeBlock implements PatinaBlock {

    private final WeatheringType type;
    private final BlockEntry<? extends FluidPipeBlock> originalEntry;

    public WeatheringGlassFluidPipeBlock(WeatheringType type, Properties properties, BlockEntry<? extends FluidPipeBlock> originalEntry) {
        super(properties);
        this.type = type;
        this.originalEntry = originalEntry;
    }
    @Override
    public WeatheringType getType() {
        return this.type;
    }
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) ||
                (isWeatheringEnabled() && type != WeatheringType.OXIDIZED);
    }
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }
    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        ConnectFuncs.reconnect(level, pos);
    }
    @Override
    public BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHERING_GLASS_FLUID_PIPE.get();
    }
    @Override
    public BlockState toRegularPipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;

        return originalEntry.get()
                .updateBlockState(originalEntry.getDefaultState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }
    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(originalEntry.getDefaultState(), be);
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos,
                                       Player player) {
        return originalEntry.asStack();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean isValidCasing = AllBlocks.COPPER_CASING.isIn(stack);
        WeatheringType CasingType = WeatheringType.UNAFFECTED;
        for(WeatheringType t : WeatheringType.values()) {
            if (isValidCasing)
                break;
            if (t == WeatheringType.UNAFFECTED)
                continue;
            if (BlockRegistry.COPPER_CASING_SET.getEntry(t).isIn(stack)) {
                isValidCasing = true;
                CasingType = t;
            }
        }
        if (!isValidCasing)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        BlockState newState;
        if (type == WeatheringType.UNAFFECTED && CasingType == WeatheringType.UNAFFECTED)
            newState = AllBlocks.ENCASED_FLUID_PIPE.getDefaultState();
        else newState = BlockRegistry.ENCASED_WHAT_FLUID_PIPES(type).getEntry(CasingType).getDefaultState();
        for (Direction d : Iterate.directionsInAxis(getAxis(state)))
            newState = newState.setValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(d), true);
        FluidTransportBehaviour.cacheFlows(level, pos);
        level.setBlockAndUpdate(pos, newState);
        FluidTransportBehaviour.loadFlows(level, pos);
        return ItemInteractionResult.SUCCESS;
    }
}
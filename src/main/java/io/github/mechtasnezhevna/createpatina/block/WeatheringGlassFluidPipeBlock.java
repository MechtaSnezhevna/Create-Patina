package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.registry.BlockRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WeatheringGlassFluidPipeBlock extends GlassFluidPipeBlock implements PatinaBlock {

    private final WeatheringType type;

    public WeatheringGlassFluidPipeBlock(WeatheringType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public WeatheringType getType() {
        return this.type;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
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
    public BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHERING_GLASS_FLUID_PIPE.get();
    }

    @Override
    public BlockState toRegularPipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
        BlockEntry<WeatheringFluidPipeBlock> originalBlockEntry = getOriginal();
        return originalBlockEntry.get()
                .updateBlockState(originalBlockEntry.getDefaultState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(getOriginal().getDefaultState(), be);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos,
                                       Player player) {
        return getOriginal().asStack();
    }

    private BlockEntry<WeatheringFluidPipeBlock> getOriginal() {
        return switch (this.getAge()) {
            case EXPOSED -> this.getType().isWaxed() ? BlockRegistry.WAXED_EXPOSED_FLUID_PIPE
                    : BlockRegistry.EXPOSED_FLUID_PIPE;
            case WEATHERED -> this.getType().isWaxed() ? BlockRegistry.WAXED_WEATHERED_FLUID_PIPE
                    : BlockRegistry.WEATHERED_FLUID_PIPE;
            case OXIDIZED -> this.getType().isWaxed() ? BlockRegistry.WAXED_OXIDIZED_FLUID_PIPE
                    : BlockRegistry.OXIDIZED_FLUID_PIPE;
            case UNAFFECTED -> BlockRegistry.WAXED_FLUID_PIPE;
        };
    }

    public static void reconnect(ServerLevel level, BlockPos pos) {
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

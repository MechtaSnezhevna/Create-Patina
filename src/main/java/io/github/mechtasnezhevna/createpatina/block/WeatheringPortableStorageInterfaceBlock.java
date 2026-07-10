package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeatheringPortableStorageInterfaceBlock extends WrenchableDirectionalBlock
        implements IBE<PortableStorageInterfaceBlockEntity>, PatinaBlock{

    private final PortableStorageInterfaceBlock portableStorageInterfaceBlock;
    private final WeatheringType type;

    public WeatheringPortableStorageInterfaceBlock(WeatheringType type, Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.portableStorageInterfaceBlock = AllBlocks.PORTABLE_FLUID_INTERFACE.get();
        this.type = type;
    }

    public PortableStorageInterfaceBlock getPortableStorageInterfaceBlock() {
        return portableStorageInterfaceBlock;
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
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public BlockEntityType<? extends PortableStorageInterfaceBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHERING_PORTABLE_FLUID_INTERFACE.get();
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_,
                                boolean p_220069_6_) {
        this.portableStorageInterfaceBlock.neighborChanged(state, world, pos, p_220069_4_, p_220069_5_, p_220069_6_);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        this.portableStorageInterfaceBlock.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection();
        if (context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown())
            direction = direction.getOpposite();
        return defaultBlockState().setValue(FACING, direction.getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.portableStorageInterfaceBlock.getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return this.portableStorageInterfaceBlock.hasAnalogOutputSignal(state);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return this.portableStorageInterfaceBlock.getAnalogOutputSignal(blockState, worldIn, pos);
    }

    @Override
    public Class<PortableStorageInterfaceBlockEntity> getBlockEntityClass() {
        return this.portableStorageInterfaceBlock.getBlockEntityClass();
    }
}

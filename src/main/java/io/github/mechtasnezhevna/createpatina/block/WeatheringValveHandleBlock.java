package io.github.mechtasnezhevna.createpatina.block;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.foundation.utility.BlockHelper;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@EventBusSubscriber
public class WeatheringValveHandleBlock extends HandCrankBlock implements PatinaBlock{

    private final ValveHandleBlock valveHandleBlock;
    private final WeatheringType type;

    public WeatheringValveHandleBlock( WeatheringType type, Properties properties) {
        super(properties);
        this.valveHandleBlock = AllBlocks.COPPER_VALVE_HANDLE.get();
        this.type = type;
    }

    public ValveHandleBlock getValveHandleBlock() {
        return valveHandleBlock;
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
    public BlockEntityType<? extends HandCrankBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.WEATHEING_VALVE_HANDLE.get();
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        //remain empty as no special reconnect action needed
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return valveHandleBlock.getShape(pState, worldIn, pos, context);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        valveHandleBlock.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public int getRotationSpeed() {
        return valveHandleBlock.getRotationSpeed();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onWeatheringCopperValveHandleClick(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (!(state.getBlock() instanceof WeatheringValveHandleBlock wvhb)) return;

        Player player = event.getEntity();

        if (!player.mayBuild()) return;
        if (AllItems.WRENCH.isIn(player.getItemInHand(event.getHand())) && player.isShiftKeyDown()) return;

        if (wvhb.clicked(event.getLevel(), event.getPos(), player)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    public boolean clicked(Level level, BlockPos pos, Player player) {
        onBlockEntityUse(level, pos,
                hcbe -> (hcbe instanceof ValveHandleBlockEntity vhbe) && vhbe.activate(player.isShiftKeyDown())
                        ? InteractionResult.SUCCESS
                        : InteractionResult.PASS);
        return true;
    }
}

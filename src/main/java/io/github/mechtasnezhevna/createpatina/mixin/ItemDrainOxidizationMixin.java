package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringItemDrainBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemDrainBlock.class)
public abstract class ItemDrainOxidizationMixin extends Block implements PatinaBlock {
    public ItemDrainOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        WeatheringItemDrainBlock.reconnect(level, pos);
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}

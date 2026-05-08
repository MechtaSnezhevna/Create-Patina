package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import io.github.mechtasnezhevna.createpatina.util.OxidizeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemDrainBlock.class)
public abstract class ItemDrainOxidizationMixin extends Block implements WeatheringCopper {
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
    public void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        OxidizeUtil.applyChangeWithState(state, level, pos);
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
}

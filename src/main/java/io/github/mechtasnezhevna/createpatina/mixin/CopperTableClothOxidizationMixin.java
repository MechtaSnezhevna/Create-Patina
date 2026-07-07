package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.block.WeatheringFluidPipeBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TableClothBlock.class)
public abstract class CopperTableClothOxidizationMixin extends Block implements PatinaBlock {

    public CopperTableClothOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getBlock().getDescriptionId().equals("block.create.copper_table_cloth");
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public void actionWhenReplaced(BlockState oldState, BlockState newState, ServerLevel level, BlockPos pos) {
        WeatheringFluidPipeBlock.reconnect(level, pos);
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.UNAFFECTED;
    }
}

package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import io.github.mechtasnezhevna.createpatina.block.PatinaFluidEndpoint;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PortableStorageInterfaceBlock.class, remap = false)
public class PortableFluidInterfaceOxidizationMixin extends Block implements PatinaFluidEndpoint {
    public PortableFluidInterfaceOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getBlock().getDescriptionId().equals("block.create.portable_fluid_interface")
                || super.isRandomlyTicking(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.UNAFFECTED;
    }
}

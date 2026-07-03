package io.github.mechtasnezhevna.createpatina.mixin;

import com.simibubi.create.AllBlocks;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(IronBarsBlock.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopperBarsOxidizationMixin extends Block implements PatinaBlock {
    public CopperBarsOxidizationMixin(Properties p_54345_) {
        super(p_54345_);
    }

    @Unique
    private static Block copperBarsCache = null;

    @Unique
    private static Block getCopperBarsSafely() {
        if (copperBarsCache == null) {
            if (AllBlocks.COPPER_BARS.isBound()) {
                copperBarsCache = AllBlocks.COPPER_BARS.get();
            } else {
                ResourceLocation loc = ResourceLocation.fromNamespaceAndPath("create", "copper_bars");
                copperBarsCache = BuiltInRegistries.BLOCK.get(loc);
            }
        }
        return copperBarsCache;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        // Copper bars use a different register way
        // that causes mixin NPE if we use simple AllBlocks.COPPER_BARS.get() here
        Block target = getCopperBarsSafely();
        return target != null && state.getBlock() == target;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringType getType() {
        return WeatheringType.UNAFFECTED;
    }
}

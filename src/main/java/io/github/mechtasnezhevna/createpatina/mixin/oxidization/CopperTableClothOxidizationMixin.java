package io.github.mechtasnezhevna.createpatina.mixin.oxidization;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlock;
import com.simibubi.create.content.logistics.tableCloth.TableClothBlockEntity;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.registry.BlockEntityRegistry;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TableClothBlock.class, remap = false)
public abstract class CopperTableClothOxidizationMixin extends Block implements PatinaBlock {

    public CopperTableClothOxidizationMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private WeatheringType patina$type;
    @Unique
    private Boolean patina$isCopperTableCloth;

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        if (patina$isCopperTableCloth == null) {
            patina$isCopperTableCloth = state.getBlock().getDescriptionId()
                    .contains("copper_table_cloth");;
        }
        return super.isRandomlyTicking(state) || canAdvanceWeathering();
    }

    @Override
    public boolean allowsNaturalWeathering() {
        if (patina$isCopperTableCloth == null) {
            return false;
        }
        return patina$isCopperTableCloth && !getType().isWaxed();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public WeatheringType getType() {
        if (patina$type == null) {
            patina$type = WeatheringType.fromBlock(this);
        }
        return patina$type;
    }

    @ModifyReturnValue(method = "getBlockEntityType", at = @At("RETURN"))
    public BlockEntityType<? extends TableClothBlockEntity> getBlockEntityType(BlockEntityType<?> original) {
        if(getType() == WeatheringType.UNAFFECTED){
            return AllBlockEntityTypes.TABLE_CLOTH.get();
        }
        return BlockEntityRegistry.WEATHERING_TABLE_CLOTH.get();
    }
}

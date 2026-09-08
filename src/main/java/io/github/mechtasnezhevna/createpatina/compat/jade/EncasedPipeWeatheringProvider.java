package io.github.mechtasnezhevna.createpatina.compat.jade;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.PatinaBlock;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.Locale;

public enum EncasedPipeWeatheringProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final String ENCASED_PREFIX = "encased_";
    private static final String PIPE_STATE_KEY = "jade.createpatina.pipe_state";
    private static final String CASING_STATE_KEY = "jade.createpatina.casing_state";
    private static final String WEATHERING_TYPE_KEY = "gui.createpatina.weathering_type.";

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        if (!(block instanceof PatinaBlock patinaBlock)) {
            return;
        }
        WeatheringType pipeType = getPipeType(block);
        if (pipeType == null) {
            return;
        }
        tooltip.add(Component.translatable(PIPE_STATE_KEY, weatheringTypeComponent(pipeType)));
        tooltip.add(Component.translatable(CASING_STATE_KEY, weatheringTypeComponent(patinaBlock.getType())));
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePatina.asResource("encased_pipe_weathering");
    }

    private static WeatheringType getPipeType(Block block) {
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        String rest = path.substring(WeatheringType.getPrefixByName(path).length());
        if (!rest.startsWith(ENCASED_PREFIX)) {
            return null;
        }
        return WeatheringType.fromIdString(rest.substring(ENCASED_PREFIX.length()));
    }

    private static Component weatheringTypeComponent(WeatheringType type) {
        return Component.translatable(WEATHERING_TYPE_KEY + type.name().toLowerCase(Locale.ROOT));
    }
}

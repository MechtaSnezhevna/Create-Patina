package io.github.mechtasnezhevna.createpatina.compat.jade;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.block.WeatheringEncasedPipeBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CreatePatina.MODID)
public class PatinaJadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(EncasedPipeWeatheringProvider.INSTANCE, WeatheringEncasedPipeBlock.class);
    }
}

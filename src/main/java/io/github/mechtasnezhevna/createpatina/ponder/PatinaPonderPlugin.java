package io.github.mechtasnezhevna.createpatina.ponder;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PatinaPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreatePatina.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        AllPatinaPonderScenes.register(helper);
    }
}

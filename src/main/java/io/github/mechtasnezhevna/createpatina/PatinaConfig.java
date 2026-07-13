package io.github.mechtasnezhevna.createpatina;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PatinaConfig {

    public static final PatinaConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.ConfigValue<Boolean> ENABLE_FLUID_INTERFACE_CROSS_MATCHING;

    static {
        Pair<PatinaConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(PatinaConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    private PatinaConfig(ModConfigSpec.Builder builder) {

        ENABLE_FLUID_INTERFACE_CROSS_MATCHING = builder
                .translation("createpatina.config.enable_fluid_interface_cross_matching")
                .comment("If true, fluid interfaces will allow cross-matching of fluids with different weathering states.")
                .define("enableFluidInterfaceCrossMatching", true);
    }

}

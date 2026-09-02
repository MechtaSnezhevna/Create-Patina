package io.github.mechtasnezhevna.createpatina;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PatinaConfig {

    public static final PatinaConfig CONFIG;
    public static final ModConfigSpec SPEC;
    public static final Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    public final ModConfigSpec.ConfigValue<Boolean> ENABLE_FLUID_INTERFACE_CROSS_MATCHING;
    public final ModConfigSpec.ConfigValue<Boolean> OXIDIZE_WHOLE_FLUID_TANK;
    public final ModConfigSpec.ConfigValue<Boolean> WEATHER_WHOLE_FLUID_TANK_WITH_TOOLS;

    static {
        Pair<PatinaConfig, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(PatinaConfig::new);
        Pair<Client, ModConfigSpec> clientPair = new ModConfigSpec.Builder().configure(Client::new);

        CONFIG = serverPair.getLeft();
        SPEC = serverPair.getRight();
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    private PatinaConfig(ModConfigSpec.Builder builder) {

        ENABLE_FLUID_INTERFACE_CROSS_MATCHING = builder
                .translation("createpatina.config.enable_portable_fluid_interface_cross_matching")
                .comment("If true, portable fluid interfaces with different weathering states can be matched to each other.")
                .define("enablePortableFluidInterfaceCrossMatching", true);

        OXIDIZE_WHOLE_FLUID_TANK = builder
                .translation("createpatina.config.oxidize_whole_fluid_tank")
                .comment("If true, natural weathering advances the entire fluid tank multiblock at once, so large tanks no longer split into separate weathered pieces.")
                .define("oxidizeWholeFluidTank", true);

        WEATHER_WHOLE_FLUID_TANK_WITH_TOOLS = builder
                .translation("createpatina.config.weather_whole_fluid_tank_with_tools")
                .comment("If true, waxing, de-waxing or scraping a fluid tank block with a honeycomb, axe or sandpaper (by hand or by a deployer) applies to the entire tank multiblock at once, so large tanks never split apart.")
                .define("weatherWholeFluidTankWithTools", true);
    }

    public static class Client {

        public final ModConfigSpec.ConfigValue<Boolean> COLLAPSE_PATINA_SETS_IN_JEI;

        private Client(ModConfigSpec.Builder builder) {
            COLLAPSE_PATINA_SETS_IN_JEI = builder
                    .translation("createpatina.config.collapse_patina_sets_in_jei")
                    .comment("If true, JEI shows each PatinaSet as its unaffected block with an interactive variant popup.")
                    .worldRestart()
                    .define("collapsePatinaSetsInJei", false);
        }
    }

}

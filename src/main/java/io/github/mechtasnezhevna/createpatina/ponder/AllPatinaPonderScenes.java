package io.github.mechtasnezhevna.createpatina.ponder;

import java.util.List;

import com.simibubi.create.infrastructure.ponder.scenes.DisplayScenes;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import com.simibubi.create.infrastructure.ponder.scenes.MovementActorScenes;
import com.simibubi.create.infrastructure.ponder.scenes.SteamScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.DrainScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.FluidMovementActorScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.FluidTankScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.HosePulleyScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.PipeScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.PumpScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.SpoutScenes;
import com.simibubi.create.infrastructure.ponder.scenes.highLogistics.StockLinkScenes;
import com.simibubi.create.infrastructure.ponder.scenes.highLogistics.TableClothScenes;

import io.github.mechtasnezhevna.createpatina.ponder.Scenes.OxidationScenes;
import io.github.mechtasnezhevna.createpatina.ponder.Scenes.WaxUsingFanScenes;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.minecraft.resources.ResourceLocation;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

public class AllPatinaPonderScenes {
    static final List<String> PREFIXS =
        List.of("exposed_", "weathered_", "oxidized_", "waxed_", "waxed_exposed_", "waxed_weathered_", "waxed_oxidized_");

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        helper.forComponents(ResourceLocation.fromNamespaceAndPath("create", "encased_fan"))
            .addStoryBoard(ResourceLocation.fromNamespaceAndPath("create", "fan/processing"), WaxUsingFanScenes::waxing);

        applyScenesToVariants(helper, "item_drain", "item_drain", DrainScenes::emptying);
        applyScenesToVariants(helper, "mechanical_pump", "mechanical_pump/flow", PumpScenes::flow);
        applyScenesToVariants(helper, "mechanical_pump", "mechanical_pump/speed", PumpScenes::speed);
        applyScenesToVariants(helper, "fluid_pipe", "fluid_pipe/flow", PipeScenes::flow);
        applyScenesToVariants(helper, "fluid_pipe", "fluid_pipe/interaction", PipeScenes::interaction);
        applyScenesToVariants(helper, "fluid_pipe", "fluid_pipe/encasing", PipeScenes::encasing);
        applyScenesToVariants(helper, "copper_casing", "fluid_pipe/encasing", PipeScenes::encasing);
        applyScenesToVariants(helper, "fluid_valve", "fluid_valve", PipeScenes::valve);
        applyScenesToVariants(helper, "smart_fluid_pipe", "smart_pipe", PipeScenes::smart);
        applyScenesToVariants(helper, "fluid_tank", "fluid_tank/storage", FluidTankScenes::storage);
        applyScenesToVariants(helper, "fluid_tank", "fluid_tank/sizes", FluidTankScenes::sizes);
        applyScenesToVariants(helper, "hose_pulley", "hose_pulley/intro", HosePulleyScenes::intro);
        applyScenesToVariants(helper, "hose_pulley", "hose_pulley/level", HosePulleyScenes::level);
        applyScenesToVariants(helper, "hose_pulley", "hose_pulley/infinite", HosePulleyScenes::infinite);
        applyScenesToVariants(helper, "spout", "spout", SpoutScenes::filling);
        applyScenesToVariants(helper, "portable_fluid_interface", "portable_interface/transfer_fluid", FluidMovementActorScenes::transfer);
        applyScenesToVariants(helper, "portable_fluid_interface", "portable_interface/redstone_fluid", MovementActorScenes::psiRedstone);
        applyScenesToVariants(helper, "steam_engine", "steam_engine", SteamScenes::engine);
        applyScenesToVariants(helper, "steam_whistle", "steam_whistle", SteamScenes::whistle);
        applyScenesToVariants(helper, "copper_valve_handle", "valve_handle", KineticsScenes::valveHandle);
        applyScenesToVariants(helper, "stock_link", "high_logistics/stock_link", StockLinkScenes::stockLink);
        applyScenesToVariants(helper, "display_link", "display_link", DisplayScenes::link);
        applyScenesToVariants(helper, "display_link", "display_link_redstone", DisplayScenes::redstone);
        applyScenesToVariants(helper, "copper_table_cloth", "high_logistics/table_cloth", TableClothScenes::tableCloth);

        List.of(
            "item_drain", "mechanical_pump", "fluid_pipe", "copper_casing", "fluid_valve", "smart_fluid_pipe",
            "fluid_tank", "hose_pulley", "spout", "portable_fluid_interface", "steam_engine", "steam_whistle",
            "copper_valve_handle", "stock_link", "display_link", "copper_table_cloth"
        ).forEach(itemName -> registerScenesForSet(helper, itemName, "oxidation", OxidationScenes::oxidation));
    }

    /**
     * Apply existing scenes in Create to all variants of an item, excluding the item itself.
     *
     * Please note that this method should only be used for existing scenes in Create.
     *
     * @see #registerScenesForSet Register a custom scene for a set of items.
     * @param helper The PonderSceneRegistrationHelper to register the scenes with.
     * @param itemName The base name of the item, excluding the namespace which will be inferred as MODID.
     * @param sceneName The name of the scene to register, excluding the namespace which will be inferred as "create".
     * @param storyboard The PonderStoryBoard to associate with the scene.
     */
    static void applyScenesToVariants(PonderSceneRegistrationHelper<ResourceLocation> helper, String itemName, String sceneName, PonderStoryBoard storyboard) {
        for (String prefix : PREFIXS) {
            helper.forComponents(ResourceLocation.fromNamespaceAndPath(MODID, prefix + itemName))
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath("create", sceneName), storyboard);
        }
    }

    /**
     * Register a custom scene for a set of items, including all the oxidized or waxed variants and the base item.
     *
     * Please note that this method should only be used for newly added custom scenes.
     *
     * @see #applyScenesToVariants Apply existing scenes in Create to all variants of an item.
     * @param helper The PonderSceneRegistrationHelper to register the scenes with.
     * @param itemName The base name of the item, excluding the namespace which will be inferred as MODID or "create", depennding on the context.
     * @param sceneName The name of the scene to register, excluding the namespace which will be inferred as MODID.
     * @param storyboard The PonderStoryBoard to associate with the scene.
     */
    static void registerScenesForSet(PonderSceneRegistrationHelper<ResourceLocation> helper, String itemName, String sceneName, PonderStoryBoard storyboard) {
        for (String prefix : PREFIXS) {
            helper.forComponents(ResourceLocation.fromNamespaceAndPath(MODID, prefix + itemName))
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(MODID, sceneName), storyboard);
        }
        helper.forComponents(ResourceLocation.fromNamespaceAndPath("create", itemName))
            .addStoryBoard(ResourceLocation.fromNamespaceAndPath(MODID, sceneName), storyboard);
    }
}
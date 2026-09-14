package io.github.mechtasnezhevna.createpatina.ponder.Scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WaxUsingFanScenes {

    public static void waxing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("waxing", "Waxing Items using Encased Fans");
        scene.configureBasePlate(1, 0, 5);
        scene.world().showSection(util.select().layer(0)
            .substract(util.select().position(0, 0, 4)), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(6, 1, 2, 5, 1, 2)
            .add(util.select().position(1, 1, 2)), Direction.DOWN);
        scene.idle(25);

        BlockPos blockPos = util.grid().at(4, 1, 2);
        BlockState honey = BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("create", "honey"))
            .defaultFluidState()
            .createLegacyBlock();

        scene.world().setBlock(util.grid().at(3, 1, 0), honey, false);
        ElementLink<WorldSectionElement> blockInFront =
            scene.world().showIndependentSection(util.select().position(3, 1, 0), Direction.SOUTH);
        scene.world().moveSection(blockInFront, util.vector().of(1, 0, 2), 0);
        scene.world().setBlock(blockPos, honey, true);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().fromTo(blockPos, blockPos.west(2)), 80)
            .colored(PonderPalette.FAST)
            .text("When passing through honey, the Air Flow would be able to wax items.");
        scene.idle(80);

        ItemStack stack = new ItemStack(Items.COPPER_BLOCK);
        ItemStack waxed = new ItemStack(Items.WAXED_COPPER_BLOCK);

        ElementLink<EntityElement> entityLink = scene.world().createItemEntity(util.vector().centerOf(blockPos.west(2)
            .above(2)), util.vector().of(0, 0.1, 0), stack);
        scene.idle(15);
        scene.world().modifyEntity(entityLink, e -> e.setDeltaMovement(-0.2f, 0, 0));
        Vec3 itemVec = util.vector().blockSurface(util.grid().at(1, 1, 2), Direction.EAST)
            .add(0.1, 0, 0);
        scene.overlay().showControls(itemVec, Pointing.DOWN, 20).withItem(stack);
        scene.idle(80);
        scene.world().modifyEntities(ItemEntity.class, ie -> ie.setItem(waxed));
        scene.idle(40);
        scene.overlay().showControls(itemVec, Pointing.DOWN, 20).withItem(waxed);
        scene.idle(20);
        scene.world().modifyEntities(ItemEntity.class, Entity::discard);

        scene.markAsFinished();
    }
}

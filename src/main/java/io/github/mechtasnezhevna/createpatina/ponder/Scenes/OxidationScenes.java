package io.github.mechtasnezhevna.createpatina.ponder.Scenes;

import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import static io.github.mechtasnezhevna.createpatina.CreatePatina.MODID;

import java.util.Random;

public class OxidationScenes {
    public static void oxidation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        Random random = new Random();

        scene.title("oxidation", "Oxidation of Copper Components");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0)
            .substract(util.select().position(5, 0, 1)), Direction.UP);
        scene.idle(5);
        scene.world().setKineticSpeed(util.select().fromTo(5, 0, 1, 5, 2, 1)
            .add(util.select().position(4, 2, 0)), 128);
        scene.world().showSection(util.select().layers(1, 3)
            .add(util.select().position(5, 0, 1)), Direction.DOWN);
        scene.world().propagatePipeChange(util.grid().at(4, 2, 0));
        scene.idle(25);

        BlockPos drainPos = util.grid().at(3, 1, 0);
        int pipeToOxidizeZ = random.nextInt(2, 4);
        BlockPos pipeToOxidizePos = util.grid().at(4, 3, pipeToOxidizeZ);
        int ladderToOxidizeY = random.nextInt(1, 4);
        BlockPos ladderToOxidizePos = util.grid().at(0, ladderToOxidizeY, 2);

        scene.overlay().showText(40)
            .pointAt(util.vector().centerOf(drainPos))
            .placeNearTarget()
            .colored(PonderPalette.WHITE)
            .text("As time passes...");
        scene.idle(50);

        scene.overlay().showText(90)
            .pointAt(util.vector().centerOf(drainPos))
            .placeNearTarget()
            .attachKeyFrame()
            .colored(PonderPalette.OUTPUT)
            .text("Copper component would get oxidized and change its appearance");
        scene.world().setBlock(drainPos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath(MODID, "exposed_item_drain"))
            .defaultBlockState(), false);
        scene.idle(45);
        scene.world().setBlock(pipeToOxidizePos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath(MODID, "exposed_fluid_pipe"))
            .defaultBlockState()
            .setValue(PipeBlock.EAST, false)
            .setValue(PipeBlock.WEST, false)
            .setValue(PipeBlock.UP, false)
            .setValue(PipeBlock.DOWN, false), false);
        scene.idle(45);
        scene.world().setBlock(ladderToOxidizePos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath(MODID, "exposed_copper_ladder"))
            .defaultBlockState()
            .setValue(LadderBlock.FACING, Direction.NORTH), false);
        scene.idle(30);

        scene.overlay().showText(90)
            .pointAt(util.vector().centerOf(drainPos))
            .placeNearTarget()
            .attachKeyFrame()
            .colored(PonderPalette.WHITE)
            .text("They would still be functional...");

        ItemStack lavaBucket = new ItemStack(Items.LAVA_BUCKET);
		scene.overlay().showControls(util.vector().blockSurface(drainPos, Direction.UP), Pointing.DOWN, 40).rightClick()
			.withItem(lavaBucket);
		scene.idle(7);
		scene.world().modifyBlockEntity(drainPos, ItemDrainBlockEntity.class, be -> {
			be.getBehaviour(SmartFluidTankBehaviour.TYPE)
				.allowInsertion();
			IFluidHandler fh = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
			if (fh != null)
				fh.fill(new FluidStack(Fluids.LAVA, 1000), FluidAction.EXECUTE);
		});
		scene.idle(93);

        ItemStack honeycomb = new ItemStack(Items.HONEYCOMB);
        ItemStack diamondAxe = new ItemStack(Items.DIAMOND_AXE);
        scene.overlay().showControls(util.vector().blockSurface(drainPos, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
            .withItem(honeycomb);
        scene.idle(20);
        scene.overlay().showText(40)
            .pointAt(util.vector().centerOf(drainPos))
            .placeNearTarget()
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("Honeycomb can be used to wax and protect the components");
        scene.world().setBlock(drainPos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath(MODID, "waxed_exposed_item_drain"))
            .defaultBlockState(), false);
        scene.idle(40);
        scene.overlay().showControls(util.vector().blockSurface(ladderToOxidizePos, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
            .withItem(diamondAxe);
        scene.idle(20);
        scene.overlay().showText(40)
            .pointAt(util.vector().centerOf(ladderToOxidizePos))
            .placeNearTarget()
            .attachKeyFrame()
            .colored(PonderPalette.GREEN)
            .text("And axes can remove the oxidation...");
        scene.world().setBlock(ladderToOxidizePos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath("create", "copper_ladder"))
            .defaultBlockState()
            .setValue(LadderBlock.FACING, Direction.NORTH), false);
        scene.idle(40);
        scene.overlay().showControls(util.vector().blockSurface(drainPos, Direction.SOUTH), Pointing.RIGHT, 40).rightClick()
            .withItem(diamondAxe);
        scene.idle(20);
        scene.overlay().showText(40)
            .pointAt(util.vector().centerOf(drainPos))
            .placeNearTarget()
            .colored(PonderPalette.GREEN)
            .text("... as well as the wax");
        scene.world().setBlock(drainPos, BuiltInRegistries.BLOCK
            .get(ResourceLocation.fromNamespaceAndPath(MODID, "exposed_item_drain"))
            .defaultBlockState(), false);
        scene.idle(40);

        scene.markAsFinished();
    }
}

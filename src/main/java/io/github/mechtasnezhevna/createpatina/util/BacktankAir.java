package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.world.item.ItemStack;

/**
 * Create 1.20.1 keeps a backtank's remaining air in the item's {@code "Air"}
 * NBT float tag ({@code BacktankUtil}) instead of the 1.21.1
 * {@code create:backtank_air} data component. Recipes assemble fresh output
 * stacks, so every code path that rolls a new stack has to copy the tag over
 * explicitly.
 */
public final class BacktankAir {

    private static final String AIR_TAG = "Air";

    public static boolean has(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrCreateTag().contains(AIR_TAG);
    }

    public static float get(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(AIR_TAG);
    }

    public static void set(ItemStack stack, float air) {
        stack.getOrCreateTag().putFloat(AIR_TAG, air);
    }

    private BacktankAir() {
    }
}

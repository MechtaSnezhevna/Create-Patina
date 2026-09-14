package io.github.mechtasnezhevna.createpatina.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Prevents a tank multiblock from advancing more than one weathering stage per game tick.
 *
 * <p>Every block of a tank multiblock is randomly ticked independently, so several blocks of
 * the same tank can roll a successful weathering check within a single game tick. When
 * whole-tank weathering is enabled the first success already advances the whole structure;
 * this guard lets the remaining random ticks of that tick see that the tank was already
 * weathered and stop them from advancing it again.</p>
 */
public final class TankWeatherGuard {

    private static final Map<ServerLevel, TickGuard> GUARDS = new WeakHashMap<>();

    private TankWeatherGuard() {
    }

    /**
     * Marks the tank with the given controller position as weathered for the current game tick.
     *
     * @return true if the tank had not been weathered yet in this tick
     */
    public static boolean tryAcquire(ServerLevel level, BlockPos controllerPos) {
        long tick = level.getGameTime();
        synchronized (GUARDS) {
            TickGuard guard = GUARDS.computeIfAbsent(level, l -> new TickGuard());
            if (guard.tick != tick) {
                guard.tick = tick;
                guard.advancedTanks.clear();
            }
            return guard.advancedTanks.add(controllerPos);
        }
    }

    private static final class TickGuard {
        long tick = Long.MIN_VALUE;
        final Set<BlockPos> advancedTanks = new HashSet<>();
    }
}

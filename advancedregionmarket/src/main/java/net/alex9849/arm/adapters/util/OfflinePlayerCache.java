package net.alex9849.arm.adapters.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Each call to {@link Bukkit#getOfflinePlayer(UUID)} results in a call to
 * net.minecraft.world.level.storage.PlayerDataStorage#load() which performs expensive I/O.
 * <br>
 * This class caches the retrieved {@link OfflinePlayer} instances as well as the result of
 * {@link OfflinePlayer#getName()} to avoid repeated disk reads.
 */
public final class OfflinePlayerCache {

    private OfflinePlayerCache() {}

    /** Cache for OfflinePlayer lookup */
    private static final Cache<UUID, OfflinePlayer> playerCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    /** Cache for player names */
    private static final Cache<UUID, String> nameCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    /**
     * Returns the cached {@link OfflinePlayer} for the given UUID,
     * loading and caching it if necessary.
     * <br>
     * Calling {@link OfflinePlayer#getName()} on CraftOfflinePlayer may trigger I/O.
     * Always use {@link #getName(UUID)} or {@link #getName(OfflinePlayer)} from {@link OfflinePlayerCache} instead of
     * {@code getPlayer(uuid).getName()}.
     *
     * @param uuid the UUID of the player
     * @return the cached or newly loaded {@link OfflinePlayer} (never null)
     */
    public static @Nonnull OfflinePlayer getPlayer(UUID uuid) {
        try {
            return playerCache.get(uuid, () -> Bukkit.getOfflinePlayer(uuid));
        } catch (ExecutionException e) {
            // This should never happen
            throw new RuntimeException("Failed to load OfflinePlayer for UUID: " + uuid, e);
        }
    }

    /**
     * Returns the cached name for the given UUID, or loads it if necessary.
     * <br>
     * This method should be used instead of calling {@code getPlayer(uuid).getName()}
     * to avoid unnecessary I/O by CraftOfflinePlayer.
     *
     * @param uuid the UUID of the player
     * @return the cached or loaded player name, or null if unknown
     */
    public static @Nullable String getName(UUID uuid) {
        String cached = nameCache.getIfPresent(uuid);
        if (cached != null) {
            return cached;
        }

        OfflinePlayer player = getPlayer(uuid);
        String name = player.getName();

        // Guava does not allow caching null values
        if (name != null) {
            nameCache.put(uuid, name);
        }

        return name;
    }

    /**
     * Convenience overload for retrieving the cached player name from an {@link OfflinePlayer} instance.
     *
     * @param player the OfflinePlayer
     * @return the cached or loaded name, or null if unknown
     */
    public static @Nullable String getName(OfflinePlayer player) {
        return getName(player.getUniqueId());
    }
}

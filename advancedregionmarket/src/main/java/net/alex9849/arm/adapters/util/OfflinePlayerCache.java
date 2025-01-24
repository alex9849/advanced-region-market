package net.alex9849.arm.adapters.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.time.Duration;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerCache {

    private static final LoadingCache<UUID, OfflinePlayer> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30))
            .build(Bukkit::getOfflinePlayer);

    public static OfflinePlayer get(UUID uuid) {
        return cache.get(uuid);
    }
}

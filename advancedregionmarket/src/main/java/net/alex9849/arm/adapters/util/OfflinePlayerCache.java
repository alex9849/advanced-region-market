package net.alex9849.arm.adapters.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

/**
 * Each call to {@link Bukkit#getOfflinePlayer(UUID)} results in a call to
 * net.minecraft.world.level.storage.PlayerDataStorage#load() which contains resource consuming IO read operations.
 * So we want to cache the retrieved offline players as well as the result of {@link OfflinePlayer#getName()}.
 */
public class OfflinePlayerCache {

    private static final LoadingCache<UUID, OfflinePlayer> playerCache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30))
            .build(uuid -> new OfflinePlayerProxy(Bukkit.getOfflinePlayer(uuid)));
    /**
     * Each call to {@link OfflinePlayer#getName()} of the org.bukkit.craftbukkit.CraftOfflinePlayer implementation
     * results in a call to net.minecraft.world.level.storage.PlayerDataStorage#load().
     * So we also want to cache the player names to reduce IO reads.
     */
    private static final LoadingCache<OfflinePlayer, String> playerNameCache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30))
            .build(OfflinePlayer::getName);

    /**
     * @param uuid the UUID of the player to retrieve
     * @return The cached result of {@link Bukkit#getOfflinePlayer(UUID)} wrapped into a proxy object which allows
     *         caching further methods that perform IO read operations (i.e. {@link OfflinePlayer#getName()}).
     * @see Bukkit#getOfflinePlayer(UUID)
     */
    public static OfflinePlayer get(UUID uuid) {
        return playerCache.get(uuid);
    }

    private static class OfflinePlayerProxy implements OfflinePlayer {
        private final OfflinePlayer delegate;

        private OfflinePlayerProxy(OfflinePlayer delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isOnline() {
            return delegate.isOnline();
        }

        /**
         * This method uses the {@link #playerNameCache} to return cached player names or cache them on demand.
         */
        @Override
        public String getName() {
            return playerNameCache.get(delegate);
        }

        @Override
        public UUID getUniqueId() {
            return delegate.getUniqueId();
        }

        @Override
        public PlayerProfile getPlayerProfile() {
            return delegate.getPlayerProfile();
        }

        @Override
        public boolean isBanned() {
            return delegate.isBanned();
        }

        @Override
        public boolean isWhitelisted() {
            return delegate.isWhitelisted();
        }

        @Override
        public void setWhitelisted(boolean value) {
            delegate.setWhitelisted(value);
        }

        @Override
        public Player getPlayer() {
            return delegate.getPlayer();
        }

        @Override
        public long getFirstPlayed() {
            return delegate.getFirstPlayed();
        }

        @Override
        public long getLastPlayed() {
            return delegate.getLastPlayed();
        }

        @Override
        public boolean hasPlayedBefore() {
            return delegate.hasPlayedBefore();
        }

        @Override
        public Location getBedSpawnLocation() {
            return delegate.getBedSpawnLocation();
        }

        @Override
        public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic);
        }

        @Override
        public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
            delegate.decrementStatistic(statistic);
        }

        @Override
        public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic, amount);
        }

        @Override
        public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
            delegate.decrementStatistic(statistic, amount);
        }

        @Override
        public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
            delegate.setStatistic(statistic, newValue);
        }

        @Override
        public int getStatistic(Statistic statistic) throws IllegalArgumentException {
            return delegate.getStatistic(statistic);
        }

        @Override
        public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic, material);
        }

        @Override
        public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
            delegate.decrementStatistic(statistic, material);
        }

        @Override
        public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
            return delegate.getStatistic(statistic, material);
        }

        @Override
        public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic, material, amount);
        }

        @Override
        public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
            delegate.decrementStatistic(statistic, material, amount);
        }

        @Override
        public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
            delegate.setStatistic(statistic, material, newValue);
        }

        @Override
        public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic, entityType);
        }

        @Override
        public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
            delegate.decrementStatistic(statistic, entityType);
        }

        @Override
        public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
            return delegate.getStatistic(statistic, entityType);
        }

        @Override
        public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
            delegate.incrementStatistic(statistic, entityType, amount);
        }

        @Override
        public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
            delegate.decrementStatistic(statistic, entityType, amount);
        }

        @Override
        public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
            delegate.setStatistic(statistic, entityType, newValue);
        }

        @Override
        public Location getLastDeathLocation() {
            return delegate.getLastDeathLocation();
        }

        @Override
        public Map<String, Object> serialize() {
            return delegate.serialize();
        }

        @Override
        public boolean isOp() {
            return delegate.isOp();
        }

        @Override
        public void setOp(boolean value) {
            delegate.setOp(value);
        }
    }
}

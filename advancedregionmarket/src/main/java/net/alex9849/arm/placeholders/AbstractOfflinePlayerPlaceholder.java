package net.alex9849.arm.placeholders;

import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.OfflinePlayer;

public abstract class AbstractOfflinePlayerPlaceholder {
    protected final AdvancedRegionMarket plugin;

    public AbstractOfflinePlayerPlaceholder(AdvancedRegionMarket plugin) {
        this.plugin = plugin;
    }

    public abstract String getIdentifier();

    public abstract String getReplacement(OfflinePlayer player, String[] arguments);

}

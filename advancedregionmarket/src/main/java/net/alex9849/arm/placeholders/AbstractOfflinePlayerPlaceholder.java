package net.alex9849.arm.placeholders;

import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractOfflinePlayerPlaceholder {
    protected final AdvancedRegionMarket plugin;

    public AbstractOfflinePlayerPlaceholder(AdvancedRegionMarket plugin) {
        this.plugin = plugin;
    }

    public abstract String getIdentifier();

    public abstract String getReplacement(@Nullable OfflinePlayer player, String[] arguments);

}

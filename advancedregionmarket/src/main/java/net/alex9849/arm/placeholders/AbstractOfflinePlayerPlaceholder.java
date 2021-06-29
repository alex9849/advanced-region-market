package net.alex9849.arm.placeholders;

import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.OfflinePlayer;

public abstract class AbstractOfflinePlayerPlaceholder {
    protected final AdvancedRegionMarket plugin;
    private final String regex;

    public AbstractOfflinePlayerPlaceholder(AdvancedRegionMarket plugin, String regex) {
        this.plugin = plugin;
        this.regex = regex;
    }

    public abstract String getReplacement(OfflinePlayer offlinePlayer, String[] arguments);

    public boolean matches(String placeholder) {
        return placeholder.matches(this.regex);
    }

}

package net.alex9849.arm.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.alex9849.arm.AdvancedRegionMarket;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlaceholderManager extends PlaceholderExpansion {
    private final AdvancedRegionMarket plugin;
    private final Set<AbstractOfflinePlayerPlaceholder> placeholders;

    public PlaceholderManager(AdvancedRegionMarket plugin) {
        this.plugin = plugin;
        this.placeholders = new HashSet<>();
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "arm";
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
        String[] identifierParts = identifier.split("_");
        if(identifierParts.length < 1) {
            return "";
        }
        final String subIdentifier = identifierParts[0];
        String[] arguments = Arrays.copyOfRange(identifierParts, 1, identifierParts.length);

        for(AbstractOfflinePlayerPlaceholder placeholder : this.placeholders) {
            if(placeholder.getIdentifier().equals(subIdentifier)) {
                return placeholder.getReplacement(offlinePlayer, arguments);
            }
        }

        return "";
    }
}

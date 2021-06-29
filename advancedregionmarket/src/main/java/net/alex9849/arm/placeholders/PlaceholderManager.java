package net.alex9849.arm.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.placeholders.implementations.*;
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
        this.placeholders.add(new AccessRegionsPlaceholder(plugin));
        this.placeholders.add(new MemberRegionsPlaceholder(plugin));
        this.placeholders.add(new OwnedRegionsPlaceholder(plugin));
        this.placeholders.add(new RegionIdAtLocationPlaceholder(plugin));
        this.placeholders.add(new RegionPlaceholderPlaceholder(plugin));
        this.placeholders.add(new RegionCountPlaceholder(plugin));
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
        identifier = PlaceholderAPI.setBracketPlaceholders(offlinePlayer, identifier);
        String[] identifierParts = identifier.split("_");
        if(identifierParts.length < 1) {
            return "";
        }
        String[] arguments = Arrays.copyOfRange(identifierParts, 1, identifierParts.length);

        for(AbstractOfflinePlayerPlaceholder placeholder : this.placeholders) {
            if(placeholder.matches(identifier)) {
                return placeholder.getReplacement(offlinePlayer, arguments);
            }
        }

        return "";
    }
}

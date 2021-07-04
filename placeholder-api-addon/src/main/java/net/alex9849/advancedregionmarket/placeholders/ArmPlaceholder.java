package net.alex9849.advancedregionmarket.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.advancedregionmarket.placeholders.implementations.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArmPlaceholder extends PlaceholderExpansion {
    private AdvancedRegionMarket plugin;
    private final Set<AbstractOfflinePlayerPlaceholder> placeholders;

    public ArmPlaceholder() {
        this.placeholders = new HashSet<>();
    }

    @Override
    public String getRequiredPlugin(){
        return "AdvancedRegionMarket";
    }

    @Override
    public boolean canRegister(){
        this.plugin = (AdvancedRegionMarket) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if(plugin == null) {
            return false;
        }
        this.placeholders.add(new AccessRegionsPlaceholder(plugin));
        this.placeholders.add(new MemberRegionsPlaceholder(plugin));
        this.placeholders.add(new OwnedRegionsPlaceholder(plugin));
        this.placeholders.add(new RegionIdAtLocationPlaceholder(plugin));
        this.placeholders.add(new RegionPlaceholderPlaceholder(plugin));
        this.placeholders.add(new RegionCountPlaceholder(plugin));
        this.placeholders.add(new LimitPlaceholder(plugin));
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
        return "alex9849";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
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

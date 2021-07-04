package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemberRegionsPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public MemberRegionsPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "memberregions(_[^;\n_ ]+)?");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        UUID uuid;
        if(arguments.length < 1) {
            uuid = offlinePlayer.getUniqueId();
        } else if (offlinePlayer != null){
            uuid = Bukkit.getOfflinePlayer(arguments[0]).getUniqueId();
        } else {
            return "";
        }
        Set<Region> accessRegions = new HashSet<>(plugin.getRegionManager().getRegionsByMember(uuid));
        accessRegions.addAll(plugin.getRegionManager().getRegionsByMember(uuid));
        return accessRegions.stream().map(x -> x.getRegion().getId()).collect(Collectors.joining(", "));
    }

}

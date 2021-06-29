package net.alex9849.arm.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.placeholders.AbstractOfflinePlayerPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.stream.Collectors;

public class AccessRegionsPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public AccessRegionsPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "accessregions(_[^;\n_ ]+)?");
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
        return plugin.getRegionManager().getRegionsByMember(uuid)
                .stream().map(x -> x.getRegion().getId()).collect(Collectors.joining(", "));
    }

}
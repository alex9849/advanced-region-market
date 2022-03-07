package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.stream.Collectors;

public class OwnedRegionsPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public OwnedRegionsPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "ownedregions(_[^;\n_ ]+)?");
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
        return plugin.getRegionManager().getRegionsByOwner(uuid)
                .stream().map(Region::getRegionId).collect(Collectors.joining(", "));
    }
}

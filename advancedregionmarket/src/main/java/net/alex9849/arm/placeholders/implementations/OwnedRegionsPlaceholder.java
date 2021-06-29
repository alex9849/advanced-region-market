package net.alex9849.arm.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.placeholders.AbstractOfflinePlayerPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.stream.Collectors;

public class OwnedRegionsPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public OwnedRegionsPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "ownedregions(_[^;\n_ ]+)?");
    }

    @Override
    public String getReplacement(OfflinePlayer player, String[] arguments) {
        UUID uuid;
        if(arguments.length < 1) {
            uuid = player.getUniqueId();
        } else if (player != null){
            uuid = Bukkit.getOfflinePlayer(arguments[0]).getUniqueId();
        } else {
            return "";
        }
        return plugin.getRegionManager().getRegionsByOwner(uuid)
                .stream().map(x -> x.getRegion().getId()).collect(Collectors.joining(", "));
    }
}

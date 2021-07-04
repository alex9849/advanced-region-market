package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionPlaceholderPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public RegionPlaceholderPlaceholder(AdvancedRegionMarket plugin) {
        //Regex = regionplaceholder_world_regionId_placeholder
        // or regionplaceholder_regionId_placeholder
        // or regionplaceholder_placeholder
        super(plugin, "regionplaceholder(_[^;\n_ ]+)?(_[^;\n_ ]+)?(_[^;\n_ ]+)");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        Region region;
        String regionPlaceholder;
        if(arguments.length == 3) {
            region = plugin.getRegionManager().getRegionByNameAndWorld(arguments[1], arguments[0]);
            regionPlaceholder = arguments[2];
        } else if(arguments.length == 2) {
            region = plugin.getRegionManager().getRegionbyNameAndWorldCommands(arguments[0], null);
            regionPlaceholder = arguments[1];
        } else {
            if(!(offlinePlayer instanceof Player)) {
                return "";
            }
            Player player = (Player) offlinePlayer;
            List<Region> regionsAtLoc = plugin.getRegionManager().getRegionsByLocation(player.getLocation());
            if(regionsAtLoc.size() != 1) {
                return "";
            }
            region = regionsAtLoc.get(0);
            regionPlaceholder = arguments[0];
        }
        if(region == null) {
            return "";
        }
        return ChatColor.stripColor(region.replaceVariables("%" + regionPlaceholder + "%"));
    }

}

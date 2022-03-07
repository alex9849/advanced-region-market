package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regions.Region;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionIdAtLocationPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public RegionIdAtLocationPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "regionid");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        if(!(offlinePlayer instanceof Player)) {
            return "";
        }
        Player player = (Player) offlinePlayer;
        List<Region> regionList = plugin.getRegionManager().getRegionsByLocation(player.getLocation());
        Region region = null;
        for(Region currentRegion : regionList) {
            if(currentRegion.getRegion() == null) {
                continue;
            }
            if(region == null || region.getRegion().getPriority() < currentRegion.getRegion().getPriority()) {
                region = currentRegion;
            }
        }
        if(region == null) {
            return "";
        }
        return region.getRegionId();
    }
}

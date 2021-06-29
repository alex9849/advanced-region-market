package net.alex9849.arm.placeholders.implementations;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regionkind.RegionKindGroup;
import net.alex9849.arm.regions.Region;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.Set;

public class RegionCountPlaceholder extends AbstractOfflinePlayerPlaceholder {


    public RegionCountPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "regioncount_(all|free|sold)(_(regionkind|regionkindgroup)_[^;\n_ ]+)?");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        String regionsToCount = arguments[0];
        Iterable<Region> filteredRegions;
        if(arguments.length == 3) {
            if(arguments[1].equals("regionkind")) {
                RegionKind regionKind = plugin.getRegionKindManager().getRegionKind(arguments[2]);
                if(regionKind == null) {
                    return "";
                }
                filteredRegions = plugin.getRegionManager().getRegionsByRegionKind(regionKind);
            } else {
                RegionKindGroup regionKindGroup = plugin.getRegionKindGroupManager().getRegionKindGroup(arguments[2]);
                Set<Region> regionkindGroupRegions = new HashSet<>();
                for(RegionKind regionKind : regionKindGroup) {
                    regionkindGroupRegions.addAll(plugin.getRegionManager().getRegionsByRegionKind(regionKind));
                }
                filteredRegions = regionkindGroupRegions;
            }
        } else {
            filteredRegions = plugin.getRegionManager();
        }

        int foundRegions = 0;
        for(Region region : filteredRegions) {
            switch (regionsToCount) {
                case "sold":
                    if (region.isSold()) {
                        foundRegions++;
                    }
                    break;
                case "free":
                    if (!region.isSold()) {
                        foundRegions++;
                    }
                    break;
                default:
                    foundRegions++;
            }
        }
        return String.valueOf(foundRegions);
    }
}

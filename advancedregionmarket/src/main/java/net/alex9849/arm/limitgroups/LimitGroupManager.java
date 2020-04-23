package net.alex9849.arm.limitgroups;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LimitGroupManager {
    private HashMap<String, LimitGroup> limitGroups = new HashMap<>();

    public void load(ConfigurationSection section) {
        Set<String> groupNames = section.getKeys(false);
        for(String groupName : groupNames) {
            LimitGroup limitGroup = new LimitGroup(groupName);
            ConfigurationSection groupSection = section.getConfigurationSection(groupName);
            Set<String> groupLimits = groupSection.getKeys(true);
            for(String groupLimit : groupLimits) {
                if(groupLimit.equalsIgnoreCase("total")) {
                    limitGroup.setTotalLimit(groupSection.getInt(groupLimit));
                } else {
                    RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(groupLimit);
                    if(regionKind == null) {
                        continue;
                    }
                    limitGroup.addLimit(regionKind, groupSection.getInt(groupLimit));
                }
            }
            this.limitGroups.put(limitGroup.getName(), limitGroup);
        }
    }

    public boolean isCanBuyAnother(Player player, Region region) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return true;
        }
        int ownedRegions = getOwnedRegions(player) + 1;
        int ownedRegionsWithType = getOwnedRegions(player, region.getRegionKind()) + 1;

        return isInLimit(getLimitTotal(player), ownedRegions)
                && isInLimit(getLimit(player, region.getRegionKind()), ownedRegionsWithType);
    }

    public boolean isInLimit(Player player, Region region) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return true;
        }
        int ownedRegions = getOwnedRegions(player);
        int ownedRegionsWithType = getOwnedRegions(player, region.getRegionKind());

        return isInLimit(getLimitTotal(player), ownedRegions)
                && isInLimit(getLimit(player, region.getRegionKind()), ownedRegionsWithType);
    }

    public boolean isInLimit(int limit, int numberOfRegions) {
        return numberOfRegions <= limit || limit == -1;
    }

    public int getLimit(Player player, RegionKind regionkind) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return -1;
        }

        int maxregionswiththistype = -1;
        for (LimitGroup limitGroup : this.limitGroups.values()) {
            if (player.hasPermission(Permission.ARM_LIMIT + limitGroup.getName())) {
                Integer limit = limitGroup.getLimits().get(regionkind);
                if(limit == null) {
                    continue;
                }
                if(limit == -1) {
                    return -1;
                }
                maxregionswiththistype = Math.max(maxregionswiththistype, limit);
            }
        }
        return maxregionswiththistype;
    }

    public int getLimitTotal(Player player) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return -1;
        }

        int maxtotal = -1;
        for (LimitGroup limitGroup : this.limitGroups.values()) {
            if (player.hasPermission(Permission.ARM_LIMIT + limitGroup.getName())) {
                int limit = limitGroup.getTotalLimit();
                if(limit == -1) {
                    return -1;
                }
                maxtotal = Math.max(maxtotal, limit);
            }
        }
        return maxtotal;
    }

    public static int getOwnedRegions(Player player, RegionKind regionkind) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId());
        int ownedregionswiththistype = 0;
        for (Region region : regions) {
            if (regionkind == region.getRegionKind()) {
                ownedregionswiththistype++;
            }
        }
        return ownedregionswiththistype;
    }

    public static int getOwnedRegions(Player player) {
        return AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId()).size();
    }

    public void printLimitInChat(Player player) {
        player.sendMessage(Messages.LIMIT_INFO_TOP);
        String syntaxtotal = Messages.LIMIT_INFO;
        syntaxtotal = syntaxtotal.replace("%regionkind%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%regionkinddisplay%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player) + "");
        int limitTotal = getLimitTotal(player);
        if (limitTotal == -1) {
            syntaxtotal = syntaxtotal.replace("%limitkind%", Messages.UNLIMITED);
        } else {
            syntaxtotal = syntaxtotal.replace("%limitkind%", limitTotal + "");
        }
        player.sendMessage(syntaxtotal);
        printLimitInChat(player, Messages.LIMIT_INFO_TOTAL, getOwnedRegions(player), limitTotal == -1 ? Messages.UNLIMITED:limitTotal + "");

        if (RegionKind.DEFAULT.isDisplayInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = RegionKind.DEFAULT.replaceVariables(syntaxtotal);
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player, RegionKind.DEFAULT) + "");
            int limit = getLimit(player, RegionKind.DEFAULT);
            if (limit == -1) {
                syntaxtotal = syntaxtotal.replace("%limitkind%", Messages.UNLIMITED);
            } else {
                syntaxtotal = syntaxtotal.replace("%limitkind%", limit + "");
            }
            player.sendMessage(syntaxtotal);
        }

        if (RegionKind.SUBREGION.isDisplayInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = RegionKind.SUBREGION.replaceVariables(syntaxtotal);
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player, RegionKind.SUBREGION) + "");
            int limit = getLimit(player, RegionKind.SUBREGION);
            if (limit == -1) {
                syntaxtotal = syntaxtotal.replace("%limitkind%", Messages.UNLIMITED);
            } else {
                syntaxtotal = syntaxtotal.replace("%limitkind%", limit + "");
            }
            player.sendMessage(syntaxtotal);
        }

        for (RegionKind regionKind : AdvancedRegionMarket.getInstance().getRegionKindManager()) {
            if (RegionKind.hasPermission(player, regionKind) && regionKind.isDisplayInLimits()) {
                syntaxtotal = Messages.LIMIT_INFO;
                syntaxtotal = regionKind.replaceVariables(syntaxtotal);
                syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player, regionKind) + "");
                int limit = getLimit(player, regionKind);
                if (limit == -1) {
                    syntaxtotal = syntaxtotal.replace("%limitkind%", Messages.UNLIMITED);
                } else {
                    syntaxtotal = syntaxtotal.replace("%limitkind%", limit + "");
                }
                player.sendMessage(syntaxtotal);
            }
        }
    }

    private void printLimitInChat(Player player, String regionKindName, int ownedRegions, String limit) {
        String massage = Messages.LIMIT_INFO;
        massage = massage.replace("%regionkind%", regionKindName);
        massage = massage.replace("%regionkinddisplay%", regionKindName);
        massage = massage.replace("%playerownedkind%", ownedRegions + "");
        massage = massage.replace("%limitkind%", limit);
        player.sendMessage(massage);
    }

    public String getRegionBuyOutOfLimitMessage(Player player, RegionKind regionKind) {
        int limittotal = getLimitTotal(player);
        int limitkind = getLimit(player, regionKind);
        String limittotalS = "" + limittotal;
        String limitkindS = "" + limitkind;

        if (limitkind == -1) {
            limitkindS = Messages.UNLIMITED;
        }
        if (limittotal == -1) {
            limittotalS = Messages.UNLIMITED;
        }
        String message = Messages.REGION_BUY_OUT_OF_LIMIT;
        message = message.replace("%playerownedkind%", getOwnedRegions(player, regionKind) + "");
        message = message.replace("%limitkind%", limitkindS);
        message = message.replace("%playerownedtotal%", getOwnedRegions(player) + "");
        message = message.replace("%limittotal%", limittotalS);
        message = regionKind.replaceVariables(message);
        return message;
    }
}

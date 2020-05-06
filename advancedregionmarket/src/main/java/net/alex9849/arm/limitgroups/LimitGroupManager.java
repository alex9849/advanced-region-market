package net.alex9849.arm.limitgroups;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.LimitGroupElement;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regionkind.RegionKindGroup;
import net.alex9849.arm.regions.Region;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LimitGroupManager {
    private HashMap<String, LimitGroup> limitGroups = new HashMap<>();

    public void load(ConfigurationSection section) {
        Set<String> groupNames = section.getKeys(false);
        for(String groupName : groupNames) {
            LimitGroup limitGroup = new LimitGroup(groupName);
            ConfigurationSection groupSection = section.getConfigurationSection(groupName);
            Set<String> groupSectionKeys = groupSection.getKeys(false);
            for(String groupKey : groupSectionKeys) {
                if(groupKey.equalsIgnoreCase("total")) {
                    limitGroup.setTotalLimit(groupSection.getInt(groupKey));

                } else if (groupKey.equalsIgnoreCase("regionkinds")) {
                    ConfigurationSection regionKindLimitSection = groupSection.getConfigurationSection(groupKey);
                    Set<String> regionKinds = regionKindLimitSection.getKeys(false);
                    for(String regionkind : regionKinds) {
                        RegionKind regionKind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(regionkind);
                        if(regionKind != null)
                            limitGroup.addLimit(regionKind, regionKindLimitSection.getInt(regionkind));
                    }

                } else if (groupKey.equalsIgnoreCase("regionkindgroups")) {
                    ConfigurationSection rkGroupLimitSection = groupSection.getConfigurationSection(groupKey);
                    Set<String> rkGroups = rkGroupLimitSection.getKeys(false);
                    for(String rkGroup : rkGroups) {
                        RegionKindGroup rkg = AdvancedRegionMarket.getInstance().getRegionKindGroupManager().getRegionKindGroup(rkGroup);
                        if(rkg != null)
                            limitGroup.addLimit(rkg, rkGroupLimitSection.getInt(rkGroup));
                    }
                }
            }
            this.limitGroups.put(limitGroup.getName(), limitGroup);
        }
    }

    public boolean isCanBuyAnother(Player player, RegionKind regionKind) {
        return isInLimit(player, regionKind, 1);
    }

    public boolean isInLimit(Player player, RegionKind regionKind) {
        return isInLimit(player, regionKind, 0);
    }

    private boolean isInLimit(Player player, RegionKind regionKind, int addOwnedRegionsNumber) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return true;
        }
        boolean totalRegionsOk = isInLimit(getLimitTotal(player), getOwnedRegions(player) + addOwnedRegionsNumber);
        boolean regionsWithRegionKindOk = isInLimit(getLimit(player, regionKind), getOwnedRegions(player, regionKind) + addOwnedRegionsNumber);
        boolean regionsWithRegionGroupOK = AdvancedRegionMarket.getInstance().getRegionKindGroupManager().getRegionKindGroupsForRegionKind(regionKind)
                                                .stream().allMatch(x -> isInLimit(getLimit(player, x), getOwnedRegions(player, x) + addOwnedRegionsNumber));
        return totalRegionsOk && regionsWithRegionKindOk && regionsWithRegionGroupOK;
    }

    public boolean isInLimit(int limit, int numberOfRegions) {
        return numberOfRegions <= limit || limit == -1;
    }

    public int getLimit(Player player, LimitGroupElement limitGroupElement) {
        if (player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return -1;
        }

        int maxregionswiththistype = -1;
        for (LimitGroup limitGroup : this.limitGroups.values()) {
            if (player.hasPermission(Permission.ARM_LIMIT + limitGroup.getName())) {
                Integer limit = limitGroup.getLimit(limitGroupElement);
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

    public static int getOwnedRegions(Player player, LimitGroupElement lge) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId());
        Set<RegionKind> checkRegionKinds = new HashSet<>();
        if(lge instanceof RegionKind) {
            checkRegionKinds.add((RegionKind) lge);
        } else if (lge instanceof RegionKindGroup) {
            RegionKindGroup rkg = (RegionKindGroup) lge;
            rkg.forEach(checkRegionKinds::add);
        } else {
            throw new RuntimeException(lge.getClass() + " is not supported here!");
        }
        return regions.stream().filter(region -> checkRegionKinds.contains(region.getRegionKind())).collect(Collectors.toList()).size();
    }

    public static int getOwnedRegions(Player player) {
        return AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId()).size();
    }

    public void printLimitInChat(Player player) {
        player.sendMessage(Messages.LIMIT_INFO_TOP);
        printLimitInChat(player, Messages.LIMIT_INFO_TOTAL, getOwnedRegions(player), getLimitTotal(player));

        List<LimitGroupElement> limitGroupElements = new ArrayList<>();
        AdvancedRegionMarket.getInstance().getRegionKindGroupManager().forEach(limitGroupElements::add);
        AdvancedRegionMarket.getInstance().getRegionKindManager().forEach(limitGroupElements::add);
        limitGroupElements.add(RegionKind.DEFAULT);
        limitGroupElements.add(RegionKind.SUBREGION);

        for (LimitGroupElement lge : limitGroupElements) {
            if (lge.isDisplayInLimits()) {
                String sendmessage;
                if(lge instanceof RegionKind) {
                    sendmessage = Messages.LIMIT_INFO_REGIONKIND;
                } else if(lge instanceof RegionKindGroup) {
                    sendmessage = Messages.LIMIT_INFO_REGIONKINDGROUP;
                } else {
                    throw new RuntimeException(lge.getClass() + " is not supported here!");
                }
                sendmessage = lge.replaceVariables(sendmessage);
                printLimitInChat(player, sendmessage, getOwnedRegions(player, lge), getLimit(player, lge));
            }
        }
    }

    private void printLimitInChat(Player player, String message, int ownedRegions, int limit) {
        String replaced = message.replace("%playerownedkind%", Integer.toString(ownedRegions));
        replaced = replaced.replace("%limitkind%", limit == -1 ? Messages.UNLIMITED:Integer.toString(limit));
        replaced = replaced.replace("%limitreachedcolor%", (ownedRegions >= limit && limit > -1) ? Messages.LIMIT_REACHED_COLOR_CODE:"");
        player.sendMessage(replaced);
    }
}

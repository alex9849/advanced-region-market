package net.alex9849.arm.limitgroups;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LimitGroup {
    private static List<LimitGroup> groupList = new ArrayList<>();
    private List<RegionKindLimit> regionKindLimits = new ArrayList<>();
    private String name;
    Integer total;

    public LimitGroup(String name) {
        this.name = name;
        FileConfiguration config = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getConfig();
        List<String> regionKindStrings = new ArrayList<String>(config.getConfigurationSection("Limits." + name).getKeys(false));
        for(int i = 0; i < regionKindStrings.size(); i++) {
            int limit = config.getInt("Limits." + name + "." + regionKindStrings.get(i));
            if(limit == -1){
                limit = Integer.MAX_VALUE;
            }
            RegionKind kind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(regionKindStrings.get(i));
            if(regionKindStrings.get(i).equals("total")){
                this.total = limit;
            } else {
                if(kind != null) {
                    regionKindLimits.add(new RegionKindLimit(kind, limit));
                }
            }
        }
        if(this.total == null){
            this.total = Integer.MAX_VALUE;
        }
    }

    public static void Reset(){
        groupList = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public int getTotal(){
        return this.total;
    }

    public List<RegionKindLimit> getRegionKindLimits() {
        return regionKindLimits;
    }

    public static List<LimitGroup> getGroupList(){
        return groupList;
    }

    public static boolean isCanBuyAnother(Player player, Region region){
        if(player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return true;
        }

        int ownedregions = getOwnedRegions(player);
        int ownedregionswiththistype = getOwnedRegions(player, region.getRegionKind());

        if(ownedregions < getLimit(player) && ownedregionswiththistype < getLimit(player, region.getRegionKind())){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInLimit(Player player, Region region) {
        if(player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return true;
        }

        int ownedregions = getOwnedRegions(player);
        int ownedregionswiththistype = getOwnedRegions(player, region.getRegionKind());

        if(ownedregions <= getLimit(player) && ownedregionswiththistype <= getLimit(player, region.getRegionKind())){
            return true;
        } else {
            return false;
        }
    }

    public static int getLimit(Player player, RegionKind regionkind){
        if(player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return Integer.MAX_VALUE;
        }

        int maxregionswiththistype = -1;

        for(int i = 0; i < groupList.size(); i++){
            if(player.hasPermission(Permission.ARM_LIMIT + groupList.get(i).getName())){
                for(int y = 0; y < groupList.get(i).getRegionKindLimits().size(); y++) {
                    if(groupList.get(i).getRegionKindLimits().get(y).getRegionKind() == regionkind){
                        if(maxregionswiththistype < groupList.get(i).getRegionKindLimits().get(y).getLimit()){
                            maxregionswiththistype = groupList.get(i).getRegionKindLimits().get(y).getLimit();
                        }
                    }
                }
            }
        }

        if(maxregionswiththistype == -1){
            maxregionswiththistype = Integer.MAX_VALUE;
        }

        return maxregionswiththistype;
    }

    public static int getLimit(Player player){
        if(player.hasPermission(Permission.ADMIN_LIMIT_BYPASS)) {
            return Integer.MAX_VALUE;
        }

        int maxtotal = -1;

        for(int i = 0; i < groupList.size(); i++) {
            if (player.hasPermission(Permission.ARM_LIMIT + groupList.get(i).getName())) {
                if (maxtotal < groupList.get(i).getTotal()) {
                    maxtotal = groupList.get(i).getTotal();
                }
            }
        }
        if(maxtotal == -1){
            maxtotal = Integer.MAX_VALUE;
        }
        return maxtotal;
    }

    public static int getOwnedRegions(Player player, RegionKind regionkind){
        List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByOwner(player.getUniqueId());
        int ownedregionswiththistype = 0;
        for(int i = 0; i < regions.size(); i++) {
            if(regionkind == regions.get(i).getRegionKind()){
                ownedregionswiththistype++;
            }
        }
        return ownedregionswiththistype;
    }

    public static int getOwnedRegions(Player player){
        return AdvancedRegionMarket.getRegionManager().getRegionsByOwner(player.getUniqueId()).size();
    }

    public static void getLimitChat(Player player) {
        player.sendMessage(Messages.LIMIT_INFO_TOP);
        String syntaxtotal = Messages.LIMIT_INFO;
        syntaxtotal = syntaxtotal.replace("%regionkind%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%regionkinddisplay%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player) + "");
        String limit = LimitGroup.getLimit(player) + "";
        if(LimitGroup.getLimit(player) == Integer.MAX_VALUE){
            limit = Messages.UNLIMITED;
        }
        syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

        player.sendMessage(syntaxtotal);

        if(RegionKind.DEFAULT.isDisplayInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = RegionKind.DEFAULT.getConvertedMessage(syntaxtotal);
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, RegionKind.DEFAULT) + "");
            limit = LimitGroup.getLimit(player, RegionKind.DEFAULT) + "";
            if(LimitGroup.getLimit(player, RegionKind.DEFAULT) == Integer.MAX_VALUE){
                limit = Messages.UNLIMITED;
            }
            syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

            player.sendMessage(syntaxtotal);
        }

        if(RegionKind.SUBREGION.isDisplayInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = RegionKind.SUBREGION.getConvertedMessage(syntaxtotal);
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, RegionKind.SUBREGION) + "");
            limit = LimitGroup.getLimit(player, RegionKind.SUBREGION) + "";
            if(LimitGroup.getLimit(player, RegionKind.SUBREGION) == Integer.MAX_VALUE){
                limit = Messages.UNLIMITED;
            }
            syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

            player.sendMessage(syntaxtotal);
        }

        for(RegionKind regionKind : AdvancedRegionMarket.getRegionKindManager()){
            if(RegionKind.hasPermission(player, regionKind) && regionKind.isDisplayInLimits()){

                syntaxtotal = Messages.LIMIT_INFO;
                syntaxtotal = regionKind.getConvertedMessage(syntaxtotal);
                syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, regionKind) + "");
                limit = LimitGroup.getLimit(player, regionKind) + "";
                if(LimitGroup.getLimit(player, regionKind) == Integer.MAX_VALUE){
                    limit = Messages.UNLIMITED;
                }
                syntaxtotal = syntaxtotal.replace("%limitkind%", limit);
                player.sendMessage(syntaxtotal);
            }
        }
    }

    public static String getRegionBuyOutOfLimitMessage(Player player, RegionKind regionKind) {
        int limittotal = LimitGroup.getLimit(player);
        int limitkind = LimitGroup.getLimit(player, regionKind);
        String limittotalS = "" + limittotal;
        String limitkindS = "" + limitkind;

        if (limitkind == Integer.MAX_VALUE) {
            limitkindS = Messages.UNLIMITED;
        }
        if (limittotal == Integer.MAX_VALUE) {
            limittotalS = Messages.UNLIMITED;
        }
        String message = Messages.REGION_BUY_OUT_OF_LIMIT;
        message = message.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, regionKind) + "");
        message = message.replace("%limitkind%", limitkindS);
        message = message.replace("%playerownedtotal%", LimitGroup.getOwnedRegions(player) + "");
        message = message.replace("%limittotal%", limittotalS);
        message = regionKind.getConvertedMessage(message);
        return message;
    }
}

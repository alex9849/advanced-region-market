package net.liggesmeyer.arm.Group;

import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.Permission;
import net.liggesmeyer.arm.regions.Region;
import net.liggesmeyer.arm.regions.RegionKind;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
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
            RegionKind kind = RegionKind.getRegionKind(regionKindStrings.get(i));
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
        List<Region> regions = Region.getRegionsByOwner(player.getUniqueId());
        int ownedregionswiththistype = 0;
        for(int i = 0; i < regions.size(); i++) {
            if(regionkind == regions.get(i).getRegionKind()){
                ownedregionswiththistype++;
            }
        }
        return ownedregionswiththistype;
    }

    public static int getOwnedRegions(Player player){
        return Region.getRegionsByOwner(player.getUniqueId()).size();
    }

    public static boolean getLimitCommand(CommandSender sender){

        if(!(sender instanceof Player)){
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;

        getLimitChat(player);

        return true;
    }

    public static void getLimitChat(Player player) {
        player.sendMessage(Messages.LIMIT_INFO_TOP);
        String syntaxtotal = Messages.LIMIT_INFO;
        syntaxtotal = syntaxtotal.replace("%regiontype%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player) + "");
        String limit = getLimit(player) + "";
        if(getLimit(player) == Integer.MAX_VALUE){
            limit = Messages.UNLIMITED;
        }
        syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

        player.sendMessage(syntaxtotal);

        if(Main.isDisplayDefaultRegionKindInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = syntaxtotal.replace("%regiontype%", RegionKind.DEFAULT.getName());
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player, RegionKind.DEFAULT) + "");
            limit = getLimit(player, RegionKind.DEFAULT) + "";
            if(getLimit(player) == Integer.MAX_VALUE){
                limit = Messages.UNLIMITED;
            }
            syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

            player.sendMessage(syntaxtotal);
        }

        for(int i = 0; i < RegionKind.getRegionKindList().size(); i++){
            if(player.hasPermission(Permission.ARM_BUYKIND + RegionKind.getRegionKindList().get(i).getName())){
                syntaxtotal = Messages.LIMIT_INFO;
                syntaxtotal = syntaxtotal.replace("%regiontype%", RegionKind.getRegionKindList().get(i).getName());
                syntaxtotal = syntaxtotal.replace("%playerownedkind%", getOwnedRegions(player, RegionKind.getRegionKindList().get(i)) + "");
                limit = getLimit(player, RegionKind.getRegionKindList().get(i)) + "";
                if(getLimit(player, RegionKind.getRegionKindList().get(i)) == Integer.MAX_VALUE){
                    limit = Messages.UNLIMITED;
                }
                syntaxtotal = syntaxtotal.replace("%limitkind%", limit);
                player.sendMessage(syntaxtotal);
            }
        }
    }
}

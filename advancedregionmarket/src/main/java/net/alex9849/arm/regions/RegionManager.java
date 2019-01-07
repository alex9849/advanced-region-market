package net.alex9849.arm.regions;

import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RegionManager {
    private static YamlConfiguration regionsconf;
    private static List<Region> regionList = new ArrayList<>();

    public static void addRegion(Region region) {
        regionList.add(region);
        writeRegionToYamlObject(region);
        saveRegionsConf();
    }

    private static void writeRegionToYamlObject(Region region) {
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId(), null);
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".price", region.getPrice());
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".sold", region.isSold());
        if(region.getRegionKind() == RegionKind.DEFAULT) {
            regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".kind", "default");
        } else {
            regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".kind", region.getRegionKind().getName());
        }
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".autoreset", region.isAutoreset());
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".lastreset", region.getLastreset());
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".isHotel", region.isHotel);
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".doBlockReset", region.isDoBlockReset());

        if(region.getTeleportLocation() != null) {
            String teleportloc = region.getTeleportLocation().getWorld().getName() + ";" + region.getTeleportLocation().getBlockX() + ";" +
                    region.getTeleportLocation().getBlockY() + ";" + region.getTeleportLocation().getBlockZ() + ";" +
                    region.getTeleportLocation().getPitch() + ";" + region.getTeleportLocation().getYaw();
            regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".teleportLoc", teleportloc);
        } else {
            regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".teleportLoc", null);
        }

        List<String> regionsigns = new ArrayList<>();
        for(Sign sign : region.getSellSigns()) {
            Location signloc = sign.getLocation();
            regionsigns.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
        }
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".signs", regionsigns);

        if(region instanceof SellRegion) {
            regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId() + ".regiontype", "sellregion");
        } else if (region instanceof RentRegion) {
            RentRegion rentRegion = (RentRegion) region;
            regionsconf.set("Regions." + rentRegion.getRegionworld() + "." + rentRegion.getRegion().getId() + ".regiontype", "rentregion");
            regionsconf.set("Regions." + rentRegion.getRegionworld() + "." + rentRegion.getRegion().getId() + ".payedTill", rentRegion.getPayedTill());
            regionsconf.set("Regions." + rentRegion.getRegionworld() + "." + rentRegion.getRegion().getId() + ".maxRentTime", rentRegion.getMaxRentTime());
            regionsconf.set("Regions." + rentRegion.getRegionworld() + "." + rentRegion.getRegion().getId() + ".rentExtendPerClick", rentRegion.getRentExtendPerClick());
        } else if (region instanceof ContractRegion) {
            ContractRegion contractRegion = (ContractRegion) region;
            regionsconf.set("Regions." + contractRegion.getRegionworld() + "." + contractRegion.getRegion().getId() + ".regiontype", "contractregion");
            regionsconf.set("Regions." + contractRegion.getRegionworld() + "." + contractRegion.getRegion().getId() + ".payedTill", contractRegion.getPayedTill());
            regionsconf.set("Regions." + contractRegion.getRegionworld() + "." + contractRegion.getRegion().getId() + ".extendTime", contractRegion.getExtendTime());
            regionsconf.set("Regions." + contractRegion.getRegionworld() + "." + contractRegion.getRegion().getId() + ".terminated", contractRegion.isTerminated());
        }
    }

    public static void removeRegion(Region region) {
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId(), null);
        regionList.remove(region);
        saveRegionsConf();
    }

    public static void writeRegionsToConfig() {
        regionsconf = new YamlConfiguration();
        for(Region region : regionList) {
            writeRegionToYamlObject(region);
        }
        saveRegionsConf();
    }

    private static List<Region> getRegionList() {
        return regionList;
    }

    public static void generatedefaultConfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regiondic = new File(pluginfolder + "/regions.yml");
        if(!regiondic.exists()){
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream stream = plugin.getResource("regions.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream output = new FileOutputStream(regiondic);
                output.write(buffer);
                output.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static YamlConfiguration getRegionsConf() {
        return RegionManager.regionsconf;
    }

    public static void setRegionsConf(){
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        RegionManager.regionsconf = YamlConfiguration.loadConfiguration(regionsconfigdic);
    }

    public static void saveRegionsConf() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        try {
            RegionManager.regionsconf.save(regionsconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Reset() {
        regionList = new ArrayList<>();
        regionsconf = null;
    }

    public static Region searchRegionbyNameAndWorld(String name, String world){
        for (int i = 0; i < RegionManager.getRegionList().size(); i++) {
            if(RegionManager.getRegionList().get(i).getRegion().getId().equalsIgnoreCase(name) && RegionManager.getRegionList().get(i).getRegionworld().equals(world)){
                return RegionManager.getRegionList().get(i);
            }
        }
        return null;
    }

    public static void updateRegions(){
        for(int i = 0; i < RegionManager.getRegionList().size(); i++) {
            RegionManager.getRegionList().get(i).updateRegion();
        }
    }

    public static Region getRegion(WGRegion wgRegion, World world) {
        for(Region region : regionList) {
            if((region.getRegion().getId().equals(wgRegion.getId())) && (region.getRegionworld().equalsIgnoreCase(world.getName()))) {
                return region;
            }
        }
        return null;
    }

    public static List<String> completeTabRegions(Player player, String arg, PlayerRegionRelationship playerRegionRelationship) {
        List<String> returnme = new ArrayList<>();

        if(Region.completeTabRegions) {
            for(Region region : RegionManager.getRegionList()) {
                if(region.getRegion().getId().toLowerCase().startsWith(arg)) {
                    if(playerRegionRelationship == PlayerRegionRelationship.OWNER) {
                        if(region.getRegion().hasOwner(player.getUniqueId())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER) {
                        if(region.getRegion().hasMember(player.getUniqueId())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerRegionRelationship == PlayerRegionRelationship.MEMBER_OR_OWNER) {
                        if(region.getRegion().hasMember(player.getUniqueId()) || region.getRegion().hasOwner(player.getUniqueId())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerRegionRelationship == PlayerRegionRelationship.ALL) {
                        returnme.add(region.getRegion().getId());
                    } else if (playerRegionRelationship == PlayerRegionRelationship.AVAILABLE) {
                        if(!region.isSold()) {
                            returnme.add(region.getRegion().getId());
                        }
                    }
                }
            }
        }

        return returnme;
    }

    public static List<Region> getRegionsByMember(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){
            if(RegionManager.getRegionList().get(i).getRegion().hasMember(uuid)) {
                regions.add(RegionManager.getRegionList().get(i));
            }
        }
        return regions;
    }

    public static List<Region> getRegionsByOwner(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){
            if(RegionManager.getRegionList().get(i).getRegion().hasOwner(uuid)){
                regions.add(RegionManager.getRegionList().get(i));
            }
        }
        return regions;
    }

    public static List<Region> getRegionsByOwnerOrMember(UUID uuid) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getRegion().hasOwner(uuid) || region.getRegion().hasOwner(uuid)) {
                regions.add(region);
            }
        }
        return regions;
    }

    public static boolean autoResetRegionsFromOwner(UUID uuid){
        List<Region> regions = RegionManager.getRegionsByOwner(uuid);
        for(int i = 0; i < regions.size(); i++){
            if(regions.get(i).getAutoreset()){
                regions.get(i).unsell();
                if(regions.get(i).isDoBlockReset()) {
                    regions.get(i).resetBlocks();
                }
            }
        }
        return true;
    }

    public static void teleportToFreeRegion(RegionKind type, Player player) throws InputException {
        for (int i = 0; i < RegionManager.getRegionList().size(); i++){

            if ((RegionManager.getRegionList().get(i).isSold() == false) && (RegionManager.getRegionList().get(i).getRegionKind() == type)){
                WGRegion regionTP = RegionManager.getRegionList().get(i).getRegion();
                String message = Messages.REGION_TELEPORT_MESSAGE.replace("%regionid%", regionTP.getId());
                Teleporter.teleport(player, RegionManager.getRegionList().get(i), Messages.PREFIX + message, true);
                return;
            }
        }
        throw new InputException(player, Messages.NO_FREE_REGION_WITH_THIS_KIND);
    }

    public static boolean checkIfSignExists(Sign sign) {
        for(int i = 0; i < RegionManager.getRegionList().size(); i++){
            if(RegionManager.getRegionList().get(i).hasSign(sign)){
                return true;
            }
        }
        return false;
    }

    public static Region getRegion(Sign sign) {
        for(Region region : regionList) {
            if(region.hasSign(sign)) {
                return region;
            }
        }
        return null;
    }

    public static List<Region> getRegionsByLocation(Location location) {
        List<Region> regions = new ArrayList<>();
        for(Region region : regionList) {
            if(region.getRegion().contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                if(region.getRegionworld().equals(location.getWorld().getName())) {
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    public static List<Region> getRegions() {
        return regionList;
    }

}

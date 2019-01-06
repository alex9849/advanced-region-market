package net.alex9849.arm.regions;

import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegionManager {
    private static RegionManager regionManager = new RegionManager();
    private static YamlConfiguration regionsconf;
    private List<Region> regionList = new ArrayList<>();

    public static RegionManager getRegionManager() {
        return RegionManager.regionManager;
    }

    public void addRegion(Region region) {
        this.regionList.add(region);
        writeRegionToYamlObject(region);
        saveRegionsConf();
    }

    private void writeRegionToYamlObject(Region region) {
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

    public void removeRegion(Region region) {
        regionsconf.set("Regions." + region.getRegionworld() + "." + region.getRegion().getId(), null);
        this.regionList.remove(region);
        saveRegionsConf();
    }

    public void writeRegionsToConfig() {
        regionsconf = new YamlConfiguration();
        for(Region region : this.regionList) {
            this.writeRegionToYamlObject(region);
        }
        saveRegionsConf();
    }

    public static void generatedefaultConfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/regions.yml");
        if(!messagesdic.exists()){
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream stream = plugin.getResource("regions.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream output = new FileOutputStream(messagesdic);
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

}

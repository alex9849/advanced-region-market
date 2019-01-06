package net.alex9849.arm.regions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegionManager {
    private static List<RegionManager> regionManagerList = new ArrayList<>();
    private static YamlConfiguration regionsconf;
    private List<Region> regionList = new ArrayList<>();
    private World world;

    public RegionManager(World world) {
        this.world = world;
    }

    public RegionManager getRegionManager(World world) {
        for(RegionManager regionManager : RegionManager.regionManagerList) {
            if(regionManager.getWorld() == world) {
                return regionManager;
            }
        }
        return null;
    }

    private World getWorld() {
        return this.world;
    }

    public void saveRegion(Region region) {

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

package net.aex9849.armImporter;

import net.aex9849.armImporter.importer.AreaShopImporter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ARMImporter extends JavaPlugin {


    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("AreaShop") == null) {
            getLogger().info("AreaShop not installed. AreaShop needs to be loaded in order to import your AreaShop files to ARM");
        }
        if (Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket") == null) {
            getLogger().info("AdvancedRegionMarket not installed. AdvancedRegionMarket needs to be loaded in order to import your AreaShop files to ARM");
        }
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) {
        String allsArgs = "";

        if(args.length > 0) {
            allsArgs = args[0];
        }
        for(int i = 1; i < args.length; i++) {
            allsArgs += " " + args[i];
        }


        if(cmd.getName().equalsIgnoreCase("armimport")) {
            if(!sender.hasPermission("armimporter.import")) {
                sender.sendMessage("You do not have the permission armimporter.import");
                return true;
            }


            if(allsArgs.matches("(?i)regionkinds (true|false)")) {
                sender.sendMessage("Creating backup...");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss.SSS");
                String backupFileName = sdf.format(date) + ".zip";
                AreaShopImporter.createBackup(new File(Bukkit.getPluginManager().getPlugin("ARMImporter").getDataFolder() + "/" + backupFileName));
                AreaShopImporter.importRegionKinds(Boolean.parseBoolean(args[1]));
                sender.sendMessage("Import complete");
                return true;
            }
            if(allsArgs.matches("(?i)regions (true|false) [0-9]+")) {
                sender.sendMessage("Creating backup...");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss.SSS");
                String backupFileName = sdf.format(date) + ".zip";
                AreaShopImporter.createBackup(new File(Bukkit.getPluginManager().getPlugin("ARMImporter").getDataFolder() + "/" + backupFileName));
                AreaShopImporter.importRegions(Boolean.parseBoolean(args[1]), Integer.parseInt(args[2]));
                sender.sendMessage("Imported regions succesfully");
                sender.sendMessage("Starting schematic import...");
                sender.sendMessage("Schematic import started! Check your log for more details!");
                sender.sendMessage(ChatColor.BOLD + "Please uninstall AreaShop now! Do not reload it!");
                sender.sendMessage(ChatColor.BOLD + "If AreaShop gets reloaded it will remove the owners of all regions and make them to members and you have to reimport all regions!");
                sender.sendMessage("Please wait with restarting your server till the import process has been completed! Check your console!");
                return true;
            }
            if(allsArgs.matches("(?i)all (true|false) [0-9]+")) {
                sender.sendMessage("Creating backup...");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss.SSS");
                String backupFileName = sdf.format(date) + ".zip";
                AreaShopImporter.createBackup(new File(Bukkit.getPluginManager().getPlugin("ARMImporter").getDataFolder() + "/" + backupFileName));
                AreaShopImporter.importRegionKinds(Boolean.parseBoolean(args[1]));
                sender.sendMessage("Imported regionkinds succesfully");
                AreaShopImporter.importRegions(Boolean.parseBoolean(args[1]), Integer.parseInt(args[2]));
                sender.sendMessage("Imported regions succesfully");
                sender.sendMessage("Starting schematic import...");
                sender.sendMessage("Schematic import started! Check your log for more details!");
                sender.sendMessage(ChatColor.BOLD + "Please uninstall AreaShop now! Do not reload it!");
                sender.sendMessage(ChatColor.BOLD + "If AreaShop gets reloaded it will remove the owners of all regions and make them to members and you have to reimport all regions!");
                sender.sendMessage("Please wait with restarting your server till the import process has been completed! Check your console!");
                return true;
            }
            if(allsArgs.matches("(?i)help")) {
                sender.sendMessage(ChatColor.GOLD + "/armimport regionkinds (true/false)" + ChatColor.WHITE + "   -   Imports regionkinds. If true arm will overwrite already existing regionkinds with the imported ones");
                sender.sendMessage(ChatColor.GOLD + "/armimport regions (true/false) ticksPerSchematic" + ChatColor.WHITE + "   -   Imports regions. If true regions that already exists will be overwritten with the imported ones. ticksPerSchematic determines how log the plugin should wait between the schematic imports. (Because ARM may has to create new schematics if AreaShop has not created schematics)");
                sender.sendMessage(ChatColor.GOLD + "/armimport all (true/false) ticksPerSchematic" + ChatColor.WHITE + "   -   Imports regionkinds and regions");
                return true;
            }
        }
        return false;
    }
}

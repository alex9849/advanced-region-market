package net.liggesmeyer.arm.regions;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.arm.AutoPrice;
import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.Permission;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public abstract class Region {
    private static List<Region> regionList = new ArrayList<>();

    protected ProtectedRegion region;
    protected String regionworld;
    protected ArrayList<Sign> sellsign;
    protected ArrayList<Location> builtblocks;
    protected double price;
    protected boolean sold;
    protected boolean autoreset;
    protected boolean isHotel;
    protected long lastreset;
    protected static int resetcooldown;
    private static YamlConfiguration regionsconf;
    protected RegionKind regionKind;
    protected static double paybackPercentage;
    protected Location teleportLocation;
    protected boolean isDoBlockReset;

    public Region(ProtectedRegion region, String regionworld, List<Sign> sellsign, double price, Boolean sold, Boolean autoreset,
                  Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, Boolean newreg){
        this.region = region;
        this.sellsign = new ArrayList<Sign>(sellsign);
        this.sold = sold;
        this.price = price;
        this.regionworld = regionworld;
        this.regionKind = regionKind;
        this.autoreset = autoreset;
        this.isDoBlockReset = doBlockReset;
        this.lastreset = lastreset;
        this.builtblocks = new ArrayList<Location>();
        this.isHotel = isHotel;
        this.teleportLocation = teleportLoc;

        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld + "/" + region.getId() + "--builtblocks.schematic");
        if(builtblocksdic.exists()){
            try {
                FileReader filereader = new FileReader(builtblocksdic);
                BufferedReader reader = new BufferedReader(filereader);
                String line;
                while ((line = reader.readLine()) != null){
                    String[] lines = line.split(";", 4);
                    int x = Integer.parseInt(lines[1]);
                    int y = Integer.parseInt(lines[2]);
                    int z = Integer.parseInt(lines[3]);
                    Location loc = new Location(Bukkit.getWorld(lines[0]), x, y, z);
                    builtblocks.add(loc);
                }
                reader.close();
                filereader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.builtblocks = new ArrayList<Location>();
        }



        if(newreg){
            YamlConfiguration config = getRegionsConf();

            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".world", regionworld);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".price", price);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".sold", false);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".rentregion", false);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".autoreset", autoreset);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".lastreset", lastreset);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".isHotel", isHotel);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".doBlockReset", doBlockReset);
            List<String> regionsigns = config.getStringList("Regions." + this.regionworld + "." + this.region.getId() + ".signs");
            Location signloc = this.sellsign.get(0).getLocation();
            regionsigns.add(signloc.getWorld().getName() + ";" + signloc.getX() + ";" + signloc.getY() + ";" + signloc.getZ());
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".signs", regionsigns);
            saveRegionsConf(config);
            this.createSchematic();
        }

    }

    public void setTeleportLocation(Location loc) {
        this.teleportLocation = loc;
        String locString = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ() + ";" + loc.getPitch() + ";" + loc.getYaw();
        YamlConfiguration conf = getRegionsConf();
        conf.set("Regions." + this.regionworld + "." + this.region.getId() + ".teleportLoc", locString);
        saveRegionsConf(conf);
    }

    public void addBuiltBlock(Location loc) {
        this.builtblocks.add(loc);

        try {
            File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
            File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld + "/" + region.getId() + "--builtblocks.schematic");
            if(!builtblocksdic.exists()){
                File builtblocksfolder = new File(pluginfolder + "/schematics/" + this.regionworld);
                builtblocksfolder.mkdirs();
                builtblocksdic.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(builtblocksdic, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            loc.getBlock().getType();
            writer.write(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
            writer.newLine();
            writer.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSign(Location loc){
        Sign newsign = (Sign) loc.getBlock().getState();
        sellsign.add(newsign);
        YamlConfiguration config = getRegionsConf();
        List<String> regionsigns = config.getStringList("Regions." + this.regionworld + "." + region.getId() + ".signs");
        regionsigns.add(loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ());
        config.set("Regions." + this.regionworld + "." + region.getId() + ".signs", regionsigns);
        saveRegionsConf(config);
        this.updateSignText(newsign);
        Bukkit.getServer().getWorld(regionworld).save();

    }

    public boolean removeSign(Location loc){
        return this.removeSign(loc, null);
    }

    public boolean removeSign(Location loc, Player destroyer){
        for(int i = 0; i < this.sellsign.size(); i++){
            if(this.sellsign.get(i).getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())){
                if(this.sellsign.get(i).getLocation().distance(loc) == 0){
                    this.sellsign.remove(i);
                    YamlConfiguration config = getRegionsConf();
                    List<String> regionSigns = config.getStringList("Regions." + this.regionworld + "." + this.region.getId() + ".signs");
                    for (int j = 0; j < regionSigns.size(); j++){
                        if (regionSigns.get(j).equals(this.regionworld + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ())){
                            regionSigns.remove(j);
                            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".signs", regionSigns);
                            saveRegionsConf(config);
                            if(destroyer != null) {
                                String message = Messages.SIGN_REMOVED_FROM_REGION.replace("%remaining%", this.getNumberOfSigns() + "");
                                destroyer.sendMessage(Messages.PREFIX + message);
                            }
                            if(regionSigns.size() == 0){
                                for(int x = 0; x < Region.getRegionList().size(); x++) {
                                    if(Region.getRegionList().get(x) == this){
                                        config.set("Regions." + this.regionworld + "." + this.region.getId(), null);
                                        saveRegionsConf(config);
                                        if(destroyer != null) {
                                            destroyer.sendMessage(Messages.PREFIX + Messages.REGION_REMOVED_FROM_ARM);
                                        }
                                        Region.getRegionList().remove(x);
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    public String getRegionworld() {
        return regionworld;
    }

    public double getPrice(){
        return this.price;
    }

    public boolean hasSign(Sign sign){
        for(int i = 0; i < this.sellsign.size(); i++){
            if(this.sellsign.get(i).getWorld().getName().equalsIgnoreCase(sign.getWorld().getName())){
                if(this.sellsign.get(i).getLocation().distance(sign.getLocation()) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public int getNumberOfSigns(){
        return this.sellsign.size();
    }

    public static double calculatePrice(ProtectedRegion region, String regiontype){

        int maxX = region.getMaximumPoint().getBlockX();
        int minX = region.getMinimumPoint().getBlockX();
        int minY = region.getMinimumPoint().getBlockY();
        int maxZ = region.getMaximumPoint().getBlockZ();
        int minZ = region.getMinimumPoint().getBlockZ();
        double price = 0;
        double priceperblock = 0;

        for(int i = 0; i < AutoPrice.getAutoPrices().size(); i++){
            if(AutoPrice.getAutoPrices().get(i).equals(regiontype)){
                priceperblock = AutoPrice.getAutoPrices().get(i).getPricePerSquareMeter();
            }
        }

        for(int x = minX; x <= maxX; x++){
            for(int z = minZ; z <= maxZ; z++) {
                if(region.contains(x, minY, z)){
                    price = price + priceperblock;
                }
            }
        }
        if(price == 0) {
            return Double.parseDouble(regiontype);
        } else {
            return price;
        }
    }

    public boolean setKind(String kind){
        for(int i = 0; i < RegionKind.getRegionKindList().size(); i++){
            if(kind.equalsIgnoreCase(RegionKind.getRegionKindList().get(i).getName())){
                YamlConfiguration config = getRegionsConf();
                config.set("Regions." + this.regionworld + "." + this.region.getId() + ".kind", RegionKind.getRegionKindList().get(i).getName());
                saveRegionsConf(config);
                this.regionKind = RegionKind.getRegionKindList().get(i);
                return true;
            }
        }
        return false;
    }

    public static List<Region> getRegionList() {
        return regionList;
    }

    public static boolean setRegionKindCommand(CommandSender sender, String region, String kind){
        if(sender instanceof Player){
            if(!sender.hasPermission(Permission.ADMIN_SETREGIONKIND)) {
                sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                return true;
            }
            Player player = (Player) sender;
            for(int i = 0; i < Region.getRegionList().size(); i++){
                if(Region.getRegionList().get(i).getRegion().getId().equalsIgnoreCase(region) && Region.getRegionList().get(i).getRegionworld().equals(player.getWorld().getName())){
                    if(Region.getRegionList().get(i).setKind(kind)){
                        sender.sendMessage(Messages.PREFIX + Messages.REGION_KIND_SET);
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + Messages.REGION_KIND_NOT_EXIST);
                        return true;
                    }
                }
            }
            sender.sendMessage(Messages.PREFIX + Messages.REGION_KIND_REGION_NOT_EXIST);
            return true;
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
        }
        return true;
    }

    public static void Reset(){
        Region.regionList = new ArrayList<>();
    }

    public RegionKind getRegionKind(){
        return this.regionKind;
    }

    public static boolean listRegionKindsCommand(CommandSender sender){
        if(!sender.hasPermission(Permission.ADMIN_LISTREGIONKINDS)) {
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        sender.sendMessage(Messages.REGIONKINDS);
        for (int i = 0; i < RegionKind.getRegionKindList().size(); i++){
           sender.sendMessage("- " + RegionKind.getRegionKindList().get(i).getName());
        }
        return true;

    }

    public boolean isSold() {
        return sold;
    }

    public static void teleportToFreeRegion(String type, Player player){
        for (int i = 0; i < Region.getRegionList().size(); i++){
            if ((Region.getRegionList().get(i).isSold() == false) && Region.getRegionList().get(i).getRegionKind().getName().equalsIgnoreCase(type)){
                ProtectedRegion regionTP = Region.getRegionList().get(i).getRegion();
                Region.getRegionList().get(i).teleportToRegion(player);
                String message = Messages.REGION_TELEPORT_MESSAGE.replace("%regionid%", regionTP.getId());
                player.sendMessage(Messages.PREFIX + message);
                return;
            }
        }
        player.sendMessage(Messages.PREFIX + Messages.NO_FREE_REGION_WITH_THIS_KIND);
    }

    public static boolean teleportToFreeRegionCommand(String type, Player player){
        if(!player.hasPermission(Permission.ARM_BUYKIND + type)){
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION_TO_SEARCH_THIS_KIND);
            return true;
        }
        if (!RegionKind.kindExists(type)){
            player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
            return true;
        }
        Region.teleportToFreeRegion(type, player);
        return true;
    }

    public void createSchematic(){

        int maxX = this.region.getMaximumPoint().getBlockX();
        int minX = this.region.getMinimumPoint().getBlockX();
        int maxY = this.region.getMaximumPoint().getBlockY();
        int minY = this.region.getMinimumPoint().getBlockY();
        int maxZ = this.region.getMaximumPoint().getBlockZ();
        int minZ = this.region.getMinimumPoint().getBlockZ();
        World world = Bukkit.getWorld(this.regionworld);
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File schematicdic = new File(pluginfolder + "/schematics/" + this.regionworld + "/" + region.getId() + ".schematic");
        File schematicfolder = new File(pluginfolder + "/schematics/" + this.regionworld);
        if(schematicdic.exists()){
            schematicdic.delete();
        }
        try {
            schematicfolder.mkdirs();
            schematicdic.createNewFile();
            FileWriter fileWriter = new FileWriter(schematicdic);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for(int x = minX; x <= maxX; x++){
                for (int y = minY; y <= maxY; y++){
                    for (int z = minZ; z <= maxZ; z++){
                        if(this.region.contains(x, y, z)){
                            Location loc = new Location(world, x, y, z);
                            String write = loc.getBlock().getType() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ() + ";" + loc.getPitch() + ";" + loc.getYaw() + ";" + loc.getBlock().getData();
                            if(loc.getBlock().getType() == Material.SIGN || loc.getBlock().getType() == Material.WALL_SIGN){
                                Sign sign = (Sign) loc.getBlock().getState();
                                write += ";";
                                write += sign.getLine(0) + "<.:>" + sign.getLine(1) + "<.:>" + sign.getLine(2) + "<.:>" + sign.getLine(3);

                            } else if(loc.getBlock().getType() == Material.CHEST || loc.getBlock().getType() == Material.TRAPPED_CHEST){

                                Chest chest = (Chest) loc.getBlock().getState();
                                Inventory inv = chest.getBlockInventory();
                                write += ";";
                                for (int i = 0; i < inv.getSize() - 1; i++){
                                    ItemStack stack = inv.getItem(i);
                                    if(stack != null){
                                        write += i + ":" + stack.getType() + ":" + stack.getAmount() + "<.:>";
                                    }
                                }
                                ItemStack stack = inv.getItem(inv.getSize() - 1);
                                if(stack != null) {
                                    write += (inv.getSize() - 1) + ":" + stack.getType() + ":" + stack.getAmount();
                                }
                            }

                            writer.write(write);
                            writer.newLine();
                        }
                    }
                }
            }
            writer.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean resetBlocks(){
        return this.resetBlocks(null);
    }

    public boolean resetBlocks(Player player){
        if(player != null) {
            player.sendMessage(Messages.PREFIX + Messages.LOADING_SCHEMATIC);
        }
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        List<String> blocks = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(pluginfolder + "/schematics/" + this.regionworld + "/" + region.getId() + ".schematic");
            BufferedReader reader = new BufferedReader(filereader);
            String line;
            while ((line = reader.readLine()) != null){
                blocks.add(line);
            }
            reader.close();
            filereader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        final Player finalPlayer = player;
        final List<String> blockdata = blocks;
        blocks = null;
        final String worldstring = this.regionworld;
        int blockclount = blockdata.size();
        int fortask = blockclount / 50000;
        if(player != null) {
            player.sendMessage(Messages.PREFIX + Messages.LOADING_SCHEMATIC_COMPLETE);
            player.sendMessage(Messages.PREFIX + Messages.RESET_REGION_RESETING_BLOCKS);
        }

        for(int i = 0; i <= fortask; i++){
            Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
            final int finalI = i;
            boolean last = false;
            if(i == fortask){
                last = true;
            } else {
                last = false;
            }
            final boolean finalLast = last;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    Region.resetBlockPart(finalI * 50000, (finalI + 1) * 50000, blockdata, worldstring, finalLast, finalPlayer);
                }
            }, 20*(i + 1));
        }

        File builtblocksdic = new File(pluginfolder + "/schematics/" + this.regionworld + "/" + region.getId() + "--builtblocks.schematic");
        if(builtblocksdic.exists()){
            builtblocksdic.delete();
        }
        return true;
    }

    public static void resetBlockPart(int start, int end, List<String> blockdata, String worldstring, Boolean last, Player player) {
        World world = Bukkit.getWorld(worldstring);
        if(end > blockdata.size()) {
            end = (blockdata.size() - 1);
        }
        for(int i = start; i <= end; i++){
            String rawcoordinates = blockdata.get(i);
            String[] coordinates = rawcoordinates.split(";");
            Location loc = new Location(world, Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3]));
            loc.setPitch(Float.parseFloat(coordinates[4]));
            loc.setYaw(Float.parseFloat(coordinates[5]));
            Material mat = Material.getMaterial(coordinates[0]);
            loc.getBlock().setType(mat);
        //    loc.getBlock().setData(Byte.parseByte(coordinates[6]));
            if(mat == Material.SIGN || mat == Material.WALL_SIGN) {
                String[] lines = coordinates[7].split("<.:>", 4);
                Sign sign = (Sign) loc.getBlock().getState();
                for(int m = 0; i< lines.length; m++){
                    if(lines[m] == null) {
                        lines[m] = "";
                    }
                }
                sign.setLine(0, lines[0]);
                sign.setLine(1, lines[1]);
                sign.setLine(2, lines[2]);
                sign.setLine(3, lines[3]);
                sign.update();
            } else if(mat == Material.CHEST || mat == Material.TRAPPED_CHEST) {
                Chest chest = (Chest) loc.getBlock().getState();
                Block block01 = (chest.getLocation().add(-1, 0, 0).getBlock());
                Block block02 = (chest.getLocation().add(0, 0, -1).getBlock());

                if(coordinates.length > 7) {
                    String[] lines = coordinates[7].split("<.:>");

                    if(block01.getType() == Material.CHEST || block01.getType() == Material.TRAPPED_CHEST || block02.getType() == Material.CHEST || block02.getType() == Material.TRAPPED_CHEST){
                        for(int m = 27; m < 54; m++) {
                            chest.getInventory().setItem(m, null);
                        }
                        for(int m = 0; m < lines.length; m++){
                            if(lines[m] != null || !lines[m].equals("::")){
                                String[] itm = lines[m].split(":", 3);
                                ItemStack stack = new ItemStack(Material.getMaterial(itm[1]));
                                stack.setAmount(Integer.parseInt(itm[2]));
                                chest.getInventory().setItem(Integer.parseInt(itm[0]) + 27, stack);
                            }
                        }
                    } else {
                        for(int m = 0; m < 27; m++) {
                            chest.getInventory().setItem(m, null);
                        }
                        for(int m = 0; m < lines.length; m++){
                            if(lines[m] != null || !lines[m].equals("::")){
                                String[] itm = lines[m].split(":", 3);
                                ItemStack stack = new ItemStack(Material.getMaterial(itm[1]));
                                stack.setAmount(Integer.parseInt(itm[2]));
                                chest.getInventory().setItem(Integer.parseInt(itm[0]), stack);
                            }
                        }
                    }
                }
            }
        }
        if(last){
            world.save();
            if(player != null) {
                player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
            }
        } else {
            if(player != null) {
                double percent = 100;
                percent = percent * end;
                percent = percent / ((double)blockdata.size());
                String message = Messages.RESET_IN_PERCENT;
                message = message.replace("%percent%", Math.floor(percent) + "");
                player.sendMessage(Messages.PREFIX + message);
            }
        }
    }

    public static Region searchRegionbyNameAndWorld(String name, String world){
        for (int i = 0; i < Region.getRegionList().size(); i++) {
            if(Region.getRegionList().get(i).getRegion().getId().equalsIgnoreCase(name) && Region.getRegionList().get(i).getRegionworld().equals(world)){
                return Region.getRegionList().get(i);
            }
        }
        return null;
    }

    public static boolean resetRegionBlocksCommand(String region, CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }

        if(!sender.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        Region resregion = Region.searchRegionbyNameAndWorld(region, ((Player) sender).getPlayer().getWorld().getName());
        if(resregion == null) {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        } else {
            resregion.resetBlocks((Player) sender);
            return true;
        }
    }

    public static boolean resetRegionCommand(String region, CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
        }

        if(!sender.hasPermission(Permission.ADMIN_RESETREGION)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        Region resregion = Region.searchRegionbyNameAndWorld(region, ((Player) sender).getPlayer().getWorld().getName());
        if(resregion == null) {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        } else {
            resregion.setAviable();

            resregion.resetBlocks((Player) sender);
            sender.sendMessage(Messages.PREFIX + Messages.REGION_NOW_AVIABLE);
            return true;
        }
    }

    public void regionInfo(CommandSender sender){
        String owners = "";
        String members = "";
        List<UUID> ownerslist = new LinkedList(this.getRegion().getOwners().getUniqueIds());
        List<UUID> memberslist = new LinkedList(this.getRegion().getMembers().getUniqueIds());
        for(int i = 0; i < ownerslist.size() - 1; i++){
            owners = owners + Bukkit.getOfflinePlayer(ownerslist.get(i)).getName() + ", ";
        }
        if(ownerslist.size() != 0){
            owners = owners + Bukkit.getOfflinePlayer(ownerslist.get(ownerslist.size() - 1)).getName();
        }
        for(int i = 0; i < memberslist.size() - 1; i++){
            members = members + Bukkit.getOfflinePlayer(memberslist.get(i)).getName() + ", ";
        }
        if(memberslist.size() != 0){
            members = members + Bukkit.getOfflinePlayer(memberslist.get(memberslist.size() - 1)).getName();
        }



        sender.sendMessage(Messages.REGION_INFO);
        sender.sendMessage(Messages.REGION_INFO_ID + this.getRegion().getId());
        sender.sendMessage(Messages.REGION_INFO_SOLD + this.isSold());
        sender.sendMessage(Messages.REGION_INFO_PRICE + this.price + " " + Messages.CURRENCY);
        sender.sendMessage(Messages.REGION_INFO_TYPE + this.getRegionKind().getName());
        sender.sendMessage(Messages.REGION_INFO_OWNER + owners);
        sender.sendMessage(Messages.REGION_INFO_MEMBERS + members);
        if(sender.hasPermission(Permission.ADMIN_INFO)){
            String autoresetmsg = Messages.REGION_INFO_AUTORESET + this.autoreset;
            if((!Main.getEnableAutoReset()) && this.autoreset){
                autoresetmsg = autoresetmsg + " (but globally disabled)";
            }
            sender.sendMessage(autoresetmsg);
            sender.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel);
            sender.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset);
        }
        if (this instanceof RentRegion){
            sender.sendMessage(Messages.REGION_INFO_REMAINING_TIME + ((RentRegion) this).calcRemainingTime());
            sender.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + ((RentRegion) this).getExtendPerClick());
            sender.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + ((RentRegion) this).getMaxRentTime());
        }
    }

    public static boolean regionInfoCommand(CommandSender sender){
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission(Permission.MEMBER_INFO)) {
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        Location loc = (player).getLocation();

        for(int i = 0; i < Region.getRegionList().size(); i++) {
            if(Region.getRegionList().get(i).getRegion().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) && Region.getRegionList().get(i).getRegionworld().equals(loc.getWorld().getName())) {
                Region.getRegionList().get(i).regionInfo(player);
                return true;
            }
        }
        player.sendMessage(Messages.PREFIX + Messages.HAVE_TO_STAND_ON_REGION_TO_SHOW_INFO);
        return true;
    }

    public static boolean regionInfoCommand(CommandSender sender, String regionname){
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission(Permission.MEMBER_INFO)) {
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        Region region = Region.searchRegionbyNameAndWorld(regionname, (player).getWorld().getName());

        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        region.regionInfo(player);
        return true;
    }

    public void addMember(Player sender, String member) {
        Player playermember = Bukkit.getPlayer(member);
        if(playermember == null) {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_NOT_ONLINE);
            return;
        }

        if(this.getRegion().getOwners().contains(((Player) sender).getUniqueId()) && sender.hasPermission(Permission.MEMBER_ADDMEMBER)) {
            this.getRegion().getMembers().addPlayer(playermember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (sender.hasPermission(Permission.ADMIN_ADDMEMBER)){
            this.getRegion().getMembers().addPlayer(playermember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (!(sender.hasPermission(Permission.MEMBER_ADDMEMBER))){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_DO_NOT_OWN);
        }
    }

    public void removeMember(Player sender, String member) {

        OfflinePlayer removemember = Bukkit.getOfflinePlayer(member);

        if(this.getRegion().getOwners().contains(((Player) sender).getUniqueId()) && sender.hasPermission(Permission.MEMBER_REMOVEMEMBER)) {
            if(!(this.getRegion().getMembers().contains(removemember.getUniqueId()))) {
                sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_NOT_A_MEMBER);
                return;
            }
            this.getRegion().getMembers().removePlayer(removemember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
            return;
        } else if (sender.hasPermission(Permission.ADMIN_REMOVEMEMBER)){
            if(!(this.getRegion().getMembers().contains(removemember.getUniqueId()))) {
                sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_NOT_A_MEMBER);
                return;
            }
            this.getRegion().getMembers().removePlayer(removemember.getUniqueId());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_REMOVED);
            return;
        } else if (!(sender.hasPermission(Permission.MEMBER_REMOVEMEMBER))){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return;
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_REMOVE_MEMBER_DO_NOT_OWN);
            return;
        }

    }

    public static boolean addMemberCommand(CommandSender sender, String member, String regionname) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Region region = Region.searchRegionbyNameAndWorld(regionname, ((Player) sender).getWorld().getName());
        if(region == null){
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        region.addMember((Player) sender, member);
        return true;
    }

    public static boolean removeMemberCommand(CommandSender sender, String member, String regionname){
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Region region = Region.searchRegionbyNameAndWorld(regionname, ((Player) sender).getWorld().getName());
        if(region == null){
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        region.removeMember((Player) sender, member);
        return true;
    }

    public static List<Region> getRegionsByOwner(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < Region.getRegionList().size(); i++){
            if(Region.getRegionList().get(i).getRegion().getOwners().contains(uuid)){
                regions.add(Region.getRegionList().get(i));
            }
        }
        return regions;
    }

    public static boolean autoResetRegionsFromOwner(UUID uuid){
        List<Region> regions = Region.getRegionsByOwner(uuid);
        for(int i = 0; i < regions.size(); i++){
            if(regions.get(i).getAutoreset()){
                regions.get(i).unsell();
                if(regions.get(i).isDoBlockReset()) {
                    regions.get(i).resetBlocks(null);
                }
            }
        }
        return true;
    }

    public static boolean checkIfSignExists(Sign sign) {
        for(int i = 0; i < Region.getRegionList().size(); i++){
            if(Region.getRegionList().get(i).hasSign(sign)){
                return true;
            }
        }
        return false;
    }

    public boolean getAutoreset() {
        return autoreset;
    }

    public void setAutoreset(Boolean state){
        this.autoreset = state;

        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".autoreset", state);
        saveRegionsConf(config);

    }

    public static boolean changeAutoresetCommand(CommandSender sender, String regionname, String state) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }

        if(!sender.hasPermission(Permission.ADMIN_CHANGEAUTORESET)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        Region region = Region.searchRegionbyNameAndWorld(regionname, ((Player) sender).getWorld().getName());
        if(region == null){
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        region.setAutoreset(Boolean.parseBoolean(state));
        String message = "disabled";

        if(Boolean.parseBoolean(state)){
            message = "enabled";
        }

        sender.sendMessage(Messages.PREFIX + "Autoreset " + message + " for " + regionname + "!");
        return true;
    }

    public Material getLogo() {
        return this.getRegionKind().getMaterial();
    }

    public void teleportToRegion(Player player) {
        if(this.teleportLocation == null) {
            World world = Bukkit.getWorld(this.regionworld);
            double coordsX = (this.getRegion().getMinimumPoint().getX() + this.getRegion().getMaximumPoint().getX()) / 2;
            double coordsZ = (this.getRegion().getMinimumPoint().getZ() + this.getRegion().getMaximumPoint().getZ()) / 2;
            for (int j = this.region.getMaximumPoint().getBlockY(); j > 0; j--) {
                Location loc = new Location(world, coordsX, j, coordsZ);
                if (!(loc.getBlock().getType() == Material.AIR) && !(loc.getBlock().getType() == Material.LAVA)) {
                    loc = new Location(world, coordsX, j + 1, coordsZ);
                    player.teleport(loc);
                    String message = Messages.REGION_TELEPORT_MESSAGE.replace("%regionid%", this.region.getId());
                    player.sendMessage(Messages.PREFIX + message);
                    return;
                }
            }
        } else {
            player.teleport(this.teleportLocation);
        }
    }

    public void setNewOwner(OfflinePlayer member){
        LinkedList<UUID> owner = new LinkedList<>(this.getRegion().getOwners().getUniqueIds());
        for (int i = 0; i < owner.size(); i++) {
            this.getRegion().getMembers().addPlayer(owner.get(i));
        }
        DefaultDomain newOwner = new DefaultDomain();
        newOwner.addPlayer(member.getUniqueId());
        this.getRegion().setOwners(newOwner);
        this.getRegion().getMembers().removePlayer(member.getUniqueId());
    }

    public static List<Region> getRegionsByMember(UUID uuid) {
        List<Region> regions = new LinkedList<>();
        for (int i = 0; i < Region.getRegionList().size(); i++){
            if(Region.getRegionList().get(i).getRegion().getMembers().contains(uuid)){
                regions.add(Region.getRegionList().get(i));
            }
        }
        return regions;
    }

    public String getDimensions(){
        int maxX = this.getRegion().getMaximumPoint().getBlockX();
        int maxY = this.getRegion().getMaximumPoint().getBlockY();
        int maxZ = this.getRegion().getMaximumPoint().getBlockZ();
        int minX = this.getRegion().getMinimumPoint().getBlockX();
        int minY = this.getRegion().getMinimumPoint().getBlockY();
        int minZ = this.getRegion().getMinimumPoint().getBlockZ();
        return Math.abs(maxX - minX) + "x" + Math.abs(maxY - minY) + "x" + Math.abs(maxZ - minZ);

    }

    public int timeSinceLastReset(){
        GregorianCalendar calendar = new GregorianCalendar();
        long result = calendar.getTimeInMillis() - this.lastreset;
        int days = (int) (result / (1000 * 60 * 60 * 24));
        return days;
    }

    public void userReset(Player player){
        this.resetBlocks(player);
        GregorianCalendar calendar = new GregorianCalendar();
        this.lastreset = calendar.getTimeInMillis();
        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".lastreset", this.lastreset);
        saveRegionsConf(config);
    }

    public static void setResetcooldown(int cooldown){
        Region.resetcooldown = cooldown;
    }

    public static int getResetCooldown(){
        return Region.resetcooldown;
    }

    public int getRemainingDaysTillReset(){
        LinkedList<UUID> ownerlist = new LinkedList<>(this.getRegion().getOwners().getUniqueIds());
            UUID owner = ownerlist.get(0);
            if(Main.getEnableTakeOver() ||Main.getEnableAutoReset()) {
                try{
                    ResultSet rs = Main.getStmt().executeQuery("SELECT * FROM `" + Main.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + owner.toString() + "'");

                    if(rs.next()){
                        Timestamp lastlogin = rs.getTimestamp("lastlogin");
                        GregorianCalendar calendar = new GregorianCalendar();

                        long time = lastlogin.getTime();
                        long result = calendar.getTimeInMillis() - time;
                        int days = (int)  (Main.getAutoResetAfter() - (result / (1000 * 60 * 60 * 24)));
                        return days;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        return 0;
    }

    public void writeSigns(){
        for (int i = 0; i < this.sellsign.size(); i++) {
            this.updateSignText(this.sellsign.get(i));
        }
    }

    protected abstract void setSold(OfflinePlayer player);
    protected abstract void updateSignText(Sign mysign);
    protected abstract void buy(Player player);
    public abstract void userSell(Player player);
    public abstract double getPaybackMoney();

    public void resetRegion(Player player){

        this.unsell();

        this.resetBlocks(player);
        return;
    }

    public void setAviable(){

        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.getRegion().getId() + ".sold", false);
        config.set("Regions." + this.regionworld + "." + this.getRegion().getId() + ".lastreset", 1);
        saveRegionsConf(config);
        DefaultDomain newMember = new DefaultDomain();
        this.region.setMembers(newMember);
        DefaultDomain newOwner = new DefaultDomain();
        this.region.setOwners(newOwner);
        this.sold = false;
        this.lastreset = 1;

        for(int i = 0; i < this.sellsign.size(); i++){
            this.updateSignText(this.sellsign.get(i));
        }
    }

    public static boolean listRegionsCommand(CommandSender sender, String args){
        if(sender.hasPermission(Permission.ADMIN_LISTREGIONS)){
            LinkedList<String> selectedRegionsOwner = new LinkedList<>();
            LinkedList<String> selectedRegionsMember = new LinkedList<>();
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(args);
            if(oplayer == null){
                sender.sendMessage(Messages.PREFIX + "Player does not exist!");
                return true;
            }
            for(int i = 0; i < Region.getRegionList().size(); i++) {
                if(Region.getRegionList().get(i).getRegion().getOwners().contains(oplayer.getUniqueId())){
                    selectedRegionsOwner.add(Region.getRegionList().get(i).getRegion().getId());
                }
                if(Region.getRegionList().get(i).getRegion().getMembers().contains(oplayer.getUniqueId())){
                    selectedRegionsMember.add(Region.getRegionList().get(i).getRegion().getId());
                }
            }

            String regionstring = "";
            for(int i = 0; i < selectedRegionsOwner.size() - 1; i++) {
                regionstring = regionstring + selectedRegionsOwner.get(i) + ", ";
            }
            if(selectedRegionsOwner.size() != 0){
                regionstring = regionstring + selectedRegionsOwner.get(selectedRegionsOwner.size() - 1);
            }
            sender.sendMessage(ChatColor.GOLD + "Owner: " + regionstring);

            regionstring = "";
            for(int i = 0; i < selectedRegionsMember.size() - 1; i++) {
                regionstring = regionstring + selectedRegionsMember.get(i) + ", ";
            }
            if(selectedRegionsMember.size() != 0){
                regionstring = regionstring + selectedRegionsMember.get(selectedRegionsMember.size() - 1);
            }
            sender.sendMessage(ChatColor.GOLD + "Member: " + regionstring);
            return true;


        } else {
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }
    }

    public static boolean listRegionsCommand(CommandSender sender){
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(sender.hasPermission(Permission.MEMBER_LISTREGIONS)){
                LinkedList<String> selectedRegionsOwner = new LinkedList<>();
                LinkedList<String> selectedRegionsMember = new LinkedList<>();
                for(int i = 0; i < Region.getRegionList().size(); i++) {
                    if(Region.getRegionList().get(i).getRegion().getOwners().contains(player.getUniqueId())){
                        selectedRegionsOwner.add(Region.getRegionList().get(i).getRegion().getId());
                    }
                    if(Region.getRegionList().get(i).getRegion().getMembers().contains(player.getUniqueId())){
                        selectedRegionsMember.add(Region.getRegionList().get(i).getRegion().getId());
                    }
                }

                String regionstring = "";
                for(int i = 0; i < selectedRegionsOwner.size() - 1; i++) {
                    regionstring = regionstring + selectedRegionsOwner.get(i) + ", ";
                }
                if(selectedRegionsOwner.size() != 0){
                    regionstring = regionstring + selectedRegionsOwner.get(selectedRegionsOwner.size() - 1);
                }
                sender.sendMessage(ChatColor.GOLD + "Owner: " + regionstring);

                regionstring = "";
                for(int i = 0; i < selectedRegionsMember.size() - 1; i++) {
                    regionstring = regionstring + selectedRegionsMember.get(i) + ", ";
                }
                if(selectedRegionsMember.size() != 0){
                    regionstring = regionstring + selectedRegionsMember.get(selectedRegionsMember.size() - 1);
                }
                sender.sendMessage(ChatColor.GOLD + "Member: " + regionstring);
                return true;


            } else {
                sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                return true;
            }
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
    }

    public static boolean setRegionOwner(CommandSender sender, String regionString, String ownerString){

        if(!sender.hasPermission(Permission.ADMIN_SETOWNER)) {
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player playersender = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionString, playersender.getWorld().getName());
        if (region == null) {
            sender.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(ownerString);

        if (player == null) {
            playersender.sendMessage(Messages.PREFIX + "Player not found!");
            return true;
        }
        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + region.regionworld + "." + region.region.getId() + ".sold", true);
        saveRegionsConf(config);

        if (region instanceof SellRegion) {
            SellRegion sellregion = (SellRegion) region;
            DefaultDomain newOwner = new DefaultDomain();
            newOwner.addPlayer(player.getUniqueId());
            sellregion.getRegion().setOwners(newOwner);
        }
        if (region instanceof RentRegion) {
            RentRegion rentregion = (RentRegion) region;
            DefaultDomain newOwner = new DefaultDomain();
            newOwner.addPlayer(player.getUniqueId());
            rentregion.getRegion().setOwners(newOwner);
            if(!region.isSold()){
                GregorianCalendar actualtime = new GregorianCalendar();
                rentregion.setPayedTill(actualtime.getTimeInMillis() + rentregion.getRentExtendPerClick());
                config.set("Regions." + rentregion.regionworld + "." + rentregion.region.getId() + ".payedTill", rentregion.getPayedTill());
                saveRegionsConf(config);
            }
        }
        region.sold = true;
        for (int i = 0; i < region.sellsign.size(); i++) {
            region.updateSignText(region.sellsign.get(i));
        }
        sender.sendMessage(Messages.PREFIX + "Owner set!");
        return true;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public boolean allowBlockBreak(Location breakloc) {
        if(this.isHotel) {
            for (int i = 0; i < this.builtblocks.size(); i++) {
                if (this.builtblocks.get(i).distance(breakloc) < 1) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void setHotel(Boolean bool) {
        this.isHotel = bool;
        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.getRegion().getId() + ".isHotel", bool);
        saveRegionsConf(config);
    }

    public static boolean setHotel(CommandSender sender, String regionString, String booleanstring){
        if(!sender.hasPermission(Permission.ADMIN_CHANGE_IS_HOTEL)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionString, player.getWorld().getName());
        if(region == null) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        region.setHotel(Boolean.parseBoolean(booleanstring));
        String state = "disabled";
        if(Boolean.parseBoolean(booleanstring)){
            state = "enabled";
        }
        player.sendMessage(Messages.PREFIX + "isHotel " + state + " for " + region.getRegion().getId());
        return true;
    }

    public static boolean createNewSchematic(CommandSender sender, String regionString) {
        if(!sender.hasPermission(Permission.ADMIN_UPDATESCHEMATIC)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionString, player.getWorld().getName());

        if(region == null) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        player.sendMessage(Messages.PREFIX + "Creating schematic...");
        region.createSchematic();
        player.sendMessage(Messages.PREFIX + Messages.COMPLETE);
        return true;
    }

    public static boolean teleport(CommandSender sender, String regionString) {
        if (!sender.hasPermission(Permission.ADMIN_TP)) {
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionString, player.getWorld().getName());

        if (region == null) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        region.teleportToRegion(player);
        return true;
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
        return Region.regionsconf;
    }

    public static void setRegionsConf(){
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        Region.regionsconf = YamlConfiguration.loadConfiguration(regionsconfigdic);
    }

    public static void saveRegionsConf(YamlConfiguration conf) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/regions.yml");
        try {
            conf.save(regionsconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPaypackPercentage(double percent){
        Region.paybackPercentage = percent;
    }

    public static boolean setTeleportLocation(String regionName, CommandSender sender){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        region.setTeleportLocation(player.getLocation());

        player.sendMessage(Messages.PREFIX + "Warp set!");
        return true;
    }

    public static boolean unsellCommand(String regionName, CommandSender sender){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        region.unsell();

        player.sendMessage(Messages.PREFIX + region.getRegion().getId() + " is now for sale!");
        return true;
    }

    public void unsell(){
        this.setAviable();
        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.getRegion().getId() + ".sold", false);
        saveRegionsConf(config);

        DefaultDomain newOwner = new DefaultDomain();
        DefaultDomain newMember = new DefaultDomain();
        this.getRegion().setOwners(newOwner);
        this.getRegion().setMembers(newMember);
    }

    public static boolean extendCommand(String regionName, CommandSender sender){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        if(!(region instanceof RentRegion)) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_IS_NOT_A_RENTREGION);
            return true;
        }

        ((RentRegion) region).extendRegion(player);

        return true;
    }

    public static boolean deleteCommand(String regionName, CommandSender sender){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        region.unsell();
        for(int i = 0; i < region.sellsign.size(); i++) {
            Location loc = region.sellsign.get(i).getLocation();
            loc.getBlock().setType(Material.AIR);
            region.removeSign(loc, null);
            i--;
        }

        player.sendMessage(Messages.PREFIX + region.getRegion().getId() + " deleted!");
        return true;
    }

    public boolean isDoBlockReset() {
        return isDoBlockReset;
    }

    public static boolean setDoBlockResetCommand(String regionName, String setting, CommandSender sender){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }

        Boolean boolsetting = Boolean.parseBoolean(setting);

        region.setDoBlockReset(boolsetting);

        player.sendMessage(Messages.PREFIX + "Setted doBlockReset for " + region.getRegion().getId() + " to " + setting);
        return true;
    }

    public void setDoBlockReset(Boolean bool) {
        this.isDoBlockReset = bool;
        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.getRegion().getId() + ".doBlockReset", bool);
        saveRegionsConf(config);
    }
}

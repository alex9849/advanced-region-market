package net.liggesmeyer.arm;

import net.liggesmeyer.adapters.*;
import net.liggesmeyer.arm.Preseter.Preset;
import net.liggesmeyer.arm.Preseter.RentPreset;
import net.liggesmeyer.arm.Preseter.SellPreset;
import net.liggesmeyer.arm.regions.Region;
import net.liggesmeyer.arm.regions.RentRegion;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.arm.Group.LimitGroup;
import net.liggesmeyer.arm.gui.Gui;
import net.liggesmeyer.arm.regions.RegionKind;
import net.liggesmeyer.arm.regions.SellRegion;
import net.liggesmeyer.inter.WorldEditInterface;
import net.liggesmeyer.inter.WorldGuardInterface;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Boolean faWeInstalled;
    private static Economy econ;
    private static WorldGuardPlugin worldguard;
    private static WorldGuardInterface worldGuardInterface;
    private static WorldEditPlugin worldedit;
    private static WorldEditInterface worldEditInterface;
    private static boolean enableAutoReset;
    private static boolean enableTakeOver;
    private static Statement stmt;
    private static String sqlPrefix;
    private static int autoResetAfter;
    private static int takeoverAfter;
    private static boolean teleportAfterSellRegionBought;
    private static boolean teleportAfterRentRegionBought;
    private static boolean teleportAfterRentRegionExtend;
    private static boolean displayDefaultRegionKindInGUI;
    private static boolean displayDefaultRegionKindInLimits;

    private static final String SET_REGION_KIND = " (?i)setregionkind [^;\n ]+ [^;\n ]+";
    private static final String LIST_REGION_KIND = " (?i)listregionkinds";
    private static final String FIND_REGION_BY_TYPE = " (?i)findfreeregion [^;\n ]+";
    private static final String RESET_REGION_BLOCKS = " (?i)resetblocks [^;\n ]+";
    private static final String RESET_REGION = " (?i)reset [^;\n ]+";
    private static final String REGION_INFO_WITHOUT_REGIONNAME = " (?i)info";
    private static final String REGION_INFO_WITH_REGIONNAME = " (?i)info [^;\n ]+";
    private static final String ADDMEMBER_WITH_REGIONNAME = " (?i)addmember [^;\n ]+ [^;\n ]+";
    private static final String REMOVEMEMBER_WITH_REGIONNAME = " (?i)removemember [^;\n ]+ [^;\n ]+";
    private static final String CHANGE_AUTORESET = " (?i)autoreset [^;\n ]+ (false|true)";
    private static final String LIST_REGIONS_OTHER = " (?i)listregions [^;\n ]+";
    private static final String LIST_REGIONS = " (?i)listregions";
    private static final String SET_OWNER = " (?i)setowner [^;\n ]+ [^;\n ]+";
    private static final String SET_ALLOW_ONLY_NEW_BLOCKS = " (?i)hotel [^;\n ]+ (false|true)";
    private static final String CREATE_NEW_SCHEMATIC = " (?i)updateschematic [^;\n ]+";
    private static final String TELEPORT = " (?i)tp [^;\n ]+";
    private static final String SET_WARP = " (?i)setwarp [^;\n ]+";
    private static final String UNSELL = " (?i)unsell [^;\n ]+";
    private static final String EXTEND = " (?i)extend [^;\n ]+";
    private static final String DELETE = " (?i)delete [^;\n ]+";
    private static final String SET_DO_BLOCK_RESET = " (?i)doblockreset [^;\n ]+ (false|true)";
    private static final String HELP = " (?i)help";
    private static final String RELOAD = " (?i)reload";
    private static final String SELLPRESET = " (?i)sellpreset [^;\n]+";
    private static final String RENTPRESET = " (?i)rentpreset [^;\n]+";

    public void onEnable(){

        if(!Main.isAllowStartup(this)){
            this.setEnabled(false);
            getLogger().log(Level.WARNING, "Plugin remotely deactivated!");
            return;
        }

        //Enable bStats
        Metrics metrics = new Metrics(this);

        Main.faWeInstalled = setupFaWe();

        //Check if Worldguard is installed
        if (!setupWorldGuard()) {
            getLogger().log(Level.INFO, "Please install Worldguard!");
        }
        //Check if Worldedit is installed
        if (!setupWorldEdit()) {
            getLogger().log(Level.INFO, "Please install Worldedit!");
        }
        //Check if Vault and an Economy Plugin is installed
        if (!setupEconomy()) {
            getLogger().log(Level.INFO, "Please install Vault and a economy Plugin!");
        }

        File schematicdic = new File(getDataFolder() + "/schematics");
        if(!schematicdic.exists()){
            schematicdic.mkdirs();
        }

        this.updateConfigs();

        Messages.read();
        ARMListener listener = new ARMListener();
        getServer().getPluginManager().registerEvents(listener, this);
        Gui guilistener = new Gui();
        getServer().getPluginManager().registerEvents(guilistener, this);
        loadAutoPrice();
        loadRegionKind();
        loadGroups();
        loadGUI();
        if(!loadAutoReset()) {
            this.setEnabled(false);
            getLogger().log(Level.WARNING, "SQL Login wrong! Disabeling Plugin...");
            return;
        }
        loadOther();
        loadRegions();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Scheduler() , 0 ,20*10);
        Bukkit.getLogger().log(Level.INFO, "Programmed by Alex9849");

    }

    public void onDisable(){
        Main.econ = null;
        Main.worldguard = null;
        Main.worldedit = null;
        Region.Reset();
        LimitGroup.Reset();
        AutoPrice.Reset();
        RegionKind.Reset();
        SellPreset.reset();
        RentPreset.reset();
        getServer().getServicesManager().unregisterAll(this);
        SignChangeEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockPhysicsEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        getServer().getScheduler().cancelTasks(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        Main.econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupFaWe(){
        Plugin plugin = getServer().getPluginManager().getPlugin("FastAsyncWorldEdit");

        if (plugin == null) {
            return false;
        }
        return true;
    }

    private boolean setupWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return false;
        }
        Main.worldguard = (WorldGuardPlugin) plugin;
        String version = "notSupported";
        if(Main.worldguard.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
        }
        try {
            final Class<?> wgClass = Class.forName("net.liggesmeyer.adapters.WorldGuard" + version);
            if(WorldGuardInterface.class.isAssignableFrom(wgClass)) {
                Main.worldGuardInterface = (WorldGuardInterface) wgClass.newInstance();
            }
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Using WorldGuard" + version + " adapter");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Could not setup WorldGuard! (Handler could not be loaded) Compatible WorldGuard versions: 6, 7");
            e.printStackTrace();
        }

        return worldguard != null;
    }

    private boolean setupWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return false;
        }
        Main.worldedit = (WorldEditPlugin) plugin;
        String version = "notSupported";

        if(Main.worldedit.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
        }
        if(Main.isFaWeInstalled()){
            version = version + "FaWe";
        }
        try {
            final Class<?> weClass = Class.forName("net.liggesmeyer.adapters.WorldEdit" + version);
            if(WorldEditInterface.class.isAssignableFrom(weClass)) {
                Main.worldEditInterface = (WorldEditInterface) weClass.newInstance();
            }
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Using WorldEdit" + version + " adapter");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Could not setup WorldEdit! (Handler could not be loaded) Compatible WorldEdit versions: 6, 7");
            e.printStackTrace();
        }


        return worldedit != null;
    }

    public static WorldGuardPlugin getWorldGuard(){
        return Main.worldguard;
    }

    public static WorldGuardInterface getWorldGuardInterface() {
        return  Main.worldGuardInterface;
    }

    public static WorldEditInterface getWorldEditInterface(){
        return Main.worldEditInterface;
    }

    private void loadRegions() {

        Region.setResetcooldown(getConfig().getInt("Other.userResetCooldown"));

        LinkedList<String> worlds = new LinkedList<String>(Region.getRegionsConf().getConfigurationSection("Regions").getKeys(false));
        if(worlds != null) {
            for(int y = 0; y < worlds.size(); y++) {
                if(Bukkit.getWorld(worlds.get(y)) != null) {
                    LinkedList<String> regions = new LinkedList<String>(Region.getRegionsConf().getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                    if(regions != null) {
                        for(int i = 0; i < regions.size(); i++){
                            String regionworld = Region.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".world");
                            String regionname = regions.get(i);
                            int price = Region.getRegionsConf().getInt("Regions." + worlds.get(y) + "." + regions.get(i) + ".price");
                            boolean sold = Region.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".sold");
                            String kind = Region.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".kind");
                            boolean autoreset = Region.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".autoreset");
                            boolean rentregion = Region.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion");
                            boolean allowonlynewblocks = Region.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".allowonlynewblocks");
                            boolean doBlockReset = Region.getRegionsConf().getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset");
                            long lastreset = Region.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".lastreset");
                            String teleportLocString = Region.getRegionsConf().getString("Regions." + worlds.get(y) + "." + regions.get(i) + ".teleportLoc");
                            Location teleportLoc = null;
                            if(teleportLocString != null) {
                                String[] teleportLocarr = teleportLocString.split(";");
                                World teleportLocWorld = Bukkit.getWorld(teleportLocarr[0]);
                                int teleportLocBlockX = Integer.parseInt(teleportLocarr[1]);
                                int teleportLocBlockY = Integer.parseInt(teleportLocarr[2]);
                                int teleportLocBlockZ = Integer.parseInt(teleportLocarr[3]);
                                float teleportLocPitch = Float.parseFloat(teleportLocarr[4]);
                                float teleportLocYaw = Float.parseFloat(teleportLocarr[5]);
                                teleportLoc = new Location(teleportLocWorld, teleportLocBlockX, teleportLocBlockY, teleportLocBlockZ);
                                teleportLoc.setYaw(teleportLocYaw);
                                teleportLoc.setPitch(teleportLocPitch);
                            }
                            RegionKind regionKind = RegionKind.DEFAULT;
                            if(kind != null){
                                RegionKind result = RegionKind.getRegionKind(kind);
                                if(result != null){
                                    regionKind = result;
                                }
                            }
                            ProtectedRegion region = Main.getWorldGuardInterface().getRegionManager(Bukkit.getWorld(regionworld), Main.worldguard).getRegion(regionname);

                            if(region != null) {
                                List<String> regionsignsloc = Region.getRegionsConf().getStringList("Regions." + worlds.get(y) + "." + regions.get(i) + ".signs");
                                List<Sign> regionsigns = new ArrayList<>();
                                for(int j = 0; j < regionsignsloc.size(); j++) {
                                    String[] locsplit = regionsignsloc.get(j).split(";", 4);
                                    World world = Bukkit.getWorld(locsplit[0]);
                                    Double x = Double.parseDouble(locsplit[1]);
                                    Double yy = Double.parseDouble(locsplit[2]);
                                    Double z = Double.parseDouble(locsplit[3]);
                                    Location loc = new Location(world, x, yy, z);
                                    Location locminone = new Location(world, x, yy - 1, z);

                                    if ((loc.getBlock().getType() != Material.SIGN) && (loc.getBlock().getType() != Material.WALL_SIGN)){
                                        if(locminone.getBlock().getType() == Material.AIR || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER
                                                || locminone.getBlock().getType() == Material.LAVA || locminone.getBlock().getType() == Material.WATER) {
                                            locminone.getBlock().setType(Material.STONE);
                                        }
                                        loc.getBlock().setType(Material.SIGN);

                                    }

                                    regionsigns.add((Sign) loc.getBlock().getState());
                                }
                                if (rentregion){
                                    long payedtill = Region.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".payedTill");
                                    long maxRentTime = Region.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".maxRentTime");
                                    long rentExtendPerClick = Region.getRegionsConf().getLong("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentExtendPerClick");
                                    Region.getRegionList().add(new RentRegion(region, regionworld, regionsigns, price, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc,
                                            lastreset, payedtill, maxRentTime, rentExtendPerClick,false));
                                } else {
                                    Region.getRegionList().add(new SellRegion(region, regionworld, regionsigns, price, sold, autoreset, allowonlynewblocks, doBlockReset, regionKind, teleportLoc, lastreset,false));
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < Region.getRegionList().size(); i++) {
            Region.getRegionList().get(i).writeSigns();
        }
    }

    public static Boolean isFaWeInstalled(){
        return Main.faWeInstalled;
    }

    private void loadAutoPrice() {
        LinkedList<String> autoPrices = new LinkedList<>(getConfig().getConfigurationSection("AutoPrice").getKeys(false));
        if(autoPrices != null) {
            for(int i = 0; i < autoPrices.size(); i++){
                AutoPrice.getAutoPrices().add(new AutoPrice(autoPrices.get(i), getConfig().getDouble("AutoPrice." + autoPrices.get(i))));
            }
        }
    }

    private void loadRegionKind(){
        RegionKind.DEFAULT.setName(getConfig().getString("DefaultRegionKind.DisplayName"));
        RegionKind.DEFAULT.setMaterial(Material.getMaterial(getConfig().getString("DefaultRegionKind.Item")));
        List<String> defaultlore = getConfig().getStringList("DefaultRegionKind.Lore");
        for(int x = 0; x < defaultlore.size(); x++){
            defaultlore.set(x, ChatColor.translateAlternateColorCodes('&', defaultlore.get(x)));
        }
        RegionKind.DEFAULT.setLore(defaultlore);

        LinkedList<String> regionKinds = new LinkedList<String>(getConfig().getConfigurationSection("RegionKinds").getKeys(false));
        if(regionKinds != null) {
            for(int i = 0; i < regionKinds.size(); i++){
                Material mat = Material.getMaterial(getConfig().getString("RegionKinds." + regionKinds.get(i) + ".item"));
                List<String> lore = getConfig().getStringList("RegionKinds." + regionKinds.get(i) + ".lore");
                for(int x = 0; x < lore.size(); x++){
                    lore.set(x, ChatColor.translateAlternateColorCodes('&', lore.get(x)));
                }
                RegionKind.getRegionKindList().add(new RegionKind(regionKinds.get(i), mat, lore));
            }
        }
    }

    private void loadGUI(){
        FileConfiguration pluginConf = getConfig();
        Gui.setRegionOwnerItem(Material.getMaterial(pluginConf.getString("GUI.RegionOwnerItem")));
        Gui.setRegionMemberItem(Material.getMaterial(pluginConf.getString("GUI.RegionMemberItem")));
        Gui.setRegionFinderItem(Material.getMaterial(pluginConf.getString("GUI.RegionFinderItem")));
        Gui.setGoBackItem(Material.getMaterial(pluginConf.getString("GUI.GoBackItem")));
        Gui.setWarningYesItem(Material.getMaterial(pluginConf.getString("GUI.WarningYesItem")));
        Gui.setWarningNoItem(Material.getMaterial(pluginConf.getString("GUI.WarningNoItem")));
        Gui.setTpItem(Material.getMaterial(pluginConf.getString("GUI.Region.TPItem")));
        Gui.setSellRegionItem(Material.getMaterial(pluginConf.getString("GUI.Region.SellRegionItem")));
        Gui.setResetItem(Material.getMaterial(pluginConf.getString("GUI.Region.ResetItem")));
        Gui.setExtendItem(Material.getMaterial(pluginConf.getString("GUI.RentRegion.ExtendItem")));
        Gui.setInfoItem(Material.getMaterial(pluginConf.getString("GUI.Region.InfoItem")));
        Gui.setPromoteMemberToOwnerItem(Material.getMaterial(pluginConf.getString("GUI.Region.PromoteMemberToOwnerItem")));
        Gui.setRemoveMemberItem(Material.getMaterial(pluginConf.getString("GUI.Region.RemoveMemberItem")));
    }

    private Boolean loadAutoReset(){
        Boolean success = true;
        Main.enableAutoReset = getConfig().getBoolean("AutoResetAndTakeOver.enableAutoReset");
        Main.enableTakeOver = getConfig().getBoolean("AutoResetAndTakeOver.enableTakeOver");
        if(Main.enableAutoReset || Main.enableTakeOver) {
            String mysqlhost = getConfig().getString("AutoResetAndTakeOver.mysql-server");
            String mysqldatabase = getConfig().getString("AutoResetAndTakeOver.mysql-database");
            String mysqlpass = getConfig().getString("AutoResetAndTakeOver.mysql-password");
            String mysqluser = getConfig().getString("AutoResetAndTakeOver.mysql-user");
            Main.sqlPrefix = getConfig().getString("AutoResetAndTakeOver.mysql-prefix");
            Main.autoResetAfter = getConfig().getInt("AutoResetAndTakeOver.autoresetAfter");
            Main.takeoverAfter = getConfig().getInt("AutoResetAndTakeOver.takeoverAfter");

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection("jdbc:mysql://" + mysqlhost + "/" + mysqldatabase, mysqluser, mysqlpass);
                Main.stmt = con.createStatement();
                Main.checkOrCreateMySql(mysqldatabase);
                getLogger().log(Level.INFO, "SQL Login successful!");
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                getLogger().log(Level.INFO, "SQL Login faulty!");
                success = false;
            }
        }
        return success;
    }

    private void loadGroups(){
        List<String> groups = new ArrayList<>(getConfig().getConfigurationSection("Limits").getKeys(false));
        if(groups != null) {
            for(int i = 0; i < groups.size(); i++) {
                LimitGroup.getGroupList().add(new LimitGroup(groups.get(i)));
            }
        }
    }

    private void loadOther(){
        Main.teleportAfterRentRegionBought = getConfig().getBoolean("Other.TeleportAfterRentRegionBought");
        Main.teleportAfterRentRegionExtend = getConfig().getBoolean("Other.TeleportAfterRentRegionExtend");
        Main.teleportAfterSellRegionBought = getConfig().getBoolean("Other.TeleportAfterSellRegionBought");
        Main.teleportAfterSellRegionBought = getConfig().getBoolean("Other.TeleportAfterSellRegionBought");
        Main.displayDefaultRegionKindInGUI = getConfig().getBoolean("DefaultRegionKind.DisplayInGUI");
        Main.displayDefaultRegionKindInLimits = getConfig().getBoolean("DefaultRegionKind.DisplayInLimits");
        Region.setPaypackPercentage(getConfig().getDouble("Other.paypackPercentage"));
    }

    private static void checkOrCreateMySql(String mysqldatabase) throws SQLException {
        ResultSet rs = Main.stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'");
        Boolean createLastlogin = true;
        while (rs.next()){
            if(rs.getString("TABLE_NAME").equals(Main.sqlPrefix + "lastlogin")){
                createLastlogin = false;
            }
        }
        if(createLastlogin){
            Main.stmt.executeUpdate("CREATE TABLE `" + mysqldatabase + "`.`" + Main.sqlPrefix + "lastlogin` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(40) NOT NULL , `lastlogin` TIMESTAMP NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
        }

    }

    public static Economy getEcon(){
        return Main.econ;
    }

    public static WorldEditPlugin getWorldedit() {return Main.worldedit;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) {
        String allargs = "";
        for (int i = 0; i < args.length; i++) {
            allargs = allargs + " " + args[i];
        }

        if (cmd.getName().equalsIgnoreCase("arm")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("setregionkind")) {
                    if (allargs.matches(Main.SET_REGION_KIND)) {                         //DONE
                        return Region.setRegionKindCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setregionkind [REGIONKIND] [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregionkinds")) {
                    if (allargs.matches(Main.LIST_REGION_KIND)) {                  //Done
                        return Region.listRegionKindsCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregionkinds");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("findfreeregion")) {
                    if (allargs.matches(Main.FIND_REGION_BY_TYPE)) {              //DONE
                        if (sender instanceof Player) {
                            return Region.teleportToFreeRegionCommand(args[1], ((Player) sender).getPlayer());
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm findfreeregion [REGIONKIND]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (allargs.matches(Main.HELP)) {                            //DONE
                        return Main.help(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("resetblocks")) {
                    if (allargs.matches(Main.RESET_REGION_BLOCKS)) {             //DONE
                        return Region.resetRegionBlocksCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm resetblocks [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (allargs.matches(Main.RESET_REGION)) {                    //DONE
                        return Region.resetRegionCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm reset [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (allargs.matches(Main.REGION_INFO_WITHOUT_REGIONNAME)) {   //DONE
                        return Region.regionInfoCommand(sender);
                    } else if (allargs.matches(Main.REGION_INFO_WITH_REGIONNAME)) {      //DONE
                        return Region.regionInfoCommand(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm info [REGION] or /arm info");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("addmember")) {
                    if (allargs.matches(Main.ADDMEMBER_WITH_REGIONNAME)) {        //DONE
                        return Region.addMemberCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm addmember [REGION] [NEWMEMBER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("removemember")) {
                    if (allargs.matches(Main.REMOVEMEMBER_WITH_REGIONNAME)) {      //DONE
                        return Region.removeMemberCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm removemember [REGION] [OLDMEMBER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("gui")) {
                    if (allargs.matches(" gui")) {
                        if (sender instanceof Player) {
                            if (sender.hasPermission(Permission.MEMBER_GUI)) {
                                Gui.openARMGui((Player) sender);
                            } else {
                                sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm gui");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("autoreset")) {
                    if (allargs.matches(Main.CHANGE_AUTORESET)) {
                        return Region.changeAutoresetCommand(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm autoreset [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregions")) {
                    if (allargs.matches(Main.LIST_REGIONS_OTHER)) {
                        return Region.listRegionsCommand(sender, args[1]);
                    } else if (allargs.matches(Main.LIST_REGIONS)) {
                        return Region.listRegionsCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregions or /arm listregions [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("hotel")) {
                    if (allargs.matches(Main.SET_ALLOW_ONLY_NEW_BLOCKS)) {
                        return Region.setHotel(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm hotel [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("updateschematic")) {
                    if (allargs.matches(Main.CREATE_NEW_SCHEMATIC)) {
                        return Region.createNewSchematic(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm updateschematic [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setowner")) {
                    if (allargs.matches(Main.SET_OWNER)) {
                        return Region.setRegionOwner(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setowner [REGION] [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("tp")) {
                    if (allargs.matches(Main.TELEPORT)) {
                        return Region.teleport(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm tp [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("limit")) {
                if (allargs.matches(" limit")) {
                    if (sender.hasPermission(Permission.MEMBER_LIMIT)) {
                        LimitGroup.getLimitCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                    }
                    return true;
                } else {
                    sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm limit");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("setwarp")) {
                    if (allargs.matches(SET_WARP)) {
                        if (sender.hasPermission(Permission.ADMIN_SETWARP)) {
                            Region.setTeleportLocation(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setwarp [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (allargs.matches(RELOAD)) {
                        if (sender.hasPermission(Permission.ADMIN_RELOAD)) {
                            sender.sendMessage(Messages.PREFIX + "Reloading...");
                            this.onDisable();
                            Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket").reloadConfig();
                            this.onEnable();
                            sender.sendMessage(Messages.PREFIX + "Complete!");
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm reload");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("unsell")) {
                    if (allargs.matches(UNSELL)) {
                        if (sender.hasPermission(Permission.ADMIN_UNSELL)) {
                            Region.unsellCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm unsell [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("extend")) {
                    if (allargs.matches(EXTEND)) {
                        if (sender.hasPermission(Permission.ARM_BUY_RENTREGION)) {
                            Region.extendCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm extend [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (allargs.matches(DELETE)) {
                        if (sender.hasPermission(Permission.ADMIN_DELETEREGION)) {
                            Region.deleteCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm delete [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("doblockreset")) {
                    if (allargs.matches(SET_DO_BLOCK_RESET)) {
                        if (sender.hasPermission(Permission.ADMIN_CHANGE_DO_BLOCK_RESET)) {
                            Region.setDoBlockResetCommand(args[1], args[2], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm doblockreset [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("sellpreset")) {
                    if (allargs.matches(SELLPRESET)) {
                        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
                            SellPreset.onCommand(sender, allargs, args);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset [SETTING]");
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "or (for help): /arm sellpreset help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("rentpreset")) {
                    if (allargs.matches(RENTPRESET)) {
                        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
                            RentPreset.onCommand(sender, allargs, args);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm rentpreset [SETTING]");
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "or (for help): /arm rentpreset help");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isDisplayDefaultRegionKindInGUI(){
        return Main.displayDefaultRegionKindInGUI;
    }

    public static boolean isDisplayDefaultRegionKindInLimits(){
        return Main.displayDefaultRegionKindInLimits;
    }

    private static boolean help(CommandSender sender) {
        if(!sender.hasPermission(Permission.ARM_HELP)){
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "/arm setregionkind [KIND] [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm listregionkinds");
        sender.sendMessage(ChatColor.GOLD + "/arm findfreeregion [KIND]");
        sender.sendMessage(ChatColor.GOLD + "/arm resetblocks [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm reset [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm info [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm addmember [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm removemember [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm autoreset [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm listregions [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm setowner [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm hotel [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm updateschematic [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm setwarp [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm unsell [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm extend [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm delete [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm doblockreset [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm sellpreset [SETTING]");
        sender.sendMessage(ChatColor.GOLD + "/arm rentpreset [SETTING]");
        sender.sendMessage(ChatColor.GOLD + "/arm limit");
        sender.sendMessage(ChatColor.GOLD + "/arm reload");
        return true;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static String getSqlPrefix(){
        return Main.sqlPrefix;
    }

    public static boolean getEnableAutoReset(){
        return Main.enableAutoReset;
    }

    public static int getAutoResetAfter(){
        return Main.autoResetAfter;
    }

    public static boolean getEnableTakeOver(){
        return Main.enableTakeOver;
    }

    public static int getTakeoverAfter(){
        return Main.takeoverAfter;
    }

    public static boolean isTeleportAfterRentRegionBought() {
        return teleportAfterRentRegionBought;
    }

    public static boolean isTeleportAfterSellRegionBought() {
        return teleportAfterSellRegionBought;
    }

    public static boolean isTeleportAfterRentRegionExtend() {
        return teleportAfterRentRegionExtend;
    }

    public static boolean isAllowStartup(Plugin plugin){
        Server server = Bukkit.getServer();
        String ip = server.getIp();
        int port = server.getPort();
        String hoststring = "";

        try {
            hoststring = InetAddress.getLocalHost().toString();
        } catch (Exception e) {
        }



        Boolean allowStart = true;

        try {
            final String userAgent = "Alex9849 Plugin";
            String str=null;
            String str1=null;
            URL url = new URL("http://mcplug.liggesmeyer.net/plugin.php?plugin=arm&host=" + hoststring + "&ip=" + ip + "&port=" + port);
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-Agent", userAgent);

            BufferedReader in = new BufferedReader(new InputStreamReader(((HttpURLConnection) con).getInputStream(), Charset.forName("UTF-8")));

            str = new String();
            while ((str1 = in.readLine()) != null) {
                str = str + str1;
            }
            in.close();
            if(str.equals("1")){
                allowStart = false;
            } else {
                allowStart = true;
            }


        } catch (IOException e) {
            return allowStart;
        }
        return allowStart;
    }

    public ARMAPI getAPI(){
        return new ARMAPI();
    }

    public void generatedefaultconfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/config.yml");
        if(!messagesdic.exists()){
            try {
                InputStream stream = plugin.getResource("config.yml");
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
        this.reloadConfig();
    }

    private void updateConfigs(){
        Region.generatedefaultConfig();
        Region.setRegionsConf();
        Messages.generatedefaultConfig();
        Preset.generatedefaultConfig();
        Preset.loadConfig();
        SellPreset.loadPresets();
        this.generatedefaultconfig();
        FileConfiguration pluginConfig = this.getConfig();
        YamlConfiguration regionConf = Region.getRegionsConf();
        Double version = pluginConfig.getDouble("Version");
        if(version < 1.1) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.1...");
            pluginConfig.set("GUI.RegionOwnerItem", Material.ENDER_CHEST.toString());
            pluginConfig.set("GUI.RegionMemberItem", Material.CHEST.toString());
            pluginConfig.set("GUI.RegionFinderItem", Material.COMPASS.toString());
            pluginConfig.set("GUI.GoBackItem", "WOOD_DOOR");
            pluginConfig.set("GUI.WarningYesItem", "MELON_BLOCK");
            pluginConfig.set("GUI.WarningNoItem", Material.REDSTONE_BLOCK.toString());
            pluginConfig.set("GUI.TPItem", Material.ENDER_PEARL.toString());
            pluginConfig.set("GUI.SellRegionItem", Material.DIAMOND.toString());
            pluginConfig.set("GUI.ResetItem", Material.TNT.toString());
            pluginConfig.set("GUI.ExtendItem", "WATCH");
            pluginConfig.set("GUI.InfoItem", Material.BOOK.toString());
            pluginConfig.set("GUI.PromoteMemberToOwnerItem", Material.LADDER.toString());
            pluginConfig.set("GUI.RemoveMemberItem", Material.LAVA_BUCKET.toString());
            pluginConfig.set("Version", 1.1);
            saveConfig();
        }
        version = pluginConfig.getDouble("Version");
        if(version < 1.2) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.2...");
            getLogger().log(Level.WARNING, "Warning!: ARM uses a new schematic format now! You have to update all region schematics with");
            getLogger().log(Level.WARNING, "/arm updateschematic [REGION] or go back to ARM version 1.1");
            pluginConfig.set("Version", 1.2);
            saveConfig();


            LinkedList<String> worlds = new LinkedList<String>(regionConf.getConfigurationSection("Regions").getKeys(false));
            if(worlds != null) {
                for(int y = 0; y < worlds.size(); y++) {
                    LinkedList<String> regions = new LinkedList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                    if(regions != null) {
                        for (int i = 0; i < regions.size(); i++) {
                            regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset", true);
                        }
                    }
                }
            }
            Region.saveRegionsConf(regionConf);
            YamlConfiguration messagesconf = Messages.getConfig();
            messagesconf.set("Messages.RegionIsNotARentregion", "&4Region is not a rentregion!");
            messagesconf.set("Messages.RegionNotOwn", "&4You do not own this region!");
            messagesconf.set("Messages.RegionNotSold", "&4Region not sold!");
            messagesconf.set("Messages.PresetRemoved", "&aPreset removed!");
            messagesconf.set("Messages.PresetSet", "&aPreset set!");
            messagesconf.set("Messages.RegionInfoDoBlockReset", "&6DoBlockReset: ");
            messagesconf.set("Messages.PresetSaved", "&aPreset saved!");
            messagesconf.set("Messages.PresetAlreadyExists", "&4A preset with this name already exists!");
            messagesconf.set("Messages.PresetPlayerDontHasPreset", "&4You do not have a preset!");
            messagesconf.set("Messages.PresetDeleted", "&aPreset deleted!");
            messagesconf.set("Messages.PresetNotFound", "&4No preset with this name found!");
            messagesconf.set("Messages.PresetLoaded", "&aPreset loaded!");
            Messages.saveConfig();
        }
        if(version < 1.21) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.21...");
            Material mat = null;
            mat = Material.getMaterial(pluginConfig.getString("GUI.RegionOwnerItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.RegionOwnerItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.RegionMemberItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.RegionMemberItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.RegionFinderItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.RegionFinderItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.GoBackItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.GoBackItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.WarningYesItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.WarningYesItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.WarningNoItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.WarningNoItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.SellRegionItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.SellRegionItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.ResetItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.ResetItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.ExtendItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.ExtendItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.InfoItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.InfoItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.PromoteMemberToOwnerItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.PromoteMemberToOwnerItem", mat.toString());
            }
            mat = Material.getMaterial(pluginConfig.getString("GUI.RemoveMemberItem"), true);
            if(mat != null) {
                pluginConfig.set("GUI.RemoveMemberItem", mat.toString());
            }

            LinkedList<String> regionKinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
            if(regionKinds != null) {
                for(int i = 0; i < regionKinds.size(); i++){
                    mat = Material.getMaterial(pluginConfig.getString("RegionKinds." + regionKinds.get(i) + ".item"), true);
                    if(mat != null) {
                        pluginConfig.set("RegionKinds." + regionKinds.get(i) + ".item", mat.toString());
                    }
                }
            }
            pluginConfig.set("Version", 1.21);
            saveConfig();
        }
        if(version < 1.3) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.3...");
            pluginConfig.set("DefaultRegionKind.DisplayName", "Default");
            pluginConfig.set("DefaultRegionKind.Item", Material.RED_BED.toString());
            List<String> defaultLore = new ArrayList<>();
            defaultLore.add("very default");
            pluginConfig.set("DefaultRegionKind.Lore", defaultLore);
            pluginConfig.set("DefaultRegionKind.DisplayInLimits", true);
            pluginConfig.set("DefaultRegionKind.DisplayInGUI", false);
            pluginConfig.set("Version", 1.3);
            saveConfig();
        }
    }
}

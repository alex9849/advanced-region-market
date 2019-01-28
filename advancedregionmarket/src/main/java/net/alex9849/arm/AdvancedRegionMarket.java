package net.alex9849.arm;

import net.alex9849.arm.Handler.ARMListener;
import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Handler.Scheduler;
import net.alex9849.arm.commands.*;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.exceptions.InputException;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.gui.Gui;
import net.alex9849.inter.WorldEditInterface;
import net.alex9849.inter.WorldGuardInterface;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import net.alex9849.arm.Preseter.ActivePresetManager;
import net.alex9849.arm.Preseter.PresetPatternManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.*;
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
import java.util.*;
import java.util.logging.Level;

public class AdvancedRegionMarket extends JavaPlugin {
    private static Boolean faWeInstalled;
    private static Economy econ;
    private static WorldGuardPlugin worldguard;
    private static WorldGuardInterface worldGuardInterface;
    private static WorldEditPlugin worldedit;
    private static WorldEditInterface worldEditInterface;
    private static CommandHandler commandHandler;

    public void onEnable(){

        if(!AdvancedRegionMarket.isAllowStartup(this)){
            this.setEnabled(false);
            getLogger().log(Level.WARNING, "Plugin remotely deactivated!");
            return;
        }

        //Enable bStats
        Metrics metrics = new Metrics(this);

        AdvancedRegionMarket.faWeInstalled = setupFaWe();

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
        loadAutoReset();
        if(!connectSQL()) {
            getLogger().log(Level.INFO, "SQL Login failed!");
            getLogger().log(Level.WARNING, "SQL Login wrong! Disabeling Plugin...");
            this.setEnabled(false);
            return;
        }
        loadOther();
        PresetPatternManager.loadPresetPatterns();
        RegionManager.loadRegionsFromConfig();
        Region.setCompleteTabRegions(getConfig().getBoolean("Other.CompleteRegionsOnTabComplete"));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Scheduler() , 0 ,20*getConfig().getInt("Other.SignAndResetUpdateInterval"));
        AdvancedRegionMarket.commandHandler = new CommandHandler(new ArrayList<>(Arrays.asList("help")), "");
        List<BasicArmCommand> commands = new ArrayList<>();
        String[] betweencmds = {};
        commands.add(new AddMemberCommand());
        commands.add(new AutoResetCommand());
        commands.add(new ContractPresetCommand());
        commands.add(new DeleteCommand());
        commands.add(new DoBlockResetCommand());
        commands.add(new ExtendCommand());
        commands.add(new RegionfinderCommand());
        commands.add(new GuiCommand());
        commands.add(new HelpCommand(AdvancedRegionMarket.commandHandler, Messages.HELP_HEADLINE, betweencmds, Permission.ARM_HELP));
        commands.add(new HotelCommand());
        commands.add(new InfoCommand());
        commands.add(new LimitCommand());
        commands.add(new ListRegionKindsCommand());
        commands.add(new ListRegionsCommand());
        commands.add(new OfferCommand());
        commands.add(new RegionstatsCommand());
        commands.add(new ReloadCommand());
        commands.add(new RemoveMemberCommand());
        commands.add(new RentPresetCommand());
        commands.add(new ResetBlocksCommand());
        commands.add(new ResetCommand());
        commands.add(new SellPresetCommand());
        commands.add(new SetOwnerCommand());
        commands.add(new SetRegionKind());
        commands.add(new SetWarpCommand());
        commands.add(new TerminateCommand());
        commands.add(new TPCommand());
        commands.add(new UnsellCommand());
        commands.add(new UpdateSchematicCommand());
        commands.add(new BuyCommand());
        commands.add(new SellBackCommand());
        commands.add(new SubRegionCommand());
        commands.add(new SetSubregionLimit());
        commands.add(new SetPriceCommand());
        commands.add(new SetIsUserResettableCommand());
        commands.add(new ListAutoPricesCommand());
        commands.add(new SignLinkModeCommand());
        AdvancedRegionMarket.commandHandler.addCommands(commands);

        getCommand("arm").setTabCompleter(this.commandHandler);
        Bukkit.getLogger().log(Level.INFO, "Programmed by Alex9849");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                RegionManager.writeRegionsToConfig(false);
            }
        }, 0, 60);
    }

    public void onDisable(){
        AdvancedRegionMarket.econ = null;
        AdvancedRegionMarket.worldguard = null;
        AdvancedRegionMarket.worldedit = null;
        RegionManager.Reset();
        LimitGroup.Reset();
        AutoPrice.reset();
        RegionKind.Reset();
        SignLinkMode.reset();
        PresetPatternManager.resetPresetPatterns();
        ActivePresetManager.reset();
        getServer().getServicesManager().unregisterAll(this);
        SignChangeEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockPhysicsEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        BlockExplodeEvent.getHandlerList().unregister(this);
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
        AdvancedRegionMarket.econ = rsp.getProvider();
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
        AdvancedRegionMarket.worldguard = (WorldGuardPlugin) plugin;
        String version = "notSupported";
        if(AdvancedRegionMarket.worldguard.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {

           version = "7";

           if((parseWorldGuardBuildNumber(worldguard) != null) && (parseWorldGuardBuildNumber(worldguard) < 1754)){
               version = "7Beta01";
           }
           if(isFaWeInstalled()) {
               version = "7FaWe";
           }
        }
        try {
            final Class<?> wgClass = Class.forName("net.alex9849.adapters.WorldGuard" + version);
            if(WorldGuardInterface.class.isAssignableFrom(wgClass)) {
                AdvancedRegionMarket.worldGuardInterface = (WorldGuardInterface) wgClass.newInstance();
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
        AdvancedRegionMarket.worldedit = (WorldEditPlugin) plugin;
        String version = "notSupported";
        Boolean hasFaWeHandler = true;

        if(AdvancedRegionMarket.worldedit.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
            if(AdvancedRegionMarket.worldedit.getDescription().getVersion().contains("beta-01") || ((parseWorldEditBuildNumber(worldedit) != null) && (parseWorldEditBuildNumber(worldedit) < 3930))){
                version = "7Beta01";
                hasFaWeHandler = false;
            }
        }

        if(AdvancedRegionMarket.isFaWeInstalled() && hasFaWeHandler){
            version = version + "FaWe";
        }

        try {
            final Class<?> weClass = Class.forName("net.alex9849.adapters.WorldEdit" + version);
            if(WorldEditInterface.class.isAssignableFrom(weClass)) {
                AdvancedRegionMarket.worldEditInterface = (WorldEditInterface) weClass.newInstance();
            }
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Using WorldEdit" + version + " adapter");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Could not setup WorldEdit! (Handler could not be loaded) Compatible WorldEdit versions: 6, 7");
            e.printStackTrace();
        }


        return worldedit != null;
    }

    private Integer parseWorldGuardBuildNumber(WorldGuardPlugin wg) {

        String version = wg.getDescription().getVersion();
        if(!version.contains("-SNAPSHOT;")) {
            return null;
        }

        String buildNumberString = version.substring(version.indexOf("-SNAPSHOT;") + 10);

        if(buildNumberString.contains("-")) {
            buildNumberString = buildNumberString.substring(0, buildNumberString.indexOf("-"));
        }

        try {
            return Integer.parseInt(buildNumberString);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private Integer parseWorldEditBuildNumber(WorldEditPlugin wg) {

        String version = wg.getDescription().getVersion();
        if(!version.contains("-SNAPSHOT;")) {
            return null;
        }

        String buildNumberString = version.substring(version.indexOf("-SNAPSHOT;") + 10);

        if(buildNumberString.contains("-")) {
            buildNumberString = buildNumberString.substring(0, buildNumberString.indexOf("-"));
        }

        try {
            return Integer.parseInt(buildNumberString);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public static CommandHandler getCommandHandler() {
        return AdvancedRegionMarket.commandHandler;
    }

    public static WorldGuardPlugin getWorldGuard(){
        return AdvancedRegionMarket.worldguard;
    }

    public static WorldGuardInterface getWorldGuardInterface() {
        return  AdvancedRegionMarket.worldGuardInterface;
    }

    public static WorldEditInterface getWorldEditInterface(){
        return AdvancedRegionMarket.worldEditInterface;
    }

    public static AdvancedRegionMarket getARM() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket");
        if(plugin instanceof AdvancedRegionMarket) {
            return (AdvancedRegionMarket) plugin;
        } else {
            return null;
        }
    }

    public static Boolean isFaWeInstalled(){
        return AdvancedRegionMarket.faWeInstalled;
    }

    private void loadAutoPrice() {
        if(getConfig().getConfigurationSection("AutoPrice") != null) {
            AutoPrice.loadAutoprices(getConfig().getConfigurationSection("AutoPrice"));
        }
        if(getConfig().getConfigurationSection("DefaultAutoprice") != null) {
            AutoPrice.loadDefaultAutoPrice(getConfig().getConfigurationSection("DefaultAutoprice"));
        }
    }

    private void loadRegionKind(){
        RegionKind.DEFAULT.setName(getConfig().getString("DefaultRegionKind.DisplayName"));
        RegionKind.DEFAULT.setMaterial(Material.getMaterial(getConfig().getString("DefaultRegionKind.Item")));
        RegionKind.DEFAULT.setDisplayInGUI(getConfig().getBoolean("DefaultRegionKind.DisplayInGUI"));
        RegionKind.DEFAULT.setDisplayInLimits(getConfig().getBoolean("DefaultRegionKind.DisplayInLimits"));
        RegionKind.DEFAULT.setPaybackPercentage(getConfig().getDouble("DefaultRegionKind.PaypackPercentage"));
        List<String> defaultlore = getConfig().getStringList("DefaultRegionKind.Lore");
        for(int x = 0; x < defaultlore.size(); x++){
            defaultlore.set(x, ChatColor.translateAlternateColorCodes('&', defaultlore.get(x)));
        }
        RegionKind.DEFAULT.setLore(defaultlore);

        RegionKind.SUBREGION.setName(getConfig().getString("SubregionRegionKind.DisplayName"));
        RegionKind.SUBREGION.setMaterial(Material.getMaterial(getConfig().getString("SubregionRegionKind.Item")));
        RegionKind.SUBREGION.setDisplayInGUI(getConfig().getBoolean("SubregionRegionKind.DisplayInGUI"));
        RegionKind.SUBREGION.setDisplayInLimits(getConfig().getBoolean("SubregionRegionKind.DisplayInLimits"));
        RegionKind.SUBREGION.setPaybackPercentage(getConfig().getDouble("SubregionRegionKind.PaypackPercentage"));
        List<String> subregionlore = getConfig().getStringList("SubregionRegionKind.Lore");
        for(int x = 0; x < defaultlore.size(); x++){
            defaultlore.set(x, ChatColor.translateAlternateColorCodes('&', defaultlore.get(x)));
        }
        RegionKind.SUBREGION.setLore(defaultlore);

        if(getConfig().get("RegionKinds") != null) {
            LinkedList<String> regionKinds = new LinkedList<String>(getConfig().getConfigurationSection("RegionKinds").getKeys(false));
            if(regionKinds != null) {
                for(int i = 0; i < regionKinds.size(); i++){
                    Material mat = Material.getMaterial(getConfig().getString("RegionKinds." + regionKinds.get(i) + ".item"));
                    String displayName = getConfig().getString("RegionKinds." + regionKinds.get(i) + ".displayName");
                    boolean displayInGUI = getConfig().getBoolean("RegionKinds." + regionKinds.get(i) + ".displayInGUI");
                    boolean displayInLimits = getConfig().getBoolean("RegionKinds." + regionKinds.get(i) + ".displayInLimits");
                    double paybackPercentage = getConfig().getDouble("RegionKinds." + regionKinds.get(i) + ".paypackPercentage");
                    List<String> lore = getConfig().getStringList("RegionKinds." + regionKinds.get(i) + ".lore");
                    for(int x = 0; x < lore.size(); x++){
                        lore.set(x, ChatColor.translateAlternateColorCodes('&', lore.get(x)));
                    }
                    displayName = ChatColor.translateAlternateColorCodes('&', displayName);
                    RegionKind.getRegionKindList().add(new RegionKind(regionKinds.get(i), mat, lore, displayName, displayInGUI, displayInLimits, paybackPercentage));
                }
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
        Gui.setTpItem(Material.getMaterial(pluginConf.getString("GUI.TPItem")));
        Gui.setSellRegionItem(Material.getMaterial(pluginConf.getString("GUI.SellRegionItem")));
        Gui.setResetItem(Material.getMaterial(pluginConf.getString("GUI.ResetItem")));
        Gui.setExtendItem(Material.getMaterial(pluginConf.getString("GUI.ExtendItem")));
        Gui.setInfoItem(Material.getMaterial(pluginConf.getString("GUI.InfoItem")));
        Gui.setPromoteMemberToOwnerItem(Material.getMaterial(pluginConf.getString("GUI.PromoteMemberToOwnerItem")));
        Gui.setRemoveMemberItem(Material.getMaterial(pluginConf.getString("GUI.RemoveMemberItem")));
        Gui.setFillItem(Material.getMaterial(pluginConf.getString("GUI.FillItem")));
        Gui.setContractItem(Material.getMaterial(pluginConf.getString("GUI.ContractItem")));
        Gui.setSubregionItem(Material.getMaterial(pluginConf.getString("GUI.SubRegionItem")));
        Gui.setDeleteItem(Material.getMaterial(pluginConf.getString("GUI.DeleteItem")));
        Gui.setTeleportToSignItem(Material.getMaterial(pluginConf.getString("GUI.TeleportToSignItem")));
        Gui.setTeleportToRegionItem(Material.getMaterial(pluginConf.getString("GUI.TeleportToRegionItem")));
        Gui.setNextPageItem(Material.getMaterial(pluginConf.getString("GUI.NextPageItem")));
        Gui.setPrevPageItem(Material.getMaterial(pluginConf.getString("GUI.PrevPageItem")));
        Gui.setHotelSettingItem(Material.getMaterial(pluginConf.getString("GUI.HotelSettingItem")));
        Gui.setUnsellItem(Material.getMaterial(pluginConf.getString("GUI.UnsellItem")));

    }

    private void loadAutoReset() {
        ArmSettings.setEnableAutoReset(getConfig().getBoolean("AutoResetAndTakeOver.enableAutoReset"));
        ArmSettings.setEnableTakeOver(getConfig().getBoolean("AutoResetAndTakeOver.enableTakeOver"));
    }

    public Boolean connectSQL(){
        Boolean success = true;
        if(ArmSettings.isEnableAutoReset() || ArmSettings.isEnableTakeOver()) {
            String mysqlhost = getConfig().getString("AutoResetAndTakeOver.mysql-server");
            String mysqldatabase = getConfig().getString("AutoResetAndTakeOver.mysql-database");
            String mysqlpass = getConfig().getString("AutoResetAndTakeOver.mysql-password");
            String mysqluser = getConfig().getString("AutoResetAndTakeOver.mysql-user");
            ArmSettings.setSqlPrefix(getConfig().getString("AutoResetAndTakeOver.mysql-prefix"));
            ArmSettings.setAutoResetAfter(getConfig().getInt("AutoResetAndTakeOver.autoresetAfter"));
            ArmSettings.setTakeoverAfter(getConfig().getInt("AutoResetAndTakeOver.takeoverAfter"));

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection("jdbc:mysql://" + mysqlhost + "/" + mysqldatabase, mysqluser, mysqlpass);
                ArmSettings.setStmt(con.createStatement());
                AdvancedRegionMarket.checkOrCreateMySql(mysqldatabase);
                getLogger().log(Level.INFO, "SQL Login successful!");
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                success = false;
            }
        }
        return success;
    }

    private void loadGroups(){
        if(getConfig().get("Limits") != null) {
            List<String> groups = new ArrayList<>(getConfig().getConfigurationSection("Limits").getKeys(false));
            if(groups != null) {
                for(int i = 0; i < groups.size(); i++) {
                    LimitGroup.getGroupList().add(new LimitGroup(groups.get(i)));
                }
            }
        }
    }

    private void loadOther(){
        ArmSettings.setIsTeleportAfterRentRegionBought(getConfig().getBoolean("Other.TeleportAfterRentRegionBought"));
        ArmSettings.setIsTeleportAfterRentRegionExtend(getConfig().getBoolean("Other.TeleportAfterRentRegionExtend"));
        ArmSettings.setIsTeleportAfterSellRegionBought(getConfig().getBoolean("Other.TeleportAfterSellRegionBought"));
        ArmSettings.setIsTeleportAfterContractRegionBought(getConfig().getBoolean("Other.TeleportAfterContractRegionBought"));
        ArmSettings.setIsSendContractRegionExtendMessage(getConfig().getBoolean("Other.SendContractRegionExtendMessage"));
        Region.setResetcooldown(getConfig().getInt("Other.userResetCooldown"));
        ArmSettings.setRemainingTimeTimeformat(getConfig().getString("Other.RemainingTimeFormat"));
        ArmSettings.setDateTimeformat(getConfig().getString("Other.DateTimeFormat"));
        ArmSettings.setUseShortCountdown(getConfig().getBoolean("Other.ShortCountdown"));
        ArmSettings.setIsAllowTeleportToBuySign(getConfig().getBoolean("Other.RegionInfoParticleBorder"));

        ArmSettings.setIsAllowSubRegionUserReset(getConfig().getBoolean("Subregions.AllowSubRegionUserReset"));
        ArmSettings.setIsSubregionBlockReset(getConfig().getBoolean("Subregions.SubregionBlockReset"));
        ArmSettings.setIsSubregionAutoReset(getConfig().getBoolean("Subregions.SubregionAutoReset"));
        ArmSettings.setDeleteSubregionsOnParentRegionBlockReset(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionBlockReset"));
        ArmSettings.setDeleteSubregionsOnParentRegionUnsell(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionUnsell"));
        ArmSettings.setAllowParentRegionOwnersBuildOnSubregions(getConfig().getBoolean("Subregions.allowParentRegionOwnersBuildOnSubregions"));
        try{
            RentRegion.setExpirationWarningTime(RentPrice.stringToTime(getConfig().getString("Other.RentRegionExpirationWarningTime")));
            RentRegion.setSendExpirationWarning(getConfig().getBoolean("Other.SendRentRegionExpirationWarning"));
        } catch (IllegalArgumentException | NullPointerException e) {
            Bukkit.getLogger().log(Level.INFO, "[AdvancedRegionMarket] Warning! Bad syntax of time format \"RentRegionExpirationWarningTime\" disabling it...");
            RentRegion.setExpirationWarningTime(0);
            RentRegion.setSendExpirationWarning(false);
        }
    }

    private static void checkOrCreateMySql(String mysqldatabase) throws SQLException {
        ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'");
        Boolean createLastlogin = true;
        while (rs.next()){
            if(rs.getString("TABLE_NAME").equals(ArmSettings.getSqlPrefix() + "lastlogin")){
                createLastlogin = false;
            }
        }
        if(createLastlogin){
            ArmSettings.getStmt().executeUpdate("CREATE TABLE `" + mysqldatabase + "`.`" + ArmSettings.getSqlPrefix() + "lastlogin` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` VARCHAR(40) NOT NULL , `lastlogin` TIMESTAMP NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
        }

    }

    public static Economy getEcon(){
        return AdvancedRegionMarket.econ;
    }

    public static WorldEditPlugin getWorldedit() {return AdvancedRegionMarket.worldedit;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) {
        try {
            return this.commandHandler.executeCommand(sender, cmd, commandsLabel, args);
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
            return true;
        }
    }

    public static boolean isAllowStartup(Plugin plugin){
        Server server = Bukkit.getServer();
        String ip = server.getIp();
        int port = server.getPort();
        String hoststring = "";

        try {
            hoststring = InetAddress.getLocalHost().toString();
        } catch (Exception e) {
            hoststring = "";
        }

        Boolean allowStart = true;

        try {
            final String userAgent = "Alex9849 Plugin";
            String str=null;
            String str1=null;
            URL url = new URL("http://mcplug.alex9849.net/mcplug.php");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setInstanceFollowRedirects(true);
            con.addRequestProperty("User-Agent", userAgent);
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());

            ps.print("plugin=arm");
            ps.print("&host=" + hoststring);
            ps.print("&ip=" + ip);
            ps.print("&port=" + port);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));

            ps.close();

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
        RegionManager.generatedefaultConfig();
        RegionManager.setRegionsConf();
        Messages.generatedefaultConfig();
        PresetPatternManager.generatedefaultConfig();
        this.generatedefaultconfig();
        FileConfiguration pluginConfig = this.getConfig();
        YamlConfiguration regionConf = RegionManager.getRegionsConf();
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
            RegionManager.saveRegionsConf();
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
            pluginConfig.set("Other.SendRentRegionExpirationWarning", true);
            pluginConfig.set("Other.RentRegionExpirationWarningTime", "2d");
            pluginConfig.set("Other.TeleportAfterContractRegionBought", true);
            pluginConfig.set("Other.SendContractRegionExtendMessage", true);
            pluginConfig.set("Other.SignAndResetUpdateInterval", 10);
            pluginConfig.set("Other.RemainingTimeFormat", "%countdown%");
            pluginConfig.set("Other.DateTimeFormat", "dd.MM.yyyy hh:mm");
            pluginConfig.set("Other.ShortCountdown", false);
            pluginConfig.set("Version", 1.3);
            saveConfig();

            if(regionConf.get("Regions") != null) {
                LinkedList<String> worlds = new LinkedList<String>(regionConf.getConfigurationSection("Regions").getKeys(false));
                if(worlds != null) {
                    for(int y = 0; y < worlds.size(); y++) {
                        if(regionConf.get("Regions." + worlds.get(y)) != null) {
                            LinkedList<String> regions = new LinkedList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                            if(regions != null) {
                                for (int i = 0; i < regions.size(); i++) {
                                    if(regionConf.getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion")) {
                                        regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype", "rentregion");
                                    } else {
                                        regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".regiontype", "sellregion");
                                    }
                                    regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion", null);
                                    regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".world", null);
                                }
                            }
                        }
                    }
                }
            }
            RegionManager.saveRegionsConf();
        }
        if(version < 1.4) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4...");
            pluginConfig.set("GUI.FillItem", "GRAY_STAINED_GLASS_PANE");
            pluginConfig.set("GUI.ContractItem", "WRITABLE_BOOK");
            pluginConfig.set("GUI.DisplayRegionOwnerButton", true);
            pluginConfig.set("GUI.DisplayRegionMemberButton", true);
            pluginConfig.set("GUI.DisplayRegionFinderButton", true);
            pluginConfig.set("Other.CompleteRegionsOnTabComplete", false);
            pluginConfig.set("Version", 1.4);
            if(pluginConfig.get("RegionKinds") != null) {
                LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
                if(regionkinds != null) {
                    for(int y = 0; y < regionkinds.size(); y++) {
                        pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayName", regionkinds.get(y));
                    }
                }
            }
            saveConfig();
        }
        if(version < 1.41) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.1...");
            pluginConfig.set("Version", 1.41);
            saveConfig();
        }
        if(version < 1.44) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.4...");
            pluginConfig.set("Other.TeleporterTimer", 0);
            pluginConfig.set("Other.TeleportAfterRegionBoughtCountdown", false);
            pluginConfig.set("Version", 1.44);
            saveConfig();
        }
        if(version < 1.5) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5...");
            pluginConfig.set("Version", 1.5);
            pluginConfig.set("Reselling.Offers.OfferTimeOut", 30);
            saveConfig();
        }
        if(version < 1.52) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5.2...");
            double paybackPercentage = pluginConfig.getDouble("Other.paypackPercentage");
            pluginConfig.set("DefaultRegionKind.PaypackPercentage", paybackPercentage);

            if(pluginConfig.get("RegionKinds") != null) {
                LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
                if(regionkinds != null) {
                    for(int y = 0; y < regionkinds.size(); y++) {
                        pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".paypackPercentage", paybackPercentage);
                        pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayInLimits", true);
                        pluginConfig.set("RegionKinds." + regionkinds.get(y) + ".displayInGUI", true);
                    }
                }
            }

            pluginConfig.set("Version", 1.52);
            saveConfig();
        }
        if(version < 1.6) {
            getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.6...");
            pluginConfig.set("SubregionRegionKind.DisplayName", "Subregion");
            pluginConfig.set("SubregionRegionKind.Item", "PLAYER_HEAD");
            List<String> subregionRegionKindLore = new ArrayList<>();
            subregionRegionKindLore.add("very subregion");
            pluginConfig.set("SubregionRegionKind.Lore", subregionRegionKindLore);
            pluginConfig.set("SubregionRegionKind.DisplayInLimits", true);
            pluginConfig.set("SubregionRegionKind.DisplayInGUI", false);
            pluginConfig.set("SubregionRegionKind.PaypackPercentage", 0);
            pluginConfig.set("Subregions.AllowSubRegionUserReset", false);
            pluginConfig.set("Subregions.SubregionBlockReset", false);
            pluginConfig.set("Subregions.SubregionAutoReset", true);
            pluginConfig.set("Subregions.deleteSubregionsOnParentRegionUnsell", false);
            pluginConfig.set("Subregions.deleteSubregionsOnParentRegionBlockReset", false);
            pluginConfig.set("Subregions.allowParentRegionOwnersBuildOnSubregions", true);
            pluginConfig.set("Other.RegionInfoParticleBorder", true);
            pluginConfig.set("GUI.SubRegionItem", "GRASS_BLOCK");
            pluginConfig.set("GUI.TeleportToSignItem", "SIGN");
            pluginConfig.set("GUI.TeleportToRegionItem", "GRASS_BLOCK");
            pluginConfig.set("GUI.DeleteItem", "BARRIER");
            pluginConfig.set("GUI.NextPageItem", "ARROW");
            pluginConfig.set("GUI.PrevPageItem", "ARROW");
            pluginConfig.set("GUI.HotelSettingItem", "RED_BED");
            pluginConfig.set("GUI.PrevPageItem", "ARROW");
            pluginConfig.set("GUI.HotelSettingItem", "RED_BED");
            pluginConfig.set("GUI.UnsellItem", "NAME_TAG");

            pluginConfig.set("AutoPrice", null);
            pluginConfig.set("AutoPrice.example1.price", 200);
            pluginConfig.set("AutoPrice.example1.extendTime", 300000);
            pluginConfig.set("AutoPrice.example1.maxRentTime", 2000000);
            pluginConfig.set("AutoPrice.example1.autoPriceCalculation", "static");
            pluginConfig.set("AutoPrice.example2.price", 2);
            pluginConfig.set("AutoPrice.example2.extendTime", 300000);
            pluginConfig.set("AutoPrice.example2.maxRentTime", 2000000);
            pluginConfig.set("AutoPrice.example2.autoPriceCalculation", "per_m2");
            pluginConfig.set("AutoPrice.example3.price", 0.05);
            pluginConfig.set("AutoPrice.example3.extendTime", 300000);
            pluginConfig.set("AutoPrice.example3.maxRentTime", 2000000);
            pluginConfig.set("AutoPrice.example3.autoPriceCalculation", "per_m3");

            pluginConfig.set("DefaultAutoprice.price", 2.0);
            pluginConfig.set("DefaultAutoprice.extendTime", 300000);
            pluginConfig.set("DefaultAutoprice.maxRentTime", 2000000);
            pluginConfig.set("DefaultAutoprice.autoPriceCalculation", "per_m2");

            pluginConfig.set("Version", 1.6);
            saveConfig();
        }
    }
}

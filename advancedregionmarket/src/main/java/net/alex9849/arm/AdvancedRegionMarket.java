package net.alex9849.arm;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.alex9849.arm.commands.DeleteCommand;
import net.alex9849.arm.commands.InfoCommand;
import net.alex9849.arm.commands.*;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.arm.entitylimit.commands.CreateCommand;
import net.alex9849.arm.entitylimit.commands.ListCommand;
import net.alex9849.arm.entitylimit.commands.*;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.flaggroups.FlagGroupManager;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.handler.Scheduler;
import net.alex9849.arm.handler.listener.*;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPatternManager;
import net.alex9849.arm.regionkind.RegionKindManager;
import net.alex9849.arm.regionkind.commands.*;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.subregions.commands.ToolCommand;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import net.alex9849.inter.WorldGuardInterface;
import net.alex9849.signs.SignDataFactory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
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
    private static RegionKindManager regionKindManager;
    private static EntityLimitGroupManager entityLimitGroupManager;
    private static RegionManager regionManager;
    private static PresetPatternManager presetPatternManager;
    private static SignDataFactory signDataFactory;
    private static FlagGroupManager flagGroupManager;

    public void onEnable(){

        //Enable bStats
        BStatsAnalytics bStatsAnalytics = new BStatsAnalytics();
        bStatsAnalytics.register(this);

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

        setupSignDataFactory();

        File schematicdic = new File(getDataFolder() + "/schematics");
        if(!schematicdic.exists()){
            schematicdic.mkdirs();
        }

        this.updateConfigs();

        Messages.read();
        BlockModifyListener blockModifyListener = new BlockModifyListener();
        getServer().getPluginManager().registerEvents(blockModifyListener, this);
        EntitySpawnListener entitySpawnListener = new EntitySpawnListener();
        getServer().getPluginManager().registerEvents(entitySpawnListener, this);
        PlayerJoinQuitEvent playerJoinQuitEvent = new PlayerJoinQuitEvent();
        getServer().getPluginManager().registerEvents(playerJoinQuitEvent, this);
        SignClickListener signClickListener = new SignClickListener();
        getServer().getPluginManager().registerEvents(signClickListener, this);
        SignModifyListener signModifyListener = new SignModifyListener();
        getServer().getPluginManager().registerEvents(signModifyListener, this);
        SubregionMarkerListener subregionMarkerListener = new SubregionMarkerListener();
        getServer().getPluginManager().registerEvents(subregionMarkerListener, this);
        Gui guilistener = new Gui();
        getServer().getPluginManager().registerEvents(guilistener, this);

        if(getConfig().getBoolean("Other.Sendstats")) {
            Thread sendStartup = new Thread(() -> {
                AdvancedRegionMarket.sendStats(this, false);
            });
            sendStartup.start();

            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                Plugin armPlugin = AdvancedRegionMarket.getARM();
                Thread sendPing = new Thread(() -> {
                    AdvancedRegionMarket.sendStats(armPlugin, true);
                });
                sendPing.start();
            }, 6000, 6000);
        }



        AdvancedRegionMarket.regionKindManager = new RegionKindManager(new File(this.getDataFolder() + "/regionkinds.yml"));
        AdvancedRegionMarket.entityLimitGroupManager = new EntityLimitGroupManager(new File(this.getDataFolder() + "/entitylimits.yml"));
        loadAutoPrice();
        loadGroups();
        loadGUI();
        AdvancedRegionMarket.flagGroupManager = new FlagGroupManager(new File(this.getDataFolder() + "/flaggroups.yml"));
        AdvancedRegionMarket.regionManager = new RegionManager(new File(this.getDataFolder() + "/regions.yml"));
        getLogger().log(Level.INFO, "Regions loaded!");
        loadAutoReset();
        if(!connectSQL()) {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    getLogger().log(Level.INFO, "SQL Login failed!");
                    getLogger().log(Level.WARNING, "SQL Login wrong! Please check your config.yml!");
                }
            }, 0, 200);
        }

        loadSignLinkingModeRegions();
        loadOther();
        AdvancedRegionMarket.presetPatternManager = new PresetPatternManager(new File(this.getDataFolder() + "/presets.yml"));
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
        commands.add(new ListRegionsCommand());
        commands.add(new TpToFreeRegion());
        commands.add(new TPCommand());
        commands.add(new UnsellCommand());
        commands.add(new UpdateSchematicCommand());
        commands.add(new BuyCommand());
        commands.add(new SellBackCommand());
        commands.add(new SetSubregionLimit());
        commands.add(new SetPriceCommand());
        commands.add(new SetIsUserResettableCommand());
        commands.add(new ListAutoPricesCommand());
        commands.add(new FlageditorCommand());
        commands.add(new SetFlaggroupCommand());
        commands.add(new SignLinkModeCommand());
        commands.add(new SetEntityLimitCommand());

        List<String> entityLimtUsage = new ArrayList<>(Arrays.asList("entitylimit [SETTING]", "entitylimit help"));
        List<BasicArmCommand> entityLimitCommands = new ArrayList<>();
        entityLimitCommands.add(new CreateCommand());
        entityLimitCommands.add(new net.alex9849.arm.entitylimit.commands.DeleteCommand());
        entityLimitCommands.add(new RemoveLimit());
        entityLimitCommands.add(new AddLimitCommand());
        entityLimitCommands.add(new net.alex9849.arm.entitylimit.commands.InfoCommand());
        entityLimitCommands.add(new ListCommand());
        entityLimitCommands.add(new CheckCommand());
        entityLimitCommands.add(new SetExtraLimitCommand());
        entityLimitCommands.add(new BuyExtraCommand());
        commands.add(new CommandSplitter("entitylimit", "(?i)entitylimit [^;\n]+", entityLimtUsage, Permission.ADMIN_ENTITYLIMIT_HELP, Messages.ENTITYLIMIT_HELP_HEADLINE, entityLimitCommands));

        List<String> regionKindUsage = new ArrayList<>(Arrays.asList("regionkind [SETTING]", "regionkind help"));
        List<BasicArmCommand> regionKindCommands = new ArrayList<>();
        regionKindCommands.add(new net.alex9849.arm.regionkind.commands.CreateCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.commands.DeleteCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.commands.ListCommand());
        regionKindCommands.add(new SetDisplayInGuiCommand());
        regionKindCommands.add(new SetDisplayInLimitsCommand());
        regionKindCommands.add(new SetItemCommand());
        regionKindCommands.add(new AddLoreLineCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.commands.InfoCommand());
        regionKindCommands.add(new RemoveLoreLineCommand());
        regionKindCommands.add(new SetDisplayNameCommand());
        regionKindCommands.add(new SetPaybackPercentage());
        commands.add(new CommandSplitter("regionkind", "(?i)regionkind [^;\n]+", regionKindUsage, Permission.REGIONKIND_HELP, Messages.REGIONKIND_HELP_HEADLINE, regionKindCommands));

        List<String> subRegionUsage = new ArrayList<>(Arrays.asList("subregion [SETTING]", "subregion help"));
        List<BasicArmCommand> subRegionCommands = new ArrayList<>();
        subRegionCommands.add(new ToolCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.CreateCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.HotelCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.TPCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.ResetBlocksCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.UnsellCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.DeleteCommand());
        commands.add(new CommandSplitter("subregion", "(?i)subregion [^;\n]+", subRegionUsage, Permission.SUBREGION_HELP, Messages.SUBREGION_HELP_HEADLINE, subRegionCommands));

        AdvancedRegionMarket.commandHandler.addCommands(commands);

        getCommand("arm").setTabCompleter(this.commandHandler);



        Bukkit.getLogger().log(Level.INFO, "Programmed by Alex9849");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                AdvancedRegionMarket.getRegionManager().updateFile();
                AdvancedRegionMarket.getEntityLimitGroupManager().updateFile();
                AdvancedRegionMarket.getRegionKindManager().updateFile();
            }
        }, 0, 60);
    }

    public void onDisable(){
        AdvancedRegionMarket.getPresetPatternManager().updateFile();
        AdvancedRegionMarket.getRegionManager().updateFile();
        AdvancedRegionMarket.getRegionKindManager().updateFile();
        AdvancedRegionMarket.getEntityLimitGroupManager().updateFile();
        AdvancedRegionMarket.econ = null;
        AdvancedRegionMarket.worldguard = null;
        AdvancedRegionMarket.worldedit = null;
        LimitGroup.Reset();
        AutoPrice.reset();
        SignLinkMode.reset();
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
        EntitySpawnEvent.getHandlerList().unregister(this);
        VehicleCreateEvent.getHandlerList().unregister(this);
        PlayerChatEvent.getHandlerList().unregister(this);
        getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }

    public static FlagGroupManager getFlagGroupManager() {
        return AdvancedRegionMarket.flagGroupManager;
    }

    public static PresetPatternManager getPresetPatternManager() {
        return AdvancedRegionMarket.presetPatternManager;
    }

    public static SignDataFactory getSignDataFactory() {
        return AdvancedRegionMarket.signDataFactory;
    }

    private static void setupSignDataFactory() {
        String classVersion = "";
        String serverVersion = Bukkit.getServer().getVersion();
        if(serverVersion.equalsIgnoreCase("1.12") || serverVersion.contains("1.12")) {
            classVersion = "112";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.12 sign adapter");
        } else if(serverVersion.equalsIgnoreCase("1.13") || serverVersion.contains("1.13")) {
            classVersion = "113";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.13 sign adapter");
        } else {
            classVersion = "114";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.14 sign adapter");
        }

        try {
            Class<?> signDataFactoryClass = Class.forName("net.alex9849.signs.SignDataFactory" + classVersion);
            if(SignDataFactory.class.isAssignableFrom(signDataFactoryClass)) {
                AdvancedRegionMarket.signDataFactory = (SignDataFactory) signDataFactoryClass.newInstance();
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not setup SignDataFactory! (Is your server compatible? Compatible versions: 1.12, 1.13, 1.14)");
        }

    }

    public static RegionKindManager getRegionKindManager() {
        return AdvancedRegionMarket.regionKindManager;
    }

    public static EntityLimitGroupManager getEntityLimitGroupManager() {
        return AdvancedRegionMarket.entityLimitGroupManager;
    }

    public static RegionManager getRegionManager() {
        return AdvancedRegionMarket.regionManager;
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
           /*
           if(isFaWeInstalled()) {
               version = "7FaWe";
           }
           */
        }
        try {
            final Class<?> wgClass = Class.forName("net.alex9849.adapters.WorldGuard" + version);
            if(WorldGuardInterface.class.isAssignableFrom(wgClass)) {
                AdvancedRegionMarket.worldGuardInterface = (WorldGuardInterface) wgClass.newInstance();
            }
            Bukkit.getLogger().log(Level.INFO, "Using WorldGuard" + version + " adapter");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "Could not setup WorldGuard! (handler could not be loaded) Compatible WorldGuard versions: 6, 7");
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
            hasFaWeHandler = false;
            if(AdvancedRegionMarket.worldedit.getDescription().getVersion().contains("beta-01") || ((parseWorldEditBuildNumber(worldedit) != null) && (parseWorldEditBuildNumber(worldedit) < 3930))){
                version = "7Beta01";
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
            Bukkit.getLogger().log(Level.INFO, "Using WorldEdit" + version + " adapter");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.INFO, "Could not setup WorldEdit! (handler could not be loaded) Compatible WorldEdit versions: 6, 7");
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
        Locale locale;
        String languageTag = getConfig().getString("PriceFormatting.locale");
        try {
            locale = Locale.forLanguageTag(languageTag);
        } catch (NullPointerException e) {
            locale = Locale.getDefault();
            Bukkit.getLogger().log(Level.WARNING, "Could not find language-Tag " + languageTag + "! Using " + locale + " now!");
        }
        NumberFormat priceFormatter = NumberFormat.getInstance(locale);
        priceFormatter.setMinimumFractionDigits(getConfig().getInt("PriceFormatting.minimumFractionDigits"));
        priceFormatter.setMaximumFractionDigits(getConfig().getInt("PriceFormatting.maximumFractionDigits"));
        priceFormatter.setMinimumIntegerDigits(getConfig().getInt("PriceFormatting.minimumIntegerDigits"));
        priceFormatter.setGroupingUsed(true);
        Price.setPriceFormater(priceFormatter);

        if(getConfig().getConfigurationSection("AutoPrice") != null) {
            AutoPrice.loadAutoprices(getConfig().getConfigurationSection("AutoPrice"));
        }
        if(getConfig().getConfigurationSection("DefaultAutoprice") != null) {
            AutoPrice.loadDefaultAutoPrice(getConfig().getConfigurationSection("DefaultAutoprice"));
        }
    }

    private void loadGUI(){
        FileConfiguration pluginConf = getConfig();
        Gui.setRegionOwnerItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.RegionOwnerItem")));
        Gui.setRegionMemberItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.RegionMemberItem")));
        Gui.setRegionFinderItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.RegionFinderItem")));
        Gui.setGoBackItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.GoBackItem")));
        Gui.setWarningYesItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.WarningYesItem")));
        Gui.setWarningNoItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.WarningNoItem")));
        Gui.setTpItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.TPItem")));
        Gui.setSellRegionItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.SellRegionItem")));
        Gui.setResetItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.ResetItem")));
        Gui.setExtendItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.ExtendItem")));
        Gui.setInfoItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.InfoItem")));
        Gui.setPromoteMemberToOwnerItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.PromoteMemberToOwnerItem")));
        Gui.setRemoveMemberItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.RemoveMemberItem")));
        Gui.setFillItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FillItem")));
        Gui.setContractItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.ContractItem")));
        Gui.setSubregionItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.SubRegionItem")));
        Gui.setDeleteItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.DeleteItem")));
        Gui.setTeleportToSignItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.TeleportToSignItem")));
        Gui.setTeleportToRegionItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.TeleportToRegionItem")));
        Gui.setNextPageItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.NextPageItem")));
        Gui.setPrevPageItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.PrevPageItem")));
        Gui.setHotelSettingItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.HotelSettingItem")));
        Gui.setUnsellItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.UnsellItem")));
        Gui.setFlageditorItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlageditorItem")));;
        Gui.setFlagItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagItem")));
        Gui.setFlagSettingSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsSelectedItem")));
        Gui.setFlagSettingNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsNotSelectedItem")));
        Gui.setFlagGroupSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupSelectedItem")));
        Gui.setFlagGroupNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupNotSelectedItem")));
        Gui.setFlagRemoveItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagRemoveItem")));
        Gui.setFlagUserInputItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagUserInputItem")));
        Gui.setFlageditorResetItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlageditorResetItem")));
    }

    private void loadAutoReset() {
        ArmSettings.setEnableAutoReset(getConfig().getBoolean("AutoResetAndTakeOver.enableAutoReset"));
        ArmSettings.setEnableTakeOver(getConfig().getBoolean("AutoResetAndTakeOver.enableTakeOver"));
    }

    public static void reconnectSQL() {
        Bukkit.getServer().getLogger().log(Level.WARNING, "SQL connection lost. Reconnecting...");
        AdvancedRegionMarket arm = AdvancedRegionMarket.getARM();
        if(arm != null) {
            if(!arm.connectSQL()) {
                Bukkit.getLogger().log(Level.INFO, "SQL Login failed!");
            }
        }
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
        ArmSettings.setIsRegionInfoParticleBorder(getConfig().getBoolean("Other.RegionInfoParticleBorder"));
        ArmSettings.setIsAllowTeleportToBuySign(getConfig().getBoolean("Other.AllowRegionfinderTeleportToBuySign"));
        ArmSettings.setRemoveEntitiesOnRegionBlockReset(getConfig().getBoolean("Other.RemoveEntitiesOnRegionBlockReset"));

        ArmSettings.setIsAllowSubRegionUserReset(getConfig().getBoolean("Subregions.AllowSubRegionUserReset"));
        ArmSettings.setIsSubregionBlockReset(getConfig().getBoolean("Subregions.SubregionBlockReset"));
        ArmSettings.setIsSubregionAutoReset(getConfig().getBoolean("Subregions.SubregionAutoReset"));
        ArmSettings.setDeleteSubregionsOnParentRegionBlockReset(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionBlockReset"));
        ArmSettings.setDeleteSubregionsOnParentRegionUnsell(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionUnsell"));
        ArmSettings.setAllowParentRegionOwnersBuildOnSubregions(getConfig().getBoolean("Subregions.allowParentRegionOwnersBuildOnSubregions"));
        ArmSettings.setSignRightClickSneakCommand(getConfig().getString("SignClickActions.RightClickSneakCmd"));
        ArmSettings.setSignRightClickNotSneakCommand(getConfig().getString("SignClickActions.RightClickNotSneakCmd"));
        ArmSettings.setSignLeftClickSneakCommand(getConfig().getString("SignClickActions.LeftClickSneakCmd"));
        ArmSettings.setSignLeftClickNotSneakCommand(getConfig().getString("SignClickActions.LeftClickNotSneakCmd"));
        ArmSettings.setActivateRegionKindPermissions(getConfig().getBoolean("RegionKinds.activateRegionKindPermissions"));


        try{
            RentRegion.setExpirationWarningTime(RentPrice.stringToTime(getConfig().getString("Other.RentRegionExpirationWarningTime")));
            RentRegion.setSendExpirationWarning(getConfig().getBoolean("Other.SendRentRegionExpirationWarning"));
        } catch (IllegalArgumentException | NullPointerException e) {
            Bukkit.getLogger().log(Level.INFO, "Warning! Bad syntax of time format \"RentRegionExpirationWarningTime\" disabling it...");
            RentRegion.setExpirationWarningTime(0);
            RentRegion.setSendExpirationWarning(false);
        }
    }

    private void loadSignLinkingModeRegions() {
        ConfigurationSection slmSection = getConfig().getConfigurationSection("SignLinkingMode");
        if(slmSection == null) {
            return;
        }
        ConfigurationSection blacklistRegionSection = slmSection.getConfigurationSection("regionblacklist");
        if(blacklistRegionSection == null) {
            return;
        }
        Set<String> worlds = blacklistRegionSection.getKeys(false);
        if(worlds == null) {
            return;
        }
        Set<WGRegion> wgRegions = new HashSet<>();
        for(String worldName : worlds) {
            World world = Bukkit.getWorld(worldName);
            if(world == null) {
                continue;
            }
            List<String> regionNames = blacklistRegionSection.getStringList(worldName);
            if(regionNames == null) {
                continue;
            }

            for(String regionName : regionNames) {
                WGRegion wgRegion = AdvancedRegionMarket.getWorldGuardInterface().getRegion(world, AdvancedRegionMarket.getWorldGuard(), regionName);
                if(wgRegion == null) {
                    continue;
                }
                wgRegions.add(wgRegion);
            }
        }
        SignLinkMode.setBlacklistedRegions(wgRegions);
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
        if (!cmd.getName().equalsIgnoreCase("arm")) {
            return true;
        }
        try {
            if(args.length >= 1) {
                return this.commandHandler.executeCommand(sender, cmd, commandsLabel, args);
            } else {
                String pluginversion = this.getDescription().getVersion();
                sender.sendMessage(Messages.ARM_BASIC_COMMAND_MESSAGE.replace("%pluginversion%", pluginversion));
                return true;
            }
        } catch (InputException inputException) {
            inputException.sendMessages(Messages.PREFIX);
            return true;
        } catch (CmdSyntaxException cmdSyntaxException) {
            List<String> syntax = cmdSyntaxException.getSyntax();
            if(syntax.size() >= 1) {
                String message = Messages.BAD_SYNTAX;

                message = message.replace("%command%", "/" + commandsLabel + " " + syntax.get(0));

                for(int x = 1; x < syntax.size(); x++) {
                    message = message + " " + Messages.BAD_SYNTAX_SPLITTER.replace("%command%", "/" + commandsLabel + " " + syntax.get(x));
                }
                sender.sendMessage(Messages.PREFIX + message);
            }
            return true;
        }
    }

    private static void sendStats(Plugin plugin, boolean isPing){
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
            URL url;

            if(isPing) {
                url = new URL("https://mcplug.alex9849.net/mcplug3.php?startup=1");
            } else {
                url = new URL("https://mcplug.alex9849.net/mcplug3.php?startup=0");
            }

            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setInstanceFollowRedirects(true);
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);
            con.addRequestProperty("User-Agent", userAgent);
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());

            ps.print("plugin=arm");
            ps.print("&host=" + hoststring);
            ps.print("&ip=" + ip);
            ps.print("&port=" + port);
            ps.print("&playercount=" + Bukkit.getOnlinePlayers().size());
            ps.print("&pversion=" + plugin.getDescription().getVersion());

            con.connect();
            con.getInputStream();
            ps.close();
            con.disconnect();

        } catch (Throwable e) {
            return;
        }
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
        EntityLimitGroupManager.writeResourceToDisc(new File(this.getDataFolder() + "/entitylimits.yml"), getResource("entitylimits.yml"));
        RegionKindManager.writeResourceToDisc(new File(this.getDataFolder() + "/regionkinds.yml"), getResource("regionkinds.yml"));
        RegionManager.writeResourceToDisc(new File(this.getDataFolder() + "/regions.yml"), getResource("regions.yml"));
        PresetPatternManager.writeResourceToDisc(new File(this.getDataFolder() + "/presets.yml"), getResource("presets.yml"));
        FlagGroupManager.writeResourceToDisc(new File(this.getDataFolder() + "/flaggroups.yml"), getResource("flaggroups.yml"));
        Messages.generatedefaultConfig();
        this.generatedefaultconfig();
        FileConfiguration pluginConfig = this.getConfig();
        Double version = pluginConfig.getDouble("Version");
        try {
            if(version < 1.1) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.1...");
                updateTo1p1(pluginConfig);
            }
            if(version < 1.2) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.2...");
                getLogger().log(Level.WARNING, "Warning!: ARM uses a new schematic format now! You have to update all region schematics with");
                getLogger().log(Level.WARNING, "/arm updateschematic [REGION] or go back to ARM version 1.1");
                updateTo1p2(pluginConfig);
            }
            if(version < 1.21) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.21...");
                updateTo1p3(pluginConfig);
            }
            if(version < 1.3) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.3...");
                updateTo1p3(pluginConfig);
            }
            if(version < 1.4) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4...");
                updateTo1p4(pluginConfig);
            }
            if(version < 1.41) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.1...");
                pluginConfig.set("Version", 1.41);
                saveConfig();
            }
            if(version < 1.44) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.4...");
                updateTo1p44(pluginConfig);
            }
            if(version < 1.5) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5...");
                pluginConfig.set("Version", 1.5);
                pluginConfig.set("Reselling.Offers.OfferTimeOut", 30);
                saveConfig();
            }
            if(version < 1.52) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5.2...");
                updateTo1p52(pluginConfig);
            }
            if(version < 1.6) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.6...");
                updateTo1p6(pluginConfig);
            }
            if(version < 1.7) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7...");
                pluginConfig.set("Other.RemoveEntitiesOnRegionBlockReset", true);
                pluginConfig.set("Version", 1.7);
                saveConfig();
            }
            if(version < 1.72) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.2...");
                pluginConfig.set("SignClickActions.RightClickNotSneakCmd", "buyaction");
                pluginConfig.set("SignClickActions.RightClickSneakCmd", "arm sellback %regionid%");
                pluginConfig.set("SignClickActions.LeftClickNotSneakCmd", "arm info %regionid%");
                pluginConfig.set("SignClickActions.LeftClickSneakCmd", "arm info %regionid%");
                pluginConfig.set("Version", 1.72);
                saveConfig();
            }
            if(version < 1.75) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.5...");
                updateTo1p75(pluginConfig);
            }
            if(version < 1.8) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8...");
                updateTo1p8(pluginConfig);
            }

            if(version < 1.81) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.1..");
                updateTo1p81(pluginConfig);
            }
            if(version < 1.83) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.3..");
                updateTo1p83(pluginConfig);
            }
            if(version < 1.9) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9..");
                updateTo1p9(pluginConfig);
            }
            if(version < 1.92) {
                updateTo1p92(pluginConfig);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTo1p1(FileConfiguration pluginConfig) {
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

    private void updateTo1p2(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.2);
        saveConfig();

        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        ArrayList<String> worlds = new ArrayList<String>(regionConf.getConfigurationSection("Regions").getKeys(false));
        if(worlds != null) {
            for(int y = 0; y < worlds.size(); y++) {
                ArrayList<String> regions = new ArrayList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                if(regions != null) {
                    for (int i = 0; i < regions.size(); i++) {
                        regionConf.set("Regions." + worlds.get(y) + "." + regions.get(i) + ".doBlockReset", true);
                    }
                }
            }
        }
        regionConf.save(regionConfDic);
    }

    private void updateTo1p21(FileConfiguration pluginConfig) {
        pluginConfig.set("Version", 1.21);
        saveConfig();
    }

    private void updateTo1p3(FileConfiguration pluginConfig) throws IOException {

        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        pluginConfig.set("DefaultRegionKind.DisplayName", "Default");
        pluginConfig.set("DefaultRegionKind.Item", "RED_BED");
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
            List<String> worlds = new ArrayList<>(regionConf.getConfigurationSection("Regions").getKeys(false));
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
        regionConf.save(regionConfDic);
    }

    private void updateTo1p4(FileConfiguration pluginConfig) {
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

    private void updateTo1p44(FileConfiguration pluginConfig) {
        pluginConfig.set("Other.TeleporterTimer", 0);
        pluginConfig.set("Other.TeleportAfterRegionBoughtCountdown", false);
        pluginConfig.set("Version", 1.44);
        saveConfig();
    }

    private void updateTo1p52(FileConfiguration pluginConfig) {
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

    private void updateTo1p6(FileConfiguration pluginConfig) {
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
        pluginConfig.set("AutoPrice.example1.extendTime", "5d");
        pluginConfig.set("AutoPrice.example1.maxRentTime", "60d");
        pluginConfig.set("AutoPrice.example1.autoPriceCalculation", "static");
        pluginConfig.set("AutoPrice.example2.price", 2);
        pluginConfig.set("AutoPrice.example2.extendTime", "12h");
        pluginConfig.set("AutoPrice.example2.maxRentTime", "7d");
        pluginConfig.set("AutoPrice.example2.autoPriceCalculation", "per_m2");
        pluginConfig.set("AutoPrice.example3.price", 0.05);
        pluginConfig.set("AutoPrice.example3.extendTime", "7d");
        pluginConfig.set("AutoPrice.example3.maxRentTime", "30d");
        pluginConfig.set("AutoPrice.example3.autoPriceCalculation", "per_m3");

        pluginConfig.set("Other.RegionInfoParticleBorder", true);
        pluginConfig.set("Other.AllowRegionfinderTeleportToBuySign", true);

        pluginConfig.set("DefaultAutoprice.price", 2.0);
        pluginConfig.set("DefaultAutoprice.extendTime", "1d");
        pluginConfig.set("DefaultAutoprice.maxRentTime", "7d");
        pluginConfig.set("DefaultAutoprice.autoPriceCalculation", "per_m2");

        pluginConfig.set("Version", 1.6);
        saveConfig();
    }

    private void updateTo1p75(FileConfiguration pluginConfig) throws IOException {
        File regionKindsConfDic = new File(this.getDataFolder() + "/regionkinds.yml");
        YamlConfiguration regionKindsConf = YamlConfiguration.loadConfiguration(regionKindsConfDic);
        ConfigurationSection oldRegionKinds = pluginConfig.getConfigurationSection("RegionKinds");

        regionKindsConf.set("RegionKinds", oldRegionKinds);
        pluginConfig.set("RegionKinds", null);

        regionKindsConf.set("DefaultRegionKind.displayName", pluginConfig.getString("DefaultRegionKind.DisplayName"));
        regionKindsConf.set("DefaultRegionKind.item", pluginConfig.getString("DefaultRegionKind.Item"));
        regionKindsConf.set("DefaultRegionKind.lore", pluginConfig.getStringList("DefaultRegionKind.Lore"));
        regionKindsConf.set("DefaultRegionKind.displayInLimits", pluginConfig.getBoolean("DefaultRegionKind.DisplayInLimits"));
        regionKindsConf.set("DefaultRegionKind.displayInGUI", pluginConfig.getBoolean("DefaultRegionKind.DisplayInGUI"));
        regionKindsConf.set("DefaultRegionKind.paypackPercentage", pluginConfig.getDouble("DefaultRegionKind.PaypackPercentage"));
        pluginConfig.set("DefaultRegionKind", null);

        regionKindsConf.set("SubregionRegionKind.displayName", pluginConfig.getString("SubregionRegionKind.DisplayName"));
        regionKindsConf.set("SubregionRegionKind.item", pluginConfig.getString("SubregionRegionKind.Item"));
        regionKindsConf.set("SubregionRegionKind.lore", pluginConfig.getStringList("SubregionRegionKind.Lore"));
        regionKindsConf.set("SubregionRegionKind.displayInLimits", pluginConfig.getBoolean("SubregionRegionKind.DisplayInLimits"));
        regionKindsConf.set("SubregionRegionKind.displayInGUI", pluginConfig.getBoolean("SubregionRegionKind.DisplayInGUI"));
        regionKindsConf.set("SubregionRegionKind.paypackPercentage", pluginConfig.getDouble("SubregionRegionKind.PaypackPercentage"));
        pluginConfig.set("SubregionRegionKind", null);

        pluginConfig.set("RegionKinds.activateRegionKindPermissions", true);
        pluginConfig.set("PriceFormatting.locale", "US");
        pluginConfig.set("PriceFormatting.minimumFractionDigits", 2);
        pluginConfig.set("PriceFormatting.maximumFractionDigits", 2);
        pluginConfig.set("PriceFormatting.minimumIntegerDigits", 1);

        pluginConfig.set("Version", 1.75);

        regionKindsConf.save(regionKindsConfDic);
        saveConfig();
    }

    private void updateTo1p8(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if(regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if(worlds != null) {
                for(String worldString : worlds) {
                    if(mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if(regions != null) {
                            for(String regionname : regions){
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for(int i = 0; i < regionsignsloc.size(); i++) {
                                    regionsignsloc.set(i, regionsignsloc.get(i) + ";NORTH;GROUND");
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList("signs");
                                            for(int i = 0; i < subregionsignsloc.size(); i++) {
                                                subregionsignsloc.set(i, subregionsignsloc.get(i) + ";NORTH;GROUND");
                                            }
                                            subregionsection.set("signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(this.getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.8);
        saveConfig();
    }

    private void updateTo1p81(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if(regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if(worlds != null) {
                for(String worldString : worlds) {
                    if(mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if(regions != null) {
                            for(String regionname : regions){
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for(int i = 0; i < regionsignsloc.size(); i++) {
                                    String signString = regionsignsloc.get(i);
                                    signString = signString.replace(";NORTH", "");
                                    signString = signString.replace(";EAST", "");
                                    signString = signString.replace(";SOUTH", "");
                                    signString = signString.replace(";WEST", "");
                                    signString = signString.replace(";UP", "");
                                    signString = signString.replace(";DOWN", "");
                                    signString = signString.replace(";NORTH_EAST", "");
                                    signString = signString.replace(";NORTH_WEST", "");
                                    signString = signString.replace(";SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_WEST", "");
                                    signString = signString.replace(";WEST_NORTH_WEST", "");
                                    signString = signString.replace(";NORTH_NORTH_WEST", "");
                                    signString = signString.replace(";NORTH_NORTH_EAST", "");
                                    signString = signString.replace(";EAST_NORTH_EAST", "");
                                    signString = signString.replace(";EAST_SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_SOUTH_EAST", "");
                                    signString = signString.replace(";SOUTH_SOUTH_WEST", "");
                                    signString = signString.replace(";WEST_SOUTH_WEST", "");
                                    signString = signString.replace(";SELF", "");
                                    regionsignsloc.set(i, signString);
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList("signs");
                                            for(int i = 0; i < subregionsignsloc.size(); i++) {
                                                String signString = subregionsignsloc.get(i);
                                                signString = signString.replace(";NORTH", "");
                                                signString = signString.replace(";EAST", "");
                                                signString = signString.replace(";SOUTH", "");
                                                signString = signString.replace(";WEST", "");
                                                signString = signString.replace(";UP", "");
                                                signString = signString.replace(";DOWN", "");
                                                signString = signString.replace(";NORTH_EAST", "");
                                                signString = signString.replace(";NORTH_WEST", "");
                                                signString = signString.replace(";SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_WEST", "");
                                                signString = signString.replace(";WEST_NORTH_WEST", "");
                                                signString = signString.replace(";NORTH_NORTH_WEST", "");
                                                signString = signString.replace(";NORTH_NORTH_EAST", "");
                                                signString = signString.replace(";EAST_NORTH_EAST", "");
                                                signString = signString.replace(";EAST_SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_SOUTH_EAST", "");
                                                signString = signString.replace(";SOUTH_SOUTH_WEST", "");
                                                signString = signString.replace(";WEST_SOUTH_WEST", "");
                                                signString = signString.replace(";SELF", "");
                                                subregionsignsloc.set(i, signString);
                                            }
                                            subregionsection.set("signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(this.getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.81);
        saveConfig();
    }

    private void updateTo1p83(FileConfiguration pluginConfig) throws IOException {
        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        if(regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if(worlds != null) {
                for(String worldString : worlds) {
                    if(mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if(regions != null) {
                            for(String regionname : regions){
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for(int i = 0; i < regionsignsloc.size(); i++) {
                                    regionsignsloc.set(i, regionsignsloc.get(i) + ";NORTH");
                                }
                                regionSection.set("signs", regionsignsloc);

                                List<Region> subregions = new ArrayList<>();
                                if (regionSection.getConfigurationSection("subregions") != null) {
                                    ConfigurationSection subregionsection = regionSection.getConfigurationSection("subregions");
                                    List<String> subregionIDS = new ArrayList<>((subregionsection).getKeys(false));
                                    if (subregionIDS != null) {
                                        for (String subregionName : subregionIDS) {
                                            List<String> subregionsignsloc = subregionsection.getStringList("signs");
                                            for(int i = 0; i < subregionsignsloc.size(); i++) {
                                                subregionsignsloc.set(i, subregionsignsloc.get(i) + ";NORTH");
                                            }
                                            subregionsection.set("signs", subregionsignsloc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        regionConf.save(this.getDataFolder() + "/regions.yml");

        pluginConfig.set("Version", 1.83);
        saveConfig();
    }

    private void updateTo1p9(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("GUI.FlageditorItem", "BANNER");
        pluginConfig.set("GUI.FlagItem", "SIGN");
        pluginConfig.set("GUI.FlagSettingsSelectedItem", "EMERALD_BLOCK");
        pluginConfig.set("GUI.FlagSettingsNotSelectedItem", "REDSTONE_BLOCK");
        pluginConfig.set("GUI.FlagGroupSelectedItem", "EMERALD_BLOCK");
        pluginConfig.set("GUI.FlagGroupNotSelectedItem", "REDSTONE_BLOCK");
        pluginConfig.set("GUI.FlagRemoveItem", "BARRIER");
        pluginConfig.set("GUI.FlagUserInputItem", "WRITABLE_BOOK");
        pluginConfig.set("GUI.FlageditorResetItem", "TNT");

        pluginConfig.set("SignLinkingMode.regionblacklist.world", Arrays.asList("blacklistedTestRegionInWorld1", "blacklistedTestRegionInWorld2"));
        pluginConfig.set("SignLinkingMode.regionblacklist.world_nether", Arrays.asList("blacklistedTestRegionInWorld_nether1", "blacklistedTestRegionInWorld_nether2"));
        pluginConfig.set("SignLinkingMode.regionblacklist.world_the_end", Arrays.asList("anotherBlacklistedRegion"));

        pluginConfig.set("Version", 1.9);
        saveConfig();
    }

    private void updateTo1p92(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Other.Sendstats", true);
        pluginConfig.set("Version", 1.92);
        saveConfig();
    }
}

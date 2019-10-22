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
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.flaggroups.FlagGroupManager;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.handler.listener.*;
import net.alex9849.arm.inactivityexpiration.InactivityExpirationGroup;
import net.alex9849.arm.inactivityexpiration.PlayerInactivityGroupMapper;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPatternManager;
import net.alex9849.arm.regionkind.RegionKindManager;
import net.alex9849.arm.regionkind.commands.*;
import net.alex9849.arm.regions.CountdownRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.subregions.commands.ToolCommand;
import net.alex9849.arm.util.MaterialFinder;
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
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;

public class AdvancedRegionMarket extends JavaPlugin {
    private Boolean faWeInstalled = null;
    private Economy econ = null;
    private net.milkbowl.vault.permission.Permission vaultPerms = null;
    private WorldGuardPlugin worldguard = null;
    private WorldGuardInterface worldGuardInterface = null;
    private WorldEditPlugin worldedit = null;
    private WorldEditInterface worldEditInterface = null;
    private CommandHandler commandHandler = null;
    private RegionKindManager regionKindManager = null;
    private EntityLimitGroupManager entityLimitGroupManager = null;
    private RegionManager regionManager = null;
    private PresetPatternManager presetPatternManager = null;
    private SignDataFactory signDataFactory = null;
    private FlagGroupManager flagGroupManager = null;
    private ArmSettings pluginSettings = null;

    public static AdvancedRegionMarket getInstance() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket");
        if (plugin instanceof AdvancedRegionMarket) {
            return (AdvancedRegionMarket) plugin;
        } else {
            return null;
        }
    }

    private static void sendStats(Plugin plugin, boolean isPing, int playerCount) {
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
            String str = null;
            String str1 = null;
            URL url;

            if (isPing) {
                url = new URL("https://mcplug.alex9849.net/mcplug3.php?startup=1");
            } else {
                url = new URL("https://mcplug.alex9849.net/mcplug3.php?startup=0");
            }

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
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
            ps.print("&playercount=" + playerCount);
            ps.print("&pversion=" + plugin.getDescription().getVersion());

            con.connect();
            con.getInputStream();
            ps.close();
            con.disconnect();

        } catch (Throwable e) {
            return;
        }
    }

    public void onEnable() {

        //Enable bStats
        BStatsAnalytics bStatsAnalytics = new BStatsAnalytics();
        bStatsAnalytics.register(this);

        this.faWeInstalled = setupFaWe();

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
            getLogger().log(Level.INFO, "Please install Vault and an economy Plugin!");
        }

        setupPermissions();

        setupSignDataFactory();

        File schematicdic = new File(getDataFolder() + "/schematics");
        if (!schematicdic.exists()) {
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

        if (getConfig().getBoolean("Other.Sendstats")) {
            final int playercount = Bukkit.getOnlinePlayers().size();
            Thread sendStartup = new Thread(() -> {
                AdvancedRegionMarket.sendStats(this, false, playercount);
            });
            sendStartup.start();

            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                Plugin armPlugin = AdvancedRegionMarket.getInstance();
                final int onlineplayers = Bukkit.getOnlinePlayers().size();
                Thread sendPing = new Thread(() -> {
                    AdvancedRegionMarket.sendStats(armPlugin, true, onlineplayers);
                });
                sendPing.start();
            }, 6000, 6000);
        }


        this.pluginSettings = new ArmSettings();
        this.pluginSettings.setIsTeleportAfterRentRegionBought(getConfig().getBoolean("Other.TeleportAfterRentRegionBought"));
        this.pluginSettings.setIsTeleportAfterRentRegionExtend(getConfig().getBoolean("Other.TeleportAfterRentRegionExtend"));
        this.pluginSettings.setIsTeleportAfterSellRegionBought(getConfig().getBoolean("Other.TeleportAfterSellRegionBought"));
        this.pluginSettings.setIsTeleportAfterContractRegionBought(getConfig().getBoolean("Other.TeleportAfterContractRegionBought"));
        this.pluginSettings.setIsSendContractRegionExtendMessage(getConfig().getBoolean("Other.SendContractRegionExtendMessage"));
        this.pluginSettings.setDateTimeformat(getConfig().getString("Other.DateTimeFormat"));
        this.pluginSettings.setIsRegionInfoParticleBorder(getConfig().getBoolean("Other.RegionInfoParticleBorder"));
        this.pluginSettings.setIsAllowTeleportToBuySign(getConfig().getBoolean("Other.AllowRegionfinderTeleportToBuySign"));
        this.pluginSettings.setRemoveEntitiesOnRegionBlockReset(getConfig().getBoolean("Other.RemoveEntitiesOnRegionBlockReset"));
        this.pluginSettings.setIsAllowSubRegionUserReset(getConfig().getBoolean("Subregions.AllowSubRegionUserReset"));
        this.pluginSettings.setIsSubregionBlockReset(getConfig().getBoolean("Subregions.SubregionBlockReset"));
        this.pluginSettings.setIsSubregionInactivityReset(getConfig().getBoolean("Subregions.SubregionInactivityReset"));
        this.pluginSettings.setDeleteSubregionsOnParentRegionBlockReset(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionBlockReset"));
        this.pluginSettings.setDeleteSubregionsOnParentRegionUnsell(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionUnsell"));
        this.pluginSettings.setAllowParentRegionOwnersBuildOnSubregions(getConfig().getBoolean("Subregions.allowParentRegionOwnersBuildOnSubregions"));
        this.pluginSettings.setSignRightClickSneakCommand(getConfig().getString("SignClickActions.RightClickSneakCmd"));
        this.pluginSettings.setSignRightClickNotSneakCommand(getConfig().getString("SignClickActions.RightClickNotSneakCmd"));
        this.pluginSettings.setSignLeftClickSneakCommand(getConfig().getString("SignClickActions.LeftClickSneakCmd"));
        this.pluginSettings.setSignLeftClickNotSneakCommand(getConfig().getString("SignClickActions.LeftClickNotSneakCmd"));
        this.pluginSettings.setActivateRegionKindPermissions(getConfig().getBoolean("RegionKinds.activateRegionKindPermissions"));
        try {
            this.pluginSettings.setUserResetCooldown(CountdownRegion.stringToTime(getConfig().getString("Other.userResetCooldown")));
        } catch (IllegalArgumentException e) {
            this.pluginSettings.setUserResetCooldown(604800000);
            getLogger().log(Level.WARNING, "Could not parse 'Other.userResetCooldown' using 7d for now!");
        }


        this.regionKindManager = new RegionKindManager(new File(this.getDataFolder() + "/regionkinds.yml"));
        this.entityLimitGroupManager = new EntityLimitGroupManager(new File(this.getDataFolder() + "/entitylimits.yml"));
        loadAutoPrice();
        loadGroups();
        loadGUI();
        this.flagGroupManager = new FlagGroupManager(new File(this.getDataFolder() + "/flaggroups.yml"));
        this.regionManager = new RegionManager(new File(this.getDataFolder() + "/regions.yml"), 20 * getConfig().getInt("Other.SignAndResetUpdateInterval"));
        getLogger().log(Level.INFO, "Regions loaded!");

        loadSignLinkingModeRegions();
        loadInactivityExpirationGroups();
        loadOther();
        this.presetPatternManager = new PresetPatternManager(new File(this.getDataFolder() + "/presets.yml"));
        Region.setCompleteTabRegions(getConfig().getBoolean("Other.CompleteRegionsOnTabComplete"));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            this.getRegionManager().doTick();
        }, 1, 1);
        this.commandHandler = new CommandHandler(new ArrayList<>(Arrays.asList("help")), "");
        List<BasicArmCommand> commands = new ArrayList<>();
        String[] betweencmds = {};
        commands.add(new AddMemberCommand());
        commands.add(new InactivityResetCommand());
        commands.add(new ContractPresetCommand());
        commands.add(new DeleteCommand());
        commands.add(new DoBlockResetCommand());
        commands.add(new ExtendCommand());
        commands.add(new RegionfinderCommand());
        commands.add(new GuiCommand());
        commands.add(new HelpCommand(this.commandHandler, Messages.HELP_HEADLINE, betweencmds, Permission.ARM_HELP));
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
        regionKindCommands.add(new SetDisplayInRegionfinderCommand());
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

        this.commandHandler.addCommands(commands);

        getCommand("arm").setTabCompleter(this.commandHandler);


        Bukkit.getLogger().log(Level.INFO, "Programmed by Alex9849");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                AdvancedRegionMarket.getInstance().getRegionManager().updateFile();
                AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().updateFile();
                AdvancedRegionMarket.getInstance().getRegionKindManager().updateFile();
                AdvancedRegionMarket.getInstance().getFlagGroupManager().updateFile();
            }
        }, 0, 60);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                PlayerInactivityGroupMapper.updateMapAscync();
            }
        }, 900, 6000);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Region region : AdvancedRegionMarket.getInstance().getRegionManager()) {
                    if (region.isInactivityResetEnabled() && region.isInactive()) {
                        //TODO logToConsole
                        try {
                            region.automaticResetRegion(Region.ActionReason.INACTIVITY, true);
                        } catch (SchematicNotFoundException e) {
                            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.getConvertedMessage(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                        }
                    }
                }
            }
        }, 1800, 6000);
    }

    public void onDisable() {
        this.getPresetPatternManager().updateFile();
        this.getRegionManager().updateFile();
        this.getRegionKindManager().updateFile();
        this.getEntityLimitGroupManager().updateFile();
        this.getFlagGroupManager().updateFile();
        this.econ = null;
        this.vaultPerms = null;
        this.worldguard = null;
        this.worldedit = null;
        LimitGroup.Reset();
        InactivityExpirationGroup.reset();
        AutoPrice.reset();
        SignLinkMode.reset();
        ActivePresetManager.reset();
        Offer.reset();
        PlayerInactivityGroupMapper.reset();
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

    public ArmSettings getPluginSettings() {
        return this.pluginSettings;
    }

    public FlagGroupManager getFlagGroupManager() {
        return this.flagGroupManager;
    }

    public PresetPatternManager getPresetPatternManager() {
        return this.presetPatternManager;
    }

    public SignDataFactory getSignDataFactory() {
        return this.signDataFactory;
    }

    private void setupSignDataFactory() {
        String classVersion = "";
        String serverVersion = Bukkit.getServer().getVersion();
        if (serverVersion.equalsIgnoreCase("1.12") || serverVersion.contains("1.12")) {
            classVersion = "112";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.12 sign adapter");
        } else if (serverVersion.equalsIgnoreCase("1.13") || serverVersion.contains("1.13")) {
            classVersion = "113";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.13 sign adapter");
        } else {
            classVersion = "114";
            Bukkit.getLogger().log(Level.INFO, "Using MC 1.14 sign adapter");
        }

        try {
            Class<?> signDataFactoryClass = Class.forName("net.alex9849.signs.SignDataFactory" + classVersion);
            if (SignDataFactory.class.isAssignableFrom(signDataFactoryClass)) {
                this.signDataFactory = (SignDataFactory) signDataFactoryClass.newInstance();
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not setup SignDataFactory! (Is your server compatible? Compatible versions: 1.12, 1.13, 1.14)");
        }

    }

    public RegionKindManager getRegionKindManager() {
        return this.regionKindManager;
    }

    public EntityLimitGroupManager getEntityLimitGroupManager() {
        return this.entityLimitGroupManager;
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.econ = rsp.getProvider();
        return this.econ != null;
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        this.vaultPerms = rsp.getProvider();
        return this.vaultPerms != null;
    }

    private boolean setupFaWe() {
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
        this.worldguard = (WorldGuardPlugin) plugin;
        String version = "notSupported";
        if (this.worldguard.getDescription().getVersion().startsWith("6.1")) {
            version = "6_1";
        } else if (this.worldguard.getDescription().getVersion().startsWith("6.2")) {
            version = "6_2";
        } else {

            version = "7";

            if ((parseWorldGuardBuildNumber(worldguard) != null) && (parseWorldGuardBuildNumber(worldguard) < 1754)) {
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
            if (WorldGuardInterface.class.isAssignableFrom(wgClass)) {
                this.worldGuardInterface = (WorldGuardInterface) wgClass.newInstance();
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
        this.worldedit = (WorldEditPlugin) plugin;
        String version = "notSupported";
        Boolean hasFaWeHandler = true;

        if (this.worldedit.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
            hasFaWeHandler = false;
            if (this.worldedit.getDescription().getVersion().contains("beta-01") || ((parseWorldEditBuildNumber(worldedit) != null) && (parseWorldEditBuildNumber(worldedit) < 3930))) {
                version = "7Beta01";
            }

        }

        if (this.isFaWeInstalled() && hasFaWeHandler) {
            version = version + "FaWe";
        }

        try {
            final Class<?> weClass = Class.forName("net.alex9849.adapters.WorldEdit" + version);
            if (WorldEditInterface.class.isAssignableFrom(weClass)) {
                this.worldEditInterface = (WorldEditInterface) weClass.newInstance();
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
        if (!version.contains("-SNAPSHOT;")) {
            return null;
        }

        String buildNumberString = version.substring(version.indexOf("-SNAPSHOT;") + 10);

        if (buildNumberString.contains("-")) {
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
        if (!version.contains("-SNAPSHOT;")) {
            return null;
        }

        String buildNumberString = version.substring(version.indexOf("-SNAPSHOT;") + 10);

        if (buildNumberString.contains("-")) {
            buildNumberString = buildNumberString.substring(0, buildNumberString.indexOf("-"));
        }

        try {
            return Integer.parseInt(buildNumberString);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public WorldGuardPlugin getWorldGuard() {
        return this.worldguard;
    }

    public WorldGuardInterface getWorldGuardInterface() {
        return this.worldGuardInterface;
    }

    public WorldEditInterface getWorldEditInterface() {
        return this.worldEditInterface;
    }

    private Boolean isFaWeInstalled() {
        return this.faWeInstalled;
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

        if (getConfig().getConfigurationSection("AutoPrice") != null) {
            AutoPrice.loadAutoprices(getConfig().getConfigurationSection("AutoPrice"));
        }
        if (getConfig().getConfigurationSection("DefaultAutoprice") != null) {
            AutoPrice.loadDefaultAutoPrice(getConfig().getConfigurationSection("DefaultAutoprice"));
        }
    }

    private void loadGUI() {
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
        Gui.setFlageditorItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlageditorItem")));
        ;
        Gui.setFlagItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagItem")));
        Gui.setFlagSettingSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsSelectedItem")));
        Gui.setFlagSettingNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsNotSelectedItem")));
        Gui.setFlagGroupSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupSelectedItem")));
        Gui.setFlagGroupNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupNotSelectedItem")));
        Gui.setFlagRemoveItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagRemoveItem")));
        Gui.setFlagUserInputItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagUserInputItem")));
        Gui.setFlageditorResetItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlageditorResetItem")));
    }

    public net.milkbowl.vault.permission.Permission getVaultPerms() {
        return this.vaultPerms;
    }

    private void loadGroups() {
        if (getConfig().get("Limits") != null) {
            List<String> groups = new ArrayList<>(getConfig().getConfigurationSection("Limits").getKeys(false));
            if (groups != null) {
                for (int i = 0; i < groups.size(); i++) {
                    LimitGroup.getGroupList().add(new LimitGroup(groups.get(i)));
                }
            }
        }
    }

    private void loadInactivityExpirationGroups() {
        if (getConfig().get("DefaultInactivityExpiration") != null) {
            InactivityExpirationGroup.DEFAULT = InactivityExpirationGroup.parse(getConfig().getConfigurationSection("DefaultInactivityExpiration"), "Default");
        }
        if (getConfig().get("InactivityExpiration") == null) {
            return;
        }
        List<String> groups = new ArrayList<>(getConfig().getConfigurationSection("InactivityExpiration").getKeys(false));
        if (groups == null) {
            return;
        }
        for (String groupname : groups) {
            ConfigurationSection groupSection = getConfig().getConfigurationSection("InactivityExpiration." + groupname);
            InactivityExpirationGroup.add(InactivityExpirationGroup.parse(groupSection, groupname));
        }
    }

    private void loadOther() {

        try {
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
        if (slmSection == null) {
            return;
        }
        ConfigurationSection blacklistRegionSection = slmSection.getConfigurationSection("regionblacklist");
        if (blacklistRegionSection == null) {
            return;
        }
        Set<String> worlds = blacklistRegionSection.getKeys(false);
        if (worlds == null) {
            return;
        }
        Set<WGRegion> wgRegions = new HashSet<>();
        for (String worldName : worlds) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }
            List<String> regionNames = blacklistRegionSection.getStringList(worldName);
            if (regionNames == null) {
                continue;
            }

            for (String regionName : regionNames) {
                WGRegion wgRegion = this.getWorldGuardInterface().getRegion(world, this.getWorldGuard(), regionName);
                if (wgRegion == null) {
                    continue;
                }
                wgRegions.add(wgRegion);
            }
        }
        SignLinkMode.setBlacklistedRegions(wgRegions);
    }

    public Economy getEcon() {
        return this.econ;
    }

    public WorldEditPlugin getWorldedit() {
        return this.worldedit;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("arm")) {
            return true;
        }
        try {
            if (args.length >= 1) {
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
            if (syntax.size() >= 1) {
                String message = Messages.BAD_SYNTAX;

                message = message.replace("%command%", "/" + commandsLabel + " " + syntax.get(0));

                for (int x = 1; x < syntax.size(); x++) {
                    message = message + " " + Messages.BAD_SYNTAX_SPLITTER.replace("%command%", "/" + commandsLabel + " " + syntax.get(x));
                }
                sender.sendMessage(Messages.PREFIX + message);
            }
            return true;
        }
    }

    public void generatedefaultconfig() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/config.yml");
        if (!messagesdic.exists()) {
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

    private void updateConfigs() {
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
            if (version < 1.1) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.1...");
                updateTo1p1(pluginConfig);
            }
            if (version < 1.2) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.2...");
                getLogger().log(Level.WARNING, "Warning!: ARM uses a new schematic format now! You have to update all region schematics with");
                getLogger().log(Level.WARNING, "/arm updateschematic [REGION] or go back to ARM version 1.1");
                updateTo1p2(pluginConfig);
            }
            if (version < 1.21) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.21...");
                updateTo1p3(pluginConfig);
            }
            if (version < 1.3) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.3...");
                updateTo1p3(pluginConfig);
            }
            if (version < 1.4) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4...");
                updateTo1p4(pluginConfig);
            }
            if (version < 1.41) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.1...");
                pluginConfig.set("Version", 1.41);
                saveConfig();
            }
            if (version < 1.44) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.4.4...");
                updateTo1p44(pluginConfig);
            }
            if (version < 1.5) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5...");
                pluginConfig.set("Version", 1.5);
                pluginConfig.set("Reselling.Offers.OfferTimeOut", 30);
                saveConfig();
            }
            if (version < 1.52) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.5.2...");
                updateTo1p52(pluginConfig);
            }
            if (version < 1.6) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.6...");
                updateTo1p6(pluginConfig);
            }
            if (version < 1.7) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7...");
                pluginConfig.set("Other.RemoveEntitiesOnRegionBlockReset", true);
                pluginConfig.set("Version", 1.7);
                saveConfig();
            }
            if (version < 1.72) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.2...");
                pluginConfig.set("SignClickActions.RightClickNotSneakCmd", "buyaction");
                pluginConfig.set("SignClickActions.RightClickSneakCmd", "arm sellback %regionid%");
                pluginConfig.set("SignClickActions.LeftClickNotSneakCmd", "arm info %regionid%");
                pluginConfig.set("SignClickActions.LeftClickSneakCmd", "arm info %regionid%");
                pluginConfig.set("Version", 1.72);
                saveConfig();
            }
            if (version < 1.75) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.7.5...");
                updateTo1p75(pluginConfig);
            }
            if (version < 1.8) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8...");
                updateTo1p8(pluginConfig);
            }

            if (version < 1.81) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.1..");
                updateTo1p81(pluginConfig);
            }
            if (version < 1.83) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.8.3..");
                updateTo1p83(pluginConfig);
            }
            if (version < 1.9) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9..");
                updateTo1p9(pluginConfig);
            }
            if (version < 1.92) {
                updateTo1p92(pluginConfig);
            }
            if (version < 1.95) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9.5..");
                updateTo1p95(pluginConfig);
            }
            if (version < 1.97) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 1.9.7..");
                updateTo1p97(pluginConfig);
            }
            if (version < 2.00) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.0.0..");
                updateTo2p00(pluginConfig);
            }
            if (version < 2.08) {
                getLogger().log(Level.WARNING, "Updating AdvancedRegionMarket config to 2.0.8..");
                updateTo2p08(pluginConfig);
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
        if (worlds != null) {
            for (int y = 0; y < worlds.size(); y++) {
                ArrayList<String> regions = new ArrayList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                if (regions != null) {
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

        if (regionConf.get("Regions") != null) {
            List<String> worlds = new ArrayList<>(regionConf.getConfigurationSection("Regions").getKeys(false));
            if (worlds != null) {
                for (int y = 0; y < worlds.size(); y++) {
                    if (regionConf.get("Regions." + worlds.get(y)) != null) {
                        LinkedList<String> regions = new LinkedList<String>(regionConf.getConfigurationSection("Regions." + worlds.get(y)).getKeys(false));
                        if (regions != null) {
                            for (int i = 0; i < regions.size(); i++) {
                                if (regionConf.getBoolean("Regions." + worlds.get(y) + "." + regions.get(i) + ".rentregion")) {
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
        if (pluginConfig.get("RegionKinds") != null) {
            LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
            if (regionkinds != null) {
                for (int y = 0; y < regionkinds.size(); y++) {
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

        if (pluginConfig.get("RegionKinds") != null) {
            LinkedList<String> regionkinds = new LinkedList<String>(pluginConfig.getConfigurationSection("RegionKinds").getKeys(false));
            if (regionkinds != null) {
                for (int y = 0; y < regionkinds.size(); y++) {
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

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
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
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
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

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
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
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
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

        if (regionConf.get("Regions") != null) {
            ConfigurationSection mainSection = regionConf.getConfigurationSection("Regions");
            List<String> worlds = new ArrayList<String>(mainSection.getKeys(false));
            if (worlds != null) {
                for (String worldString : worlds) {
                    if (mainSection.get(worldString) != null) {
                        ConfigurationSection worldSection = mainSection.getConfigurationSection(worldString);
                        List<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                        if (regions != null) {
                            for (String regionname : regions) {
                                ConfigurationSection regionSection = worldSection.getConfigurationSection(regionname);
                                //SIGNS
                                List<String> regionsignsloc = regionSection.getStringList("signs");
                                for (int i = 0; i < regionsignsloc.size(); i++) {
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
                                            for (int i = 0; i < subregionsignsloc.size(); i++) {
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

    private void updateTo1p95(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.95);
        saveConfig();
    }

    private void updateTo1p97(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 1.97);

        File schemfolder = new File(this.getDataFolder() + "/schematics");
        if (schemfolder.isDirectory()) {
            List<File> worldFolders = new ArrayList<>();
            for (File listedFile : schemfolder.listFiles()) {
                if (listedFile.isDirectory()) {
                    worldFolders.add(listedFile);
                }
            }

            for (File worldfolder : worldFolders) {
                List<File> builtblocksFiles = new ArrayList<>();
                for (File schemFile : worldfolder.listFiles()) {
                    if (schemFile.getName().endsWith("--builtblocks.schematic")) {
                        builtblocksFiles.add(schemFile);
                    }
                }

                for (File builtBlocksFile : builtblocksFiles) {
                    FileReader fileReader = new FileReader(builtBlocksFile);
                    BufferedReader bReader = new BufferedReader(fileReader);
                    List<String> blocks = new LinkedList<>();
                    String line;
                    while ((line = bReader.readLine()) != null) {
                        blocks.add(line);
                    }
                    bReader.close();
                    fileReader.close();
                    builtBlocksFile.delete();

                    File newBuiltBlocksFile = new File(builtBlocksFile.getAbsolutePath().substring(0, builtBlocksFile.getAbsolutePath().length() - 23) + ".builtblocks");
                    FileWriter fileWriter = new FileWriter(newBuiltBlocksFile);
                    BufferedWriter bWriter = new BufferedWriter(fileWriter);

                    for (String location : blocks) {
                        String[] coordinates = location.split(";", 2);
                        bWriter.write(coordinates[1]);
                        bWriter.newLine();
                    }
                    bWriter.close();
                    fileWriter.close();
                }
            }
        }
        saveConfig();
    }

    private void updateTo2p00(FileConfiguration pluginConfig) throws IOException {
        pluginConfig.set("Version", 2.00);
        pluginConfig.set("DefaultInactivityExpiration.resetAfter", "none");
        pluginConfig.set("DefaultInactivityExpiration.takeOverAfter", "none");

        pluginConfig.set("InactivityExpiration.examplegroup1.resetAfter", "30d");
        pluginConfig.set("InactivityExpiration.examplegroup1.takeOverAfter", "20d");
        pluginConfig.set("InactivityExpiration.examplegroup2.resetAfter", "60d");
        pluginConfig.set("InactivityExpiration.examplegroup2.takeOverAfter", "50d");
        pluginConfig.set("InactivityExpiration.examplegroup3.resetAfter", "none");
        pluginConfig.set("Subregions.SubregionInactivityReset", pluginConfig.getBoolean("Subregions.SubregionAutoReset"));
        pluginConfig.set("InactivityExpiration.examplegroup3.takeOverAfter", "none");
        pluginConfig.set("Other.userResetCooldown", "7d");
        pluginConfig.set("AutoResetAndTakeOver", null);
        saveConfig();

        File regionConfDic = new File(this.getDataFolder() + "/regions.yml");
        YamlConfiguration regionConf = YamlConfiguration.loadConfiguration(regionConfDic);

        ConfigurationSection regionsSection = regionConf.getConfigurationSection("Regions");
        if (regionsSection != null) {
            ArrayList<String> worlds = new ArrayList<String>(regionsSection.getKeys(false));
            for (String worldName : worlds) {
                ConfigurationSection worldSection = regionsSection.getConfigurationSection(worldName);
                if (worldSection == null) {
                    continue;
                }
                ArrayList<String> regions = new ArrayList<String>(worldSection.getKeys(false));
                for (String regionID : regions) {
                    ConfigurationSection regSection = worldSection.getConfigurationSection(regionID);
                    if (regSection == null) {
                        continue;
                    }
                    boolean autoreset = regSection.getBoolean("autoreset");
                    regSection.set("inactivityReset", autoreset);
                    regSection.set("lastLogin", new GregorianCalendar().getTimeInMillis());
                    if (regSection.getString("regiontype").equalsIgnoreCase("rentregion")) {
                        long extendTime = regSection.getLong("rentExtendPerClick");
                        regSection.set("extendTime", extendTime);
                        regSection.set("rentExtendPerClick", null);
                    }
                    ConfigurationSection subSection = regSection.getConfigurationSection("subregions");
                    if (subSection != null) {
                        ArrayList<String> subregions = new ArrayList<String>(subSection.getKeys(false));
                        for (String subregionID : subregions) {
                            ConfigurationSection subregionSection = subSection.getConfigurationSection(subregionID);
                            if (subregionSection == null) {
                                continue;
                            }
                            subregionSection.set("lastLogin", new GregorianCalendar().getTimeInMillis());
                            if (subregionSection.getString("regiontype").equalsIgnoreCase("rentregion")) {
                                long extendTime = subregionSection.getLong("rentExtendPerClick");
                                subregionSection.set("extendTime", extendTime);
                                subregionSection.set("rentExtendPerClick", null);
                            }
                        }
                    }
                }
            }
        }


        regionConf.save(regionConfDic);

        File messagesConfDic = new File(this.getDataFolder() + "/messages.yml");
        YamlConfiguration messagesConf = YamlConfiguration.loadConfiguration(messagesConfDic);
        ConfigurationSection messagesSection = messagesConf.getConfigurationSection("Messages");
        if (messagesSection != null) {
            ArrayList<String> messageKeys = new ArrayList<String>(messagesSection.getKeys(false));

            for (String key : messageKeys) {
                Object msgObject = messagesSection.get(key);
                if (msgObject instanceof List) {
                    List<String> msgList = (List) msgObject;
                    for (int i = 0; i < msgList.size(); i++) {
                        msgList.set(i, msgList.get(i).replace("%extendperclick%", "%extendtime%"));
                        msgList.set(i, msgList.get(i).replace("%extend%", "%extendtime%"));
                        msgList.set(i, msgList.get(i).replace("%isautoreset%", "%isinactivityreset%"));
                        msgList.set(i, msgList.get(i).replace("%remainingdays%", "%remainingusersellcooldown%"));
                        msgList.set(i, msgList.get(i).replace("%days%", "%userresetcooldown%"));
                    }
                } else if (msgObject instanceof String) {
                    String msgString = (String) msgObject;
                    msgString = msgString.replace("%extendperclick%", "%extendtime%");
                    msgString = msgString.replace("%extend%", "%extendtime%");
                    msgString = msgString.replace("%isautoreset%", "%isinactivityreset%");
                    msgString = msgString.replace("%remainingdays%", "%remainingusersellcooldown%");
                    msgString = msgString.replace("%days%", "%userresetcooldown%");

                    messagesSection.set(key, msgString);
                }
            }
        }
        messagesConf.set("Messages.ResetRegionCooldownError", "&7You have to wait&6 %remainingusersellcooldown% &7till you can reset your region again");
        messagesConf.set("Messages.RegionInfoSellregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoRentregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoContractregionAdmin", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoSellregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoRentregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoContractregionUser", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%",
                "&9Allowed Subregions: &e%subregionlimit%",
                "&9Subregions: &e%subregions%")));
        messagesConf.set("Messages.RegionInfoSellregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.RegionInfoRentregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7max.: &e%maxrenttime%",
                "&9Remaining time: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.RegionInfoContractregionSubregion", new ArrayList<>(Arrays.asList("&6=========[Region Info]=========",
                "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
                "&9Sold: &e%issold%",
                "&9Price: &e%price% &7per &e%extendtime% &7(auto extend)",
                "&9Next extend in: &e%remaining%",
                "&9Owner: &e%owner%",
                "&9Members: &e%members%",
                "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
                "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
                "&9UserResettable: &e%isuserresettable% &9InactivityResetEnabled: &e%isinactivityreset%",
                "&9Owners last login: &e%lastownerlogin%",
                "&9InactivityReset in: &e%inactivityresetin%",
                "&9TakeOver possible in: &e%takeoverin%",
                "&9DoBlockReset: &e%isdoblockreset% &9Autoprice: &e%autoprice%")));
        messagesConf.set("Messages.TakeOverItemLore", new ArrayList<>(Arrays.asList("&aYou are a member of this region.",
                "&aThe owner of it hasn''t been",
                "&aonline for a long time. You",
                "&acan transfer the owner rights to your",
                "&aaccount for free. The actual owner",
                "&aof it will become a member of the region.",
                "&cIf the region does not get transferred",
                "&cor the owner does not come online",
                "&cwithin &7%inactivityresetin% &cthe",
                "&cregion will be resetted and everybody on it",
                "&cwill lose their rights.",
                "&cAfterwards it will go back for sale!")));
        messagesConf.save(messagesConfDic);


        File flaggroupsConfDic = new File(this.getDataFolder() + "/flaggroups.yml");
        YamlConfiguration flaggroupsConf = YamlConfiguration.loadConfiguration(flaggroupsConfDic);

        class FlagUpdater {
            void updateFlagGroup(ConfigurationSection cSection) {
                if (cSection == null) {
                    return;
                }
                ConfigurationSection availableSection = cSection.getConfigurationSection("available");
                ConfigurationSection soldSection = cSection.getConfigurationSection("sold");
                updateFlagSet(availableSection);
                updateFlagSet(soldSection);
            }

            void updateFlagSet(ConfigurationSection cSection) {
                if (cSection == null) {
                    return;
                }
                ArrayList<String> keys = new ArrayList<String>(cSection.getKeys(false));
                for (String key : keys) {
                    String setting = cSection.getString(key + ".setting");
                    if (setting == null) {
                        continue;
                    }
                    setting = setting.replace("%extend%", "%extendtime%");
                    setting = setting.replace("%extendperclick%", "%extendtime%");
                    cSection.set(key + ".setting", setting);
                }
            }

        }

        FlagUpdater flagUpdater = new FlagUpdater();
        ConfigurationSection groupsSection = flaggroupsConf.getConfigurationSection("FlagGroups");
        if (groupsSection != null) {
            ArrayList<String> groupKeys = new ArrayList<String>(groupsSection.getKeys(false));
            for (String key : groupKeys) {
                flagUpdater.updateFlagGroup(groupsSection.getConfigurationSection(key));
            }
        }
        flagUpdater.updateFlagGroup(flaggroupsConf.getConfigurationSection("DefaultFlagGroup"));
        flagUpdater.updateFlagGroup(flaggroupsConf.getConfigurationSection("SubregionFlagGroup"));
        flaggroupsConf.save(flaggroupsConfDic);
    }

    private void updateTo2p08(FileConfiguration pluginConfig) throws IOException {
        File messagesConfDic = new File(this.getDataFolder() + "/messages.yml");
        YamlConfiguration messagesConf = YamlConfiguration.loadConfiguration(messagesConfDic);
        ConfigurationSection messagesSection = messagesConf.getConfigurationSection("Messages");
        if (messagesSection != null) {
            ArrayList<String> messageKeys = new ArrayList<String>(messagesSection.getKeys(false));

            for (String key : messageKeys) {
                Object msgObject = messagesSection.get(key);
                if (msgObject instanceof List) {
                    List<String> msgList = (List) msgObject;
                    for (int i = 0; i < msgList.size(); i++) {
                        msgList.set(i, msgList.get(i).replace("%remainingusersellcooldown%", "%remaininguserresetcooldown%"));
                    }
                } else if (msgObject instanceof String) {
                    String msgString = (String) msgObject;
                    msgString = msgString.replace("%remainingusersellcooldown%", "%remaininguserresetcooldown%");
                    messagesSection.set(key, msgString);
                }
            }
        }
        messagesConf.save(messagesConfDic);
        File regionkindsConfDic = new File(this.getDataFolder() + "/regionkinds.yml");
        YamlConfiguration regionkindsConf = YamlConfiguration.loadConfiguration(regionkindsConfDic);
        ConfigurationSection regionKindsSection = regionkindsConf.getConfigurationSection("RegionKinds");
        if (regionKindsSection != null) {
            ArrayList<String> worlds = new ArrayList<String>(regionKindsSection.getKeys(false));
            for (String regionkindName : worlds) {
                ConfigurationSection regionKindSection = regionKindsSection.getConfigurationSection(regionkindName);
                if (regionKindSection == null) {
                    continue;
                }
                regionKindSection.set("displayInRegionfinder", regionKindSection.getBoolean("displayInGUI"));
            }
        }
        regionkindsConf.save(regionkindsConfDic);
        pluginConfig.set("Version", 2.08);
        saveConfig();
    }

}

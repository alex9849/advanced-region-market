package net.alex9849.arm;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.alex9849.arm.adapters.WGRegion;
import net.alex9849.arm.adapters.WorldEditInterface;
import net.alex9849.arm.adapters.WorldGuardInterface;
import net.alex9849.arm.adapters.signs.SignDataFactory;
import net.alex9849.arm.adapters.util.AbstractMaterialFinder;
import net.alex9849.arm.adapters.util.Version;
import net.alex9849.arm.adapters.util.YamlFileManager;
import net.alex9849.arm.commands.DeleteCommand;
import net.alex9849.arm.commands.InfoCommand;
import net.alex9849.arm.commands.*;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.arm.entitylimit.commands.CreateCommand;
import net.alex9849.arm.entitylimit.commands.ListCommand;
import net.alex9849.arm.entitylimit.commands.*;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.flaggroups.FlagGroupManager;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.gui.GuiConstants;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.handler.listener.*;
import net.alex9849.arm.inactivityexpiration.InactivityExpirationGroup;
import net.alex9849.arm.inactivityexpiration.PlayerInactivityGroupMapper;
import net.alex9849.arm.limitgroups.LimitGroupManager;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPatternManager;
import net.alex9849.arm.presets.commands.MaxExtendTimeCommand;
import net.alex9849.arm.presets.commands.PaybackPercentageCommand;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regionkind.RegionKindGroupManager;
import net.alex9849.arm.regionkind.RegionKindManager;
import net.alex9849.arm.regionkind.regionkindcommands.*;
import net.alex9849.arm.regions.CountdownRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.subregions.commands.ToolCommand;
import net.alex9849.pluginstats.client.Analytics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
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

import java.io.File;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;

public class AdvancedRegionMarket extends JavaPlugin {
    private Economy econ = null;
    private net.milkbowl.vault.permission.Permission vaultPerms = null;
    private WorldGuardInterface worldGuardInterface = null;
    private WorldEditInterface worldEditInterface = null;
    private CommandHandler commandHandler = null;
    private RegionKindManager regionKindManager = null;
    private RegionKindGroupManager regionKindGroupManager = null;
    private EntityLimitGroupManager entityLimitGroupManager = null;
    private RegionManager regionManager = null;
    private PresetPatternManager presetPatternManager = null;
    private SignDataFactory signDataFactory = null;
    private FlagGroupManager flagGroupManager = null;
    private LimitGroupManager limitGroupManager = null;
    private ArmSettings pluginSettings = null;
    private AbstractMaterialFinder materialFinder = null;
    private Analytics analytics = null;


    /*#########################################
    ######### Startup / Shutdown stuff ########
    #########################################*/
    public static AdvancedRegionMarket getInstance() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket");
        if (plugin instanceof AdvancedRegionMarket) {
            return (AdvancedRegionMarket) plugin;
        } else {
            return null;
        }
    }

    public void onEnable() {
        Reader pluginYmlReader = Objects.requireNonNull(getTextResource("plugin.yml"));
        YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(pluginYmlReader);
        //This is a workaround to make shure that this plugin is loaded after the last world has been loaded.
        boolean doStartupWorkaround = false;
        List<String> softdependCheckPlugins = Arrays.asList("MultiWorld", "Multiverse-Core");
        for (String pluginName : softdependCheckPlugins) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            doStartupWorkaround |= plugin != null && !plugin.isEnabled();
        }
        if (doStartupWorkaround) {
            getLogger().log(Level.WARNING, "It looks like one of these plugins is installed, but not loaded yet:\n" +
                    String.join(", ", softdependCheckPlugins) + "\n" +
                    "In order to keep ARM working it scheduled its own enabling code to the end of the startup process as fallback!\n");
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::startup, 1);
        } else {
            startup();
        }
    }

    public void startup() {
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
        setupServerVersionFactories();

        File schematicdic = new File(getDataFolder() + "/schematics");
        if (!schematicdic.exists()) {
            schematicdic.mkdirs();
        }

        this.generateConfigs();
        Updater.updateConfigs();

        String localeString = getConfig().getString("Other.Language");
        Messages.MessageLocale messageLocale = Messages.MessageLocale.byCode(localeString);
        if (messageLocale == null) {
            messageLocale = Messages.MessageLocale.EN;
            getLogger().log(Level.WARNING, "Could not file Message locale \"" + localeString + "\"! Using English as fallback!");
        }
        Messages.reload(new File(getDataFolder() + "/messages.yml"), messageLocale);
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

        this.pluginSettings = new ArmSettings();
        this.pluginSettings.setIsTeleportAfterRentRegionBought(getConfig().getBoolean("Other.TeleportAfterRentRegionBought"));
        this.pluginSettings.setIsTeleportAfterRentRegionExtend(getConfig().getBoolean("Other.TeleportAfterRentRegionExtend"));
        this.pluginSettings.setIsTeleportAfterSellRegionBought(getConfig().getBoolean("Other.TeleportAfterSellRegionBought"));
        this.pluginSettings.setIsTeleportAfterContractRegionBought(getConfig().getBoolean("Other.TeleportAfterContractRegionBought"));
        this.pluginSettings.setIsSendContractRegionExtendMessage(getConfig().getBoolean("Other.SendContractRegionExtendMessage"));
        this.pluginSettings.setDateTimeformat(getConfig().getString("Other.DateTimeFormat"));
        this.pluginSettings.setIsRegionInfoParticleBorder(getConfig().getBoolean("Other.RegionInfoParticleBorder"));
        this.pluginSettings.setRemoveEntitiesOnRegionBlockReset(getConfig().getBoolean("Other.RemoveEntitiesOnRegionRestore"));
        this.pluginSettings.setIsAllowSubregionUserRestore(getConfig().getBoolean("Subregions.AllowSubregionUserRestore"));
        this.pluginSettings.setIsSubregionAutoRestore(getConfig().getBoolean("Subregions.SubregionAutoRestore"));
        this.pluginSettings.setIsSubregionInactivityReset(getConfig().getBoolean("Subregions.SubregionInactivityReset"));
        this.pluginSettings.setDeleteSubregionsOnParentRegionBlockReset(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionRestore"));
        this.pluginSettings.setDeleteSubregionsOnParentRegionUnsell(getConfig().getBoolean("Subregions.deleteSubregionsOnParentRegionUnsell"));
        this.pluginSettings.setAllowParentRegionOwnersBuildOnSubregions(getConfig().getBoolean("Subregions.allowParentRegionOwnersBuildOnSubregions"));
        this.pluginSettings.setSignRightClickSneakCommand(getConfig().getString("SignClickActions.RightClickSneakCmd"));
        this.pluginSettings.setSignRightClickNotSneakCommand(getConfig().getString("SignClickActions.RightClickNotSneakCmd"));
        this.pluginSettings.setSignLeftClickSneakCommand(getConfig().getString("SignClickActions.LeftClickSneakCmd"));
        this.pluginSettings.setSignLeftClickNotSneakCommand(getConfig().getString("SignClickActions.LeftClickNotSneakCmd"));
        this.pluginSettings.setCreateBackupOnRegionRestore(getConfig().getBoolean("Backups.createBackupOnRegionRestore"));
        this.pluginSettings.setCreateBackupOnRegionUnsell(getConfig().getBoolean("Backups.createBackupOnRegionUnsell"));
        this.pluginSettings.setMaxSubRegionMembers(getConfig().getInt("Subregions.SubregionMaxMembers"));
        this.pluginSettings.setSubRegionPaybackPercentage(getConfig().getInt("Subregions.SubregionPaybackPercentage"));
        this.pluginSettings.setSendRentRegionExpirationWarning(getConfig().getBoolean("Other.SendRentRegionExpirationWarning"));
        this.pluginSettings.setRentRegionExpirationWarningTime(RentPrice.stringToTime(getConfig().getString("Other.RentRegionExpirationWarningTime")));
        FlagGroup.setFeatureEnabled(getConfig().getBoolean("FlagGroups.enabled"));

        try {
            this.pluginSettings.setUserResetCooldown(CountdownRegion.stringToTime(getConfig().getString("Other.userResetCooldown")));
        } catch (IllegalArgumentException e) {
            this.pluginSettings.setUserResetCooldown(604800000);
            getLogger().log(Level.WARNING, "Could not parse 'Other.userResetCooldown' using 7d for now!");
        }


        this.regionKindManager = new RegionKindManager(new File(this.getDataFolder() + "/regionkinds.yml"));
        this.regionKindGroupManager = new RegionKindGroupManager(new File(this.getDataFolder() + "/regionkindgroups.yml"), this.regionKindManager);
        this.entityLimitGroupManager = new EntityLimitGroupManager(new File(this.getDataFolder() + "/entitylimits.yml"));
        loadAutoPrice();
        loadLimits();
        loadGUI();
        this.flagGroupManager = new FlagGroupManager(new File(this.getDataFolder() + "/flaggroups.yml"));
        this.regionManager = new RegionManager(new File(this.getDataFolder() + "/regions.yml"), 20 * getConfig().getInt("Other.SignAndResetUpdateInterval"));
        getLogger().log(Level.INFO, "Regions loaded!");

        loadSignLinkingModeRegions();
        loadInactivityExpirationGroups();
        this.presetPatternManager = new PresetPatternManager(new File(this.getDataFolder() + "/presets.yml"));
        this.getRegionManager().setTabCompleteRegions(getConfig().getBoolean("Other.CompleteRegionsOnTabComplete"));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.getRegionManager().doTick(), 1, 1);

        this.loadCommands();

        getLogger().log(Level.INFO, "Programmed by Alex9849");
        getLogger().log(Level.INFO, "I'm always searching for better translations of AdvancedRegionMarket. "
                + "If you've translated the plugin it would be very nice if you would send me your translation via "
                + "spigot private message! :)");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                AdvancedRegionMarket.getInstance().getRegionManager().updateFile();
                AdvancedRegionMarket.getInstance().getRegionKindGroupManager().updateFile();
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
                    if (region.isInactivityReset() && region.isInactive()) {
                        try {
                            region.automaticResetRegion(Region.ActionReason.INACTIVITY, true);
                        } catch (SchematicNotFoundException e) {
                            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
                        }
                    }
                }
            }
        }, 1800, 6000);

        //Enable bStats
        BStatsAnalytics bStatsAnalytics = new BStatsAnalytics();
        bStatsAnalytics.register(this);
        //Enable own analytics
        try {
            this.analytics = Analytics.genInstance(this, new URL("https://mc-analytics.alex9849.net"), () -> {
                    },
                    () -> {
                        Map<String, String> pluginSpecificData = new LinkedHashMap<>();
                        BStatsAnalytics.RegionStatistics rs = BStatsAnalytics.getRegionStatistics();
                        int totalRegions = rs.getAvailableContractRegions();
                        totalRegions += rs.getAvailableRentRegions();
                        totalRegions += rs.getAvailableSellRegions();
                        totalRegions += rs.getSoldContractRegions();
                        totalRegions += rs.getSoldRentRegions();
                        totalRegions += rs.getSoldSellRegions();
                        pluginSpecificData.put("regionsTotal", totalRegions + "");
                        pluginSpecificData.put("regionsSell", (rs.getAvailableSellRegions() + rs.getSoldSellRegions()) + "");
                        pluginSpecificData.put("regionsRent", (rs.getAvailableRentRegions() + rs.getSoldRentRegions()) + "");
                        pluginSpecificData.put("regionsContract", (rs.getAvailableContractRegions() + rs.getSoldContractRegions()) + "");
                        return pluginSpecificData;
                    });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void onDisable() {
        this.getPresetPatternManager().updateFile();
        this.getRegionManager().updateFile();
        this.getRegionKindManager().updateFile();
        this.getEntityLimitGroupManager().updateFile();
        this.getFlagGroupManager().updateFile();
        this.econ = null;
        this.vaultPerms = null;
        InactivityExpirationGroup.reset();
        AutoPrice.reset();
        SignLinkMode.reset();
        ActivePresetManager.reset();
        Offer.reset();
        PlayerInactivityGroupMapper.reset();
        if (this.analytics != null)
            this.analytics.shutdown();
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
        EntityChangeBlockEvent.getHandlerList().unregister(this);
        getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }

    private void loadCommands() {
        this.commandHandler = new CommandHandler();
        List<BasicArmCommand> commands = new ArrayList<>();
        String[] betweencmds = {};
        commands.add(new AddMemberCommand(this));
        commands.add(new ApplyPresetCommand(this));
        commands.add(new SetInactivityResetCommand(this));
        commands.add(new DeleteCommand(this));
        commands.add(new AddCommand(this));
        commands.add(new SetAutoRestoreCommand(this));
        commands.add(new ExtendCommand(this));
        commands.add(new RegionfinderCommand(this));
        commands.add(new GuiCommand(this));
        commands.add(new HelpCommand(this.commandHandler, this, Messages.HELP_HEADLINE, betweencmds, Permission.ARM_HELP));
        commands.add(new SetHotelCommand(this));
        commands.add(new InfoCommand(this));
        commands.add(new SetTpLocation(this));
        commands.add(new LimitCommand(this));
        commands.add(new OfferCommand(this));
        commands.add(new CreateBackupCommand(this));
        commands.add(new RestoreBackupCommand(this));
        commands.add(new ListBackupsCommand(this));
        commands.add(new RegionstatsCommand(this));
        commands.add(new ReloadCommand(this));
        commands.add(new RemoveMemberCommand(this));
        commands.add(new RestoreCommand(this));
        commands.add(new ResetCommand(this));
        commands.add(new SetOwnerCommand(this));
        commands.add(new SetRegionKind(this));
        commands.add(new SetPaybackPercentageCommand(this));
        commands.add(new SetWarpCommand(this));
        commands.add(new TerminateCommand(this));
        commands.add(new ListRegionsCommand(this));
        commands.add(new TpToFreeRegion(this));
        commands.add(new TPCommand(this));
        commands.add(new UnsellCommand(this));
        commands.add(new UpdateSchematicCommand(this));
        commands.add(new BuyCommand(this));
        commands.add(new SellBackCommand(this));
        commands.add(new SetSubregionLimit(this));
        commands.add(new SetMaxMembersCommand(this));
        commands.add(new SetPriceCommand(this));
        commands.add(new SetIsUserRestorableCommand(this));
        commands.add(new ListAutoPricesCommand(this));
        commands.add(new FlageditorCommand(this));
        commands.add(new SetFlaggroupCommand(this));
        commands.add(new SignLinkModeCommand(this));
        commands.add(new SetEntityLimitCommand(this));
        commands.add(new SetLandLord(this));
        commands.add(new SetProtectionOfContinuance(this));
        commands.add(new AddTimeCommand(this));

        List<String> entityLimtUsage = new ArrayList<>(Arrays.asList("entitylimit [SETTING]", "entitylimit help"));
        List<BasicArmCommand> entityLimitCommands = new ArrayList<>();
        entityLimitCommands.add(new CreateCommand(this));
        entityLimitCommands.add(new net.alex9849.arm.entitylimit.commands.DeleteCommand(this));
        entityLimitCommands.add(new RemoveLimit(this));
        entityLimitCommands.add(new AddLimitCommand(this));
        entityLimitCommands.add(new net.alex9849.arm.entitylimit.commands.InfoCommand(this));
        entityLimitCommands.add(new ListCommand(this));
        entityLimitCommands.add(new CheckCommand(this));
        entityLimitCommands.add(new SetExtraLimitCommand(this));
        entityLimitCommands.add(new BuyExtraCommand(this));
        commands.add(new CommandSplitter("entitylimit", this, entityLimtUsage, Permission.ADMIN_ENTITYLIMIT_HELP, Messages.ENTITYLIMIT_HELP_HEADLINE, entityLimitCommands));

        List<String> regionKindUsage = new ArrayList<>(Arrays.asList("regionkind [SETTING]", "regionkind help"));
        List<BasicArmCommand> regionKindCommands = new ArrayList<>();
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.CreateCommand(this));
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.DeleteCommand(this));
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.ListCommand(this));
        regionKindCommands.add(new SetDisplayInRegionfinderCommand(this));
        regionKindCommands.add(new SetDisplayInLimitsCommand(this));
        regionKindCommands.add(new SetItemCommand(this));
        regionKindCommands.add(new AddLoreLineCommand(this));
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.InfoCommand(this));
        regionKindCommands.add(new RemoveLoreLineCommand(this));
        regionKindCommands.add(new SetDisplayNameCommand(this));
        commands.add(new CommandSplitter("regionkind", this, regionKindUsage, Permission.REGIONKIND_HELP, Messages.REGIONKIND_HELP_HEADLINE, regionKindCommands));

        List<String> regionkindGroupUsage = Arrays.asList("regionkindgroup [SETTING]", "regionkindgroup help");
        List<BasicArmCommand> regionkindGroupCommands = new ArrayList<>();
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.CreateCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.DeleteCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.InfoCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.ListCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.SetDisplayInLimitsCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.SetDisplayNameCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.AddRegionkindCommand(this));
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.RemoveRegionkindCommand(this));
        commands.add(new CommandSplitter("regionkindgroup", this, regionkindGroupUsage, Permission.REGIONKINDGROUP_HELP, Messages.REGIONKINDGROUP_HELP_HEADLINE, regionkindGroupCommands));

        List<String> subRegionUsage = new ArrayList<>(Arrays.asList("subregion [SETTING]", "subregion help"));
        List<BasicArmCommand> subRegionCommands = new ArrayList<>();
        subRegionCommands.add(new ToolCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.CreateCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.SetHotelCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.TPCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.RestoreCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.UnsellCommand(this));
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.DeleteCommand(this));
        commands.add(new CommandSplitter("subregion", this, subRegionUsage, Permission.SUBREGION_HELP, Messages.SUBREGION_HELP_HEADLINE, subRegionCommands));

        List<String> sellPresetUsage = new ArrayList<>(Arrays.asList("sellpreset [SETTING]", "sellpreset help"));
        List<BasicArmCommand> sellPresetCommands = new ArrayList<>();
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new PaybackPercentageCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.UpdateCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.SELLPRESET, this));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.SELLPRESET, this));
        commands.add(new CommandSplitter("sellpreset", this, sellPresetUsage, Permission.ADMIN_PRESET_HELP,
                Messages.SELLPRESET_HELP_HEADLINE, sellPresetCommands));

        List<String> constractPresetUsage = new ArrayList<>(Arrays.asList("contractpreset [SETTING]", "contractpreset help"));
        List<BasicArmCommand> contractPresetCommands = new ArrayList<>();
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ExtendTimeCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new PaybackPercentageCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.UpdateCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.CONTRACTPRESET, this));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.CONTRACTPRESET, this));
        commands.add(new CommandSplitter("contractpreset", this, constractPresetUsage, Permission.ADMIN_PRESET_HELP,
                Messages.CONTRACTPRESET_HELP_HEADLINE, contractPresetCommands));

        List<String> rentPresetUsage = new ArrayList<>(Arrays.asList("rentpreset [SETTING]", "rentpreset help"));
        List<BasicArmCommand> rentPresetCommands = new ArrayList<>();
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ExtendTimeCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new MaxExtendTimeCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new PaybackPercentageCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.UpdateCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.RENTPRESET, this));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.RENTPRESET, this));
        commands.add(new CommandSplitter("rentpreset", this, rentPresetUsage, Permission.ADMIN_PRESET_HELP,
                Messages.RENTPRESET_HELP_HEADLINE, rentPresetCommands));
        this.commandHandler.addCommands(commands);
        getCommand("arm").setTabCompleter(this.commandHandler);
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

    private boolean isFaWeInstalled() {
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
        WorldGuardPlugin worldguard = (WorldGuardPlugin) plugin;
        String version = "notSupported";
        if (worldguard.getDescription().getVersion().startsWith("6.1")) {
            version = "6_1";
        } else if (worldguard.getDescription().getVersion().startsWith("6.2")) {
            version = "6_2";
        } else {
            version = "7";
        }
        try {
            final Class<?> wgClass = Class.forName("net.alex9849.adapters.WorldGuard" + version);
            if (WorldGuardInterface.class.isAssignableFrom(wgClass)) {
                this.worldGuardInterface = (WorldGuardInterface) wgClass.newInstance();
            }
            getLogger().log(Level.INFO, "Using WorldGuard" + version + " adapter");
        } catch (Exception e) {
            getLogger().log(Level.INFO, "Could not setup WorldGuard! (handler could not be loaded) Compatible WorldGuard versions: 6, 7");
            e.printStackTrace();
        }

        return worldguard != null;
    }

    private boolean setupWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

        if (!(plugin instanceof WorldEditPlugin)) {
            return false;
        }
        WorldEditPlugin worldedit = (WorldEditPlugin) plugin;
        String version = "notSupported";
        boolean hasFaWeHandler = false;

        if (worldedit.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
        }

        if (this.isFaWeInstalled() && hasFaWeHandler) {
            version = version + "FaWe";
        }

        try {
            final Class<?> weClass = Class.forName("net.alex9849.adapters.WorldEdit" + version);
            if (WorldEditInterface.class.isAssignableFrom(weClass)) {
                this.worldEditInterface = (WorldEditInterface) weClass.newInstance();
            }
            getLogger().log(Level.INFO, "Using WorldEdit" + version + " adapter");
        } catch (Exception e) {
            getLogger().log(Level.INFO, "Could not setup WorldEdit! (handler could not be loaded) Compatible WorldEdit versions: 6, 7");
            e.printStackTrace();
        }


        return worldedit != null;
    }

    private void setupServerVersionFactories() {
        String classVersion = "";
        Version serverVersion = Version.fromString(Bukkit.getServer().getVersion());
        Version v1p12 = new Version(1, 12);
        Version v1p13 = new Version(1, 13);
        Version v1p14 = new Version(1, 14);
        Version v1p20 = new Version(1, 20);
        if (serverVersion.equals(v1p12) || (serverVersion.biggerThan(v1p12) && v1p13.biggerThan(serverVersion))) {
            classVersion = "112";
            getLogger().log(Level.INFO, "Using MC 1.12 adapter");
        } else if (serverVersion.equals(v1p13) || (serverVersion.biggerThan(v1p13) && v1p13.biggerThan(serverVersion))) {
            classVersion = "113";
            getLogger().log(Level.INFO, "Using MC 1.13 adapter");
        } else if (serverVersion.equals(v1p14) || (serverVersion.biggerThan(v1p14) && v1p20.biggerThan(serverVersion))) {
            classVersion = "114";
            getLogger().log(Level.INFO, "Using MC 1.14 adapter");
        } else {
            classVersion = "120";
            getLogger().log(Level.INFO, "Using MC 1.20 adapter");
        }

        try {
            Class<?> signDataFactoryClass = Class.forName("net.alex9849.arm.signs.SignDataFactory" + classVersion);
            if (SignDataFactory.class.isAssignableFrom(signDataFactoryClass)) {
                this.signDataFactory = (SignDataFactory) signDataFactoryClass.newInstance();
            }
            Class<?> materialDataFactoryClass = Class.forName("net.alex9849.arm.util.MaterialFinder" + classVersion);
            if (AbstractMaterialFinder.class.isAssignableFrom(materialDataFactoryClass)) {
                this.materialFinder = (AbstractMaterialFinder) materialDataFactoryClass.newInstance();
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not setup server version adapters! (Is your server compatible? Compatible versions: 1.12, 1.13, 1.14, 1.20)");
        }

    }

    private void loadAutoPrice() {
        Locale locale;
        String languageTag = getConfig().getString("PriceFormatting.locale");
        try {
            locale = Locale.forLanguageTag(languageTag);
        } catch (NullPointerException e) {
            locale = Locale.getDefault();
            getLogger().log(Level.WARNING, "Could not find language-Tag " + languageTag + "! Using " + locale + " now!");
        }
        NumberFormat priceFormatter = NumberFormat.getInstance(locale);
        priceFormatter.setMinimumFractionDigits(getConfig().getInt("PriceFormatting.minimumFractionDigits"));
        priceFormatter.setMaximumFractionDigits(getConfig().getInt("PriceFormatting.maximumFractionDigits"));
        priceFormatter.setMinimumIntegerDigits(getConfig().getInt("PriceFormatting.minimumIntegerDigits"));
        priceFormatter.setGroupingUsed(true);
        Price.setPriceFormatter(priceFormatter);

        if (getConfig().getConfigurationSection("AutoPrice") != null) {
            AutoPrice.loadAutoprices(getConfig().getConfigurationSection("AutoPrice"));
        }
        if (getConfig().getConfigurationSection("DefaultAutoprice") != null) {
            AutoPrice.loadDefaultAutoPrice(getConfig().getConfigurationSection("DefaultAutoprice"));
        }
    }

    private void loadGUI() {
        FileConfiguration pluginConf = getConfig();
        GuiConstants.setRegionOwnerItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.RegionOwnerItem")), materialFinder.getGuiRegionOwnerItem()));
        GuiConstants.setRegionMemberItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.RegionMemberItem")), materialFinder.getGuiRegionMemberItem()));
        GuiConstants.setRegionFinderItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.RegionFinderItem")), materialFinder.getGuiRegionFinderItem()));
        GuiConstants.setGoBackItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.GoBackItem")), materialFinder.getGuiGoBackItem()));
        GuiConstants.setWarningYesItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.WarningYesItem")), materialFinder.getGuiWarningYesItem()));
        GuiConstants.setWarningNoItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.WarningNoItem")), materialFinder.getGuiWarningNoItem()));
        GuiConstants.setTpItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.TPItem")), materialFinder.getGuiTpItem()));
        GuiConstants.setSellRegionItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.SellRegionItem")), materialFinder.getGuiSellRegionItem()));
        GuiConstants.setResetItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.ResetItem")), materialFinder.getGuiResetItem()));
        GuiConstants.setExtendItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.ExtendItem")), materialFinder.getGuiExtendItem()));
        GuiConstants.setInfoItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.InfoItem")), materialFinder.getGuiInfoItem()));
        GuiConstants.setPromoteMemberToOwnerItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.PromoteMemberToOwnerItem")), materialFinder.getGuiPromoteMemberToOwnerItem()));
        GuiConstants.setRemoveMemberItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.RemoveMemberItem")), materialFinder.getGuiRemoveMemberItem()));
        GuiConstants.setFillItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FillItem")), materialFinder.getGuiFillItem()));
        GuiConstants.setContractItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.ContractItem")), materialFinder.getGuiContractItem()));
        GuiConstants.setSubregionItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.SubRegionItem")), materialFinder.getGuiSubregionItem()));
        GuiConstants.setDeleteItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.DeleteItem")), materialFinder.getGuiDeleteItem()));
        GuiConstants.setTeleportToSignItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.TeleportToSignItem")), materialFinder.getGuiTeleportToSignItem()));
        GuiConstants.setTeleportToRegionItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.TeleportToRegionItem")), materialFinder.getGuiTeleportToRegionItem()));
        GuiConstants.setNextPageItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.NextPageItem")), materialFinder.getGuiNextPageItem()));
        GuiConstants.setPrevPageItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.PrevPageItem")), materialFinder.getGuiPrevPageItem()));
        GuiConstants.setHotelSettingItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.HotelSettingItem")), materialFinder.getGuiHotelSettingItem()));
        GuiConstants.setUnsellItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.UnsellItem")), materialFinder.getGuiUnsellItem()));
        GuiConstants.setFlageditorItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlageditorItem")), materialFinder.getGuiFlageditorItem()));
        GuiConstants.setFlagItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagItem")), materialFinder.getGuiFlagItem()));
        GuiConstants.setFlagSettingSelectedItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsSelectedItem")), materialFinder.getGuiFlagSettingSelectedItem()));
        GuiConstants.setFlagSettingNotSelectedItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsNotSelectedItem")), materialFinder.getGuiFlagSettingNotSelectedItem()));
        GuiConstants.setFlagGroupSelectedItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupSelectedItem")), materialFinder.getGuiFlagGroupSelectedItem()));
        GuiConstants.setFlagGroupNotSelectedItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupNotSelectedItem")), materialFinder.getGuiFlagGroupNotSelectedItem()));
        GuiConstants.setFlagRemoveItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagRemoveItem")), materialFinder.getGuiFlagRemoveItem()));
        GuiConstants.setFlagUserInputItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlagUserInputItem")), materialFinder.getGuiFlagUserInputItem()));
        GuiConstants.setFlageditorResetItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.FlageditorResetItem")), materialFinder.getGuiFlageditorResetItem()));
        GuiConstants.setEntityLimitGroupItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.EntityLimitGroupItem")), materialFinder.getEntityLimitGroupItem()));
        GuiConstants.setRegionFinderSelltypeSelectorItem(materialFinder.firstNonNull(materialFinder.getMaterial(pluginConf.getString("GUI.RegionFinderSellTypeSelectorItem")), materialFinder.getRegionFinderSelltypeSelectorItem()));
        GuiConstants.setPlayerHeadItem(materialFinder.getPlayerHead());
    }

    private void loadLimits() {
        this.limitGroupManager = new LimitGroupManager();
        ConfigurationSection limitsection = getConfig().getConfigurationSection("Limits");
        if (limitsection != null) {
            this.limitGroupManager.load(limitsection);
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
        HashMap<String, Set<String>> blackListedRegions = new HashMap<>();
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
                WGRegion wgRegion = this.getWorldGuardInterface().getRegion(world, regionName);
                if (wgRegion == null) {
                    continue;
                }
                blackListedRegions.putIfAbsent(worldName, new HashSet<>());
                Set<String> blackListedRegionsForWorld = blackListedRegions.get(worldName);
                blackListedRegionsForWorld.add(regionName);
            }
        }
        SignLinkMode.setBlacklistedRegions(blackListedRegions);
    }

    private void generateConfigs() {
        YamlFileManager.writeResourceToDisc(new File(this.getDataFolder() + "/config.yml"), this.getResource("config.yml"));
        this.reloadConfig();
        EntityLimitGroupManager.writeResourceToDisc(new File(this.getDataFolder() + "/entitylimits.yml"), getResource("entitylimits.yml"));
        RegionKindManager.writeResourceToDisc(new File(this.getDataFolder() + "/regionkinds.yml"), getResource("regionkinds.yml"));
        RegionManager.writeResourceToDisc(new File(this.getDataFolder() + "/regions.yml"), getResource("regions.yml"));
        PresetPatternManager.writeResourceToDisc(new File(this.getDataFolder() + "/presets.yml"), getResource("presets.yml"));
        FlagGroupManager.writeResourceToDisc(new File(this.getDataFolder() + "/flaggroups.yml"), getResource("flaggroups.yml"));
        RegionKindGroupManager.writeResourceToDisc(new File(this.getDataFolder() + "/regionkindgroups.yml"), getResource("regionkindgroups.yml"));
    }


    /*###############################
    ############ Getter #############
    ###############################*/

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

    public RegionKindManager getRegionKindManager() {
        return this.regionKindManager;
    }

    public EntityLimitGroupManager getEntityLimitGroupManager() {
        return this.entityLimitGroupManager;
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }

    public RegionKindGroupManager getRegionKindGroupManager() {
        return this.regionKindGroupManager;
    }

    public LimitGroupManager getLimitGroupManager() {
        return this.limitGroupManager;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public WorldGuardInterface getWorldGuardInterface() {
        return this.worldGuardInterface;
    }

    public WorldEditInterface getWorldEditInterface() {
        return this.worldEditInterface;
    }

    public net.milkbowl.vault.permission.Permission getVaultPerms() {
        return this.vaultPerms;
    }

    public Economy getEcon() {
        return this.econ;
    }

    public AbstractMaterialFinder getMaterialFinder() {
        return this.materialFinder;
    }


    /*#####################################
    ############# Other stuff #############
    #####################################*/

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("arm")) {
            return true;
        }
        try {
            if (args.length >= 1) {
                return this.commandHandler.executeCommand(sender,
                        Messages.getStringList(Arrays.asList(args), x -> x, " "), commandsLabel);
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
        } catch (NoPermissionException e) {
            sender.sendMessage(Messages.PREFIX + e.getMessage());
            return true;
        }
    }

}

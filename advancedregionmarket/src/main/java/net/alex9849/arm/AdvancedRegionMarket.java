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
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.flaggroups.FlagGroupManager;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.handler.listener.*;
import net.alex9849.arm.inactivityexpiration.InactivityExpirationGroup;
import net.alex9849.arm.inactivityexpiration.PlayerInactivityGroupMapper;
import net.alex9849.arm.limitgroups.LimitGroupManager;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPatternManager;
import net.alex9849.arm.presets.commands.MaxRentTimeCommand;
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
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.arm.util.YamlFileManager;
import net.alex9849.inter.WGRegion;
import net.alex9849.inter.WorldEditInterface;
import net.alex9849.inter.WorldGuardInterface;
import net.alex9849.pluginstats.client.Analytics;
import net.alex9849.signs.SignDataFactory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
        //This is a workaround to make shure that this plugin is loaded after the last world has been loaded.
        boolean doStartupWorkaround = false;
        List<String> softdependCheckPlugins = Arrays.asList("MultiWorld", "Multiverse-Core");
        for(String pluginName : softdependCheckPlugins) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            doStartupWorkaround |= plugin != null && !plugin.isEnabled();
        }
        if(doStartupWorkaround) {
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
        setupSignDataFactory();

        File schematicdic = new File(getDataFolder() + "/schematics");
        if (!schematicdic.exists()) {
            schematicdic.mkdirs();
        }

        this.generateConfigs();
        Updater.updateConfigs();

        //TODO get locale from config
        String localeString = getConfig().getString("Other.Language");
        Messages.MessageLocale messageLocale = Messages.MessageLocale.byCode(localeString);
        if(messageLocale == null) {
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
        commands.add(new AddMemberCommand());
        commands.add(new ApplyPresetCommand());
        commands.add(new SetInactivityResetCommand());
        commands.add(new DeleteCommand());
        commands.add(new AddCommand());
        commands.add(new SetAutoRestoreCommand());
        commands.add(new ExtendCommand());
        commands.add(new RegionfinderCommand());
        commands.add(new GuiCommand());
        commands.add(new HelpCommand(this.commandHandler, Messages.HELP_HEADLINE, betweencmds, Permission.ARM_HELP));
        commands.add(new SetHotelCommand());
        commands.add(new InfoCommand());
        commands.add(new LimitCommand());
        commands.add(new OfferCommand());
        commands.add(new CreateBackupCommand());
        commands.add(new RestoreBackupCommand());
        commands.add(new ListBackupsCommand());
        commands.add(new RegionstatsCommand());
        commands.add(new ReloadCommand());
        commands.add(new RemoveMemberCommand());
        commands.add(new RestoreCommand());
        commands.add(new ResetCommand());
        commands.add(new SetOwnerCommand());
        commands.add(new SetRegionKind());
        commands.add(new SetPaybackPercentageCommand());
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
        commands.add(new SetMaxMembersCommand());
        commands.add(new SetPriceCommand());
        commands.add(new SetIsUserRestorableCommand());
        commands.add(new ListAutoPricesCommand());
        commands.add(new FlageditorCommand());
        commands.add(new SetFlaggroupCommand());
        commands.add(new SignLinkModeCommand());
        commands.add(new SetEntityLimitCommand());
        commands.add(new SetLandLord());

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
        commands.add(new CommandSplitter("entitylimit", entityLimtUsage, Permission.ADMIN_ENTITYLIMIT_HELP, Messages.ENTITYLIMIT_HELP_HEADLINE, entityLimitCommands));

        List<String> regionKindUsage = new ArrayList<>(Arrays.asList("regionkind [SETTING]", "regionkind help"));
        List<BasicArmCommand> regionKindCommands = new ArrayList<>();
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.CreateCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.DeleteCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.ListCommand());
        regionKindCommands.add(new SetDisplayInRegionfinderCommand());
        regionKindCommands.add(new SetDisplayInLimitsCommand());
        regionKindCommands.add(new SetItemCommand());
        regionKindCommands.add(new AddLoreLineCommand());
        regionKindCommands.add(new net.alex9849.arm.regionkind.regionkindcommands.InfoCommand());
        regionKindCommands.add(new RemoveLoreLineCommand());
        regionKindCommands.add(new SetDisplayNameCommand());
        commands.add(new CommandSplitter("regionkind", regionKindUsage, Permission.REGIONKIND_HELP, Messages.REGIONKIND_HELP_HEADLINE, regionKindCommands));

        List<String> regionkindGroupUsage = Arrays.asList("regionkindgroup [SETTING]", "regionkindgroup help");
        List<BasicArmCommand> regionkindGroupCommands = new ArrayList<>();
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.CreateCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.DeleteCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.InfoCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.ListCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.SetDisplayInLimitsCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.SetDisplayNameCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.AddRegionkindCommand());
        regionkindGroupCommands.add(new net.alex9849.arm.regionkind.regionkindgroupcommands.RemoveRegionkindCommand());
        commands.add(new CommandSplitter("regionkindgroup", regionkindGroupUsage, Permission.REGIONKINDGROUP_HELP, Messages.REGIONKINDGROUP_HELP_HEADLINE, regionkindGroupCommands));

        List<String> subRegionUsage = new ArrayList<>(Arrays.asList("subregion [SETTING]", "subregion help"));
        List<BasicArmCommand> subRegionCommands = new ArrayList<>();
        subRegionCommands.add(new ToolCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.CreateCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.SetHotelCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.TPCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.RestoreCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.UnsellCommand());
        subRegionCommands.add(new net.alex9849.arm.subregions.commands.DeleteCommand());
        commands.add(new CommandSplitter("subregion", subRegionUsage, Permission.SUBREGION_HELP, Messages.SUBREGION_HELP_HEADLINE, subRegionCommands));

        List<String> sellPresetUsage = new ArrayList<>(Arrays.asList("sellpreset [SETTING]", "sellpreset help"));
        List<BasicArmCommand> sellPresetCommands = new ArrayList<>();
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new PaybackPercentageCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.SELLPRESET));
        sellPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.SELLPRESET));
        commands.add(new CommandSplitter("sellpreset", sellPresetUsage, Permission.ADMIN_PRESET_HELP,
                Messages.SELLPRESET_HELP_HEADLINE, sellPresetCommands));

        List<String> constractPresetUsage = new ArrayList<>(Arrays.asList("contractpreset [SETTING]", "contractpreset help"));
        List<BasicArmCommand> contractPresetCommands = new ArrayList<>();
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ExtendTimeCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new PaybackPercentageCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.CONTRACTPRESET));
        contractPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.CONTRACTPRESET));
        commands.add(new CommandSplitter("contractpreset", constractPresetUsage, Permission.ADMIN_PRESET_HELP,
                Messages.CONTRACTPRESET_HELP_HEADLINE, contractPresetCommands));

        List<String> rentPresetUsage = new ArrayList<>(Arrays.asList("rentpreset [SETTING]", "rentpreset help"));
        List<BasicArmCommand> rentPresetCommands = new ArrayList<>();
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.InactivityResetResetCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ExtendTimeCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new MaxRentTimeCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AutoRestoreCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new PaybackPercentageCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.UserRestorableCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SetMaxMembersCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.RENTPRESET));
        rentPresetCommands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.RENTPRESET));
        commands.add(new CommandSplitter("rentpreset", rentPresetUsage, Permission.ADMIN_PRESET_HELP,
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

        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return false;
        }
        WorldEditPlugin worldedit = (WorldEditPlugin) plugin;
        String version = "notSupported";
        Boolean hasFaWeHandler = true;

        if (worldedit.getDescription().getVersion().startsWith("6.")) {
            version = "6";
        } else {
            version = "7";
            hasFaWeHandler = false;
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

    private void setupSignDataFactory() {
        String classVersion = "";
        String serverVersion = Bukkit.getServer().getVersion();
        if (serverVersion.equalsIgnoreCase("1.12") || serverVersion.contains("1.12")) {
            classVersion = "112";
            getLogger().log(Level.INFO, "Using MC 1.12 sign adapter");
        } else if (serverVersion.equalsIgnoreCase("1.13") || serverVersion.contains("1.13")) {
            classVersion = "113";
            getLogger().log(Level.INFO, "Using MC 1.13 sign adapter");
        } else {
            classVersion = "114";
            getLogger().log(Level.INFO, "Using MC 1.14 sign adapter");
        }

        try {
            Class<?> signDataFactoryClass = Class.forName("net.alex9849.signs.SignDataFactory" + classVersion);
            if (SignDataFactory.class.isAssignableFrom(signDataFactoryClass)) {
                this.signDataFactory = (SignDataFactory) signDataFactoryClass.newInstance();
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not setup SignDataFactory! (Is your server compatible? Compatible versions: 1.12, 1.13, 1.14)");
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
        Gui.setFlagItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagItem")));
        Gui.setFlagSettingSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsSelectedItem")));
        Gui.setFlagSettingNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagSettingsNotSelectedItem")));
        Gui.setFlagGroupSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupSelectedItem")));
        Gui.setFlagGroupNotSelectedItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagGroupNotSelectedItem")));
        Gui.setFlagRemoveItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagRemoveItem")));
        Gui.setFlagUserInputItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlagUserInputItem")));
        Gui.setFlageditorResetItem(MaterialFinder.getMaterial(pluginConf.getString("GUI.FlageditorResetItem")));
    }

    private void loadLimits() {
        this.limitGroupManager = new LimitGroupManager();
        ConfigurationSection limitsection = getConfig().getConfigurationSection("Limits");
        if(limitsection != null) {
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
                WGRegion wgRegion = this.getWorldGuardInterface().getRegion(world, regionName);
                if (wgRegion == null) {
                    continue;
                }
                wgRegions.add(wgRegion);
            }
        }
        SignLinkMode.setBlacklistedRegions(wgRegions);
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

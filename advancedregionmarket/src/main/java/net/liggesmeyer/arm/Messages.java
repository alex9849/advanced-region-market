package net.liggesmeyer.arm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Messages {

    private static YamlConfiguration config;
    public static String PREFIX = "";
    public static String REGION_BUYMESSAGE = "";
    public static String NOT_ENOUGHT_MONEY = "";
    public static String REGION_ALREADY_SOLD = "";
    public static String NO_PERMISSION = "";
    public static String WORLD_DOES_NOT_EXIST = "";
    public static String REGION_DOES_NOT_EXIST = "";
    public static String REGION_ADDED_TO_ARM = "";
    public static String SIGN_ADDED_TO_REGION = "";
    public static String SIGN_REMOVED_FROM_REGION = "";
    public static String PLEASE_USE_A_NUMBER_AS_PRICE = "";
    public static String REGION_REMOVED_FROM_ARM = "";
    public static String SELL_SIGN1 = "";
    public static String SELL_SIGN2 = "";
    public static String SELL_SIGN3 = "";
    public static String SELL_SIGN4 = "";
    public static String SOLD_SIGN1 = "";
    public static String SOLD_SIGN2 = "";
    public static String SOLD_SIGN3 = "";
    public static String SOLD_SIGN4 = "";
    public static String CURRENCY = "";
    public static String REGION_KIND_SET = "";
    public static String REGION_KIND_NOT_EXIST = "";
    public static String REGION_KIND_REGION_NOT_EXIST = "";
    public static String COMMAND_ONLY_INGAME = "";
    public static String REGION_INFO = "";
    public static String REGION_INFO_ID = "";
    public static String REGION_INFO_PRICE = "";
    public static String REGION_INFO_TYPE = "";
    public static String REGION_INFO_OWNER = "";
    public static String REGION_INFO_MEMBERS = "";
    public static String REGION_INFO_SOLD = "";
    public static String REGION_INFO_AUTORESET = "";
    public static String REGION_INFO_MAX_RENT_TIME = "";
    public static String REGION_INFO_EXTEND_PER_CLICK = "";
    public static String REGION_INFO_REMAINING_TIME = "";
    public static String REGION_INFO_HOTEL = "";
    public static String GUI_MAIN_MENU_NAME = "";
    public static String GUI_GO_BACK = "";
    public static String GUI_MY_OWN_REGIONS = "";
    public static String GUI_MY_MEMBER_REGIONS = "";
    public static String GUI_SEARCH_FREE_REGION = "";
    public static String GUI_OWN_REGIONS_MENU_NAME = "";
    public static String GUI_MEMBERS_BUTTON = "";
    public static String GUI_SHOW_INFOS_BUTTON = "";
    public static String GUI_TELEPORT_TO_REGION_BUTTON = "";
    public static String GUI_REGION_FINDER_MENU_NAME = "";
    public static String GUI_MEMBER_LIST_MENU_NAME = "";
    public static String GUI_MAKE_OWNER_BUTTON = "";
    public static String GUI_REMOVE_MEMBER_BUTTON = "";
    public static String GUI_MAKE_OWNER_WARNING_NAME = "";
    public static String GUI_YES = "";
    public static String GUI_NO = "";
    public static String GUI_MEMBER_REGIONS_MENU_NAME = "";
    public static String REGION_TELEPORT_MESSAGE = "";
    public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = "";
    public static String REGIONKINDS = "";
    public static String NO_FREE_REGION_WITH_THIS_KIND = "";
    public static String NO_PERMISSION_TO_SEARCH_THIS_KIND = "";
    public static String REGIONKIND_DOES_NOT_EXIST = "";
    public static String RESET_REGION_RESETING_BLOCKS = "";
    public static String REGION_NOW_AVIABLE = "";
    public static String HAVE_TO_STAND_ON_REGION_TO_SHOW_INFO = "";
    public static String REGION_ADD_MEMBER_NOT_ONLINE = "";
    public static String REGION_ADD_MEMBER_DO_NOT_OWN = "";
    public static String REGION_ADD_MEMBER_ADDED = "";
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER = "";
    public static String REGION_REMOVE_MEMBER_REMOVED = "";
    public static String REGION_REMOVE_MEMBER_DO_NOT_OWN = "";
    public static String GUI_RESET_REGION_BUTTON = "";
    public static String GUI_RESET_REGION_WARNING_NAME = "";
    public static String RESET_COMPLETE = "";
    public static String RESET_REGION_COOLDOWN_ERROR = "";
    public static String GUI_TAKEOVER_MENU_NAME = "";
    public static String REGION_TRANSFER_COMPLETE_MESSAGE = "";
    public static String GUI_CLOSE = "";
    public static String MEMBERLIST_INFO = "";
    public static String RENT_SIGN1 = "";
    public static String RENT_SIGN2 = "";
    public static String RENT_SIGN3 = "";
    public static String RENT_SIGN4 = "";
    public static String RENTED_SIGN1 = "";
    public static String RENTED_SIGN2 = "";
    public static String RENTED_SIGN3 = "";
    public static String RENTED_SIGN4 = "";
    public static String RENT_EXTEND_MESSAGE = "";
    public static String RENT_EXTEND_ERROR = "";
    public static String GUI_EXTEND_BUTTON = "";
    public static String COMPLETE = "";
    public static String RESET_IN_PERCENT = "";
    public static String LOADING_SCHEMATIC = "";
    public static String LOADING_SCHEMATIC_COMPLETE = "";
    public static String REGION_BUY_OUT_OF_LIMIT = "";
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE = "";
    public static String UNLIMITED = "";
    public static String GUI_USER_SELL_BUTTON = "";
    public static String GUI_USER_SELL_WARNING = "";
    public static String LIMIT_INFO_TOP = "";
    public static String LIMIT_INFO = "";
    public static String GUI_MY_LIMITS_BUTTON = "";
    public static String GUI_MEMBER_INFO_ITEM = "";
    public static String REGION_IS_NOT_A_RENTREGION = "Region is not a rentregion!";
    public static String REGION_NOT_OWN = "You do not own this region!";
    public static String REGION_NOT_SOLD = "Region not sold!";
    public static String PRESET_REMOVED = "Preset removed!";
    public static String PRESET_SET = "Preset set!";
    public static String REGION_INFO_DO_BLOCK_RESET = "DoBlockReset: ";
    public static String PRESET_SAVED = "Preset saved!";
    public static String PRESET_ALREADY_EXISTS = "A preset with this name already exists!";
    public static String PRESET_PLAYER_DONT_HAS_PRESET = "You do not have a preset!";
    public static String PRESET_DELETED = "Preset deleted!";
    public static String PRESET_NOT_FOUND = "No preset with this name found!";
    public static String PRESET_LOADED = "Preset loaded!";
    public static String LIMIT_INFO_TOTAL = "Total";
    public static String GUI_REGION_ITEM_NAME = "%regionid% (%regionkind%)";
    public static String GUI_REGIONFINDER_REGIONKIND_NAME = "%regionkind%";
    public static String RENTREGION_EXPIRATION_WARNING = "[WARNING] This RentRegion(s) will expire soon: ";
    public static String CONTRACT_SIGN1 = "&2Contract";
    public static String CONTRACT_SIGN2 = "&2available";
    public static String CONTRACT_SIGN3 = "%regionid%";
    public static String CONTRACT_SIGN4 = "%price%%currency%/%extend%";
    public static String CONTRACT_SOLD_SIGN1 = "&4Contract in use";
    public static String CONTRACT_SOLD_SIGN2 = "%regionid%/%owner%";
    public static String CONTRACT_SOLD_SIGN3 = "%price%%currency%/%extend%";
    public static String CONTRACT_SOLD_SIGN4 = "%remaining%";
    public static String CONTRACT_REGION_EXTENDED = "&aYour contract region %regionid% has been extended for %extend%. (For %price%%currency%.) ";
    public static String GUI_CONTRACT_ITEM = "&aManage contract";
    public static String REGION_INFO_TERMINATED = "&6Terminated: ";
    public static String REGION_INFO_AUTO_EXTEND_TIME = "&6Extend time: ";
    public static String REGION_INFO_NEXT_EXTEND_REMAINING_TIME = "&6Next extend in: ";
    public static String CONTRACT_REGION_CHANGE_TERMINATED = "&6The contract of &a%regionid% &6has been set to %statuslong%";
    public static String CONTRACT_REGION_STATUS_ACTIVE_LONG = "&aActive&6! Next Extension in %remaining%";
    public static String CONTRACT_REGION_STATUS_ACTIVE = "&aActive";
    public static String CONTRACT_REGION_STATUS_TERMINATED_LONG = "&4Terminated&6! It will be resetted in %remaining%";
    public static String CONTRACT_REGION_STATUS_TERMINATED = "&4Terminated";
    public static String REGION_IS_NOT_A_CONTRACT_REGION = "&4Region is not a contractregion!";
    public static String GUI_OWNER_MEMBER_INFO_ITEM = "";
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE = "Member not online!";
    public static String REGION_TRANSFER_LIMIT_ERROR = "Transfer aborted! (Region would exceed players limit)";
    public static String TIME_SECONDS = "s";
    public static String TIME_MINUTES = "m";
    public static String TIME_HOURS = "h";
    public static String TIME_DAYS = "d";
    public static String TIME_SECONDS_SHORT = "";
    public static String TIME_MINUTES_SHORT = "";
    public static String TIME_HOURS_SHORT = "";
    public static String TIME_DAYS_SHORT = "";

    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_RESET_REGION_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_TAKEOVER_ITEM_LORE = new LinkedList<>();
    public static List<String> MEMBERLIST_INFO_LORE = new LinkedList<>();
    public static List<String> GUI_EXTEND_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_RENT_REGION_LORE = new LinkedList<>();
    public static List<String> GUI_USER_SELL_BUTTON_LORE = new LinkedList<>();
    public static List<String> GUI_MEMBER_INFO_LORE = new LinkedList<>();
    public static List<String> GUI_CONTRACT_ITEM_LORE = new LinkedList<>();
    public static List<String> GUI_CONTRACT_REGION_LORE = new LinkedList<>();
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE = new LinkedList<>();


    public static void read(){
        Messages.generatedefaultConfig();
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Configuration config = YamlConfiguration.loadConfiguration(messagesconfigdic);



        PREFIX = config.getString("Messages.Prefix") + " ";
        REGION_BUYMESSAGE = config.getString("Messages.Buymessage");
        NOT_ENOUGHT_MONEY = config.getString("Messages.NotEnoughtMoney");
        REGION_ALREADY_SOLD = config.getString("Messages.RegionAlreadySold");
        SIGN_REMOVED_FROM_REGION = config.getString("Messages.SignRemovedFromRegion");
        NO_PERMISSION = config.getString("Messages.NoPermission");
        WORLD_DOES_NOT_EXIST = config.getString("Messages.WorldDoesNotExist");
        REGION_DOES_NOT_EXIST = config.getString("Messages.RegionDoesNotExist");
        REGION_ADDED_TO_ARM = config.getString("Messages.RegionAddedToARM");
        SIGN_ADDED_TO_REGION = config.getString("Messages.SignAddedToRegion");
        PLEASE_USE_A_NUMBER_AS_PRICE = config.getString("Messages.UseANumberAsPrice");
        REGION_REMOVED_FROM_ARM = config.getString("Messages.RegionRemovedFromARM");
        SELL_SIGN1 = config.getString("Messages.SellSign1");
        SELL_SIGN2 = config.getString("Messages.SellSign2");
        SELL_SIGN3 = config.getString("Messages.SellSign3");
        SELL_SIGN4 = config.getString("Messages.SellSign4");
        SOLD_SIGN1 = config.getString("Messages.SoldSign1");
        SOLD_SIGN2 = config.getString("Messages.SoldSign2");
        SOLD_SIGN3 = config.getString("Messages.SoldSign3");
        SOLD_SIGN4 = config.getString("Messages.SoldSign4");
        CURRENCY = config.getString("Messages.Currency");
        REGION_KIND_SET = config.getString("Messages.RegionKindSet");
        REGION_KIND_NOT_EXIST = config.getString("Messages.RegionKindNotExist");
        REGION_KIND_REGION_NOT_EXIST = config.getString("Messages.RegionKindRegionNotExist");
        COMMAND_ONLY_INGAME = config.getString("Messages.CommandOnlyIngame");
        REGION_INFO = config.getString("Messages.RegionInfo");
        REGION_INFO_ID = config.getString("Messages.RegionInfoID");
        REGION_INFO_PRICE = config.getString("Messages.RegionInfoPrice");
        REGION_INFO_TYPE = config.getString("Messages.RegionInfoType");
        REGION_INFO_OWNER = config.getString("Messages.RegionInfoOwner");
        REGION_INFO_MEMBERS = config.getString("Messages.RegionInfoMembers");
        REGION_INFO_SOLD = config.getString("Messages.RegionInfoSold");
        REGION_INFO_AUTORESET = config.getString("Messages.RegionInfoAutoreset");
        GUI_MAIN_MENU_NAME = config.getString("Messages.GUIMainMenuName");
        GUI_GO_BACK = config.getString("Messages.GUIGoBack");
        GUI_MY_OWN_REGIONS = config.getString("Messages.GUIMyOwnRegions");
        GUI_MY_MEMBER_REGIONS = config.getString("Messages.GUIMyMemberRegions");
        GUI_SEARCH_FREE_REGION = config.getString("Messages.GUISearchFreeRegion");
        GUI_OWN_REGIONS_MENU_NAME = config.getString("Messages.GUIOwnRegionsMenuName");
        GUI_MEMBERS_BUTTON = config.getString("Messages.GUIMembersButton");
        GUI_TELEPORT_TO_REGION_BUTTON = config.getString("Messages.GUITeleportToRegionButton");
        GUI_SHOW_INFOS_BUTTON = config.getString("Messages.GUIShowInfosButton");
        GUI_REGION_FINDER_MENU_NAME = config.getString("Messages.GUIRegionFinderMenuName");
        GUI_MEMBER_LIST_MENU_NAME = config.getString("Messages.GUIMemberListMenuName");
        GUI_MAKE_OWNER_BUTTON = config.getString("Messages.GUIMakeOwnerButton");
        GUI_REMOVE_MEMBER_BUTTON = config.getString("Messages.GUIRemoveMemberButton");
        GUI_MAKE_OWNER_WARNING_NAME = config.getString("Messages.GUIMakeOwnerWarningName");
        GUI_YES = config.getString("Messages.GUIWarningYes");
        GUI_NO = config.getString("Messages.GUIWarningNo");
        GUI_MEMBER_REGIONS_MENU_NAME = config.getString("Messages.GUIMemberRegionsMenuName");
        REGION_TELEPORT_MESSAGE = config.getString("Messages.RegionTeleportMessage");
        NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = config.getString("Messages.NoPermissionsToBuyThisKindOfRegion");
        REGIONKINDS = config.getString("Messages.RegionKinds");
        NO_FREE_REGION_WITH_THIS_KIND = config.getString("Messages.NoFreeRegionWithThisKind");
        NO_PERMISSION_TO_SEARCH_THIS_KIND = config.getString("Messages.NoPermissionToSearchKind");
        REGIONKIND_DOES_NOT_EXIST = config.getString("Messages.RegionkindDoesNotExist");
        RESET_REGION_RESETING_BLOCKS = config.getString("Messages.ResetRegionResetingBlocks");
        REGION_NOW_AVIABLE = config.getString("Messages.RegionNowAviable");
        HAVE_TO_STAND_ON_REGION_TO_SHOW_INFO = config.getString("Messages.HaveToStandOnRegionToShowInfo");
        REGION_ADD_MEMBER_NOT_ONLINE = config.getString("Messages.RegionAddMemberNotOnline");
        REGION_ADD_MEMBER_DO_NOT_OWN = config.getString("Messages.RegionAddMemberDoNotOwn");
        REGION_ADD_MEMBER_ADDED = config.getString("Messages.RegionAddMemberAdded");
        REGION_REMOVE_MEMBER_NOT_A_MEMBER = config.getString("Messages.RegionRemoveMemberNotAMember");
        REGION_REMOVE_MEMBER_REMOVED = config.getString("Messages.RegionRemoveMemberRemoved");
        REGION_REMOVE_MEMBER_DO_NOT_OWN = config.getString("Messages.RegionRemoveMemberDoNotOwn");
        GUI_RESET_REGION_BUTTON = config.getString("Messages.GUIResetRegionButton");
        GUI_RESET_REGION_WARNING_NAME = config.getString("Messages.GUIResetRegionWarningName");
        RESET_COMPLETE = config.getString("Messages.ResetComplete");
        RESET_IN_PERCENT = config.getString("Messages.ResetPerCentComplete");
        COMPLETE = config.getString("Messages.Complete");
        LOADING_SCHEMATIC = config.getString("Messages.LoadingSchematic");
        LOADING_SCHEMATIC_COMPLETE = config.getString("Messages.LoadingSchematicComplete");
        RESET_REGION_COOLDOWN_ERROR = config.getString("Messages.ResetRegionCooldownError");
        GUI_TAKEOVER_MENU_NAME = config.getString("Messages.GUIRegionTakeOverMenuName");
        REGION_TRANSFER_COMPLETE_MESSAGE = config.getString("Messages.RegionTransferCompleteMessage");
        GUI_CLOSE = config.getString("Messages.GUICloseWindow");
        GUI_OWNER_MEMBER_INFO_ITEM = config.getString("Messages.OwnerMemberlistInfo");
        RENT_SIGN1 = config.getString("Messages.RentSign1");
        RENT_SIGN2 = config.getString("Messages.RentSign2");
        RENT_SIGN3 = config.getString("Messages.RentSign3");
        RENT_SIGN4 = config.getString("Messages.RentSign4");
        RENTED_SIGN1 = config.getString("Messages.RentedSign1");
        RENTED_SIGN2 = config.getString("Messages.RentedSign2");
        RENTED_SIGN3 = config.getString("Messages.RentedSign3");
        RENTED_SIGN4 = config.getString("Messages.RentedSign4");
        REGION_ERROR_CAN_NOT_BUILD_HERE = config.getString("Messages.RegionErrorCanNotBuildHere");
        RENT_EXTEND_MESSAGE = config.getString("Messages.RentExtendMessage");
        RENT_EXTEND_ERROR = config.getString("Messages.RentExtendError");
        REGION_INFO_HOTEL = config.getString("Messages.isHotel");
        GUI_EXTEND_BUTTON = config.getString("Messages.GUIExtendRentRegionButton");
        REGION_INFO_MAX_RENT_TIME = config.getString("Messages.RegionInfoMaxRentTime");
        REGION_INFO_EXTEND_PER_CLICK = config.getString("Messages.RegionInfoExtendPerClick");
        REGION_INFO_REMAINING_TIME = config.getString("Messages.RegionInfoRemainingTime");
        GUI_TELEPORT_TO_REGION_BUTTON_LORE = config.getStringList("Messages.GUITeleportToRegionButtonLore");
        GUI_MAKE_OWNER_BUTTON_LORE = config.getStringList("Messages.GUIMakeOwnerButtonLore");
        GUI_REMOVE_MEMBER_BUTTON_LORE = config.getStringList("Messages.GUIRemoveMemberButtonLore");
        GUI_RESET_REGION_BUTTON_LORE = config.getStringList("Messages.GUIResetRegionButtonLore");
        GUI_TAKEOVER_ITEM_LORE = config.getStringList("Messages.TakeOverItemLore");
        GUI_OWNER_MEMBER_INFO_LORE = config.getStringList("Messages.OwnerMemberlistInfoLore");
        GUI_EXTEND_BUTTON_LORE = config.getStringList("Messages.GUIExtendRentRegionButtonLore");
        GUI_RENT_REGION_LORE = config.getStringList("Messages.GUIRentRegionLore");
        GUI_USER_SELL_BUTTON_LORE = config.getStringList("Messages.GUIUserSellButtonLore");
        REGION_BUY_OUT_OF_LIMIT = config.getString("Messages.RegionBuyOutOfLimit");
        UNLIMITED = config.getString("Messages.Unlimited");
        GUI_USER_SELL_BUTTON = config.getString("Messages.GUIUserSellButton");
        GUI_USER_SELL_WARNING = config.getString("Messages.GUIUserSellWarning");
        LIMIT_INFO_TOP = config.getString("Messages.LimitInfoTop");
        LIMIT_INFO = config.getString("Messages.LimitInfo");
        GUI_MY_LIMITS_BUTTON = config.getString("Messages.GUILimitButton");
        GUI_MEMBER_INFO_ITEM = config.getString("Messages.MemberlistInfo");
        GUI_MEMBER_INFO_LORE = config.getStringList("Messages.MemberlistInfoLore");
        REGION_IS_NOT_A_RENTREGION = config.getString("Messages.RegionIsNotARentregion");
        REGION_NOT_OWN = config.getString("Messages.RegionNotOwn");
        REGION_NOT_SOLD = config.getString("Messages.RegionNotSold");
        PRESET_REMOVED = config.getString("Messages.PresetRemoved");
        PRESET_SET = config.getString("Messages.PresetSet");
        REGION_INFO_DO_BLOCK_RESET = config.getString("Messages.RegionInfoDoBlockReset");
        PRESET_SAVED = config.getString("Messages.PresetSaved");
        PRESET_ALREADY_EXISTS = config.getString("Messages.PresetAlreadyExists");
        PRESET_PLAYER_DONT_HAS_PRESET = config.getString("Messages.PresetPlayerDontHasPreset");
        PRESET_DELETED = config.getString("Messages.PresetDeleted");
        PRESET_NOT_FOUND = config.getString("Messages.PresetNotFound");
        PRESET_LOADED = config.getString("Messages.PresetLoaded");
        LIMIT_INFO_TOTAL = config.getString("Messages.LimitInfoTotal");
        GUI_REGION_ITEM_NAME = config.getString("Messages.GUIRegionItemName");
        GUI_REGIONFINDER_REGIONKIND_NAME = config.getString("Messages.GUIRegionFinderRegionKindName");
        RENTREGION_EXPIRATION_WARNING = config.getString("Messages.RentRegionExpirationWarning");
        CONTRACT_SIGN1 = config.getString("Messages.ContractSign1");
        CONTRACT_SIGN2 = config.getString("Messages.ContractSign2");
        CONTRACT_SIGN3 = config.getString("Messages.ContractSign3");
        CONTRACT_SIGN4 = config.getString("Messages.ContractSign4");
        CONTRACT_SOLD_SIGN1 = config.getString("Messages.ContractSoldSign1");
        CONTRACT_SOLD_SIGN2 = config.getString("Messages.ContractSoldSign2");
        CONTRACT_SOLD_SIGN3 = config.getString("Messages.ContractSoldSign3");
        CONTRACT_SOLD_SIGN4 = config.getString("Messages.ContractSoldSign4");
        CONTRACT_REGION_EXTENDED = config.getString("Messages.ContractRegionExtended");
        GUI_CONTRACT_ITEM = config.getString("Messages.GUIContractItem");
        REGION_INFO_TERMINATED = config.getString("Messages.RegionInfoTerminated");
        REGION_INFO_AUTO_EXTEND_TIME = config.getString("Messages.RegionInfoAutoExtendTime");
        REGION_INFO_NEXT_EXTEND_REMAINING_TIME = config.getString("Messages.RegionInfoRemainingTime");
        CONTRACT_REGION_CHANGE_TERMINATED = config.getString("Messages.ContractRegionChangeTerminated");
        CONTRACT_REGION_STATUS_ACTIVE_LONG = config.getString("Messages.ContractRegionStatusActiveLong");
        CONTRACT_REGION_STATUS_ACTIVE = config.getString("Messages.ContractRegionStatusActive");
        CONTRACT_REGION_STATUS_TERMINATED_LONG = config.getString("Messages.ContractRegionStatusTerminatedLong");
        CONTRACT_REGION_STATUS_TERMINATED = config.getString("Messages.ContractRegionStatusTerminated");
        GUI_CONTRACT_ITEM_LORE = config.getStringList("Messages.GUIContractItemLore");
        GUI_CONTRACT_REGION_LORE = config.getStringList("Messages.GUIContractItemRegionLore");
        REGION_IS_NOT_A_CONTRACT_REGION = config.getString("Messages.RegionIsNotAContractRegion");
        REGION_TRANSFER_MEMBER_NOT_ONLINE = config.getString("Messages.RegiontransferMemberNotOnline");
        REGION_TRANSFER_LIMIT_ERROR = config.getString("Messages.RegiontransferLimitError");
        TIME_SECONDS = config.getString("Messages.Seconds");
        TIME_MINUTES = config.getString("Messages.Minutes");
        TIME_HOURS = config.getString("Messages.Hours");
        TIME_DAYS = config.getString("Messages.Days");
        TIME_SECONDS_SHORT = config.getString("Messages.SecondsForShortCountDown");
        TIME_MINUTES_SHORT = config.getString("Messages.MinutesForShortCountDown");
        TIME_HOURS_SHORT = config.getString("Messages.HoursForShortCountDown");
        TIME_DAYS_SHORT = config.getString("Messages.DaysForShortCountDown");

        Messages.translateColorCodes();
    }

    private static void translateColorCodes(){
        PREFIX = ChatColor.translateAlternateColorCodes('&', PREFIX);
        REGION_BUYMESSAGE = ChatColor.translateAlternateColorCodes('&', REGION_BUYMESSAGE);
        NOT_ENOUGHT_MONEY = ChatColor.translateAlternateColorCodes('&', NOT_ENOUGHT_MONEY);
        REGION_ALREADY_SOLD = ChatColor.translateAlternateColorCodes('&', REGION_ALREADY_SOLD);
        NO_PERMISSION = ChatColor.translateAlternateColorCodes('&', NO_PERMISSION);
        WORLD_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', WORLD_DOES_NOT_EXIST);
        REGION_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGION_DOES_NOT_EXIST);
        REGION_ADDED_TO_ARM = ChatColor.translateAlternateColorCodes('&', REGION_ADDED_TO_ARM);
        SIGN_ADDED_TO_REGION = ChatColor.translateAlternateColorCodes('&', SIGN_ADDED_TO_REGION);
        PLEASE_USE_A_NUMBER_AS_PRICE = ChatColor.translateAlternateColorCodes('&', PLEASE_USE_A_NUMBER_AS_PRICE);
        REGION_REMOVED_FROM_ARM = ChatColor.translateAlternateColorCodes('&', REGION_REMOVED_FROM_ARM);
        SELL_SIGN1 = ChatColor.translateAlternateColorCodes('&', SELL_SIGN1);
        SELL_SIGN2 = ChatColor.translateAlternateColorCodes('&', SELL_SIGN2);
        SELL_SIGN3 = ChatColor.translateAlternateColorCodes('&', SELL_SIGN3);
        SELL_SIGN4 = ChatColor.translateAlternateColorCodes('&', SELL_SIGN4);
        SOLD_SIGN1 = ChatColor.translateAlternateColorCodes('&', SOLD_SIGN1);
        SOLD_SIGN2 = ChatColor.translateAlternateColorCodes('&', SOLD_SIGN2);
        SOLD_SIGN3 = ChatColor.translateAlternateColorCodes('&', SOLD_SIGN3);
        SOLD_SIGN4 = ChatColor.translateAlternateColorCodes('&', SOLD_SIGN4);
        CURRENCY = ChatColor.translateAlternateColorCodes('&', CURRENCY);
        REGION_KIND_SET = ChatColor.translateAlternateColorCodes('&', REGION_KIND_SET);
        REGION_KIND_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGION_KIND_NOT_EXIST);
        REGION_KIND_REGION_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGION_KIND_REGION_NOT_EXIST);
        REGION_INFO = ChatColor.translateAlternateColorCodes('&', REGION_INFO);
        REGION_INFO_ID = ChatColor.translateAlternateColorCodes('&', REGION_INFO_ID);
        REGION_INFO_PRICE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_PRICE);
        REGION_INFO_TYPE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_TYPE);
        REGION_INFO_OWNER = ChatColor.translateAlternateColorCodes('&', REGION_INFO_OWNER);
        REGION_INFO_MEMBERS = ChatColor.translateAlternateColorCodes('&', REGION_INFO_MEMBERS);
        COMMAND_ONLY_INGAME = ChatColor.translateAlternateColorCodes('&', COMMAND_ONLY_INGAME);
        REGION_INFO_SOLD = ChatColor.translateAlternateColorCodes('&', REGION_INFO_SOLD);
        REGION_INFO_AUTORESET = ChatColor.translateAlternateColorCodes('&', REGION_INFO_AUTORESET);
        GUI_MAIN_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MAIN_MENU_NAME);
        GUI_GO_BACK = ChatColor.translateAlternateColorCodes('&', GUI_GO_BACK);
        GUI_MY_OWN_REGIONS = ChatColor.translateAlternateColorCodes('&', GUI_MY_OWN_REGIONS);
        GUI_MY_MEMBER_REGIONS = ChatColor.translateAlternateColorCodes('&', GUI_MY_MEMBER_REGIONS);
        GUI_SEARCH_FREE_REGION = ChatColor.translateAlternateColorCodes('&', GUI_SEARCH_FREE_REGION);
        GUI_OWN_REGIONS_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_OWN_REGIONS_MENU_NAME);
        GUI_MEMBERS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_MEMBERS_BUTTON);
        GUI_TELEPORT_TO_REGION_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_REGION_BUTTON);
        GUI_SHOW_INFOS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_SHOW_INFOS_BUTTON);
        GUI_REGION_FINDER_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_REGION_FINDER_MENU_NAME);
        GUI_MEMBER_LIST_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_LIST_MENU_NAME);
        GUI_MAKE_OWNER_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_MAKE_OWNER_BUTTON);
        GUI_REMOVE_MEMBER_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_REMOVE_MEMBER_BUTTON);
        GUI_MAKE_OWNER_WARNING_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MAKE_OWNER_WARNING_NAME);
        GUI_YES = ChatColor.translateAlternateColorCodes('&', GUI_YES);
        GUI_NO = ChatColor.translateAlternateColorCodes('&', GUI_NO);
        GUI_MEMBER_REGIONS_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_REGIONS_MENU_NAME);
        SIGN_REMOVED_FROM_REGION = ChatColor.translateAlternateColorCodes('&', SIGN_REMOVED_FROM_REGION);
        REGION_TELEPORT_MESSAGE = ChatColor.translateAlternateColorCodes('&', REGION_TELEPORT_MESSAGE);
        NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = ChatColor.translateAlternateColorCodes('&', NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
        REGIONKINDS = ChatColor.translateAlternateColorCodes('&', REGIONKINDS);
        NO_FREE_REGION_WITH_THIS_KIND = ChatColor.translateAlternateColorCodes('&', NO_FREE_REGION_WITH_THIS_KIND);
        NO_PERMISSION_TO_SEARCH_THIS_KIND = ChatColor.translateAlternateColorCodes('&', NO_PERMISSION_TO_SEARCH_THIS_KIND);
        REGIONKIND_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGIONKIND_DOES_NOT_EXIST);
        RESET_REGION_RESETING_BLOCKS = ChatColor.translateAlternateColorCodes('&', RESET_REGION_RESETING_BLOCKS);
        REGION_NOW_AVIABLE = ChatColor.translateAlternateColorCodes('&', REGION_NOW_AVIABLE);
        HAVE_TO_STAND_ON_REGION_TO_SHOW_INFO = ChatColor.translateAlternateColorCodes('&', HAVE_TO_STAND_ON_REGION_TO_SHOW_INFO);
        REGION_ADD_MEMBER_NOT_ONLINE = ChatColor.translateAlternateColorCodes('&', REGION_ADD_MEMBER_NOT_ONLINE);
        REGION_ADD_MEMBER_DO_NOT_OWN = ChatColor.translateAlternateColorCodes('&', REGION_ADD_MEMBER_DO_NOT_OWN);
        REGION_ADD_MEMBER_ADDED = ChatColor.translateAlternateColorCodes('&', REGION_ADD_MEMBER_ADDED);
        REGION_REMOVE_MEMBER_NOT_A_MEMBER = ChatColor.translateAlternateColorCodes('&', REGION_REMOVE_MEMBER_NOT_A_MEMBER);
        REGION_REMOVE_MEMBER_REMOVED = ChatColor.translateAlternateColorCodes('&', REGION_REMOVE_MEMBER_REMOVED);
        REGION_REMOVE_MEMBER_DO_NOT_OWN = ChatColor.translateAlternateColorCodes('&', REGION_REMOVE_MEMBER_DO_NOT_OWN);
        GUI_RESET_REGION_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_BUTTON);
        GUI_RESET_REGION_WARNING_NAME = ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_WARNING_NAME);
        RESET_COMPLETE = ChatColor.translateAlternateColorCodes('&', RESET_COMPLETE);
        RESET_REGION_COOLDOWN_ERROR = ChatColor.translateAlternateColorCodes('&', RESET_REGION_COOLDOWN_ERROR);
        GUI_TAKEOVER_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_TAKEOVER_MENU_NAME);
        REGION_TRANSFER_COMPLETE_MESSAGE = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_COMPLETE_MESSAGE);
        GUI_CLOSE = ChatColor.translateAlternateColorCodes('&', GUI_CLOSE);
        MEMBERLIST_INFO = ChatColor.translateAlternateColorCodes('&', MEMBERLIST_INFO);
        RENT_SIGN1 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN1);
        RENT_SIGN2 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN2);
        RENT_SIGN3 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN3);
        RENT_SIGN4 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN4);
        RENTED_SIGN1 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN1);
        RENTED_SIGN2 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN2);
        RENTED_SIGN3 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN3);
        RENTED_SIGN4 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN4);
        RENT_EXTEND_MESSAGE = ChatColor.translateAlternateColorCodes('&', RENT_EXTEND_MESSAGE);
        RENT_EXTEND_ERROR = ChatColor.translateAlternateColorCodes('&', RENT_EXTEND_ERROR);
        GUI_EXTEND_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_EXTEND_BUTTON);
        REGION_INFO_MAX_RENT_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_MAX_RENT_TIME);
        REGION_INFO_EXTEND_PER_CLICK = ChatColor.translateAlternateColorCodes('&', REGION_INFO_EXTEND_PER_CLICK);
        REGION_INFO_REMAINING_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_REMAINING_TIME);
        REGION_INFO_HOTEL = ChatColor.translateAlternateColorCodes('&', REGION_INFO_HOTEL);
        REGION_ERROR_CAN_NOT_BUILD_HERE = ChatColor.translateAlternateColorCodes('&', REGION_ERROR_CAN_NOT_BUILD_HERE);
        COMPLETE = ChatColor.translateAlternateColorCodes('&', COMPLETE);
        RESET_IN_PERCENT = ChatColor.translateAlternateColorCodes('&', RESET_IN_PERCENT);
        LOADING_SCHEMATIC_COMPLETE = ChatColor.translateAlternateColorCodes('&', LOADING_SCHEMATIC_COMPLETE);
        LOADING_SCHEMATIC = ChatColor.translateAlternateColorCodes('&', LOADING_SCHEMATIC);
        REGION_BUY_OUT_OF_LIMIT = ChatColor.translateAlternateColorCodes('&', REGION_BUY_OUT_OF_LIMIT);
        UNLIMITED = ChatColor.translateAlternateColorCodes('&', UNLIMITED);
        GUI_USER_SELL_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_USER_SELL_BUTTON);
        GUI_USER_SELL_WARNING = ChatColor.translateAlternateColorCodes('&', GUI_USER_SELL_WARNING);
        LIMIT_INFO = ChatColor.translateAlternateColorCodes('&', LIMIT_INFO);
        LIMIT_INFO_TOP = ChatColor.translateAlternateColorCodes('&', LIMIT_INFO_TOP);
        GUI_MY_LIMITS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_MY_LIMITS_BUTTON);
        GUI_MEMBER_INFO_ITEM = ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_INFO_ITEM);
        REGION_IS_NOT_A_RENTREGION = ChatColor.translateAlternateColorCodes('&', REGION_IS_NOT_A_RENTREGION);
        REGION_NOT_OWN = ChatColor.translateAlternateColorCodes('&', REGION_NOT_OWN);
        REGION_NOT_SOLD = ChatColor.translateAlternateColorCodes('&', REGION_NOT_SOLD);
        PRESET_REMOVED = ChatColor.translateAlternateColorCodes('&', PRESET_REMOVED);
        PRESET_SET= ChatColor.translateAlternateColorCodes('&', PRESET_SET);
        REGION_INFO_DO_BLOCK_RESET = ChatColor.translateAlternateColorCodes('&', REGION_INFO_DO_BLOCK_RESET);
        PRESET_SAVED = ChatColor.translateAlternateColorCodes('&', PRESET_SAVED);
        PRESET_ALREADY_EXISTS = ChatColor.translateAlternateColorCodes('&', PRESET_ALREADY_EXISTS);
        PRESET_PLAYER_DONT_HAS_PRESET = ChatColor.translateAlternateColorCodes('&', PRESET_PLAYER_DONT_HAS_PRESET);
        PRESET_DELETED = ChatColor.translateAlternateColorCodes('&', PRESET_DELETED);
        PRESET_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', PRESET_NOT_FOUND);
        PRESET_LOADED = ChatColor.translateAlternateColorCodes('&', PRESET_LOADED);
        LIMIT_INFO_TOTAL = ChatColor.translateAlternateColorCodes('&', LIMIT_INFO_TOTAL);
        GUI_REGION_ITEM_NAME = ChatColor.translateAlternateColorCodes('&', GUI_REGION_ITEM_NAME);
        GUI_REGIONFINDER_REGIONKIND_NAME = ChatColor.translateAlternateColorCodes('&', GUI_REGIONFINDER_REGIONKIND_NAME);
        RENTREGION_EXPIRATION_WARNING = ChatColor.translateAlternateColorCodes('&', RENTREGION_EXPIRATION_WARNING);
        CONTRACT_SIGN1 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SIGN1);
        CONTRACT_SIGN2 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SIGN2);
        CONTRACT_SIGN3 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SIGN3);
        CONTRACT_SIGN4 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SIGN4);
        CONTRACT_SOLD_SIGN1 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SOLD_SIGN1);
        CONTRACT_SOLD_SIGN2 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SOLD_SIGN2);
        CONTRACT_SOLD_SIGN3 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SOLD_SIGN3);
        CONTRACT_SOLD_SIGN4 = ChatColor.translateAlternateColorCodes('&', CONTRACT_SOLD_SIGN4);
        CONTRACT_REGION_EXTENDED = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_EXTENDED);
        GUI_CONTRACT_ITEM = ChatColor.translateAlternateColorCodes('&', GUI_CONTRACT_ITEM);
        REGION_INFO_TERMINATED = ChatColor.translateAlternateColorCodes('&', REGION_INFO_TERMINATED);
        REGION_INFO_AUTO_EXTEND_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_AUTO_EXTEND_TIME);
        REGION_INFO_NEXT_EXTEND_REMAINING_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_NEXT_EXTEND_REMAINING_TIME);
        CONTRACT_REGION_CHANGE_TERMINATED = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_CHANGE_TERMINATED);
        CONTRACT_REGION_STATUS_ACTIVE_LONG = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_ACTIVE_LONG);
        CONTRACT_REGION_STATUS_ACTIVE = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_ACTIVE);
        CONTRACT_REGION_STATUS_TERMINATED_LONG = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_TERMINATED_LONG);
        CONTRACT_REGION_STATUS_TERMINATED = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_TERMINATED);
        REGION_IS_NOT_A_CONTRACT_REGION = ChatColor.translateAlternateColorCodes('&', REGION_IS_NOT_A_CONTRACT_REGION);
        GUI_OWNER_MEMBER_INFO_ITEM = ChatColor.translateAlternateColorCodes('&', GUI_OWNER_MEMBER_INFO_ITEM);
        REGION_TRANSFER_MEMBER_NOT_ONLINE = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_MEMBER_NOT_ONLINE);
        REGION_TRANSFER_LIMIT_ERROR = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_LIMIT_ERROR);
        TIME_SECONDS = ChatColor.translateAlternateColorCodes('&', TIME_SECONDS);
        TIME_MINUTES = ChatColor.translateAlternateColorCodes('&', TIME_MINUTES);
        TIME_HOURS = ChatColor.translateAlternateColorCodes('&', TIME_HOURS);
        TIME_DAYS = ChatColor.translateAlternateColorCodes('&', TIME_DAYS);
        TIME_SECONDS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_SECONDS_SHORT);
        TIME_MINUTES_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_MINUTES_SHORT);
        TIME_HOURS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_HOURS_SHORT);
        TIME_DAYS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_DAYS_SHORT);


        for(int i = 0; i < GUI_TELEPORT_TO_REGION_BUTTON_LORE.size(); i++){
            GUI_TELEPORT_TO_REGION_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_REGION_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_MAKE_OWNER_BUTTON_LORE.size(); i++){
            GUI_MAKE_OWNER_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_MAKE_OWNER_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_REMOVE_MEMBER_BUTTON_LORE.size(); i++){
            GUI_REMOVE_MEMBER_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_REMOVE_MEMBER_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_RESET_REGION_BUTTON_LORE.size(); i++){
            GUI_RESET_REGION_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_TAKEOVER_ITEM_LORE.size(); i++){
            GUI_TAKEOVER_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_TAKEOVER_ITEM_LORE.get(i)));
        }
        for(int i = 0; i < MEMBERLIST_INFO_LORE.size(); i++){
            MEMBERLIST_INFO_LORE.set(i, ChatColor.translateAlternateColorCodes('&', MEMBERLIST_INFO_LORE.get(i)));
        }
        for(int i = 0; i < GUI_EXTEND_BUTTON_LORE.size(); i++){
            GUI_EXTEND_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_EXTEND_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_RENT_REGION_LORE.size(); i++){
            GUI_RENT_REGION_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_RENT_REGION_LORE.get(i)));
        }
        for(int i = 0; i < GUI_USER_SELL_BUTTON_LORE.size(); i++){
            GUI_USER_SELL_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_USER_SELL_BUTTON_LORE.get(i)));
        }
        for(int i = 0; i < GUI_MEMBER_INFO_LORE.size(); i++){
            GUI_MEMBER_INFO_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_INFO_LORE.get(i)));
        }
        for(int i = 0; i < GUI_CONTRACT_ITEM_LORE.size(); i++){
            GUI_CONTRACT_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_CONTRACT_ITEM_LORE.get(i)));
        }
        for(int i = 0; i < GUI_CONTRACT_REGION_LORE.size(); i++){
            GUI_CONTRACT_REGION_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_CONTRACT_REGION_LORE.get(i)));
        }
        for(int i = 0; i < GUI_OWNER_MEMBER_INFO_LORE.size(); i++) {
            GUI_OWNER_MEMBER_INFO_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_OWNER_MEMBER_INFO_LORE.get(i)));
        }
    }

    public static void generatedefaultConfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/messages.yml");
        if(!messagesdic.exists()){
            try {
                InputStream stream = plugin.getResource("messages.yml");
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
        setConfig();
    }

    public static YamlConfiguration getConfig(){
        return Messages.config;
    }

    public static void setConfig(){
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Messages.config = YamlConfiguration.loadConfiguration(messagesconfigdic);
    }

    public static void saveConfig(){
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        try {
            Messages.config.save(messagesconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

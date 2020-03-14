package net.alex9849.arm;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public class Messages {

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
    public static String COMMAND_ONLY_INGAME = "";
    public static String REGION_INFO_PRICE = "";
    public static String REGION_INFO_TYPE = "";
    public static String REGION_INFO_INACTIVITYRESET = "";
    public static String REGION_INFO_MAX_RENT_TIME = "";
    public static String REGION_INFO_EXTEND_PER_CLICK = "";
    public static String REGION_INFO_HOTEL = "";
    public static String REGION_INFO_ALLOWED_SUBREGIONS = "";
    public static String REGION_INFO_MAX_MEMBERS = "";
    public static String REGION_INFO_EXPIRED = "&6Expired";
    public static String GUI_MAIN_MENU_NAME = "";
    public static String GUI_GO_BACK = "";
    public static String GUI_MY_OWN_REGIONS = "";
    public static String GUI_MEMBER_REGIONS_MENU_NAME = "";
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
    public static String REGION_TELEPORT_MESSAGE = "";
    public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = "";
    public static String NO_FREE_REGION_WITH_THIS_KIND = "";
    public static String REGIONKIND_DOES_NOT_EXIST = "";
    public static String REGION_NOW_AVIABLE = "";
    public static String NO_REGION_AT_PLAYERS_POSITION = "";
    public static String REGION_ADD_MEMBER_NOT_ONLINE = "";
    public static String REGION_ADD_MEMBER_ADDED = "";
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER = "";
    public static String REGION_REMOVE_MEMBER_REMOVED = "";
    public static String GUI_RESET_REGION_BUTTON = "";
    public static String GUI_RESET_REGION_WARNING_NAME = "";
    public static String RESET_COMPLETE = "";
    public static String RESET_REGION_COOLDOWN_ERROR = "";
    public static String GUI_TAKEOVER_MENU_NAME = "";
    public static String REGION_TRANSFER_COMPLETE_MESSAGE = "";
    public static String GUI_CLOSE = "";
    public static String RENT_SIGN1 = "";
    public static String RENT_SIGN2 = "";
    public static String RENT_SIGN3 = "";
    public static String RENT_SIGN4 = "";
    public static String RENTED_SIGN1 = "";
    public static String RENTED_SIGN2 = "";
    public static String RENTED_SIGN3 = "";
    public static String RENTED_SIGN4 = "";
    public static String RENT_EXTEND_MESSAGE = "";
    public static String RENT_EXTEND_MAX_RENT_TIME_EXCEEDED = "";
    public static String GUI_EXTEND_BUTTON = "";
    public static String COMPLETE = "";
    public static String REGION_BUY_OUT_OF_LIMIT = "";
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE = "";
    public static String UNLIMITED = "";
    public static String GUI_USER_SELL_BUTTON = "";
    public static String GUI_USER_SELL_WARNING = "";
    public static String LIMIT_INFO_TOP = "";
    public static String LIMIT_INFO = "";
    public static String GUI_MY_LIMITS_BUTTON = "";
    public static String GUI_MEMBER_INFO_ITEM = "";
    public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED = "";
    public static String REGION_IS_NOT_A_RENTREGION = "Region is not a rentregion!";
    public static String REGION_NOT_OWN = "You do not own this region!";
    public static String REGION_NOT_SOLD = "Region not sold!";
    public static String PRESET_REMOVED = "Preset removed!";
    public static String PRESET_SET = "Preset set!";
    public static String REGION_INFO_AUTORESTORE = "Autorestore: ";
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
    public static String CONTRACT_SIGN4 = "%price%%currency%/%extendtime%";
    public static String CONTRACT_SOLD_SIGN1 = "&4Contract in use";
    public static String CONTRACT_SOLD_SIGN2 = "%regionid%/%owner%";
    public static String CONTRACT_SOLD_SIGN3 = "%price%%currency%/%extendtime%";
    public static String CONTRACT_SOLD_SIGN4 = "%remaining%";
    public static String CONTRACT_REGION_EXTENDED = "&aYour contract region %regionid% has been extended for %extendtime%. (For %price%%currency%.) ";
    public static String GUI_CONTRACT_ITEM = "&aManage contract";
    public static String REGION_INFO_AUTO_EXTEND_TIME = "&6Extend time: ";
    public static String REGION_INFO_AUTOPRICE = "&6Autoprice: ";
    public static String CONTRACT_REGION_STATUS_ACTIVE = "&aActive";
    public static String CONTRACT_REGION_STATUS_TERMINATED = "&4Terminated";
    public static String REGION_IS_NOT_A_CONTRACT_REGION = "&4Region is not a contractregion!";
    public static String GUI_OWNER_MEMBER_INFO_ITEM = "";
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE = "Member not online!";
    public static String REGION_TRANSFER_LIMIT_ERROR = "Transfer aborted! (Region would exceed players limit)";
    public static String TIME_SECONDS_SINGULAR = "Second";
    public static String TIME_MINUTES_SINGULAR = "Minute";
    public static String TIME_HOURS_SINGULAR = "Hour";
    public static String TIME_DAYS_SINGULAR = "Day";
    public static String TIME_SECONDS_PLURAL = "Seconds";
    public static String TIME_MINUTES_PLURAL = "Minutes";
    public static String TIME_HOURS_PLURAL = "Hours";
    public static String TIME_DAYS_PLURAL = "Days";
    public static String TIME_SECONDS_SHORT = "s";
    public static String TIME_MINUTES_SHORT = "m";
    public static String TIME_HOURS_SHORT = "h";
    public static String TIME_DAYS_SHORT = "d";
    public static String TIME_UNIT_SPLITTER = " and ";
    public static String TIME_UNIT_SPLITTER_SHORT = ":";
    public static String NOT_A_MEMBER_OR_OWNER = "";
    public static String YES = "";
    public static String NO = "";
    public static String REGION_STATS = "&6=========[Region stats]=========";
    public static String REGION_STATS_PATTERN = "&6Used regions (%8egionkind%&8)";
    public static String RENT_REGION = "&6Rentregion";
    public static String SELL_REGION = "&6Sellregion";
    public static String CONTRACT_REGION = "&6Contractregion";
    public static String TELEPORTER_NO_SAVE_LOCATION_FOUND = "&4Could not find a save teleport location";
    public static String TELEPORTER_DONT_MOVE = "&6Teleportation will commence in &c%time% Seconds&6. Do not move!";
    public static String TELEPORTER_TELEPORTATION_ABORDED = "&4Teleportation aborded!";
    public static String OFFER_SENT = "&aYour offer has been sent";
    public static String OFFER_ACCEPTED_SELLER = "&a%buyer% &aaccepted your offer";
    public static String OFFER_ACCEPTED_BUYER = "&aOffer accepted! You are now the owner of &c%regionid%";
    public static String NO_OFFER_TO_ANSWER = "&4You dont have an offer to answer";
    public static String OFFER_REJECTED = "&aOffer rejected!";
    public static String OFFER_HAS_BEEN_REJECTED = "&4%seller% &4rejected your offer!";
    public static String NO_OFFER_TO_REJECT = "&4You do not have an offer to reject";
    public static String OFFER_CANCELED = "&aYour offer has been cancelled!";
    public static String OFFER_HAS_BEEN_CANCELLED = "&4%seller% &4cancelled his offer!";
    public static String NO_OFFER_TO_CANCEL = "&4You do not have an offer to cancel";
    public static String BUYER_ALREADY_GOT_AN_OFFER = "&4The selected buyer already got an offer that he has to answer first!";
    public static String SELLER_ALREADY_CREATED_AN_OFFER = "&4You have already created an offer! Please wait for an answer or cancel it first!";
    public static String SELLER_DOES_NOT_LONGER_OWN_REGION = "&4%seller% &4does not longer own this region. His offer has been cancelled";
    public static String INCOMING_OFFER = "";
    public static String SELECTED_PLAYER_NOT_ONLINE = "&4The selected player is not online";
    public static String OFFER_TIMED_OUT = "&4Offer timed out!";
    public static String BAD_SYNTAX = "&7Bad syntax! Please use: &8%command%";
    public static String BAD_SYNTAX_SPLITTER = "&7or &8%command%";
    public static String HELP_HEADLINE = "&6=====[AdvancedRegionMarket Help ]=====\n&3Page %actualpage% / %maxpage%";
    public static String PRESET_SETUP_COMMANDS = "&6Setup commands:";
    public static String PRICE_CAN_NOT_BE_NEGATIVE = "&4Price can not be negative!";
    public static String SELLBACK_WARNING = "&4Sell region back to the server: \n&4WARNING: &cThis can not be undone! Your region &6%regionid% &cwill be released and all blocks on it will be resetted! " +
            "You and all members will loose their rights on it. You will get &6%paybackmoney% %currency% &cback";
    public static String SUB_REGION_INACTIVITYRESET_ERROR = "";
    public static String SUB_REGION_AUTORESTORE_ERROR = "";
    public static String REGION_NOT_RESETTABLE = "";
    public static String REGION_SELECTED_MULTIPLE_REGIONS = "";
    public static String SUB_REGION_REGIONKIND_ERROR = "";
    public static String SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS = "";
    public static String SUB_REGION_TELEPORT_LOCATION_ERROR = "";
    public static String REGION_NOT_REGISTRED = "";
    public static String FIRST_POSITION_SET = "";
    public static String SECOND_POSITION_SET = "";
    public static String MARK_IN_OTHER_REGION_REMOVING = "";
    public static String PARENT_REGION_NOT_OWN = "";
    public static String NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD = "";
    public static String NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE = "";
    public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION = "";
    public static String ALREADY_SUB_REGION_AT_THIS_POSITION = "";
    public static String SUB_REGION_LIMIT_REACHED = "";
    public static String SELECTION_INVALID = "";
    public static String REGION_CREATED_AND_SAVED = "";
    public static String REGION_NOT_A_SUBREGION = "";
    public static String REGION_DELETED = "";
    public static String DELETE_REGION_WARNING_NAME = "&4Delete region?";
    public static String UNSELL_REGION_BUTTON = "&4Unsell region";
    public static List<String> UNSELL_REGION_BUTTON_LORE = new ArrayList<>(Arrays.asList("&4Click to unsell your subregion and", "&4kick the players of it"));
    public static String UNSELL_REGION_WARNING_NAME = "&4Unsell region?";
    public static String SUBREGION_HELP_HEADLINE = "&6=====[AdvancedRegionMarket Subregion Help ]=====\n&3Page %actualpage% / %maxpage%";
    public static String SELLREGION_NAME = "Sellregion";
    public static String CONTRACTREGION_NAME = "Contractregion";
    public static String RENTREGION_NAME = "Rentregion";
    public static String GUI_SUBREGION_ITEM_BUTTON = "&6Subregions";
    public static String GUI_SUBREGION_LIST_MENU_NAME = "&1Subregions";
    public static String GUI_SUBREGION_HOTEL_BUTTON = "&6Hotel-function";
    public static String GUI_SUBREGION_DELETE_REGION_BUTTON = "&4Delete region";
    public static String GUI_TELEPORT_TO_SIGN_OR_REGION = "Teleport to sign or region?";
    public static String GUI_TELEPORT_TO_SIGN = "&6Teleport to buy sign!";
    public static String GUI_TELEPORT_TO_REGION = "&6Teleport to region!";
    public static String GUI_NEXT_PAGE = "&6Next page";
    public static String GUI_PREV_PAGE = "&6Prev page";
    public static String ENABLED = "&aenabled";
    public static String DISABLED = "&cdisabled";
    public static String SOLD = "&csold";
    public static String AVAILABLE = "&aavailable";
    public static String REGION_INFO_IS_USER_RESTORABLE = "";
    public static String SUB_REGION_IS_USER_RESETTABLE_ERROR = "&4The selected region is a subregion. You can change the isUserRestorable setting for all subregions in the config.yml!";
    public static String SUB_REGION_MAX_MEMBERS_ERROR = "";
    public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE = new ArrayList<>(Arrays.asList("&6The hotel function allows you to prevent players", "&6from breaking blocks they do not have placed", "&6Status: %hotelfunctionstatus%", "&6Click to enable/disable"));
    public static List<String> GUI_SUBREGION_REGION_INFO_SELL = new ArrayList<>(Arrays.asList("%regionid%", "Selltype: %selltype%", "Status: %soldstatus%", "Price: %price%", "Price per M2: %priceperm2%", "Dimensions: %dimensions%"));
    public static List<String> GUI_SUBREGION_REGION_INFO_RENT = new ArrayList<>(Arrays.asList("%regionid%", "Selltype: %selltype%", "Status: %soldstatus%", "Price: %price%", "Price per M2 (per week): %priceperm2perweek%", "Extend per click: %extendtime%", "Max. extended time: %maxrenttime%", "Dimensions: %dimensions%"));
    public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT = new ArrayList<>(Arrays.asList("%regionid%", "Selltype: %selltype%", "Status: %soldstatus%", "Price: %price%", "Price per M2 (per week): %priceperm2perweek%", "Automatic extend time: %extendtime%", "Dimensions: %dimensions%"));
    public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL = new ArrayList<>(Arrays.asList("%regionid%", "Price: %price%", "Price per M2: %priceperm2%", "Dimensions: %dimensions%", "World: %world%"));
    public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT = new ArrayList<>(Arrays.asList("%regionid%", "Price: %price%", "Price per M2 (per week): %priceperm2perweek%", "Extend per click: %extendtime%", "Max. extended time: %maxrenttime%", "Dimensions: %dimensions%", "World: %world%"));
    public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT = new ArrayList<>(Arrays.asList("%regionid%", "Price: %price%", "Price per M2 (per week): %priceperm2perweek%", "Automatic extend time: %extendtime%", "Dimensions: %dimensions%", "World: %world%"));
    public static List<String> SELECTION_SAVED_CREATE_SIGN = new ArrayList<>(Arrays.asList("&aYour selection has been saved! You can now create a sign to sell the region.", "&aCreate a Sell-Region:", "&6First line: &1[sub-sell]", "&6Last line: &1price", "", "&aCreate a Rent-Region:", "&6First line: &1[sub-rent]",
            "&6Last line: &1PricePerPeriod&6;&1ExtendPerClick&6;&1MaxExtendTime", "&6example for ExtendPerClick/MaxExtendTime: 5d (5 days)", "", "&aCreate a Contract-Region:", "&6First line: &1[sub-contract]", "&6Last line: &1PricePerPeriod&6;&1ExtendTime", "&6example for ExtendTime: 12h (12 hours)"));
    public static List<String> SUBREGION_TOOL_INSTRUCTION = new ArrayList<>(Arrays.asList("&aYou got a tool in your inventory (feather) to select 2 points of your region that will mark the corners of your new subregion.", "&aLeft click to select pos1", "&aRight click to select pos2", "&aType \"&6/arm subregion create\" &aif you are done"));
    public static String SUBREGION_TOOL_ALREADY_OWNED = "&4You already own a Subregion Tool. Please use this instead of a new one!";
    public static String AUTOPRICE_LIST = "&6=========[Autoprices]=========";
    public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM = "&6Info";
    public static String SELLTYPE_NOT_EXIST = "&4The selected selltype does not exist!";
    public static String SIGN_LINK_MODE_ACTIVATED = "&aSign-Link-Mode activated! Click on a sign and on a region to create a region or to add the sign to the region";
    public static String SIGN_LINK_MODE_DEACTIVATED = "&aSign-Link-Mode deactivated!";
    public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED = "&4Sign-Link-Mode is already deactivated!";
    public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY = "&cThe selected preset is not price-ready! All regions you will create now will be created with the default autoprice";
    public static String SIGN_LINK_MODE_NO_PRESET_SELECTED = "&cYou dont have a preset loaded! Please load or create a preset first! &cYou can create a preset by using the &6/arm sellpreset/rentpreset/contractpreset &ccommands!\nFor more &cinformation about presets click here:\n&6https://goo.gl/3upfAA (Gitlab Wiki)";
    public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION = "&4Sign belongs to another region!";
    public static String SIGN_LINK_MODE_SIGN_SELECTED = "&aSign selected!";
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS = "&4Could not select WorldGuard-Region! There is more then one region available!";
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION = "&4Could not select WorldGuard-Region! There is no region at this position!";
    public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD = "&4Could not identify world! Please select the WorldGuard-Region again!";
    public static String SIGN_LINK_MODE_NO_SIGN_SELECTED = "&4You have not selected a sign";
    public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED = "&4You have not selected a WorldGuard-Region";
    public static String SIGN_LINK_MODE_REGION_SELECTED = "&aSelected region: %regionid%";
    public static String SCHEMATIC_NOT_FOUND_ERROR_USER = "&4It seems like the schematic of your region %regionid% has not been created. Please contact an admin!";
    public static String SCHEMATIC_NOT_FOUND_ERROR_ADMIN = "&4Could not find schematic for %regionid%";
    public static String ENTITYLIMIT_HELP_HEADLINE = "&6=====[AdvancedRegionMarket EntityLimit Help ]=====\n&3Page %actualpage% / %maxpage%";
    public static String ENTITYLIMITGROUP_DOES_NOT_EXIST = "&4EntityLimitGroup does not exist!";
    public static String ENTITYLIMIT_SET = "&aEntityLimit has been set!";
    public static String ENTITYLIMIT_REMOVED = "&aEntityLimit has been removed!";
    public static String ENTITYTYPE_DOES_NOT_EXIST = "&4The entitytype &6%entitytype% &4 does not exist!";
    public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT = "&4The selected EntityLimitGroup does not contain the selected EntityType";
    public static String ENTITYLIMIT_TOTAL = "Total";
    public static String SUB_REGION_PAYBACKPERCENTAGE_ERROR = "";
    public static String ENTITYLIMIT_CHECK_HEADLINE = "===[EntityLimitCheck for %regionid%]===";
    public static String ENTITYLIMIT_CHECK_PATTERN = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%";
    public static String ENTITYLIMIT_CHECK_EXTENSION_INFO = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity% %currency%&6/entity";
    public static String ENTITYLIMITGROUP_ALREADY_EXISTS = "&4Group already exists!";
    public static String ENTITYLIMITGROUP_CREATED = "&aEntitylimitgroup has been created!";
    public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM = "&4You can not remove a system-EntityLimitGroup!";
    public static String ENTITYLIMITGROUP_DELETED = "&aEntitylimitgroup has been deleted!";
    public static String ENTITYLIMITGROUP_INFO_HEADLINE = "&6======[Entitylimitgroup Info]======";
    public static String ENTITYLIMITGROUP_INFO_GROUPNAME = "&6Groupname: ";
    public static String ENTITYLIMITGROUP_INFO_PATTERN = "&6%entitytype%: &r%softlimitentities% %entityextensioninfo%";
    public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity% %currency%&6/entity";
    public static String ENTITYLIMITGROUP_LIST_HEADLINE = "&6EntityLimitGroups:";
    public static String REGION_INFO_ENTITYLIMITGROUP = "&6EntityLimitGroup: ";
    public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS = "&4SubregionEntityLimitGroup only for subregions";
    public static String MASSACTION_SPLITTER = "&6all regions with regionkind &a%regionkind%";
    public static String SUB_REGION_ENTITYLIMITGROUP_ERROR = "&4Could not change EntiyLimitGroup for the region &6%regionid%&4! Region is a Subregion!";
    public static String SUB_REGION_FLAGGROUP_ERROR = "&4Could not change Flaggroup for the region &6%regionid%&4! Region is a Subregion!";
    public static String GUI_ENTITYLIMIT_ITEM_BUTTON = "&6EntityLimits";
    public static List<String> GUI_ENTITYLIMIT_ITEM_LORE = new ArrayList<>(Arrays.asList("&6Click to display the entity-limits", "&6for this region in chat", "%entityinfopattern%"));
    public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a  %entityextensioninfo%)";
    public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity% %currency%&6/entity";
    public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED = "&4EntityLimit for the selected entity and region is already unlimited!";
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET = "&aExtra-Entities have been set!";
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS = "&aYou have sucessfully expanded the entitylimit to &6%softlimitentities% &aentities!";
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED = "&4Can not buy another entity-expansion! Hardlimit has been reached!";
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR = "&4Can not change entitylimit! Region is a Subregion";
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR = "&4Can not expand entitylimit! Region is a Subregion";
    public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY = "&4Could not spawn entity on region %region%! The not spawned entity would exeed the regions " +
            "entitylimit for more information type &6/arm entitylimit check %region%&4! Everybody on region %region% recieved this message! If you are not a member of this region, you can ignore this message!";
    public static String ARM_BASIC_COMMAND_MESSAGE = "&6AdvancedRegionMarket v%pluginversion% by Alex9849\n&6Download: &3https://bit.ly/2CfO3An\n&6Get a list with all commands with &3/arm help";
    public static String REGIONKIND_CREATED = "&aRegionKind created!";
    public static String REGIONKIND_ALREADY_EXISTS = "&4RegionKind already exists!";
    public static String REGIONKIND_DELETED = "&aRegionKind deleted!";
    public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM = "&4You can not remove a system-RegionKind!";
    public static String REGIONKIND_LIST_HEADLINE = "&6Regionkinds:";
    public static String REGIONKIND_MODIFIED = "&aRegionKind modified!";
    public static String MATERIAL_NOT_FOUND = "&4Material not found!";
    public static String REGIONKIND_LORE_LINE_NOT_EXIST = "&aThe selected lore-line does not exist!";
    public static String REGIONKIND_INFO_HEADLINE = "&6=========[Regionkind info]=========";
    public static String REGIONKIND_INFO_INTERNAL_NAME = "&6Internal name: %regionkind%";
    public static String REGIONKIND_INFO_DISPLAY_NAME = "&6Displayname: %regionkinddisplay%";
    public static String REGIONKIND_INFO_MATERIAL = "&6Material: %regionkinditem%";
    public static String REGIONKIND_INFO_DISPLAY_IN_GUI = "&6DisplayInGui: %regionkinddisplayingui%";
    public static String REGIONKIND_INFO_DISPLAY_IN_LIMITS = "&6DisplayInLimits: %regionkinddisplayinlimits%";
    public static String REGIONKIND_INFO_LORE = "&6Lore:";
    public static String REGIONKIND_HELP_HEADLINE = "&6=====[AdvancedRegionMarket RegionKind Help ]=====\n&3Page %actualpage% / %maxpage%";
    public static String PLAYER_NOT_FOUND = "&4Could not find selected player!";
    public static List<String> REGION_INFO_SELLREGION = new ArrayList<>();
    public static List<String> REGION_INFO_RENTREGION = new ArrayList<>();
    public static List<String> REGION_INFO_CONTRACTREGION = new ArrayList<>();
    public static List<String> REGION_INFO_SELLREGION_ADMIN = new ArrayList<>();
    public static List<String> REGION_INFO_RENTREGION_ADMIN = new ArrayList<>();
    public static List<String> REGION_INFO_CONTRACTREGION_ADMIN = new ArrayList<>();
    public static List<String> REGION_INFO_SELLREGION_SUBREGION = new ArrayList<>();
    public static List<String> REGION_INFO_RENTREGION_SUBREGION = new ArrayList<>();
    public static List<String> REGION_INFO_CONTRACTREGION_SUBREGION = new ArrayList<>();
    public static String GUI_FLAGEDITOR_BUTTON = "&6FlagEditor";
    public static String GUI_FLAGEDITOR_MENU_NAME = "&1FlagEditor (%region%)";
    public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON = "&4Delete flag";
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON = "&9Set for everyone";
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON = "&9Set for members";
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON = "&9Set for owners";
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON = "&9Set for non members\n (Everyone without permissions on your region)";
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON = "&9Set for non owners";
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON = "&2Allow";
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON = "&4Deny";
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON = "&2Yes";
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON = "&4No";
    public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON = "&2Set message";
    public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON = "&2Set number";
    public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON = "&2Set number";
    public static String FlAGEDITOR_FLAG_NOT_ACTIVATED = "&4Flag not activated!";
    public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED = "&2Flag has been deleted!";
    public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED = "&2Flag has been updated!";
    public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED = "Could not modify flag %flag%!";
    public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO = "&9Please write down a message:";
    public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO = "&9Please write down a number that does not have decimals:";
    public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO = "&9Please write down a number:";
    public static String GUI_FLAGEDITOR_RESET_BUTTON = "&4Reset all Flags to default settings";
    public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON = "&2Set properties";
    public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO = "&9Please write down your new flag properties:";
    public static String FLAGGROUP_DOES_NOT_EXIST = "&4Flaggroup does not exist!";
    public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS = "&4Subregion flaggroup only for subregions";
    public static String REGION_INFO_FLAGGROUP = "&6FlagGroup: ";
    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_RESET_REGION_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_TAKEOVER_ITEM_LORE = new ArrayList<>();
    public static List<String> GUI_EXTEND_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_RENT_REGION_LORE = new ArrayList<>();
    public static List<String> GUI_USER_SELL_BUTTON_LORE = new ArrayList<>();
    public static List<String> GUI_MEMBER_INFO_LORE = new ArrayList<>();
    public static List<String> GUI_CONTRACT_ITEM_LORE = new ArrayList<>();
    public static List<String> GUI_CONTRACT_REGION_LORE = new ArrayList<>();
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE = new ArrayList<>();
    public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE = new ArrayList<>();
    public static String INFO_DEACTIVATED = "&4Deactivated";
    public static String INFO_REGION_NOT_SOLD = "&4Region not sold!";
    public static String INFO_NOW = "&2Now";
    public static String INFO_NOT_CALCULATED = "&8Awaiting calculation...";
    public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG = "&4Could not find or load schematic for region %region% in world %world%! You can regenerate it with /arm updateschematic %region%";
    public static String REGION_SOLD_BACK_SUCCESSFULLY = "&2Your region %regionid% has been successfully sold back to the server! %paybackmoney%%currency% have been added to your account!";
    public static String REGION_MODIFIED_BOOLEAN = "&6%option% %state% &6for %selectedregions%&6!";
    public static String REGION_MODIFIED = "&6%option% &6modified for %selectedregions%&6!";
    public static String UPDATING_SCHEMATIC = "&8Updating schematic...";
    public static String SCHEMATIC_UPDATED = "&aSchematic updated!";
    public static String CONTRACTREGION_TERMINATED = "&aYour contractregion &6%region% &ahas successfully been &4terminated&a! It will be resetted in &6%remaining% &aexcept it gets reactivated!";
    public static String CONTRACTREGION_REACTIVATED = "&aYour contractregion &6%region% &ahas successfully been &4reactivated&a! It will automatically extended in &6%remaining% &aif you can pay for the rent!";
    public static String REGION_INFO_FEATURE_DISABLED = "";
    public static String FLAGGROUP_FEATURE_DISABLED = "";
    public static String BACKUP_CREATED = "";
    public static String BACKUP_RESTORED = "";
    public static String COULD_NOT_LOAD_BACKUP = "";
    public static String BACKUP_LIST_HEADER = "";
    private static YamlConfiguration config;

    static void read() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Configuration config = YamlConfiguration.loadConfiguration(messagesconfigdic);

        PREFIX = config.getString("Messages.Prefix") + " ";
        if (config.getString("Messages.Prefix").equals(""))
            PREFIX = "";
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
        COMMAND_ONLY_INGAME = config.getString("Messages.CommandOnlyIngame");
        REGION_INFO_PRICE = config.getString("Messages.RegionInfoPrice");
        REGION_INFO_TYPE = config.getString("Messages.RegionInfoType");
        REGION_INFO_INACTIVITYRESET = config.getString("Messages.RegionInfoInactivityReset");
        REGION_INFO_ALLOWED_SUBREGIONS = config.getString("Messages.RegionInfoAllowedSubregions");
        REGION_INFO_EXPIRED = config.getString("Messages.RegionInfoExpired");
        REGION_INFO_AUTOPRICE = config.getString("Messages.RegionInfoAutoprice");
        REGION_INFO_MAX_MEMBERS = config.getString("Messages.RegionInfoMaxMembers");
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
        REGION_TELEPORT_MESSAGE = config.getString("Messages.RegionTeleportMessage");
        NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = config.getString("Messages.NoPermissionsToBuyThisKindOfRegion");
        NO_FREE_REGION_WITH_THIS_KIND = config.getString("Messages.NoFreeRegionWithThisKind");
        REGIONKIND_DOES_NOT_EXIST = config.getString("Messages.RegionkindDoesNotExist");
        REGION_NOW_AVIABLE = config.getString("Messages.RegionNowAviable");
        NO_REGION_AT_PLAYERS_POSITION = config.getString("Messages.NoRegionAtPlayersPosition");
        REGION_ADD_MEMBER_NOT_ONLINE = config.getString("Messages.RegionAddMemberNotOnline");
        REGION_ADD_MEMBER_ADDED = config.getString("Messages.RegionAddMemberAdded");
        REGION_REMOVE_MEMBER_NOT_A_MEMBER = config.getString("Messages.RegionRemoveMemberNotAMember");
        REGION_REMOVE_MEMBER_REMOVED = config.getString("Messages.RegionRemoveMemberRemoved");
        GUI_RESET_REGION_BUTTON = config.getString("Messages.GUIResetRegionButton");
        GUI_RESET_REGION_WARNING_NAME = config.getString("Messages.GUIResetRegionWarningName");
        RESET_COMPLETE = config.getString("Messages.ResetComplete");
        COMPLETE = config.getString("Messages.Complete");
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
        RENT_EXTEND_MAX_RENT_TIME_EXCEEDED = config.getString("Messages.RentExtendMaxRentTimeExceeded");
        REGION_INFO_HOTEL = config.getString("Messages.isHotel");
        GUI_EXTEND_BUTTON = config.getString("Messages.GUIExtendRentRegionButton");
        REGION_INFO_MAX_RENT_TIME = config.getString("Messages.RegionInfoMaxRentTime");
        REGION_INFO_EXTEND_PER_CLICK = config.getString("Messages.RegionInfoExtendPerClick");
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
        REGION_INFO_AUTORESTORE = config.getString("Messages.RegionInfoAutoRestore");
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
        REGION_INFO_AUTO_EXTEND_TIME = config.getString("Messages.RegionInfoAutoExtendTime");
        CONTRACT_REGION_STATUS_ACTIVE = config.getString("Messages.ContractRegionStatusActive");
        CONTRACT_REGION_STATUS_TERMINATED = config.getString("Messages.ContractRegionStatusTerminated");
        GUI_CONTRACT_ITEM_LORE = config.getStringList("Messages.GUIContractItemLore");
        GUI_CONTRACT_REGION_LORE = config.getStringList("Messages.GUIContractItemRegionLore");
        REGION_IS_NOT_A_CONTRACT_REGION = config.getString("Messages.RegionIsNotAContractRegion");
        REGION_TRANSFER_MEMBER_NOT_ONLINE = config.getString("Messages.RegiontransferMemberNotOnline");
        REGION_TRANSFER_LIMIT_ERROR = config.getString("Messages.RegiontransferLimitError");
        TIME_SECONDS_SINGULAR = config.getString("Messages.SecondsSingular");
        TIME_MINUTES_SINGULAR = config.getString("Messages.MinutesSingular");
        TIME_HOURS_SINGULAR = config.getString("Messages.HoursSingular");
        TIME_DAYS_SINGULAR = config.getString("Messages.DaysSingular");
        TIME_SECONDS_PLURAL = config.getString("Messages.SecondsPlural");
        TIME_MINUTES_PLURAL = config.getString("Messages.MinutesPlural");
        TIME_HOURS_PLURAL = config.getString("Messages.HoursPlural");
        TIME_DAYS_PLURAL = config.getString("Messages.DaysPlural");
        TIME_SECONDS_SHORT = config.getString("Messages.SecondsShort");
        TIME_MINUTES_SHORT = config.getString("Messages.MinutesShort");
        TIME_HOURS_SHORT = config.getString("Messages.HoursShort");
        TIME_DAYS_SHORT = config.getString("Messages.DaysShort");
        TIME_UNIT_SPLITTER = config.getString("Messages.TimeUnitSplitter");
        TIME_UNIT_SPLITTER_SHORT = config.getString("Messages.TimeUnitSplitterShort");
        NOT_A_MEMBER_OR_OWNER = config.getString("Messages.UserNotAMemberOrOwner");
        YES = config.getString("Messages.RegionInfoYes");
        NO = config.getString("Messages.RegionInfoNo");
        RENT_REGION = config.getString("Messages.RentRegion");
        SELL_REGION = config.getString("Messages.SellRegion");
        CONTRACT_REGION = config.getString("Messages.ContractRegion");
        REGION_STATS = config.getString("Messages.RegionStats");
        REGION_STATS_PATTERN = config.getString("Messages.RegionStatsPattern");
        TELEPORTER_NO_SAVE_LOCATION_FOUND = config.getString("Messages.TeleporterNoSaveLocation");
        TELEPORTER_DONT_MOVE = config.getString("Messages.TeleporterDontMove");
        TELEPORTER_TELEPORTATION_ABORDED = config.getString("Messages.TeleporterTeleportationAborded");
        OFFER_SENT = config.getString("Messages.OfferSent");
        OFFER_ACCEPTED_SELLER = config.getString("Messages.OfferAcceptedSeller");
        OFFER_ACCEPTED_BUYER = config.getString("Messages.OfferAcceptedBuyer");
        NO_OFFER_TO_ANSWER = config.getString("Messages.NoOfferToAnswer");
        OFFER_REJECTED = config.getString("Messages.OfferRejected");
        OFFER_HAS_BEEN_REJECTED = config.getString("Messages.OfferHasBeenRejected");
        NO_OFFER_TO_REJECT = config.getString("Messages.NoOfferToReject");
        OFFER_CANCELED = config.getString("Messages.OfferCancelled");
        OFFER_HAS_BEEN_CANCELLED = config.getString("Messages.OfferHasBeenCancelled");
        NO_OFFER_TO_CANCEL = config.getString("Messages.NoOfferToCancel");
        BUYER_ALREADY_GOT_AN_OFFER = config.getString("Messages.BuyerAlreadyGotAnOffer");
        SELLER_ALREADY_CREATED_AN_OFFER = config.getString("Messages.SellerAlreadyCreatedAnOffer");
        SELLER_DOES_NOT_LONGER_OWN_REGION = config.getString("Messages.SellerDoesNotLongerOwnRegion");
        INCOMING_OFFER = config.getString("Messages.IncommingOffer");
        SELECTED_PLAYER_NOT_ONLINE = config.getString("Messages.SelectedPlayerIsNotOnline");
        BAD_SYNTAX = config.getString("Messages.BadSyntax");
        BAD_SYNTAX_SPLITTER = config.getString("Messages.BadSyntaxSplitter");
        OFFER_TIMED_OUT = config.getString("Messages.OfferTimedOut");
        PRESET_SETUP_COMMANDS = config.getString("Messages.PresetSetupCommands");
        PRICE_CAN_NOT_BE_NEGATIVE = config.getString("Messages.PriceCanNotBeNegative");
        SELLBACK_WARNING = config.getString("Messages.SellBackWarning");
        SUB_REGION_INACTIVITYRESET_ERROR = config.getString("Messages.SubregionInactivityResetError");
        SUB_REGION_AUTORESTORE_ERROR = config.getString("Messages.SubregionAutoRestoreError");
        SUB_REGION_MAX_MEMBERS_ERROR = config.getString("Messages.SubregionMaxMembersError");
        SUB_REGION_IS_USER_RESETTABLE_ERROR = config.getString("Messages.SubregionIsUserResettableError");
        REGION_NOT_RESETTABLE = config.getString("Messages.RegionNotResettable");
        REGION_SELECTED_MULTIPLE_REGIONS = config.getString("Messages.RegionSelectedMultipleRegions");
        SUB_REGION_REGIONKIND_ERROR = config.getString("Messages.SubregionRegionkindError");
        SUB_REGION_PAYBACKPERCENTAGE_ERROR = config.getString("Messages.SubregionPaybackPercentageError");
        SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS = config.getString("Messages.SubRegionRegionkindOnlyForSubregions");
        SUB_REGION_TELEPORT_LOCATION_ERROR = config.getString("Messages.SubregionTeleportLocationError");
        REGION_NOT_REGISTRED = config.getString("Messages.RegionNotRegistred");
        FIRST_POSITION_SET = config.getString("Messages.FirstPositionSet");
        SECOND_POSITION_SET = config.getString("Messages.SecondPositionSet");
        MARK_IN_OTHER_REGION_REMOVING = config.getString("Messages.MarkInOtherRegion");
        PARENT_REGION_NOT_OWN = config.getString("Messages.ParentRegionNotOwn");
        ADD_MEMBER_MAX_MEMBERS_EXCEEDED = config.getString("Messages.AddMemberMaxMembersExceeded");
        NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD = config.getString("Messages.SubRegionRemoveNoPermissionBecauseSold");
        NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE = config.getString("Messages.SubRegionRemoveNoPermissionBecauseAvailable");
        POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION = config.getString("Messages.PosCloudNotBeSetMarkOutsideRegion");
        ALREADY_SUB_REGION_AT_THIS_POSITION = config.getString("Messages.SubRegionAlreadyAtThisPosition");
        SUB_REGION_LIMIT_REACHED = config.getString("Messages.SubRegionLimitReached");
        SELECTION_INVALID = config.getString("Messages.SelectionInvalid");
        REGION_CREATED_AND_SAVED = config.getString("Messages.RegionCreatedAndSaved");
        REGION_NOT_A_SUBREGION = config.getString("Messages.RegionNotASubregion");
        REGION_DELETED = config.getString("Messages.RegionDeleted");
        SELLREGION_NAME = config.getString("Messages.SellregionName");
        CONTRACTREGION_NAME = config.getString("Messages.ContractregionName");
        RENTREGION_NAME = config.getString("Messages.RentregionName");
        GUI_SUBREGION_ITEM_BUTTON = config.getString("Messages.GUISubregionsButton");
        GUI_SUBREGION_LIST_MENU_NAME = config.getString("Messages.GUISubregionListMenuName");
        GUI_SUBREGION_HOTEL_BUTTON = config.getString("Messages.GUIHotelButton");
        GUI_SUBREGION_DELETE_REGION_BUTTON = config.getString("Messages.GUIDeleteRegionButton");
        GUI_TELEPORT_TO_SIGN_OR_REGION = config.getString("Messages.GUITeleportToSignOrRegionButton");
        GUI_TELEPORT_TO_SIGN = config.getString("Messages.GUIRegionfinderTeleportToSignButton");
        GUI_TELEPORT_TO_REGION = config.getString("Messages.GUIRegionfinderTeleportToRegionButton");
        GUI_MEMBER_REGIONS_MENU_NAME = config.getString("Messages.GUIMemberRegionsMenuName");
        GUI_NEXT_PAGE = config.getString("Messages.GUINextPageButton");
        GUI_PREV_PAGE = config.getString("Messages.GUIPrevPageButton");
        ENABLED = config.getString("Messages.Enabled");
        DISABLED = config.getString("Messages.Disabled");
        SOLD = config.getString("Messages.Sold");
        AVAILABLE = config.getString("Messages.Available");
        GUI_SUBREGION_HOTEL_BUTTON_LORE = config.getStringList("Messages.GUIHotelButtonLore");
        GUI_SUBREGION_REGION_INFO_SELL = config.getStringList("Messages.GUISubregionInfoSell");
        GUI_SUBREGION_REGION_INFO_RENT = config.getStringList("Messages.GUISubregionInfoRent");
        GUI_SUBREGION_REGION_INFO_CONTRACT = config.getStringList("Messages.GUISubregionInfoContract");
        GUI_REGIONFINDER_REGION_INFO_SELL = config.getStringList("Messages.GUIRegionfinderInfoSell");
        GUI_REGIONFINDER_REGION_INFO_RENT = config.getStringList("Messages.GUIRegionfinderInfoRent");
        GUI_REGIONFINDER_REGION_INFO_CONTRACT = config.getStringList("Messages.GUIRegionfinderInfoContract");
        SELECTION_SAVED_CREATE_SIGN = config.getStringList("Messages.SubregionCreationCreateSignInfo");
        SUBREGION_TOOL_INSTRUCTION = config.getStringList("Messages.SubregionCreationSelectAreaInfo");
        SUBREGION_TOOL_ALREADY_OWNED = config.getString("Messages.SubregionToolAlreadyOwned");
        REGION_INFO_IS_USER_RESTORABLE = config.getString("Messages.RegionInfoIsUserRestorable");
        DELETE_REGION_WARNING_NAME = config.getString("Messages.DeleteRegionWarningName");
        UNSELL_REGION_BUTTON = config.getString("Messages.UnsellRegionButton");
        UNSELL_REGION_BUTTON_LORE = config.getStringList("Messages.UnsellRegionButtonLore");
        UNSELL_REGION_WARNING_NAME = config.getString("Messages.UnsellRegionWarningName");
        AUTOPRICE_LIST = config.getString("Messages.AutopriceList");
        SUBREGION_HELP_HEADLINE = config.getString("Messages.SubregionHelpHeadline");
        GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE = config.getStringList("Messages.GUISubregionManagerNoSubregionItemLore");
        GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM = config.getString("Messages.GUISubregionManagerNoSubregionItem");
        SCHEMATIC_NOT_FOUND_ERROR_USER = config.getString("Messages.SchematicNotFoundErrorUser");
        SCHEMATIC_NOT_FOUND_ERROR_ADMIN = config.getString("Messages.SchematicNotFoundErrorAdmin");

        SELLTYPE_NOT_EXIST = config.getString("Messages.SelltypeNotExist");
        SIGN_LINK_MODE_ACTIVATED = config.getString("Messages.SignLinkModeActivated");
        SIGN_LINK_MODE_DEACTIVATED = config.getString("Messages.SignLinkModeDeactivated");
        SIGN_LINK_MODE_ALREADY_DEACTIVATED = config.getString("Messages.SignLinkModeAlreadyDeactivated");
        SIGN_LINK_MODE_PRESET_NOT_PRICEREADY = config.getString("Messages.SignLinkModePresetNotPriceready");
        SIGN_LINK_MODE_NO_PRESET_SELECTED = config.getString("Messages.SignLinkModeNoPresetSelected");
        SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION = config.getString("Messages.SignLinkModeSignBelongsToAnotherRegion");
        SIGN_LINK_MODE_SIGN_SELECTED = config.getString("Messages.SignLinkModeSignSelected");
        SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS = config.getString("Messages.SignLinkModeMultipleWgRegionsAtPosition");
        SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION = config.getString("Messages.SignLinkModeNoWgRegionAtPosition");
        SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD = config.getString("Messages.SignLinkModeCouldNotIdentifyWorld");
        SIGN_LINK_MODE_NO_SIGN_SELECTED = config.getString("Messages.SignLinkModeNoSignSelected");
        SIGN_LINK_MODE_NO_WG_REGION_SELECTED = config.getString("Messages.SignLinkModeNoWgRegionSelected");
        SIGN_LINK_MODE_REGION_SELECTED = config.getString("Messages.SignLinkModeSelectedRegion");

        ENTITYLIMIT_HELP_HEADLINE = config.getString("Messages.EntityLimitHelpHeadline");
        ENTITYLIMITGROUP_DOES_NOT_EXIST = config.getString("Messages.EntityLimitGroupNotExist");
        ENTITYLIMIT_SET = config.getString("Messages.EntityLimitSet");
        ENTITYLIMIT_REMOVED = config.getString("Messages.EntityLimitRemoved");
        ENTITYTYPE_DOES_NOT_EXIST = config.getString("Messages.EntityTypeDoesNotExist");
        ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT = config.getString("Messages.EntityLimitGroupNotContainEntityLimit");
        ENTITYLIMIT_TOTAL = config.getString("Messages.EntityLimitTotal");
        ENTITYLIMIT_CHECK_HEADLINE = config.getString("Messages.EntityLimitCheckHeadline");
        ENTITYLIMIT_CHECK_PATTERN = config.getString("Messages.EntityLimitCheckPattern");
        ENTITYLIMITGROUP_ALREADY_EXISTS = config.getString("Messages.EntityLimitGroupAlreadyExists");
        ENTITYLIMITGROUP_CREATED = config.getString("Messages.EntityLimitGroupCreated");
        ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM = config.getString("Messages.EntityLimitGroupCanNotRemoveSystem");
        ENTITYLIMITGROUP_DELETED = config.getString("Messages.EntityLimitGroupDeleted");
        ENTITYLIMITGROUP_INFO_HEADLINE = config.getString("Messages.EntityLimitGroupInfoHeadline");
        ENTITYLIMITGROUP_INFO_GROUPNAME = config.getString("Messages.EntityLimitGroupInfoGroupname");
        ENTITYLIMITGROUP_INFO_PATTERN = config.getString("Messages.EntityLimitGroupInfoPattern");
        ENTITYLIMITGROUP_LIST_HEADLINE = config.getString("Messages.EntityLimitGroupListHeadline");
        ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY = config.getString("Messages.EntityLimitGroupCouldNotspawnEntity");
        REGION_INFO_ENTITYLIMITGROUP = config.getString("Messages.RegionInfoEntityLimit");
        ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS = config.getString("Messages.SubregionEntityLimitOnlyForSubregions");
        MASSACTION_SPLITTER = config.getString("Messages.MassactionSplitter");
        SUB_REGION_ENTITYLIMITGROUP_ERROR = config.getString("Messages.SubregionEntityLimitError");
        SUB_REGION_FLAGGROUP_ERROR = config.getString("Messages.SubregionFlagGroupError");
        GUI_ENTITYLIMIT_ITEM_BUTTON = config.getString("Messages.GUIEntityLimitItemButton");
        GUI_ENTITYLIMIT_ITEM_LORE = config.getStringList("Messages.GUIEntityLimitItemLore");
        GUI_ENTITYLIMIT_ITEM_INFO_PATTERN = config.getString("Messages.GUIEntityLimitInfoPattern");
        ENTITYLIMIT_CHECK_EXTENSION_INFO = config.getString("Messages.EntityLimitCheckExtensionInfo");
        GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO = config.getString("Messages.GUIEntityLimitInfoExtensionInfo");
        ENTITYLIMITGROUP_INFO_EXTENSION_INFO = config.getString("Messages.EntityLimitInfoExtensionInfo");
        ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED = config.getString("Messages.EntityLimitGroupEntityLimitAlreadyUnlimited");
        ENTITYLIMITGROUP_EXTRA_ENTITIES_SET = config.getString("Messages.EntityLimitGroupExtraEntitiesSet");
        ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS = config.getString("Messages.EntityLimitGroupExtraEntitiesExpandSuccess");
        ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED = config.getString("Messages.EntityLimitGroupExtraEntitiesHardlimitReached");
        ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR = config.getString("Messages.EntityLimitGroupExtraEntitiesSetSubregionError");
        ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR = config.getString("Messages.EntityLimitGroupExtraEntitiesBuySubregionError");
        ARM_BASIC_COMMAND_MESSAGE = config.getString("Messages.ArmBasicCommandMessage");

        REGIONKIND_CREATED = config.getString("Messages.RegionKindCreated");
        REGIONKIND_ALREADY_EXISTS = config.getString("Messages.RegionKindAlreadyExists");
        REGIONKIND_DELETED = config.getString("Messages.RegionKindDeleted");
        REGIONKIND_CAN_NOT_REMOVE_SYSTEM = config.getString("Messages.RegionKindCanNotRemoveSystem");
        REGIONKIND_LIST_HEADLINE = config.getString("Messages.RegionKindListHeadline");
        REGIONKIND_MODIFIED = config.getString("Messages.RegionKindModified");
        MATERIAL_NOT_FOUND = config.getString("Messages.MaterialNotFound");
        REGIONKIND_LORE_LINE_NOT_EXIST = config.getString("Messages.RegionKindLoreLineNotExist");
        REGIONKIND_INFO_HEADLINE = config.getString("Messages.RegionKindInfoHeadline");
        REGIONKIND_INFO_INTERNAL_NAME = config.getString("Messages.RegionKindInfoInternalName");
        REGIONKIND_INFO_DISPLAY_NAME = config.getString("Messages.RegionKindInfoDisplayName");
        REGIONKIND_INFO_MATERIAL = config.getString("Messages.RegionKindInfoMaterial");
        REGIONKIND_INFO_DISPLAY_IN_GUI = config.getString("Messages.RegionKindInfoDisplayInGui");
        REGIONKIND_INFO_DISPLAY_IN_LIMITS = config.getString("Messages.RegionKindInfoDisplayInLimits");
        REGIONKIND_INFO_LORE = config.getString("Messages.RegionKindInfoLore");
        REGIONKIND_HELP_HEADLINE = config.getString("Messages.RegionKindHelpHeadline");
        PLAYER_NOT_FOUND = config.getString("Messages.PlayerNotFound");

        REGION_INFO_SELLREGION = config.getStringList("Messages.RegionInfoSellregionUser");
        REGION_INFO_RENTREGION = config.getStringList("Messages.RegionInfoRentregionUser");
        REGION_INFO_CONTRACTREGION = config.getStringList("Messages.RegionInfoContractregionUser");
        REGION_INFO_SELLREGION_ADMIN = config.getStringList("Messages.RegionInfoSellregionAdmin");
        REGION_INFO_RENTREGION_ADMIN = config.getStringList("Messages.RegionInfoRentregionAdmin");
        REGION_INFO_CONTRACTREGION_ADMIN = config.getStringList("Messages.RegionInfoContractregionAdmin");
        REGION_INFO_SELLREGION_SUBREGION = config.getStringList("Messages.RegionInfoSellregionSubregion");
        REGION_INFO_RENTREGION_SUBREGION = config.getStringList("Messages.RegionInfoRentregionSubregion");
        REGION_INFO_CONTRACTREGION_SUBREGION = config.getStringList("Messages.RegionInfoContractregionSubregion");

        GUI_FLAGEDITOR_BUTTON = config.getString("Messages.GUIFlageditorButton");
        GUI_FLAGEDITOR_MENU_NAME = config.getString("Messages.GUIFlageditorMenuName");
        GUI_FLAGEDITOR_DELETE_FLAG_BUTTON = config.getString("Messages.GUIFlageditorDeleteFlagButton");
        GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON = config.getString("Messages.GUIFlageditorSetFlagGroupAllButton");
        GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON = config.getString("Messages.GUIFlageditorSetFlagGroupMembersButton");
        GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON = config.getString("Messages.GUIFlageditorSetFlagGroupOwnersButton");
        GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON = config.getString("Messages.GUIFlageditorSetFlagGroupNonMembersButton");
        GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON = config.getString("Messages.GUIFlageditorSetFlagGroupNonOwnersButton");
        GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON = config.getString("Messages.GUIFlageditorSetStateflagAllowButton");
        GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON = config.getString("Messages.GUIFlageditorSetStateflagDenyButton");
        GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON = config.getString("Messages.GUIFlageditorSetBooleanflagTrueButton");
        GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON = config.getString("Messages.GUIFlageditorSetBooleanflagFalseButton");
        GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON = config.getString("Messages.GUIFlageditorSetStringflagSetMessageButton");
        GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON = config.getString("Messages.GUIFlageditorSetIntegerflagSetIntegerButton");
        GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON = config.getString("Messages.GUIFlageditorSetDoubleflagSetDoubleButton");
        FlAGEDITOR_FLAG_NOT_ACTIVATED = config.getString("Messages.FlageditorFlagNotActivated");
        FLAGEDITOR_FLAG_HAS_BEEN_UPDATED = config.getString("Messages.FlageditorFlagHasBeenUpdated");
        FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED = config.getString("Messages.FlageditorFlagCouldNotBeUpdated");
        FlAGEDITOR_FLAG_HAS_BEEN_DELETED = config.getString("Messages.FlageditorFlagHasBeenDeleted");
        FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO = config.getString("Messages.FlageditorStringflagSetMessageInfo");
        FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO = config.getString("Messages.FlageditorIntegerflagSetMessageInfo");
        FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO = config.getString("Messages.FlageditorDoubleflagSetMessageInfo");
        GUI_FLAGEDITOR_RESET_BUTTON = config.getString("Messages.GUIFlageditorResetButton");
        GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON = config.getString("Messages.GUIFlageditorUnknownFlagSetPropertiesButton");
        FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO = config.getString("Messages.GUIFlageditorUnknownFlagSetPropertiesInfo");
        FLAGGROUP_DOES_NOT_EXIST = config.getString("Messages.FlaggroupDoesNotExist");
        SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS = config.getString("Messages.SubregionFlaggroupOnlyForSubregions");
        REGION_INFO_FLAGGROUP = config.getString("Messages.RegionInfoFlaggroup");
        INFO_DEACTIVATED = config.getString("Messages.InfoDeactivated");
        INFO_REGION_NOT_SOLD = config.getString("Messages.InfoNotSold");
        INFO_NOW = config.getString("Messages.InfoNow");
        INFO_NOT_CALCULATED = config.getString("Messages.InfoNotCalculated");
        COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG = config.getString("Messages.CouldNotFindOrLoadSchematicLog");
        REGION_SOLD_BACK_SUCCESSFULLY = config.getString("Messages.RegionSoldBackSuccessfully");
        REGION_MODIFIED_BOOLEAN = config.getString("Messages.RegionModifiedBoolean");
        REGION_MODIFIED = config.getString("Messages.RegionModified");
        UPDATING_SCHEMATIC = config.getString("Messages.UpdatingSchematic");
        SCHEMATIC_UPDATED = config.getString("Messages.SchematicUpdated");
        CONTRACTREGION_TERMINATED = config.getString("Messages.ContractRegionTerminated");
        CONTRACTREGION_REACTIVATED = config.getString("Messages.ContractRegionReactivated");
        REGION_INFO_FEATURE_DISABLED = config.getString("Messages.RegionInfoFeatureDisabled");
        FLAGGROUP_FEATURE_DISABLED = config.getString("Messages.FlagGroupFeatureDisabled");
        BACKUP_CREATED = config.getString("Messages.BackupCreated");
        BACKUP_RESTORED = config.getString("Messages.BackupRestored");
        COULD_NOT_LOAD_BACKUP = config.getString("Messages.CouldNotLoadBackup");
        BACKUP_LIST_HEADER = config.getString("Messages.BackupListHeader");

        Messages.translateColorCodes();
    }

    private static void translateColorCodes() {
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
        REGION_INFO_PRICE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_PRICE);
        REGION_INFO_MAX_MEMBERS = ChatColor.translateAlternateColorCodes('&', REGION_INFO_MAX_MEMBERS);
        REGION_INFO_TYPE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_TYPE);
        COMMAND_ONLY_INGAME = ChatColor.translateAlternateColorCodes('&', COMMAND_ONLY_INGAME);
        REGION_INFO_INACTIVITYRESET = ChatColor.translateAlternateColorCodes('&', REGION_INFO_INACTIVITYRESET);
        GUI_MAIN_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MAIN_MENU_NAME);
        GUI_GO_BACK = ChatColor.translateAlternateColorCodes('&', GUI_GO_BACK);
        GUI_MY_OWN_REGIONS = ChatColor.translateAlternateColorCodes('&', GUI_MY_OWN_REGIONS);
        GUI_MY_MEMBER_REGIONS = ChatColor.translateAlternateColorCodes('&', GUI_MY_MEMBER_REGIONS);
        GUI_SEARCH_FREE_REGION = ChatColor.translateAlternateColorCodes('&', GUI_SEARCH_FREE_REGION);
        GUI_OWN_REGIONS_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_OWN_REGIONS_MENU_NAME);
        SUB_REGION_FLAGGROUP_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_FLAGGROUP_ERROR);
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
        ADD_MEMBER_MAX_MEMBERS_EXCEEDED = ChatColor.translateAlternateColorCodes('&', ADD_MEMBER_MAX_MEMBERS_EXCEEDED);
        SIGN_REMOVED_FROM_REGION = ChatColor.translateAlternateColorCodes('&', SIGN_REMOVED_FROM_REGION);
        REGION_TELEPORT_MESSAGE = ChatColor.translateAlternateColorCodes('&', REGION_TELEPORT_MESSAGE);
        NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION = ChatColor.translateAlternateColorCodes('&', NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
        NO_FREE_REGION_WITH_THIS_KIND = ChatColor.translateAlternateColorCodes('&', NO_FREE_REGION_WITH_THIS_KIND);
        REGIONKIND_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGIONKIND_DOES_NOT_EXIST);
        REGION_NOW_AVIABLE = ChatColor.translateAlternateColorCodes('&', REGION_NOW_AVIABLE);
        NO_REGION_AT_PLAYERS_POSITION = ChatColor.translateAlternateColorCodes('&', NO_REGION_AT_PLAYERS_POSITION);
        REGION_ADD_MEMBER_NOT_ONLINE = ChatColor.translateAlternateColorCodes('&', REGION_ADD_MEMBER_NOT_ONLINE);
        REGION_ADD_MEMBER_ADDED = ChatColor.translateAlternateColorCodes('&', REGION_ADD_MEMBER_ADDED);
        REGION_REMOVE_MEMBER_NOT_A_MEMBER = ChatColor.translateAlternateColorCodes('&', REGION_REMOVE_MEMBER_NOT_A_MEMBER);
        REGION_REMOVE_MEMBER_REMOVED = ChatColor.translateAlternateColorCodes('&', REGION_REMOVE_MEMBER_REMOVED);
        GUI_RESET_REGION_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_BUTTON);
        GUI_RESET_REGION_WARNING_NAME = ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_WARNING_NAME);
        RESET_COMPLETE = ChatColor.translateAlternateColorCodes('&', RESET_COMPLETE);
        RESET_REGION_COOLDOWN_ERROR = ChatColor.translateAlternateColorCodes('&', RESET_REGION_COOLDOWN_ERROR);
        GUI_TAKEOVER_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_TAKEOVER_MENU_NAME);
        REGION_TRANSFER_COMPLETE_MESSAGE = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_COMPLETE_MESSAGE);
        SUB_REGION_PAYBACKPERCENTAGE_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_PAYBACKPERCENTAGE_ERROR);
        GUI_CLOSE = ChatColor.translateAlternateColorCodes('&', GUI_CLOSE);
        RENT_SIGN1 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN1);
        RENT_SIGN2 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN2);
        RENT_SIGN3 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN3);
        RENT_SIGN4 = ChatColor.translateAlternateColorCodes('&', RENT_SIGN4);
        RENTED_SIGN1 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN1);
        RENTED_SIGN2 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN2);
        RENTED_SIGN3 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN3);
        RENTED_SIGN4 = ChatColor.translateAlternateColorCodes('&', RENTED_SIGN4);
        RENT_EXTEND_MESSAGE = ChatColor.translateAlternateColorCodes('&', RENT_EXTEND_MESSAGE);
        RENT_EXTEND_MAX_RENT_TIME_EXCEEDED = ChatColor.translateAlternateColorCodes('&', RENT_EXTEND_MAX_RENT_TIME_EXCEEDED);
        GUI_EXTEND_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_EXTEND_BUTTON);
        REGION_INFO_MAX_RENT_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_MAX_RENT_TIME);
        REGION_INFO_EXTEND_PER_CLICK = ChatColor.translateAlternateColorCodes('&', REGION_INFO_EXTEND_PER_CLICK);
        REGION_INFO_HOTEL = ChatColor.translateAlternateColorCodes('&', REGION_INFO_HOTEL);
        REGION_ERROR_CAN_NOT_BUILD_HERE = ChatColor.translateAlternateColorCodes('&', REGION_ERROR_CAN_NOT_BUILD_HERE);
        COMPLETE = ChatColor.translateAlternateColorCodes('&', COMPLETE);
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
        PRESET_SET = ChatColor.translateAlternateColorCodes('&', PRESET_SET);
        REGION_INFO_AUTORESTORE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_AUTORESTORE);
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
        REGION_INFO_AUTO_EXTEND_TIME = ChatColor.translateAlternateColorCodes('&', REGION_INFO_AUTO_EXTEND_TIME);
        CONTRACT_REGION_STATUS_ACTIVE = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_ACTIVE);
        CONTRACT_REGION_STATUS_TERMINATED = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION_STATUS_TERMINATED);
        REGION_IS_NOT_A_CONTRACT_REGION = ChatColor.translateAlternateColorCodes('&', REGION_IS_NOT_A_CONTRACT_REGION);
        GUI_OWNER_MEMBER_INFO_ITEM = ChatColor.translateAlternateColorCodes('&', GUI_OWNER_MEMBER_INFO_ITEM);
        REGION_TRANSFER_MEMBER_NOT_ONLINE = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_MEMBER_NOT_ONLINE);
        REGION_TRANSFER_LIMIT_ERROR = ChatColor.translateAlternateColorCodes('&', REGION_TRANSFER_LIMIT_ERROR);
        TIME_SECONDS_SINGULAR = ChatColor.translateAlternateColorCodes('&', TIME_SECONDS_SINGULAR);
        TIME_MINUTES_SINGULAR = ChatColor.translateAlternateColorCodes('&', TIME_MINUTES_SINGULAR);
        TIME_HOURS_SINGULAR = ChatColor.translateAlternateColorCodes('&', TIME_HOURS_SINGULAR);
        TIME_DAYS_SINGULAR = ChatColor.translateAlternateColorCodes('&', TIME_DAYS_SINGULAR);
        TIME_SECONDS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_SECONDS_SHORT);
        TIME_MINUTES_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_MINUTES_SHORT);
        TIME_HOURS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_HOURS_SHORT);
        TIME_DAYS_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_DAYS_SHORT);
        TIME_SECONDS_PLURAL = ChatColor.translateAlternateColorCodes('&', TIME_SECONDS_PLURAL);
        TIME_MINUTES_PLURAL = ChatColor.translateAlternateColorCodes('&', TIME_MINUTES_PLURAL);
        TIME_HOURS_PLURAL = ChatColor.translateAlternateColorCodes('&', TIME_HOURS_PLURAL);
        TIME_DAYS_PLURAL = ChatColor.translateAlternateColorCodes('&', TIME_DAYS_PLURAL);
        NOT_A_MEMBER_OR_OWNER = ChatColor.translateAlternateColorCodes('&', NOT_A_MEMBER_OR_OWNER);
        YES = ChatColor.translateAlternateColorCodes('&', YES);
        NO = ChatColor.translateAlternateColorCodes('&', NO);
        REGION_STATS = ChatColor.translateAlternateColorCodes('&', REGION_STATS);
        RENT_REGION = ChatColor.translateAlternateColorCodes('&', RENT_REGION);
        SELL_REGION = ChatColor.translateAlternateColorCodes('&', SELL_REGION);
        CONTRACT_REGION = ChatColor.translateAlternateColorCodes('&', CONTRACT_REGION);
        REGION_STATS_PATTERN = ChatColor.translateAlternateColorCodes('&', REGION_STATS_PATTERN);
        TELEPORTER_NO_SAVE_LOCATION_FOUND = ChatColor.translateAlternateColorCodes('&', TELEPORTER_NO_SAVE_LOCATION_FOUND);
        TELEPORTER_DONT_MOVE = ChatColor.translateAlternateColorCodes('&', TELEPORTER_DONT_MOVE);
        TELEPORTER_TELEPORTATION_ABORDED = ChatColor.translateAlternateColorCodes('&', TELEPORTER_TELEPORTATION_ABORDED);
        OFFER_SENT = ChatColor.translateAlternateColorCodes('&', OFFER_SENT);
        OFFER_ACCEPTED_SELLER = ChatColor.translateAlternateColorCodes('&', OFFER_ACCEPTED_SELLER);
        OFFER_ACCEPTED_BUYER = ChatColor.translateAlternateColorCodes('&', OFFER_ACCEPTED_BUYER);
        NO_OFFER_TO_ANSWER = ChatColor.translateAlternateColorCodes('&', NO_OFFER_TO_ANSWER);
        OFFER_REJECTED = ChatColor.translateAlternateColorCodes('&', OFFER_REJECTED);
        OFFER_HAS_BEEN_REJECTED = ChatColor.translateAlternateColorCodes('&', OFFER_HAS_BEEN_REJECTED);
        NO_OFFER_TO_REJECT = ChatColor.translateAlternateColorCodes('&', NO_OFFER_TO_REJECT);
        OFFER_CANCELED = ChatColor.translateAlternateColorCodes('&', OFFER_CANCELED);
        OFFER_HAS_BEEN_CANCELLED = ChatColor.translateAlternateColorCodes('&', OFFER_HAS_BEEN_CANCELLED);
        NO_OFFER_TO_CANCEL = ChatColor.translateAlternateColorCodes('&', NO_OFFER_TO_CANCEL);
        BUYER_ALREADY_GOT_AN_OFFER = ChatColor.translateAlternateColorCodes('&', BUYER_ALREADY_GOT_AN_OFFER);
        SELLER_ALREADY_CREATED_AN_OFFER = ChatColor.translateAlternateColorCodes('&', SELLER_ALREADY_CREATED_AN_OFFER);
        SELLER_DOES_NOT_LONGER_OWN_REGION = ChatColor.translateAlternateColorCodes('&', SELLER_DOES_NOT_LONGER_OWN_REGION);
        INCOMING_OFFER = ChatColor.translateAlternateColorCodes('&', INCOMING_OFFER);
        SELECTED_PLAYER_NOT_ONLINE = ChatColor.translateAlternateColorCodes('&', SELECTED_PLAYER_NOT_ONLINE);
        BAD_SYNTAX = ChatColor.translateAlternateColorCodes('&', BAD_SYNTAX);
        BAD_SYNTAX_SPLITTER = ChatColor.translateAlternateColorCodes('&', BAD_SYNTAX_SPLITTER);
        OFFER_TIMED_OUT = ChatColor.translateAlternateColorCodes('&', OFFER_TIMED_OUT);
        HELP_HEADLINE = ChatColor.translateAlternateColorCodes('&', HELP_HEADLINE);
        PRESET_SETUP_COMMANDS = ChatColor.translateAlternateColorCodes('&', PRESET_SETUP_COMMANDS);
        PRICE_CAN_NOT_BE_NEGATIVE = ChatColor.translateAlternateColorCodes('&', PRICE_CAN_NOT_BE_NEGATIVE);
        SELLBACK_WARNING = ChatColor.translateAlternateColorCodes('&', SELLBACK_WARNING);
        SUB_REGION_INACTIVITYRESET_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_INACTIVITYRESET_ERROR);
        SUB_REGION_AUTORESTORE_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_AUTORESTORE_ERROR);
        REGION_NOT_RESETTABLE = ChatColor.translateAlternateColorCodes('&', REGION_NOT_RESETTABLE);
        REGION_SELECTED_MULTIPLE_REGIONS = ChatColor.translateAlternateColorCodes('&', REGION_SELECTED_MULTIPLE_REGIONS);
        SUB_REGION_REGIONKIND_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_REGIONKIND_ERROR);
        SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS = ChatColor.translateAlternateColorCodes('&', SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS);
        SUB_REGION_TELEPORT_LOCATION_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_TELEPORT_LOCATION_ERROR);
        REGION_INFO_ALLOWED_SUBREGIONS = ChatColor.translateAlternateColorCodes('&', REGION_INFO_ALLOWED_SUBREGIONS);
        REGION_INFO_EXPIRED = ChatColor.translateAlternateColorCodes('&', REGION_INFO_EXPIRED);
        REGION_NOT_REGISTRED = ChatColor.translateAlternateColorCodes('&', REGION_NOT_REGISTRED);
        FIRST_POSITION_SET = ChatColor.translateAlternateColorCodes('&', FIRST_POSITION_SET);
        SECOND_POSITION_SET = ChatColor.translateAlternateColorCodes('&', SECOND_POSITION_SET);
        MARK_IN_OTHER_REGION_REMOVING = ChatColor.translateAlternateColorCodes('&', MARK_IN_OTHER_REGION_REMOVING);
        PARENT_REGION_NOT_OWN = ChatColor.translateAlternateColorCodes('&', PARENT_REGION_NOT_OWN);
        NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD = ChatColor.translateAlternateColorCodes('&', NOT_ALLOWED_TO_REMOVE_SUB_REGION_SOLD);
        NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE = ChatColor.translateAlternateColorCodes('&', NOT_ALLOWED_TO_REMOVE_SUB_REGION_AVAILABLE);
        POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION = ChatColor.translateAlternateColorCodes('&', POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION);
        ALREADY_SUB_REGION_AT_THIS_POSITION = ChatColor.translateAlternateColorCodes('&', ALREADY_SUB_REGION_AT_THIS_POSITION);
        SUB_REGION_LIMIT_REACHED = ChatColor.translateAlternateColorCodes('&', SUB_REGION_LIMIT_REACHED);
        SELECTION_INVALID = ChatColor.translateAlternateColorCodes('&', SELECTION_INVALID);
        REGION_CREATED_AND_SAVED = ChatColor.translateAlternateColorCodes('&', REGION_CREATED_AND_SAVED);
        REGION_NOT_A_SUBREGION = ChatColor.translateAlternateColorCodes('&', REGION_NOT_A_SUBREGION);
        REGION_DELETED = ChatColor.translateAlternateColorCodes('&', REGION_DELETED);
        SUB_REGION_MAX_MEMBERS_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_MAX_MEMBERS_ERROR);
        SUB_REGION_IS_USER_RESETTABLE_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_IS_USER_RESETTABLE_ERROR);
        SELLREGION_NAME = ChatColor.translateAlternateColorCodes('&', SELLREGION_NAME);
        CONTRACTREGION_NAME = ChatColor.translateAlternateColorCodes('&', CONTRACTREGION_NAME);
        RENTREGION_NAME = ChatColor.translateAlternateColorCodes('&', RENTREGION_NAME);
        GUI_SUBREGION_ITEM_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_ITEM_BUTTON);
        GUI_SUBREGION_LIST_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_LIST_MENU_NAME);
        GUI_SUBREGION_HOTEL_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_HOTEL_BUTTON);
        GUI_SUBREGION_DELETE_REGION_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_DELETE_REGION_BUTTON);
        GUI_TELEPORT_TO_SIGN_OR_REGION = ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_SIGN_OR_REGION);
        GUI_TELEPORT_TO_SIGN = ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_SIGN);
        GUI_TELEPORT_TO_REGION = ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_REGION);
        GUI_NEXT_PAGE = ChatColor.translateAlternateColorCodes('&', GUI_NEXT_PAGE);
        GUI_PREV_PAGE = ChatColor.translateAlternateColorCodes('&', GUI_PREV_PAGE);
        ENABLED = ChatColor.translateAlternateColorCodes('&', ENABLED);
        DISABLED = ChatColor.translateAlternateColorCodes('&', DISABLED);
        SOLD = ChatColor.translateAlternateColorCodes('&', SOLD);
        AVAILABLE = ChatColor.translateAlternateColorCodes('&', AVAILABLE);
        REGION_INFO_IS_USER_RESTORABLE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_IS_USER_RESTORABLE);
        DELETE_REGION_WARNING_NAME = ChatColor.translateAlternateColorCodes('&', DELETE_REGION_WARNING_NAME);
        UNSELL_REGION_BUTTON = ChatColor.translateAlternateColorCodes('&', UNSELL_REGION_BUTTON);
        UNSELL_REGION_WARNING_NAME = ChatColor.translateAlternateColorCodes('&', UNSELL_REGION_WARNING_NAME);
        REGION_INFO_AUTOPRICE = ChatColor.translateAlternateColorCodes('&', REGION_INFO_AUTOPRICE);
        AUTOPRICE_LIST = ChatColor.translateAlternateColorCodes('&', AUTOPRICE_LIST);
        SUBREGION_HELP_HEADLINE = ChatColor.translateAlternateColorCodes('&', SUBREGION_HELP_HEADLINE);
        SUBREGION_TOOL_ALREADY_OWNED = ChatColor.translateAlternateColorCodes('&', SUBREGION_TOOL_ALREADY_OWNED);
        GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM = ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM);
        SCHEMATIC_NOT_FOUND_ERROR_USER = ChatColor.translateAlternateColorCodes('&', SCHEMATIC_NOT_FOUND_ERROR_USER);
        SCHEMATIC_NOT_FOUND_ERROR_ADMIN = ChatColor.translateAlternateColorCodes('&', SCHEMATIC_NOT_FOUND_ERROR_ADMIN);
        TIME_UNIT_SPLITTER = ChatColor.translateAlternateColorCodes('&', TIME_UNIT_SPLITTER);
        SELLTYPE_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', SELLTYPE_NOT_EXIST);
        SIGN_LINK_MODE_ACTIVATED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_ACTIVATED);
        SIGN_LINK_MODE_DEACTIVATED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_DEACTIVATED);
        SIGN_LINK_MODE_ALREADY_DEACTIVATED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_ALREADY_DEACTIVATED);
        SIGN_LINK_MODE_PRESET_NOT_PRICEREADY = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_PRESET_NOT_PRICEREADY);
        SIGN_LINK_MODE_NO_PRESET_SELECTED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_NO_PRESET_SELECTED);
        SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION);
        SIGN_LINK_MODE_SIGN_SELECTED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_SIGN_SELECTED);
        SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS);
        SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION);
        SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD);
        SIGN_LINK_MODE_NO_SIGN_SELECTED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_NO_SIGN_SELECTED);
        SIGN_LINK_MODE_NO_WG_REGION_SELECTED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_NO_WG_REGION_SELECTED);
        SIGN_LINK_MODE_REGION_SELECTED = ChatColor.translateAlternateColorCodes('&', SIGN_LINK_MODE_REGION_SELECTED);
        TIME_UNIT_SPLITTER_SHORT = ChatColor.translateAlternateColorCodes('&', TIME_UNIT_SPLITTER_SHORT);

        ENTITYLIMIT_HELP_HEADLINE = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_HELP_HEADLINE);
        ENTITYLIMITGROUP_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_DOES_NOT_EXIST);
        ENTITYLIMIT_SET = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_SET);
        ENTITYLIMIT_REMOVED = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_REMOVED);
        ENTITYTYPE_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', ENTITYTYPE_DOES_NOT_EXIST);
        ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT);
        ENTITYLIMIT_TOTAL = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_TOTAL);
        ENTITYLIMIT_CHECK_HEADLINE = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_CHECK_HEADLINE);
        ENTITYLIMIT_CHECK_PATTERN = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_CHECK_PATTERN);
        ENTITYLIMITGROUP_ALREADY_EXISTS = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_ALREADY_EXISTS);
        ENTITYLIMITGROUP_CREATED = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_CREATED);
        ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM);
        ENTITYLIMITGROUP_DELETED = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_DELETED);
        ENTITYLIMITGROUP_INFO_HEADLINE = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_INFO_HEADLINE);
        ENTITYLIMITGROUP_INFO_GROUPNAME = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_INFO_GROUPNAME);
        ENTITYLIMITGROUP_INFO_PATTERN = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_INFO_PATTERN);
        ENTITYLIMITGROUP_LIST_HEADLINE = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_LIST_HEADLINE);
        ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY);
        REGION_INFO_ENTITYLIMITGROUP = ChatColor.translateAlternateColorCodes('&', REGION_INFO_ENTITYLIMITGROUP);
        ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        MASSACTION_SPLITTER = ChatColor.translateAlternateColorCodes('&', MASSACTION_SPLITTER);
        SUB_REGION_ENTITYLIMITGROUP_ERROR = ChatColor.translateAlternateColorCodes('&', SUB_REGION_ENTITYLIMITGROUP_ERROR);
        GUI_ENTITYLIMIT_ITEM_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_ENTITYLIMIT_ITEM_BUTTON);
        GUI_ENTITYLIMIT_ITEM_INFO_PATTERN = ChatColor.translateAlternateColorCodes('&', GUI_ENTITYLIMIT_ITEM_INFO_PATTERN);
        ENTITYLIMIT_CHECK_EXTENSION_INFO = ChatColor.translateAlternateColorCodes('&', ENTITYLIMIT_CHECK_EXTENSION_INFO);
        GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO = ChatColor.translateAlternateColorCodes('&', GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO);
        ENTITYLIMITGROUP_INFO_EXTENSION_INFO = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_INFO_EXTENSION_INFO);
        ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED);
        ENTITYLIMITGROUP_EXTRA_ENTITIES_SET = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_EXTRA_ENTITIES_SET);
        ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS);
        ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED);
        ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR);
        ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR = ChatColor.translateAlternateColorCodes('&', ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR);
        GUI_MEMBER_REGIONS_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_REGIONS_MENU_NAME);
        ARM_BASIC_COMMAND_MESSAGE = ChatColor.translateAlternateColorCodes('&', ARM_BASIC_COMMAND_MESSAGE);

        REGIONKIND_CREATED = ChatColor.translateAlternateColorCodes('&', REGIONKIND_CREATED);
        REGIONKIND_ALREADY_EXISTS = ChatColor.translateAlternateColorCodes('&', REGIONKIND_ALREADY_EXISTS);
        REGIONKIND_DELETED = ChatColor.translateAlternateColorCodes('&', REGIONKIND_DELETED);
        REGIONKIND_CAN_NOT_REMOVE_SYSTEM = ChatColor.translateAlternateColorCodes('&', REGIONKIND_CAN_NOT_REMOVE_SYSTEM);
        REGIONKIND_LIST_HEADLINE = ChatColor.translateAlternateColorCodes('&', REGIONKIND_LIST_HEADLINE);
        REGIONKIND_MODIFIED = ChatColor.translateAlternateColorCodes('&', REGIONKIND_MODIFIED);
        MATERIAL_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', MATERIAL_NOT_FOUND);
        REGIONKIND_LORE_LINE_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', REGIONKIND_LORE_LINE_NOT_EXIST);
        REGIONKIND_INFO_HEADLINE = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_HEADLINE);
        REGIONKIND_INFO_INTERNAL_NAME = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_INTERNAL_NAME);
        REGIONKIND_INFO_DISPLAY_NAME = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_DISPLAY_NAME);
        REGIONKIND_INFO_MATERIAL = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_MATERIAL);
        REGIONKIND_INFO_DISPLAY_IN_GUI = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_DISPLAY_IN_GUI);
        REGIONKIND_INFO_DISPLAY_IN_LIMITS = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_DISPLAY_IN_LIMITS);
        REGIONKIND_INFO_LORE = ChatColor.translateAlternateColorCodes('&', REGIONKIND_INFO_LORE);
        REGIONKIND_HELP_HEADLINE = ChatColor.translateAlternateColorCodes('&', REGIONKIND_HELP_HEADLINE);
        PLAYER_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', PLAYER_NOT_FOUND);

        GUI_FLAGEDITOR_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_BUTTON);
        GUI_FLAGEDITOR_MENU_NAME = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_MENU_NAME);
        GUI_FLAGEDITOR_DELETE_FLAG_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_DELETE_FLAG_BUTTON);
        GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON);
        GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON);
        GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON);
        GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON);
        GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON);
        GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON);
        GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON);
        GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON);
        GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON);
        GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON);
        GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON);
        GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON);
        FlAGEDITOR_FLAG_NOT_ACTIVATED = ChatColor.translateAlternateColorCodes('&', FlAGEDITOR_FLAG_NOT_ACTIVATED);
        FLAGEDITOR_FLAG_HAS_BEEN_UPDATED = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_FLAG_HAS_BEEN_UPDATED);
        FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED);
        FlAGEDITOR_FLAG_HAS_BEEN_DELETED = ChatColor.translateAlternateColorCodes('&', FlAGEDITOR_FLAG_HAS_BEEN_DELETED);
        FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO);
        FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO);
        FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO);
        GUI_FLAGEDITOR_RESET_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_RESET_BUTTON);
        GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON = ChatColor.translateAlternateColorCodes('&', GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON);
        FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO = ChatColor.translateAlternateColorCodes('&', FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO);
        FLAGGROUP_DOES_NOT_EXIST = ChatColor.translateAlternateColorCodes('&', FLAGGROUP_DOES_NOT_EXIST);
        SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS = ChatColor.translateAlternateColorCodes('&', SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS);
        REGION_INFO_FLAGGROUP = ChatColor.translateAlternateColorCodes('&', REGION_INFO_FLAGGROUP);
        INFO_DEACTIVATED = ChatColor.translateAlternateColorCodes('&', INFO_DEACTIVATED);
        INFO_REGION_NOT_SOLD = ChatColor.translateAlternateColorCodes('&', INFO_REGION_NOT_SOLD);
        INFO_NOW = ChatColor.translateAlternateColorCodes('&', INFO_NOW);
        COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG = ChatColor.translateAlternateColorCodes('&', COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG);
        INFO_NOT_CALCULATED = ChatColor.translateAlternateColorCodes('&', INFO_NOT_CALCULATED);
        REGION_SOLD_BACK_SUCCESSFULLY = ChatColor.translateAlternateColorCodes('&', REGION_SOLD_BACK_SUCCESSFULLY);
        REGION_MODIFIED_BOOLEAN = ChatColor.translateAlternateColorCodes('&', REGION_MODIFIED_BOOLEAN);
        REGION_MODIFIED = ChatColor.translateAlternateColorCodes('&', REGION_MODIFIED);
        UPDATING_SCHEMATIC = ChatColor.translateAlternateColorCodes('&', UPDATING_SCHEMATIC);
        SCHEMATIC_UPDATED = ChatColor.translateAlternateColorCodes('&', SCHEMATIC_UPDATED);
        CONTRACTREGION_TERMINATED = ChatColor.translateAlternateColorCodes('&', CONTRACTREGION_TERMINATED);
        CONTRACTREGION_REACTIVATED = ChatColor.translateAlternateColorCodes('&', CONTRACTREGION_REACTIVATED);
        REGION_INFO_FEATURE_DISABLED = ChatColor.translateAlternateColorCodes('&', REGION_INFO_FEATURE_DISABLED);
        FLAGGROUP_FEATURE_DISABLED = ChatColor.translateAlternateColorCodes('&', FLAGGROUP_FEATURE_DISABLED);
        BACKUP_CREATED = ChatColor.translateAlternateColorCodes('&', BACKUP_CREATED);
        BACKUP_RESTORED = ChatColor.translateAlternateColorCodes('&', BACKUP_RESTORED);
        COULD_NOT_LOAD_BACKUP = ChatColor.translateAlternateColorCodes('&', COULD_NOT_LOAD_BACKUP);
        BACKUP_LIST_HEADER = ChatColor.translateAlternateColorCodes('&', BACKUP_LIST_HEADER);

        for (int i = 0; i < REGION_INFO_SELLREGION.size(); i++) {
            REGION_INFO_SELLREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_SELLREGION.get(i)));
        }
        for (int i = 0; i < REGION_INFO_RENTREGION.size(); i++) {
            REGION_INFO_RENTREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_RENTREGION.get(i)));
        }
        for (int i = 0; i < REGION_INFO_CONTRACTREGION.size(); i++) {
            REGION_INFO_CONTRACTREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_CONTRACTREGION.get(i)));
        }
        for (int i = 0; i < REGION_INFO_SELLREGION_ADMIN.size(); i++) {
            REGION_INFO_SELLREGION_ADMIN.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_SELLREGION_ADMIN.get(i)));
        }
        for (int i = 0; i < REGION_INFO_RENTREGION_ADMIN.size(); i++) {
            REGION_INFO_RENTREGION_ADMIN.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_RENTREGION_ADMIN.get(i)));
        }
        for (int i = 0; i < REGION_INFO_CONTRACTREGION_ADMIN.size(); i++) {
            REGION_INFO_CONTRACTREGION_ADMIN.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_CONTRACTREGION_ADMIN.get(i)));
        }
        for (int i = 0; i < REGION_INFO_SELLREGION_SUBREGION.size(); i++) {
            REGION_INFO_SELLREGION_SUBREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_SELLREGION_SUBREGION.get(i)));
        }
        for (int i = 0; i < REGION_INFO_RENTREGION_SUBREGION.size(); i++) {
            REGION_INFO_RENTREGION_SUBREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_RENTREGION_SUBREGION.get(i)));
        }
        for (int i = 0; i < REGION_INFO_CONTRACTREGION_SUBREGION.size(); i++) {
            REGION_INFO_CONTRACTREGION_SUBREGION.set(i, ChatColor.translateAlternateColorCodes('&', REGION_INFO_CONTRACTREGION_SUBREGION.get(i)));
        }
        for (int i = 0; i < GUI_ENTITYLIMIT_ITEM_LORE.size(); i++) {
            GUI_ENTITYLIMIT_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_ENTITYLIMIT_ITEM_LORE.get(i)));
        }
        for (int i = 0; i < GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE.size(); i++) {
            GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE.get(i)));
        }
        for (int i = 0; i < GUI_SUBREGION_HOTEL_BUTTON_LORE.size(); i++) {
            GUI_SUBREGION_HOTEL_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_HOTEL_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_SUBREGION_REGION_INFO_SELL.size(); i++) {
            GUI_SUBREGION_REGION_INFO_SELL.set(i, ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_REGION_INFO_SELL.get(i)));
        }
        for (int i = 0; i < GUI_SUBREGION_REGION_INFO_RENT.size(); i++) {
            GUI_SUBREGION_REGION_INFO_RENT.set(i, ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_REGION_INFO_RENT.get(i)));
        }
        for (int i = 0; i < GUI_SUBREGION_REGION_INFO_CONTRACT.size(); i++) {
            GUI_SUBREGION_REGION_INFO_CONTRACT.set(i, ChatColor.translateAlternateColorCodes('&', GUI_SUBREGION_REGION_INFO_CONTRACT.get(i)));
        }
        for (int i = 0; i < GUI_REGIONFINDER_REGION_INFO_SELL.size(); i++) {
            GUI_REGIONFINDER_REGION_INFO_SELL.set(i, ChatColor.translateAlternateColorCodes('&', GUI_REGIONFINDER_REGION_INFO_SELL.get(i)));
        }
        for (int i = 0; i < GUI_REGIONFINDER_REGION_INFO_RENT.size(); i++) {
            GUI_REGIONFINDER_REGION_INFO_RENT.set(i, ChatColor.translateAlternateColorCodes('&', GUI_REGIONFINDER_REGION_INFO_RENT.get(i)));
        }
        for (int i = 0; i < GUI_REGIONFINDER_REGION_INFO_CONTRACT.size(); i++) {
            GUI_REGIONFINDER_REGION_INFO_CONTRACT.set(i, ChatColor.translateAlternateColorCodes('&', GUI_REGIONFINDER_REGION_INFO_CONTRACT.get(i)));
        }
        for (int i = 0; i < SELECTION_SAVED_CREATE_SIGN.size(); i++) {
            SELECTION_SAVED_CREATE_SIGN.set(i, ChatColor.translateAlternateColorCodes('&', SELECTION_SAVED_CREATE_SIGN.get(i)));
        }
        for (int i = 0; i < SUBREGION_TOOL_INSTRUCTION.size(); i++) {
            SUBREGION_TOOL_INSTRUCTION.set(i, ChatColor.translateAlternateColorCodes('&', SUBREGION_TOOL_INSTRUCTION.get(i)));
        }
        for (int i = 0; i < GUI_TELEPORT_TO_REGION_BUTTON_LORE.size(); i++) {
            GUI_TELEPORT_TO_REGION_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_TELEPORT_TO_REGION_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_MAKE_OWNER_BUTTON_LORE.size(); i++) {
            GUI_MAKE_OWNER_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_MAKE_OWNER_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_REMOVE_MEMBER_BUTTON_LORE.size(); i++) {
            GUI_REMOVE_MEMBER_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_REMOVE_MEMBER_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_RESET_REGION_BUTTON_LORE.size(); i++) {
            GUI_RESET_REGION_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_RESET_REGION_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_TAKEOVER_ITEM_LORE.size(); i++) {
            GUI_TAKEOVER_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_TAKEOVER_ITEM_LORE.get(i)));
        }
        for (int i = 0; i < GUI_EXTEND_BUTTON_LORE.size(); i++) {
            GUI_EXTEND_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_EXTEND_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_RENT_REGION_LORE.size(); i++) {
            GUI_RENT_REGION_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_RENT_REGION_LORE.get(i)));
        }
        for (int i = 0; i < GUI_USER_SELL_BUTTON_LORE.size(); i++) {
            GUI_USER_SELL_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_USER_SELL_BUTTON_LORE.get(i)));
        }
        for (int i = 0; i < GUI_MEMBER_INFO_LORE.size(); i++) {
            GUI_MEMBER_INFO_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_MEMBER_INFO_LORE.get(i)));
        }
        for (int i = 0; i < GUI_CONTRACT_ITEM_LORE.size(); i++) {
            GUI_CONTRACT_ITEM_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_CONTRACT_ITEM_LORE.get(i)));
        }
        for (int i = 0; i < GUI_CONTRACT_REGION_LORE.size(); i++) {
            GUI_CONTRACT_REGION_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_CONTRACT_REGION_LORE.get(i)));
        }
        for (int i = 0; i < GUI_OWNER_MEMBER_INFO_LORE.size(); i++) {
            GUI_OWNER_MEMBER_INFO_LORE.set(i, ChatColor.translateAlternateColorCodes('&', GUI_OWNER_MEMBER_INFO_LORE.get(i)));
        }
        for (int i = 0; i < UNSELL_REGION_BUTTON_LORE.size(); i++) {
            UNSELL_REGION_BUTTON_LORE.set(i, ChatColor.translateAlternateColorCodes('&', UNSELL_REGION_BUTTON_LORE.get(i)));
        }
    }

    private static void updateDefauts() {
        YamlConfiguration modelConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(AdvancedRegionMarket.getInstance().getResource("messages_en.yml")));

        ConfigurationSection msgSection = modelConfig.getConfigurationSection("Messages");
        if(msgSection == null) {
            return;
        }
        Set<String> msgKeys = msgSection.getKeys(false);

        boolean fileUpdated = false;
        for(String key : msgKeys) {
            fileUpdated |= YamlFileManager.addDefault(config, "Messages." + key, msgSection.get(key));
        }

        if (fileUpdated) {
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    public static void generatedefaultConfig(String languageCode) {
        if(languageCode == null) {
            languageCode = "en";
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/messages.yml");
        if (!messagesdic.exists()) {
            try {
                InputStream stream = plugin.getResource("messages_" + languageCode + ".yml");
                if(stream == null) {
                    stream = plugin.getResource("messages_en.yml");
                }
                OutputStream output = new FileOutputStream(messagesdic);

                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.flush();
                output.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setConfig();
        updateDefauts();
    }

    public static YamlConfiguration getConfig() {
        return Messages.config;
    }

    public static void setConfig() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Messages.config = YamlConfiguration.loadConfiguration(messagesconfigdic);
    }

    public static void saveConfig() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        try {
            Messages.config.save(messagesconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertYesNo(Boolean bool) {
        if (bool) {
            return Messages.YES;
        } else {
            return Messages.NO;
        }
    }

    public static String convertEnabledDisabled(boolean bool) {
        if (bool) {
            return Messages.ENABLED;
        } else {
            return Messages.DISABLED;
        }
    }

    public static <X> String getStringList(Iterable<X> xList, StringGetter<X> stringGetter, String splitter) {
        StringBuilder sb = new StringBuilder();

        Iterator<X> iterator = xList.iterator();
        while (iterator.hasNext()) {
            X x = iterator.next();
            sb.append(stringGetter.get(x));
            if (iterator.hasNext()) {
                sb.append(splitter);
            }
        }
        return sb.toString();
    }

    public interface StringGetter<X> {
        String get(X x);
    }

    public static <X> String getStringValue(X object, StringGetter<X> stringGetter, String nullString) {
        if(object != null) {
            return stringGetter.get(object);
        } else {
            return nullString;
        }
    }

}

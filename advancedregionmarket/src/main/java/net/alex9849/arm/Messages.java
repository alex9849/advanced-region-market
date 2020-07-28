package net.alex9849.arm;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Messages {
    public enum MessageLocale {
        EN("en"), DE("de"), FR("fr"), RU("ru"), ES("es");
        private String code;

        MessageLocale(String code) {
            this.code = code;
        }

        String code() {
            return code;
        }

        public static MessageLocale byCode(String code) {
            for (MessageLocale locale : MessageLocale.values()) {
                if (locale.code().equalsIgnoreCase(code)) {
                    return locale;
                }
            }
            return null;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Message {
        public String name();

        public String[] message();

        public int version() default 0;
    }

    //Current version = 4
    @Message(name = "Prefix", message = "&b[ARM]&r ", version = 1)
    public static String PREFIX;
    @Message(name = "Buymessage", message = "&aRegion successfully bought!")
    public static String REGION_BUYMESSAGE;
    @Message(name = "NotEnoughMoney", message = "&4You do not have enough money!")
    public static String NOT_ENOUGH_MONEY;
    @Message(name = "RegionAlreadySold", message = "&4Region already Sold!")
    public static String REGION_ALREADY_SOLD;
    @Message(name = "NoPermission", message = "&4You do not have permission!")
    public static String NO_PERMISSION;
    @Message(name = "WorldDoesNotExist", message = "&4The selected world does not exist!")
    public static String WORLD_DOES_NOT_EXIST;
    @Message(name = "RegionDoesNotExist", message = "&4The selected region does not exist in this (or the selected) world!")
    public static String REGION_DOES_NOT_EXIST;
    @Message(name = "WGRegionNotFound", message = "&4Could not find region! Make sure to execute this command in the same world in that the region exists! Some versions of WorldGuard require a case-sensitive region name!")
    public static String WGREGION_NOT_FOUND;
    @Message(name = "RegionAddedToARM", message = "&7Region has been created and added to ARM!", version = 3)
    public static String REGION_ADDED_TO_ARM;
    @Message(name = "RegionAndSignAddedToARM", message = "&7Regionsign has been created and region has been added to ARM!")
    public static String REGION_AND_SIGN_ADDED_TO_ARM;
    @Message(name = "SignAddedToRegion", message = "&7Region already exists! The sign has been added to the region!")
    public static String SIGN_ADDED_TO_REGION;
    @Message(name = "SignRemovedFromRegion", message = "&7Regionsign removed! %remaining% Sign(s) remaining!", version = 3)
    public static String SIGN_REMOVED_FROM_REGION;
    @Message(name = "RegionZeroSignsReached", message = "&7You've deleted the last sign of this region! Please note that this region has &4&l&nNOT BEEN REMOVED FROM ARM!!&r &7If you want to unregister this region execute &6/arm delete %regionid%&7!")
    public static String REGION_ZERO_SIGNS_REACHED;
    @Message(name = "SellSign1", message = "&2For Sale")
    public static String SELL_SIGN1;
    @Message(name = "SellSign2", message = "%regionid%")
    public static String SELL_SIGN2;
    @Message(name = "SellSign3", message = "%price%%currency%")
    public static String SELL_SIGN3;
    @Message(name = "SellSign4", message = "%dimensions%")
    public static String SELL_SIGN4;
    @Message(name = "SoldSign1", message = "&4Sold")
    public static String SOLD_SIGN1;
    @Message(name = "SoldSign2", message = "%regionid%")
    public static String SOLD_SIGN2;
    @Message(name = "SoldSign3", message = "")
    public static String SOLD_SIGN3;
    @Message(name = "SoldSign4", message = "%owner%")
    public static String SOLD_SIGN4;
    @Message(name = "Currency", message = "$")
    public static String CURRENCY;
    @Message(name = "CommandOnlyIngame", message = "&4This command can only be executed ingame!")
    public static String COMMAND_ONLY_INGAME;
    @Message(name = "RegionInfoExpired", message = "&4Expired")
    public static String REGION_INFO_EXPIRED;
    @Message(name = "GUIMainMenuName", message = "&1ARM - Menu")
    public static String GUI_MAIN_MENU_NAME;
    @Message(name = "GUIGoBack", message = "&6Go back")
    public static String GUI_GO_BACK;
    @Message(name = "GUIMyOwnRegions", message = "&6My regions (Owner)")
    public static String GUI_MY_OWN_REGIONS;
    @Message(name = "GUIMemberRegionsMenuName", message = "&1ARM - My regions (Member)")
    public static String GUI_MEMBER_REGIONS_MENU_NAME;
    @Message(name = "GUIMyMemberRegions", message = "&6My regions (Member)")
    public static String GUI_MY_MEMBER_REGIONS;
    @Message(name = "GUISearchFreeRegion", message = "&6Search free region")
    public static String GUI_SEARCH_FREE_REGION;
    @Message(name = "GUIOwnRegionsMenuName", message = "&1ARM - My regions (Owner)")
    public static String GUI_OWN_REGIONS_MENU_NAME;
    @Message(name = "GUIMembersButton", message = "&6Members")
    public static String GUI_MEMBERS_BUTTON;
    @Message(name = "GUIShowInfosButton", message = "&6Show infos")
    public static String GUI_SHOW_INFOS_BUTTON;
    @Message(name = "GUITeleportToRegionButton", message = "&6Teleport to region")
    public static String GUI_TELEPORT_TO_REGION_BUTTON;
    @Message(name = "GUIRegionFinderMenuName", message = "&1ARM - Regionfinder")
    public static String GUI_REGION_FINDER_MENU_NAME;
    @Message(name = "GUIMemberListMenuName", message = "&1ARM - Members of %regionid%")
    public static String GUI_MEMBER_LIST_MENU_NAME;
    @Message(name = "GUIMakeOwnerButton", message = "&aMake owner")
    public static String GUI_MAKE_OWNER_BUTTON;
    @Message(name = "GUIRemoveMemberButton", message = "&4Remove")
    public static String GUI_REMOVE_MEMBER_BUTTON;
    @Message(name = "GUIMakeOwnerWarningName", message = "&4&lAre you sure?")
    public static String GUI_MAKE_OWNER_WARNING_NAME;
    @Message(name = "GUIWarningYes", message = "&aYes")
    public static String GUI_YES;
    @Message(name = "GUIWarningNo", message = "&4No")
    public static String GUI_NO;
    @Message(name = "RegionTeleportMessage", message = "&7You have been teleported to %regionid%")
    public static String REGION_TELEPORT_MESSAGE;
    @Message(name = "NoPermissionsToBuyThisKindOfRegion", message = "&4You do not have permission to buy this kind of region (You need the permission &6arm.buykind.%regionkind%&4)")
    public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION;
    @Message(name = "NoFreeRegionWithThisKind", message = "&7No free region with this type found :(")
    public static String NO_FREE_REGION_WITH_THIS_KIND;
    @Message(name = "RegionkindDoesNotExist", message = "&4The selected regionkind does not exist!")
    public static String REGIONKIND_DOES_NOT_EXIST;
    @Message(name = "RegionkindgroupNotExists", message = "&4The selected regionkindgroup does not exist!")
    public static String REGIONKINDGROUP_NOT_EXISTS;
    @Message(name = "RegionkindgroupModified", message = "&aRegionkindGroup modified!")
    public static String REGIONKINDGROUP_MODIFIED;
    @Message(name = "RegionkindgroupListHeadline", message = "&6Regionkindgroups:")
    public static String REGIONKINDGROUP_LIST_HEADLINE;
    @Message(name = "RegionkindgroupAlreadyExists", message = "&4RegionKindGroup already exists!")
    public static String REGIONKINDGROUP_ALREADY_EXISTS;
    @Message(name = "RegionkindgroupCreated", message = "&aRegionKindgroup created!")
    public static String REGIONKINDGROUP_CREATED;
    @Message(name = "RegionkindgroupDeleted", message = "&aRegionkindgroup deleted!")
    public static String REGIONKINDGROUP_DELETED;
    @Message(name = "RegionkindgroupHelpHeadline", message = "&6==[AdvancedRegionMarket RegionkindGroup Help]==\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String REGIONKINDGROUP_HELP_HEADLINE;
    @Message(name = "RegionkindgroupInfo", message = {"&6======[RegionkindGroup Info]======",
    "&9Name: &e%regionkindgroup%",
    "&9DisplayName: &r%regionkindgroupdisplay%",
    "&9Display in limits: &e%regionkindgroupdisplayinlimits%",
    "&9Member Regionkinds: &e%regionkindgroupmembers%"})
    public static List<String> REGIONKINDGROUP_INFO;
    @Message(name = "RegionNowAvailable", message = "&aRegion is now available!")
    public static String REGION_NOW_AVAILABLE;
    @Message(name = "NoRegionAtPlayersPosition", message = "&7Could not find a region at your position!")
    public static String NO_REGION_AT_PLAYERS_POSITION;
    @Message(name = "RegionAddMemberNotOnline", message = "&4The selected player is not online!")
    public static String REGION_ADD_MEMBER_NOT_ONLINE;
    @Message(name = "RegionAddMemberAdded", message = "&aMember has been added!")
    public static String REGION_ADD_MEMBER_ADDED;
    @Message(name = "RegionRemoveMemberNotAMember", message = "&4The selected player is not a member of the region")
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER;
    @Message(name = "RegionRemoveMemberRemoved", message = "&aMember has been removed!")
    public static String REGION_REMOVE_MEMBER_REMOVED;
    @Message(name = "GUIResetRegionButton", message = "&4Reset region")
    public static String GUI_RESET_REGION_BUTTON;
    @Message(name = "GUIResetRegionWarningName", message = "&4&lReset your region?")
    public static String GUI_RESET_REGION_WARNING_NAME;
    @Message(name = "ResetComplete", message = "&aReset complete!")
    public static String RESET_COMPLETE;
    @Message(name = "ResetRegionCooldownError", message = "&7You have to wait&6 %remaininguserresetcooldown-countdown-writtenout% &7till you can reset your region again")
    public static String RESET_REGION_COOLDOWN_ERROR;
    @Message(name = "GUIRegionTakeOverMenuName", message = "&4Region take-over")
    public static String GUI_TAKEOVER_MENU_NAME;
    @Message(name = "RegionTransferCompleteMessage", message = "&aTransfer complete!")
    public static String REGION_TRANSFER_COMPLETE_MESSAGE;
    @Message(name = "GUICloseWindow", message = "&6Close window")
    public static String GUI_CLOSE;
    @Message(name = "RentSign1", message = "&2For Rent")
    public static String RENT_SIGN1;
    @Message(name = "RentSign2", message = "%regionid%")
    public static String RENT_SIGN2;
    @Message(name = "RentSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENT_SIGN3;
    @Message(name = "RentSign4", message = "Max.: %maxrenttime-writtenout%")
    public static String RENT_SIGN4;
    @Message(name = "RentedSign1", message = "&4Rented")
    public static String RENTED_SIGN1;
    @Message(name = "RentedSign2", message = "%regionid%/%owner%")
    public static String RENTED_SIGN2;
    @Message(name = "RentedSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENTED_SIGN3;
    @Message(name = "RentedSign4", message = "%remainingtime-countdown-short%")
    public static String RENTED_SIGN4;
    @Message(name = "RentExtendMessage", version = 4, message = "&aRegion extended for &6%extendtime-current-writtenout%&a (For %price-current%%currency%. Remaining time: &6%remainingtime-countdown-short%")
    public static String RENT_EXTEND_MESSAGE;
    @Message(name = "GUIExtendRentRegionButton", message = "&1Extend region")
    public static String GUI_EXTEND_BUTTON;
    @Message(name = "Complete", message = "&aComplete!")
    public static String COMPLETE;
    @Message(name = "RegionBuyOutOfLimit", version = 2, message = "&4Out of Limit! You can see your current limits by executing &6/arm limit&4!")
    public static String REGION_BUY_OUT_OF_LIMIT;
    @Message(name = "RegionErrorCanNotBuildHere", message = "&4You are only allowed to break blocks you placed here!")
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE;
    @Message(name = "Unlimited", message = "∞")
    public static String UNLIMITED;
    @Message(name = "GUIUserSellButton", message = "&4Reset and sell Region")
    public static String GUI_USER_SELL_BUTTON;
    @Message(name = "GUIUserSellWarning", message = "&4&lSell your region?")
    public static String GUI_USER_SELL_WARNING;
    @Message(name = "LimitInfoTop", message = "&6=========[Limit Info]=========")
    public static String LIMIT_INFO_TOP;
    @Message(name = "LimitInfoLimitReachedColorCode", message = "&r&4")
    public static String LIMIT_REACHED_COLOR_CODE;
    @Message(name = "LimitInfoTotal", version = 2, message = "&6- Total: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_TOTAL;
    @Message(name = "LimitInfoRegionkind", version = 2, message = "&6- %regionkinddisplay%: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_REGIONKIND;
    @Message(name = "LimitInfoRegionkindGroup", version = 2, message = "&6- %regionkindgroupdisplay%: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_REGIONKINDGROUP;
    @Message(name = "GUILimitButton", message = "&6My limits")
    public static String GUI_MY_LIMITS_BUTTON;
    @Message(name = "MemberlistInfo", message = "&6How to become a Member:")
    public static String GUI_MEMBER_INFO_ITEM;
    @Message(name = "AddMemberMaxMembersExceeded", message = "&4Cloud not add member to region! You can only have %maxmembers% members on this region!")
    public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED;
    @Message(name = "RegionIsNotARentregion", message = "&4Region is not a rentregion!")
    public static String REGION_IS_NOT_A_RENTREGION;
    @Message(name = "RegionNotOwn", message = "&4You do not own this region!")
    public static String REGION_NOT_OWN;
    @Message(name = "RegionNotSold", message = "&4Region not sold!")
    public static String REGION_NOT_SOLD;
    @Message(name = "PresetRemoved", message = "&aPreset removed!")
    public static String PRESET_REMOVED;
    @Message(name = "PresetSet", message = "&aPreset set!")
    public static String PRESET_SET;
    @Message(name = "PresetSaved", message = "&aPreset saved!")
    public static String PRESET_SAVED;
    @Message(name = "PresetAlreadyExists", message = "&4A preset with this name already exists!")
    public static String PRESET_ALREADY_EXISTS;
    @Message(name = "PresetPlayerDontHasPreset", message = "&4You do not have a preset loaded!")
    public static String PRESET_PLAYER_DONT_HAS_PRESET;
    @Message(name = "PresetDeleted", message = "&aPreset deleted!")
    public static String PRESET_DELETED;
    @Message(name = "PresetNotFound", message = "&4No preset with this name found!")
    public static String PRESET_NOT_FOUND;
    @Message(name = "PresetLoaded", message = "&aPreset loaded!")
    public static String PRESET_LOADED;
    @Message(name = "GUIRegionItemName", message = "%regionid% (%regionkinddisplay%)")
    public static String GUI_REGION_ITEM_NAME;
    @Message(name = "GUIRegionFinderRegionKindName", message = "&a&l%regionkinddisplay%")
    public static String GUI_REGIONFINDER_REGIONKIND_NAME;
    @Message(name = "RentRegionExpirationWarning", version = 4, message = "&4&lWARNING! &r&4Your RentRegion &6%regionid% &4will expire in about &6%remainingtime-countdown-writtenout%&4!")
    public static String RENTREGION_EXPIRATION_WARNING;
    @Message(name = "ContractSign1", message = "&2Contract")
    public static String CONTRACT_SIGN1;
    @Message(name = "ContractSign2", message = "&2available")
    public static String CONTRACT_SIGN2;
    @Message(name = "ContractSign3", message = "%regionid%")
    public static String CONTRACT_SIGN3;
    @Message(name = "ContractSign4", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SIGN4;
    @Message(name = "ContractSoldSign1", message = "&4Contract in use")
    public static String CONTRACT_SOLD_SIGN1;
    @Message(name = "ContractSoldSign2", message = "%regionid%/%owner%")
    public static String CONTRACT_SOLD_SIGN2;
    @Message(name = "ContractSoldSign3", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SOLD_SIGN3;
    @Message(name = "ContractSoldSign4", message = "%remainingtime-countdown-short%")
    public static String CONTRACT_SOLD_SIGN4;
    @Message(name = "ContractRegionExtended", message = "&aYour contract region %regionid% has been extended for %extendtime-writtenout%. (For %price%%currency%.)")
    public static String CONTRACT_REGION_EXTENDED;
    @Message(name = "GUIContractItem", message = "&6Manage contract")
    public static String GUI_CONTRACT_ITEM;
    @Message(name = "ContractRegionStatusActive", message = "&aActive")
    public static String CONTRACT_REGION_STATUS_ACTIVE;
    @Message(name = "ContractRegionStatusTerminated", message = "&4Terminated")
    public static String CONTRACT_REGION_STATUS_TERMINATED;
    @Message(name = "RegionIsNotAContractRegion", message = "&4Region is not a contractregion!")
    public static String REGION_IS_NOT_A_CONTRACT_REGION;
    @Message(name = "OwnerMemberlistInfo", message = "&6Adding members:")
    public static String GUI_OWNER_MEMBER_INFO_ITEM;
    @Message(name = "RegiontransferMemberNotOnline", message = "&4Member not online!")
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE;
    @Message(name = "RegiontransferLimitError", message = "&4Transfer aborted! (Region would exceed players limit)")
    public static String REGION_TRANSFER_LIMIT_ERROR;
    @Message(name = "SecondsSingular", message = "second")
    public static String TIME_SECONDS_SINGULAR;
    @Message(name = "MinutesSingular", message = "minute")
    public static String TIME_MINUTES_SINGULAR;
    @Message(name = "HoursSingular", message = "hour")
    public static String TIME_HOURS_SINGULAR;
    @Message(name = "DaysSingular", message = "day")
    public static String TIME_DAYS_SINGULAR;
    @Message(name = "SecondsPlural", message = "seconds")
    public static String TIME_SECONDS_PLURAL;
    @Message(name = "MinutesPlural", message = "minutes")
    public static String TIME_MINUTES_PLURAL;
    @Message(name = "HoursPlural", message = "hours")
    public static String TIME_HOURS_PLURAL;
    @Message(name = "DaysPlural", message = "days")
    public static String TIME_DAYS_PLURAL;
    @Message(name = "SecondsShort", message = "s")
    public static String TIME_SECONDS_SHORT;
    @Message(name = "MinutesShort", message = "m")
    public static String TIME_MINUTES_SHORT;
    @Message(name = "HoursShort", message = "h")
    public static String TIME_HOURS_SHORT;
    @Message(name = "DaysShort", message = "d")
    public static String TIME_DAYS_SHORT;
    @Message(name = "TimeUnitSplitter", message = " and ")
    public static String TIME_UNIT_SPLITTER;
    @Message(name = "TimeUnitSplitterShort", message = ":")
    public static String TIME_UNIT_SPLITTER_SHORT;
    @Message(name = "UserNotAMemberOrOwner", message = "&4You are not a member or owner of this region!")
    public static String NOT_A_MEMBER_OR_OWNER;
    @Message(name = "RegionInfoYes", message = "&2yes")
    public static String YES;
    @Message(name = "RegionInfoNo", message = "&4no")
    public static String NO;
    @Message(name = "RegionInfoNever", message = "&4Never")
    public static String NEVER;
    @Message(name = "RegionAleadyRegistred", message = "&4Region already registered! &c(If you want to delete and re-add it. Delete it first with &6/arm delete [REGION-ID]&c!)")
    public static String REGION_ALREADY_REGISTERED;
    @Message(name = "UnknownUUID", message = "Unknown UUID")
    public static String UNKNOWN_UUID;
    @Message(name = "RegionStats", message = "&6=========[Region stats]=========")
    public static String REGION_STATS;
    @Message(name = "RegionStatsPattern", message = "&8Used regions (%regionkind%&8):")
    public static String REGION_STATS_PATTERN;
    @Message(name = "TeleporterNoSaveLocation", message = "&4Could not find a save teleport location")
    public static String TELEPORTER_NO_SAVE_LOCATION_FOUND;
    @Message(name = "TeleporterDontMove", message = "&6Teleportation will commence in &c%time% Seconds&6. Do not move!")
    public static String TELEPORTER_DONT_MOVE;
    @Message(name = "TeleporterTeleportationAborded", message = "&4Teleportation aborded!")
    public static String TELEPORTER_TELEPORTATION_ABORDED;
    @Message(name = "OfferSent", message = "&aYour offer has been sent")
    public static String OFFER_SENT;
    @Message(name = "OfferAcceptedSeller", message = "&a%buyer% &aaccepted your offer")
    public static String OFFER_ACCEPTED_SELLER;
    @Message(name = "OfferAcceptedBuyer", message = "&aOffer accepted! You are now the owner of &c%regionid%")
    public static String OFFER_ACCEPTED_BUYER;
    @Message(name = "NoOfferToAnswer", message = "&4You dont have an offer to answer")
    public static String NO_OFFER_TO_ANSWER;
    @Message(name = "OfferRejected", message = "&aOffer rejected!")
    public static String OFFER_REJECTED;
    @Message(name = "OfferHasBeenRejected", message = "&4%buyer% &4rejected your offer!")
    public static String OFFER_HAS_BEEN_REJECTED;
    @Message(name = "NoOfferToReject", message = "&4You do not have an offer to reject")
    public static String NO_OFFER_TO_REJECT;
    @Message(name = "OfferCancelled", message = "&aYour offer has been cancelled!")
    public static String OFFER_CANCELED;
    @Message(name = "OfferHasBeenCancelled", message = "&4%seller% &4cancelled his offer!")
    public static String OFFER_HAS_BEEN_CANCELLED;
    @Message(name = "NoOfferToCancel", message = "&4You do not have an offer to cancel")
    public static String NO_OFFER_TO_CANCEL;
    @Message(name = "BuyerAlreadyGotAnOffer", message = "&4The selected buyer already got an offer that he has to answer first!")
    public static String BUYER_ALREADY_GOT_AN_OFFER;
    @Message(name = "SellerAlreadyCreatedAnOffer", message = "&4You have already created an offer! Please wait for an answer or cancel it first!")
    public static String SELLER_ALREADY_CREATED_AN_OFFER;
    @Message(name = "SellerDoesNotLongerOwnRegion", message = "&4%seller% &4does not longer own this region. His offer has been cancelled")
    public static String SELLER_DOES_NOT_LONGER_OWN_REGION;
    @Message(name = "IncomingOffer", message = "&c%seller% &6offers you his region &c%regionid% &6in the world &c%world% &6for &c%price%%currency%&6! You can accept his offer with &c/arm offer accept &6or reject it &c/arm offer reject")
    public static String INCOMING_OFFER;
    @Message(name = "SelectedPlayerIsNotOnline", message = "&4The selected player is not online")
    public static String SELECTED_PLAYER_NOT_ONLINE;
    @Message(name = "OfferTimedOut", message = "&4Offer timed out!")
    public static String OFFER_TIMED_OUT;
    @Message(name = "BadSyntax", message = "&7Bad syntax! Please use: &8%command%")
    public static String BAD_SYNTAX;
    @Message(name = "LandlordServer", message = "Server")
    public static String LANDLORD_SERVER;
    @Message(name = "BadSyntaxSplitter", message = "\n&7or &8%command%")
    public static String BAD_SYNTAX_SPLITTER;
    @Message(name = "HelpHeadline", message = "&6=====[AdvancedRegionMarket Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String HELP_HEADLINE;
    @Message(name = "PresetInfoSellregion", message = {"&6=========[Region Info]=========",
            "&9Autoprice: &e%presetautoprice%",
            "&9Price: &e%presetprice%",
            "&9PaybackPercentage: &e%presetpaybackpercentage%",
            "&9Regionkind: &e%presetregionkind%",
            "&9FlagGroup: &e%presetflaggroup%",
            "&9EntitylimitGroup: &e%presetentitylimitgroup%",
            "&9InactivityReset: &e%presetinactivityreset%",
            "&9isHotel: &e%presetishotel%",
            "&9AutoRestore: &e%presetautorestore%",
            "&9UserRestorable: &e%presetisuserrestorable%",
            "&9Max. number of members: &e%presetmaxmembers%",
            "&9AllowedSubregions: &e%presetallowedsubregions%",
            "&9Setup commands:",
            "%presetsetupcommands%"})
    public static List<String> PRESET_INFO_SELLREGION;
    @Message(name = "PresetInfoContractregion", message = {"&6=========[Region Info]=========",
            "&9Autoprice: &e%presetautoprice%",
            "&9Price: &e%presetprice%",
            "&9ExtendTime: &e%extendtime%",
            "&9PaybackPercentage: &e%presetpaybackpercentage%",
            "&9Regionkind: &e%presetregionkind%",
            "&9FlagGroup: &e%presetflaggroup%",
            "&9EntitylimitGroup: &e%presetentitylimitgroup%",
            "&9InactivityReset: &e%presetinactivityreset%",
            "&9isHotel: &e%presetishotel%",
            "&9AutoRestore: &e%presetautorestore%",
            "&9UserRestorable: &e%presetisuserrestorable%",
            "&9Max. number of members: &e%presetmaxmembers%",
            "&9AllowedSubregions: &e%presetallowedsubregions%",
            "&9Setup commands:",
            "%presetsetupcommands%"})
    public static List<String> PRESET_INFO_CONTRACTREGION;
    @Message(name = "PresetInfoRentregion", message = {"&6=========[Region Info]=========",
            "&9Autoprice: &e%presetautoprice%",
            "&9Price: &e%presetprice%",
            "&9ExtendTime: &e%extendtime%",
            "&9MaxRentTime: &e%maxrenttime%",
            "&9PaybackPercentage: &e%presetpaybackpercentage%",
            "&9Regionkind: &e%presetregionkind%",
            "&9FlagGroup: &e%presetflaggroup%",
            "&9EntitylimitGroup: &e%presetentitylimitgroup%",
            "&9InactivityReset: &e%presetinactivityreset%",
            "&9isHotel: &e%presetishotel%",
            "&9AutoRestore: &e%presetautorestore%",
            "&9UserRestorable: &e%presetisuserrestorable%",
            "&9Max. number of members: &e%presetmaxmembers%",
            "&9AllowedSubregions: &e%presetallowedsubregions%",
            "&9Setup commands:",
            "%presetsetupcommands%"})
    public static List<String> PRESET_INFO_RENTREGION;
    @Message(name = "NotDefined", message = "&8&onot defined")
    public static String NOT_DEFINED;
    @Message(name = "PriceCanNotBeNegative", message = "&4Price can not be negative!")
    public static String PRICE_CAN_NOT_BE_NEGATIVE;
    @Message(name = "SellBackWarning", message = "&4Sell region back to the server:\n" +
            "&4WARNING: &cThis can not be undone! \n" +
            "Your region &6%regionid% &cwill be released and all blocks on it\n" +
            "will be resetted! You and all members will loose their rights on it.\n" +
            "You will get &6%paybackmoney%%currency% &cback")
    public static String SELLBACK_WARNING;
    @Message(name = "SubregionInactivityResetError", message = "&4The selected region is a subregion. You can change the InactivityReset setting for all subregions in the config.yml!")
    public static String SUBREGION_INACTIVITYRESET_ERROR;
    @Message(name = "SubregionAutoRestoreError", message = "&4The selected region is a subregion. You can change the autoRestore setting for all subregions in the config.yml!")
    public static String SUBREGION_AUTORESTORE_ERROR;
    @Message(name = "SubregionSetLandlordError", message = "&4The selected region is a subregion. The landlord is always the owner of the surrounding region!")
    public static String SUBREGION_LANDLORD_ERROR;
    @Message(name = "RegionNotRestoreable", message = "&4Region not restorable!")
    public static String REGION_NOT_RESTORABLE;
    @Message(name = "RegionSelectedMultipleRegions", message = "&6There is more than one region at your position. Please select one: &4")
    public static String REGION_SELECTED_MULTIPLE_REGIONS;
    @Message(name = "SubregionRegionkindError", message = "&4The selected region is a subregion. You can edit the regionkind for all subregions in the config.yml!")
    public static String SUBREGION_REGIONKIND_ERROR;
    @Message(name = "SubRegionRegionkindOnlyForSubregions", message = "&4Subregion regionkind only for subregions!")
    public static String SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS;
    @Message(name = "SubregionTeleportLocationError", message = "&4The selected region is a subregion. Teleport location can not be changed")
    public static String SUBREGION_TELEPORT_LOCATION_ERROR;
    @Message(name = "RegionNotRegistred", message = "&4Region not registred")
    public static String REGION_NOT_REGISTRED;
    @Message(name = "FirstPositionSet", message = "&aFirst position set!")
    public static String FIRST_POSITION_SET;
    @Message(name = "SecondPositionSet", message = "&aSecond position set!")
    public static String SECOND_POSITION_SET;
    @Message(name = "MarkInOtherRegion", message = "&4Mark in other Region. Removing old mark")
    public static String MARK_IN_OTHER_REGION_REMOVING;
    @Message(name = "ParentRegionNotOwn", message = "&4You don not own the parent region!")
    public static String PARENT_REGION_NOT_OWN;
    @Message(name = "SubRegionRemoveNoPermissionBecauseSold", message = "&4You are not allowed to remove this region. Please ask an admin if you believe this is an error")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD;
    @Message(name = "SubRegionRemoveNoPermissionBecauseAvailable", message = "&4You are not allowed to remove this region, because it is sold. You may ask the owner or an admin to release it")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE;
    @Message(name = "PosCloudNotBeSetMarkOutsideRegion", message = "&4Position could not be set! Position outside region")
    public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION;
    @Message(name = "SubRegionAlreadyAtThisPosition", message = "&4Your selection would overlap with a subregion that already has been created")
    public static String ALREADY_SUBREGION_AT_THIS_POSITION;
    @Message(name = "SubRegionLimitReached", message = "&4Subregion limit reached! You are not allowed to create more than &6%subregionlimit% &4subregions")
    public static String SUBREGION_LIMIT_REACHED;
    @Message(name = "SelectionInvalid", message = "&4Selection invalid! You need to select 2 positions! (Left/Right click) Type \"&6/arm subregion tool&4\" to get the selection tool")
    public static String SELECTION_INVALID;
    @Message(name = "RegionCreatedAndSaved", message = "&aRegion created and saved!")
    public static String REGION_CREATED_AND_SAVED;
    @Message(name = "RegionNotASubregion", message = "&4Region not a subregion!")
    public static String REGION_NOT_A_SUBREGION;
    @Message(name = "RegionDeleted", message = "&aRegion deleted!")
    public static String REGION_DELETED;
    @Message(name = "DeleteRegionWarningName", message = "&4&lDelete region?")
    public static String DELETE_REGION_WARNING_NAME;
    @Message(name = "UnsellRegionButton", message = "&4Unsell region")
    public static String UNSELL_REGION_BUTTON;
    @Message(name = "UnsellRegionButtonLore", message = {"&4Click to unsell your subregion and",
            "&4kick the players of it"})
    public static List<String> UNSELL_REGION_BUTTON_LORE;
    @Message(name = "UnsellRegionWarningName", message = "&4&lUnsell region?")
    public static String UNSELL_REGION_WARNING_NAME;
    @Message(name = "SubregionHelpHeadline", message = "&6=====[AdvancedRegionMarket Subregion Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SUBREGION_HELP_HEADLINE;
    @Message(name = "SellbackLandlordNotEnoughMoney", message = "&4The landlord of this region doesn't have enough money! If you want to give the region back without getting money back use &7/arm sellback %region% nomoney&4!")
    public static String SELLBACK_LANDLORD_NOT_ENOUGH_MONEY;
    @Message(name = "SellregionName", message = "Sellregion")
    public static String SELLREGION_NAME;
    @Message(name = "ContractregionName", message = "Contractregion")
    public static String CONTRACTREGION_NAME;
    @Message(name = "RentregionName", message = "Rentregion")
    public static String RENTREGION_NAME;
    @Message(name = "GUISubregionsButton", message = "&6Subregions")
    public static String GUI_SUBREGION_ITEM_BUTTON;
    @Message(name = "GUISubregionListMenuName", message = "&1Subregions")
    public static String GUI_SUBREGION_LIST_MENU_NAME;
    @Message(name = "GUIHotelButton", message = "&6Hotel-function")
    public static String GUI_SUBREGION_HOTEL_BUTTON;
    @Message(name = "GUIDeleteRegionButton", message = "&4Delete region")
    public static String GUI_SUBREGION_DELETE_REGION_BUTTON;
    @Message(name = "GUITeleportToSignOrRegionButton", message = "Teleport to sign or region?")
    public static String GUI_TELEPORT_TO_SIGN_OR_REGION;
    @Message(name = "GUIRegionfinderTeleportToSignButton", message = "&6Teleport to buy sign!")
    public static String GUI_TELEPORT_TO_SIGN;
    @Message(name = "GUIRegionfinderTeleportToRegionButton", message = "&6Teleport to region!")
    public static String GUI_TELEPORT_TO_REGION;
    @Message(name = "GUINextPageButton", message = "&6Next page")
    public static String GUI_NEXT_PAGE;
    @Message(name = "GUIPrevPageButton", message = "&6Prev page")
    public static String GUI_PREV_PAGE;
    @Message(name = "Enabled", message = "&aenabled")
    public static String ENABLED;
    @Message(name = "Disabled", message = "&cdisabled")
    public static String DISABLED;
    @Message(name = "Sold", message = "&csold")
    public static String SOLD;
    @Message(name = "Available", message = "&aavailable")
    public static String AVAILABLE;
    @Message(name = "SubregionIsUserResettableError", message = "&4The selected region is a subregion. You can change the isUserResettable setting for all subregions in the config.yml!")
    public static String SUBREGION_IS_USER_RESETTABLE_ERROR;
    @Message(name = "SubregionMaxMembersError", message = "&4The selected region is a subregion. You can change the maxMember setting for all subregions in the config.yml!")
    public static String SUBREGION_MAX_MEMBERS_ERROR;
    @Message(name = "GUIHotelButtonLore", message = {"&6The hotel function allows you to prevent players",
            "&6from breaking blocks they do not have placed",
            "&6Status: %hotelfunctionstatus%",
            "&6Click to enable/disable"})
    public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE;
    @Message(name = "GUISubregionInfoSell", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_SELL;
    @Message(name = "GUISubregionInfoRent", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxrenttime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_RENT;
    @Message(name = "GUISubregionInfoContract", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT;
    @Message(name = "GUIRegionfinderInfoSell", message = {"&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL;
    @Message(name = "GUIRegionfinderInfoRent", message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxrenttime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT;
    @Message(name = "GUIRegionfinderInfoContract", message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT;
    @Message(name = "SubregionCreationCreateSignInfo", message = {"&aYour selection has been saved! You can now create a sign to sell the region.",
            "&aCreate a Sell-Region:",
            "&6First line: &b[sub-sell]",
            "&6Last line: &bprice",
            "",
            "&aCreate a Rent-Region:",
            "&6First line: &b[sub-rent]",
            "&6Last line: &bPricePerPeriod&6;&bExtendPerClick&6;&bMaxExtendTime",
            "&6example for ExtendPerClick/MaxExtendTime: 5d (5 days)",
            "",
            "&aCreate a Contract-Region:",
            "&6First line: &b[sub-contract]",
            "&6Last line: &bPricePerPeriod&6;&bExtendTime",
            "&6example for ExtendTime: 12h (12 hours)",
            "&4We would strongly recommend to not place the sign within the subregion!"})
    public static List<String> SELECTION_SAVED_CREATE_SIGN;
    @Message(name = "SubregionCreationSelectAreaInfo", message = {"&aYou got a tool in your inventory (feather) to select 2 points of your region that will mark the corners of your new subregion.",
            "&aLeft click to select pos1",
            "&aRight click to select pos2",
            "&aType \"&6/arm subregion create\" &aif you are done"})
    public static List<String> SUBREGION_TOOL_INSTRUCTION;
    @Message(name = "SubregionToolAlreadyOwned", message = "&4You already own a Subregion Tool. Please use that instead of a new one!")
    public static String SUBREGION_TOOL_ALREADY_OWNED;
    @Message(name = "AutopriceList", message = "&6=========[Autoprices]=========")
    public static String AUTOPRICE_LIST;
    @Message(name = "GUISubregionManagerNoSubregionItem", message = "&6Info")
    public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM;
    @Message(name = "SelltypeNotExist", message = "&4The selected selltype does not exist!")
    public static String SELLTYPE_NOT_EXIST;
    @Message(name = "SignLinkModeActivated", message = "&aSign-Link-Mode activated! Click into a region and afterwards click on a sign. ARM will automatically create a region (or will just add the sign if the region already exists) with the settings of your preset")
    public static String SIGN_LINK_MODE_ACTIVATED;
    @Message(name = "SignLinkModeDeactivated", message = "&aSign-Link-Mode deactivated!")
    public static String SIGN_LINK_MODE_DEACTIVATED;
    @Message(name = "SignLinkModeAlreadyDeactivated", message = "&4Sign-Link-Mode is already deactivated!")
    public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED;
    @Message(name = "SignLinkModePresetNotPriceready", message = "&cThe selected preset is not price-ready! All regions you will create now will be created with the default autoprice")
    public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY;
    @Message(name = "SignLinkModeNoPresetSelected", message = "&cYou dont have a preset loaded! Please load or create a preset first! &cYou can create a preset by using the &6/arm sellpreset/rentpreset/contractpreset &ccommands!\n" +
            "For more &cinformation about presets click here:\n" +
            "&6https://bit.ly/2HURK0v (Github Wiki)")
    public static String SIGN_LINK_MODE_NO_PRESET_SELECTED;
    @Message(name = "SignLinkModeSignBelongsToAnotherRegion", message = "&4Sign belongs to another region!")
    public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION;
    @Message(name = "SignLinkModeSignSelected", message = "&aSign selected!")
    public static String SIGN_LINK_MODE_SIGN_SELECTED;
    @Message(name = "SignLinkModeMultipleWgRegionsAtPosition", message = "&4Could not select WorldGuard-Region!" +
            "There is more than one region available! You can add unwanted regions to the ignore-list" +
            "located in the config.yml, if you want ARM to ignore that regions!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS;
    @Message(name = "SignLinkModeNoWgRegionAtPosition", message = "&4Could not select WorldGuard-Region! There is no region at your position!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION;
    @Message(name = "SignLinkModeCouldNotIdentifyWorld", message = "&4Could not identify world! Please select the WorldGuard-Region again!")
    public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD;
    @Message(name = "SignLinkModeNoSignSelected", message = "&4You have not selected a sign")
    public static String SIGN_LINK_MODE_NO_SIGN_SELECTED;
    @Message(name = "SignLinkModeNoWgRegionSelected", message = "&4You have not selected a WorldGuard-Region")
    public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED;
    @Message(name = "SignLinkModeSelectedRegion", message = "&aSelected region: %regionid%")
    public static String SIGN_LINK_MODE_REGION_SELECTED;
    @Message(name = "SchematicNotFoundErrorUser", message = "&4It seems like the schematic of your region %regionid% has not been created. Please contact an admin!")
    public static String SCHEMATIC_NOT_FOUND_ERROR_USER;
    @Message(name = "EntityLimitHelpHeadline", message = "&6=====[AdvancedRegionMarket EntityLimit Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String ENTITYLIMIT_HELP_HEADLINE;
    @Message(name = "EntityLimitGroupNotExist", message = "&4EntityLimitGroup does not exist!")
    public static String ENTITYLIMITGROUP_DOES_NOT_EXIST;
    @Message(name = "EntityLimitSet", message = "&aEntityLimit has been set!")
    public static String ENTITYLIMIT_SET;
    @Message(name = "EntityLimitRemoved", message = "&aEntityLimit has been removed!")
    public static String ENTITYLIMIT_REMOVED;
    @Message(name = "EntityTypeDoesNotExist", message = "&4The entitytype &6%entitytype% &4does not exist!")
    public static String ENTITYTYPE_DOES_NOT_EXIST;
    @Message(name = "EntityLimitGroupNotContainEntityLimit", message = "&4The selected EntityLimitGroup does not contain the selected EntityType")
    public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT;
    @Message(name = "EntityLimitTotal", message = "Total")
    public static String ENTITYLIMIT_TOTAL;
    @Message(name = "SubregionPaybackPercentageError", message = "&4The selected region is a subregion. You can edit the paybackPercentage for all subregions in the config.yml!")
    public static String SUBREGION_PAYBACKPERCENTAGE_ERROR;
    @Message(name = "EntityLimitCheckHeadline", message = "&6===[EntityLimitCheck for %regionid%]===")
    public static String ENTITYLIMIT_CHECK_HEADLINE;
    @Message(name = "EntityLimitCheckPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String ENTITYLIMIT_CHECK_PATTERN;
    @Message(name = "EntityLimitCheckExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMIT_CHECK_EXTENSION_INFO;
    @Message(name = "EntityLimitGroupAlreadyExists", message = "&4Group already exists!")
    public static String ENTITYLIMITGROUP_ALREADY_EXISTS;
    @Message(name = "EntityLimitGroupCreated", message = "&aEntitylimitgroup has been created!")
    public static String ENTITYLIMITGROUP_CREATED;
    @Message(name = "EntityLimitGroupCanNotRemoveSystem", message = "&4You can not remove a system-EntityLimitGroup!")
    public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM;
    @Message(name = "EntityLimitGroupDeleted", message = "&aEntitylimitgroup has been deleted!")
    public static String ENTITYLIMITGROUP_DELETED;
    @Message(name = "EntityLimitGroupInfoHeadline", message = "&6======[Entitylimitgroup Info]======")
    public static String ENTITYLIMITGROUP_INFO_HEADLINE;
    @Message(name = "EntityLimitGroupInfoGroupname", message = "&6Groupname: ")
    public static String ENTITYLIMITGROUP_INFO_GROUPNAME;
    @Message(name = "EntityLimitGroupInfoPattern", message = "&6%entitytype%: &r%softlimitentities% %entityextensioninfo%")
    public static String ENTITYLIMITGROUP_INFO_PATTERN;
    @Message(name = "EntityLimitInfoExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO;
    @Message(name = "EntityLimitGroupListHeadline", message = "&6EntityLimitGroups:")
    public static String ENTITYLIMITGROUP_LIST_HEADLINE;
    @Message(name = "SubregionEntityLimitOnlyForSubregions", message = "&4SubregionEntityLimitGroup only for subregions")
    public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS;
    @Message(name = "MassactionSplitter", message = "&6all regions with regionkind &a%regionkind%")
    public static String MASSACTION_SPLITTER;
    @Message(name = "SubregionEntityLimitError", message = "&4Could not change EntiyLimitGroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_ENTITYLIMITGROUP_ERROR;
    @Message(name = "SubregionFlagGroupError", message = "&4Could not change Flaggroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_FLAGGROUP_ERROR;
    @Message(name = "GUIEntityLimitItemButton", message = "&6EntityLimits")
    public static String GUI_ENTITYLIMIT_ITEM_BUTTON;
    @Message(name = "GUIEntityLimitItemLore", message = {"&6Click to display the entity-limits",
            "&6for this region in chat",
            "%entityinfopattern%",
            "",
            "You can expand your entity-limit with:",
            "&6/arm entitylimit buyextra %regionid% [ENTITYTYPE/total]"})
    public static List<String> GUI_ENTITYLIMIT_ITEM_LORE;
    @Message(name = "GUIEntityLimitInfoPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN;
    @Message(name = "GUIEntityLimitInfoExtensionInfo", message = "&6&oMax. &r%hardlimitentities% &6for &r%priceperextraentity%%currency%&6/entity")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO;
    @Message(name = "EntityLimitGroupEntityLimitAlreadyUnlimited", message = "&4EntityLimit for the selected entity and region is already unlimited!")
    public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED;
    @Message(name = "EntityLimitGroupExtraEntitiesSet", message = "&aExtra-Entities have been set!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET;
    @Message(name = "EntityLimitGroupExtraEntitiesExpandSuccess", message = "&aYou have sucessfully expanded the entitylimit to &6%softlimitentities% &aentities! (For &6%priceperextraentity%%currency%&a)")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS;
    @Message(name = "EntityLimitGroupExtraEntitiesHardlimitReached", message = "&4Can not buy another entity-expansion! Hardlimit has been reached!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED;
    @Message(name = "EntityLimitGroupExtraEntitiesSetSubregionError", message = "&4Can not change entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR;
    @Message(name = "EntityLimitGroupExtraEntitiesBuySubregionError", message = "&4Can not expand entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR;
    @Message(name = "ApplyPresetSubregionError", message = "&4Can not apply preset! Region is a subregion")
    public static String APPLY_PRESET_SUBREGION_ERROR;
    @Message(name = "EntityLimitGroupCouldNotspawnEntity", message = "&4Could not spawn entity on region &6%region%&4!\n" +
            "The not spawned entity would exceed the region\'s entitylimit. For more information type &6/arm entitylimit check %region%&4!\n" +
            "Everybody on region %region% received this message! If you are not a member of this region, you can ignore this message.")
    public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY;
    @Message(name = "ArmBasicCommandMessage", message = "&6AdvancedRegionMarket v%pluginversion% by Alex9849\n" +
            "&6Download: &3https://bit.ly/2CfO3An\n" +
            "&6Get a list with all commands with &3/arm help")
    public static String ARM_BASIC_COMMAND_MESSAGE;
    @Message(name = "RegionKindCreated", message = "&aRegionKind created!")
    public static String REGIONKIND_CREATED;
    @Message(name = "RegionKindAlreadyExists", message = "&4RegionKind already exists!")
    public static String REGIONKIND_ALREADY_EXISTS;
    @Message(name = "RegionKindDeleted", message = "&aRegionKind deleted!")
    public static String REGIONKIND_DELETED;
    @Message(name = "RegionKindCanNotRemoveSystem", message = "&4You can not remove a system-RegionKind!")
    public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM;
    @Message(name = "RegionKindListHeadline", message = "&6Regionkinds:")
    public static String REGIONKIND_LIST_HEADLINE;
    @Message(name = "RegionKindModified", message = "&aRegionKind modified!")
    public static String REGIONKIND_MODIFIED;
    @Message(name = "MaterialNotFound", message = "&4Material not found!")
    public static String MATERIAL_NOT_FOUND;
    @Message(name = "RegionKindLoreLineNotExist", message = "&aThe selected lore-line does not exist!")
    public static String REGIONKIND_LORE_LINE_NOT_EXIST;
    @Message(name = "RegionKindInfoHeadline", message = "&6=========[Regionkind info]=========")
    public static String REGIONKIND_INFO_HEADLINE;
    @Message(name = "RegionKindInfoInternalName", message = "&6Internal name: %regionkind%")
    public static String REGIONKIND_INFO_INTERNAL_NAME;
    @Message(name = "RegionKindInfoDisplayName", message = "&6Displayname: %regionkinddisplay%")
    public static String REGIONKIND_INFO_DISPLAY_NAME;
    @Message(name = "RegionKindInfoMaterial", message = "&6Material: %regionkinditem%")
    public static String REGIONKIND_INFO_MATERIAL;
    @Message(name = "RegionKindInfoDisplayInGui", message = "&6DisplayInGui: %regionkinddisplayingui%")
    public static String REGIONKIND_INFO_DISPLAY_IN_GUI;
    @Message(name = "RegionKindInfoDisplayInLimits", message = "&6DisplayInLimits: %regionkinddisplayinlimits%")
    public static String REGIONKIND_INFO_DISPLAY_IN_LIMITS;
    @Message(name = "RegionKindInfoLore", message = "&6Lore:")
    public static String REGIONKIND_INFO_LORE;
    @Message(name = "RegionkindInfo", message = {"&6======[Regionkind Info]======",
            "&9Name: &e%regionkind%",
            "&9DisplayName: &r%regionkinddisplay%",
            "&9Item: &e%regionkinditem%",
            "&9Display in limits: &e%regionkinddisplayinlimits%",
            "&9Display in gui: &e%regionkinddisplayingui%",
            "&9In RegionkindGroups: &e%regionkindregionkindgroups%",
            "&9Lore:",
            "%regionkindlorelist%"})
    public static List<String> REGIONKIND_INFO;
    @Message(name = "RegionKindHelpHeadline", message = "&6=====[AdvancedRegionMarket RegionKind Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String REGIONKIND_HELP_HEADLINE;
    @Message(name = "PlayerNotFound", message = "&4Could not find selected player!")
    public static String PLAYER_NOT_FOUND;
    @Message(name = "RegionInfoSellregionUser", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e&e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION;
    @Message(name = "RegionInfoRentregionUser", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION;
    @Message(name = "RegionInfoContractregionUser", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_CONTRACTREGION;
    @Message(name = "RegionInfoSellregionAdmin", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION_ADMIN;
    @Message(name = "RegionInfoRentregionAdmin", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION_ADMIN;
    @Message(name = "RegionInfoContractregionAdmin", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_CONTRACTREGION_ADMIN;
    @Message(name = "RegionInfoSellregionSubregion", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%"})
    public static List<String> REGION_INFO_SELLREGION_SUBREGION;
    @Message(name = "RegionInfoRentregionSubregion", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%"})
    public static List<String> REGION_INFO_RENTREGION_SUBREGION;
    @Message(name = "RegionInfoContractregionSubregion", version = 2, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &7%regionkindregionkindgroupsdisplaywithbrackets%",
            "&9FlagGroup: &e%flaggroup%",
            "&9Landlord: &e%landlord% &7(Gets the money)",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: &e%paypackpercentage%"})
    public static List<String> REGION_INFO_CONTRACTREGION_SUBREGION;
    @Message(name = "GUIFlageditorButton", message = "&6FlagEditor")
    public static String GUI_FLAGEDITOR_BUTTON;
    @Message(name = "GUIFlageditorMenuName", message = "&1FlagEditor (%region%)")
    public static String GUI_FLAGEDITOR_MENU_NAME;
    @Message(name = "GUIFlageditorDeleteFlagButton", message = "&4Delete flag")
    public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON;
    @Message(name = "GUIFlageditorSetFlagGroupAllButton", message = "&9Set for everyone")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON;
    @Message(name = "GUIFlageditorSetFlagGroupMembersButton", message = "&9Set for members and owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON;
    @Message(name = "GUIFlageditorSetFlagGroupOwnersButton", message = "&9Set for owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON;
    @Message(name = "GUIFlageditorSetFlagGroupNonMembersButton", message = "&9Set for non members and non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON;
    @Message(name = "GUIFlageditorSetFlagGroupNonOwnersButton", message = "&9Set for non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON;
    @Message(name = "GUIFlageditorSetStateflagAllowButton", message = "&2Allow")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON;
    @Message(name = "GUIFlageditorSetStateflagDenyButton", message = "&4Deny")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON;
    @Message(name = "GUIFlageditorSetBooleanflagTrueButton", message = "&2Yes")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON;
    @Message(name = "GUIFlageditorSetBooleanflagFalseButton", message = "&4No")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON;
    @Message(name = "GUIFlageditorSetStringflagSetMessageButton", message = "&2Set message")
    public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON;
    @Message(name = "GUIFlageditorSetIntegerflagSetIntegerButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON;
    @Message(name = "GUIFlageditorSetDoubleflagSetDoubleButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON;
    @Message(name = "FlageditorFlagNotActivated", message = "&4Flag not activated!")
    public static String FlAGEDITOR_FLAG_NOT_ACTIVATED;
    @Message(name = "FlageditorFlagHasBeenDeleted", message = "&2Flag has been deleted!")
    public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED;
    @Message(name = "FlageditorFlagHasBeenUpdated", message = "&2Flag has been updated!")
    public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED;
    @Message(name = "FlageditorFlagCouldNotBeUpdated", message = "Could not modify flag %flag%!")
    public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED;
    @Message(name = "FlageditorStringflagSetMessageInfo", message = "&9Please write down a message:")
    public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO;
    @Message(name = "FlageditorIntegerflagSetMessageInfo", message = "&9Please write down a number that does not have decimals:")
    public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO;
    @Message(name = "FlageditorDoubleflagSetMessageInfo", message = "&9Please write down a number:")
    public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO;
    @Message(name = "GUIFlageditorResetButton", message = "&4Reset all Flags to default settings")
    public static String GUI_FLAGEDITOR_RESET_BUTTON;
    @Message(name = "GUIFlageditorUnknownFlagSetPropertiesButton", message = "&2Set properties")
    public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON;
    @Message(name = "GUIFlageditorUnknownFlagSetPropertiesInfo", message = "&9Please write down your new flag properties: FlaggroupDoesNotExist: '&4Flaggroup does not exist!")
    public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO;
    @Message(name = "FlaggroupDoesNotExist", message = "&4Flaggroup does not exist!")
    public static String FLAGGROUP_DOES_NOT_EXIST;
    @Message(name = "SubregionFlaggroupOnlyForSubregions", message = "&4Subregion flaggroup only for subregions")
    public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS;
    @Message(name = "GUITeleportToRegionButtonLore", message = {"Click to teleport you to",
            "your region"})
    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE;
    @Message(name = "GUIMakeOwnerButtonLore", message = {"Click to transfer your owner rights",
            "to the selected member.",
            "&4WARNING: &cYou will lose your owner",
            "&crights and become a member'"})
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE;
    @Message(name = "GUIRemoveMemberButtonLore", message = {"Click to remove the selected member",
            "from your region"})
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE;
    @Message(name = "GUIResetRegionButtonLore", message = {"Click to reset your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be resetted and everything on it will",
            "&cbe deleted!",
            "",
            "&cYou can only reset you region once every %userresetcooldown%",
            "&2You and all members keep their rights on the region"})
    public static List<String> GUI_RESET_REGION_BUTTON_LORE;
    @Message(name = "TakeOverItemLore", message = {"&aYou are a member of this region.",
            "&aThe owner of it hasn''''t been",
            "&aonline for a long time. You",
            "&acan transfer the owner rights to your",
            "&aaccount for free. The actual owner",
            "&aof it will become a member of the region.",
            "&cIf the region does not get transferred",
            "&cor the owner does not come online",
            "&cwithin &7%inactivityresetin-countdown-short% &cthe",
            "&cregion will be resetted and everybody on it",
            "&cwill lose their rights.",
            "&cAfterwards it will go back for sale!"})
    public static List<String> GUI_TAKEOVER_ITEM_LORE;
    @Message(name = "GUIExtendRentRegionButtonLore", version = 4, message = {"&aClick to extend your region for &6%extendtime-current-writtenout%",
            "&athis will cost you &6%price-current%%currency%&a!",
            "&aThis region will expire in &6%remainingtime-countdown-short%&a.",
            "&aYou can extend your region up to &6%maxrenttime-writtenout%&a."})
    public static List<String> GUI_EXTEND_BUTTON_LORE;
    @Message(name = "GUIRentRegionLore", message = {"&aExpires in &6%remainingtime-countdown-short%"})
    public static List<String> GUI_RENT_REGION_LORE;
    @Message(name = "GUIUserSellButtonLore", message = {"Click to sell your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be released and all blocks on it will be",
            "&cresetted! You and all members of it will loose",
            "&ctheir rights on it.",
            "&cYou will get &6%paybackmoney%%currency% &cback"})
    public static List<String> GUI_USER_SELL_BUTTON_LORE;
    @Message(name = "MemberlistInfoLore", message = {"&aYou can be added as a member to",
            "&athe region of someone else in order",
            "&ato build with him together",
            "&aJust ask a region owner to add you with:",
            "&6/arm addmember REGIONID USERNAME",
            "&aYou need to be online for this"})
    public static List<String> GUI_MEMBER_INFO_LORE;
    @Message(name = "GUIContractItemLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_ITEM_LORE;
    @Message(name = "GUIContractItemRegionLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_REGION_LORE;
    @Message(name = "OwnerMemberlistInfoLore", message = {"&aYou can add members to your region",
            "&ain order to build with them together",
            "&aYou can add members with:",
            "&6/arm addmember %regionid% USERNAME",
            "&aMembers need to be online to add them"})
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE;
    @Message(name = "GUISubregionManagerNoSubregionItemLore", message = {"&aYou do not have any subregions on your region.",
            "&aYou can create a new subregion, that you",
            "&acan sell to other players by typing",
            "&6/arm subregion tool &aand following displayed the steps"})
    public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE;
    @Message(name = "SellPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket SellPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SELLPRESET_HELP_HEADLINE;
    @Message(name = "ContractPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket ContractPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String CONTRACTPRESET_HELP_HEADLINE;
    @Message(name = "RentPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket RentPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String RENTPRESET_HELP_HEADLINE;
    @Message(name = "InfoDeactivated", message = "&4deactivated")
    public static String INFO_DEACTIVATED;
    @Message(name = "InfoNotSold", message = "&4Region not sold!")
    public static String INFO_REGION_NOT_SOLD;
    @Message(name = "InfoNow", message = "&2now")
    public static String INFO_NOW;
    @Message(name = "InfoNotCalculated", message = "&8Awaiting calculation...")
    public static String INFO_NOT_CALCULATED;
    @Message(name = "CouldNotFindOrLoadSchematicLog", message = "&4Could not find or load schematic for region %region% in world %world%! You can regenerate it with /arm updateschematic %region%")
    public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG;
    @Message(name = "RegionSoldBackSuccessfully", message = "&2Your region &6%regionid% &2has successfully been sold back to the server! " +
            "&6%paybackmoney%%currency% &2have been added to your account!")
    public static String REGION_SOLD_BACK_SUCCESSFULLY;
    @Message(name = "RegionModifiedBoolean", message = "&6%option% %state% &6for &a%selectedregions%&6!")
    public static String REGION_MODIFIED_BOOLEAN;
    @Message(name = "RegionModified", message = "&6%option% &6modified for %selectedregions%&6!")
    public static String REGION_MODIFIED;
    @Message(name = "UpdatingSchematic", message = "&8Updating schematic...")
    public static String UPDATING_SCHEMATIC;
    @Message(name = "SchematicUpdated", message = "&aSchematic updated!")
    public static String SCHEMATIC_UPDATED;
    @Message(name = "ContractRegionTerminated", message = "&6Your contractregion &a%region% &6has successfully been " +
            "&4terminated&6! It will be resetted in &a%remainingtime-countdown-short% &6except it gets reactivated!")
    public static String CONTRACTREGION_TERMINATED;
    @Message(name = "ContractRegionReactivated", message = "&6Your contractregion &a%region% &6has successfully " +
            "been &areactivated&6! It will automatically be extended in &a%remainingtime-countdown-short% &6if " +
            "you can pay for the rent!")
    public static String CONTRACTREGION_REACTIVATED;
    @Message(name = "RegionInfoFeatureDisabled", message = "&4Feature disbaled!")
    public static String REGION_INFO_FEATURE_DISABLED;
    @Message(name = "FlagGroupFeatureDisabled", message = "&4FlagGroups are currently disabled! You can activate them in the config.yml!")
    public static String FLAGGROUP_FEATURE_DISABLED;
    @Message(name = "BackupCreated", message = "&aBackup created!")
    public static String BACKUP_CREATED;
    @Message(name = "BackupRestored", message = "&aBackup restored!")
    public static String BACKUP_RESTORED;
    @Message(name = "CouldNotLoadBackup", message = "&4Could not load backup! Maybe it does not exist or the file is corrupted!")
    public static String COULD_NOT_LOAD_BACKUP;
    @Message(name = "BackupListHeader", message = "&6=======[Backups of region %regionid%]=======")
    public static String BACKUP_LIST_HEADER;

    public static void reload(File savePath, MessageLocale locale) {
        YamlConfiguration config = updateAndWriteConfig(locale, savePath);
        load(config);
    }

    private static YamlConfiguration updateAndWriteConfig(MessageLocale locale, File savePath) {
        YamlConfiguration config;
        if (savePath.exists()) {
            config = YamlConfiguration.loadConfiguration(savePath);
        } else {
            config = new YamlConfiguration();
        }
        int configVersion = config.getInt("FileVersion");
        int newConfigVersion = configVersion;
        ConfigurationSection localeconfigMessages = null;
        int localeFileVersion = 0;
        if (MessageLocale.EN != locale) {
            InputStreamReader reader = new InputStreamReader(AdvancedRegionMarket.getInstance()
                    .getResource("messages_" + locale.code() + ".yml"), Charset.forName("UTF-8"));
            YamlConfiguration localeConfig = YamlConfiguration.loadConfiguration(reader);
            localeFileVersion = localeConfig.getInt("FileVersion");
            localeconfigMessages = localeConfig.getConfigurationSection("Messages");
        }

        ConfigurationSection configMessages = config.getConfigurationSection("Messages");
        if (configMessages == null) {
            configMessages = new YamlConfiguration();
        }

        boolean fileUpdated = false;
        for (Field field : Messages.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Message.class)) {
                continue;
            }
            int requestedVersion = getRequestedVersion(field);
            String serializedKey = getSerializedKey(field);
            if ((configVersion >= requestedVersion)
                    && configMessages.get(serializedKey) != null
                    && field.getType().isAssignableFrom(configMessages.get(serializedKey).getClass())) {
                continue;
            }
            Object replaceMessage = getMessage(field);
            if (localeconfigMessages != null) {
                Object localeConfigMessage = localeconfigMessages.get(serializedKey);
                if (localeConfigMessage != null && localeFileVersion >= requestedVersion
                        && field.getType().isAssignableFrom(localeConfigMessage.getClass())) {
                    replaceMessage = localeConfigMessage;
                }
            }

            configMessages.set(serializedKey, replaceMessage);
            newConfigVersion = Math.max(newConfigVersion, requestedVersion);
            fileUpdated = true;
        }

        if (fileUpdated) {
            config.set("FileVersion", newConfigVersion);
            config.set("Messages", configMessages);
            config.options().copyDefaults(true);
            try {
                config.save(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    private static void load(YamlConfiguration config) {
        ConfigurationSection cs = config.getConfigurationSection("Messages");
        if (cs == null) {
            return;
        }

        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Message.class)) {
                field.setAccessible(true);
                Object parsedOption = cs.get(getSerializedKey(field));
                if (parsedOption instanceof String) {
                    parsedOption = ChatColor.translateAlternateColorCodes('&', (String) parsedOption);

                } else if (parsedOption instanceof List<?>) {
                    List<String> parsedOptionList = new ArrayList<>();
                    for (Object listElement : (List<?>) parsedOption) {
                        if (listElement instanceof String) {
                            parsedOptionList.add(ChatColor.translateAlternateColorCodes('&', (String) listElement));
                        }
                    }
                    parsedOption = parsedOptionList;
                }

                try {
                    if (field.getType().isAssignableFrom(parsedOption.getClass())) {
                        field.set(Messages.class, parsedOption);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }
    }

    private static Object getMessage(Field field) {
        if (!field.isAnnotationPresent(Message.class)) {
            return null;
        }
        List<String> messageList = Arrays.asList(field.getAnnotation(Message.class).message());
        if (field.getType().isAssignableFrom(String.class)) {
            return getStringList(messageList, x -> x, "\n");
        }
        return messageList;
    }

    private static String getSerializedKey(Field field) {
        if (field.isAnnotationPresent(Message.class)) {
            String annotationValue = field.getAnnotation(Message.class).name();
            if (annotationValue.isEmpty()) {
                annotationValue = field.getName();
            }
            return annotationValue;
        }
        return null;
    }

    private static int getRequestedVersion(Field field) {
        if (field.isAnnotationPresent(Message.class)) {
            return field.getAnnotation(Message.class).version();
        }
        return 0;
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
        if (object != null) {
            return stringGetter.get(object);
        } else {
            return nullString;
        }
    }

}

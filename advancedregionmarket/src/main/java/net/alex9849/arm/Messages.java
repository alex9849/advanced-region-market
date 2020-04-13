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
import java.util.*;

public class Messages {
    public enum MessageLocale {
        EN("en"), DE("de"), FR("fr"), RU("ru");
        private String code;

        MessageLocale(String code) {
            this.code = code;
        }

        String code() {
            return code;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SerialzedString {
        public String name();
        public String message();
        public int version() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SerialzedStringList {
        public String name();
        public String[] message();
        public int version() default 0;
    }

    @SerialzedString(name = "Prefix", message = "&b[ARM]&r ")
    public static String PREFIX;
    @SerialzedString(name = "Buymessage", message = "&aRegion successfully bought!")
    public static String REGION_BUYMESSAGE;
    @SerialzedString(name = "NotEnoughMoney", message = "&4You do not have enough money!")
    public static String NOT_ENOUGH_MONEY;
    @SerialzedString(name = "RegionAlreadySold", message = "&4Region already Sold!")
    public static String REGION_ALREADY_SOLD;
    @SerialzedString(name = "NoPermission", message = "&4You do not have permission!")
    public static String NO_PERMISSION;
    @SerialzedString(name = "WorldDoesNotExist", message = "&4The selected world does not exist!")
    public static String WORLD_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionDoesNotExist", message = "&4The selected region does not exist in this (or the selected) world!")
    public static String REGION_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionAddedToARM", message = "&7Regionsign has been created and region has been added to ARM!")
    public static String REGION_ADDED_TO_ARM;
    @SerialzedString(name = "SignAddedToRegion", message = "&7Region already exists! The sign has been added to the region!")
    public static String SIGN_ADDED_TO_REGION;
    @SerialzedString(name = "SignRemovedFromRegion", message = "&7Regionsign removed! %remaining% Sign(s) remaining before region gets removed from ARM!")
    public static String SIGN_REMOVED_FROM_REGION;
    @SerialzedString(name = "RegionRemovedFromARM", message = "&7The region has been removed from ARM!")
    public static String REGION_REMOVED_FROM_ARM;
    @SerialzedString(name = "SellSign1", message = "&2For Sale")
    public static String SELL_SIGN1;
    @SerialzedString(name = "SellSign2", message = "%regionid%")
    public static String SELL_SIGN2;
    @SerialzedString(name = "SellSign3", message = "%price%%currency%")
    public static String SELL_SIGN3;
    @SerialzedString(name = "SellSign4", message = "%dimensions%")
    public static String SELL_SIGN4;
    @SerialzedString(name = "SoldSign1", message = "&4Sold")
    public static String SOLD_SIGN1;
    @SerialzedString(name = "SoldSign2", message = "%regionid%")
    public static String SOLD_SIGN2;
    @SerialzedString(name = "SoldSign3", message = "")
    public static String SOLD_SIGN3;
    @SerialzedString(name = "SoldSign4", message = "%owner%")
    public static String SOLD_SIGN4;
    @SerialzedString(name = "Currency", message = "$")
    public static String CURRENCY;
    @SerialzedString(name = "CommandOnlyIngame", message = "&4This command can only be executed ingame!")
    public static String COMMAND_ONLY_INGAME;
    @SerialzedString(name = "RegionInfoExpired", message = "&4Expired")
    public static String REGION_INFO_EXPIRED;
    @SerialzedString(name = "GUIMainMenuName", message = "&1ARM - Menu")
    public static String GUI_MAIN_MENU_NAME;
    @SerialzedString(name = "GUIGoBack", message = "&6Go back")
    public static String GUI_GO_BACK;
    @SerialzedString(name = "GUIMyOwnRegions", message = "&6My regions (Owner)")
    public static String GUI_MY_OWN_REGIONS;
    @SerialzedString(name = "GUIMemberRegionsMenuName", message = "&1ARM - My regions (Member)")
    public static String GUI_MEMBER_REGIONS_MENU_NAME;
    @SerialzedString(name = "GUIMyMemberRegions", message = "&6My regions (Member)")
    public static String GUI_MY_MEMBER_REGIONS;
    @SerialzedString(name = "GUISearchFreeRegion", message = "&6Search free region")
    public static String GUI_SEARCH_FREE_REGION;
    @SerialzedString(name = "GUIOwnRegionsMenuName", message = "&1ARM - My regions (Owner)")
    public static String GUI_OWN_REGIONS_MENU_NAME;
    @SerialzedString(name = "GUIMembersButton", message = "&6Members")
    public static String GUI_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIShowInfosButton", message = "&6Show infos")
    public static String GUI_SHOW_INFOS_BUTTON;
    @SerialzedString(name = "GUITeleportToRegionButton", message = "&6Teleport to region")
    public static String GUI_TELEPORT_TO_REGION_BUTTON;
    @SerialzedString(name = "GUIRegionFinderMenuName", message = "&1ARM - Regionfinder")
    public static String GUI_REGION_FINDER_MENU_NAME;
    @SerialzedString(name = "GUIMemberListMenuName", message = "&1ARM - Members of %regionid%")
    public static String GUI_MEMBER_LIST_MENU_NAME;
    @SerialzedString(name = "GUIMakeOwnerButton", message = "&aMake owner")
    public static String GUI_MAKE_OWNER_BUTTON;
    @SerialzedString(name = "GUIRemoveMemberButton", message = "&4Remove")
    public static String GUI_REMOVE_MEMBER_BUTTON;
    @SerialzedString(name = "GUIMakeOwnerWarningName", message = "&4&lAre you sure?")
    public static String GUI_MAKE_OWNER_WARNING_NAME;
    @SerialzedString(name = "GUIWarningYes", message = "&aYes")
    public static String GUI_YES;
    @SerialzedString(name = "GUIWarningNo", message = "&4No")
    public static String GUI_NO;
    @SerialzedString(name = "RegionTeleportMessage", message = "&7You have been teleported to %regionid%")
    public static String REGION_TELEPORT_MESSAGE;
    @SerialzedString(name = "NoPermissionsToBuyThisKindOfRegion", message = "&4You do not have permission to buy this kind of region (You need the permission &6arm.buykind.%regionkind%&4)")
    public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION;
    @SerialzedString(name = "NoFreeRegionWithThisKind", message = "&7No free region with this type found :(")
    public static String NO_FREE_REGION_WITH_THIS_KIND;
    @SerialzedString(name = "RegionkindDoesNotExist", message = "&4The selected regionkind does not exist!")
    public static String REGIONKIND_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionNowAvailable", message = "&aRegion is now available!")
    public static String REGION_NOW_AVAILABLE;
    @SerialzedString(name = "NoRegionAtPlayersPosition", message = "&7Could not find a region at your position!")
    public static String NO_REGION_AT_PLAYERS_POSITION;
    @SerialzedString(name = "RegionAddMemberNotOnline", message = "&4The selected player is not online!")
    public static String REGION_ADD_MEMBER_NOT_ONLINE;
    @SerialzedString(name = "RegionAddMemberAdded", message = "&aMember has been added!")
    public static String REGION_ADD_MEMBER_ADDED;
    @SerialzedString(name = "RegionRemoveMemberNotAMember", message = "&4The selected player is not a member of the region")
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER;
    @SerialzedString(name = "RegionRemoveMemberRemoved", message = "&aMember has been removed!")
    public static String REGION_REMOVE_MEMBER_REMOVED;
    @SerialzedString(name = "GUIResetRegionButton", message = "&4Reset region")
    public static String GUI_RESET_REGION_BUTTON;
    @SerialzedString(name = "GUIResetRegionWarningName", message = "&4&lReset your region?")
    public static String GUI_RESET_REGION_WARNING_NAME;
    @SerialzedString(name = "ResetComplete", message = "&aReset complete!")
    public static String RESET_COMPLETE;
    @SerialzedString(name = "ResetRegionCooldownError", message = "&7You have to wait&6 %remaininguserresetcooldown-countdown-writtenout% &7till you can reset your region again")
    public static String RESET_REGION_COOLDOWN_ERROR;
    @SerialzedString(name = "GUIRegionTakeOverMenuName", message = "&4Region take-over")
    public static String GUI_TAKEOVER_MENU_NAME;
    @SerialzedString(name = "RegionTransferCompleteMessage", message = "&aTransfer complete!")
    public static String REGION_TRANSFER_COMPLETE_MESSAGE;
    @SerialzedString(name = "GUICloseWindow", message = "&6Close window")
    public static String GUI_CLOSE;
    @SerialzedString(name = "RentSign1", message = "&2For Rent")
    public static String RENT_SIGN1;
    @SerialzedString(name = "RentSign2", message = "%regionid%")
    public static String RENT_SIGN2;
    @SerialzedString(name = "RentSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENT_SIGN3;
    @SerialzedString(name = "RentSign4", message = "Max.: %maxrenttime-writtenout%")
    public static String RENT_SIGN4;
    @SerialzedString(name = "RentedSign1", message = "&4Rented")
    public static String RENTED_SIGN1;
    @SerialzedString(name = "RentedSign2", message = "%regionid%/%owner%")
    public static String RENTED_SIGN2;
    @SerialzedString(name = "RentedSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENTED_SIGN3;
    @SerialzedString(name = "RentedSign4", message = "%remainingtime-countdown-short%")
    public static String RENTED_SIGN4;
    @SerialzedString(name = "RentExtendMessage", message = "&aRegion extended for &6%extendtime-writtenout%&a (For %price%%currency%. Remaining time: &6%remainingtime-countdown-short%")
    public static String RENT_EXTEND_MESSAGE;
    @SerialzedString(name = "RentExtendMaxRentTimeExceeded", message = "&4You can not pay this region for more than &6%maxrenttime-writtenout% &4in advance!")
    public static String RENT_EXTEND_MAX_RENT_TIME_EXCEEDED;
    @SerialzedString(name = "GUIExtendRentRegionButton", message = "&1Extend region")
    public static String GUI_EXTEND_BUTTON;
    @SerialzedString(name = "Complete", message = "&aComplete!")
    public static String COMPLETE;
    @SerialzedString(name = "RegionBuyOutOfLimit", message = "&4Out of Limit! You have &7%playerownedkind%/%limitkind% &4%regionkind%-regions and &7%playerownedtotal%/%limittotal% &4Regions total!")
    public static String REGION_BUY_OUT_OF_LIMIT;
    @SerialzedString(name = "RegionErrorCanNotBuildHere", message = "&4You are only allowed to break blocks you placed here!")
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE;
    @SerialzedString(name = "Unlimited", message = "unlimited")
    public static String UNLIMITED;
    @SerialzedString(name = "GUIUserSellButton", message = "&4Reset and sell Region")
    public static String GUI_USER_SELL_BUTTON;
    @SerialzedString(name = "GUIUserSellWarning", message = "&4&lSell your region?")
    public static String GUI_USER_SELL_WARNING;
    @SerialzedString(name = "LimitInfoTop", message = "&6=========[Limit Info]=========")
    public static String LIMIT_INFO_TOP;
    @SerialzedString(name = "LimitInfo", message = "&6%regionkind%: %playerownedkind%/%limitkind%")
    public static String LIMIT_INFO;
    @SerialzedString(name = "GUILimitButton", message = "&6My limits")
    public static String GUI_MY_LIMITS_BUTTON;
    @SerialzedString(name = "MemberlistInfo", message = "&6How to become a Member:")
    public static String GUI_MEMBER_INFO_ITEM;
    @SerialzedString(name = "AddMemberMaxMembersExceeded", message = "&4Cloud not add member to region! You can only have %maxmembers% members on this region!")
    public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED;
    @SerialzedString(name = "RegionIsNotARentregion", message = "&4Region is not a rentregion!")
    public static String REGION_IS_NOT_A_RENTREGION;
    @SerialzedString(name = "RegionNotOwn", message = "&4You do not own this region!")
    public static String REGION_NOT_OWN;
    @SerialzedString(name = "RegionNotSold", message = "&4Region not sold!")
    public static String REGION_NOT_SOLD;
    @SerialzedString(name = "PresetRemoved", message = "&aPreset removed!")
    public static String PRESET_REMOVED;
    @SerialzedString(name = "PresetSet", message = "&aPreset set!")
    public static String PRESET_SET;
    @SerialzedString(name = "PresetSaved", message = "&aPreset saved!")
    public static String PRESET_SAVED;
    @SerialzedString(name = "PresetAlreadyExists", message = "&4A preset with this name already exists!")
    public static String PRESET_ALREADY_EXISTS;
    @SerialzedString(name = "PresetPlayerDontHasPreset", message = "&4You do not have a preset!")
    public static String PRESET_PLAYER_DONT_HAS_PRESET;
    @SerialzedString(name = "PresetDeleted", message = "&aPreset deleted!")
    public static String PRESET_DELETED;
    @SerialzedString(name = "PresetNotFound", message = "&4No preset with this name found!")
    public static String PRESET_NOT_FOUND;
    @SerialzedString(name = "PresetLoaded", message = "&aPreset loaded!")
    public static String PRESET_LOADED;
    @SerialzedString(name = "LimitInfoTotal", message = "&6Total")
    public static String LIMIT_INFO_TOTAL;
    @SerialzedString(name = "GUIRegionItemName", message = "%regionid% (%regionkinddisplay%)")
    public static String GUI_REGION_ITEM_NAME;
    @SerialzedString(name = "GUIRegionFinderRegionKindName", message = "&a&l%regionkinddisplay%")
    public static String GUI_REGIONFINDER_REGIONKIND_NAME;
    @SerialzedString(name = "RentRegionExpirationWarning", message = "&4WARNING! This RentRegion(s) will expire soon: &c")
    public static String RENTREGION_EXPIRATION_WARNING;
    @SerialzedString(name = "ContractSign1", message = "&2Contract")
    public static String CONTRACT_SIGN1;
    @SerialzedString(name = "ContractSign2", message = "&2available")
    public static String CONTRACT_SIGN2;
    @SerialzedString(name = "ContractSign3", message = "%regionid%")
    public static String CONTRACT_SIGN3;
    @SerialzedString(name = "ContractSign4", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SIGN4;
    @SerialzedString(name = "ContractSoldSign1", message = "&4Contract in use")
    public static String CONTRACT_SOLD_SIGN1;
    @SerialzedString(name = "ContractSoldSign2", message = "%regionid%/%owner%")
    public static String CONTRACT_SOLD_SIGN2;
    @SerialzedString(name = "ContractSoldSign3", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SOLD_SIGN3;
    @SerialzedString(name = "ContractSoldSign4", message = "%remainingtime-countdown-short%")
    public static String CONTRACT_SOLD_SIGN4;
    @SerialzedString(name = "ContractRegionExtended", message = "&aYour contract region %regionid% has been extended for %extendtime-writtenout%. (For %price%%currency%.)")
    public static String CONTRACT_REGION_EXTENDED;
    @SerialzedString(name = "GUIContractItem", message = "&6Manage contract")
    public static String GUI_CONTRACT_ITEM;
    @SerialzedString(name = "ContractRegionStatusActive", message = "&aActive")
    public static String CONTRACT_REGION_STATUS_ACTIVE;
    @SerialzedString(name = "ContractRegionStatusTerminated", message = "&4Terminated")
    public static String CONTRACT_REGION_STATUS_TERMINATED;
    @SerialzedString(name = "RegionIsNotAContractRegion", message = "&4Region is not a contractregion!")
    public static String REGION_IS_NOT_A_CONTRACT_REGION;
    @SerialzedString(name = "OwnerMemberlistInfo", message = "&6Adding members:")
    public static String GUI_OWNER_MEMBER_INFO_ITEM;
    @SerialzedString(name = "RegiontransferMemberNotOnline", message = "&4Member not online!")
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE;
    @SerialzedString(name = "RegiontransferLimitError", message = "&4Transfer aborted! (Region would exceed players limit)")
    public static String REGION_TRANSFER_LIMIT_ERROR;
    @SerialzedString(name = "SecondsSingular", message = "second")
    public static String TIME_SECONDS_SINGULAR;
    @SerialzedString(name = "MinutesSingular", message = "minute")
    public static String TIME_MINUTES_SINGULAR;
    @SerialzedString(name = "HoursSingular", message = "hour")
    public static String TIME_HOURS_SINGULAR;
    @SerialzedString(name = "DaysSingular", message = "day")
    public static String TIME_DAYS_SINGULAR;
    @SerialzedString(name = "SecondsPlural", message = "seconds")
    public static String TIME_SECONDS_PLURAL;
    @SerialzedString(name = "MinutesPlural", message = "minutes")
    public static String TIME_MINUTES_PLURAL;
    @SerialzedString(name = "HoursPlural", message = "hours")
    public static String TIME_HOURS_PLURAL;
    @SerialzedString(name = "DaysPlural", message = "days")
    public static String TIME_DAYS_PLURAL;
    @SerialzedString(name = "SecondsShort", message = "s")
    public static String TIME_SECONDS_SHORT;
    @SerialzedString(name = "MinutesShort", message = "m")
    public static String TIME_MINUTES_SHORT;
    @SerialzedString(name = "HoursShort", message = "h")
    public static String TIME_HOURS_SHORT;
    @SerialzedString(name = "DaysShort", message = "d")
    public static String TIME_DAYS_SHORT;
    @SerialzedString(name = "TimeUnitSplitter", message = " and ")
    public static String TIME_UNIT_SPLITTER;
    @SerialzedString(name = "TimeUnitSplitterShort", message = ":")
    public static String TIME_UNIT_SPLITTER_SHORT;
    @SerialzedString(name = "UserNotAMemberOrOwner", message = "&4You are not a member or owner of this region!")
    public static String NOT_A_MEMBER_OR_OWNER;
    @SerialzedString(name = "RegionInfoYes", message = "&2yes")
    public static String YES;
    @SerialzedString(name = "RegionInfoNo", message = "&4no")
    public static String NO;
    @SerialzedString(name = "RegionStats", message = "&6=========[Region stats]=========")
    public static String REGION_STATS;
    @SerialzedString(name = "RegionStatsPattern", message = "&8Used regions (%regionkind%&8):")
    public static String REGION_STATS_PATTERN;
    @SerialzedString(name = "TeleporterNoSaveLocation", message = "&4Could not find a save teleport location")
    public static String TELEPORTER_NO_SAVE_LOCATION_FOUND;
    @SerialzedString(name = "TeleporterDontMove", message = "&6Teleportation will commence in &c%time% Seconds&6. Do not move!")
    public static String TELEPORTER_DONT_MOVE;
    @SerialzedString(name = "TeleporterTeleportationAborded", message = "&4Teleportation aborded!")
    public static String TELEPORTER_TELEPORTATION_ABORDED;
    @SerialzedString(name = "OfferSent", message = "&aYour offer has been sent")
    public static String OFFER_SENT;
    @SerialzedString(name = "OfferAcceptedSeller", message = "&a%buyer% &aaccepted your offer")
    public static String OFFER_ACCEPTED_SELLER;
    @SerialzedString(name = "OfferAcceptedBuyer", message = "&aOffer accepted! You are now the owner of &c%regionid%")
    public static String OFFER_ACCEPTED_BUYER;
    @SerialzedString(name = "NoOfferToAnswer", message = "&4You dont have an offer to answer")
    public static String NO_OFFER_TO_ANSWER;
    @SerialzedString(name = "OfferRejected", message = "&aOffer rejected!")
    public static String OFFER_REJECTED;
    @SerialzedString(name = "OfferHasBeenRejected", message = "&4%buyer% &4rejected your offer!")
    public static String OFFER_HAS_BEEN_REJECTED;
    @SerialzedString(name = "NoOfferToReject", message = "&4You do not have an offer to reject")
    public static String NO_OFFER_TO_REJECT;
    @SerialzedString(name = "OfferCancelled", message = "&aYour offer has been cancelled!")
    public static String OFFER_CANCELED;
    @SerialzedString(name = "OfferHasBeenCancelled", message = "&4%seller% &4cancelled his offer!")
    public static String OFFER_HAS_BEEN_CANCELLED;
    @SerialzedString(name = "NoOfferToCancel", message = "&4You do not have an offer to cancel")
    public static String NO_OFFER_TO_CANCEL;
    @SerialzedString(name = "BuyerAlreadyGotAnOffer", message = "&4The selected buyer already got an offer that he has to answer first!")
    public static String BUYER_ALREADY_GOT_AN_OFFER;
    @SerialzedString(name = "SellerAlreadyCreatedAnOffer", message = "&4You have already created an offer! Please wait for an answer or cancel it first!")
    public static String SELLER_ALREADY_CREATED_AN_OFFER;
    @SerialzedString(name = "SellerDoesNotLongerOwnRegion", message = "&4%seller% &4does not longer own this region. His offer has been cancelled")
    public static String SELLER_DOES_NOT_LONGER_OWN_REGION;
    @SerialzedString(name = "IncomingOffer", message = "&c%seller% &6offers you his region &c%regionid% &6in the world &c%world% &6for &c%price%%currency%&6! You can accept his offer with &c/arm offer accept &6or reject it &c/arm offer reject")
    public static String INCOMING_OFFER;
    @SerialzedString(name = "SelectedPlayerIsNotOnline", message = "&4The selected player is not online")
    public static String SELECTED_PLAYER_NOT_ONLINE;
    @SerialzedString(name = "OfferTimedOut", message = "&4Offer timed out!")
    public static String OFFER_TIMED_OUT;
    @SerialzedString(name = "BadSyntax", message = "&7Bad syntax! Please use: &8%command%")
    public static String BAD_SYNTAX;
    @SerialzedString(name = "BadSyntaxSplitter", message = "\n&7or &8%command%")
    public static String BAD_SYNTAX_SPLITTER;
    @SerialzedString(name = "HelpHeadline", message = "&6=====[AdvancedRegionMarket Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String HELP_HEADLINE;
    @SerialzedStringList(name = "PresetInfoSellregion", message = {"&6=========[Region Info]=========",
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
    @SerialzedStringList(name = "PresetInfoContractregion", message = {"&6=========[Region Info]=========",
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
    @SerialzedStringList(name = "PresetInfoRentregion", message = {"&6=========[Region Info]=========",
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
    @SerialzedString(name = "NotDefined", message = "&8&onot defined")
    public static String NOT_DEFINED;
    @SerialzedString(name = "PriceCanNotBeNegative", message = "&4Price can not be negative!")
    public static String PRICE_CAN_NOT_BE_NEGATIVE;
    @SerialzedString(name = "SellBackWarning", message = "&4Sell region back to the server:\n" +
            "&4WARNING: &cThis can not be undone! \n" +
            "Your region &6%regionid% &cwill be released and all blocks on it\n" +
            "will be resetted! You and all members will loose their rights on it.\n" +
            "You will get &6%paybackmoney%%currency% &cback")
    public static String SELLBACK_WARNING;
    @SerialzedString(name = "SubregionInactivityResetError", message = "&4The selected region is a subregion. You can change the InactivityReset setting for all subregions in the config.yml!")
    public static String SUBREGION_INACTIVITYRESET_ERROR;
    @SerialzedString(name = "SubregionAutoRestoreError", message = "&4The selected region is a subregion. You can change the autoRestore setting for all subregions in the config.yml!")
    public static String SUBREGION_AUTORESTORE_ERROR;
    @SerialzedString(name = "RegionNotRestoreable", message = "&4Region not restorable!")
    public static String REGION_NOT_RESTORABLE;
    @SerialzedString(name = "RegionSelectedMultipleRegions", message = "&6There is more than one region at your position. Please select one: &4")
    public static String REGION_SELECTED_MULTIPLE_REGIONS;
    @SerialzedString(name = "SubregionRegionkindError", message = "&4The selected region is a subregion. You can edit the regionkind for all subregions in the config.yml!")
    public static String SUBREGION_REGIONKIND_ERROR;
    @SerialzedString(name = "SubRegionRegionkindOnlyForSubregions", message = "&4Subregion regionkind only for subregions!")
    public static String SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS;
    @SerialzedString(name = "SubregionTeleportLocationError", message = "&4The selected region is a subregion. Teleport location can not be changed")
    public static String SUBREGION_TELEPORT_LOCATION_ERROR;
    @SerialzedString(name = "RegionNotRegistred", message = "&4Region not registred")
    public static String REGION_NOT_REGISTRED;
    @SerialzedString(name = "FirstPositionSet", message = "&aFirst position set!")
    public static String FIRST_POSITION_SET;
    @SerialzedString(name = "SecondPositionSet", message = "&aSecond position set!")
    public static String SECOND_POSITION_SET;
    @SerialzedString(name = "MarkInOtherRegion", message = "&4Mark in other Region. Removing old mark")
    public static String MARK_IN_OTHER_REGION_REMOVING;
    @SerialzedString(name = "ParentRegionNotOwn", message = "&4You don not own the parent region!")
    public static String PARENT_REGION_NOT_OWN;
    @SerialzedString(name = "SubRegionRemoveNoPermissionBecauseSold", message = "&4You are not allowed to remove this region. Please ask an admin if you believe this is an error")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD;
    @SerialzedString(name = "SubRegionRemoveNoPermissionBecauseAvailable", message = "&4You are not allowed to remove this region, because it is sold. You may ask the owner or an admin to release it")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE;
    @SerialzedString(name = "PosCloudNotBeSetMarkOutsideRegion", message = "&4Position could not be set! Position outside region")
    public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION;
    @SerialzedString(name = "SubRegionAlreadyAtThisPosition", message = "&4Your selection would overlap with a subregion that already has been created")
    public static String ALREADY_SUBREGION_AT_THIS_POSITION;
    @SerialzedString(name = "SubRegionLimitReached", message = "&4Subregion limit reached! You are not allowed to create more than &6%subregionlimit% &4subregions")
    public static String SUBREGION_LIMIT_REACHED;
    @SerialzedString(name = "SelectionInvalid", message = "&4Selection invalid! You need to select 2 positions! (Left/Right click) Type \"&6/arm subregion tool&4\" to get the selection tool")
    public static String SELECTION_INVALID;
    @SerialzedString(name = "RegionCreatedAndSaved", message = "&aRegion created and saved!")
    public static String REGION_CREATED_AND_SAVED;
    @SerialzedString(name = "RegionNotASubregion", message = "&4Region not a subregion!")
    public static String REGION_NOT_A_SUBREGION;
    @SerialzedString(name = "RegionDeleted", message = "&aRegion deleted!")
    public static String REGION_DELETED;
    @SerialzedString(name = "DeleteRegionWarningName", message = "&4&lDelete region?")
    public static String DELETE_REGION_WARNING_NAME;
    @SerialzedString(name = "UnsellRegionButton", message = "&4Unsell region")
    public static String UNSELL_REGION_BUTTON;
    @SerialzedStringList(name = "UnsellRegionButtonLore", message = {"&4Click to unsell your subregion and",
            "&4kick the players of it"})
    public static List<String> UNSELL_REGION_BUTTON_LORE;
    @SerialzedString(name = "UnsellRegionWarningName", message = "&4&lUnsell region?")
    public static String UNSELL_REGION_WARNING_NAME;
    @SerialzedString(name = "SubregionHelpHeadline", message = "&6=====[AdvancedRegionMarket Subregion Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SUBREGION_HELP_HEADLINE;
    @SerialzedString(name = "SellregionName", message = "Sellregion")
    public static String SELLREGION_NAME;
    @SerialzedString(name = "ContractregionName", message = "Contractregion")
    public static String CONTRACTREGION_NAME;
    @SerialzedString(name = "RentregionName", message = "Rentregion")
    public static String RENTREGION_NAME;
    @SerialzedString(name = "GUISubregionsButton", message = "&6Subregions")
    public static String GUI_SUBREGION_ITEM_BUTTON;
    @SerialzedString(name = "GUISubregionListMenuName", message = "&1Subregions")
    public static String GUI_SUBREGION_LIST_MENU_NAME;
    @SerialzedString(name = "GUIHotelButton", message = "&6Hotel-function")
    public static String GUI_SUBREGION_HOTEL_BUTTON;
    @SerialzedString(name = "GUIDeleteRegionButton", message = "&4Delete region")
    public static String GUI_SUBREGION_DELETE_REGION_BUTTON;
    @SerialzedString(name = "GUITeleportToSignOrRegionButton", message = "Teleport to sign or region?")
    public static String GUI_TELEPORT_TO_SIGN_OR_REGION;
    @SerialzedString(name = "GUIRegionfinderTeleportToSignButton", message = "&6Teleport to buy sign!")
    public static String GUI_TELEPORT_TO_SIGN;
    @SerialzedString(name = "GUIRegionfinderTeleportToRegionButton", message = "&6Teleport to region!")
    public static String GUI_TELEPORT_TO_REGION;
    @SerialzedString(name = "GUINextPageButton", message = "&6Next page")
    public static String GUI_NEXT_PAGE;
    @SerialzedString(name = "GUIPrevPageButton", message = "&6Prev page")
    public static String GUI_PREV_PAGE;
    @SerialzedString(name = "Enabled", message = "&aenabled")
    public static String ENABLED;
    @SerialzedString(name = "Disabled", message = "&cdisabled")
    public static String DISABLED;
    @SerialzedString(name = "Sold", message = "&csold")
    public static String SOLD;
    @SerialzedString(name = "Available", message = "&aavailable")
    public static String AVAILABLE;
    @SerialzedString(name = "SubregionIsUserResettableError", message = "&4The selected region is a subregion. You can change the isUserResettable setting for all subregions in the config.yml!")
    public static String SUBREGION_IS_USER_RESETTABLE_ERROR;
    @SerialzedString(name = "SubregionMaxMembersError", message = "&4The selected region is a subregion. You can change the maxMember setting for all subregions in the config.yml!")
    public static String SUBREGION_MAX_MEMBERS_ERROR;
    @SerialzedStringList(name = "GUIHotelButtonLore", message = {"&6The hotel function allows you to prevent players",
            "&6from breaking blocks they do not have placed",
            "&6Status: %hotelfunctionstatus%",
            "&6Click to enable/disable"})
    public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE;
    @SerialzedStringList(name = "GUISubregionInfoSell", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_SELL;
    @SerialzedStringList(name = "GUISubregionInfoRent", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxrenttime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_RENT;
    @SerialzedStringList(name = "GUISubregionInfoContract", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT;
    @SerialzedStringList(name = "GUIRegionfinderInfoSell", message = {"&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL;
    @SerialzedStringList(name = "GUIRegionfinderInfoRent", message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxrenttime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT;
    @SerialzedStringList(name = "GUIRegionfinderInfoContract", message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT;
    @SerialzedStringList(name = "SubregionCreationCreateSignInfo", message = {"&aYour selection has been saved! You can now create a sign to sell the region.",
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
    @SerialzedStringList(name = "SubregionCreationSelectAreaInfo", message = {"&aYou got a tool in your inventory (feather) to select 2 points of your region that will mark the corners of your new subregion.",
            "&aLeft click to select pos1",
            "&aRight click to select pos2",
            "&aType \"&6/arm subregion create\" &aif you are done"})
    public static List<String> SUBREGION_TOOL_INSTRUCTION;
    @SerialzedString(name = "SubregionToolAlreadyOwned", message = "&4You already own a Subregion Tool. Please use that instead of a new one!")
    public static String SUBREGION_TOOL_ALREADY_OWNED;
    @SerialzedString(name = "AutopriceList", message = "&6=========[Autoprices]=========")
    public static String AUTOPRICE_LIST;
    @SerialzedString(name = "GUISubregionManagerNoSubregionItem", message = "&6Info")
    public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM;
    @SerialzedString(name = "SelltypeNotExist", message = "&4The selected selltype does not exist!")
    public static String SELLTYPE_NOT_EXIST;
    @SerialzedString(name = "SignLinkModeActivated", message = "&aSign-Link-Mode activated! Click into a region and afterwards click on a sign. ARM will automatically create a region (or will just add the sign if the region already exists) with the settings of your preset")
    public static String SIGN_LINK_MODE_ACTIVATED;
    @SerialzedString(name = "SignLinkModeDeactivated", message = "&aSign-Link-Mode deactivated!")
    public static String SIGN_LINK_MODE_DEACTIVATED;
    @SerialzedString(name = "SignLinkModeAlreadyDeactivated", message = "&4Sign-Link-Mode is already deactivated!")
    public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED;
    @SerialzedString(name = "SignLinkModePresetNotPriceready", message = "&cThe selected preset is not price-ready! All regions you will create now will be created with the default autoprice")
    public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY;
    @SerialzedString(name = "SignLinkModeNoPresetSelected", message = "&cYou dont have a preset loaded! Please load or create a preset first! &cYou can create a preset by using the &6/arm sellpreset/rentpreset/contractpreset &ccommands!\n" +
            "For more &cinformation about presets click here:\n" +
            "&6https://bit.ly/2HURK0v (Github Wiki)")
    public static String SIGN_LINK_MODE_NO_PRESET_SELECTED;
    @SerialzedString(name = "SignLinkModeSignBelongsToAnotherRegion", message = "&4Sign belongs to another region!")
    public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION;
    @SerialzedString(name = "SignLinkModeSignSelected", message = "&aSign selected!")
    public static String SIGN_LINK_MODE_SIGN_SELECTED;
    @SerialzedString(name = "SignLinkModeMultipleWgRegionsAtPosition", message = "&4Could not select WorldGuard-Region!" +
            "There is more than one region available! You can add unwanted regions to the ignore-list" +
            "located in the config.yml, if you want ARM to ignore that regions!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS;
    @SerialzedString(name = "SignLinkModeNoWgRegionAtPosition", message = "&4Could not select WorldGuard-Region! There is no region at your position!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION;
    @SerialzedString(name = "SignLinkModeCouldNotIdentifyWorld", message = "&4Could not identify world! Please select the WorldGuard-Region again!")
    public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD;
    @SerialzedString(name = "SignLinkModeNoSignSelected", message = "&4You have not selected a sign")
    public static String SIGN_LINK_MODE_NO_SIGN_SELECTED;
    @SerialzedString(name = "SignLinkModeNoWgRegionSelected", message = "&4You have not selected a WorldGuard-Region")
    public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED;
    @SerialzedString(name = "SignLinkModeSelectedRegion", message = "&aSelected region: %regionid%")
    public static String SIGN_LINK_MODE_REGION_SELECTED;
    @SerialzedString(name = "SchematicNotFoundErrorUser", message = "&4It seems like the schematic of your region %regionid% has not been created. Please contact an admin!")
    public static String SCHEMATIC_NOT_FOUND_ERROR_USER;
    @SerialzedString(name = "EntityLimitHelpHeadline", message = "&6=====[AdvancedRegionMarket EntityLimit Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String ENTITYLIMIT_HELP_HEADLINE;
    @SerialzedString(name = "EntityLimitGroupNotExist", message = "&4EntityLimitGroup does not exist!")
    public static String ENTITYLIMITGROUP_DOES_NOT_EXIST;
    @SerialzedString(name = "EntityLimitSet", message = "&aEntityLimit has been set!")
    public static String ENTITYLIMIT_SET;
    @SerialzedString(name = "EntityLimitRemoved", message = "&aEntityLimit has been removed!")
    public static String ENTITYLIMIT_REMOVED;
    @SerialzedString(name = "EntityTypeDoesNotExist", message = "&4The entitytype &6%entitytype% &4does not exist!")
    public static String ENTITYTYPE_DOES_NOT_EXIST;
    @SerialzedString(name = "EntityLimitGroupNotContainEntityLimit", message = "&4The selected EntityLimitGroup does not contain the selected EntityType")
    public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT;
    @SerialzedString(name = "EntityLimitTotal", message = "Total")
    public static String ENTITYLIMIT_TOTAL;
    @SerialzedString(name = "SubregionPaybackPercentageError", message = "&4The selected region is a subregion. You can edit the paybackPercentage for all subregions in the config.yml!")
    public static String SUBREGION_PAYBACKPERCENTAGE_ERROR;
    @SerialzedString(name = "EntityLimitCheckHeadline", message = "&6===[EntityLimitCheck for %regionid%]===")
    public static String ENTITYLIMIT_CHECK_HEADLINE;
    @SerialzedString(name = "EntityLimitCheckPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String ENTITYLIMIT_CHECK_PATTERN;
    @SerialzedString(name = "EntityLimitCheckExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMIT_CHECK_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupAlreadyExists", message = "&4Group already exists!")
    public static String ENTITYLIMITGROUP_ALREADY_EXISTS;
    @SerialzedString(name = "EntityLimitGroupCreated", message = "&aEntitylimitgroup has been created!")
    public static String ENTITYLIMITGROUP_CREATED;
    @SerialzedString(name = "EntityLimitGroupCanNotRemoveSystem", message = "&4You can not remove a system-EntityLimitGroup!")
    public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedString(name = "EntityLimitGroupDeleted", message = "&aEntitylimitgroup has been deleted!")
    public static String ENTITYLIMITGROUP_DELETED;
    @SerialzedString(name = "EntityLimitGroupInfoHeadline", message = "&6======[Entitylimitgroup Info]======")
    public static String ENTITYLIMITGROUP_INFO_HEADLINE;
    @SerialzedString(name = "EntityLimitGroupInfoGroupname", message = "&6Groupname: ")
    public static String ENTITYLIMITGROUP_INFO_GROUPNAME;
    @SerialzedString(name = "EntityLimitGroupInfoPattern", message = "&6%entitytype%: &r%softlimitentities% %entityextensioninfo%")
    public static String ENTITYLIMITGROUP_INFO_PATTERN;
    @SerialzedString(name = "EntityLimitInfoExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupListHeadline", message = "&6EntityLimitGroups:")
    public static String ENTITYLIMITGROUP_LIST_HEADLINE;
    @SerialzedString(name = "SubregionEntityLimitOnlyForSubregions", message = "&4SubregionEntityLimitGroup only for subregions")
    public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedString(name = "MassactionSplitter", message = "&6all regions with regionkind &a%regionkind%")
    public static String MASSACTION_SPLITTER;
    @SerialzedString(name = "SubregionEntityLimitError", message = "&4Could not change EntiyLimitGroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_ENTITYLIMITGROUP_ERROR;
    @SerialzedString(name = "SubregionFlagGroupError", message = "&4Could not change Flaggroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_FLAGGROUP_ERROR;
    @SerialzedString(name = "GUIEntityLimitItemButton", message = "&6EntityLimits")
    public static String GUI_ENTITYLIMIT_ITEM_BUTTON;
    @SerialzedStringList(name = "GUIEntityLimitItemLore", message = {"&6Click to display the entity-limits",
            "&6for this region in chat",
            "%entityinfopattern%",
            "",
            "You can expand your entity-limit with:",
            "&6/arm entitylimit buyextra %regionid% [ENTITYTYPE/total]"})
    public static List<String> GUI_ENTITYLIMIT_ITEM_LORE;
    @SerialzedString(name = "GUIEntityLimitInfoPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN;
    @SerialzedString(name = "GUIEntityLimitInfoExtensionInfo", message = "&6&oMax. &r%hardlimitentities% &6for &r%priceperextraentity%%currency%&6/entity")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupEntityLimitAlreadyUnlimited", message = "&4EntityLimit for the selected entity and region is already unlimited!")
    public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesSet", message = "&aExtra-Entities have been set!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesExpandSuccess", message = "&aYou have sucessfully expanded the entitylimit to &6%softlimitentities% &aentities! (For &6%priceperextraentity%%currency%&a)")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesHardlimitReached", message = "&4Can not buy another entity-expansion! Hardlimit has been reached!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesSetSubregionError", message = "&4Can not change entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesBuySubregionError", message = "&4Can not expand entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR;
    @SerialzedString(name = "EntityLimitGroupCouldNotspawnEntity", message = "&4Could not spawn entity on region &6%region%&4!\n" +
            "The not spawned entity would exceed the region\'s entitylimit. For more information type &6/arm entitylimit check %region%&4!\n" +
            "Everybody on region %region% received this message! If you are not a member of this region, you can ignore this message.")
    public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY;
    @SerialzedString(name = "ArmBasicCommandMessage", message = "&6AdvancedRegionMarket v%pluginversion% by Alex9849\n" +
            "&6Download: &3https://bit.ly/2CfO3An\n" +
            "&6Get a list with all commands with &3/arm help")
    public static String ARM_BASIC_COMMAND_MESSAGE;
    @SerialzedString(name = "RegionKindCreated", message = "&aRegionKind created!")
    public static String REGIONKIND_CREATED;
    @SerialzedString(name = "RegionKindAlreadyExists", message = "&4RegionKind already exists!")
    public static String REGIONKIND_ALREADY_EXISTS;
    @SerialzedString(name = "RegionKindDeleted", message = "&aRegionKind deleted!")
    public static String REGIONKIND_DELETED;
    @SerialzedString(name = "RegionKindCanNotRemoveSystem", message = "&4You can not remove a system-RegionKind!")
    public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedString(name = "RegionKindListHeadline", message = "&6Regionkinds:")
    public static String REGIONKIND_LIST_HEADLINE;
    @SerialzedString(name = "RegionKindModified", message = "&aRegionKind modified!")
    public static String REGIONKIND_MODIFIED;
    @SerialzedString(name = "MaterialNotFound", message = "&4Material not found!")
    public static String MATERIAL_NOT_FOUND;
    @SerialzedString(name = "RegionKindLoreLineNotExist", message = "&aThe selected lore-line does not exist!")
    public static String REGIONKIND_LORE_LINE_NOT_EXIST;
    @SerialzedString(name = "RegionKindInfoHeadline", message = "&6=========[Regionkind info]=========")
    public static String REGIONKIND_INFO_HEADLINE;
    @SerialzedString(name = "RegionKindInfoInternalName", message = "&6Internal name: %regionkind%")
    public static String REGIONKIND_INFO_INTERNAL_NAME;
    @SerialzedString(name = "RegionKindInfoDisplayName", message = "&6Displayname: %regionkinddisplay%")
    public static String REGIONKIND_INFO_DISPLAY_NAME;
    @SerialzedString(name = "RegionKindInfoMaterial", message = "&6Material: %regionkinditem%")
    public static String REGIONKIND_INFO_MATERIAL;
    @SerialzedString(name = "RegionKindInfoDisplayInGui", message = "&6DisplayInGui: %regionkinddisplayingui%")
    public static String REGIONKIND_INFO_DISPLAY_IN_GUI;
    @SerialzedString(name = "RegionKindInfoDisplayInLimits", message = "&6DisplayInLimits: %regionkinddisplayinlimits%")
    public static String REGIONKIND_INFO_DISPLAY_IN_LIMITS;
    @SerialzedString(name = "RegionKindInfoLore", message = "&6Lore:")
    public static String REGIONKIND_INFO_LORE;
    @SerialzedString(name = "RegionKindHelpHeadline", message = "&6=====[AdvancedRegionMarket RegionKind Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String REGIONKIND_HELP_HEADLINE;
    @SerialzedString(name = "PlayerNotFound", message = "&4Could not find selected player!")
    public static String PLAYER_NOT_FOUND;
    @SerialzedStringList(name = "RegionInfoSellregionUser", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION;
    @SerialzedStringList(name = "RegionInfoRentregionUser", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION;
    @SerialzedStringList(name = "RegionInfoContractregionUser", message = {"&6=========[Region Info]=========" +
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_CONTRACTREGION;
    @SerialzedStringList(name = "RegionInfoSellregionAdmin", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION_ADMIN;
    @SerialzedStringList(name = "RegionInfoRentregionAdmin", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION_ADMIN;
    @SerialzedStringList(name = "RegionInfoContractregionAdmin", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%'"})
    public static List<String> REGION_INFO_CONTRACTREGION_ADMIN;
    @SerialzedStringList(name = "RegionInfoSellregionSubregion", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%"})
    public static List<String> REGION_INFO_SELLREGION_SUBREGION;
    @SerialzedStringList(name = "RegionInfoRentregionSubregion", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxrenttime-writtenout%",
            "&9Remaining time: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%"})
    public static List<String> REGION_INFO_RENTREGION_SUBREGION;
    @SerialzedStringList(name = "RegionInfoContractregionSubregion", message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7(auto extend)",
            "&9Next extend in: &e%remainingtime-countdown-short%",
            "&9Owner: &e%owner%",
            "&9Members: &e%members%",
            "&9Max. number of members: &e%maxmembers%",
            "&9Regionkind: &e%regionkinddisplay% &9FlagGroup: &e%flaggroup%",
            "&9EntityLimitGroup: &e%entitylimitgroup% &9isHotel: &e%ishotel%",
            "&9UserRestorable: &e%isuserrestorable% &9InactivityResetEnabled: &e%isinactivityreset%",
            "&9Owners last login: &e%lastownerlogin%",
            "&9InactivityReset in: &e%inactivityresetin-countdown-short%",
            "&9TakeOver possible in: &e%takeoverin-countdown-short%",
            "&9AutoRestore: &e%isautorestore% &9Autoprice: &e%autoprice%",
            "&9PaybackPercentage in %: %paypackpercentage%"})
    public static List<String> REGION_INFO_CONTRACTREGION_SUBREGION;
    @SerialzedString(name = "GUIFlageditorButton", message = "&6FlagEditor")
    public static String GUI_FLAGEDITOR_BUTTON;
    @SerialzedString(name = "GUIFlageditorMenuName", message = "&1FlagEditor (%region%)")
    public static String GUI_FLAGEDITOR_MENU_NAME;
    @SerialzedString(name = "GUIFlageditorDeleteFlagButton", message = "&4Delete flag")
    public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupAllButton", message = "&9Set for everyone")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupMembersButton", message = "&9Set for members and owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupOwnersButton", message = "&9Set for owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupNonMembersButton", message = "&9Set for non members and non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupNonOwnersButton", message = "&9Set for non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStateflagAllowButton", message = "&2Allow")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStateflagDenyButton", message = "&4Deny")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetBooleanflagTrueButton", message = "&2Yes")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetBooleanflagFalseButton", message = "&4No")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStringflagSetMessageButton", message = "&2Set message")
    public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetIntegerflagSetIntegerButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetDoubleflagSetDoubleButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON;
    @SerialzedString(name = "FlageditorFlagNotActivated", message = "&4Flag not activated!")
    public static String FlAGEDITOR_FLAG_NOT_ACTIVATED;
    @SerialzedString(name = "FlageditorFlagHasBeenDeleted", message = "&2Flag has been deleted!")
    public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED;
    @SerialzedString(name = "FlageditorFlagHasBeenUpdated", message = "&2Flag has been updated!")
    public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED;
    @SerialzedString(name = "FlageditorFlagCouldNotBeUpdated", message = "Could not modify flag %flag%!")
    public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED;
    @SerialzedString(name = "FlageditorStringflagSetMessageInfo", message = "&9Please write down a message:")
    public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO;
    @SerialzedString(name = "FlageditorIntegerflagSetMessageInfo", message = "&9Please write down a number that does not have decimals:")
    public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO;
    @SerialzedString(name = "FlageditorDoubleflagSetMessageInfo", message = "&9Please write down a number:")
    public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO;
    @SerialzedString(name = "GUIFlageditorResetButton", message = "&4Reset all Flags to default settings")
    public static String GUI_FLAGEDITOR_RESET_BUTTON;
    @SerialzedString(name = "GUIFlageditorUnknownFlagSetPropertiesButton", message = "&2Set properties")
    public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON;
    @SerialzedString(name = "GUIFlageditorUnknownFlagSetPropertiesInfo", message = "&9Please write down your new flag properties: FlaggroupDoesNotExist: '&4Flaggroup does not exist!")
    public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO;
    @SerialzedString(name = "FlaggroupDoesNotExist", message = "&4Flaggroup does not exist!")
    public static String FLAGGROUP_DOES_NOT_EXIST;
    @SerialzedString(name = "SubregionFlaggroupOnlyForSubregions", message = "&4Subregion flaggroup only for subregions")
    public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedStringList(name = "GUITeleportToRegionButtonLore", message = {"Click to teleport you to",
            "your region"})
    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE;
    @SerialzedStringList(name = "GUIMakeOwnerButtonLore", message = {"Click to transfer your owner rights",
            "to the selected member.",
            "&4WARNING: &cYou will lose your owner",
            "&crights and become a member'"})
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE;
    @SerialzedStringList(name = "GUIRemoveMemberButtonLore", message = {"Click to remove the selected member",
            "from your region"})
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE;
    @SerialzedStringList(name = "GUIResetRegionButtonLore", message = {"Click to reset your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be resetted and everything on it will",
            "&cbe deleted!",
            "",
            "&cYou can only reset you region once every %userresetcooldown%",
            "&2You and all members keep their rights on the region"})
    public static List<String> GUI_RESET_REGION_BUTTON_LORE;
    @SerialzedStringList(name = "TakeOverItemLore", message = {"&aYou are a member of this region.",
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
    @SerialzedStringList(name = "GUIExtendRentRegionButtonLore", message = {"&aClick to extend your region for &6%extendtime-writtenout%",
            "&athis will cost you &6%price%%currency%&a!",
            "&aThis region will expire in &6%remainingtime-countdown-short%&a.",
            "&aYou can extend your region up to &6%maxrenttime-writtenout%&a."})
    public static List<String> GUI_EXTEND_BUTTON_LORE;
    @SerialzedStringList(name = "GUIRentRegionLore", message = {"&aExpires in &6%remainingtime-countdown-short%"})
    public static List<String> GUI_RENT_REGION_LORE;
    @SerialzedStringList(name = "GUIUserSellButtonLore", message = {"Click to sell your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be released and all blocks on it will be",
            "&cresetted! You and all members of it will loose",
            "&ctheir rights on it.",
            "&cYou will get &6%paybackmoney%%currency% &cback"})
    public static List<String> GUI_USER_SELL_BUTTON_LORE;
    @SerialzedStringList(name = "MemberlistInfoLore", message = {"&aYou can be added as a member to",
            "&athe region of someone else in order",
            "&ato build with him together",
            "&aJust ask a region owner to add you with:",
            "&6/arm addmember REGIONID USERNAME",
            "&aYou need to be online for this"})
    public static List<String> GUI_MEMBER_INFO_LORE;
    @SerialzedStringList(name = "GUIContractItemLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_ITEM_LORE;
    @SerialzedStringList(name = "GUIContractItemRegionLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_REGION_LORE;
    @SerialzedStringList(name = "OwnerMemberlistInfoLore", message = {"&aYou can add members to your region",
            "&ain order to build with them together",
            "&aYou can add members with:",
            "&6/arm addmember %regionid% USERNAME",
            "&aMembers need to be online to add them"})
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE;
    @SerialzedStringList(name = "GUISubregionManagerNoSubregionItemLore", message = {"&aYou do not have any subregions on your region.",
            "&aYou can create a new subregion, that you",
            "&acan sell to other players by typing",
            "&6/arm subregion tool &aand following displayed the steps"})
    public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE;
    @SerialzedString(name = "SellPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket SellPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SELLPRESET_HELP_HEADLINE;
    @SerialzedString(name = "ContractPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket ContractPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String CONTRACTPRESET_HELP_HEADLINE;
    @SerialzedString(name = "RentPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket RentPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String RENTPRESET_HELP_HEADLINE;
    @SerialzedString(name = "InfoDeactivated", message = "&4deactivated")
    public static String INFO_DEACTIVATED;
    @SerialzedString(name = "InfoNotSold", message = "&4Region not sold!")
    public static String INFO_REGION_NOT_SOLD;
    @SerialzedString(name = "InfoNow", message = "&2now")
    public static String INFO_NOW;
    @SerialzedString(name = "InfoNotCalculated", message = "&8Awaiting calculation...")
    public static String INFO_NOT_CALCULATED;
    @SerialzedString(name = "CouldNotFindOrLoadSchematicLog", message = "&4Could not find or load schematic for region %region% in world %world%! You can regenerate it with /arm updateschematic %region%")
    public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG;
    @SerialzedString(name = "RegionSoldBackSuccessfully", message = "&2Your region &6%regionid% &2has successfully been sold back to the server! " +
            "&6%paybackmoney%%currency% &2have been added to your account!")
    public static String REGION_SOLD_BACK_SUCCESSFULLY;
    @SerialzedString(name = "RegionModifiedBoolean", message = "&6%option% %state% &6for &a%selectedregions%&6!")
    public static String REGION_MODIFIED_BOOLEAN;
    @SerialzedString(name = "RegionModified", message = "&6%option% &6modified for %selectedregions%&6!")
    public static String REGION_MODIFIED;
    @SerialzedString(name = "UpdatingSchematic", message = "&8Updating schematic...")
    public static String UPDATING_SCHEMATIC;
    @SerialzedString(name = "SchematicUpdated", message = "&aSchematic updated!")
    public static String SCHEMATIC_UPDATED;
    @SerialzedString(name = "ContractRegionTerminated", message = "&6Your contractregion &a%region% &6has successfully been " +
            "&4terminated&6! It will be resetted in &a%remainingtime-countdown-short% &6except it gets reactivated!")
    public static String CONTRACTREGION_TERMINATED;
    @SerialzedString(name = "ContractRegionReactivated", message = "&6Your contractregion &a%region% &6has successfully " +
            "been &areactivated&6! It will automatically be extended in &a%remainingtime-countdown-short% &6if " +
            "you can pay for the rent!")
    public static String CONTRACTREGION_REACTIVATED;
    @SerialzedString(name = "RegionInfoFeatureDisabled", message = "&4Feature disbaled!")
    public static String REGION_INFO_FEATURE_DISABLED;
    @SerialzedString(name = "FlagGroupFeatureDisabled", message = "&4FlagGroups are currently disabled! You can activate them in the config.yml!")
    public static String FLAGGROUP_FEATURE_DISABLED;
    @SerialzedString(name = "BackupCreated", message = "&aBackup created!")
    public static String BACKUP_CREATED;
    @SerialzedString(name = "BackupRestored", message = "&aBackup restored!")
    public static String BACKUP_RESTORED;
    @SerialzedString(name = "CouldNotLoadBackup", message = "&4Could not load backup! Maybe it does not exist or the file is corrupted!")
    public static String COULD_NOT_LOAD_BACKUP;
    @SerialzedString(name = "BackupListHeader", message = "&6=======[Backups of region %regionid%]=======")
    public static String BACKUP_LIST_HEADER;

    public static void reload(File savePath, MessageLocale locale) {
        YamlConfiguration config = updateAndWriteConfig(locale, savePath);
        load(config);
    }

    private static YamlConfiguration updateAndWriteConfig(MessageLocale locale, File savePath) {
        YamlConfiguration config;
        if(savePath.exists()) {
            config = YamlConfiguration.loadConfiguration(savePath);
        } else {
            config = new YamlConfiguration();
        }
        int configVersion = config.getInt("FileVersion");
        int newConfigVersion = configVersion;
        ConfigurationSection localeconfigMessages = null;
        int localeFileVersion = 0;
        if(MessageLocale.EN != locale) {
            YamlConfiguration localeConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(AdvancedRegionMarket.getInstance().getResource("messages_" + locale.code() + ".yml")));
            localeFileVersion = localeConfig.getInt("FileVersion");
            localeconfigMessages = localeConfig.getConfigurationSection("Messages");
        }

        ConfigurationSection configMessages = config.getConfigurationSection("Messages");
        if (configMessages == null) {
            configMessages = new YamlConfiguration();
        }

        boolean fileUpdated = false;
        for(Field field : Messages.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(SerialzedString.class) && field.isAnnotationPresent(SerialzedStringList.class)) {
                continue;
            }
            int requestedVersion = getRequestedVersion(field);
            String serializedKey = getSerializedKey(field);
            if((configVersion >= requestedVersion) && configMessages.get(serializedKey) != null) {
                continue;
            }
            Object replaceMessage = getMessage(field);
            if(localeconfigMessages != null) {
                Object localeConfigMessage = localeconfigMessages.get(serializedKey);
                if(localeConfigMessage != null && localeFileVersion >= requestedVersion) {
                    replaceMessage = localeConfigMessage;
                }
            }

            configMessages.set(serializedKey, replaceMessage);
            newConfigVersion = Math.max(newConfigVersion, requestedVersion);
            fileUpdated = true;
        }

        if(fileUpdated) {
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

        Map<String, Field> keyToField = new HashMap<>();
        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(SerialzedString.class) || field.isAnnotationPresent(SerialzedStringList.class)) {
                field.setAccessible(true);
                Object parsedOption = cs.get(getSerializedKey(field), field.getType());
                if (parsedOption instanceof String) {
                    parsedOption = ChatColor.translateAlternateColorCodes('&', (String) parsedOption);
                } else if (parsedOption.getClass().isArray()) {
                    List<String> parsedOptionList = new ArrayList<>();
                    for(String parsedString : (String[]) parsedOption) {
                        parsedOptionList.add(ChatColor.translateAlternateColorCodes('&', parsedString));
                    }
                    parsedOption = parsedOptionList;
                }
                if(field.getType().isAssignableFrom(parsedOption.getClass())) {
                    try {
                        field.set(Messages.class, parsedOption);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                field.setAccessible(false);
            }
        }
    }

    private static Object getMessage(Field field) {
        if(field.isAnnotationPresent(SerialzedString.class)) {
            return field.getAnnotation(SerialzedString.class).message();
        } else if (field.isAnnotationPresent(SerialzedStringList.class)) {
            return field.getAnnotation(SerialzedStringList.class).message();
        }
        return null;
    }

    private static String getSerializedKey(Field field) {
        if(field.isAnnotationPresent(SerialzedString.class)) {
            String annotationValue = field.getAnnotation(SerialzedString.class).name();
            if(annotationValue.isEmpty()) {
                return field.getName();
            }
        } else if (field.isAnnotationPresent(SerialzedStringList.class)) {
            String annotationValue = field.getAnnotation(SerialzedStringList.class).name();
            if(annotationValue.isEmpty()) {
                return field.getName();
            }
        }
        return null;
    }

    private static int getRequestedVersion(Field field) {
        if(field.isAnnotationPresent(SerialzedString.class)) {
            return field.getAnnotation(SerialzedString.class).version();
        } else if (field.isAnnotationPresent(SerialzedStringList.class)) {
            return field.getAnnotation(SerialzedStringList.class).version();
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

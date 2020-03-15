package net.alex9849.arm;

import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Messages {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SerialzedName {
        public String name();
    }

    @SerialzedName(name = "Prefix") public static String PREFIX;
    @SerialzedName(name = "Buymessage") public static String REGION_BUYMESSAGE;
    @SerialzedName(name = "NotEnoughtMoney") public static String NOT_ENOUGHT_MONEY;
    @SerialzedName(name = "RegionAlreadySold") public static String REGION_ALREADY_SOLD;
    @SerialzedName(name = "NoPermission") public static String NO_PERMISSION;
    @SerialzedName(name = "WorldDoesNotExist") public static String WORLD_DOES_NOT_EXIST;
    @SerialzedName(name = "RegionDoesNotExist") public static String REGION_DOES_NOT_EXIST;
    @SerialzedName(name = "RegionAddedToARM") public static String REGION_ADDED_TO_ARM;
    @SerialzedName(name = "SignAddedToRegion") public static String SIGN_ADDED_TO_REGION;
    @SerialzedName(name = "SignRemovedFromRegion") public static String SIGN_REMOVED_FROM_REGION;
    @SerialzedName(name = "UseANumberAsPrice") public static String PLEASE_USE_A_NUMBER_AS_PRICE;
    @SerialzedName(name = "RegionRemovedFromARM") public static String REGION_REMOVED_FROM_ARM;
    @SerialzedName(name = "SellSign1") public static String SELL_SIGN1;
    @SerialzedName(name = "SellSign2") public static String SELL_SIGN2;
    @SerialzedName(name = "SellSign3") public static String SELL_SIGN3;
    @SerialzedName(name = "SellSign4") public static String SELL_SIGN4;
    @SerialzedName(name = "SoldSign1") public static String SOLD_SIGN1;
    @SerialzedName(name = "SoldSign2") public static String SOLD_SIGN2;
    @SerialzedName(name = "SoldSign3") public static String SOLD_SIGN3;
    @SerialzedName(name = "SoldSign4") public static String SOLD_SIGN4;
    @SerialzedName(name = "Currency") public static String CURRENCY;
    @SerialzedName(name = "RegionKindSet") public static String REGION_KIND_SET;
    @SerialzedName(name = "CommandOnlyIngame") public static String COMMAND_ONLY_INGAME;
    @SerialzedName(name = "RegionInfoPrice") public static String REGION_INFO_PRICE;
    @SerialzedName(name = "RegionInfoType") public static String REGION_INFO_TYPE;
    @SerialzedName(name = "RegionInfoInactivityReset") public static String REGION_INFO_INACTIVITYRESET;
    @SerialzedName(name = "RegionInfoMaxRentTime") public static String REGION_INFO_MAX_RENT_TIME;
    @SerialzedName(name = "RegionInfoExtendPerClick") public static String REGION_INFO_EXTEND_PER_CLICK;
    @SerialzedName(name = "isHotel") public static String REGION_INFO_HOTEL;
    @SerialzedName(name = "RegionInfoAllowedSubregions") public static String REGION_INFO_ALLOWED_SUBREGIONS;
    @SerialzedName(name = "RegionInfoMaxMembers") public static String REGION_INFO_MAX_MEMBERS;
    @SerialzedName(name = "RegionInfoExpired") public static String REGION_INFO_EXPIRED;
    @SerialzedName(name = "GUIMainMenuName") public static String GUI_MAIN_MENU_NAME;
    @SerialzedName(name = "GUIGoBack") public static String GUI_GO_BACK;
    @SerialzedName(name = "GUIMyOwnRegions") public static String GUI_MY_OWN_REGIONS;
    @SerialzedName(name = "GUIMemberRegionsMenuName") public static String GUI_MEMBER_REGIONS_MENU_NAME;
    @SerialzedName(name = "GUIMyMemberRegions") public static String GUI_MY_MEMBER_REGIONS;
    @SerialzedName(name = "GUISearchFreeRegion") public static String GUI_SEARCH_FREE_REGION;
    @SerialzedName(name = "GUIOwnRegionsMenuName") public static String GUI_OWN_REGIONS_MENU_NAME;
    @SerialzedName(name = "GUIMembersButton") public static String GUI_MEMBERS_BUTTON;
    @SerialzedName(name = "GUIShowInfosButton") public static String GUI_SHOW_INFOS_BUTTON;
    @SerialzedName(name = "GUITeleportToRegionButton") public static String GUI_TELEPORT_TO_REGION_BUTTON;
    @SerialzedName(name = "GUIRegionFinderMenuName") public static String GUI_REGION_FINDER_MENU_NAME;
    @SerialzedName(name = "GUIMemberListMenuName") public static String GUI_MEMBER_LIST_MENU_NAME;
    @SerialzedName(name = "GUIMakeOwnerButton") public static String GUI_MAKE_OWNER_BUTTON;
    @SerialzedName(name = "GUIRemoveMemberButton") public static String GUI_REMOVE_MEMBER_BUTTON;
    @SerialzedName(name = "GUIMakeOwnerWarningName") public static String GUI_MAKE_OWNER_WARNING_NAME;
    @SerialzedName(name = "GUIWarningYes") public static String GUI_YES;
    @SerialzedName(name = "GUIWarningNo") public static String GUI_NO;
    @SerialzedName(name = "RegionTeleportMessage") public static String REGION_TELEPORT_MESSAGE;
    @SerialzedName(name = "NoPermissionsToBuyThisKindOfRegion") public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION;
    @SerialzedName(name = "NoFreeRegionWithThisKind") public static String NO_FREE_REGION_WITH_THIS_KIND;
    @SerialzedName(name = "RegionkindDoesNotExist") public static String REGIONKIND_DOES_NOT_EXIST;
    @SerialzedName(name = "RegionNowAviable") public static String REGION_NOW_AVIABLE;
    @SerialzedName(name = "NoRegionAtPlayersPosition") public static String NO_REGION_AT_PLAYERS_POSITION;
    @SerialzedName(name = "RegionAddMemberNotOnline") public static String REGION_ADD_MEMBER_NOT_ONLINE;
    @SerialzedName(name = "RegionAddMemberAdded") public static String REGION_ADD_MEMBER_ADDED;
    @SerialzedName(name = "RegionRemoveMemberNotAMember") public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER;
    @SerialzedName(name = "RegionRemoveMemberRemoved") public static String REGION_REMOVE_MEMBER_REMOVED;
    @SerialzedName(name = "GUIResetRegionButton") public static String GUI_RESET_REGION_BUTTON;
    @SerialzedName(name = "GUIResetRegionWarningName") public static String GUI_RESET_REGION_WARNING_NAME;
    @SerialzedName(name = "ResetComplete") public static String RESET_COMPLETE;
    @SerialzedName(name = "ResetRegionCooldownError") public static String RESET_REGION_COOLDOWN_ERROR;
    @SerialzedName(name = "GUIRegionTakeOverMenuName") public static String GUI_TAKEOVER_MENU_NAME;
    @SerialzedName(name = "RegionTransferCompleteMessage") public static String REGION_TRANSFER_COMPLETE_MESSAGE;
    @SerialzedName(name = "GUICloseWindow") public static String GUI_CLOSE;
    @SerialzedName(name = "RentSign1") public static String RENT_SIGN1;
    @SerialzedName(name = "RentSign2") public static String RENT_SIGN2;
    @SerialzedName(name = "RentSign3") public static String RENT_SIGN3;
    @SerialzedName(name = "RentSign4") public static String RENT_SIGN4;
    @SerialzedName(name = "RentedSign1") public static String RENTED_SIGN1;
    @SerialzedName(name = "RentedSign2") public static String RENTED_SIGN2;
    @SerialzedName(name = "RentedSign3") public static String RENTED_SIGN3;
    @SerialzedName(name = "RentedSign4") public static String RENTED_SIGN4;
    @SerialzedName(name = "RentExtendMessage") public static String RENT_EXTEND_MESSAGE;
    @SerialzedName(name = "RentExtendMaxRentTimeExceeded") public static String RENT_EXTEND_MAX_RENT_TIME_EXCEEDED;
    @SerialzedName(name = "GUIExtendRentRegionButton") public static String GUI_EXTEND_BUTTON;
    @SerialzedName(name = "Complete") public static String COMPLETE;
    @SerialzedName(name = "RegionBuyOutOfLimit") public static String REGION_BUY_OUT_OF_LIMIT;
    @SerialzedName(name = "RegionErrorCanNotBuildHere") public static String REGION_ERROR_CAN_NOT_BUILD_HERE;
    @SerialzedName(name = "Unlimited") public static String UNLIMITED;
    @SerialzedName(name = "GUIUserSellButton") public static String GUI_USER_SELL_BUTTON;
    @SerialzedName(name = "GUIUserSellWarning") public static String GUI_USER_SELL_WARNING;
    @SerialzedName(name = "LimitInfoTop") public static String LIMIT_INFO_TOP;
    @SerialzedName(name = "LimitInfo") public static String LIMIT_INFO;
    @SerialzedName(name = "GUILimitButton") public static String GUI_MY_LIMITS_BUTTON;
    @SerialzedName(name = "MemberlistInfo") public static String GUI_MEMBER_INFO_ITEM;
    @SerialzedName(name = "AddMemberMaxMembersExceeded") public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED;
    @SerialzedName(name = "RegionIsNotARentregion") public static String REGION_IS_NOT_A_RENTREGION;
    @SerialzedName(name = "RegionNotOwn") public static String REGION_NOT_OWN;
    @SerialzedName(name = "RegionNotSold") public static String REGION_NOT_SOLD;
    @SerialzedName(name = "PresetRemoved") public static String PRESET_REMOVED;
    @SerialzedName(name = "PresetSet") public static String PRESET_SET;
    @SerialzedName(name = "RegionInfoAutoRestore") public static String REGION_INFO_AUTORESTORE;
    @SerialzedName(name = "PresetSaved") public static String PRESET_SAVED;
    @SerialzedName(name = "PresetAlreadyExists") public static String PRESET_ALREADY_EXISTS;
    @SerialzedName(name = "PresetPlayerDontHasPreset") public static String PRESET_PLAYER_DONT_HAS_PRESET;
    @SerialzedName(name = "PresetDeleted") public static String PRESET_DELETED;
    @SerialzedName(name = "PresetNotFound") public static String PRESET_NOT_FOUND;
    @SerialzedName(name = "PresetLoaded") public static String PRESET_LOADED;
    @SerialzedName(name = "LimitInfoTotal") public static String LIMIT_INFO_TOTAL;
    @SerialzedName(name = "GUIRegionItemName") public static String GUI_REGION_ITEM_NAME;
    @SerialzedName(name = "GUIRegionFinderRegionKindName") public static String GUI_REGIONFINDER_REGIONKIND_NAME;
    @SerialzedName(name = "RentRegionExpirationWarning") public static String RENTREGION_EXPIRATION_WARNING;
    @SerialzedName(name = "ContractSign1") public static String CONTRACT_SIGN1;
    @SerialzedName(name = "ContractSign2") public static String CONTRACT_SIGN2;
    @SerialzedName(name = "ContractSign3") public static String CONTRACT_SIGN3;
    @SerialzedName(name = "ContractSign4") public static String CONTRACT_SIGN4;
    @SerialzedName(name = "ContractSoldSign1") public static String CONTRACT_SOLD_SIGN1;
    @SerialzedName(name = "ContractSoldSign2") public static String CONTRACT_SOLD_SIGN2;
    @SerialzedName(name = "ContractSoldSign3") public static String CONTRACT_SOLD_SIGN3;
    @SerialzedName(name = "ContractSoldSign4") public static String CONTRACT_SOLD_SIGN4;
    @SerialzedName(name = "ContractRegionExtended") public static String CONTRACT_REGION_EXTENDED;
    @SerialzedName(name = "GUIContractItem") public static String GUI_CONTRACT_ITEM;
    @SerialzedName(name = "RegionInfoAutoExtendTime") public static String REGION_INFO_AUTO_EXTEND_TIME;
    @SerialzedName(name = "RegionInfoAutoprice") public static String REGION_INFO_AUTOPRICE;
    @SerialzedName(name = "ContractRegionStatusActive") public static String CONTRACT_REGION_STATUS_ACTIVE;
    @SerialzedName(name = "ContractRegionStatusTerminated") public static String CONTRACT_REGION_STATUS_TERMINATED;
    @SerialzedName(name = "RegionIsNotAContractRegion") public static String REGION_IS_NOT_A_CONTRACT_REGION;
    @SerialzedName(name = "OwnerMemberlistInfo") public static String GUI_OWNER_MEMBER_INFO_ITEM;
    @SerialzedName(name = "RegiontransferMemberNotOnline") public static String REGION_TRANSFER_MEMBER_NOT_ONLINE;
    @SerialzedName(name = "RegiontransferLimitError") public static String REGION_TRANSFER_LIMIT_ERROR;
    @SerialzedName(name = "SecondsSingular") public static String TIME_SECONDS_SINGULAR;
    @SerialzedName(name = "MinutesSingular") public static String TIME_MINUTES_SINGULAR;
    @SerialzedName(name = "HoursSingular") public static String TIME_HOURS_SINGULAR;
    @SerialzedName(name = "DaysSingular") public static String TIME_DAYS_SINGULAR;
    @SerialzedName(name = "SecondsPlural") public static String TIME_SECONDS_PLURAL;
    @SerialzedName(name = "MinutesPlural") public static String TIME_MINUTES_PLURAL;
    @SerialzedName(name = "HoursPlural") public static String TIME_HOURS_PLURAL;
    @SerialzedName(name = "DaysPlural") public static String TIME_DAYS_PLURAL;
    @SerialzedName(name = "SecondsShort") public static String TIME_SECONDS_SHORT;
    @SerialzedName(name = "MinutesShort") public static String TIME_MINUTES_SHORT;
    @SerialzedName(name = "HoursShort") public static String TIME_HOURS_SHORT;
    @SerialzedName(name = "DaysShort") public static String TIME_DAYS_SHORT;
    @SerialzedName(name = "TimeUnitSplitter") public static String TIME_UNIT_SPLITTER;
    @SerialzedName(name = "TimeUnitSplitterShort") public static String TIME_UNIT_SPLITTER_SHORT;
    @SerialzedName(name = "UserNotAMemberOrOwner") public static String NOT_A_MEMBER_OR_OWNER;
    @SerialzedName(name = "RegionInfoYes") public static String YES;
    @SerialzedName(name = "RegionInfoNo") public static String NO;
    @SerialzedName(name = "RegionStats") public static String REGION_STATS;
    @SerialzedName(name = "RegionStatsPattern") public static String REGION_STATS_PATTERN;
    @SerialzedName(name = "RentRegion") public static String RENT_REGION;
    @SerialzedName(name = "SellRegion") public static String SELL_REGION;
    @SerialzedName(name = "ContractRegion") public static String CONTRACT_REGION;
    @SerialzedName(name = "TeleporterNoSaveLocation") public static String TELEPORTER_NO_SAVE_LOCATION_FOUND;
    @SerialzedName(name = "TeleporterDontMove") public static String TELEPORTER_DONT_MOVE;
    @SerialzedName(name = "TeleporterTeleportationAborded") public static String TELEPORTER_TELEPORTATION_ABORDED;
    @SerialzedName(name = "OfferSent") public static String OFFER_SENT;
    @SerialzedName(name = "OfferAcceptedSeller") public static String OFFER_ACCEPTED_SELLER;
    @SerialzedName(name = "OfferAcceptedBuyer") public static String OFFER_ACCEPTED_BUYER;
    @SerialzedName(name = "NoOfferToAnswer") public static String NO_OFFER_TO_ANSWER;
    @SerialzedName(name = "OfferRejected") public static String OFFER_REJECTED;
    @SerialzedName(name = "OfferHasBeenRejected") public static String OFFER_HAS_BEEN_REJECTED;
    @SerialzedName(name = "NoOfferToReject") public static String NO_OFFER_TO_REJECT;
    @SerialzedName(name = "OfferCancelled") public static String OFFER_CANCELED;
    @SerialzedName(name = "OfferHasBeenCancelled") public static String OFFER_HAS_BEEN_CANCELLED;
    @SerialzedName(name = "NoOfferToCancel") public static String NO_OFFER_TO_CANCEL;
    @SerialzedName(name = "BuyerAlreadyGotAnOffer") public static String BUYER_ALREADY_GOT_AN_OFFER;
    @SerialzedName(name = "SellerAlreadyCreatedAnOffer") public static String SELLER_ALREADY_CREATED_AN_OFFER;
    @SerialzedName(name = "SellerDoesNotLongerOwnRegion") public static String SELLER_DOES_NOT_LONGER_OWN_REGION;
    @SerialzedName(name = "IncommingOffer") public static String INCOMING_OFFER;
    @SerialzedName(name = "SelectedPlayerIsNotOnline") public static String SELECTED_PLAYER_NOT_ONLINE;
    @SerialzedName(name = "OfferTimedOut") public static String OFFER_TIMED_OUT;
    @SerialzedName(name = "BadSyntax") public static String BAD_SYNTAX;
    @SerialzedName(name = "BadSyntaxSplitter") public static String BAD_SYNTAX_SPLITTER;
    @SerialzedName(name = "HelpHeadline") public static String HELP_HEADLINE;
    @SerialzedName(name = "PresetSetupCommands") public static String PRESET_SETUP_COMMANDS;
    @SerialzedName(name = "PresetInfoSellregion") public static List<String> PRESET_INFO_SELLREGION;
    @SerialzedName(name = "PresetInfoContractregion") public static List<String> PRESET_INFO_CONTRACTREGION;
    @SerialzedName(name = "PresetInfoRentregion") public static List<String> PRESET_INFO_RENTREGION;
    @SerialzedName(name = "NotDefined") public static String NOT_DEFINED;
    @SerialzedName(name = "PriceCanNotBeNegative") public static String PRICE_CAN_NOT_BE_NEGATIVE;
    @SerialzedName(name = "SellBackWarning") public static String SELLBACK_WARNING;
    @SerialzedName(name = "SubregionInactivityResetError") public static String SUBREGION_INACTIVITYRESET_ERROR;
    @SerialzedName(name = "SubregionAutoRestoreError") public static String SUBREGION_AUTORESTORE_ERROR;
    @SerialzedName(name = "RegionNotRestoreable") public static String REGION_NOT_RESTORABLE;
    @SerialzedName(name = "RegionSelectedMultipleRegions") public static String REGION_SELECTED_MULTIPLE_REGIONS;
    @SerialzedName(name = "SubregionRegionkindError") public static String SUBREGION_REGIONKIND_ERROR;
    @SerialzedName(name = "SubRegionRegionkindOnlyForSubregions") public static String SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS;
    @SerialzedName(name = "SubregionTeleportLocationError") public static String SUBREGION_TELEPORT_LOCATION_ERROR;
    @SerialzedName(name = "RegionNotRegistred") public static String REGION_NOT_REGISTRED;
    @SerialzedName(name = "FirstPositionSet") public static String FIRST_POSITION_SET;
    @SerialzedName(name = "SecondPositionSet") public static String SECOND_POSITION_SET;
    @SerialzedName(name = "MarkInOtherRegion") public static String MARK_IN_OTHER_REGION_REMOVING;
    @SerialzedName(name = "ParentRegionNotOwn") public static String PARENT_REGION_NOT_OWN;
    @SerialzedName(name = "SubRegionRemoveNoPermissionBecauseSold") public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD;
    @SerialzedName(name = "SubRegionRemoveNoPermissionBecauseAvailable") public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE;
    @SerialzedName(name = "PosCloudNotBeSetMarkOutsideRegion") public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION;
    @SerialzedName(name = "SubRegionAlreadyAtThisPosition") public static String ALREADY_SUBREGION_AT_THIS_POSITION;
    @SerialzedName(name = "SubRegionLimitReached") public static String SUBREGION_LIMIT_REACHED;
    @SerialzedName(name = "SelectionInvalid") public static String SELECTION_INVALID;
    @SerialzedName(name = "RegionCreatedAndSaved") public static String REGION_CREATED_AND_SAVED;
    @SerialzedName(name = "RegionNotASubregion") public static String REGION_NOT_A_SUBREGION;
    @SerialzedName(name = "RegionDeleted") public static String REGION_DELETED;
    @SerialzedName(name = "DeleteRegionWarningName") public static String DELETE_REGION_WARNING_NAME;
    @SerialzedName(name = "UnsellRegionButton") public static String UNSELL_REGION_BUTTON;
    @SerialzedName(name = "UnsellRegionButtonLore") public static List<String> UNSELL_REGION_BUTTON_LORE;
    @SerialzedName(name = "UnsellRegionWarningName") public static String UNSELL_REGION_WARNING_NAME;
    @SerialzedName(name = "SubregionHelpHeadline") public static String SUBREGION_HELP_HEADLINE;
    @SerialzedName(name = "SellregionName") public static String SELLREGION_NAME;
    @SerialzedName(name = "ContractregionName") public static String CONTRACTREGION_NAME;
    @SerialzedName(name = "RentregionName") public static String RENTREGION_NAME;
    @SerialzedName(name = "GUISubregionsButton") public static String GUI_SUBREGION_ITEM_BUTTON;
    @SerialzedName(name = "GUISubregionListMenuName") public static String GUI_SUBREGION_LIST_MENU_NAME;
    @SerialzedName(name = "GUIHotelButton") public static String GUI_SUBREGION_HOTEL_BUTTON;
    @SerialzedName(name = "GUIDeleteRegionButton") public static String GUI_SUBREGION_DELETE_REGION_BUTTON;
    @SerialzedName(name = "GUITeleportToSignOrRegionButton") public static String GUI_TELEPORT_TO_SIGN_OR_REGION;
    @SerialzedName(name = "GUIRegionfinderTeleportToSignButton") public static String GUI_TELEPORT_TO_SIGN;
    @SerialzedName(name = "GUIRegionfinderTeleportToRegionButton") public static String GUI_TELEPORT_TO_REGION;
    @SerialzedName(name = "GUINextPageButton") public static String GUI_NEXT_PAGE;
    @SerialzedName(name = "GUIPrevPageButton") public static String GUI_PREV_PAGE;
    @SerialzedName(name = "Enabled") public static String ENABLED;
    @SerialzedName(name = "Disabled") public static String DISABLED;
    @SerialzedName(name = "Sold") public static String SOLD;
    @SerialzedName(name = "Available") public static String AVAILABLE;
    @SerialzedName(name = "RegionInfoIsUserRestorable") public static String REGION_INFO_IS_USER_RESTORABLE;
    @SerialzedName(name = "SubregionIsUserResettableError") public static String SUBREGION_IS_USER_RESETTABLE_ERROR;
    @SerialzedName(name = "SubregionMaxMembersError") public static String SUBREGION_MAX_MEMBERS_ERROR;
    @SerialzedName(name = "GUIHotelButtonLore") public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE;
    @SerialzedName(name = "GUISubregionInfoSell") public static List<String> GUI_SUBREGION_REGION_INFO_SELL;
    @SerialzedName(name = "GUISubregionInfoRent") public static List<String> GUI_SUBREGION_REGION_INFO_RENT;
    @SerialzedName(name = "GUISubregionInfoContract") public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT;
    @SerialzedName(name = "GUIRegionfinderInfoSell") public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL;
    @SerialzedName(name = "GUIRegionfinderInfoRent") public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT;
    @SerialzedName(name = "GUIRegionfinderInfoContract") public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT;
    @SerialzedName(name = "SubregionCreationCreateSignInfo") public static List<String> SELECTION_SAVED_CREATE_SIGN;
    @SerialzedName(name = "SubregionCreationSelectAreaInfo") public static List<String> SUBREGION_TOOL_INSTRUCTION;
    @SerialzedName(name = "SubregionToolAlreadyOwned") public static String SUBREGION_TOOL_ALREADY_OWNED;
    @SerialzedName(name = "AutopriceList") public static String AUTOPRICE_LIST;
    @SerialzedName(name = "GUISubregionManagerNoSubregionItem") public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM;
    @SerialzedName(name = "SelltypeNotExist") public static String SELLTYPE_NOT_EXIST;
    @SerialzedName(name = "SignLinkModeActivated") public static String SIGN_LINK_MODE_ACTIVATED;
    @SerialzedName(name = "SignLinkModeDeactivated") public static String SIGN_LINK_MODE_DEACTIVATED;
    @SerialzedName(name = "SignLinkModeAlreadyDeactivated") public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED;
    @SerialzedName(name = "SignLinkModePresetNotPriceready") public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY;
    @SerialzedName(name = "SignLinkModeNoPresetSelected") public static String SIGN_LINK_MODE_NO_PRESET_SELECTED;
    @SerialzedName(name = "SignLinkModeSignBelongsToAnotherRegion") public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION;
    @SerialzedName(name = "SignLinkModeSignSelected") public static String SIGN_LINK_MODE_SIGN_SELECTED;
    @SerialzedName(name = "SignLinkModeMultipleWgRegionsAtPosition") public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS;
    @SerialzedName(name = "SignLinkModeNoWgRegionAtPosition") public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION;
    @SerialzedName(name = "SignLinkModeCouldNotIdentifyWorld") public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD;
    @SerialzedName(name = "SignLinkModeNoSignSelected") public static String SIGN_LINK_MODE_NO_SIGN_SELECTED;
    @SerialzedName(name = "SignLinkModeNoWgRegionSelected") public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED;
    @SerialzedName(name = "SignLinkModeSelectedRegion") public static String SIGN_LINK_MODE_REGION_SELECTED;
    @SerialzedName(name = "SchematicNotFoundErrorUser") public static String SCHEMATIC_NOT_FOUND_ERROR_USER;
    @SerialzedName(name = "SchematicNotFoundErrorAdmin") public static String SCHEMATIC_NOT_FOUND_ERROR_ADMIN;
    @SerialzedName(name = "EntityLimitHelpHeadline") public static String ENTITYLIMIT_HELP_HEADLINE;
    @SerialzedName(name = "EntityLimitGroupNotExist") public static String ENTITYLIMITGROUP_DOES_NOT_EXIST;
    @SerialzedName(name = "EntityLimitSet") public static String ENTITYLIMIT_SET;
    @SerialzedName(name = "EntityLimitRemoved") public static String ENTITYLIMIT_REMOVED;
    @SerialzedName(name = "EntityTypeDoesNotExist") public static String ENTITYTYPE_DOES_NOT_EXIST;
    @SerialzedName(name = "EntityLimitGroupNotContainEntityLimit") public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT;
    @SerialzedName(name = "EntityLimitTotal") public static String ENTITYLIMIT_TOTAL;
    @SerialzedName(name = "SubregionPaybackPercentageError") public static String SUBREGION_PAYBACKPERCENTAGE_ERROR;
    @SerialzedName(name = "EntityLimitCheckHeadline") public static String ENTITYLIMIT_CHECK_HEADLINE;
    @SerialzedName(name = "EntityLimitCheckPattern") public static String ENTITYLIMIT_CHECK_PATTERN;
    @SerialzedName(name = "EntityLimitCheckExtensionInfo") public static String ENTITYLIMIT_CHECK_EXTENSION_INFO;
    @SerialzedName(name = "EntityLimitGroupAlreadyExists") public static String ENTITYLIMITGROUP_ALREADY_EXISTS;
    @SerialzedName(name = "EntityLimitGroupCreated") public static String ENTITYLIMITGROUP_CREATED;
    @SerialzedName(name = "EntityLimitGroupCanNotRemoveSystem") public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedName(name = "EntityLimitGroupDeleted") public static String ENTITYLIMITGROUP_DELETED;
    @SerialzedName(name = "EntityLimitGroupInfoHeadline") public static String ENTITYLIMITGROUP_INFO_HEADLINE;
    @SerialzedName(name = "EntityLimitGroupInfoGroupname") public static String ENTITYLIMITGROUP_INFO_GROUPNAME;
    @SerialzedName(name = "EntityLimitGroupInfoPattern") public static String ENTITYLIMITGROUP_INFO_PATTERN;
    @SerialzedName(name = "EntityLimitInfoExtensionInfo") public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO;
    @SerialzedName(name = "EntityLimitGroupListHeadline") public static String ENTITYLIMITGROUP_LIST_HEADLINE;
    @SerialzedName(name = "RegionInfoEntityLimit") public static String REGION_INFO_ENTITYLIMITGROUP;
    @SerialzedName(name = "SubregionEntityLimitOnlyForSubregions") public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedName(name = "MassactionSplitter") public static String MASSACTION_SPLITTER;
    @SerialzedName(name = "SubregionEntityLimitError") public static String SUBREGION_ENTITYLIMITGROUP_ERROR;
    @SerialzedName(name = "SubregionFlagGroupError") public static String SUBREGION_FLAGGROUP_ERROR;
    @SerialzedName(name = "GUIEntityLimitItemButton") public static String GUI_ENTITYLIMIT_ITEM_BUTTON;
    @SerialzedName(name = "GUIEntityLimitItemLore") public static List<String> GUI_ENTITYLIMIT_ITEM_LORE;
    @SerialzedName(name = "GUIEntityLimitInfoPattern") public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN;
    @SerialzedName(name = "GUIEntityLimitInfoExtensionInfo") public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO;
    @SerialzedName(name = "EntityLimitGroupEntityLimitAlreadyUnlimited") public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED;
    @SerialzedName(name = "EntityLimitGroupExtraEntitiesSet") public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET;
    @SerialzedName(name = "EntityLimitGroupExtraEntitiesExpandSuccess") public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS;
    @SerialzedName(name = "EntityLimitGroupExtraEntitiesHardlimitReached") public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED;
    @SerialzedName(name = "EntityLimitGroupExtraEntitiesSetSubregionError") public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR;
    @SerialzedName(name = "EntityLimitGroupExtraEntitiesBuySubregionError") public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR;
    @SerialzedName(name = "EntityLimitGroupCouldNotspawnEntity") public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY;
    @SerialzedName(name = "ArmBasicCommandMessage") public static String ARM_BASIC_COMMAND_MESSAGE;
    @SerialzedName(name = "RegionKindCreated") public static String REGIONKIND_CREATED;
    @SerialzedName(name = "RegionKindAlreadyExists") public static String REGIONKIND_ALREADY_EXISTS;
    @SerialzedName(name = "RegionKindDeleted") public static String REGIONKIND_DELETED;
    @SerialzedName(name = "RegionKindCanNotRemoveSystem") public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedName(name = "RegionKindListHeadline") public static String REGIONKIND_LIST_HEADLINE;
    @SerialzedName(name = "RegionKindModified") public static String REGIONKIND_MODIFIED;
    @SerialzedName(name = "MaterialNotFound") public static String MATERIAL_NOT_FOUND;
    @SerialzedName(name = "RegionKindLoreLineNotExist") public static String REGIONKIND_LORE_LINE_NOT_EXIST;
    @SerialzedName(name = "RegionKindInfoHeadline") public static String REGIONKIND_INFO_HEADLINE;
    @SerialzedName(name = "RegionKindInfoInternalName") public static String REGIONKIND_INFO_INTERNAL_NAME;
    @SerialzedName(name = "RegionKindInfoDisplayName") public static String REGIONKIND_INFO_DISPLAY_NAME;
    @SerialzedName(name = "RegionKindInfoMaterial") public static String REGIONKIND_INFO_MATERIAL;
    @SerialzedName(name = "RegionKindInfoDisplayInGui") public static String REGIONKIND_INFO_DISPLAY_IN_GUI;
    @SerialzedName(name = "RegionKindInfoDisplayInLimits") public static String REGIONKIND_INFO_DISPLAY_IN_LIMITS;
    @SerialzedName(name = "RegionKindInfoLore") public static String REGIONKIND_INFO_LORE;
    @SerialzedName(name = "RegionKindHelpHeadline") public static String REGIONKIND_HELP_HEADLINE;
    @SerialzedName(name = "PlayerNotFound") public static String PLAYER_NOT_FOUND;
    @SerialzedName(name = "RegionInfoSellregionUser") public static List<String> REGION_INFO_SELLREGION;
    @SerialzedName(name = "RegionInfoRentregionUser") public static List<String> REGION_INFO_RENTREGION;
    @SerialzedName(name = "RegionInfoContractregionUser") public static List<String> REGION_INFO_CONTRACTREGION;
    @SerialzedName(name = "RegionInfoSellregionAdmin") public static List<String> REGION_INFO_SELLREGION_ADMIN;
    @SerialzedName(name = "RegionInfoRentregionAdmin") public static List<String> REGION_INFO_RENTREGION_ADMIN;
    @SerialzedName(name = "RegionInfoContractregionAdmin") public static List<String> REGION_INFO_CONTRACTREGION_ADMIN;
    @SerialzedName(name = "RegionInfoSellregionSubregion") public static List<String> REGION_INFO_SELLREGION_SUBREGION;
    @SerialzedName(name = "RegionInfoRentregionSubregion") public static List<String> REGION_INFO_RENTREGION_SUBREGION;
    @SerialzedName(name = "RegionInfoContractregionSubregion") public static List<String> REGION_INFO_CONTRACTREGION_SUBREGION;
    @SerialzedName(name = "GUIFlageditorButton") public static String GUI_FLAGEDITOR_BUTTON;
    @SerialzedName(name = "GUIFlageditorMenuName") public static String GUI_FLAGEDITOR_MENU_NAME;
    @SerialzedName(name = "GUIFlageditorDeleteFlagButton") public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetFlagGroupAllButton") public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetFlagGroupMembersButton") public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetFlagGroupOwnersButton") public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetFlagGroupNonMembersButton") public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetFlagGroupNonOwnersButton") public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetStateflagAllowButton") public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetStateflagDenyButton") public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetBooleanflagTrueButton") public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetBooleanflagFalseButton") public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetStringflagSetMessageButton") public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetIntegerflagSetIntegerButton") public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON;
    @SerialzedName(name = "GUIFlageditorSetDoubleflagSetDoubleButton") public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON;
    @SerialzedName(name = "FlageditorFlagNotActivated") public static String FlAGEDITOR_FLAG_NOT_ACTIVATED;
    @SerialzedName(name = "FlageditorFlagHasBeenDeleted") public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED;
    @SerialzedName(name = "FlageditorFlagHasBeenUpdated") public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED;
    @SerialzedName(name = "FlageditorFlagCouldNotBeUpdated") public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED;
    @SerialzedName(name = "FlageditorStringflagSetMessageInfo") public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO;
    @SerialzedName(name = "FlageditorIntegerflagSetMessageInfo") public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO;
    @SerialzedName(name = "FlageditorDoubleflagSetMessageInfo") public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO;
    @SerialzedName(name = "GUIFlageditorResetButton") public static String GUI_FLAGEDITOR_RESET_BUTTON;
    @SerialzedName(name = "GUIFlageditorUnknownFlagSetPropertiesButton") public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON;
    @SerialzedName(name = "GUIFlageditorUnknownFlagSetPropertiesInfo") public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO;
    @SerialzedName(name = "FlaggroupDoesNotExist") public static String FLAGGROUP_DOES_NOT_EXIST;
    @SerialzedName(name = "SubregionFlaggroupOnlyForSubregions") public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedName(name = "RegionInfoFlaggroup") public static String REGION_INFO_FLAGGROUP;
    @SerialzedName(name = "GUITeleportToRegionButtonLore") public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE;
    @SerialzedName(name = "GUIMakeOwnerButtonLore") public static List<String> GUI_MAKE_OWNER_BUTTON_LORE;
    @SerialzedName(name = "GUIRemoveMemberButtonLore") public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE;
    @SerialzedName(name = "GUIResetRegionButtonLore") public static List<String> GUI_RESET_REGION_BUTTON_LORE;
    @SerialzedName(name = "TakeOverItemLore") public static List<String> GUI_TAKEOVER_ITEM_LORE;
    @SerialzedName(name = "GUIExtendRentRegionButtonLore") public static List<String> GUI_EXTEND_BUTTON_LORE;
    @SerialzedName(name = "GUIRentRegionLore") public static List<String> GUI_RENT_REGION_LORE;
    @SerialzedName(name = "GUIUserSellButtonLore") public static List<String> GUI_USER_SELL_BUTTON_LORE;
    @SerialzedName(name = "MemberlistInfoLore") public static List<String> GUI_MEMBER_INFO_LORE;
    @SerialzedName(name = "GUIContractItemLore") public static List<String> GUI_CONTRACT_ITEM_LORE;
    @SerialzedName(name = "GUIContractItemRegionLore") public static List<String> GUI_CONTRACT_REGION_LORE;
    @SerialzedName(name = "OwnerMemberlistInfoLore") public static List<String> GUI_OWNER_MEMBER_INFO_LORE;
    @SerialzedName(name = "GUISubregionManagerNoSubregionItemLore") public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE;
    @SerialzedName(name = "InfoDeactivated") public static String INFO_DEACTIVATED;
    @SerialzedName(name = "InfoNotSold") public static String INFO_REGION_NOT_SOLD;
    @SerialzedName(name = "InfoNow") public static String INFO_NOW;
    @SerialzedName(name = "InfoNotCalculated") public static String INFO_NOT_CALCULATED;
    @SerialzedName(name = "CouldNotFindOrLoadSchematicLog") public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG;
    @SerialzedName(name = "RegionSoldBackSuccessfully") public static String REGION_SOLD_BACK_SUCCESSFULLY;
    @SerialzedName(name = "RegionModifiedBoolean") public static String REGION_MODIFIED_BOOLEAN;
    @SerialzedName(name = "RegionModified") public static String REGION_MODIFIED;
    @SerialzedName(name = "UpdatingSchematic") public static String UPDATING_SCHEMATIC;
    @SerialzedName(name = "SchematicUpdated") public static String SCHEMATIC_UPDATED;
    @SerialzedName(name = "ContractRegionTerminated") public static String CONTRACTREGION_TERMINATED;
    @SerialzedName(name = "ContractRegionReactivated") public static String CONTRACTREGION_REACTIVATED;
    @SerialzedName(name = "RegionInfoFeatureDisabled") public static String REGION_INFO_FEATURE_DISABLED;
    @SerialzedName(name = "FlagGroupFeatureDisabled") public static String FLAGGROUP_FEATURE_DISABLED;
    @SerialzedName(name = "BackupCreated") public static String BACKUP_CREATED;
    @SerialzedName(name = "BackupRestored") public static String BACKUP_RESTORED;
    @SerialzedName(name = "CouldNotLoadBackup") public static String COULD_NOT_LOAD_BACKUP;
    @SerialzedName(name = "BackupListHeader") public static String BACKUP_LIST_HEADER;

    private static YamlConfiguration config;

    static void read() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Configuration config = YamlConfiguration.loadConfiguration(messagesconfigdic);
        ConfigurationSection cs = config.getConfigurationSection("Messages");
        if(cs == null) {
            return;
        }

        for(Field field : Messages.class.getDeclaredFields()) {
            if(field.isAnnotationPresent(SerialzedName.class)) {
                field.setAccessible(true);
                Object parsedOption = cs.get(getSerializedKey(field), field.getType());
                if (parsedOption instanceof List) {
                    List stringList = (List) parsedOption;
                    for(int i = 0; i < stringList.size(); i++) {
                        if(stringList.get(i) instanceof String) {
                            stringList.set(i, ChatColor.translateAlternateColorCodes('&', (String) stringList.get(i)));
                        }
                    }
                } else if (parsedOption instanceof String) {
                    parsedOption = ChatColor.translateAlternateColorCodes('&', (String) parsedOption);
                }

                try {
                    field.set(field.getType(), parsedOption);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(false);
            }
        }
    }

    private static String getSerializedKey(Field field) {
        String annotationValue = field.getAnnotation(SerialzedName.class).name();
        if (annotationValue.isEmpty()) {
            return field.getName();
        }
        return annotationValue;
    }

    public static void readOld() {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesconfigdic = new File(pluginfolder + "/messages.yml");
        Configuration config = YamlConfiguration.loadConfiguration(messagesconfigdic);

        //PREFIX = config.getString("Messages.Prefix") + " ";
        //if (config.getString("Messages.Prefix").equals(""))
        //    PREFIX = "";
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

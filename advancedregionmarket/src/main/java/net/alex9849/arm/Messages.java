package net.alex9849.arm;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

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

    @SerialzedString(name = "Prefix")
    public static String PREFIX;
    @SerialzedString(name = "Buymessage")
    public static String REGION_BUYMESSAGE;
    @SerialzedString(name = "NotEnoughtMoney")
    public static String NOT_ENOUGHT_MONEY;
    @SerialzedString(name = "RegionAlreadySold")
    public static String REGION_ALREADY_SOLD;
    @SerialzedString(name = "NoPermission")
    public static String NO_PERMISSION;
    @SerialzedString(name = "WorldDoesNotExist")
    public static String WORLD_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionDoesNotExist")
    public static String REGION_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionAddedToARM")
    public static String REGION_ADDED_TO_ARM;
    @SerialzedString(name = "SignAddedToRegion")
    public static String SIGN_ADDED_TO_REGION;
    @SerialzedString(name = "SignRemovedFromRegion")
    public static String SIGN_REMOVED_FROM_REGION;
    @SerialzedString(name = "RegionRemovedFromARM")
    public static String REGION_REMOVED_FROM_ARM;
    @SerialzedString(name = "SellSign1")
    public static String SELL_SIGN1;
    @SerialzedString(name = "SellSign2")
    public static String SELL_SIGN2;
    @SerialzedString(name = "SellSign3")
    public static String SELL_SIGN3;
    @SerialzedString(name = "SellSign4")
    public static String SELL_SIGN4;
    @SerialzedString(name = "SoldSign1")
    public static String SOLD_SIGN1;
    @SerialzedString(name = "SoldSign2")
    public static String SOLD_SIGN2;
    @SerialzedString(name = "SoldSign3")
    public static String SOLD_SIGN3;
    @SerialzedString(name = "SoldSign4")
    public static String SOLD_SIGN4;
    @SerialzedString(name = "Currency")
    public static String CURRENCY;
    @SerialzedString(name = "CommandOnlyIngame")
    public static String COMMAND_ONLY_INGAME;
    @SerialzedString(name = "RegionInfoExpired")
    public static String REGION_INFO_EXPIRED;
    @SerialzedString(name = "GUIMainMenuName")
    public static String GUI_MAIN_MENU_NAME;
    @SerialzedString(name = "GUIGoBack")
    public static String GUI_GO_BACK;
    @SerialzedString(name = "GUIMyOwnRegions")
    public static String GUI_MY_OWN_REGIONS;
    @SerialzedString(name = "GUIMemberRegionsMenuName")
    public static String GUI_MEMBER_REGIONS_MENU_NAME;
    @SerialzedString(name = "GUIMyMemberRegions")
    public static String GUI_MY_MEMBER_REGIONS;
    @SerialzedString(name = "GUISearchFreeRegion")
    public static String GUI_SEARCH_FREE_REGION;
    @SerialzedString(name = "GUIOwnRegionsMenuName")
    public static String GUI_OWN_REGIONS_MENU_NAME;
    @SerialzedString(name = "GUIMembersButton")
    public static String GUI_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIShowInfosButton")
    public static String GUI_SHOW_INFOS_BUTTON;
    @SerialzedString(name = "GUITeleportToRegionButton")
    public static String GUI_TELEPORT_TO_REGION_BUTTON;
    @SerialzedString(name = "GUIRegionFinderMenuName")
    public static String GUI_REGION_FINDER_MENU_NAME;
    @SerialzedString(name = "GUIMemberListMenuName")
    public static String GUI_MEMBER_LIST_MENU_NAME;
    @SerialzedString(name = "GUIMakeOwnerButton")
    public static String GUI_MAKE_OWNER_BUTTON;
    @SerialzedString(name = "GUIRemoveMemberButton")
    public static String GUI_REMOVE_MEMBER_BUTTON;
    @SerialzedString(name = "GUIMakeOwnerWarningName")
    public static String GUI_MAKE_OWNER_WARNING_NAME;
    @SerialzedString(name = "GUIWarningYes")
    public static String GUI_YES;
    @SerialzedString(name = "GUIWarningNo")
    public static String GUI_NO;
    @SerialzedString(name = "RegionTeleportMessage")
    public static String REGION_TELEPORT_MESSAGE;
    @SerialzedString(name = "NoPermissionsToBuyThisKindOfRegion")
    public static String NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION;
    @SerialzedString(name = "NoFreeRegionWithThisKind")
    public static String NO_FREE_REGION_WITH_THIS_KIND;
    @SerialzedString(name = "RegionkindDoesNotExist")
    public static String REGIONKIND_DOES_NOT_EXIST;
    @SerialzedString(name = "RegionNowAviable")
    public static String REGION_NOW_AVIABLE;
    @SerialzedString(name = "NoRegionAtPlayersPosition")
    public static String NO_REGION_AT_PLAYERS_POSITION;
    @SerialzedString(name = "RegionAddMemberNotOnline")
    public static String REGION_ADD_MEMBER_NOT_ONLINE;
    @SerialzedString(name = "RegionAddMemberAdded")
    public static String REGION_ADD_MEMBER_ADDED;
    @SerialzedString(name = "RegionRemoveMemberNotAMember")
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER;
    @SerialzedString(name = "RegionRemoveMemberRemoved")
    public static String REGION_REMOVE_MEMBER_REMOVED;
    @SerialzedString(name = "GUIResetRegionButton")
    public static String GUI_RESET_REGION_BUTTON;
    @SerialzedString(name = "GUIResetRegionWarningName")
    public static String GUI_RESET_REGION_WARNING_NAME;
    @SerialzedString(name = "ResetComplete")
    public static String RESET_COMPLETE;
    @SerialzedString(name = "ResetRegionCooldownError")
    public static String RESET_REGION_COOLDOWN_ERROR;
    @SerialzedString(name = "GUIRegionTakeOverMenuName")
    public static String GUI_TAKEOVER_MENU_NAME;
    @SerialzedString(name = "RegionTransferCompleteMessage")
    public static String REGION_TRANSFER_COMPLETE_MESSAGE;
    @SerialzedString(name = "GUICloseWindow")
    public static String GUI_CLOSE;
    @SerialzedString(name = "RentSign1")
    public static String RENT_SIGN1;
    @SerialzedString(name = "RentSign2")
    public static String RENT_SIGN2;
    @SerialzedString(name = "RentSign3")
    public static String RENT_SIGN3;
    @SerialzedString(name = "RentSign4")
    public static String RENT_SIGN4;
    @SerialzedString(name = "RentedSign1")
    public static String RENTED_SIGN1;
    @SerialzedString(name = "RentedSign2")
    public static String RENTED_SIGN2;
    @SerialzedString(name = "RentedSign3")
    public static String RENTED_SIGN3;
    @SerialzedString(name = "RentedSign4")
    public static String RENTED_SIGN4;
    @SerialzedString(name = "RentExtendMessage")
    public static String RENT_EXTEND_MESSAGE;
    @SerialzedString(name = "RentExtendMaxRentTimeExceeded")
    public static String RENT_EXTEND_MAX_RENT_TIME_EXCEEDED;
    @SerialzedString(name = "GUIExtendRentRegionButton")
    public static String GUI_EXTEND_BUTTON;
    @SerialzedString(name = "Complete")
    public static String COMPLETE;
    @SerialzedString(name = "RegionBuyOutOfLimit")
    public static String REGION_BUY_OUT_OF_LIMIT;
    @SerialzedString(name = "RegionErrorCanNotBuildHere")
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE;
    @SerialzedString(name = "Unlimited")
    public static String UNLIMITED;
    @SerialzedString(name = "GUIUserSellButton")
    public static String GUI_USER_SELL_BUTTON;
    @SerialzedString(name = "GUIUserSellWarning")
    public static String GUI_USER_SELL_WARNING;
    @SerialzedString(name = "LimitInfoTop")
    public static String LIMIT_INFO_TOP;
    @SerialzedString(name = "LimitInfo")
    public static String LIMIT_INFO;
    @SerialzedString(name = "GUILimitButton")
    public static String GUI_MY_LIMITS_BUTTON;
    @SerialzedString(name = "MemberlistInfo")
    public static String GUI_MEMBER_INFO_ITEM;
    @SerialzedString(name = "AddMemberMaxMembersExceeded")
    public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED;
    @SerialzedString(name = "RegionIsNotARentregion")
    public static String REGION_IS_NOT_A_RENTREGION;
    @SerialzedString(name = "RegionNotOwn")
    public static String REGION_NOT_OWN;
    @SerialzedString(name = "RegionNotSold")
    public static String REGION_NOT_SOLD;
    @SerialzedString(name = "PresetRemoved")
    public static String PRESET_REMOVED;
    @SerialzedString(name = "PresetSet")
    public static String PRESET_SET;
    @SerialzedString(name = "PresetSaved")
    public static String PRESET_SAVED;
    @SerialzedString(name = "PresetAlreadyExists")
    public static String PRESET_ALREADY_EXISTS;
    @SerialzedString(name = "PresetPlayerDontHasPreset")
    public static String PRESET_PLAYER_DONT_HAS_PRESET;
    @SerialzedString(name = "PresetDeleted")
    public static String PRESET_DELETED;
    @SerialzedString(name = "PresetNotFound")
    public static String PRESET_NOT_FOUND;
    @SerialzedString(name = "PresetLoaded")
    public static String PRESET_LOADED;
    @SerialzedString(name = "LimitInfoTotal")
    public static String LIMIT_INFO_TOTAL;
    @SerialzedString(name = "GUIRegionItemName")
    public static String GUI_REGION_ITEM_NAME;
    @SerialzedString(name = "GUIRegionFinderRegionKindName")
    public static String GUI_REGIONFINDER_REGIONKIND_NAME;
    @SerialzedString(name = "RentRegionExpirationWarning")
    public static String RENTREGION_EXPIRATION_WARNING;
    @SerialzedString(name = "ContractSign1")
    public static String CONTRACT_SIGN1;
    @SerialzedString(name = "ContractSign2")
    public static String CONTRACT_SIGN2;
    @SerialzedString(name = "ContractSign3")
    public static String CONTRACT_SIGN3;
    @SerialzedString(name = "ContractSign4")
    public static String CONTRACT_SIGN4;
    @SerialzedString(name = "ContractSoldSign1")
    public static String CONTRACT_SOLD_SIGN1;
    @SerialzedString(name = "ContractSoldSign2")
    public static String CONTRACT_SOLD_SIGN2;
    @SerialzedString(name = "ContractSoldSign3")
    public static String CONTRACT_SOLD_SIGN3;
    @SerialzedString(name = "ContractSoldSign4")
    public static String CONTRACT_SOLD_SIGN4;
    @SerialzedString(name = "ContractRegionExtended")
    public static String CONTRACT_REGION_EXTENDED;
    @SerialzedString(name = "GUIContractItem")
    public static String GUI_CONTRACT_ITEM;
    @SerialzedString(name = "ContractRegionStatusActive")
    public static String CONTRACT_REGION_STATUS_ACTIVE;
    @SerialzedString(name = "ContractRegionStatusTerminated")
    public static String CONTRACT_REGION_STATUS_TERMINATED;
    @SerialzedString(name = "RegionIsNotAContractRegion")
    public static String REGION_IS_NOT_A_CONTRACT_REGION;
    @SerialzedString(name = "OwnerMemberlistInfo")
    public static String GUI_OWNER_MEMBER_INFO_ITEM;
    @SerialzedString(name = "RegiontransferMemberNotOnline")
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE;
    @SerialzedString(name = "RegiontransferLimitError")
    public static String REGION_TRANSFER_LIMIT_ERROR;
    @SerialzedString(name = "SecondsSingular")
    public static String TIME_SECONDS_SINGULAR;
    @SerialzedString(name = "MinutesSingular")
    public static String TIME_MINUTES_SINGULAR;
    @SerialzedString(name = "HoursSingular")
    public static String TIME_HOURS_SINGULAR;
    @SerialzedString(name = "DaysSingular")
    public static String TIME_DAYS_SINGULAR;
    @SerialzedString(name = "SecondsPlural")
    public static String TIME_SECONDS_PLURAL;
    @SerialzedString(name = "MinutesPlural")
    public static String TIME_MINUTES_PLURAL;
    @SerialzedString(name = "HoursPlural")
    public static String TIME_HOURS_PLURAL;
    @SerialzedString(name = "DaysPlural")
    public static String TIME_DAYS_PLURAL;
    @SerialzedString(name = "SecondsShort")
    public static String TIME_SECONDS_SHORT;
    @SerialzedString(name = "MinutesShort")
    public static String TIME_MINUTES_SHORT;
    @SerialzedString(name = "HoursShort")
    public static String TIME_HOURS_SHORT;
    @SerialzedString(name = "DaysShort")
    public static String TIME_DAYS_SHORT;
    @SerialzedString(name = "TimeUnitSplitter")
    public static String TIME_UNIT_SPLITTER;
    @SerialzedString(name = "TimeUnitSplitterShort")
    public static String TIME_UNIT_SPLITTER_SHORT;
    @SerialzedString(name = "UserNotAMemberOrOwner")
    public static String NOT_A_MEMBER_OR_OWNER;
    @SerialzedString(name = "RegionInfoYes")
    public static String YES;
    @SerialzedString(name = "RegionInfoNo")
    public static String NO;
    @SerialzedString(name = "RegionStats")
    public static String REGION_STATS;
    @SerialzedString(name = "RegionStatsPattern")
    public static String REGION_STATS_PATTERN;
    @SerialzedString(name = "TeleporterNoSaveLocation")
    public static String TELEPORTER_NO_SAVE_LOCATION_FOUND;
    @SerialzedString(name = "TeleporterDontMove")
    public static String TELEPORTER_DONT_MOVE;
    @SerialzedString(name = "TeleporterTeleportationAborded")
    public static String TELEPORTER_TELEPORTATION_ABORDED;
    @SerialzedString(name = "OfferSent")
    public static String OFFER_SENT;
    @SerialzedString(name = "OfferAcceptedSeller")
    public static String OFFER_ACCEPTED_SELLER;
    @SerialzedString(name = "OfferAcceptedBuyer")
    public static String OFFER_ACCEPTED_BUYER;
    @SerialzedString(name = "NoOfferToAnswer")
    public static String NO_OFFER_TO_ANSWER;
    @SerialzedString(name = "OfferRejected")
    public static String OFFER_REJECTED;
    @SerialzedString(name = "OfferHasBeenRejected")
    public static String OFFER_HAS_BEEN_REJECTED;
    @SerialzedString(name = "NoOfferToReject")
    public static String NO_OFFER_TO_REJECT;
    @SerialzedString(name = "OfferCancelled")
    public static String OFFER_CANCELED;
    @SerialzedString(name = "OfferHasBeenCancelled")
    public static String OFFER_HAS_BEEN_CANCELLED;
    @SerialzedString(name = "NoOfferToCancel")
    public static String NO_OFFER_TO_CANCEL;
    @SerialzedString(name = "BuyerAlreadyGotAnOffer")
    public static String BUYER_ALREADY_GOT_AN_OFFER;
    @SerialzedString(name = "SellerAlreadyCreatedAnOffer")
    public static String SELLER_ALREADY_CREATED_AN_OFFER;
    @SerialzedString(name = "SellerDoesNotLongerOwnRegion")
    public static String SELLER_DOES_NOT_LONGER_OWN_REGION;
    @SerialzedString(name = "IncommingOffer")
    public static String INCOMING_OFFER;
    @SerialzedString(name = "SelectedPlayerIsNotOnline")
    public static String SELECTED_PLAYER_NOT_ONLINE;
    @SerialzedString(name = "OfferTimedOut")
    public static String OFFER_TIMED_OUT;
    @SerialzedString(name = "BadSyntax")
    public static String BAD_SYNTAX;
    @SerialzedString(name = "BadSyntaxSplitter")
    public static String BAD_SYNTAX_SPLITTER;
    @SerialzedString(name = "HelpHeadline")
    public static String HELP_HEADLINE;
    @SerialzedString(name = "PresetInfoSellregion")
    public static List<String> PRESET_INFO_SELLREGION;
    @SerialzedString(name = "PresetInfoContractregion")
    public static List<String> PRESET_INFO_CONTRACTREGION;
    @SerialzedString(name = "PresetInfoRentregion")
    public static List<String> PRESET_INFO_RENTREGION;
    @SerialzedString(name = "NotDefined")
    public static String NOT_DEFINED;
    @SerialzedString(name = "PriceCanNotBeNegative")
    public static String PRICE_CAN_NOT_BE_NEGATIVE;
    @SerialzedString(name = "SellBackWarning")
    public static String SELLBACK_WARNING;
    @SerialzedString(name = "SubregionInactivityResetError")
    public static String SUBREGION_INACTIVITYRESET_ERROR;
    @SerialzedString(name = "SubregionAutoRestoreError")
    public static String SUBREGION_AUTORESTORE_ERROR;
    @SerialzedString(name = "RegionNotRestoreable")
    public static String REGION_NOT_RESTORABLE;
    @SerialzedString(name = "RegionSelectedMultipleRegions")
    public static String REGION_SELECTED_MULTIPLE_REGIONS;
    @SerialzedString(name = "SubregionRegionkindError")
    public static String SUBREGION_REGIONKIND_ERROR;
    @SerialzedString(name = "SubRegionRegionkindOnlyForSubregions")
    public static String SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS;
    @SerialzedString(name = "SubregionTeleportLocationError")
    public static String SUBREGION_TELEPORT_LOCATION_ERROR;
    @SerialzedString(name = "RegionNotRegistred")
    public static String REGION_NOT_REGISTRED;
    @SerialzedString(name = "FirstPositionSet")
    public static String FIRST_POSITION_SET;
    @SerialzedString(name = "SecondPositionSet")
    public static String SECOND_POSITION_SET;
    @SerialzedString(name = "MarkInOtherRegion")
    public static String MARK_IN_OTHER_REGION_REMOVING;
    @SerialzedString(name = "ParentRegionNotOwn")
    public static String PARENT_REGION_NOT_OWN;
    @SerialzedString(name = "SubRegionRemoveNoPermissionBecauseSold")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD;
    @SerialzedString(name = "SubRegionRemoveNoPermissionBecauseAvailable")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE;
    @SerialzedString(name = "PosCloudNotBeSetMarkOutsideRegion")
    public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION;
    @SerialzedString(name = "SubRegionAlreadyAtThisPosition")
    public static String ALREADY_SUBREGION_AT_THIS_POSITION;
    @SerialzedString(name = "SubRegionLimitReached")
    public static String SUBREGION_LIMIT_REACHED;
    @SerialzedString(name = "SelectionInvalid")
    public static String SELECTION_INVALID;
    @SerialzedString(name = "RegionCreatedAndSaved")
    public static String REGION_CREATED_AND_SAVED;
    @SerialzedString(name = "RegionNotASubregion")
    public static String REGION_NOT_A_SUBREGION;
    @SerialzedString(name = "RegionDeleted")
    public static String REGION_DELETED;
    @SerialzedString(name = "DeleteRegionWarningName")
    public static String DELETE_REGION_WARNING_NAME;
    @SerialzedString(name = "UnsellRegionButton")
    public static String UNSELL_REGION_BUTTON;
    @SerialzedString(name = "UnsellRegionButtonLore")
    public static List<String> UNSELL_REGION_BUTTON_LORE;
    @SerialzedString(name = "UnsellRegionWarningName")
    public static String UNSELL_REGION_WARNING_NAME;
    @SerialzedString(name = "SubregionHelpHeadline")
    public static String SUBREGION_HELP_HEADLINE;
    @SerialzedString(name = "SellregionName")
    public static String SELLREGION_NAME;
    @SerialzedString(name = "ContractregionName")
    public static String CONTRACTREGION_NAME;
    @SerialzedString(name = "RentregionName")
    public static String RENTREGION_NAME;
    @SerialzedString(name = "GUISubregionsButton")
    public static String GUI_SUBREGION_ITEM_BUTTON;
    @SerialzedString(name = "GUISubregionListMenuName")
    public static String GUI_SUBREGION_LIST_MENU_NAME;
    @SerialzedString(name = "GUIHotelButton")
    public static String GUI_SUBREGION_HOTEL_BUTTON;
    @SerialzedString(name = "GUIDeleteRegionButton")
    public static String GUI_SUBREGION_DELETE_REGION_BUTTON;
    @SerialzedString(name = "GUITeleportToSignOrRegionButton")
    public static String GUI_TELEPORT_TO_SIGN_OR_REGION;
    @SerialzedString(name = "GUIRegionfinderTeleportToSignButton")
    public static String GUI_TELEPORT_TO_SIGN;
    @SerialzedString(name = "GUIRegionfinderTeleportToRegionButton")
    public static String GUI_TELEPORT_TO_REGION;
    @SerialzedString(name = "GUINextPageButton")
    public static String GUI_NEXT_PAGE;
    @SerialzedString(name = "GUIPrevPageButton")
    public static String GUI_PREV_PAGE;
    @SerialzedString(name = "Enabled")
    public static String ENABLED;
    @SerialzedString(name = "Disabled")
    public static String DISABLED;
    @SerialzedString(name = "Sold")
    public static String SOLD;
    @SerialzedString(name = "Available")
    public static String AVAILABLE;
    @SerialzedString(name = "SubregionIsUserResettableError")
    public static String SUBREGION_IS_USER_RESETTABLE_ERROR;
    @SerialzedString(name = "SubregionMaxMembersError")
    public static String SUBREGION_MAX_MEMBERS_ERROR;
    @SerialzedString(name = "GUIHotelButtonLore")
    public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE;
    @SerialzedString(name = "GUISubregionInfoSell")
    public static List<String> GUI_SUBREGION_REGION_INFO_SELL;
    @SerialzedString(name = "GUISubregionInfoRent")
    public static List<String> GUI_SUBREGION_REGION_INFO_RENT;
    @SerialzedString(name = "GUISubregionInfoContract")
    public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT;
    @SerialzedString(name = "GUIRegionfinderInfoSell")
    public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL;
    @SerialzedString(name = "GUIRegionfinderInfoRent")
    public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT;
    @SerialzedString(name = "GUIRegionfinderInfoContract")
    public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT;
    @SerialzedString(name = "SubregionCreationCreateSignInfo")
    public static List<String> SELECTION_SAVED_CREATE_SIGN;
    @SerialzedString(name = "SubregionCreationSelectAreaInfo")
    public static List<String> SUBREGION_TOOL_INSTRUCTION;
    @SerialzedString(name = "SubregionToolAlreadyOwned")
    public static String SUBREGION_TOOL_ALREADY_OWNED;
    @SerialzedString(name = "AutopriceList")
    public static String AUTOPRICE_LIST;
    @SerialzedString(name = "GUISubregionManagerNoSubregionItem")
    public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM;
    @SerialzedString(name = "SelltypeNotExist")
    public static String SELLTYPE_NOT_EXIST;
    @SerialzedString(name = "SignLinkModeActivated")
    public static String SIGN_LINK_MODE_ACTIVATED;
    @SerialzedString(name = "SignLinkModeDeactivated")
    public static String SIGN_LINK_MODE_DEACTIVATED;
    @SerialzedString(name = "SignLinkModeAlreadyDeactivated")
    public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED;
    @SerialzedString(name = "SignLinkModePresetNotPriceready")
    public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY;
    @SerialzedString(name = "SignLinkModeNoPresetSelected")
    public static String SIGN_LINK_MODE_NO_PRESET_SELECTED;
    @SerialzedString(name = "SignLinkModeSignBelongsToAnotherRegion")
    public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION;
    @SerialzedString(name = "SignLinkModeSignSelected")
    public static String SIGN_LINK_MODE_SIGN_SELECTED;
    @SerialzedString(name = "SignLinkModeMultipleWgRegionsAtPosition")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS;
    @SerialzedString(name = "SignLinkModeNoWgRegionAtPosition")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION;
    @SerialzedString(name = "SignLinkModeCouldNotIdentifyWorld")
    public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD;
    @SerialzedString(name = "SignLinkModeNoSignSelected")
    public static String SIGN_LINK_MODE_NO_SIGN_SELECTED;
    @SerialzedString(name = "SignLinkModeNoWgRegionSelected")
    public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED;
    @SerialzedString(name = "SignLinkModeSelectedRegion")
    public static String SIGN_LINK_MODE_REGION_SELECTED;
    @SerialzedString(name = "SchematicNotFoundErrorUser")
    public static String SCHEMATIC_NOT_FOUND_ERROR_USER;
    @SerialzedString(name = "EntityLimitHelpHeadline")
    public static String ENTITYLIMIT_HELP_HEADLINE;
    @SerialzedString(name = "EntityLimitGroupNotExist")
    public static String ENTITYLIMITGROUP_DOES_NOT_EXIST;
    @SerialzedString(name = "EntityLimitSet")
    public static String ENTITYLIMIT_SET;
    @SerialzedString(name = "EntityLimitRemoved")
    public static String ENTITYLIMIT_REMOVED;
    @SerialzedString(name = "EntityTypeDoesNotExist")
    public static String ENTITYTYPE_DOES_NOT_EXIST;
    @SerialzedString(name = "EntityLimitGroupNotContainEntityLimit")
    public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT;
    @SerialzedString(name = "EntityLimitTotal")
    public static String ENTITYLIMIT_TOTAL;
    @SerialzedString(name = "SubregionPaybackPercentageError")
    public static String SUBREGION_PAYBACKPERCENTAGE_ERROR;
    @SerialzedString(name = "EntityLimitCheckHeadline")
    public static String ENTITYLIMIT_CHECK_HEADLINE;
    @SerialzedString(name = "EntityLimitCheckPattern")
    public static String ENTITYLIMIT_CHECK_PATTERN;
    @SerialzedString(name = "EntityLimitCheckExtensionInfo")
    public static String ENTITYLIMIT_CHECK_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupAlreadyExists")
    public static String ENTITYLIMITGROUP_ALREADY_EXISTS;
    @SerialzedString(name = "EntityLimitGroupCreated")
    public static String ENTITYLIMITGROUP_CREATED;
    @SerialzedString(name = "EntityLimitGroupCanNotRemoveSystem")
    public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedString(name = "EntityLimitGroupDeleted")
    public static String ENTITYLIMITGROUP_DELETED;
    @SerialzedString(name = "EntityLimitGroupInfoHeadline")
    public static String ENTITYLIMITGROUP_INFO_HEADLINE;
    @SerialzedString(name = "EntityLimitGroupInfoGroupname")
    public static String ENTITYLIMITGROUP_INFO_GROUPNAME;
    @SerialzedString(name = "EntityLimitGroupInfoPattern")
    public static String ENTITYLIMITGROUP_INFO_PATTERN;
    @SerialzedString(name = "EntityLimitInfoExtensionInfo")
    public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupListHeadline")
    public static String ENTITYLIMITGROUP_LIST_HEADLINE;
    @SerialzedString(name = "SubregionEntityLimitOnlyForSubregions")
    public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedString(name = "MassactionSplitter")
    public static String MASSACTION_SPLITTER;
    @SerialzedString(name = "SubregionEntityLimitError")
    public static String SUBREGION_ENTITYLIMITGROUP_ERROR;
    @SerialzedString(name = "SubregionFlagGroupError")
    public static String SUBREGION_FLAGGROUP_ERROR;
    @SerialzedString(name = "GUIEntityLimitItemButton")
    public static String GUI_ENTITYLIMIT_ITEM_BUTTON;
    @SerialzedString(name = "GUIEntityLimitItemLore")
    public static List<String> GUI_ENTITYLIMIT_ITEM_LORE;
    @SerialzedString(name = "GUIEntityLimitInfoPattern")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN;
    @SerialzedString(name = "GUIEntityLimitInfoExtensionInfo")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO;
    @SerialzedString(name = "EntityLimitGroupEntityLimitAlreadyUnlimited")
    public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesSet")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesExpandSuccess")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesHardlimitReached")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesSetSubregionError")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR;
    @SerialzedString(name = "EntityLimitGroupExtraEntitiesBuySubregionError")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR;
    @SerialzedString(name = "EntityLimitGroupCouldNotspawnEntity")
    public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY;
    @SerialzedString(name = "ArmBasicCommandMessage")
    public static String ARM_BASIC_COMMAND_MESSAGE;
    @SerialzedString(name = "RegionKindCreated")
    public static String REGIONKIND_CREATED;
    @SerialzedString(name = "RegionKindAlreadyExists")
    public static String REGIONKIND_ALREADY_EXISTS;
    @SerialzedString(name = "RegionKindDeleted")
    public static String REGIONKIND_DELETED;
    @SerialzedString(name = "RegionKindCanNotRemoveSystem")
    public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM;
    @SerialzedString(name = "RegionKindListHeadline")
    public static String REGIONKIND_LIST_HEADLINE;
    @SerialzedString(name = "RegionKindModified")
    public static String REGIONKIND_MODIFIED;
    @SerialzedString(name = "MaterialNotFound")
    public static String MATERIAL_NOT_FOUND;
    @SerialzedString(name = "RegionKindLoreLineNotExist")
    public static String REGIONKIND_LORE_LINE_NOT_EXIST;
    @SerialzedString(name = "RegionKindInfoHeadline")
    public static String REGIONKIND_INFO_HEADLINE;
    @SerialzedString(name = "RegionKindInfoInternalName")
    public static String REGIONKIND_INFO_INTERNAL_NAME;
    @SerialzedString(name = "RegionKindInfoDisplayName")
    public static String REGIONKIND_INFO_DISPLAY_NAME;
    @SerialzedString(name = "RegionKindInfoMaterial")
    public static String REGIONKIND_INFO_MATERIAL;
    @SerialzedString(name = "RegionKindInfoDisplayInGui")
    public static String REGIONKIND_INFO_DISPLAY_IN_GUI;
    @SerialzedString(name = "RegionKindInfoDisplayInLimits")
    public static String REGIONKIND_INFO_DISPLAY_IN_LIMITS;
    @SerialzedString(name = "RegionKindInfoLore")
    public static String REGIONKIND_INFO_LORE;
    @SerialzedString(name = "RegionKindHelpHeadline")
    public static String REGIONKIND_HELP_HEADLINE;
    @SerialzedString(name = "PlayerNotFound")
    public static String PLAYER_NOT_FOUND;
    @SerialzedString(name = "RegionInfoSellregionUser")
    public static List<String> REGION_INFO_SELLREGION;
    @SerialzedString(name = "RegionInfoRentregionUser")
    public static List<String> REGION_INFO_RENTREGION;
    @SerialzedString(name = "RegionInfoContractregionUser")
    public static List<String> REGION_INFO_CONTRACTREGION;
    @SerialzedString(name = "RegionInfoSellregionAdmin")
    public static List<String> REGION_INFO_SELLREGION_ADMIN;
    @SerialzedString(name = "RegionInfoRentregionAdmin")
    public static List<String> REGION_INFO_RENTREGION_ADMIN;
    @SerialzedString(name = "RegionInfoContractregionAdmin")
    public static List<String> REGION_INFO_CONTRACTREGION_ADMIN;
    @SerialzedString(name = "RegionInfoSellregionSubregion")
    public static List<String> REGION_INFO_SELLREGION_SUBREGION;
    @SerialzedString(name = "RegionInfoRentregionSubregion")
    public static List<String> REGION_INFO_RENTREGION_SUBREGION;
    @SerialzedString(name = "RegionInfoContractregionSubregion")
    public static List<String> REGION_INFO_CONTRACTREGION_SUBREGION;
    @SerialzedString(name = "GUIFlageditorButton")
    public static String GUI_FLAGEDITOR_BUTTON;
    @SerialzedString(name = "GUIFlageditorMenuName")
    public static String GUI_FLAGEDITOR_MENU_NAME;
    @SerialzedString(name = "GUIFlageditorDeleteFlagButton")
    public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupAllButton")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupMembersButton")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupOwnersButton")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupNonMembersButton")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetFlagGroupNonOwnersButton")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStateflagAllowButton")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStateflagDenyButton")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetBooleanflagTrueButton")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetBooleanflagFalseButton")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetStringflagSetMessageButton")
    public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetIntegerflagSetIntegerButton")
    public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON;
    @SerialzedString(name = "GUIFlageditorSetDoubleflagSetDoubleButton")
    public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON;
    @SerialzedString(name = "FlageditorFlagNotActivated")
    public static String FlAGEDITOR_FLAG_NOT_ACTIVATED;
    @SerialzedString(name = "FlageditorFlagHasBeenDeleted")
    public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED;
    @SerialzedString(name = "FlageditorFlagHasBeenUpdated")
    public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED;
    @SerialzedString(name = "FlageditorFlagCouldNotBeUpdated")
    public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED;
    @SerialzedString(name = "FlageditorStringflagSetMessageInfo")
    public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO;
    @SerialzedString(name = "FlageditorIntegerflagSetMessageInfo")
    public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO;
    @SerialzedString(name = "FlageditorDoubleflagSetMessageInfo")
    public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO;
    @SerialzedString(name = "GUIFlageditorResetButton")
    public static String GUI_FLAGEDITOR_RESET_BUTTON;
    @SerialzedString(name = "GUIFlageditorUnknownFlagSetPropertiesButton")
    public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON;
    @SerialzedString(name = "GUIFlageditorUnknownFlagSetPropertiesInfo")
    public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO;
    @SerialzedString(name = "FlaggroupDoesNotExist")
    public static String FLAGGROUP_DOES_NOT_EXIST;
    @SerialzedString(name = "SubregionFlaggroupOnlyForSubregions")
    public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS;
    @SerialzedString(name = "GUITeleportToRegionButtonLore")
    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE;
    @SerialzedString(name = "GUIMakeOwnerButtonLore")
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE;
    @SerialzedString(name = "GUIRemoveMemberButtonLore")
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE;
    @SerialzedString(name = "GUIResetRegionButtonLore")
    public static List<String> GUI_RESET_REGION_BUTTON_LORE;
    @SerialzedString(name = "TakeOverItemLore")
    public static List<String> GUI_TAKEOVER_ITEM_LORE;
    @SerialzedString(name = "GUIExtendRentRegionButtonLore")
    public static List<String> GUI_EXTEND_BUTTON_LORE;
    @SerialzedString(name = "GUIRentRegionLore")
    public static List<String> GUI_RENT_REGION_LORE;
    @SerialzedString(name = "GUIUserSellButtonLore")
    public static List<String> GUI_USER_SELL_BUTTON_LORE;
    @SerialzedString(name = "MemberlistInfoLore")
    public static List<String> GUI_MEMBER_INFO_LORE;
    @SerialzedString(name = "GUIContractItemLore")
    public static List<String> GUI_CONTRACT_ITEM_LORE;
    @SerialzedString(name = "GUIContractItemRegionLore")
    public static List<String> GUI_CONTRACT_REGION_LORE;
    @SerialzedString(name = "OwnerMemberlistInfoLore")
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE;
    @SerialzedString(name = "GUISubregionManagerNoSubregionItemLore")
    public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE;
    @SerialzedString(name = "SellPresetHelpHeadline")
    public static String SELLPRESET_HELP_HEADLINE;
    @SerialzedString(name = "ContractPresetHelpHeadline")
    public static String CONTRACTPRESET_HELP_HEADLINE;
    @SerialzedString(name = "RentPresetHelpHeadline")
    public static String RENTPRESET_HELP_HEADLINE;
    @SerialzedString(name = "InfoDeactivated")
    public static String INFO_DEACTIVATED;
    @SerialzedString(name = "InfoNotSold")
    public static String INFO_REGION_NOT_SOLD;
    @SerialzedString(name = "InfoNow")
    public static String INFO_NOW;
    @SerialzedString(name = "InfoNotCalculated")
    public static String INFO_NOT_CALCULATED;
    @SerialzedString(name = "CouldNotFindOrLoadSchematicLog")
    public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG;
    @SerialzedString(name = "RegionSoldBackSuccessfully")
    public static String REGION_SOLD_BACK_SUCCESSFULLY;
    @SerialzedString(name = "RegionModifiedBoolean")
    public static String REGION_MODIFIED_BOOLEAN;
    @SerialzedString(name = "RegionModified")
    public static String REGION_MODIFIED;
    @SerialzedString(name = "UpdatingSchematic")
    public static String UPDATING_SCHEMATIC;
    @SerialzedString(name = "SchematicUpdated")
    public static String SCHEMATIC_UPDATED;
    @SerialzedString(name = "ContractRegionTerminated")
    public static String CONTRACTREGION_TERMINATED;
    @SerialzedString(name = "ContractRegionReactivated")
    public static String CONTRACTREGION_REACTIVATED;
    @SerialzedString(name = "RegionInfoFeatureDisabled")
    public static String REGION_INFO_FEATURE_DISABLED;
    @SerialzedString(name = "FlagGroupFeatureDisabled")
    public static String FLAGGROUP_FEATURE_DISABLED;
    @SerialzedString(name = "BackupCreated")
    public static String BACKUP_CREATED;
    @SerialzedString(name = "BackupRestored")
    public static String BACKUP_RESTORED;
    @SerialzedString(name = "CouldNotLoadBackup")
    public static String COULD_NOT_LOAD_BACKUP;
    @SerialzedString(name = "BackupListHeader")
    public static String BACKUP_LIST_HEADER;

    public Messages(File savePath, MessageLocale locale) {
        YamlConfiguration config = writeConfigFile(savePath, locale);
        updateDefauts(config, locale, savePath);
        load(config);
    }

    private static YamlConfiguration writeConfigFile(File savePath, MessageLocale locale) {
        if(!savePath.exists()) {
            try {
                InputStream stream = AdvancedRegionMarket
                        .getInstance().getResource("messages_" + locale.code() + ".yml");
                OutputStream output = new FileOutputStream(savePath);
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
        return YamlConfiguration.loadConfiguration(savePath);
    }

    private void updateDefauts(YamlConfiguration config, MessageLocale locale, File savePath) {
        int configVersion = config.getInt("FileVersion");
        int newConfigVersion = configVersion;
        YamlConfiguration localeConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(AdvancedRegionMarket.getInstance().getResource("messages_" + locale.code() + ".yml")));
        int localeFileVersion = localeConfig.getInt("FileVersion");

        ConfigurationSection configMessages = config.getConfigurationSection("Messages");
        if (configMessages == null) {
            return;
        }
        ConfigurationSection localeconfigMessages = localeConfig.getConfigurationSection("Messages");
        if (localeconfigMessages == null) {
            return;
        }
        boolean fileUpdated = false;
        for(Field field : Messages.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(SerialzedString.class)) {
                continue;
            }
            int requestedVersion = getRequestedVersion(field);
            String serializedKey =  getSerializedKey(field);
            if((configVersion >= requestedVersion) && configMessages.get(serializedKey) != null) {
                continue;
            }
            Object replaceMessage = localeconfigMessages.get(serializedKey);
            if(replaceMessage == null || localeFileVersion < requestedVersion) {
                try {
                    replaceMessage = field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
    }

    private void load(YamlConfiguration config) {
        ConfigurationSection cs = config.getConfigurationSection("Messages");
        if (cs == null) {
            return;
        }

        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(SerialzedString.class)) {
                field.setAccessible(true);
                Object parsedOption = cs.get(getSerializedKey(field), field.getType());
                if (parsedOption instanceof List) {
                    List stringList = (List) parsedOption;
                    for (int i = 0; i < stringList.size(); i++) {
                        if (stringList.get(i) instanceof String) {
                            stringList.set(i, ChatColor.translateAlternateColorCodes('&', (String) stringList.get(i)));
                        }
                    }
                } else if (parsedOption instanceof String) {
                    parsedOption = ChatColor.translateAlternateColorCodes('&', (String) parsedOption);
                }

                if(parsedOption != null && field.getType().isAssignableFrom(parsedOption.getClass())) {
                    try {
                        field.set(this, parsedOption);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                field.setAccessible(false);
            }
        }
    }

    private static String getSerializedKey(Field field) {
        String annotationValue = field.getAnnotation(SerialzedString.class).name();
        if (annotationValue.isEmpty()) {
            return field.getName();
        }
        return annotationValue;
    }

    private static int getRequestedVersion(Field field) {
        return field.getAnnotation(SerialzedString.class).version();
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

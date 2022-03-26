package net.alex9849.arm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public class MessageVariables {
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Message {
        public String name();

        public String[] message();

        public int version() default 0;
    }

    //Current version = 7
    @Messages.Message(name = "Prefix", message = "&b[ARM]&r ", version = 1)
    public static String PREFIX;
    @Messages.Message(name = "Buymessage", message = "&aRegion successfully bought!")
    public static String REGION_BUYMESSAGE;
    @Messages.Message(name = "NotEnoughMoney", message = "&4You do not have enough money!")
    public static String NOT_ENOUGH_MONEY;
    @Messages.Message(name = "RegionAlreadySold", message = "&4Region already Sold!")
    public static String REGION_ALREADY_SOLD;
    @Messages.Message(name = "NoPermission", message = "&4You do not have permission!")
    public static String NO_PERMISSION;
    @Messages.Message(name = "WorldDoesNotExist", message = "&4The selected world does not exist!")
    public static String WORLD_DOES_NOT_EXIST;
    @Messages.Message(name = "RegionDoesNotExist", message = "&4The selected region does not exist in this (or the selected) world!")
    public static String REGION_DOES_NOT_EXIST;
    @Messages.Message(name = "WGRegionNotFound", message = "&4Could not find region! Make sure to execute this command in the same world in that the region exists! Some versions of WorldGuard require a case-sensitive region name!")
    public static String WGREGION_NOT_FOUND;
    @Messages.Message(name = "RegionAddedToARM", message = "&7Region has been created and added to ARM!", version = 3)
    public static String REGION_ADDED_TO_ARM;
    @Messages.Message(name = "RegionAndSignAddedToARM", message = "&7Regionsign has been created and region has been added to ARM!")
    public static String REGION_AND_SIGN_ADDED_TO_ARM;
    @Messages.Message(name = "SignAddedToRegion", message = "&7Region already exists! The sign has been added to the region!")
    public static String SIGN_ADDED_TO_REGION;
    @Messages.Message(name = "SignRemovedFromRegion", message = "&7Regionsign removed! %remaining% Sign(s) remaining!", version = 3)
    public static String SIGN_REMOVED_FROM_REGION;
    @Messages.Message(name = "RegionZeroSignsReached", message = "&7You've deleted the last sign of this region! Please note that this region has &4&l&nNOT BEEN REMOVED FROM ARM!!&r &7If you want to unregister this region execute &6/arm delete %regionid%&7!")
    public static String REGION_ZERO_SIGNS_REACHED;
    @Messages.Message(name = "SellSign1", message = "&2For Sale")
    public static String SELL_SIGN1;
    @Messages.Message(name = "SellSign2", message = "%regionid%")
    public static String SELL_SIGN2;
    @Messages.Message(name = "SellSign3", message = "%price%%currency%")
    public static String SELL_SIGN3;
    @Messages.Message(name = "SellSign4", message = "%dimensions%")
    public static String SELL_SIGN4;
    @Messages.Message(name = "SoldSign1", message = "&4Sold")
    public static String SOLD_SIGN1;
    @Messages.Message(name = "SoldSign2", message = "%regionid%")
    public static String SOLD_SIGN2;
    @Messages.Message(name = "SoldSign3", message = "")
    public static String SOLD_SIGN3;
    @Messages.Message(name = "SoldSign4", message = "%owner%")
    public static String SOLD_SIGN4;
    @Messages.Message(name = "ProtectionOfContinuanceSign1", message = "&4Not for sale!")
    public static String PROTECTION_OF_CONTINUANCE_SIGN1;
    @Messages.Message(name = "ProtectionOfContinuanceSign2", message = "Region is under")
    public static String PROTECTION_OF_CONTINUANCE_SIGN2;
    @Messages.Message(name = "ProtectionOfContinuanceSign3", message = "protection of")
    public static String PROTECTION_OF_CONTINUANCE_SIGN3;
    @Messages.Message(name = "ProtectionOfContinuanceSign4", message = "continuance!")
    public static String PROTECTION_OF_CONTINUANCE_SIGN4;
    @Messages.Message(name = "Currency", message = "$")
    public static String CURRENCY;
    @Messages.Message(name = "CommandOnlyIngame", message = "&4This command can only be executed ingame!")
    public static String COMMAND_ONLY_INGAME;
    @Messages.Message(name = "RegionInfoExpired", message = "&4Expired")
    public static String REGION_INFO_EXPIRED;
    @Messages.Message(name = "GUIMainMenuName", message = "&1ARM - Menu")
    public static String GUI_MAIN_MENU_NAME;
    @Messages.Message(name = "GUIGoBack", message = "&6Go back")
    public static String GUI_GO_BACK;
    @Messages.Message(name = "GUIMyOwnRegions", message = "&6My regions (Owner)")
    public static String GUI_MY_OWN_REGIONS;
    @Messages.Message(name = "GUIMemberRegionsMenuName", message = "&1ARM - My regions (Member)")
    public static String GUI_MEMBER_REGIONS_MENU_NAME;
    @Messages.Message(name = "GUIMyMemberRegions", message = "&6My regions (Member)")
    public static String GUI_MY_MEMBER_REGIONS;
    @Messages.Message(name = "GUISearchFreeRegion", message = "&6Search free region")
    public static String GUI_SEARCH_FREE_REGION;
    @Messages.Message(name = "GUIOwnRegionsMenuName", message = "&1ARM - My regions (Owner)")
    public static String GUI_OWN_REGIONS_MENU_NAME;
    @Messages.Message(name = "GUIMembersButton", message = "&6Members")
    public static String GUI_MEMBERS_BUTTON;
    @Messages.Message(name = "GUIShowInfosButton", message = "&6Show infos")
    public static String GUI_SHOW_INFOS_BUTTON;
    @Messages.Message(name = "GUITeleportToRegionButton", message = "&6Teleport to region")
    public static String GUI_TELEPORT_TO_REGION_BUTTON;
    @Messages.Message(name = "GUIRegionFinderMenuName", message = "&1ARM - Regionfinder")
    public static String GUI_REGION_FINDER_MENU_NAME;
    @Messages.Message(name = "GUIMemberListMenuName", message = "&1ARM - Members of %regionid%")
    public static String GUI_MEMBER_LIST_MENU_NAME;
    @Messages.Message(name = "GUIMakeOwnerButton", message = "&aMake owner")
    public static String GUI_MAKE_OWNER_BUTTON;
    @Messages.Message(name = "GUIRemoveMemberButton", message = "&4Remove")
    public static String GUI_REMOVE_MEMBER_BUTTON;
    @Messages.Message(name = "GUIMakeOwnerWarningName", message = "&4&lAre you sure?")
    public static String GUI_MAKE_OWNER_WARNING_NAME;
    @Messages.Message(name = "GUIWarningYes", message = "&aYes")
    public static String GUI_YES;
    @Messages.Message(name = "GUIWarningNo", message = "&4No")
    public static String GUI_NO;
    @Messages.Message(name = "RegionTeleportMessage", message = "&7You have been teleported to %regionid%")
    public static String REGION_TELEPORT_MESSAGE;
    @Messages.Message(name = "NoFreeRegionWithThisKind", message = "&7No free region with this type found :(")
    public static String NO_FREE_REGION_WITH_THIS_KIND;
    @Messages.Message(name = "RegionkindDoesNotExist", message = "&4The selected regionkind does not exist!")
    public static String REGIONKIND_DOES_NOT_EXIST;
    @Messages.Message(name = "RegionkindgroupNotExists", message = "&4The selected regionkindgroup does not exist!")
    public static String REGIONKINDGROUP_NOT_EXISTS;
    @Messages.Message(name = "RegionkindgroupModified", message = "&aRegionkindGroup modified!")
    public static String REGIONKINDGROUP_MODIFIED;
    @Messages.Message(name = "RegionkindgroupListHeadline", message = "&6Regionkindgroups:")
    public static String REGIONKINDGROUP_LIST_HEADLINE;
    @Messages.Message(name = "RegionkindgroupAlreadyExists", message = "&4RegionKindGroup already exists!")
    public static String REGIONKINDGROUP_ALREADY_EXISTS;
    @Messages.Message(name = "RegionkindgroupCreated", message = "&aRegionKindgroup created!")
    public static String REGIONKINDGROUP_CREATED;
    @Messages.Message(name = "RegionkindgroupDeleted", message = "&aRegionkindgroup deleted!")
    public static String REGIONKINDGROUP_DELETED;
    @Messages.Message(name = "RegionkindgroupHelpHeadline", message = "&6==[AdvancedRegionMarket RegionkindGroup Help]==\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String REGIONKINDGROUP_HELP_HEADLINE;
    @Messages.Message(name = "RegionkindgroupInfo", message = {"&6======[RegionkindGroup Info]======",
            "&9Name: &e%regionkindgroup%",
            "&9DisplayName: &r%regionkindgroupdisplay%",
            "&9Display in limits: &e%regionkindgroupdisplayinlimits%",
            "&9Member Regionkinds: &e%regionkindgroupmembers%"})
    public static List<String> REGIONKINDGROUP_INFO;
    @Messages.Message(name = "RegionNowAvailable", message = "&aRegion is now available!")
    public static String REGION_NOW_AVAILABLE;
    @Messages.Message(name = "NoRegionAtPlayersPosition", message = "&7Could not find a region at your position!")
    public static String NO_REGION_AT_PLAYERS_POSITION;
    @Messages.Message(name = "RegionAddMemberNotOnline", message = "&4The selected player is not online!")
    public static String REGION_ADD_MEMBER_NOT_ONLINE;
    @Messages.Message(name = "RegionAddMemberAdded", message = "&aMember has been added!")
    public static String REGION_ADD_MEMBER_ADDED;
    @Messages.Message(name = "RegionRemoveMemberNotAMember", message = "&4The selected player is not a member of the region")
    public static String REGION_REMOVE_MEMBER_NOT_A_MEMBER;
    @Messages.Message(name = "RegionRemoveMemberRemoved", message = "&aMember has been removed!")
    public static String REGION_REMOVE_MEMBER_REMOVED;
    @Messages.Message(name = "GUIResetRegionButton", message = "&4Reset region")
    public static String GUI_RESET_REGION_BUTTON;
    @Messages.Message(name = "GUIResetRegionWarningName", message = "&4&lReset your region?")
    public static String GUI_RESET_REGION_WARNING_NAME;
    @Messages.Message(name = "ResetComplete", message = "&aReset complete!")
    public static String RESET_COMPLETE;
    @Messages.Message(name = "ResetRegionCooldownError", message = "&7You have to wait&6 %remaininguserrestorecooldown-countdown-writtenout% &7till you can reset your region again")
    public static String RESET_REGION_COOLDOWN_ERROR;
    @Messages.Message(name = "GUIRegionTakeOverMenuName", message = "&4Region take-over")
    public static String GUI_TAKEOVER_MENU_NAME;
    @Messages.Message(name = "RegionTransferCompleteMessage", message = "&aTransfer complete!")
    public static String REGION_TRANSFER_COMPLETE_MESSAGE;
    @Messages.Message(name = "GUICloseWindow", message = "&6Close window")
    public static String GUI_CLOSE;
    @Messages.Message(name = "RentSign1", message = "&2For Rent")
    public static String RENT_SIGN1;
    @Messages.Message(name = "RentSign2", message = "%regionid%")
    public static String RENT_SIGN2;
    @Messages.Message(name = "RentSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENT_SIGN3;
    @Messages.Message(name = "RentSign4", version = 5, message = "Max.: %maxextendtime-writtenout%")
    public static String RENT_SIGN4;
    @Messages.Message(name = "RentedSign1", message = "&4Rented")
    public static String RENTED_SIGN1;
    @Messages.Message(name = "RentedSign2", message = "%regionid%/%owner%")
    public static String RENTED_SIGN2;
    @Messages.Message(name = "RentedSign3", message = "%price%%currency%/%extendtime-short%")
    public static String RENTED_SIGN3;
    @Messages.Message(name = "RentedSign4", message = "%remainingtime-countdown-short%")
    public static String RENTED_SIGN4;
    @Messages.Message(name = "RentExtendMessage", version = 4, message = "&aRegion extended for &6%extendtime-current-writtenout%&a (For %price-current%%currency%. Remaining time: &6%remainingtime-countdown-short%")
    public static String RENT_EXTEND_MESSAGE;
    @Messages.Message(name = "GUIExtendRentRegionButton", message = "&1Extend region")
    public static String GUI_EXTEND_BUTTON;
    @Messages.Message(name = "Complete", message = "&aComplete!")
    public static String COMPLETE;
    @Messages.Message(name = "RegionBuyOutOfLimit", version = 2, message = "&4Out of Limit! You can see your current limits by executing &6/arm limit&4!")
    public static String REGION_BUY_OUT_OF_LIMIT;
    @Messages.Message(name = "RegionErrorCanNotBuildHere", message = "&4You are only allowed to break blocks you placed here!")
    public static String REGION_ERROR_CAN_NOT_BUILD_HERE;
    @Messages.Message(name = "Unlimited", message = "∞")
    public static String UNLIMITED;
    @Messages.Message(name = "GUIUserSellButton", message = "&4Reset and sell Region")
    public static String GUI_USER_SELL_BUTTON;
    @Messages.Message(name = "GUIUserSellWarning", message = "&4&lSell your region?")
    public static String GUI_USER_SELL_WARNING;
    @Messages.Message(name = "LimitInfoTop", message = "&6=========[Limit Info]=========")
    public static String LIMIT_INFO_TOP;
    @Messages.Message(name = "LimitInfoFreePluginVersionDisclaimer", message = "&cThis server uses the free version of AdvancedRegionMarket. Therefore the number of regions a player can own has been set to 1!")
    public static String LIMIT_INTO_FREE_PLUGIN_VERSION_DISCLAIMER;
    @Messages.Message(name = "LimitInfoLimitReachedColorCode", message = "&r&4")
    public static String LIMIT_REACHED_COLOR_CODE;
    @Messages.Message(name = "LimitInfoTotal", version = 2, message = "&6- Total: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_TOTAL;
    @Messages.Message(name = "LimitInfoRegionkind", version = 2, message = "&6- %regionkinddisplay%: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_REGIONKIND;
    @Messages.Message(name = "LimitInfoRegionkindGroup", version = 2, message = "&6- %regionkindgroupdisplay%: (&a%limitreachedcolor%%playerownedkind%/%limitkind%&6)")
    public static String LIMIT_INFO_REGIONKINDGROUP;
    @Messages.Message(name = "GUILimitButton", message = "&6My limits")
    public static String GUI_MY_LIMITS_BUTTON;
    @Messages.Message(name = "MemberlistInfo", message = "&6How to become a Member:")
    public static String GUI_MEMBER_INFO_ITEM;
    @Messages.Message(name = "AddMemberMaxMembersExceeded", message = "&4Cloud not add member to region! You can only have %maxmembers% members on this region!")
    public static String ADD_MEMBER_MAX_MEMBERS_EXCEEDED;
    @Messages.Message(name = "RegionIsNotARentregion", message = "&4Region is not a rentregion!")
    public static String REGION_IS_NOT_A_RENTREGION;
    @Messages.Message(name = "RegionIsNotARentOrContractregion", message = "&4Region is not a rent or contractregion!")
    public static String REGION_IS_NOT_A_RENT_OR_CONTRACTREGION;
    @Messages.Message(name = "RegionTimeAdded", message = "&aRegion extended! New expiration-date: %remainingtime-date%")
    public static String REGION_TIME_ADDED;
    @Messages.Message(name = "RegionTimeHalted", message = "&aCountdown stopped for all rent- and contract-regions")
    public static String TIME_HALTED;
    @Messages.Message(name = "RegionTimeContinuing", message = "&aCountdown continues for all rent- and contract-regions")
    public static String TIME_CONTINUING;
    @Messages.Message(name = "RegionNotOwn", message = "&4You do not own this region!")
    public static String REGION_NOT_OWN;
    @Messages.Message(name = "RegionNotSold", message = "&4Region not sold!")
    public static String REGION_NOT_SOLD;
    @Messages.Message(name = "PresetRemoved", message = "&aPreset removed!")
    public static String PRESET_REMOVED;
    @Messages.Message(name = "PresetSet", message = "&aPreset set!")
    public static String PRESET_SET;
    @Messages.Message(name = "PresetSaved", message = "&aPreset saved!")
    public static String PRESET_SAVED;
    @Messages.Message(name = "PresetAlreadyExists", message = "&4A preset with this name already exists!")
    public static String PRESET_ALREADY_EXISTS;
    @Messages.Message(name = "PresetPlayerDontHasPreset", message = "&4You do not have a preset loaded!")
    public static String PRESET_PLAYER_DONT_HAS_PRESET;
    @Messages.Message(name = "PresetDeleted", message = "&aPreset deleted!")
    public static String PRESET_DELETED;
    @Messages.Message(name = "PresetNotFound", message = "&4No preset with this name found!")
    public static String PRESET_NOT_FOUND;
    @Messages.Message(name = "PresetLoaded", message = "&aPreset loaded!")
    public static String PRESET_LOADED;
    @Messages.Message(name = "GUIRegionItemName", message = "%regionid% (%regionkinddisplay%)")
    public static String GUI_REGION_ITEM_NAME;
    @Messages.Message(name = "GUIRegionFinderRegionKindName", message = "&a&l%regionkinddisplay%")
    public static String GUI_REGIONFINDER_REGIONKIND_NAME;
    @Messages.Message(name = "RentRegionExpirationWarning", version = 4, message = "&4&lWARNING! &r&4Your RentRegion &6%regionid% &4will expire in about &6%remainingtime-countdown-writtenout%&4!")
    public static String RENTREGION_EXPIRATION_WARNING;
    @Messages.Message(name = "ContractSign1", message = "&2Contract")
    public static String CONTRACT_SIGN1;
    @Messages.Message(name = "ContractSign2", message = "&2available")
    public static String CONTRACT_SIGN2;
    @Messages.Message(name = "ContractSign3", message = "%regionid%")
    public static String CONTRACT_SIGN3;
    @Messages.Message(name = "ContractSign4", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SIGN4;
    @Messages.Message(name = "ContractSoldSign1", message = "&4Contract in use")
    public static String CONTRACT_SOLD_SIGN1;
    @Messages.Message(name = "ContractSoldSign2", message = "%regionid%/%owner%")
    public static String CONTRACT_SOLD_SIGN2;
    @Messages.Message(name = "ContractSoldSign3", message = "%price%%currency%/%extendtime-short%")
    public static String CONTRACT_SOLD_SIGN3;
    @Messages.Message(name = "ContractSoldSign4", message = "%remainingtime-countdown-short%")
    public static String CONTRACT_SOLD_SIGN4;
    @Messages.Message(name = "ContractRegionExtended", message = "&aYour contract region %regionid% has been extended for %extendtime-writtenout%. (For %price%%currency%.)")
    public static String CONTRACT_REGION_EXTENDED;
    @Messages.Message(name = "GUIContractItem", message = "&6Manage contract")
    public static String GUI_CONTRACT_ITEM;
    @Messages.Message(name = "ContractRegionStatusActive", message = "&aActive")
    public static String CONTRACT_REGION_STATUS_ACTIVE;
    @Messages.Message(name = "ContractRegionStatusTerminated", message = "&4Terminated")
    public static String CONTRACT_REGION_STATUS_TERMINATED;
    @Messages.Message(name = "RegionIsNotAContractRegion", message = "&4Region is not a contractregion!")
    public static String REGION_IS_NOT_A_CONTRACT_REGION;
    @Messages.Message(name = "OwnerMemberlistInfo", message = "&6Adding members:")
    public static String GUI_OWNER_MEMBER_INFO_ITEM;
    @Messages.Message(name = "RegiontransferMemberNotOnline", message = "&4Member not online!")
    public static String REGION_TRANSFER_MEMBER_NOT_ONLINE;
    @Messages.Message(name = "RegiontransferLimitError", message = "&4Transfer aborted! (Region would exceed players limit)")
    public static String REGION_TRANSFER_LIMIT_ERROR;
    @Messages.Message(name = "SecondsSingular", message = "second")
    public static String TIME_SECONDS_SINGULAR;
    @Messages.Message(name = "MinutesSingular", message = "minute")
    public static String TIME_MINUTES_SINGULAR;
    @Messages.Message(name = "HoursSingular", message = "hour")
    public static String TIME_HOURS_SINGULAR;
    @Messages.Message(name = "DaysSingular", message = "day")
    public static String TIME_DAYS_SINGULAR;
    @Messages.Message(name = "SecondsPlural", message = "seconds")
    public static String TIME_SECONDS_PLURAL;
    @Messages.Message(name = "MinutesPlural", message = "minutes")
    public static String TIME_MINUTES_PLURAL;
    @Messages.Message(name = "HoursPlural", message = "hours")
    public static String TIME_HOURS_PLURAL;
    @Messages.Message(name = "DaysPlural", message = "days")
    public static String TIME_DAYS_PLURAL;
    @Messages.Message(name = "SecondsShort", message = "s")
    public static String TIME_SECONDS_SHORT;
    @Messages.Message(name = "MinutesShort", message = "m")
    public static String TIME_MINUTES_SHORT;
    @Messages.Message(name = "HoursShort", message = "h")
    public static String TIME_HOURS_SHORT;
    @Messages.Message(name = "DaysShort", message = "d")
    public static String TIME_DAYS_SHORT;
    @Messages.Message(name = "TimeUnitSplitter", message = " and ")
    public static String TIME_UNIT_SPLITTER;
    @Messages.Message(name = "TimeUnitSplitterShort", message = ":")
    public static String TIME_UNIT_SPLITTER_SHORT;
    @Messages.Message(name = "UserNotAMemberOrOwner", message = "&4You are not a member or owner of this region!")
    public static String NOT_A_MEMBER_OR_OWNER;
    @Messages.Message(name = "RegionInfoYes", message = "&2yes")
    public static String YES;
    @Messages.Message(name = "RegionInfoNo", message = "&4no")
    public static String NO;
    @Messages.Message(name = "RegionInfoNever", message = "&4Never")
    public static String NEVER;
    @Messages.Message(name = "RegionAleadyRegistred", message = "&4Region already registered! &c(If you want to delete and re-add it. Delete it first with &6/arm delete [REGION-ID]&c!)")
    public static String REGION_ALREADY_REGISTERED;
    @Messages.Message(name = "UnknownUUID", message = "Unknown UUID")
    public static String UNKNOWN_UUID;
    @Messages.Message(name = "RegionStats", message = "&6=========[Region stats]=========")
    public static String REGION_STATS;
    @Messages.Message(name = "RegionStatsPattern", message = "&8Used regions (%regionkind%&8):")
    public static String REGION_STATS_PATTERN;
    @Messages.Message(name = "TeleporterNoSaveLocation", message = "&4Could not find a save teleport location")
    public static String TELEPORTER_NO_SAVE_LOCATION_FOUND;
    @Messages.Message(name = "TeleporterDontMove", message = "&6Teleportation will commence in &c%time% Seconds&6. Do not move!")
    public static String TELEPORTER_DONT_MOVE;
    @Messages.Message(name = "TeleporterTeleportationAborded", message = "&4Teleportation aborded!")
    public static String TELEPORTER_TELEPORTATION_ABORDED;
    @Messages.Message(name = "OfferSent", message = "&aYour offer has been sent")
    public static String OFFER_SENT;
    @Messages.Message(name = "OfferAcceptedSeller", message = "&a%buyer% &aaccepted your offer")
    public static String OFFER_ACCEPTED_SELLER;
    @Messages.Message(name = "OfferAcceptedBuyer", message = "&aOffer accepted! You are now the owner of &c%regionid%")
    public static String OFFER_ACCEPTED_BUYER;
    @Messages.Message(name = "NoOfferToAnswer", message = "&4You dont have an offer to answer")
    public static String NO_OFFER_TO_ANSWER;
    @Messages.Message(name = "OfferRejected", message = "&aOffer rejected!")
    public static String OFFER_REJECTED;
    @Messages.Message(name = "OfferHasBeenRejected", message = "&4%buyer% &4rejected your offer!")
    public static String OFFER_HAS_BEEN_REJECTED;
    @Messages.Message(name = "NoOfferToReject", message = "&4You do not have an offer to reject")
    public static String NO_OFFER_TO_REJECT;
    @Messages.Message(name = "OfferCancelled", message = "&aYour offer has been cancelled!")
    public static String OFFER_CANCELED;
    @Messages.Message(name = "OfferHasBeenCancelled", message = "&4%seller% &4cancelled his offer!")
    public static String OFFER_HAS_BEEN_CANCELLED;
    @Messages.Message(name = "NoOfferToCancel", message = "&4You do not have an offer to cancel")
    public static String NO_OFFER_TO_CANCEL;
    @Messages.Message(name = "BuyerAlreadyGotAnOffer", message = "&4The selected buyer already got an offer that he has to answer first!")
    public static String BUYER_ALREADY_GOT_AN_OFFER;
    @Messages.Message(name = "SellerAlreadyCreatedAnOffer", message = "&4You have already created an offer! Please wait for an answer or cancel it first!")
    public static String SELLER_ALREADY_CREATED_AN_OFFER;
    @Messages.Message(name = "SellerDoesNotLongerOwnRegion", message = "&4%seller% &4does not longer own this region. His offer has been cancelled")
    public static String SELLER_DOES_NOT_LONGER_OWN_REGION;
    @Messages.Message(name = "IncomingOffer", message = "&c%seller% &6offers you his region &c%regionid% &6in the world &c%world% &6for &c%price%%currency%&6! You can accept his offer with &c/arm offer accept &6or reject it &c/arm offer reject")
    public static String INCOMING_OFFER;
    @Messages.Message(name = "SelectedPlayerIsNotOnline", message = "&4The selected player is not online")
    public static String SELECTED_PLAYER_NOT_ONLINE;
    @Messages.Message(name = "OfferTimedOut", message = "&4Offer timed out!")
    public static String OFFER_TIMED_OUT;
    @Messages.Message(name = "BadSyntax", message = "&7Bad syntax! Please use: &8%command%")
    public static String BAD_SYNTAX;
    @Messages.Message(name = "LandlordServer", message = "Server")
    public static String LANDLORD_SERVER;
    @Messages.Message(name = "BadSyntaxSplitter", message = "\n&7or &8%command%")
    public static String BAD_SYNTAX_SPLITTER;
    @Messages.Message(name = "HelpHeadline", message = "&6=====[AdvancedRegionMarket Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String HELP_HEADLINE;
    @Messages.Message(name = "PresetInfoSellregion", version = 5, message = {"&6=========[Preset Info]=========",
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
    @Messages.Message(name = "PresetInfoContractregion", version = 5, message = {"&6=========[Preset Info]=========",
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
    @Messages.Message(name = "PresetInfoRentregion", version = 5, message = {"&6=========[Preset Info]=========",
            "&9Autoprice: &e%presetautoprice%",
            "&9Price: &e%presetprice%",
            "&9ExtendTime: &e%extendtime%",
            "&9MaxExtendTime: &e%maxextendtime%",
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
    @Messages.Message(name = "NotDefined", message = "&8&onot defined")
    public static String NOT_DEFINED;
    @Messages.Message(name = "PriceCanNotBeNegative", message = "&4Price can not be negative!")
    public static String PRICE_CAN_NOT_BE_NEGATIVE;
    @Messages.Message(name = "SellBackWarning", message = "&4Sell region back to the server:\n" +
            "&4WARNING: &cThis can not be undone! \n" +
            "Your region &6%regionid% &cwill be released and all blocks on it\n" +
            "will be resetted! You and all members will loose their rights on it.\n" +
            "You will get &6%paybackmoney%%currency% &cback")
    public static String SELLBACK_WARNING;
    @Messages.Message(name = "SubregionProtectionOfContinuanceError", message = "&4The selected region is a subregion! Protection of continuance can't be activated")
    public static String SUBREGION_PROTECTION_OF_CONTINUANCE_ERROR;
    @Messages.Message(name = "SubregionInactivityResetError", message = "&4The selected region is a subregion. You can change the InactivityReset setting for all subregions in the config.yml!")
    public static String SUBREGION_INACTIVITYRESET_ERROR;
    @Messages.Message(name = "SubregionAutoRestoreError", message = "&4The selected region is a subregion. You can change the autoRestore setting for all subregions in the config.yml!")
    public static String SUBREGION_AUTORESTORE_ERROR;
    @Messages.Message(name = "SubregionSetLandlordError", message = "&4The selected region is a subregion. The landlord is always the owner of the surrounding region!")
    public static String SUBREGION_LANDLORD_ERROR;
    @Messages.Message(name = "RegionNotRestoreable", message = "&4Region not restorable!")
    public static String REGION_NOT_RESTORABLE;
    @Messages.Message(name = "RegionSelectedMultipleRegions", message = "&6There is more than one region at your position. Please select one: &4")
    public static String REGION_SELECTED_MULTIPLE_REGIONS;
    @Messages.Message(name = "SubregionRegionkindError", message = "&4The selected region is a subregion. You can edit the regionkind for all subregions in the config.yml!")
    public static String SUBREGION_REGIONKIND_ERROR;
    @Messages.Message(name = "SubRegionRegionkindOnlyForSubregions", message = "&4Subregion regionkind only for subregions!")
    public static String SUBREGION_REGIONKIND_ONLY_FOR_SUBREGIONS;
    @Messages.Message(name = "SubregionTeleportLocationError", message = "&4The selected region is a subregion. Teleport location can not be changed")
    public static String SUBREGION_TELEPORT_LOCATION_ERROR;
    @Messages.Message(name = "RegionNotRegistred", message = "&4Region not registred")
    public static String REGION_NOT_REGISTRED;
    @Messages.Message(name = "FirstPositionSet", message = "&aFirst position set!")
    public static String FIRST_POSITION_SET;
    @Messages.Message(name = "SecondPositionSet", message = "&aSecond position set!")
    public static String SECOND_POSITION_SET;
    @Messages.Message(name = "MarkInOtherRegion", message = "&4Mark in other Region. Removing old mark")
    public static String MARK_IN_OTHER_REGION_REMOVING;
    @Messages.Message(name = "ParentRegionNotOwn", message = "&4You don not own the parent region!")
    public static String PARENT_REGION_NOT_OWN;
    @Messages.Message(name = "SubRegionRemoveNoPermissionBecauseSold", message = "&4You are not allowed to remove this region. Please ask an admin if you believe this is an error")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_SOLD;
    @Messages.Message(name = "SubRegionRemoveNoPermissionBecauseAvailable", message = "&4You are not allowed to remove this region, because it is sold. You may ask the owner or an admin to release it")
    public static String NOT_ALLOWED_TO_REMOVE_SUBREGION_AVAILABLE;
    @Messages.Message(name = "PosCloudNotBeSetMarkOutsideRegion", message = "&4Position could not be set! Position outside region")
    public static String POSITION_CLOUD_NOT_BE_SET_MARK_OUTSIDE_REGION;
    @Messages.Message(name = "SubRegionAlreadyAtThisPosition", message = "&4Your selection would overlap with a subregion that already has been created")
    public static String ALREADY_SUBREGION_AT_THIS_POSITION;
    @Messages.Message(name = "SubRegionLimitReached", message = "&4Subregion limit reached! You are not allowed to create more than &6%subregionlimit% &4subregions")
    public static String SUBREGION_LIMIT_REACHED;
    @Messages.Message(name = "SelectionInvalid", message = "&4Selection invalid! You need to select 2 positions! (Left/Right click) Type \"&6/arm subregion tool&4\" to get the selection tool")
    public static String SELECTION_INVALID;
    @Messages.Message(name = "RegionCreatedAndSaved", message = "&aRegion created and saved!")
    public static String REGION_CREATED_AND_SAVED;
    @Messages.Message(name = "RegionNotASubregion", message = "&4Region not a subregion!")
    public static String REGION_NOT_A_SUBREGION;
    @Messages.Message(name = "RegionDeleted", message = "&aRegion deleted!")
    public static String REGION_DELETED;
    @Messages.Message(name = "DeleteRegionWarningName", message = "&4&lDelete region?")
    public static String DELETE_REGION_WARNING_NAME;
    @Messages.Message(name = "UnsellRegionButton", message = "&4Unsell region")
    public static String UNSELL_REGION_BUTTON;
    @Messages.Message(name = "UnsellRegionButtonLore", message = {"&4Click to unsell your subregion and",
            "&4kick the players of it"})
    public static List<String> UNSELL_REGION_BUTTON_LORE;
    @Messages.Message(name = "UnsellRegionWarningName", message = "&4&lUnsell region?")
    public static String UNSELL_REGION_WARNING_NAME;
    @Messages.Message(name = "SubregionHelpHeadline", message = "&6=====[AdvancedRegionMarket Subregion Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SUBREGION_HELP_HEADLINE;
    @Messages.Message(name = "SellbackLandlordNotEnoughMoney", message = "&4The landlord of this region doesn't have enough money! If you want to give the region back without getting money back use &7/arm sellback %region% nomoney&4!")
    public static String SELLBACK_LANDLORD_NOT_ENOUGH_MONEY;
    @Messages.Message(name = "SellregionName", message = "Sellregion")
    public static String SELLREGION_NAME;
    @Messages.Message(name = "ContractregionName", message = "Contractregion")
    public static String CONTRACTREGION_NAME;
    @Messages.Message(name = "RentregionName", message = "Rentregion")
    public static String RENTREGION_NAME;
    @Messages.Message(name = "GUISubregionsButton", message = "&6Subregions")
    public static String GUI_SUBREGION_ITEM_BUTTON;
    @Messages.Message(name = "GUISubregionListMenuName", message = "&1Subregions")
    public static String GUI_SUBREGION_LIST_MENU_NAME;
    @Messages.Message(name = "GUIHotelButton", message = "&6Hotel-function")
    public static String GUI_SUBREGION_HOTEL_BUTTON;
    @Messages.Message(name = "GUIDeleteRegionButton", message = "&4Delete region")
    public static String GUI_SUBREGION_DELETE_REGION_BUTTON;
    @Messages.Message(name = "GUITeleportToSignOrRegionButton", message = "Teleport to sign or region?")
    public static String GUI_TELEPORT_TO_SIGN_OR_REGION;
    @Messages.Message(name = "GUIRegionfinderTeleportToSignButton", message = "&6Teleport to buy sign!")
    public static String GUI_TELEPORT_TO_SIGN;
    @Messages.Message(name = "GUIRegionfinderTeleportToRegionButton", message = "&6Teleport to region!")
    public static String GUI_TELEPORT_TO_REGION;
    @Messages.Message(name = "GUINextPageButton", message = "&6Next page")
    public static String GUI_NEXT_PAGE;
    @Messages.Message(name = "GUIPrevPageButton", message = "&6Prev page")
    public static String GUI_PREV_PAGE;
    @Messages.Message(name = "Enabled", message = "&aenabled")
    public static String ENABLED;
    @Messages.Message(name = "Disabled", message = "&cdisabled")
    public static String DISABLED;
    @Messages.Message(name = "Sold", message = "&csold")
    public static String SOLD;
    @Messages.Message(name = "Available", message = "&aavailable")
    public static String AVAILABLE;
    @Messages.Message(name = "SubregionIsUserResettableError", message = "&4The selected region is a subregion. You can change the isUserResettable setting for all subregions in the config.yml!")
    public static String SUBREGION_IS_USER_RESETTABLE_ERROR;
    @Messages.Message(name = "SubregionMaxMembersError", message = "&4The selected region is a subregion. You can change the maxMember setting for all subregions in the config.yml!")
    public static String SUBREGION_MAX_MEMBERS_ERROR;
    @Messages.Message(name = "GUIHotelButtonLore", message = {"&6The hotel function allows you to prevent players",
            "&6from breaking blocks they do not have placed",
            "&6Status: %hotelfunctionstatus%",
            "&6Click to enable/disable"})
    public static List<String> GUI_SUBREGION_HOTEL_BUTTON_LORE;
    @Messages.Message(name = "GUISubregionInfoSell", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_SELL;
    @Messages.Message(name = "GUISubregionInfoRent", version = 5, message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxextendtime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_RENT;
    @Messages.Message(name = "GUISubregionInfoContract", message = {"&6Selltype: %selltype%",
            "&6Status: %soldstatus%",
            "&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%"})
    public static List<String> GUI_SUBREGION_REGION_INFO_CONTRACT;
    @Messages.Message(name = "GUIRegionfinderInfoSell", message = {"&6Price: %price%",
            "&6Price per M2: %priceperm2%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_SELL;
    @Messages.Message(name = "GUIRegionfinderInfoRent", version = 5, message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Extend per click: %extendtime-writtenout%",
            "&6Max. extended time: %maxextendtime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_RENT;
    @Messages.Message(name = "GUIRegionfinderInfoContract", message = {"&6Price: %price%",
            "&6Price per M2 (per week): %priceperm2perweek%",
            "&6Automatic extend time: %extendtime-writtenout%",
            "&6Dimensions: %dimensions%",
            "&6World: %world%"})
    public static List<String> GUI_REGIONFINDER_REGION_INFO_CONTRACT;
    @Messages.Message(name = "SubregionCreationCreateSignInfo", message = {"&aYour selection has been saved! You can now create a sign to sell the region.",
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
    @Messages.Message(name = "SubregionCreationSelectAreaInfo", message = {"&aYou got a tool in your inventory (feather) to select 2 points of your region that will mark the corners of your new subregion.",
            "&aLeft click to select pos1",
            "&aRight click to select pos2",
            "&aType \"&6/arm subregion create\" &aif you are done"})
    public static List<String> SUBREGION_TOOL_INSTRUCTION;
    @Messages.Message(name = "SubregionToolAlreadyOwned", message = "&4You already own a Subregion Tool. Please use that instead of a new one!")
    public static String SUBREGION_TOOL_ALREADY_OWNED;
    @Messages.Message(name = "AutopriceList", message = "&6=========[Autoprices]=========")
    public static String AUTOPRICE_LIST;
    @Messages.Message(name = "GUISubregionManagerNoSubregionItem", message = "&6Info")
    public static String GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM;
    @Messages.Message(name = "SelltypeNotExist", message = "&4The selected selltype does not exist!")
    public static String SELLTYPE_NOT_EXIST;
    @Messages.Message(name = "SignLinkModeActivated", message = "&aSign-Link-Mode activated! Click into a region and afterwards click on a sign. ARM will automatically create a region (or will just add the sign if the region already exists) with the settings of your preset")
    public static String SIGN_LINK_MODE_ACTIVATED;
    @Messages.Message(name = "SignLinkModeDeactivated", message = "&aSign-Link-Mode deactivated!")
    public static String SIGN_LINK_MODE_DEACTIVATED;
    @Messages.Message(name = "SignLinkModeAlreadyDeactivated", message = "&4Sign-Link-Mode is already deactivated!")
    public static String SIGN_LINK_MODE_ALREADY_DEACTIVATED;
    @Messages.Message(name = "SignLinkModePresetNotPriceready", message = "&cThe selected preset is not price-ready! All regions you will create now will be created with the default autoprice")
    public static String SIGN_LINK_MODE_PRESET_NOT_PRICEREADY;
    @Messages.Message(name = "SignLinkModeNoPresetSelected", message = "&cYou dont have a preset loaded! Please load or create a preset first! &cYou can create a preset by using the &6/arm sellpreset/rentpreset/contractpreset &ccommands!\n" +
            "For more &cinformation about presets click here:\n" +
            "&6https://bit.ly/2HURK0v (Github Wiki)")
    public static String SIGN_LINK_MODE_NO_PRESET_SELECTED;
    @Messages.Message(name = "SignLinkModeSignBelongsToAnotherRegion", message = "&4Sign belongs to another region!")
    public static String SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION;
    @Messages.Message(name = "SignLinkModeSignSelected", message = "&aSign selected!")
    public static String SIGN_LINK_MODE_SIGN_SELECTED;
    @Messages.Message(name = "SignLinkModeMultipleWgRegionsAtPosition", message = "&4Could not select WorldGuard-Region!" +
            "There is more than one region available! You can add unwanted regions to the ignore-list" +
            "located in the config.yml, if you want ARM to ignore that regions!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS;
    @Messages.Message(name = "SignLinkModeNoWgRegionAtPosition", message = "&4Could not select WorldGuard-Region! There is no region at your position!")
    public static String SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION;
    @Messages.Message(name = "SignLinkModeCouldNotIdentifyWorld", message = "&4Could not identify world! Please select the WorldGuard-Region again!")
    public static String SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD;
    @Messages.Message(name = "SignLinkModeNoSignSelected", message = "&4You have not selected a sign")
    public static String SIGN_LINK_MODE_NO_SIGN_SELECTED;
    @Messages.Message(name = "SignLinkModeNoWgRegionSelected", message = "&4You have not selected a WorldGuard-Region")
    public static String SIGN_LINK_MODE_NO_WG_REGION_SELECTED;
    @Messages.Message(name = "SignLinkModeSelectedRegion", message = "&aSelected region: %regionid%")
    public static String SIGN_LINK_MODE_REGION_SELECTED;
    @Messages.Message(name = "SchematicNotFoundErrorUser", message = "&4It seems like the schematic of your region %regionid% has not been created. Please contact an admin!")
    public static String SCHEMATIC_NOT_FOUND_ERROR_USER;
    @Messages.Message(name = "EntityLimitHelpHeadline", message = "&6=====[AdvancedRegionMarket EntityLimit Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String ENTITYLIMIT_HELP_HEADLINE;
    @Messages.Message(name = "EntityLimitGroupNotExist", message = "&4EntityLimitGroup does not exist!")
    public static String ENTITYLIMITGROUP_DOES_NOT_EXIST;
    @Messages.Message(name = "EntityLimitSet", message = "&aEntityLimit has been set!")
    public static String ENTITYLIMIT_SET;
    @Messages.Message(name = "EntityLimitRemoved", message = "&aEntityLimit has been removed!")
    public static String ENTITYLIMIT_REMOVED;
    @Messages.Message(name = "EntityTypeDoesNotExist", message = "&4The entitytype &6%entitytype% &4does not exist!")
    public static String ENTITYTYPE_DOES_NOT_EXIST;
    @Messages.Message(name = "EntityLimitGroupNotContainEntityLimit", message = "&4The selected EntityLimitGroup does not contain the selected EntityType")
    public static String ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT;
    @Messages.Message(name = "EntityLimitTotal", message = "Total")
    public static String ENTITYLIMIT_TOTAL;
    @Messages.Message(name = "SubregionPaybackPercentageError", message = "&4The selected region is a subregion. You can edit the paybackPercentage for all subregions in the config.yml!")
    public static String SUBREGION_PAYBACKPERCENTAGE_ERROR;
    @Messages.Message(name = "EntityLimitCheckHeadline", message = "&6===[EntityLimitCheck for %regionid%]===")
    public static String ENTITYLIMIT_CHECK_HEADLINE;
    @Messages.Message(name = "EntityLimitCheckPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String ENTITYLIMIT_CHECK_PATTERN;
    @Messages.Message(name = "EntityLimitCheckExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMIT_CHECK_EXTENSION_INFO;
    @Messages.Message(name = "EntityLimitGroupAlreadyExists", message = "&4Group already exists!")
    public static String ENTITYLIMITGROUP_ALREADY_EXISTS;
    @Messages.Message(name = "EntityLimitGroupCreated", message = "&aEntitylimitgroup has been created!")
    public static String ENTITYLIMITGROUP_CREATED;
    @Messages.Message(name = "EntityLimitGroupCanNotRemoveSystem", message = "&4You can not remove a system-EntityLimitGroup!")
    public static String ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM;
    @Messages.Message(name = "EntityLimitGroupDeleted", message = "&aEntitylimitgroup has been deleted!")
    public static String ENTITYLIMITGROUP_DELETED;
    @Messages.Message(name = "EntityLimitGroupInfoHeadline", message = "&6======[Entitylimitgroup Info]======")
    public static String ENTITYLIMITGROUP_INFO_HEADLINE;
    @Messages.Message(name = "EntityLimitGroupInfoGroupname", message = "&6Groupname: ")
    public static String ENTITYLIMITGROUP_INFO_GROUPNAME;
    @Messages.Message(name = "EntityLimitGroupInfoPattern", message = "&6%entitytype%: &r%softlimitentities% %entityextensioninfo%")
    public static String ENTITYLIMITGROUP_INFO_PATTERN;
    @Messages.Message(name = "EntityLimitInfoExtensionInfo", message = "\n&6&o--> Limit extendable up to &r%hardlimitentities% &6entities for &r%priceperextraentity%%currency%&6/entity")
    public static String ENTITYLIMITGROUP_INFO_EXTENSION_INFO;
    @Messages.Message(name = "EntityLimitGroupListHeadline", message = "&6EntityLimitGroups:")
    public static String ENTITYLIMITGROUP_LIST_HEADLINE;
    @Messages.Message(name = "SubregionEntityLimitOnlyForSubregions", message = "&4SubregionEntityLimitGroup only for subregions")
    public static String ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS;
    @Messages.Message(name = "MassactionSplitter", message = "&6all regions with regionkind &a%regionkind%")
    public static String MASSACTION_SPLITTER;
    @Messages.Message(name = "SubregionEntityLimitError", message = "&4Could not change EntiyLimitGroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_ENTITYLIMITGROUP_ERROR;
    @Messages.Message(name = "SubregionFlagGroupError", message = "&4Could not change Flaggroup for the region &6%regionid%&4! Region is a Subregion!")
    public static String SUBREGION_FLAGGROUP_ERROR;
    @Messages.Message(name = "GUIEntityLimitItemButton", message = "&6EntityLimits")
    public static String GUI_ENTITYLIMIT_ITEM_BUTTON;
    @Messages.Message(name = "GUIEntityLimitItemLore", message = {"&6Click to display the entity-limits",
            "&6for this region in chat",
            "%entityinfopattern%",
            "",
            "You can expand your entity-limit with:",
            "&6/arm entitylimit buyextra %regionid% [ENTITYTYPE/total]"})
    public static List<String> GUI_ENTITYLIMIT_ITEM_LORE;
    @Messages.Message(name = "GUIEntityLimitInfoPattern", message = "&6%entitytype%: &a(&r%actualentities%&a/&r%softlimitentities%&a) %entityextensioninfo%")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_PATTERN;
    @Messages.Message(name = "GUIEntityLimitInfoExtensionInfo", message = "&6&oMax. &r%hardlimitentities% &6for &r%priceperextraentity%%currency%&6/entity")
    public static String GUI_ENTITYLIMIT_ITEM_INFO_EXTENSION_INFO;
    @Messages.Message(name = "EntityLimitGroupEntityLimitAlreadyUnlimited", message = "&4EntityLimit for the selected entity and region is already unlimited!")
    public static String ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED;
    @Messages.Message(name = "EntityLimitGroupExtraEntitiesSet", message = "&aExtra-Entities have been set!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET;
    @Messages.Message(name = "EntityLimitGroupExtraEntitiesExpandSuccess", message = "&aYou have sucessfully expanded the entitylimit to &6%softlimitentities% &aentities! (For &6%priceperextraentity%%currency%&a)")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS;
    @Messages.Message(name = "EntityLimitGroupExtraEntitiesHardlimitReached", message = "&4Can not buy another entity-expansion! Hardlimit has been reached!")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED;
    @Messages.Message(name = "EntityLimitGroupExtraEntitiesSetSubregionError", message = "&4Can not change entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_SET_SUBREGION_ERROR;
    @Messages.Message(name = "EntityLimitGroupExtraEntitiesBuySubregionError", message = "&4Can not expand entitylimit! Region is a Subregion")
    public static String ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR;
    @Messages.Message(name = "ApplyPresetSubregionError", message = "&4Can not apply preset! Region is a subregion")
    public static String APPLY_PRESET_SUBREGION_ERROR;
    @Messages.Message(name = "EntityLimitGroupCouldNotspawnEntity", message = "&4Could not spawn entity on region &6%region%&4!\n" +
            "The not spawned entity would exceed the region\'s entitylimit. For more information type &6/arm entitylimit check %region%&4!\n" +
            "Everybody on region %region% received this message! If you are not a member of this region, you can ignore this message.")
    public static String ENTITYLIMITGROUP_COULD_NOT_SPAWN_ENTITY;
    @Messages.Message(name = "ArmBasicCommandMessagePro", message = "&6AdvancedRegionMarket &4&lPro&r &6v%pluginversion% by Alex9849\n" +
            "&6Download: &3https://bit.ly/2CfO3An\n" +
            "&6Get a list with all commands with &3/arm help")
    public static String ARM_BASIC_COMMAND_MESSAGE_PRO;
    @Messages.Message(name = "ArmBasicCommandMessageFree", message = "&6AdvancedRegionMarket &3&oFree&r &6v%pluginversion% by Alex9849\n" +
            "&cThis free version limits the maximum number of regions a player can own to 1!\n" +
            "&6Download: &3https://bit.ly/2CfO3An\n" +
            "&6Get a list with all commands with &3/arm help")
    public static String ARM_BASIC_COMMAND_MESSAGE_FREE;
    @Messages.Message(name = "RegionBuyProtectionOfContinuance", message = "&4Can't buy region! Region is unter protection of continuance and won't be for sale again!")
    public static String REGION_BUY_PROTECTION_OF_CONTINUANCE;
    @Messages.Message(name = "RegionResetProtectionOfContinuanceError", message = "&4Can't reset region! Region is unter protection of continuance!")
    public static String REGION_RESET_PROTECTION_OF_CONTINUANCE_ERROR;
    @Messages.Message(name = "RegionRestoreProtectionOfContinuanceError", message = "&4Can't restore region! Region is unter protection of continuance!")
    public static String REGION_RESTORE_PROTECTION_OF_CONTINUANCE_ERROR;
    @Messages.Message(name = "RegionKindCreated", message = "&aRegionKind created!")
    public static String REGIONKIND_CREATED;
    @Messages.Message(name = "RegionKindAlreadyExists", message = "&4RegionKind already exists!")
    public static String REGIONKIND_ALREADY_EXISTS;
    @Messages.Message(name = "RegionKindDeleted", message = "&aRegionKind deleted!")
    public static String REGIONKIND_DELETED;
    @Messages.Message(name = "RegionKindCanNotRemoveSystem", message = "&4You can not remove a system-RegionKind!")
    public static String REGIONKIND_CAN_NOT_REMOVE_SYSTEM;
    @Messages.Message(name = "RegionKindListHeadline", message = "&6Regionkinds:")
    public static String REGIONKIND_LIST_HEADLINE;
    @Messages.Message(name = "RegionKindModified", message = "&aRegionKind modified!")
    public static String REGIONKIND_MODIFIED;
    @Messages.Message(name = "MaterialNotFound", message = "&4Material not found!")
    public static String MATERIAL_NOT_FOUND;
    @Messages.Message(name = "RegionKindLoreLineNotExist", message = "&aThe selected lore-line does not exist!")
    public static String REGIONKIND_LORE_LINE_NOT_EXIST;
    @Messages.Message(name = "RegionKindCanNotBeSearchedInRegionfinder", message = "&4The selected regionkind isn't available in the regionfinder!")
    public static String REGIONKIND_CAN_NOT_BE_SEARCHED_IN_REGIONFINDER;
    @Messages.Message(name = "RegionkindInfo", message = {"&6======[Regionkind Info]======",
            "&9Name: &e%regionkind%",
            "&9DisplayName: &r%regionkinddisplay%",
            "&9Item: &e%regionkinditem%",
            "&9Display in limits: &e%regionkinddisplayinlimits%",
            "&9Display in regionfinder: &e%regionkinddisplayinregionfinder%",
            "&9In RegionkindGroups: &e%regionkindregionkindgroups%",
            "&9Lore:",
            "%regionkindlorelist%"}, version = 7)
    public static List<String> REGIONKIND_INFO;
    @Messages.Message(name = "RegionKindHelpHeadline", message = "&6=====[AdvancedRegionMarket RegionKind Help ]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String REGIONKIND_HELP_HEADLINE;
    @Messages.Message(name = "PlayerNotFound", message = "&4Could not find selected player!")
    public static String PLAYER_NOT_FOUND;
    @Messages.Message(name = "RegionInfoSellregionUser", version = 5, message = {"&6=========[Region Info]=========",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e&e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION;
    @Messages.Message(name = "RegionInfoRentregionUser", version = 5, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxextendtime-writtenout%",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION;
    @Messages.Message(name = "RegionInfoContractregionUser", version = 5, message = {"&6=========[Region Info]=========",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_CONTRACTREGION;
    @Messages.Message(name = "RegionInfoSellregionAdmin", version = 5, message = {"&6=========[Region Info]=========",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_SELLREGION_ADMIN;
    @Messages.Message(name = "RegionInfoRentregionAdmin", version = 5, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxextendtime-writtenout%",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_RENTREGION_ADMIN;
    @Messages.Message(name = "RegionInfoContractregionAdmin", version = 5, message = {"&6=========[Region Info]=========",
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
            "&9Protection of Continuance: &e%isprotectionofcontinuance%",
            "&9Allowed Subregions: &e%subregionlimit%",
            "&9Subregions: &e%subregions%"})
    public static List<String> REGION_INFO_CONTRACTREGION_ADMIN;
    @Messages.Message(name = "RegionInfoSellregionSubregion", version = 2, message = {"&6=========[Region Info]=========",
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
    @Messages.Message(name = "RegionInfoRentregionSubregion", version = 5, message = {"&6=========[Region Info]=========",
            "&9ID: &e%regionid% &7(Type: &r%selltype%&7, Subregion)",
            "&9Sold: &e%issold%",
            "&9Price: &e%price% &7per &e%extendtime-writtenout% &7max.: &e%maxextendtime-writtenout%",
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
    @Messages.Message(name = "RegionInfoContractregionSubregion", version = 2, message = {"&6=========[Region Info]=========",
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
    @Messages.Message(name = "GUIWarningInfoButton", message = "&4Warning!")
    public static String GUI_WARNING_INFO_BUTTON;
    @Messages.Message(name = "GUIFlageditorButton", message = "&6FlagEditor")
    public static String GUI_FLAGEDITOR_BUTTON;
    @Messages.Message(name = "GUIFlageditorMenuName", message = "&1FlagEditor (%region%)")
    public static String GUI_FLAGEDITOR_MENU_NAME;
    @Messages.Message(name = "GUIFlageditorDeleteFlagButton", message = "&4Delete flag")
    public static String GUI_FLAGEDITOR_DELETE_FLAG_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetFlagGroupAllButton", message = "&9Set for everyone")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_ALL_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetFlagGroupMembersButton", message = "&9Set for members and owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_MEMBERS_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetFlagGroupOwnersButton", message = "&9Set for owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_OWNERS_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetFlagGroupNonMembersButton", message = "&9Set for non members and non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_MEMBERS_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetFlagGroupNonOwnersButton", message = "&9Set for non owners")
    public static String GUI_FLAGEDITOR_SET_FLAG_GROUP_NON_OWNERS_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetStateflagAllowButton", message = "&2Allow")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_ALLOW_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetStateflagDenyButton", message = "&4Deny")
    public static String GUI_FLAGEDITOR_SET_STATEFLAG_DENY_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetBooleanflagTrueButton", message = "&2Yes")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_TRUE_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetBooleanflagFalseButton", message = "&4No")
    public static String GUI_FLAGEDITOR_SET_BOOLEANFLAG_FALSE_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetStringflagSetMessageButton", message = "&2Set message")
    public static String GUI_FLAGEDITOR_SET_STRINGFLAG_SET_MESSAGE_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetIntegerflagSetIntegerButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_INTEGERFLAG_SET_INTEGER_BUTTON;
    @Messages.Message(name = "GUIFlageditorSetDoubleflagSetDoubleButton", message = "&2Set number")
    public static String GUI_FLAGEDITOR_SET_DOUBLEFLAG_SET_DOUBLE_BUTTON;
    @Messages.Message(name = "FlageditorFlagNotActivated", message = "&4Flag not activated!")
    public static String FlAGEDITOR_FLAG_NOT_ACTIVATED;
    @Messages.Message(name = "FlageditorFlagHasBeenDeleted", message = "&2Flag has been deleted!")
    public static String FlAGEDITOR_FLAG_HAS_BEEN_DELETED;
    @Messages.Message(name = "FlageditorFlagHasBeenUpdated", message = "&2Flag has been updated!")
    public static String FLAGEDITOR_FLAG_HAS_BEEN_UPDATED;
    @Messages.Message(name = "FlageditorFlagCouldNotBeUpdated", message = "Could not modify flag %flag%!")
    public static String FLAGEDITOR_FLAG_COULD_NOT_BE_MODIFIED;
    @Messages.Message(name = "FlageditorStringflagSetMessageInfo", message = "&9Please write down a message:")
    public static String FLAGEDITOR_STRINGFLAG_SET_MESSAGE_INFO;
    @Messages.Message(name = "FlageditorIntegerflagSetMessageInfo", message = "&9Please write down a number that does not have decimals:")
    public static String FLAGEDITOR_INTEGERFLAG_SET_NUMBER_INFO;
    @Messages.Message(name = "FlageditorDoubleflagSetMessageInfo", message = "&9Please write down a number:")
    public static String FLAGEDITOR_DOUBLEFLAG_SET_NUMBER_INFO;
    @Messages.Message(name = "GUIFlageditorResetButton", message = "&4Reset all Flags to default settings")
    public static String GUI_FLAGEDITOR_RESET_BUTTON;
    @Messages.Message(name = "GUIFlageditorUnknownFlagSetPropertiesButton", message = "&2Set properties")
    public static String GUI_FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_BUTTON;
    @Messages.Message(name = "GUIFlageditorUnknownFlagSetPropertiesInfo", message = "&9Please write down your new flag properties: FlaggroupDoesNotExist: '&4Flaggroup does not exist!")
    public static String FLAGEDITOR_UNKNOWNFLAG_SET_PROPERTIES_INFO;
    @Messages.Message(name = "FlaggroupDoesNotExist", message = "&4Flaggroup does not exist!")
    public static String FLAGGROUP_DOES_NOT_EXIST;
    @Messages.Message(name = "SubregionFlaggroupOnlyForSubregions", message = "&4Subregion flaggroup only for subregions")
    public static String SUBREGION_FLAGGROUP_ONLY_FOR_SUBREGIONS;
    @Messages.Message(name = "GUITeleportToRegionButtonLore", message = {"Click to teleport you to",
            "your region"})
    public static List<String> GUI_TELEPORT_TO_REGION_BUTTON_LORE;
    @Messages.Message(name = "GUIMakeOwnerButtonLore", message = {"Click to transfer your owner rights",
            "to the selected member.",
            "&4WARNING: &cYou will lose your owner",
            "&crights and become a member"})
    public static List<String> GUI_MAKE_OWNER_BUTTON_LORE;
    @Messages.Message(name = "GUIRemoveMemberButtonLore", message = {"Click to remove the selected member",
            "from your region"})
    public static List<String> GUI_REMOVE_MEMBER_BUTTON_LORE;
    @Messages.Message(name = "GUIResetRegionButtonLore", version = 6,message = {"Click to reset your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be resetted and everything on it will",
            "&cbe deleted!",
            "",
            "&cYou can only reset you region once every %userrestorecooldown%",
            "&2You and all members keep their rights on the region"})
    public static List<String> GUI_RESET_REGION_BUTTON_LORE;
    @Messages.Message(name = "TakeOverItemLore", message = {"&aYou are a member of this region.",
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
    @Messages.Message(name = "GUIExtendRentRegionButtonLore", version = 5, message = {"&aClick to extend your region for &6%extendtime-current-writtenout%",
            "&athis will cost you &6%price-current%%currency%&a!",
            "&aThis region will expire in &6%remainingtime-countdown-short%&a.",
            "&aYou can extend your region up to &6%maxextendtime-writtenout%&a."})
    public static List<String> GUI_EXTEND_BUTTON_LORE;
    @Messages.Message(name = "GUIRentRegionLore", message = {"&aExpires in &6%remainingtime-countdown-short%"})
    public static List<String> GUI_RENT_REGION_LORE;
    @Messages.Message(name = "GUIUserSellButtonLore", message = {"Click to sell your region",
            "&4WARNING: &cThis can not be undone! Your region",
            "&cwill be released and all blocks on it will be",
            "&cresetted! You and all members of it will loose",
            "&ctheir rights on it.",
            "&cYou will get &6%paybackmoney%%currency% &cback"})
    public static List<String> GUI_USER_SELL_BUTTON_LORE;
    @Messages.Message(name = "MemberlistInfoLore", message = {"&aYou can be added as a member to",
            "&athe region of someone else in order",
            "&ato build with him together",
            "&aJust ask a region owner to add you with:",
            "&6/arm addmember REGIONID USERNAME",
            "&aYou need to be online for this"})
    public static List<String> GUI_MEMBER_INFO_LORE;
    @Messages.Message(name = "GUIContractItemLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_ITEM_LORE;
    @Messages.Message(name = "GUIContractItemRegionLore", message = {"&aStatus: %status%",
            "&aIf active the next extend is in:",
            "&6%remainingtime-countdown-short%"})
    public static List<String> GUI_CONTRACT_REGION_LORE;
    @Messages.Message(name = "OwnerMemberlistInfoLore", message = {"&aYou can add members to your region",
            "&ain order to build with them together",
            "&aYou can add members with:",
            "&6/arm addmember %regionid% USERNAME",
            "&aMembers need to be online to add them"})
    public static List<String> GUI_OWNER_MEMBER_INFO_LORE;
    @Messages.Message(name = "GUISubregionManagerNoSubregionItemLore", message = {"&aYou do not have any subregions on your region.",
            "&aYou can create a new subregion, that you",
            "&acan sell to other players by typing",
            "&6/arm subregion tool &aand following displayed the steps"})
    public static List<String> GUI_SUBREGION_MANAGER_NO_SUBREGION_ITEM_LORE;
    @Messages.Message(name = "SellPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket SellPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String SELLPRESET_HELP_HEADLINE;
    @Messages.Message(name = "ContractPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket ContractPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String CONTRACTPRESET_HELP_HEADLINE;
    @Messages.Message(name = "RentPresetHelpHeadline", message = "&6=====[AdvancedRegionMarket RentPreset Help]=====\n" +
            "&3Page %actualpage% / %maxpage%")
    public static String RENTPRESET_HELP_HEADLINE;
    @Messages.Message(name = "InfoDeactivated", message = "&4deactivated")
    public static String INFO_DEACTIVATED;
    @Messages.Message(name = "InfoNotSold", message = "&4Region not sold!")
    public static String INFO_REGION_NOT_SOLD;
    @Messages.Message(name = "InfoNow", message = "&2now")
    public static String INFO_NOW;
    @Messages.Message(name = "InfoNotCalculated", message = "&8Awaiting calculation...")
    public static String INFO_NOT_CALCULATED;
    @Messages.Message(name = "CouldNotFindOrLoadSchematicLog", message = "&4Could not find or load schematic for region %region% in world %world%! You can regenerate it with /arm updateschematic %region%")
    public static String COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG;
    @Messages.Message(name = "RegionSoldBackSuccessfully", message = "&2Your region &6%regionid% &2has successfully been sold back to the server! " +
            "&6%paybackmoney%%currency% &2have been added to your account!")
    public static String REGION_SOLD_BACK_SUCCESSFULLY;
    @Messages.Message(name = "RegionModifiedBoolean", message = "&6%option% %state% &6for &a%selectedregions%&6!")
    public static String REGION_MODIFIED_BOOLEAN;
    @Messages.Message(name = "RegionModified", message = "&6%option% &6modified for %selectedregions%&6!")
    public static String REGION_MODIFIED;
    @Messages.Message(name = "UpdatingSchematic", message = "&8Updating schematic...")
    public static String UPDATING_SCHEMATIC;
    @Messages.Message(name = "SchematicUpdated", message = "&aSchematic updated!")
    public static String SCHEMATIC_UPDATED;
    @Messages.Message(name = "ContractRegionTerminated", message = "&6Your contractregion &a%region% &6has successfully been " +
            "&4terminated&6! It will be resetted in &a%remainingtime-countdown-short% &6except it gets reactivated!")
    public static String CONTRACTREGION_TERMINATED;
    @Messages.Message(name = "ContractRegionReactivated", message = "&6Your contractregion &a%region% &6has successfully " +
            "been &areactivated&6! It will automatically be extended in &a%remainingtime-countdown-short% &6if " +
            "you can pay for the rent!")
    public static String CONTRACTREGION_REACTIVATED;
    @Messages.Message(name = "RegionInfoFeatureDisabled", message = "&4Feature disbaled!")
    public static String REGION_INFO_FEATURE_DISABLED;
    @Messages.Message(name = "FlagGroupFeatureDisabled", message = "&4FlagGroups are currently disabled! You can activate them in the config.yml!")
    public static String FLAGGROUP_FEATURE_DISABLED;
    @Messages.Message(name = "BackupCreated", message = "&aBackup created!")
    public static String BACKUP_CREATED;
    @Messages.Message(name = "BackupRestored", message = "&aBackup restored!")
    public static String BACKUP_RESTORED;
    @Messages.Message(name = "CouldNotLoadBackup", message = "&4Could not load backup! Maybe it does not exist or the file is corrupted!")
    public static String COULD_NOT_LOAD_BACKUP;
    @Messages.Message(name = "BackupListHeader", message = "&6=======[Backups of region %regionid%]=======")
    public static String BACKUP_LIST_HEADER;
    @Messages.Message(name = "TeleportLocationHasToBeInsideRegion", message = "&4Teleport-location has to be inside your region!")
    public static String TELEPORT_LOCATION_HAS_TO_BE_INSIDE_REGION;
    @Messages.Message(name = "TeleportLocationUnsafe", message = "&4Teleport-location is unsafe!")
    public static String TELEPORT_LOCATION_IS_UNSAFE;
    @Messages.Message(name = "TeleportLocationUpdated", message = "&2Teleport-location updated!")
    public static String TELEPORT_LOCATION_UPDATED;

}

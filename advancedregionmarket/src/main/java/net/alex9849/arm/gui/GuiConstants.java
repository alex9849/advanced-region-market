package net.alex9849.arm.gui;

import org.bukkit.Material;

public class GuiConstants {
    public static final int GUI_ROW_SIZE = 9;
    public static final int GUI_MAX_COL_SIZE = 6;
    public static final int GUI_MAX_ITEM_SIZE = GUI_ROW_SIZE * GUI_MAX_COL_SIZE;

    //Button Items
    private static Material REGION_OWNER_ITEM;
    private static Material REGION_MEMBER_ITEM;
    private static Material REGION_FINDER_ITEM;
    private static Material REGION_FINDER_SELLTYPE_SELECTOR_ITEM;
    private static Material PLAYER_HEAD_ITEM;
    private static Material GO_BACK_ITEM;
    private static Material WARNING_YES_ITEM;
    private static Material WARNING_NO_ITEM;
    private static Material TP_ITEM;
    private static Material SELL_REGION_ITEM;
    private static Material RESET_ITEM;
    private static Material EXTEND_ITEM;
    private static Material INFO_ITEM;
    private static Material PROMOTE_MEMBER_TO_OWNER_ITEM;
    private static Material REMOVE_MEMBER_ITEM;
    private static Material CONTRACT_ITEM;
    private static Material FILL_ITEM;
    private static Material ENTITY_LIMIT_GROUP_ITEM;
    private static Material SUBREGION_ITEM;
    private static Material DELETE_ITEM;
    private static Material TELEPORT_TO_SIGN_ITEM;
    private static Material TELEPORT_TO_REGION_ITEM;
    private static Material NEXT_PAGE_ITEM;
    private static Material PREV_PAGE_ITEM;
    private static Material HOTEL_SETTING_ITEM;
    private static Material UNSELL_ITEM;
    private static Material FLAG_REMOVE_ITEM;
    private static Material FLAG_SETTING_SELECTED_ITEM;
    private static Material FLAG_SETTING_NOT_SELECTED_ITEM;
    private static Material FLAG_GROUP_SELECTED_ITEM;
    private static Material FLAG_GROUP_NOT_SELECTED_ITEM;
    private static Material FLAGEDITOR_ITEM;
    private static Material FLAG_ITEM;
    private static Material FLAG_USER_INPUT_ITEM;
    private static Material FLAGEDITOR_RESET_ITEM;

    public static Material getRegionOwnerItem() {
        return REGION_OWNER_ITEM;
    }

    public static void setRegionOwnerItem(Material regionOwnerItem) {
        if (regionOwnerItem == null) return;
        REGION_OWNER_ITEM = regionOwnerItem;
    }

    public static Material getRegionFinderSelltypeSelectorItem() {
        return REGION_FINDER_SELLTYPE_SELECTOR_ITEM;
    }

    public static void setRegionFinderSelltypeSelectorItem(Material regionFinderSelltypeSelectorItem) {
        if (regionFinderSelltypeSelectorItem == null) return;
        REGION_FINDER_SELLTYPE_SELECTOR_ITEM = regionFinderSelltypeSelectorItem;
    }

    public static Material getPlayerHeadItem() {
        return PLAYER_HEAD_ITEM;
    }

    public static void setPlayerHeadItem(Material playerHeadItem) {
        if (playerHeadItem == null) return;
        PLAYER_HEAD_ITEM = playerHeadItem;
    }

    public static Material getEntityLimitGroupItem() {
        return ENTITY_LIMIT_GROUP_ITEM;
    }

    public static void setEntityLimitGroupItem(Material entityLimitGroupItem) {
        if (entityLimitGroupItem == null) return;
        ENTITY_LIMIT_GROUP_ITEM = entityLimitGroupItem;
    }

    public static Material getRegionMemberItem() {
        return REGION_MEMBER_ITEM;
    }

    public static void setRegionMemberItem(Material regionMemberItem) {
        if (regionMemberItem == null) return;
        REGION_MEMBER_ITEM = regionMemberItem;
    }

    public static Material getRegionFinderItem() {
        return REGION_FINDER_ITEM;
    }

    public static void setRegionFinderItem(Material regionFinderItem) {
        if(regionFinderItem == null) return;
        REGION_FINDER_ITEM = regionFinderItem;
    }

    public static Material getGoBackItem() {
        return GO_BACK_ITEM;
    }

    public static void setGoBackItem(Material goBackItem) {
        if(goBackItem == null) return;
        GO_BACK_ITEM = goBackItem;
    }

    public static Material getWarningYesItem() {
        return WARNING_YES_ITEM;
    }

    public static void setWarningYesItem(Material warningYesItem) {
        if(warningYesItem == null) return;
        WARNING_YES_ITEM = warningYesItem;
    }

    public static Material getWarningNoItem() {
        return WARNING_NO_ITEM;
    }

    public static void setWarningNoItem(Material warningNoItem) {
        if(warningNoItem == null) return;
        WARNING_NO_ITEM = warningNoItem;
    }

    public static Material getTpItem() {
        return TP_ITEM;
    }

    public static void setTpItem(Material tpItem) {
        if(tpItem == null) return;
        TP_ITEM = tpItem;
    }

    public static Material getSellRegionItem() {
        return SELL_REGION_ITEM;
    }

    public static void setSellRegionItem(Material sellRegionItem) {
        if(sellRegionItem == null) return;
        SELL_REGION_ITEM = sellRegionItem;
    }

    public static Material getResetItem() {
        return RESET_ITEM;
    }

    public static void setResetItem(Material resetItem) {
        if(resetItem == null) return;
        RESET_ITEM = resetItem;
    }

    public static Material getExtendItem() {
        return EXTEND_ITEM;
    }

    public static void setExtendItem(Material extendItem) {
        if(extendItem == null) return;
        EXTEND_ITEM = extendItem;
    }

    public static Material getInfoItem() {
        return INFO_ITEM;
    }

    public static void setInfoItem(Material infoItem) {
        if(infoItem == null) return;
        INFO_ITEM = infoItem;
    }

    public static Material getPromoteMemberToOwnerItem() {
        return PROMOTE_MEMBER_TO_OWNER_ITEM;
    }

    public static void setPromoteMemberToOwnerItem(Material promoteMemberToOwnerItem) {
        if(promoteMemberToOwnerItem == null) return;
        PROMOTE_MEMBER_TO_OWNER_ITEM = promoteMemberToOwnerItem;
    }

    public static Material getRemoveMemberItem() {
        return REMOVE_MEMBER_ITEM;
    }

    public static void setRemoveMemberItem(Material removeMemberItem) {
        if(removeMemberItem == null) return;
        REMOVE_MEMBER_ITEM = removeMemberItem;
    }

    public static Material getContractItem() {
        return CONTRACT_ITEM;
    }

    public static void setContractItem(Material contractItem) {
        if(contractItem == null) return;
        CONTRACT_ITEM = contractItem;
    }

    public static Material getFillItem() {
        return FILL_ITEM;
    }

    public static void setFillItem(Material fillItem) {
        if(fillItem == null) return;
        FILL_ITEM = fillItem;
    }

    public static Material getSubregionItem() {
        return SUBREGION_ITEM;
    }

    public static void setSubregionItem(Material subregionItem) {
        if(subregionItem == null) return;
        SUBREGION_ITEM = subregionItem;
    }

    public static Material getDeleteItem() {
        return DELETE_ITEM;
    }

    public static void setDeleteItem(Material deleteItem) {
        if(deleteItem == null) return;
        DELETE_ITEM = deleteItem;
    }

    public static Material getTeleportToSignItem() {
        return TELEPORT_TO_SIGN_ITEM;
    }

    public static void setTeleportToSignItem(Material teleportToSignItem) {
        if(teleportToSignItem == null) return;
        TELEPORT_TO_SIGN_ITEM = teleportToSignItem;
    }

    public static Material getTeleportToRegionItem() {
        return TELEPORT_TO_REGION_ITEM;
    }

    public static void setTeleportToRegionItem(Material teleportToRegionItem) {
        if(teleportToRegionItem == null) return;
        TELEPORT_TO_REGION_ITEM = teleportToRegionItem;
    }

    public static Material getNextPageItem() {
        return NEXT_PAGE_ITEM;
    }

    public static void setNextPageItem(Material nextPageItem) {
        if(nextPageItem == null) return;
        NEXT_PAGE_ITEM = nextPageItem;
    }

    public static Material getPrevPageItem() {
        return PREV_PAGE_ITEM;
    }

    public static void setPrevPageItem(Material prevPageItem) {
        if(prevPageItem == null) return;
        PREV_PAGE_ITEM = prevPageItem;
    }

    public static Material getHotelSettingItem() {
        return HOTEL_SETTING_ITEM;
    }

    public static void setHotelSettingItem(Material hotelSettingItem) {
        if(hotelSettingItem == null) return;
        HOTEL_SETTING_ITEM = hotelSettingItem;
    }

    public static Material getUnsellItem() {
        return UNSELL_ITEM;
    }

    public static void setUnsellItem(Material unsellItem) {
        if(unsellItem == null) return;
        UNSELL_ITEM = unsellItem;
    }

    public static Material getFlagRemoveItem() {
        return FLAG_REMOVE_ITEM;
    }

    public static void setFlagRemoveItem(Material flagRemoveItem) {
        if(flagRemoveItem == null) return;
        FLAG_REMOVE_ITEM = flagRemoveItem;
    }

    public static Material getFlagSettingSelectedItem() {
        return FLAG_SETTING_SELECTED_ITEM;
    }

    public static void setFlagSettingSelectedItem(Material flagSettingSelectedItem) {
        if(flagSettingSelectedItem == null) return;
        FLAG_SETTING_SELECTED_ITEM = flagSettingSelectedItem;
    }

    public static Material getFlagSettingNotSelectedItem() {
        return FLAG_SETTING_NOT_SELECTED_ITEM;
    }

    public static void setFlagSettingNotSelectedItem(Material flagSettingNotSelectedItem) {
        if(flagSettingNotSelectedItem == null) return;
        FLAG_SETTING_NOT_SELECTED_ITEM = flagSettingNotSelectedItem;
    }

    public static Material getFlagGroupSelectedItem() {
        return FLAG_GROUP_SELECTED_ITEM;
    }

    public static void setFlagGroupSelectedItem(Material flagGroupSelectedItem) {
        if(flagGroupSelectedItem == null) return;
        FLAG_GROUP_SELECTED_ITEM = flagGroupSelectedItem;
    }

    public static Material getFlagGroupNotSelectedItem() {
        return FLAG_GROUP_NOT_SELECTED_ITEM;
    }

    public static void setFlagGroupNotSelectedItem(Material flagGroupNotSelectedItem) {
        if(flagGroupNotSelectedItem == null) return;
        FLAG_GROUP_NOT_SELECTED_ITEM = flagGroupNotSelectedItem;
    }

    public static Material getFlageditorItem() {
        return FLAGEDITOR_ITEM;
    }

    public static void setFlageditorItem(Material flageditorItem) {
        if(flageditorItem == null) return;
        FLAGEDITOR_ITEM = flageditorItem;
    }

    public static Material getFlagItem() {
        return FLAG_ITEM;
    }

    public static void setFlagItem(Material flagItem) {
        if(flagItem == null) return;
        FLAG_ITEM = flagItem;
    }

    public static Material getFlagUserInputItem() {
        return FLAG_USER_INPUT_ITEM;
    }

    public static void setFlagUserInputItem(Material flagUserInputItem) {
        if(flagUserInputItem == null) return;
        FLAG_USER_INPUT_ITEM = flagUserInputItem;
    }

    public static Material getFlageditorResetItem() {
        return FLAGEDITOR_RESET_ITEM;
    }

    public static void setFlageditorResetItem(Material flageditorResetItem) {
        if(flageditorResetItem == null) return;
        FLAGEDITOR_RESET_ITEM = flageditorResetItem;
    }
}

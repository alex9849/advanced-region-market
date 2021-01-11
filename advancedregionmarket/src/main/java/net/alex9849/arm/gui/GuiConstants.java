package net.alex9849.arm.gui;

import net.alex9849.arm.util.MaterialFinder;
import org.bukkit.Material;

public class GuiConstants {
    public static final int GUI_ROW_SIZE = 9;
    public static final int GUI_MAX_COL_SIZE = 6;
    public static final int GUI_MAX_ITEM_SIZE = GUI_ROW_SIZE * GUI_MAX_COL_SIZE;

    //Button Items
    private static Material REGION_OWNER_ITEM = MaterialFinder.getGuiRegionOwnerItem();
    private static Material REGION_MEMBER_ITEM = MaterialFinder.getGuiRegionMemberItem();
    private static Material REGION_FINDER_ITEM = MaterialFinder.getGuiRegionFinderItem();
    private static Material GO_BACK_ITEM = MaterialFinder.getGuiGoBackItem();
    private static Material WARNING_YES_ITEM = MaterialFinder.getGuiWarningYesItem();
    private static Material WARNING_NO_ITEM = MaterialFinder.getGuiWarningNoItem();
    private static Material TP_ITEM = MaterialFinder.getGuiTpItem();
    private static Material SELL_REGION_ITEM = MaterialFinder.getGuiSellRegionItem();
    private static Material RESET_ITEM = MaterialFinder.getGuiResetItem();
    private static Material EXTEND_ITEM = MaterialFinder.getGuiExtendItem();
    private static Material INFO_ITEM = MaterialFinder.getGuiInfoItem();
    private static Material PROMOTE_MEMBER_TO_OWNER_ITEM = MaterialFinder.getGuiPromoteMemberToOwnerItem();
    private static Material REMOVE_MEMBER_ITEM = MaterialFinder.getGuiRemoveMemberItem();
    private static Material CONTRACT_ITEM = MaterialFinder.getGuiContractItem();
    private static Material FILL_ITEM = MaterialFinder.getGuiFillItem();
    private static Material SUBREGION_ITEM = MaterialFinder.getGuiSubregionItem();
    private static Material DELETE_ITEM = MaterialFinder.getGuiDeleteItem();
    private static Material TELEPORT_TO_SIGN_ITEM = MaterialFinder.getGuiTeleportToSignItem();
    private static Material TELEPORT_TO_REGION_ITEM = MaterialFinder.getGuiTeleportToRegionItem();
    private static Material NEXT_PAGE_ITEM = MaterialFinder.getGuiNextPageItem();
    private static Material PREV_PAGE_ITEM = MaterialFinder.getGuiPrevPageItem();
    private static Material HOTEL_SETTING_ITEM = MaterialFinder.getGuiHotelSettingItem();
    private static Material UNSELL_ITEM = MaterialFinder.getGuiUnsellItem();
    private static Material FLAG_REMOVE_ITEM = MaterialFinder.getGuiFlagRemoveItem();
    private static Material FLAG_SETTING_SELECTED_ITEM = MaterialFinder.getGuiFlagSettingSelectedItem();
    private static Material FLAG_SETTING_NOT_SELECTED_ITEM = MaterialFinder.getGuiFlagSettingNotSelectedItem();
    private static Material FLAG_GROUP_SELECTED_ITEM = MaterialFinder.getGuiFlagGroupSelectedItem();
    private static Material FLAG_GROUP_NOT_SELECTED_ITEM = MaterialFinder.getGuiFlagGroupNotSelectedItem();
    private static Material FLAGEDITOR_ITEM = MaterialFinder.getGuiFlageditorItem();
    private static Material FLAG_ITEM = MaterialFinder.getGuiFlagItem();
    private static Material FLAG_USER_INPUT_ITEM = MaterialFinder.getGuiFlagUserInputItem();
    private static Material FLAGEDITOR_RESET_ITEM = MaterialFinder.getGuiFlageditorResetItem();

    public static Material getRegionOwnerItem() {
        return REGION_OWNER_ITEM;
    }

    public static void setRegionOwnerItem(Material regionOwnerItem) {
        REGION_OWNER_ITEM = regionOwnerItem;
    }

    public static Material getRegionMemberItem() {
        return REGION_MEMBER_ITEM;
    }

    public static void setRegionMemberItem(Material regionMemberItem) {
        REGION_MEMBER_ITEM = regionMemberItem;
    }

    public static Material getRegionFinderItem() {
        return REGION_FINDER_ITEM;
    }

    public static void setRegionFinderItem(Material regionFinderItem) {
        REGION_FINDER_ITEM = regionFinderItem;
    }

    public static Material getGoBackItem() {
        return GO_BACK_ITEM;
    }

    public static void setGoBackItem(Material goBackItem) {
        GO_BACK_ITEM = goBackItem;
    }

    public static Material getWarningYesItem() {
        return WARNING_YES_ITEM;
    }

    public static void setWarningYesItem(Material warningYesItem) {
        WARNING_YES_ITEM = warningYesItem;
    }

    public static Material getWarningNoItem() {
        return WARNING_NO_ITEM;
    }

    public static void setWarningNoItem(Material warningNoItem) {
        WARNING_NO_ITEM = warningNoItem;
    }

    public static Material getTpItem() {
        return TP_ITEM;
    }

    public static void setTpItem(Material tpItem) {
        TP_ITEM = tpItem;
    }

    public static Material getSellRegionItem() {
        return SELL_REGION_ITEM;
    }

    public static void setSellRegionItem(Material sellRegionItem) {
        SELL_REGION_ITEM = sellRegionItem;
    }

    public static Material getResetItem() {
        return RESET_ITEM;
    }

    public static void setResetItem(Material resetItem) {
        RESET_ITEM = resetItem;
    }

    public static Material getExtendItem() {
        return EXTEND_ITEM;
    }

    public static void setExtendItem(Material extendItem) {
        EXTEND_ITEM = extendItem;
    }

    public static Material getInfoItem() {
        return INFO_ITEM;
    }

    public static void setInfoItem(Material infoItem) {
        INFO_ITEM = infoItem;
    }

    public static Material getPromoteMemberToOwnerItem() {
        return PROMOTE_MEMBER_TO_OWNER_ITEM;
    }

    public static void setPromoteMemberToOwnerItem(Material promoteMemberToOwnerItem) {
        PROMOTE_MEMBER_TO_OWNER_ITEM = promoteMemberToOwnerItem;
    }

    public static Material getRemoveMemberItem() {
        return REMOVE_MEMBER_ITEM;
    }

    public static void setRemoveMemberItem(Material removeMemberItem) {
        REMOVE_MEMBER_ITEM = removeMemberItem;
    }

    public static Material getContractItem() {
        return CONTRACT_ITEM;
    }

    public static void setContractItem(Material contractItem) {
        CONTRACT_ITEM = contractItem;
    }

    public static Material getFillItem() {
        return FILL_ITEM;
    }

    public static void setFillItem(Material fillItem) {
        FILL_ITEM = fillItem;
    }

    public static Material getSubregionItem() {
        return SUBREGION_ITEM;
    }

    public static void setSubregionItem(Material subregionItem) {
        SUBREGION_ITEM = subregionItem;
    }

    public static Material getDeleteItem() {
        return DELETE_ITEM;
    }

    public static void setDeleteItem(Material deleteItem) {
        DELETE_ITEM = deleteItem;
    }

    public static Material getTeleportToSignItem() {
        return TELEPORT_TO_SIGN_ITEM;
    }

    public static void setTeleportToSignItem(Material teleportToSignItem) {
        TELEPORT_TO_SIGN_ITEM = teleportToSignItem;
    }

    public static Material getTeleportToRegionItem() {
        return TELEPORT_TO_REGION_ITEM;
    }

    public static void setTeleportToRegionItem(Material teleportToRegionItem) {
        TELEPORT_TO_REGION_ITEM = teleportToRegionItem;
    }

    public static Material getNextPageItem() {
        return NEXT_PAGE_ITEM;
    }

    public static void setNextPageItem(Material nextPageItem) {
        NEXT_PAGE_ITEM = nextPageItem;
    }

    public static Material getPrevPageItem() {
        return PREV_PAGE_ITEM;
    }

    public static void setPrevPageItem(Material prevPageItem) {
        PREV_PAGE_ITEM = prevPageItem;
    }

    public static Material getHotelSettingItem() {
        return HOTEL_SETTING_ITEM;
    }

    public static void setHotelSettingItem(Material hotelSettingItem) {
        HOTEL_SETTING_ITEM = hotelSettingItem;
    }

    public static Material getUnsellItem() {
        return UNSELL_ITEM;
    }

    public static void setUnsellItem(Material unsellItem) {
        UNSELL_ITEM = unsellItem;
    }

    public static Material getFlagRemoveItem() {
        return FLAG_REMOVE_ITEM;
    }

    public static void setFlagRemoveItem(Material flagRemoveItem) {
        FLAG_REMOVE_ITEM = flagRemoveItem;
    }

    public static Material getFlagSettingSelectedItem() {
        return FLAG_SETTING_SELECTED_ITEM;
    }

    public static void setFlagSettingSelectedItem(Material flagSettingSelectedItem) {
        FLAG_SETTING_SELECTED_ITEM = flagSettingSelectedItem;
    }

    public static Material getFlagSettingNotSelectedItem() {
        return FLAG_SETTING_NOT_SELECTED_ITEM;
    }

    public static void setFlagSettingNotSelectedItem(Material flagSettingNotSelectedItem) {
        FLAG_SETTING_NOT_SELECTED_ITEM = flagSettingNotSelectedItem;
    }

    public static Material getFlagGroupSelectedItem() {
        return FLAG_GROUP_SELECTED_ITEM;
    }

    public static void setFlagGroupSelectedItem(Material flagGroupSelectedItem) {
        FLAG_GROUP_SELECTED_ITEM = flagGroupSelectedItem;
    }

    public static Material getFlagGroupNotSelectedItem() {
        return FLAG_GROUP_NOT_SELECTED_ITEM;
    }

    public static void setFlagGroupNotSelectedItem(Material flagGroupNotSelectedItem) {
        FLAG_GROUP_NOT_SELECTED_ITEM = flagGroupNotSelectedItem;
    }

    public static Material getFlageditorItem() {
        return FLAGEDITOR_ITEM;
    }

    public static void setFlageditorItem(Material flageditorItem) {
        FLAGEDITOR_ITEM = flageditorItem;
    }

    public static Material getFlagItem() {
        return FLAG_ITEM;
    }

    public static void setFlagItem(Material flagItem) {
        FLAG_ITEM = flagItem;
    }

    public static Material getFlagUserInputItem() {
        return FLAG_USER_INPUT_ITEM;
    }

    public static void setFlagUserInputItem(Material flagUserInputItem) {
        FLAG_USER_INPUT_ITEM = flagUserInputItem;
    }

    public static Material getFlageditorResetItem() {
        return FLAGEDITOR_RESET_ITEM;
    }

    public static void setFlageditorResetItem(Material flageditorResetItem) {
        FLAGEDITOR_RESET_ITEM = flageditorResetItem;
    }
}

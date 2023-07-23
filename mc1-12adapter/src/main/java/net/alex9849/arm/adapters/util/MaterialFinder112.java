package net.alex9849.arm.adapters.util;

import org.bukkit.Material;

public class MaterialFinder112 extends AbstractMaterialFinder {

    private static MaterialFinder112 instance;

    public static MaterialFinder112 getInstance() {
        if (instance == null) {
            instance = new MaterialFinder112();
        }
        return instance;
    }

    @Override
    public Material getWallSign() {
        return Material.WALL_SIGN;
    }

    @Override
    public Material getGuiFlagItem() {
        return Material.SIGN_POST;
    }

    @Override
    public Material getSign() {
        return Material.SIGN_POST;
    }

    @Override
    public Material getGuiFlagRemoveItem() {
        return Material.TNT;
    }

    @Override
    public Material getGuiFlagUserInputItem() {
        return Material.BOOK_AND_QUILL;
    }

    @Override
    public Material getGuiFlageditorItem() {
        return Material.SIGN_POST;
    }

    @Override
    public Material getGuiFlagSettingSelectedItem() {
        return Material.MELON_BLOCK;
    }

    @Override
    public Material getGuiFlagSettingNotSelectedItem() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public Material getGuiFlagGroupSelectedItem() {
        return Material.MELON_BLOCK;
    }

    @Override
    public Material getGuiFlagGroupNotSelectedItem() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public Material getGuiRegionOwnerItem() {
        return Material.ENDER_CHEST;
    }

    @Override
    public Material getGuiRegionMemberItem() {
        return Material.CHEST;
    }

    @Override
    public Material getGuiRegionFinderItem() {
        return Material.COMPASS;
    }

    @Override
    public Material getGuiGoBackItem() {
        return Material.WOOD_DOOR;
    }

    @Override
    public Material getGuiWarningYesItem() {
        return Material.MELON_BLOCK;
    }

    @Override
    public Material getGuiWarningNoItem() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public Material getGuiTpItem() {
        return Material.ENDER_PEARL;
    }

    @Override
    public Material getGuiSellRegionItem() {
        return Material.DIAMOND;
    }

    @Override
    public Material getGuiResetItem() {
        return Material.TNT;
    }

    @Override
    public Material getGuiExtendItem() {
        return Material.WATCH;
    }

    @Override
    public Material getGuiInfoItem() {
        return Material.BOOK;
    }

    @Override
    public Material getGuiPromoteMemberToOwnerItem() {
        return Material.LADDER;
    }

    @Override
    public Material getGuiRemoveMemberItem() {
        return Material.LAVA_BUCKET;
    }

    @Override
    public Material getGuiContractItem() {
        return Material.BOOK_AND_QUILL;
    }

    @Override
    public Material getGuiFillItem() {
        return Material.AIR;
    }

    @Override
    public Material getGuiSubregionItem() {
        return Material.DIRT;
    }

    @Override
    public Material getGuiDeleteItem() {
        return Material.BARRIER;
    }

    @Override
    public Material getGuiTeleportToSignItem() {
        return Material.SIGN_POST;
    }

    @Override
    public Material getGuiTeleportToRegionItem() {
        return Material.GRASS;
    }

    @Override
    public Material getGuiNextPageItem() {
        return Material.ARROW;
    }

    @Override
    public Material getGuiPrevPageItem() {
        return Material.ARROW;
    }

    @Override
    public Material getGuiHotelSettingItem() {
        return Material.BED;
    }

    @Override
    public Material getGuiUnsellItem() {
        return Material.NAME_TAG;
    }

    @Override
    public Material getPlayerHead() {
        return Material.SKULL_ITEM;
    }

    @Override
    public Material getEntityLimitGroupItem() {
        return Material.EGG;
    }

    @Override
    public Material getRegionFinderSelltypeSelectorItem() {
        return Material.BRICK;
    }

    @Override
    public Material getRedBed() {
        return Material.BED;
    }

    @Override
    public Material getWallTorch() {
        return Material.TORCH;
    }

    @Override
    public Material getMagmaBlock() {
        return Material.LAVA;
    }

    @Override
    public Material getGuiFlageditorResetItem() {
        return Material.TNT;
    }
}

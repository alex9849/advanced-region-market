package net.alex9849.arm.adapters.util;

import org.bukkit.Material;

public class MaterialFinder114 extends AbstractMaterialFinder {

    private static MaterialFinder114 instance;

    public static MaterialFinder114 getInstance() {
        if (instance == null) {
            instance = new MaterialFinder114();
        }
        return instance;
    }

    @Override
    public Material getWallSign() {
        return Material.OAK_WALL_SIGN;
    }

    @Override
    public Material getGuiFlagItem() {
        return Material.OAK_SIGN;
    }

    @Override
    public Material getSign() {
        return Material.OAK_SIGN;
    }

    @Override
    public Material getGuiFlagRemoveItem() {
        return Material.TNT;
    }

    @Override
    public Material getGuiFlagUserInputItem() {
        return Material.WRITABLE_BOOK;
    }

    @Override
    public Material getGuiFlageditorItem() {
        return Material.OAK_SIGN;
    }

    @Override
    public Material getGuiFlagSettingSelectedItem() {
        return Material.MELON;
    }

    @Override
    public Material getGuiFlagSettingNotSelectedItem() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public Material getGuiFlagGroupSelectedItem() {
        return Material.MELON;
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
        return Material.OAK_DOOR;
    }

    @Override
    public Material getGuiWarningYesItem() {
        return Material.MELON;
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
        return Material.CLOCK;
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
        return Material.WRITABLE_BOOK;
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
        return Material.OAK_SIGN;
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
        return Material.RED_BED;
    }

    @Override
    public Material getGuiUnsellItem() {
        return Material.NAME_TAG;
    }

    @Override
    public Material getPlayerHead() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public Material getEntityLimitGroupItem() {
        return Material.CHICKEN_SPAWN_EGG;
    }

    @Override
    public Material getRegionFinderSelltypeSelectorItem() {
        return Material.BRICK;
    }

    @Override
    public Material getRedBed() {
        return Material.RED_BED;
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

package net.alex9849.arm.util;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMaterialFinder {
    private List<Material> signMaterials;

    public List<Material> getSignMaterials() {
        if (signMaterials == null) {
            signMaterials = new ArrayList<Material>();
            for (Material mat : Material.values()) {
                if (mat.toString().contains("SIGN")) {
                    signMaterials.add(mat);
                }
            }
        }
        return signMaterials;
    }

    public static Material firstNonNull(Material... materials) {
        for (Material m : materials) {
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    public Material getMaterial(String materialName) {
        Material material = this.getMaterial(materialName, false);
        if (material == null) {
            material = this.getMaterial(materialName, true);
        }
        return material;
    }

    public Material getHangingSign() {
        throw new IllegalStateException("Hanging sign doesn't exist in this minecraft version!");
    }

    public abstract Material getWallSign();

    public abstract Material getGuiFlagItem();

    public abstract Material getSign();

    public Material getMaterial(String name, boolean legacyName) {
        if (legacyName) {
            if (!name.startsWith("LEGACY_")) {
                name = "LEGACY_" + name;
            }
        }
        return Material.matchMaterial(name);
    }

    public abstract Material getGuiFlagRemoveItem();

    public abstract Material getGuiFlagUserInputItem();

    public abstract Material getGuiFlageditorItem();

    public abstract Material getGuiFlagSettingSelectedItem();

    public abstract Material getGuiFlagSettingNotSelectedItem();

    public abstract Material getGuiFlagGroupSelectedItem();

    public abstract Material getGuiFlagGroupNotSelectedItem();

    public abstract Material getGuiRegionOwnerItem();

    public abstract Material getGuiRegionMemberItem();

    public abstract Material getGuiRegionFinderItem();

    public abstract Material getGuiGoBackItem();

    public abstract Material getGuiWarningYesItem();

    public abstract Material getGuiWarningNoItem();

    public abstract Material getGuiTpItem();

    public abstract Material getGuiSellRegionItem();

    public abstract Material getGuiResetItem();

    public abstract Material getGuiExtendItem();

    public abstract Material getGuiInfoItem();

    public abstract Material getGuiPromoteMemberToOwnerItem();

    public abstract Material getGuiRemoveMemberItem();

    public abstract Material getGuiContractItem();

    public abstract Material getGuiFillItem();

    public abstract Material getGuiSubregionItem();

    public abstract Material getGuiDeleteItem();

    public abstract Material getGuiTeleportToSignItem();

    public abstract Material getGuiTeleportToRegionItem();

    public abstract Material getGuiNextPageItem();

    public abstract Material getGuiPrevPageItem();

    public abstract Material getGuiHotelSettingItem();

    public abstract Material getGuiUnsellItem();

    public abstract Material getPlayerHead();

    public abstract Material getEntityLimitGroupItem();

    public abstract Material getRegionFinderSelltypeSelectorItem();

    public abstract Material getRedBed();

    public abstract Material getWallTorch();

    public abstract Material getMagmaBlock();

    public abstract Material getGuiFlageditorResetItem();
}

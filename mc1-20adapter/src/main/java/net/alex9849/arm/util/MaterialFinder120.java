package net.alex9849.arm.util;

import org.bukkit.Material;

public class MaterialFinder120 extends MaterialFinder114 {

    private static MaterialFinder120 instance;

    public static MaterialFinder120 getInstance() {
        if (instance == null) {
            instance = new MaterialFinder120();
        }
        return instance;
    }

    @Override
    public Material getHangingSign() {
        return Material.OAK_HANGING_SIGN;
    }

    @Override
    public Material getWallHangingSign() {
        return Material.OAK_WALL_HANGING_SIGN;
    }
}

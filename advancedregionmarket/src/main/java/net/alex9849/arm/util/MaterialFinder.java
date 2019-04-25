package net.alex9849.arm.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialFinder {

    private static List<Material> signMaterials;
    private static Material defaultSign;
    private static Material wallSign;

    static {
        MaterialFinder.signMaterials = new ArrayList<>();
        for(Material mat : Material.values()) {
            if(mat.toString().contains("SIGN")) {
                MaterialFinder.signMaterials.add(mat);
            }
        }

        String serverVersion = Bukkit.getServer().getVersion();

        if(serverVersion.equalsIgnoreCase("1.13") || serverVersion.contains("1.13")) {
            MaterialFinder.defaultSign = MaterialFinder.getMaterial("SIGN");
            MaterialFinder.wallSign = MaterialFinder.getMaterial("WALL_SIGN");
        } else {
            MaterialFinder.defaultSign =  MaterialFinder.getMaterial("OAK_SIGN");
            MaterialFinder.wallSign = MaterialFinder.getMaterial("OAK_WALL_SIGN");
        }
    }

    public static List<Material> getSignMaterials() {
        return signMaterials;
    }

    public static Material getMaterial(String materialName) {
        Material material = Material.getMaterial(materialName);
        if(material == null) {
            material = Material.getMaterial(materialName, true);
        }
        return material;
    }

    public static Material getWallSign() {
        return MaterialFinder.wallSign;
    }

    public static Material getSign() {
        return MaterialFinder.defaultSign;
    }

}

package net.liggesmeyer.arm.regions;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RegionKind {
    private final String name;
    private final Material material;
    public static final RegionKind DEFAULT = new RegionKind("Default", Material.BED, new ArrayList<String>());
    private static List<RegionKind> list = new ArrayList<>();
    private final List<String> lore;

    public RegionKind(String name, Material material, List<String> lore){
        this.name = name;
        this.material = material;
        this.lore = lore;
    }

    public String getName(){
        return this.name;
    }

    public Material getMaterial(){
        return this.material;
    }

    public static List<RegionKind> getRegionKindList() {
        return list;
    }

    public static boolean kindExists(String kind){
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equals(kind)){
                return true;
            }
        }
        return false;
    }

    public static void Reset(){
        list = new ArrayList<>();
    }

    public static RegionKind getRegionKind(String name){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getName().equalsIgnoreCase(name)){
                return list.get(i);
            }
        }
        if(name.equalsIgnoreCase("default")){
            return RegionKind.DEFAULT;
        }
        return null;
    }

    public List<String> getLore(){
        return this.lore;
    }
}

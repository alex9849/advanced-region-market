package net.alex9849.arm.regions;

import net.alex9849.arm.Permission;
import org.bukkit.Material;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RegionKind {
    private String name;
    private Material material;
    public static RegionKind DEFAULT = new RegionKind("Default", Material.RED_BED, new ArrayList<String>(), "Default");
    private static List<RegionKind> list = new ArrayList<>();
    private List<String> lore;
    private String displayName;

    public RegionKind(String name, Material material, List<String> lore, String displayName){
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.displayName = displayName;
    }

    public void setMaterial(Material mat) {
        this.material = mat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLore(List<String> lore) {
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
        if(kind.equalsIgnoreCase("default")) {
            return true;
        }
        if(kind.equalsIgnoreCase(DEFAULT.getName())){
            return true;
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
        if(name.equalsIgnoreCase("default") || name.equalsIgnoreCase(RegionKind.DEFAULT.getName())){
            return RegionKind.DEFAULT;
        }
        return null;
    }

    public List<String> getLore(){
        return this.lore;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<String> completeTabRegionKinds(String arg) {
        List<String> returnme = new ArrayList<>();

        for (RegionKind regionkind : RegionKind.getRegionKindList()) {
            if (regionkind.getName().toLowerCase().startsWith(arg)) {
                returnme.add(regionkind.getName());
            }
        }
        if ("default".startsWith(arg)) {
            returnme.add("default");
        }

        return returnme;
    }

    public static boolean hasPermission(CommandSender sender, RegionKind regionKind) {
        if(regionKind == RegionKind.DEFAULT) {
            return true;
        } else {
            return sender.hasPermission(Permission.ARM_BUYKIND + regionKind.getName());
        }
    }
}

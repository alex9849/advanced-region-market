package net.alex9849.arm.regions.regionkind;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import org.bukkit.Material;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RegionKind {
    private String name;
    private Material material;
    public static RegionKind DEFAULT = new RegionKind("Default", Material.RED_BED, new ArrayList<String>(), "Default", true, true, 50);
    public static RegionKind SUBREGION = new RegionKind("Subregion", Material.RED_BED, new ArrayList<String>(), "Subregion", false, false, 0);
    private static List<RegionKind> list = new ArrayList<>();
    private List<String> lore;
    private String displayName;
    private boolean displayInGUI;
    private boolean displayInLimits;
    private double paybackPercentage;

    public RegionKind(String name, Material material, List<String> lore, String displayName, boolean displayInGUI, boolean displayInLimits, double paybackPercentage){
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.displayName = displayName;
        this.displayInGUI = displayInGUI;
        this.displayInLimits = displayInLimits;
        this.paybackPercentage = paybackPercentage;
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
        if(kind.equalsIgnoreCase(DEFAULT.getDisplayName())){
            return true;
        }
        if(kind.equalsIgnoreCase("subregion")) {
            return true;
        }
        if(kind.equalsIgnoreCase(SUBREGION.getDisplayName())){
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
        if(name.equalsIgnoreCase("default") || name.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())){
            return RegionKind.DEFAULT;
        }
        if(name.equalsIgnoreCase("subregion") || name.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())){
            return RegionKind.SUBREGION;
        }
        return null;
    }

    public List<String> getLore(){
        return this.lore;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<String> completeTabRegionKinds(String arg, String prefix) {
        List<String> returnme = new ArrayList<>();

        for (RegionKind regionkind : RegionKind.getRegionKindList()) {
            if ((prefix + regionkind.getName()).toLowerCase().startsWith(arg)) {
                returnme.add(prefix + regionkind.getName());
            }
        }
        if ((prefix + "default").startsWith(arg)) {
            returnme.add(prefix + "default");
        }
        if ((prefix + "subregion").startsWith(arg)) {
            returnme.add(prefix + "subregion");
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

    public void setPaybackPercentage(double paybackPercentage) {
        this.paybackPercentage = paybackPercentage;
    }

    public void setDisplayInGUI(boolean displayInGUI) {
        this.displayInGUI = displayInGUI;
    }

    public void setDisplayInLimits(boolean displayInLimits) {
        this.displayInLimits = displayInLimits;
    }

    public boolean isDisplayInGUI() {
        return displayInGUI;
    }

    public boolean isDisplayInLimits() {
        return displayInLimits;
    }

    public double getPaybackPercentage() {
        return paybackPercentage;
    }

    public String getConvertedMessage(String message) {
        message = message.replace("%regionkinddisplay%", this.getDisplayName());
        message = message.replace("%regionkind%", this.getName());
        message = message.replace("%currency%", Messages.CURRENCY);
        return message;
    }
}

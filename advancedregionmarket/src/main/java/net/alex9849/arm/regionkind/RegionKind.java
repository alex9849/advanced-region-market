package net.alex9849.arm.regionkind;

import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.arm.util.Saveable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RegionKind implements Saveable {
    private String name;
    private Material material;
    public static RegionKind DEFAULT = new RegionKind("Default", MaterialFinder.getRedBed(), new ArrayList<String>(), "Default", true, true, 50);
    public static RegionKind SUBREGION = new RegionKind("Subregion", MaterialFinder.getRedBed(), new ArrayList<String>(), "Subregion", false, false, 0);
    private List<String> lore;
    private String displayName;
    private boolean displayInGUI;
    private boolean displayInLimits;
    private double paybackPercentage;
    private boolean needsSave;

    public RegionKind(String name, Material material, List<String> lore, String displayName, boolean displayInGUI, boolean displayInLimits, double paybackPercentage){
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.displayName = displayName;
        this.displayInGUI = displayInGUI;
        this.displayInLimits = displayInLimits;
        this.paybackPercentage = paybackPercentage;
        this.needsSave = false;
    }

    public void setMaterial(Material mat) {
        this.material = mat;
        this.queueSave();
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
        this.queueSave();
    }

    public String getName(){
        return this.name;
    }

    public Material getMaterial(){
        return this.material;
    }

    public List<String> getLore(){
        List<String> newLore = new ArrayList<>();
        for(String msg : this.lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return newLore;
    }

    public List<String> getRawLore() {
        return this.lore;
    }

    public String getRawDisplayName() {
        return this.displayName;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', this.displayName);
    }

    public static boolean hasPermission(CommandSender sender, RegionKind regionKind) {
        if(!ArmSettings.isActivateRegionKindPermissions()) {
            return true;
        }
        if(regionKind == RegionKind.DEFAULT) {
            return true;
        } else {
            return sender.hasPermission(Permission.ARM_BUYKIND + regionKind.getName());
        }
    }

    public void setPaybackPercentage(double paybackPercentage) {
        this.paybackPercentage = paybackPercentage;
        this.queueSave();
    }

    public void setDisplayInGUI(boolean displayInGUI) {
        this.displayInGUI = displayInGUI;
        this.queueSave();
    }

    public void setDisplayInLimits(boolean displayInLimits) {
        this.displayInLimits = displayInLimits;
        this.queueSave();
    }

    public void setItem(Material material) {
        this.material = material;
        this.queueSave();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.queueSave();
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
        if(message.contains("%regionkinddisplay%")) message = message.replace("%regionkinddisplay%", this.getDisplayName());
        if(message.contains("%regionkind%")) message = message.replace("%regionkind%", this.getName());
        if(message.contains("%currency%")) message = message.replace("%currency%", Messages.CURRENCY);
        if(message.contains("%paypackpercentage%")) message = message.replace("%paypackpercentage%", this.getPaybackPercentage() + "");
        if(message.contains("%regionkinditem%")) message = message.replace("%regionkinditem%", this.getMaterial().toString());
        if(message.contains("%regionkinddisplayinlimits%")) message = message.replace("%regionkinddisplayinlimits%", Messages.convertYesNo(this.isDisplayInLimits()));
        if(message.contains("%regionkinddisplayingui%")) message = message.replace("%regionkinddisplayingui%", Messages.convertYesNo(this.isDisplayInGUI()));
        return message;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection confSection = new YamlConfiguration();
        confSection.set("item", this.getMaterial().toString());
        confSection.set("displayName", this.getRawDisplayName());
        confSection.set("displayInLimits", this.isDisplayInLimits());
        confSection.set("displayInGUI", this.isDisplayInGUI());
        confSection.set("paypackPercentage", this.getPaybackPercentage());
        confSection.set("lore", this.getRawLore());
        return confSection;
    }

    public static RegionKind parse(ConfigurationSection confSection, String id) {
        Material material = MaterialFinder.getMaterial(confSection.getString("item"));
        if(material == null) {
            material = MaterialFinder.getRedBed();
        }
        String displayName = confSection.getString("displayName");
        boolean displayInLimits = confSection.getBoolean("displayInLimits");
        boolean displayInGUI = confSection.getBoolean("displayInGUI");
        double paybackPercentage = confSection.getDouble("paypackPercentage");
        List<String> lore = new ArrayList<>(confSection.getStringList("lore"));

        return new RegionKind(id, material, lore, displayName, displayInGUI, displayInLimits, paybackPercentage);
    }

    @Override
    public void queueSave() {
        this.needsSave = true;
    }

    @Override
    public void setSaved() {
        this.needsSave = false;
    }

    @Override
    public boolean needsSave() {
        return this.needsSave;
    }
}

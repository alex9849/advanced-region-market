package net.alex9849.arm.regionkind;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.util.MaterialFinder;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegionKind implements Saveable {
    public static RegionKind DEFAULT = new RegionKind("Default", MaterialFinder.getRedBed(), new ArrayList<String>(), "Default", true, true);
    public static RegionKind SUBREGION = new RegionKind("Subregion", MaterialFinder.getRedBed(), new ArrayList<String>(), "Subregion", false, false);
    private String name;
    private Material material;
    private List<String> lore;
    private String displayName;
    private boolean displayInRegionFinder;
    private boolean displayInLimits;
    private boolean needsSave;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%regionkinddisplay%", () -> {
            return this.getDisplayName();
        });
        variableReplacements.put("%regionkind%", () -> {
            return this.getName();
        });
        variableReplacements.put("%currency%", () -> {
            return Messages.CURRENCY;
        });
        variableReplacements.put("%regionkinditem%", () -> {
            return this.getMaterial().toString();
        });
        variableReplacements.put("%regionkinddisplayinlimits%", () -> {
            return Messages.convertYesNo(this.isDisplayInLimits());
        });
        variableReplacements.put("%regionkinddisplayingui%", () -> {
            return Messages.convertYesNo(this.isDisplayInRegionfinder());
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 20);
    }

    public RegionKind(String name, Material material, List<String> lore, String displayName, boolean displayInRegionFinder, boolean displayInLimits) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.displayName = displayName;
        this.displayInRegionFinder = displayInRegionFinder;
        this.displayInLimits = displayInLimits;
        this.needsSave = false;
    }

    public static boolean hasPermission(CommandSender sender, RegionKind regionKind) {
        if (!AdvancedRegionMarket.getInstance().getPluginSettings().isActivateRegionKindPermissions()) {
            return true;
        }
        if (regionKind == RegionKind.DEFAULT) {
            return true;
        } else {
            return sender.hasPermission(Permission.ARM_BUYKIND + regionKind.getName());
        }
    }

    public static RegionKind parse(ConfigurationSection confSection, String name) {
        Material material = MaterialFinder.getMaterial(confSection.getString("item"));
        if (material == null) {
            material = MaterialFinder.getRedBed();
        }
        String displayName = confSection.getString("displayName");
        boolean displayInLimits = confSection.getBoolean("displayInLimits");
        boolean displayInRegionfinder = confSection.getBoolean("displayInRegionfinder");
        List<String> lore = new ArrayList<>(confSection.getStringList("lore"));

        return new RegionKind(name, material, lore, displayName, displayInRegionfinder, displayInLimits);
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material mat) {
        this.material = mat;
        this.queueSave();
    }

    public List<String> getLore() {
        List<String> newLore = new ArrayList<>();
        for (String msg : this.lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return newLore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
        this.queueSave();
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.queueSave();
    }

    public void setItem(Material material) {
        this.material = material;
        this.queueSave();
    }

    public boolean isDisplayInRegionfinder() {
        return displayInRegionFinder;
    }

    public void setDisplayInRegionfinder(boolean displayInRegionfinder) {
        this.displayInRegionFinder = displayInRegionfinder;
        this.queueSave();
    }

    public boolean isDisplayInLimits() {
        return displayInLimits;
    }

    public void setDisplayInLimits(boolean displayInLimits) {
        this.displayInLimits = displayInLimits;
        this.queueSave();
    }

    public String getConvertedMessage(String message) {
        return this.stringReplacer.replace(message).toString();
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection confSection = new YamlConfiguration();
        confSection.set("item", this.getMaterial().toString());
        confSection.set("displayName", this.getRawDisplayName());
        confSection.set("displayInLimits", this.isDisplayInLimits());
        confSection.set("displayInRegionfinder", this.isDisplayInRegionfinder());
        confSection.set("lore", this.getRawLore());
        return confSection;
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

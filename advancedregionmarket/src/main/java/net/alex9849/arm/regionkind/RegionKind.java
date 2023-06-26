package net.alex9849.arm.regionkind;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.util.AbstractMaterialFinder;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.StringReplacer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class RegionKind implements LimitGroupElement, Saveable {
    public static RegionKind DEFAULT;
    public static RegionKind SUBREGION;
    private String name;
    private Material material;
    private int customItemModel;
    private List<String> lore;
    private String displayName;
    private boolean displayInRegionFinder;
    private boolean displayInLimits;
    private boolean needsSave;
    private StringReplacer stringReplacer;

    {
        HashMap<String, Supplier<String>> variableReplacements = new HashMap<>();
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
        variableReplacements.put("%regionkinddisplayinregionfinder%", () -> {
            return Messages.convertYesNo(this.isDisplayInRegionfinder());
        });
        variableReplacements.put("%regionkindlore%", () -> {
            return Messages.getStringList(this.getLore(), x -> ChatColor.translateAlternateColorCodes('&', x), "\n");
        });
        variableReplacements.put("%regionkindlorelist%", () -> {
            return Messages.getStringList(this.getLore(), x -> ChatColor.translateAlternateColorCodes('&', "&r- " + x), "\n");
        });
        variableReplacements.put("%regionkindregionkindgroups%", () -> {
            return Messages.getStringList(AdvancedRegionMarket.getInstance()
                    .getRegionKindGroupManager().getRegionKindGroupsForRegionKind(this),
                    x -> x.getName(), ", ");
        });
        variableReplacements.put("%regionkindregionkindgroupsdisplay%", () -> {
            return Messages.getStringList(AdvancedRegionMarket.getInstance()
                            .getRegionKindGroupManager().getRegionKindGroupsForRegionKind(this),
                    x -> x.getDisplayName(), ", ");
        });
        variableReplacements.put("%regionkindregionkindgroupsdisplaywithbrackets%", () -> {
            String msg =  Messages.getStringList(AdvancedRegionMarket.getInstance()
                            .getRegionKindGroupManager().getRegionKindGroupsForRegionKind(this),
                    x -> x.getDisplayName(), ", ");
            return msg.isEmpty() ? "" : "(" + msg + ")";
        });

        this.stringReplacer = new StringReplacer(variableReplacements);
    }

    public RegionKind(String name, Material material, List<String> lore, String displayName, boolean displayInRegionFinder, boolean displayInLimits, int customItemModel) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.displayName = displayName;
        this.displayInRegionFinder = displayInRegionFinder;
        this.displayInLimits = displayInLimits;
        this.needsSave = false;
        this.customItemModel = customItemModel;
    }

    public RegionKind(String name, Material material, List<String> lore, String displayName, boolean displayInRegionFinder, boolean displayInLimits) { this(name, material, lore, displayName, displayInRegionFinder, displayInLimits, -1); }

    public static RegionKind parse(ConfigurationSection confSection, String name) {
        AbstractMaterialFinder materialFinder = AdvancedRegionMarket.getInstance().getMaterialFinder();
        Material material = materialFinder.getMaterial(confSection.getString("item"));
        if (material == null) {
            material = materialFinder.getRedBed();
        }
        int customItemModel = confSection.getInt("customItemModel", -1);
        String displayName = confSection.getString("displayName");
        boolean displayInLimits = confSection.getBoolean("displayInLimits");
        boolean displayInRegionfinder = confSection.getBoolean("displayInRegionfinder");
        List<String> lore = new ArrayList<>(confSection.getStringList("lore"));

        return new RegionKind(name, material, lore, displayName, displayInRegionfinder, displayInLimits, customItemModel);
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

    public int getCustomItemModel() {
    	return customItemModel;
    }

    public void setCustomItemModel(int customItemModel) {
    	this.customItemModel = customItemModel;
    	this.queueSave();
    }

    public String replaceVariables(String message) {
        return this.stringReplacer.replace(message).toString();
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection confSection = new YamlConfiguration();
        confSection.set("item", this.getMaterial().toString());
        if(this.customItemModel != -1) { confSection.set("customItemModel", this.getCustomItemModel()); }
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

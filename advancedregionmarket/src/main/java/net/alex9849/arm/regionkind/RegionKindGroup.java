package net.alex9849.arm.regionkind;

import net.alex9849.arm.Messages;
import net.alex9849.arm.util.Saveable;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class RegionKindGroup implements Iterable<RegionKind>, LimitGroupElement, Saveable {
    private String name;
    private String displayName;
    private boolean displayInLimits = true;
    private Set<RegionKind> regionKinds = new HashSet<>();
    private boolean needsSave = false;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%regionkindgroupdisplay%", () -> {
            return ChatColor.translateAlternateColorCodes('&', this.getDisplayName());
        });
        variableReplacements.put("%regionkindgroup%", () -> {
            return this.getName();
        });
        variableReplacements.put("%currency%", () -> {
            return Messages.CURRENCY;
        });
        variableReplacements.put("%regionkindgroupdisplayinlimits%", () -> {
            return Messages.convertYesNo(this.isDisplayInLimits());
        });
        variableReplacements.put("%regionkindgroupmembers%", () -> {
            return Messages.getStringList(this.regionKinds, x -> x.getName(), ", ");
        });
        variableReplacements.put("%regionkindgroupmembersdisplay%", () -> {
            return Messages.getStringList(this.regionKinds, x -> x.getDisplayName(), ", ");
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 20);
    }

    public RegionKindGroup(String name) {
        this.name = name;
        this.displayName = name;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.queueSave();
    }

    public boolean isDisplayInLimits() {
        return displayInLimits;
    }

    public void setDisplayInLimits(boolean displayInLimits) {
        this.displayInLimits = displayInLimits;
        this.queueSave();
    }

    public boolean addRegionKind(RegionKind regionKind) {
        if(this.regionKinds.add(regionKind)) {
            this.queueSave();
            return true;
        }
        return false;
    }

    public boolean removeRegionKind(RegionKind regionKind) {
        if(this.regionKinds.remove(regionKind)) {
            this.queueSave();
            return true;
        }
        return false;
    }

    public boolean contains(RegionKind regionKind) {
        return this.regionKinds.contains(regionKind);
    }

    @Override
    public Iterator<RegionKind> iterator() {
        return this.regionKinds.iterator();
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection confSection = new YamlConfiguration();
        confSection.set("displayName", this.displayName);
        confSection.set("displayInLimits", this.displayInLimits);
        confSection.set("regionKinds", this.regionKinds.stream().map(x -> x.getName()).collect(Collectors.toList()));
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

    public String replaceVariables(String msg) {
        return this.stringReplacer.replace(msg).toString();
    }
}

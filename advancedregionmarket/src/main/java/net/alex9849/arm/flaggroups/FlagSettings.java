package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FlagSettings {
    private Flag flag;
    private boolean editable;
    private String settings;
    private Set<SellType> applyTo;
    private List<String> guidescription;
    private String editPermission;

    public FlagSettings(Flag flag, boolean editable, String settings, Set<SellType> applyTo, List<String> guidescription, String editPermission) {
        this.flag = flag;
        this.editable = editable;
        this.settings = settings;
        this.applyTo = applyTo;
        this.guidescription = new ArrayList<>(guidescription);
        this.editPermission = editPermission;
    }

    public Flag getFlag() {
        return flag;
    }

    public List<String> getGuidescription() {
        List<String> convertedDescription = new ArrayList<>();

        for (String msg : this.guidescription) {
            convertedDescription.add(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return convertedDescription;
    }

    public List<String> getRawGuiDescription() {
        return new ArrayList<>(this.guidescription);
    }

    public boolean isEditable() {
        return editable;
    }

    public Set<SellType> getApplyTo() {
        return applyTo;
    }

    public String getSettings() {
        return settings;
    }

    public String getEditPermission() {
        return this.editPermission;
    }

    public boolean hasEditPermission() {
        return !this.editPermission.equals("");
    }

    public void applyTo(Region region) {
        if (this.getSettings() == null || this.getSettings().isEmpty()
                || this.getSettings().equalsIgnoreCase("remove")) {
            region.getRegion().deleteFlags(this.getFlag());
        } else {
            RegionGroupFlag groupFlag = this.getFlag().getRegionGroupFlag();
            String settings = null;
            RegionGroup groupFlagSettings = null;

            if (groupFlag == null) {
                settings = this.getSettings();
            } else {
                for (String part : this.getSettings().split(" ")) {
                    if (part.startsWith("g:")) {
                        if (part.length() > 2) {
                            try {
                                groupFlagSettings = AdvancedRegionMarket.getInstance().getWorldGuardInterface().parseFlagInput(groupFlag, part.substring(2));
                            } catch (InvalidFlagFormat iff) {
                                Bukkit.getLogger().info("Could not parse groupflag-settings for groupflag " + groupFlag.getName() + "! Flag will be ignored! Please check your flaggroups.yml");
                                continue;
                            }
                        }
                    } else {
                        if (settings == null) {
                            settings = part;
                        } else {
                            settings += " " + part;
                        }
                    }
                }
            }

            if (settings != null) {
                try {
                    Object wgFlagSettings = AdvancedRegionMarket.getInstance().getWorldGuardInterface().parseFlagInput(this.getFlag(), region.replaceVariables(settings));
                    region.getRegion().setFlag(this.getFlag(), wgFlagSettings);
                } catch (InvalidFlagFormat invalidFlagFormat) {
                    Bukkit.getLogger().info("Could not parse flag-settings for flag " + this.getFlag().getName() + "! Flag will be ignored! Please check your flaggroups.yml");
                    return;
                }
            }
            if (groupFlagSettings != null) {
                region.getRegion().setFlag(groupFlag, groupFlagSettings);
            }
        }
    }
}

package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import net.alex9849.arm.regions.SellType;

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
}

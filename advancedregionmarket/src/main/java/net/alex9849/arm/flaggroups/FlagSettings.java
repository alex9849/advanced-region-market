package net.alex9849.arm.flaggroups;

import com.sk89q.worldguard.protection.flags.Flag;
import net.alex9849.arm.regions.SellType;

import java.util.Set;

public class FlagSettings {
    private Flag flag;
    private boolean editable;
    private String settings;
    private Set<SellType> applyTo;

    public FlagSettings(Flag flag, boolean editable, String settings, Set<SellType> applyTo) {
        this.flag = flag;
        this.editable = editable;
        this.settings = settings;
        this.applyTo = applyTo;
    }

    public Flag getFlag() {
        return flag;
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
}

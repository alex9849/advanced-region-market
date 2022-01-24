package net.alex9849.arm.flaggroups;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PresetContent {

    private String settings;
    private String permission;
    private List<String> description;

    public PresetContent(String settings, String permission, List<String> description) {
        this.settings = settings;
        this.permission = permission;
        this.description = new ArrayList<>(description);
    }

    public String getSettings() {
        return settings;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getDescription() {
        List<String> convertedDescription = new ArrayList<>();

        for (String msg : this.description) {
            convertedDescription.add(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return convertedDescription;
    }
}

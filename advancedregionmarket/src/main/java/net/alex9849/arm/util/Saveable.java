package net.alex9849.arm.util;

import org.bukkit.configuration.ConfigurationSection;

public interface Saveable {

    public ConfigurationSection toConfigureationSection();

    public void queueSave();

    public void setSaved();

    public boolean needSave();
}

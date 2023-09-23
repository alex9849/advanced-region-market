package net.alex9849.arm.adapters.util;

import org.bukkit.configuration.ConfigurationSection;

public interface Saveable {

    ConfigurationSection toConfigurationSection();

    void queueSave();

    void setSaved();

    boolean needsSave();
}

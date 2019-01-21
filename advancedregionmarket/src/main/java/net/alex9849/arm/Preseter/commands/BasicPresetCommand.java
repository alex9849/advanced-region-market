package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class BasicPresetCommand {

    public abstract boolean matchesRegex(String command);

    public abstract String getRootCommand();

    public abstract String getUsage();

    public abstract boolean runCommand(Player player, String[] args, String allargs, PresetType presetType) throws InputException;

    public abstract List<String> onTabComplete(Player player, String args[], PresetType presetType);

}

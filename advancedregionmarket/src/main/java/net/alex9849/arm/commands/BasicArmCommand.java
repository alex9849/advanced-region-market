package net.alex9849.arm.commands;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class BasicArmCommand {

    protected abstract boolean matchesRegex(String command);

    public abstract String getRootCommand();

    public abstract boolean runCommand(String args[]);

    public abstract List<String> onTabComplete(Player player, String args[]);
}

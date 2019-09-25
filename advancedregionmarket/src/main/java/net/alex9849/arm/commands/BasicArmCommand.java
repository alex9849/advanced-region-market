package net.alex9849.arm.commands;

import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface BasicArmCommand {

    boolean matchesRegex(String command);

    String getRootCommand();

    List<String> getUsage();

    boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException;

    List<String> onTabComplete(Player player, String args[]);
}

package net.alex9849.arm.flaggroups.commands;

import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveFlag implements BasicArmCommand {
    @Override
    public boolean matchesRegex(String command) {
        return false;
    }

    @Override
    public String getRootCommand() {
        return null;
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException {
        return false;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return null;
    }
}

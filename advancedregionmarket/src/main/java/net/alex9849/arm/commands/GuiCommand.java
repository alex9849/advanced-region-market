package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.gui.Gui;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCommand extends BasicArmCommand {
    private final String rootCommand = "gui";
    private final String regex = "(?i)gui";
    private final List<String> usage = new ArrayList<>(Arrays.asList("gui"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (sender instanceof Player) {
            if (sender.hasPermission(Permission.MEMBER_GUI)) {
                Gui.openARMGui((Player) sender);
                return true;
            } else {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
        } else {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.MEMBER_GUI)) {
                    returnme.add(this.rootCommand);
                }
            }
        }

        return returnme;
    }
}

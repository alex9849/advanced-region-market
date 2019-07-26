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

public class RegionfinderCommand extends BasicArmCommand {

    private final String rootCommand = "regionfinder";
    private final String regex = "(?i)regionfinder";
    private final List<String> usage = new ArrayList<>(Arrays.asList("regionfinder"));
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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if(!player.hasPermission(Permission.MEMBER_REGIONFINDER)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        Gui.openRegionFinder(player, false);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(!player.hasPermission(Permission.MEMBER_REGIONFINDER)) {
            return returnme;
        }

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(args.length == 1) {
                    returnme.add(this.rootCommand);
                }
            }
        }

        return returnme;
    }
}

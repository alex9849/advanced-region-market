package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.subregions.SubRegionCreator;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends BasicArmCommand {
    private final String rootCommand = "create";
    private final String regex = "(?i)create";
    private final List<String> usage = new ArrayList<>(Arrays.asList("create"));

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
        if(!Permission.hasAnySubregionCreatePermission(sender)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        SubRegionCreator selection = SubRegionCreator.getSubRegioncreator(player);
        if(selection == null) {
            throw new InputException(player, Messages.SELECTION_INVALID);
        }
        selection.createWGRegion();
        for(String msg : Messages.SELECTION_SAVED_CREATE_SIGN) {
            player.sendMessage(Messages.PREFIX + msg);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(Permission.hasAnySubregionCreatePermission(player)) {
            if(args.length == 1) {
                if(this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            }
        }

        return returnme;
    }
}

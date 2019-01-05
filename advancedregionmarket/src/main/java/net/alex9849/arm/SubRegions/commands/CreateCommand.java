package net.alex9849.arm.SubRegions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.SubRegions.Mark;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.inter.WGRegion;
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
        if(!sender.hasPermission(Permission.SUBREGION_CREATE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Mark selection = Mark.getMark(player);
        if(selection == null) {
            //TODO
            throw new InputException(player, "No selection");
        }
        WGRegion wgRegion = selection.createWGRegion();
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<String>();
    }
}

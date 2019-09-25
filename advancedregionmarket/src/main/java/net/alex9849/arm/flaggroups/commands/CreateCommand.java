package net.alex9849.arm.flaggroups.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand implements BasicArmCommand {
    private final String rootCommand = "create";
    private final String regex = "(?i)create [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("create [FLAGGROUPNAME]"));

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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException {
        //TODO CHANGE MESSAGES
        if(!sender.hasPermission(Permission.ADMIN_FLAGGROUP_CREATE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        FlagGroup createdFlagGroup = new FlagGroup(args[1], 20, new ArrayList(), new ArrayList<>());
        AdvancedRegionMarket.getInstance().getFlagGroupManager().add(createdFlagGroup);
        sender.sendMessage(Messages.PREFIX + "FlagGroup created!");

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_FLAGGROUP_CREATE)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }
}

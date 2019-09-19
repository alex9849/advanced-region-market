package net.alex9849.arm.flaggroups.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand implements BasicArmCommand {
    private final String rootCommand = "delete";
    private final String regex = "(?i)delete [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("delete [REGIONKIND]"));

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
        if (!sender.hasPermission(Permission.ADMIN_FLAGGROUP_DELETE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        //TODO CHANGE MESSAGES
        FlagGroup flagGroup = AdvancedRegionMarket.getARM().getFlagGroupManager().getFlagGroup(args[1]);
        if(flagGroup == null) {
            throw new InputException(sender, "FlagGroup does not exist!");
        }
        if(flagGroup == FlagGroup.DEFAULT) {
            throw new InputException(sender, "Can't remove default FlagGroup!");
        }
        if(flagGroup == FlagGroup.SUBREGION) {
            throw new InputException(sender, "Can't remove subregion FlagGroup!");
        }

        AdvancedRegionMarket.getARM().getFlagGroupManager().remove(flagGroup);

        for(Region region : AdvancedRegionMarket.getARM().getRegionManager()) {
            if(region.getFlagGroup() == flagGroup) {
                region.setFlagGroup(FlagGroup.DEFAULT);
            }
        }

        sender.sendMessage(Messages.PREFIX + "FlagGroup deleted!");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.ADMIN_FLAGGROUP_DELETE)) {
            return returnme;
        }

        if(args.length >= 1) {
            if(args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                returnme.addAll(AdvancedRegionMarket.getARM().getFlagGroupManager().tabCompleteFlaggroup(args[1]));
            }
        }
        return returnme;
    }
}

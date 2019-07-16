package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCommand extends BasicArmCommand {
    private final String rootCommand = "list";
    private final String regex = "(?i)list";
    private final List<String> usage = new ArrayList<>(Arrays.asList("list"));

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
        if (!sender.hasPermission(Permission.ADMIN_ENTITYLIMIT_LIST)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        sender.sendMessage(Messages.ENTITYLIMITGROUP_LIST_HEADLINE);
        for(EntityLimitGroup entityLimitGroup : AdvancedRegionMarket.getEntityLimitGroupManager()) {
            sender.sendMessage("- " + entityLimitGroup.getName());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_ENTITYLIMIT_LIST)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }
}
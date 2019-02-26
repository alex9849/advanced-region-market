package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfoCommand extends BasicArmCommand {
    private final String rootCommand = "info";
    private final String regex = "(?i)info [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("info [GROUPNAME]"));

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
        if (!sender.hasPermission(Permission.MEMBER_ENTITYLIMIT_INFO)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        EntityLimitGroup entityLimitGroup = EntityLimitGroupManager.getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        sendInfoToPlayer(player, entityLimitGroup);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.MEMBER_ENTITYLIMIT_INFO)) {
            return returnme;
        }

        if(args.length >= 1) {
            if(args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.addAll(EntityLimitGroupManager.tabCompleteEntityLimitGroups(args[1]));

                }
            }
        }
        return returnme;
    }

    public static void sendInfoToPlayer(Player player, EntityLimitGroup entityLimitGroup) {
        player.sendMessage(Messages.ENTITYLIMITGROUP_INFO_HEADLINE);
        player.sendMessage(Messages.ENTITYLIMITGROUP_INFO_GROUPNAME + entityLimitGroup.getName());
        String totalstatus = entityLimitGroup.getConvertedMessage(Messages.ENTITYLIMITGROUP_INFO_PATTERN, new ArrayList<>(), 0);
        if(entityLimitGroup.getSoftLimit(0) < entityLimitGroup.getHardLimit()) {
            totalstatus = totalstatus.replace("%entityextensioninfo%", entityLimitGroup.getConvertedMessage(Messages.ENTITYLIMITGROUP_INFO_EXTENSION_INFO, new ArrayList<>(), 0));
        } else {
            totalstatus = totalstatus.replace("%entityextensioninfo%", "");
        }
        player.sendMessage(totalstatus);

        for(EntityLimit entityLimit : entityLimitGroup.getEntityLimits()) {
            String entitystatus = entityLimit.getConvertedMessage(Messages.ENTITYLIMITGROUP_INFO_PATTERN, new ArrayList<>(), 0);
            if(entityLimit.getSoftLimit(0) < entityLimit.getHardLimit()) {
                entitystatus = entitystatus.replace("%entityextensioninfo%", entityLimit.getConvertedMessage(Messages.ENTITYLIMITGROUP_INFO_EXTENSION_INFO, new ArrayList<>(), 0));
            } else {
                entitystatus = entitystatus.replace("%entityextensioninfo%", "");
            }
            player.sendMessage(entitystatus);
        }
    }
}

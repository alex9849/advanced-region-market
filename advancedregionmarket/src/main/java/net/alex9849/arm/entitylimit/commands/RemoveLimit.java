package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveLimit implements BasicArmCommand {
    private final String rootCommand = "removelimit";
    private final String regex = "(?i)removelimit [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("removelimit [GROUPNAME] [ENTITYTYPE]"));

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
        if (!sender.hasPermission(Permission.ADMIN_ENTITYLIMIT_REMOVE_LIMIT)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        if(args[2].equalsIgnoreCase("total")) {
            entityLimitGroup.setSoftLimit(Integer.MAX_VALUE);
            sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_REMOVED);
            return true;
        } else {
            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);
            if(limitableEntityType == null) {
                throw new InputException(sender, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }


            for(int i = 0; i < entityLimitGroup.getEntityLimits().size(); i++) {
                if(entityLimitGroup.getEntityLimits().get(i).getLimitableEntityType() == limitableEntityType) {
                    entityLimitGroup.getEntityLimits().remove(i);
                    entityLimitGroup.queueSave();
                    sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_REMOVED);
                    return true;
                }
            }
        }

        throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_CONTAIN_ENTITYLIMIT);
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.ADMIN_ENTITYLIMIT_REMOVE_LIMIT)) {
            return returnme;
        }

        if(args.length >= 1) {
            if(args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]));

                }
            } else if((args.length == 3) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
                    if(entityLimitGroup == null) {
                        return returnme;
                    }
                    for(EntityLimit entityLimit : entityLimitGroup.getEntityLimits()) {
                        if(entityLimit.getLimitableEntityType().toString().toLowerCase().startsWith(args[2])) {
                            returnme.add(entityLimit.getLimitableEntityType().toString());
                        }
                    }
                    if("total".startsWith(args[2])) {
                        returnme.add("total");
                    }
                }
            }
        }
        return returnme;
    }
}
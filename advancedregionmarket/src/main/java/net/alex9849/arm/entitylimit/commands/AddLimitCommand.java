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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddLimitCommand extends BasicArmCommand {
    private final String rootCommand = "addlimit";
    private final String regex = "(?i)addlimit [^;\n ]+ [^;\n ]+ [0-9]+ [0-9]+ [0-9]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("addlimit [GROUPNAME] [ENTITYTYPE] [SOFTLIMIT] [HARDLIMIT] [PRICE PER EXTRA-ENTITY]"));

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
        if (!sender.hasPermission(Permission.ADMIN_ENTITYLIMIT_ADD_LIMIT)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        EntityLimitGroup entityLimitGroup = EntityLimitGroupManager.getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        if(args[2].equalsIgnoreCase("total")) {
            entityLimitGroup.setTotalLimit(Integer.parseInt(args[3]));
            sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_SET);
        } else {
            EntityType entityType = null;
            try {
                entityType = EntityType.valueOf(args[2]);
            } catch (IllegalArgumentException e) {
                throw new InputException(sender, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }


            for(int i = 0; i < entityLimitGroup.getEntityLimits().size(); i++) {
                if(entityLimitGroup.getEntityLimits().get(i).getEntityType() == entityType) {
                    entityLimitGroup.getEntityLimits().remove(i);
                    entityLimitGroup.queueSave();
                }
            }
            entityLimitGroup.getEntityLimits().add(new EntityLimit(entityType, Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
            entityLimitGroup.queueSave();
            sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_SET);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.ADMIN_ENTITYLIMIT_ADD_LIMIT)) {
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
            } else if((args.length == 3) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                for(EntityType entityType : EntityType.values()) {
                    if(entityType.toString().toLowerCase().startsWith(args[2])) {
                        returnme.add(entityType.toString());
                    }
                }
                if("total".startsWith(args[2])) {
                    returnme.add("total");
                }
            }
        }
        return returnme;
    }
}
package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AddLimitCommand extends BasicArmCommand {
    private final String rootCommand = "addlimit";
    private final String regex = "(?i)addlimit [^;\n ]+ [^;\n ]+ ([0-9]+|(?i)unlimited) ([0-9]+|(?i)unlimited) [0-9]+";
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
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        int softLimit;
        int hardLimit;
        int pricePerExtraEntity = Integer.parseInt(args[5]);

        if(args[3].equalsIgnoreCase("unlimited")) {
            softLimit = Integer.MAX_VALUE;
        } else {
            softLimit = Integer.parseInt(args[3]);
        }

        if(args[4].equalsIgnoreCase("unlimited")) {
            hardLimit = Integer.MAX_VALUE;
        } else {
            hardLimit = Integer.parseInt(args[4]);
        }

        if(hardLimit == -1) {
            hardLimit = Integer.MAX_VALUE;
        }
        if(softLimit == -1) {
            softLimit = Integer.MAX_VALUE;
        }


        if(args[2].equalsIgnoreCase("total")) {
            entityLimitGroup.setSoftLimit(softLimit);
            entityLimitGroup.setHardLimit(hardLimit);
            entityLimitGroup.setPricePerExtraEntity(pricePerExtraEntity);
            sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMIT_SET);
        } else {
            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);

            if(limitableEntityType == null) {
                throw new InputException(sender, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }

            final EntityLimit.LimitableEntityType finalLimitableEntity = limitableEntityType;
            entityLimitGroup.getEntityLimits().removeIf(new Predicate<EntityLimit>() {
                @Override
                public boolean test(EntityLimit entityLimit) {
                    return entityLimit.getLimitableEntityType() == finalLimitableEntity;
                }
            });

            if(softLimit != Integer.MAX_VALUE) {
                entityLimitGroup.getEntityLimits().add(new EntityLimit(limitableEntityType, softLimit, hardLimit, pricePerExtraEntity));
            }

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
                    returnme.addAll(AdvancedRegionMarket.getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]));

                }
            } else if((args.length == 3) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                for(EntityLimit.LimitableEntityType entityType : EntityLimit.entityTypes) {
                    if(entityType.getName().toLowerCase().startsWith(args[2])) {
                        returnme.add(entityType.toString());
                    }
                }
                if("total".startsWith(args[2])) {
                    returnme.add("total");
                }
            } else if((args.length == 4) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if("unlimited".startsWith(args[3])) {
                    returnme.add("unlimited");
                }
            } else if((args.length == 5) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if("unlimited".startsWith(args[4])) {
                    returnme.add("unlimited");
                }
            }
        }
        return returnme;
    }
}
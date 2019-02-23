package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.Messages;
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
    private final String regex = "(?i)addlimit [^;\n ]+ [^;\n ]+ [0-9]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("addlimit [GROUPNAME] [ENTITYTYPE] [LIMIT]"));

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
        //TODO
        EntityLimitGroup entityLimitGroup = EntityLimitGroupManager.getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, "Group does not exists!");
        }

        if(args[2].equalsIgnoreCase("total")) {
            entityLimitGroup.setTotalLimit(Integer.parseInt(args[3]));
            sender.sendMessage(Messages.PREFIX + "Entitylimit has been set!");
        } else {
            EntityType entityType = null;
            try {
                entityType = EntityType.valueOf(args[2]);
            } catch (IllegalArgumentException e) {
                throw new InputException(sender, "The entitytype " + args[2] + " does not exist!");
            }


            for(int i = 0; i < entityLimitGroup.getEntityLimits().size(); i++) {
                if(entityLimitGroup.getEntityLimits().get(i).getEntityType() == entityType) {
                    entityLimitGroup.getEntityLimits().remove(i);
                    entityLimitGroup.queueSave();
                }
            }
            entityLimitGroup.getEntityLimits().add(new EntityLimit(entityType, Integer.parseInt(args[3])));
            entityLimitGroup.queueSave();
            sender.sendMessage(Messages.PREFIX + "Entitylimit has been set!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
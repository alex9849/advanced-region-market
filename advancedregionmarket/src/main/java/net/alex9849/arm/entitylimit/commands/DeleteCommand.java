package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends BasicArmCommand {
    private final String rootCommand = "delete";
    private final String regex = "(?i)delete [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("delete [GROUPNAME]"));

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
        if (!sender.hasPermission(Permission.ADMIN_ENTITYLIMIT_DELETE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }
        if(entityLimitGroup == EntityLimitGroup.DEFAULT) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM);
        }
        if(entityLimitGroup == EntityLimitGroup.SUBREGION) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_CAN_NOT_REMOVE_SYSTEM);
        }

        AdvancedRegionMarket.getEntityLimitGroupManager().remove(entityLimitGroup);

        for(Region region : AdvancedRegionMarket.getRegionManager()) {
            if(region.getEntityLimitGroup() == entityLimitGroup) {
                region.setEntityLimitGroup(EntityLimitGroup.DEFAULT);
            }
        }

        sender.sendMessage(Messages.PREFIX + Messages.ENTITYLIMITGROUP_DELETED);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.ADMIN_ENTITYLIMIT_DELETE)) {
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
            }
        }
        return returnme;
    }
}

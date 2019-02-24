package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.entitylimit.EntityLimitGroupManager;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends BasicArmCommand {

    private final String rootCommand = "create";
    private final String regex = "(?i)create [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("create [GROUPNAME]"));

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
        if(EntityLimitGroupManager.getEntityLimitGroup(args[1]) != null) {
            throw new InputException(sender, "Group already exists!");
        }
        EntityLimitGroupManager.add(new EntityLimitGroup(new ArrayList<>(), -1, args[1]));
        //TODO
        sender.sendMessage(Messages.PREFIX + "Entitylimitgroup has been created");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_ENTITYLIMIT_DELETE)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }
}

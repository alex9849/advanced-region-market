package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LimitCommand extends BasicArmCommand {

    private final String rootCommand = "limit";
    private final String regex = "(?i)limit";
    private final List<String> usage = new ArrayList<>(Arrays.asList("limit"));

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
        if (sender.hasPermission(Permission.MEMBER_LIMIT)) {
            if(!(sender instanceof Player)){
                throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
            }
            Player player = (Player) sender;

            LimitGroup.getLimitChat(player);

            return true;
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.MEMBER_LIMIT)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }
}

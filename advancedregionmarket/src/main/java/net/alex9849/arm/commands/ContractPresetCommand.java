package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.ContractPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ContractPresetCommand extends SellPresetCommand {

    private final String rootCommand = "contractpreset";
    private final String regex = "(?i)contractpreset [^;\n]+";
    private final String usage = "/arm contractpreset [SETTING] or (for help): /arm contractpreset help";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (sender.hasPermission(Permission.ADMIN_PRESET)) {

            String newallargs = "";
            String[] newargs = new String[args.length - 1];

            for (int i = 1; i < args.length; i++) {
                newargs[i - 1] = args[i];
                if(i == 1) {
                    newallargs = args[i];
                } else {
                    newallargs = newallargs + " " + args[i];
                }
            }

            return ContractPreset.onCommand(sender, newallargs, newargs);
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (!player.hasPermission(Permission.ADMIN_PRESET)) {
            return returnme;
        }

        String[] newargs = new String[args.length - 1];

        for(int i = 1; i < args.length; i++) {
            newargs[i - 1] = args[i];
        }

        if(args.length == 1) {
            if(this.rootCommand.startsWith(args[0])) {
                returnme.add(this.rootCommand);
            }
        }
        if(args.length >= 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
            returnme.addAll(ContractPreset.onTabComplete(player, newargs));
        }
        return returnme;
    }

}

package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.Preseter.commands.BasicPresetCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SellPresetCommand extends BasicArmCommand {

    private final String rootCommand = "sellpreset";
    private final String regex = "(?i)sellpreset [^;\n]+";
    private final String usage = "/arm sellpreset [SETTING] or (for help): /arm sellpreset help";

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
            return SellPreset.onCommand(sender, allargs, args);
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_PRESET)) {
                    returnme.addAll(SellPreset.tabCompletePreset(player, args));
                }
            }
        }
        return returnme;
    }

}

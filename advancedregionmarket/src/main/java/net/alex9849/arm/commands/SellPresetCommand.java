package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellPresetCommand extends BasicArmCommand {

    private final String rootCommand = "sellpreset";
    private final String regex = "(?i)sellpreset [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("sellpreset [SETTING]", "sellpreset help"));
    private CommandHandler commandHandler;

    public SellPresetCommand() {
        this.commandHandler = new CommandHandler(this.usage, this.rootCommand);
        List<BasicArmCommand> commands = new ArrayList<>();
        commands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.DeleteCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.DoBlockResetCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.HelpCommand(PresetType.SELLPRESET, this.commandHandler));
        commands.add(new net.alex9849.arm.presets.commands.HotelCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.InfoCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.ListCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.LoadCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.PriceCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.RegionKindCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.ResetCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.SaveCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.AddCommandCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.RemoveCommandCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.AllowedSubregionsCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.UserResettableCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.SetAutoPriceCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.EntityLimitCommand(PresetType.SELLPRESET));
        commands.add(new net.alex9849.arm.presets.commands.FlaggroupCommand(PresetType.SELLPRESET));
        this.commandHandler.addCommands(commands);
    }

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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException {
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

            return  this.commandHandler.executeCommand(sender, cmd , commandsLabel, newargs);
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
            returnme.addAll(this.commandHandler.onTabComplete(player, newargs));
        }
        return returnme;
    }

}

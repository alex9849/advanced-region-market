package net.alex9849.arm.commands;

import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.Preseter.presets.RentPreset;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RentPresetCommand extends SellPresetCommand {

    private final String rootCommand = "rentpreset";
    private final String regex = "(?i)rentpreset [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("rentpreset [SETTING]", "rentpreset help"));

    private CommandHandler commandHandler;

    public RentPresetCommand() {
        this.commandHandler = new CommandHandler(this.usage, this.rootCommand);
        List<BasicArmCommand> commands = new ArrayList<>();
        commands.add(new net.alex9849.arm.Preseter.commands.AutoResetCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.RentPresetExtendPerClickCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.RentPresetMaxRentTimeCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.DeleteCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.DoBlockResetCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.HelpCommand(PresetType.RENTPRESET, this.commandHandler));
        commands.add(new net.alex9849.arm.Preseter.commands.HotelCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.InfoCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.ListCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.LoadCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.PriceCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.RegionKindCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.ResetCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.SaveCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.AddCommandCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.RemoveCommandCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.AllowedSubregionsCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.UserResettableCommand(PresetType.RENTPRESET));
        commands.add(new net.alex9849.arm.Preseter.commands.SetAutoPriceCommand(PresetType.RENTPRESET));
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

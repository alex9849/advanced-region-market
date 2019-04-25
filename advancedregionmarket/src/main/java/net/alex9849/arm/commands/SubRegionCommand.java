package net.alex9849.arm.commands;

import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.subregions.commands.CreateCommand;
import net.alex9849.arm.subregions.commands.DeleteCommand;
import net.alex9849.arm.subregions.commands.HotelCommand;
import net.alex9849.arm.subregions.commands.ResetBlocksCommand;
import net.alex9849.arm.subregions.commands.TPCommand;
import net.alex9849.arm.subregions.commands.ToolCommand;
import net.alex9849.arm.subregions.commands.UnsellCommand;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubRegionCommand extends BasicArmCommand {
    private final String rootCommand = "subregion";
    private final String regex = "(?i)subregion [^;\n]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("subregion [SETTING]", "subregion help"));
    private CommandHandler commandHandler;

    public SubRegionCommand() {
        this.commandHandler = new CommandHandler(this.usage, this.rootCommand);
        String[] betweencmds = {this.rootCommand};
        List<BasicArmCommand> commands = new ArrayList<>();
        commands.add(new ToolCommand());
        commands.add(new CreateCommand());
        commands.add(new HotelCommand());
        commands.add(new TPCommand());
        commands.add(new ResetBlocksCommand());
        commands.add(new UnsellCommand());
        commands.add(new DeleteCommand());
        commands.add(new HelpCommand(this.commandHandler, Messages.SUBREGION_HELP_HEADLINE, betweencmds, Permission.SUBREGION_HELP));
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
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if(Permission.hasAnySubregionPermission(player)) {
                    if(args.length == 1) {
                        if(this.rootCommand.startsWith(args[0])) {
                            returnme.add(this.rootCommand);
                        }
                    }
                    if(args.length > 1) {
                        String[] newargs = new String[args.length - 1];
                        for(int i = 0; i < newargs.length; i++) {
                            newargs[i] = args[i + 1];
                        }
                        returnme.addAll(this.commandHandler.onTabComplete(player, newargs));
                    }
                }
            }
        }
        return returnme;
    }
}

package net.alex9849.arm.commands;

import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSplitter extends BasicArmCommand {

    private final String rootCommand;
    private final String regex;
    private final List<String> usage;
    private CommandHandler commandHandler;

    public CommandSplitter(String rootCommand, String regex, List<String> usage, String helpCommandPermission, String helpCommandHeadline, Collection<BasicArmCommand> commands) {
        this.rootCommand = rootCommand;
        this.regex = regex;
        this.usage = new ArrayList<>(usage);
        this.commandHandler = new CommandHandler(usage, rootCommand);
        if(commands != null) {
            this.commandHandler.addCommands(commands);
        }
        this.commandHandler.addCommand(new HelpCommand(this.commandHandler, helpCommandHeadline, new String[]{rootCommand}, helpCommandPermission));
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

        String[] newargs = new String[args.length - 1];

        for(int i = 1; i < args.length; i++) {
            newargs[i - 1] = args[i];
        }

        if(args.length == 1) {
            if(this.rootCommand.startsWith(args[0]) && this.commandHandler.onTabComplete(player, new String[]{""}).size() != 0) {
                returnme.add(this.rootCommand);
            }
        }
        if(args.length >= 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
            returnme.addAll(this.commandHandler.onTabComplete(player, newargs));
        }
        return returnme;
    }

}

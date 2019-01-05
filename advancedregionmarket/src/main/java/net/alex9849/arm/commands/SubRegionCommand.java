package net.alex9849.arm.commands;

import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.SubRegions.commands.CreateCommand;
import net.alex9849.arm.SubRegions.commands.ToolCommand;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.ChatColor;
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
        List<BasicArmCommand> commands = new ArrayList<>();
        commands.add(new ToolCommand());
        commands.add(new CreateCommand());
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
        return new ArrayList<String>();
    }
}

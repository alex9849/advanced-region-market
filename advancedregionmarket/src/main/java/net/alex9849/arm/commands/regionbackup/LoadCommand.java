package net.alex9849.arm.commands.regionbackup;

import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO Finalize
public class LoadCommand extends BasicArmCommand {

    private final String rootCommand = "load";
    private final String regex_with_args = "(?i)load [^;\n ]+ [^;\n ]+";
    private final String regex = "(?i)load";
    private final List<String> usage = new ArrayList<>(Arrays.asList("load [REGION] [NAME]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_with_args);
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
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        return returnme;
    }
}

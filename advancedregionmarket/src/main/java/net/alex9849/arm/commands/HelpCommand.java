package net.alex9849.arm.commands;

import net.alex9849.arm.Permission;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends BasicArmCommand {

    private final String rootCommand = "help";
    private final String regex = " (?i)help";

    @Override
    protected boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public boolean runCommand(String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (rootCommand.startsWith(args[0])) {
            if (player.hasPermission(Permission.ARM_HELP)) {
                returnme.add("help");
            }
        }
        return returnme;
    }
}

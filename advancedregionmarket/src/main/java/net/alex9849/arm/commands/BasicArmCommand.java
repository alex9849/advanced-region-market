package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BasicArmCommand {
    private final String rootCommand;
    private final boolean isConsoleCommand;
    private final List<String> regexList;
    private final List<String> permissions;
    private final List<String> usage;
    private final AdvancedRegionMarket plugin;

    public BasicArmCommand(boolean isConsoleCommand, AdvancedRegionMarket plugin, String rootCommand, List<String> regexList, List<String> usage,
                           List<String> permissions) {
        this.isConsoleCommand = isConsoleCommand;
        this.rootCommand = rootCommand;
        this.permissions = Collections.unmodifiableList(new ArrayList<>(permissions));
        this.regexList = Collections.unmodifiableList(new ArrayList<>(regexList));
        this.usage = Collections.unmodifiableList(new ArrayList<>(usage));
        this.plugin = plugin;
    }

    public AdvancedRegionMarket getPlugin() {
        return plugin;
    }

    public boolean isConsoleCommand() {
        return this.isConsoleCommand;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public String getRootCommand() {
        return this.rootCommand;
    }

    public List<String> getRegexList() {
        return this.regexList;
    }

    public List<String> getUsage() {
        return this.usage;
    }

    public boolean hasPermission(Player player) {
        return this.getPermissions().isEmpty()
        || this.getPermissions().stream().anyMatch(player::hasPermission);
    }

    public boolean runCommand(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException {
        if (!this.isConsoleCommand() && sender instanceof ConsoleCommandSender) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!hasPermission(player)) {
                throw new NoPermissionException(Messages.NO_PERMISSION);
            }
        }

        boolean hasMinOneRegexMatching = false;
        for (String regex : this.getRegexList()) {
            hasMinOneRegexMatching |= command.matches(regex);
        }

        if (!hasMinOneRegexMatching) {
            throw new CmdSyntaxException(new ArrayList<>(this.getUsage()));
        }

        return this.runCommandLogic(sender, command, commandLabel);
    }

    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!hasPermission(player)) {
            return returnme;
        }
        if (args.length >= 1 && this.getRootCommand().startsWith(args[0])) {
            if (args.length == 1) {
                returnme.add(this.getRootCommand());
            } else if (this.getRootCommand().equalsIgnoreCase(args[0])) {
                returnme.addAll(onTabCompleteArguments(player, args));
            }
        }
        return returnme;
    }

    protected abstract boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException;

    /**
     * @param player Has the permission to execute this command
     * @param args Has at least a length of 2 and the first arg matches the root command
     * @return An ArrayList (not null)
     */
    protected abstract List<String> onTabCompleteArguments(Player player, String[] args);
}

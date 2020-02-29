package net.alex9849.arm.handler;

import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandHandler implements TabCompleter {

    private List<BasicArmCommand> commands;

    public CommandHandler() {
        this.commands = new ArrayList<>();
    }

    public static List<String> tabCompleteOnlinePlayers(String args) {
        List<String> returnme = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(args)) {
                returnme.add(player.getName());
            }
        }
        return returnme;
    }

    public List<BasicArmCommand> getCommands() {
        return this.commands;
    }

    public void addCommands(Collection<? extends BasicArmCommand> commands) {
        this.commands.addAll(commands);
    }

    public void addCommand(BasicArmCommand cmd) {
        this.commands.add(cmd);
    }

    public boolean executeCommand(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        for (BasicArmCommand armCommand : this.commands) {
            if (args[0].equalsIgnoreCase(armCommand.getRootCommand())) {
                return armCommand.runCommand(sender, command, commandLabel);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandsLabel, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!(commandSender instanceof Player)) {
            return returnme;
        }

        Player player = (Player) commandSender;

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        if (command.getName().equalsIgnoreCase("arm")) {
            returnme.addAll(this.onTabComplete(player, args));
        }

        return returnme;
    }

    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        for (int i = 0; i < this.commands.size(); i++) {
            returnme.addAll(this.commands.get(i).onTabComplete(player, args));
        }
        return returnme;
    }

}

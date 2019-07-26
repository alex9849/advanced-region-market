package net.alex9849.arm.handler;

import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
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
    private List<String> usage;
    private String rootcommand;

    public CommandHandler(List<String> usage, String rootcommand) {
        this.usage = usage;
        this.rootcommand = rootcommand;
        this.commands = new ArrayList<>();
    }

    public String getRootcommand() {
        return this.rootcommand;
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

    public boolean executeCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) throws InputException, CmdSyntaxException {
        String allargs = "";

        for (int i = 0; i < args.length; i++) {
            if(i == 0) {
                allargs = args[i];
            } else {
                allargs = allargs + " " + args[i];
            }
        }

        if(args.length >= 1) {
            for(BasicArmCommand command : this.commands) {
                if(command.getRootCommand().equalsIgnoreCase(args[0])) {
                    if(command.matchesRegex(allargs)) {

                        try {
                            return command.runCommand(sender, cmd, commandsLabel, args, allargs);
                        } catch (CmdSyntaxException syntaxException) {
                            List<String> syntax = syntaxException.getSyntax();

                            if(!this.rootcommand.equalsIgnoreCase("")) {
                                for(int i = 0; i < syntax.size(); i++) {
                                    syntax.set(i, this.rootcommand + " " + syntax.get(i));
                                }
                            }
                            throw new CmdSyntaxException(syntax);
                        }

                    } else {
                        List<String> syntax = new ArrayList<>(command.getUsage());

                        if(!this.rootcommand.equalsIgnoreCase("")) {
                            for(int i = 0; i < syntax.size(); i++) {
                                syntax.set(i, this.rootcommand + " " + syntax.get(i));
                            }
                        }
                        throw new CmdSyntaxException(syntax);
                    }
                }
            }
        }
        if(this.usage.size() >= 1) {
            List<String> syntax = new ArrayList<>(this.usage);
            throw new CmdSyntaxException(syntax);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandsLabel, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(!(commandSender instanceof Player)) {
            return returnme;
        }

        Player player = (Player) commandSender;

        for(int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        if(command.getName().equalsIgnoreCase("arm")) {
            returnme.addAll(this.onTabComplete(player, args));
        }

        return returnme;
    }


    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        for(int i = 0; i < this.commands.size(); i++) {
            returnme.addAll(this.commands.get(i).onTabComplete(player, args));
        }
        return returnme;
    }

    public static List<String> tabCompleteOnlinePlayers(String args) {
        List<String> returnme = new ArrayList<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(player.getName().toLowerCase().startsWith(args)) {
                returnme.add(player.getName());
            }
        }
        return returnme;
    }

}

package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends BasicArmCommand {

    private final String rootCommand = "help";
    private final String regex_args = "(?i)help [0-9]+";
    private final String regex = "(?i)help";
    private final List<String> usage = new ArrayList<>(Arrays.asList("help", "help [page]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_args);
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
        if(!sender.hasPermission(Permission.ARM_HELP)){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        int selectedpage;
        if(allargs.matches(this.regex_args)) {
            selectedpage = Integer.parseInt(args[1]);
        } else {
            selectedpage = 1;
        }

        List<BasicArmCommand> commands = AdvancedRegionMarket.getCommandHandler().getCommands();
        List<String> usages = new ArrayList<>();

        Collections.sort(usages);

        for(BasicArmCommand command : commands) {
            usages.addAll(command.getUsage());
        }

        final int commandsPerPage = 7;
        int pages = usages.size() / commandsPerPage;
        if((usages.size() % commandsPerPage) != 0) {
            pages++;
        }

        if(pages < selectedpage) {
            selectedpage = pages;
        }

        int firstCommand = (selectedpage * commandsPerPage) - commandsPerPage;
        int lastCommand = selectedpage * commandsPerPage;

        if(usages.size() < lastCommand) {
            lastCommand = usages.size();
        }

        sender.sendMessage(Messages.HELP_HEADLINE.replace("%actualpage%", selectedpage + "").replace("%maxpage%", pages + ""));
        for(int i = firstCommand; i < lastCommand; i++) {
            sender.sendMessage(ChatColor.GOLD + "/" + commandsLabel + " " + usages.get(i));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {

        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ARM_HELP)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }


}

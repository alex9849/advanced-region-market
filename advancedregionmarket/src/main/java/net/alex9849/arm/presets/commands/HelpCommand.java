package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.InputException;
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
    private final List<String> usage = new ArrayList<>(Arrays.asList("help"));
    private PresetType presetType;
    private CommandHandler cmdHandler;
    private String help_Headline ;

    public HelpCommand(PresetType presetType, CommandHandler cmdHandler) {
        this.presetType = presetType;
        this.cmdHandler = cmdHandler;
        this.help_Headline = "&6=====[AdvancedRegionMarket " + this.presetType.getName() + " Help ]=====\n&3Page %actualpage% / %maxpage%";
        this.help_Headline = ChatColor.translateAlternateColorCodes('&', this.help_Headline);
    }

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
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_PRESET_HELP)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        int selectedpage;
        if(allargs.matches(this.regex_args)) {
            selectedpage = Integer.parseInt(args[1]);
        } else {
            selectedpage = 1;
        }

        List<BasicArmCommand> commands = this.cmdHandler.getCommands();
        List<String> usages = new ArrayList<>();

        for(BasicArmCommand command : commands) {
            usages.addAll(command.getUsage());
        }

        Collections.sort(usages);

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

        sender.sendMessage(this.help_Headline.replace("%actualpage%", selectedpage + "").replace("%maxpage%", pages + ""));
        for(int i = firstCommand; i < lastCommand; i++) {
            sender.sendMessage(ChatColor.GOLD + "/" + commandsLabel + " " + this.cmdHandler.getRootcommand() + " " + usages.get(i));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_HELP)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
            }
        }
        return returnme;
    }
}

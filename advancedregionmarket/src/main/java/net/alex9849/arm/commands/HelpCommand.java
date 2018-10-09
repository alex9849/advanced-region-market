package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends BasicArmCommand {

    private final String rootCommand = "help";
    private final String regex = "(?i)help";
    private final String usage = "/arm help";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) throws InputException {
        if(!sender.hasPermission(Permission.ARM_HELP)){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        sender.sendMessage(ChatColor.GOLD + "/arm setregionkind [KIND] [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm listregionkinds");
        sender.sendMessage(ChatColor.GOLD + "/arm findfreeregion [KIND]");
        sender.sendMessage(ChatColor.GOLD + "/arm resetblocks [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm reset [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm info [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm addmember [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm removemember [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm autoreset [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm listregions [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm setowner [REGION] [PLAYER]");
        sender.sendMessage(ChatColor.GOLD + "/arm hotel [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm updateschematic [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm setwarp [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm unsell [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm extend [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm delete [REGION]");
        sender.sendMessage(ChatColor.GOLD + "/arm doblockreset [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm terminate [REGION] [true/false]");
        sender.sendMessage(ChatColor.GOLD + "/arm sellpreset [SETTING]");
        sender.sendMessage(ChatColor.GOLD + "/arm rentpreset [SETTING]");
        sender.sendMessage(ChatColor.GOLD + "/arm contractpreset [SETTING]");
        sender.sendMessage(ChatColor.GOLD + "/arm limit");
        sender.sendMessage(ChatColor.GOLD + "/arm reload");
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

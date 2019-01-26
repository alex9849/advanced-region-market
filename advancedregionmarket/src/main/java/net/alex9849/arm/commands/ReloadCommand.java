package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand extends BasicArmCommand {

    private final String rootCommand = "reload";
    private final String regex = "(?i)reload";
    private final List<String> usage = new ArrayList<>(Arrays.asList("reload"));

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
        if (sender.hasPermission(Permission.ADMIN_RELOAD)) {
            sender.sendMessage(Messages.PREFIX + "Reloading...");
            AdvancedRegionMarket.getARM().onDisable();
            Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket").reloadConfig();
            AdvancedRegionMarket.getARM().onEnable();
            sender.sendMessage(Messages.PREFIX + "Complete!");
        } else {
            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_RELOAD)) {
                    returnme.add(this.rootCommand);
                }
            }
        }

        return returnme;
    }
}

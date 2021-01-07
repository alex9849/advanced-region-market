package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand extends BasicArmCommand {

    public ReloadCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "reload",
                Arrays.asList("(?i)reload"),
                Arrays.asList("reload"),
                Arrays.asList(Permission.ADMIN_RELOAD));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) {
        sender.sendMessage(Messages.PREFIX + "Reloading...");
        getPlugin().onDisable();
        Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket").reloadConfig();
        getPlugin().startup();
        sender.sendMessage(Messages.PREFIX + "Complete!");
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}

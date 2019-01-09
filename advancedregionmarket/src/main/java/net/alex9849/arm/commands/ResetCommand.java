package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResetCommand extends BasicArmCommand {

    private final String rootCommand = "reset";
    private final String regex = "(?i)reset [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("reset [REGION]"));

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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }

        if(!sender.hasPermission(Permission.ADMIN_RESETREGION)){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        Region resregion = RegionManager.getRegionbyNameAndWorldCommands(args[1], ((Player) sender).getPlayer().getWorld().getName());
        if(resregion == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        } else {
            resregion.unsell();

            resregion.resetBlocks();
            sender.sendMessage(Messages.PREFIX + Messages.REGION_NOW_AVIABLE);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.ADMIN_RESETREGION)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(RegionManager.completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true));
                    }
                }
            }
        }
        return returnme;
    }
}

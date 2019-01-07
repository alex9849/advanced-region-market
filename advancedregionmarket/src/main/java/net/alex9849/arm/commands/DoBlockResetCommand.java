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

public class DoBlockResetCommand extends BasicArmCommand {

    private final String rootCommand = "doblockreset";
    private final String regex = "(?i)doblockreset [^;\n ]+ (false|true)";
    private final List<String> usage = new ArrayList<>(Arrays.asList("doblockreset [REGION] [true/false]"));

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
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Region region = RegionManager.searchRegionbyNameAndWorld(args[1], player.getWorld().getName());
        if(region == null){
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }

        Boolean boolsetting = Boolean.parseBoolean(args[2]);

        region.setDoBlockReset(boolsetting);

        String message = "disabled";

        if(boolsetting){
            message = "enabled";
        }

        sender.sendMessage(Messages.PREFIX + "DoBlockReset " + message + " for " + region.getRegion().getId() + "!");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.ADMIN_CHANGE_DO_BLOCK_RESET)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(RegionManager.completeTabRegions(player, args[1], PlayerRegionRelationship.ALL));
                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

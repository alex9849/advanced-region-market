package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import net.alex9849.arm.regions.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFreeRegionCommand extends BasicArmCommand {

    private final String rootCommand = "findfreeregion";
    private final String regex = "(?i)findfreeregion [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("findfreeregion [REGIONKIND]"));
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission(Permission.ARM_BUYKIND + args[1])){
                throw new InputException(player, Messages.NO_PERMISSION_TO_SEARCH_THIS_KIND);
            }
            RegionKind regionKind = RegionKind.getRegionKind(args[1]);
            if (regionKind == null){
                throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            RegionManager.teleportToFreeRegion(regionKind, player);
            return true;
        } else {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(args.length == 1) {
                    Boolean addffr = false;
                    for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                        if (player.hasPermission(Permission.ARM_BUYKIND + regionkind.getName())) {
                            addffr = true;
                        }
                    }
                    if(player.hasPermission(Permission.ARM_BUYKIND + "default")) {
                        addffr = true;
                    }
                    if(addffr) {
                        returnme.add(this.rootCommand);
                    }
                } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                    for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                        if(regionkind.getName().toLowerCase().startsWith(args[1])) {
                            if(player.hasPermission(Permission.ARM_BUYKIND + regionkind.getName())) {
                                returnme.add(regionkind.getName());
                            }
                        }
                    }
                    if("default".startsWith(args[1])) {
                        if(player.hasPermission(Permission.ARM_BUYKIND + "default")) {
                            returnme.add("default");
                        }
                    }
                }
            }
        }

        return returnme;
    }
}

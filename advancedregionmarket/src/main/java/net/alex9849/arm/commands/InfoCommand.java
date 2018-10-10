package net.alex9849.arm.commands;

import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends BasicArmCommand {
    private final String rootCommand = "info";
    private final String regex = "(?i)info";
    private final String usage = "/arm info [REGION] or /arm info";
    private final String regex_with_args = "(?i)info [^;\n ]+";

    @Override
    public boolean matchesRegex(String command) {
        if(command.matches(this.regex)){
            return true;
        } else {
            return command.matches(this.regex_with_args);
        }
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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (allargs.matches(this.regex)) {
            return Region.regionInfoCommand(sender);
        } else {
            return Region.regionInfoCommand(sender, args[1]);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.ADMIN_INFO) || player.hasPermission(Permission.MEMBER_INFO)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2) {
                        returnme.addAll(Region.completeTabRegions(player, args[1], PlayerRegionRelationship.ALL));
                    }
                }
            }
        }

        return returnme;
    }
}

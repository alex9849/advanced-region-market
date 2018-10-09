package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LimitCommand extends BasicArmCommand {

    private final String rootCommand = "limit";
    private final String regex = "(?i)limit";
    private final String usage = "/arm limit";

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
        if (sender.hasPermission(Permission.MEMBER_LIMIT)) {
            if(!(sender instanceof Player)){
                throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
            }
            Player player = (Player) sender;

            getLimitChat(player);

            return true;
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.MEMBER_LIMIT)) {
                    returnme.add(this.rootCommand);
                }
            }
        }
        return returnme;
    }

    public static void getLimitChat(Player player) {
        player.sendMessage(Messages.LIMIT_INFO_TOP);
        String syntaxtotal = Messages.LIMIT_INFO;
        syntaxtotal = syntaxtotal.replace("%regiontype%", Messages.LIMIT_INFO_TOTAL);
        syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player) + "");
        String limit = LimitGroup.getLimit(player) + "";
        if(LimitGroup.getLimit(player) == Integer.MAX_VALUE){
            limit = Messages.UNLIMITED;
        }
        syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

        player.sendMessage(syntaxtotal);

        if(AdvancedRegionMarket.isDisplayDefaultRegionKindInLimits()) {
            syntaxtotal = Messages.LIMIT_INFO;
            syntaxtotal = syntaxtotal.replace("%regiontype%", RegionKind.DEFAULT.getName());
            syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, RegionKind.DEFAULT) + "");
            limit = LimitGroup.getLimit(player, RegionKind.DEFAULT) + "";
            if(LimitGroup.getLimit(player) == Integer.MAX_VALUE){
                limit = Messages.UNLIMITED;
            }
            syntaxtotal = syntaxtotal.replace("%limitkind%", limit);

            player.sendMessage(syntaxtotal);
        }

        for(int i = 0; i < RegionKind.getRegionKindList().size(); i++){
            if(player.hasPermission(Permission.ARM_BUYKIND + RegionKind.getRegionKindList().get(i).getDisplayName())){
                syntaxtotal = Messages.LIMIT_INFO;
                syntaxtotal = syntaxtotal.replace("%regiontype%", RegionKind.getRegionKindList().get(i).getName());
                syntaxtotal = syntaxtotal.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, RegionKind.getRegionKindList().get(i)) + "");
                limit = LimitGroup.getLimit(player, RegionKind.getRegionKindList().get(i)) + "";
                if(LimitGroup.getLimit(player, RegionKind.getRegionKindList().get(i)) == Integer.MAX_VALUE){
                    limit = Messages.UNLIMITED;
                }
                syntaxtotal = syntaxtotal.replace("%limitkind%", limit);
                player.sendMessage(syntaxtotal);
            }
        }
    }
}

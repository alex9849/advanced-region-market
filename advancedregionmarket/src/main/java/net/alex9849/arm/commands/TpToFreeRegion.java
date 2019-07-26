package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.exceptions.CmdSyntaxException;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TpToFreeRegion extends BasicArmCommand {
    private final String rootCommand = "tptofreeregion";
    private final String regex = "(?i)tptofreeregion [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("tptofreeregion [REGIONKIND]"));

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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException, CmdSyntaxException {
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.MEMBER_TP_TO_FREE_REGION)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(args[1]);
        if(regionKind == null) {
            throw new InputException(player, Messages.REGIONKIND_DOES_NOT_EXIST);
        }
        if(!RegionKind.hasPermission(player, regionKind)) {
            //TODO Das hier noch ändern!
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        AdvancedRegionMarket.getRegionManager().teleportToFreeRegion(regionKind, player);

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {

            if(!player.hasPermission(Permission.MEMBER_TP_TO_FREE_REGION)) {
                return returnme;
            }

            if (this.rootCommand.startsWith(args[0])) {
                if (Permission.hasAnyBuyPermission(player)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], "", player));
                    }
                }
            }
        }
        return returnme;
    }
}

package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetBlocksCommand extends BasicArmCommand {
    private final String rootCommand = "resetblocks";
    private final String regex = "(?i)resetblocks [^;\n ]+";
    private final String usage = "/arm resetblocks [REGION]";

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
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS) && !player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)){
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        Region resregion = Region.searchRegionbyNameAndWorld(args[1], (player).getPlayer().getWorld().getName());
        if(resregion == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if(player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
            resregion.resetBlocks();
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
            return true;
        } else {
            if(AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, resregion.getRegion())) {
                if(resregion.timeSinceLastReset() >= Region.getResetCooldown()){
                    Gui.openRegionResetWarning(player, resregion, false);
                } else {
                    String message = Messages.RESET_REGION_COOLDOWN_ERROR.replace("%remainingdays%", (Region.getResetCooldown() - resregion.timeSinceLastReset()) + "");
                    throw new InputException(player, message);
                }
            } else {
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) || player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(Region.completeTabRegions(player, args[1], playerRegionRelationship));
                    }
                }
            }
        }
        return returnme;
    }
}

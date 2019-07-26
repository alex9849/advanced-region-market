package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import net.alex9849.exceptions.SchematicNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ResetBlocksCommand extends BasicArmCommand {
    private final String rootCommand = "resetblocks";
    private final String regex_with_args = "(?i)resetblocks [^;\n ]+";
    private final String regex = "(?i)resetblocks";
    private final List<String> usage = new ArrayList<>(Arrays.asList("resetblocks [REGION]", "resetblocks"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_with_args);
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
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS) && !player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS)){
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        Region resregion;
        if(allargs.matches(this.regex)) {
            resregion = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        } else {
            resregion = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
        }

        if(player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
            try {
                resregion.resetBlocks();
            } catch (SchematicNotFoundException e) {
                player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_ADMIN.replace("%regionid%", e.getRegion().getId()));
                Bukkit.getLogger().log(Level.WARNING, "Could not find schematic file for region " + resregion.getRegion().getId() + "in world " + resregion.getRegionworld().getName());
            }
            player.sendMessage(Messages.PREFIX + Messages.RESET_COMPLETE);
            return true;
        } else {
            if(resregion.getRegion().hasOwner(player.getUniqueId())) {
                if(!resregion.isUserResettable()) {
                    throw new InputException(player, Messages.REGION_NOT_RESETTABLE);
                }
                if(resregion.timeSinceLastReset() >= Region.getResetCooldown()){
                    Gui.openRegionResetWarning(player, resregion, false);
                    return true;
                } else {
                    String message = resregion.getConvertedMessage(Messages.RESET_REGION_COOLDOWN_ERROR);
                    throw new InputException(player, message);
                }
            } else {
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
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
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], playerRegionRelationship,true, true));
                    }
                }
            }
        }
        return returnme;
    }
}

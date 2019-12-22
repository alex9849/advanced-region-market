package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class RestoreCommand implements BasicArmCommand {
    private final String rootCommand = "restore";
    private final String regex = "(?i)restore [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("restore [REGION]"));

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
        if (!sender.hasPermission(Permission.SUBREGION_RESTORE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if (!region.isSubregion()) {
            throw new InputException(sender, Messages.REGION_NOT_A_SUBREGION);
        }

        if (!region.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(sender, Messages.PARENT_REGION_NOT_OWN);
        }

        if (!region.isUserRestorable()) {
            throw new InputException(sender, Messages.REGION_NOT_RESETTABLE);
        }

        try {
            //TODO logToConsole
            region.restoreRegion(Region.ActionReason.MANUALLY_BY_PARENT_REGION_OWNER, true, false);
            sender.sendMessage(Messages.PREFIX + Messages.COMPLETE);
        } catch (SchematicNotFoundException e) {
            AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, region.getConvertedMessage(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG));
            player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_NOT_FOUND_ERROR_USER.replace("%regionid%", e.getRegion().getId()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.SUBREGION_RESTORE)) {
                    if (args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.PARENTREGION_OWNER, false, true));
                    }
                }
            }
        }
        return returnme;
    }
}
package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSchematicCommand extends BasicArmCommand {

    private final String rootCommand = "updateschematic";
    private final String regex = "(?i)updateschematic";
    private final String regex_with_args = "(?i)updateschematic [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("updateschematic [REGION]", "updateschematic"));

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
        if(!sender.hasPermission(Permission.ADMIN_UPDATESCHEMATIC)){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        Region region;
        if(allargs.matches(this.regex)) {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        } else {
            region = AdvancedRegionMarket.getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
        }

        player.sendMessage(Messages.PREFIX + "Creating schematic...");
        region.createSchematic();
        player.sendMessage(Messages.PREFIX + Messages.COMPLETE);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_UPDATESCHEMATIC)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true,true));
                    }
                }
            }
        }
        return returnme;
    }
}

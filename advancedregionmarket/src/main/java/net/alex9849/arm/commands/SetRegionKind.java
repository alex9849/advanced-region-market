package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetRegionKind extends BasicArmCommand {
    private final String rootCommand = "setregionkind";
    private final String regex = "(?i)setregionkind [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setregionkind [REGIONKIND] [REGION]"));

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
        if(sender instanceof Player){
            if(!sender.hasPermission(Permission.ADMIN_SETREGIONKIND)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            Player player = (Player) sender;
            Region region = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[2], player.getWorld().getName());

            if(region == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
            if(region.isSubregion()) {
                throw new InputException(sender, Messages.SUB_REGION_REGIONKIND_ERROR);
            }
            RegionKind regionKind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(args[1]);
            if(regionKind == null) {
                throw new InputException(sender, Messages.REGION_KIND_NOT_EXIST);
            }
            if(regionKind == RegionKind.SUBREGION) {
                throw new InputException(sender, Messages.SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS);
            }
            region.setKind(regionKind);
            sender.sendMessage(Messages.PREFIX + Messages.REGION_KIND_SET);
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
                if (player.hasPermission(Permission.ADMIN_SETREGIONKIND))
                    if (args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], ""));
                    } else if (args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[2], PlayerRegionRelationship.ALL, true,false));
                    }
            }
        }
        return returnme;
    }
}

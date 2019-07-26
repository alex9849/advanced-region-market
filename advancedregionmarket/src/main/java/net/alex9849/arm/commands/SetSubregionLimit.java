package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSubregionLimit extends BasicArmCommand {

    private final String rootCommand = "setsubregionlimit";
    private final String regex = "(?i)setsubregionlimit [^;\n ]+ [0-9]+";
    private final String regex_massaction = "(?i)setsubregionlimit rk:[^;\n ]+ [0-9]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setsubregionlimit [REGION] [AMOUNT]", "setsubregionlimit rk:[REGIONKIND] [true/false]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_massaction);
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
        if (!sender.hasPermission(Permission.ADMIN_SET_SUBREGION_LIMIT)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        List<Region> regions = new ArrayList<>();
        String selectedName;

        if(allargs.matches(regex_massaction) && (AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName()) == null)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);

            RegionKind selectedRegionkind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if(selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            if(selectedRegionkind == RegionKind.SUBREGION) {
                throw new InputException(sender, ChatColor.RED + "Subregions can not have subregions");
            }
            regions = AdvancedRegionMarket.getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if(selectedRegion == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if(selectedRegion.isSubregion()) {
                throw new InputException(sender, ChatColor.RED + "Subregions can not have subregions");
            }
            regions.add(selectedRegion);
            selectedName = "&a" + selectedRegion.getRegion().getId();
        }

        int allowedSubregions = Integer.parseInt(args[2]);
        for(Region region : regions) {
            if(!region.isSubregion()) {
                region.setAllowedSubregions(allowedSubregions);
            }
        }
        String sendmessage = Messages.PREFIX + "&6Subregionlimit has been set to &a" + allowedSubregions + " &6for " + selectedName + "&6!";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', sendmessage));

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_SET_SUBREGION_LIMIT)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true,false));
                        if("rk:".startsWith(args[1])) {
                            returnme.add("rk:");
                        }
                        if (args[1].matches("rk:([^;\n]+)?")) {
                            returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
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

public class SetEntityLimitCommand extends BasicArmCommand {
    private final String rootCommand = "setentitylimit";
    private final String regex = "(?i)setentitylimit [^;\n ]+ [^;\n ]+";
    private final String regex_massaction = "(?i)setentitylimit rk:[^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setentitylimit [REGION] [ENTITYTYPE]", "setentitylimit rk:[REGIONKIND] [ENTITYTYPE]"));

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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_SET_ENTITYLIMIT)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        List<Region> regions = new ArrayList<>();
        String selectedName;

        if(allargs.matches(regex_massaction) && (AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName()) == null)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);

            RegionKind selectedRegionkind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if(selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            regions = AdvancedRegionMarket.getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if(selectedRegion == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            regions.add(selectedRegion);
            selectedName = "&a" + selectedRegion.getRegion().getId();
        }

        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(args[2]);

        if(entityLimitGroup == null) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        if(entityLimitGroup == EntityLimitGroup.SUBREGION) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        }

        for(Region region : regions) {
            region.setEntityLimitGroup(entityLimitGroup);
            if(region.isSubregion()) {
                throw new InputException(sender, region.getConvertedMessage(Messages.SUB_REGION_ENTITYLIMITGROUP_ERROR));
            }
        }
        String sendmessage = Messages.PREFIX + "&6Set entityLimitGroup &a" + entityLimitGroup.getName() + " &6for " + selectedName + "&6!";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', sendmessage));

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.ADMIN_SET_ENTITYLIMIT)) {
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

                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[2]));
                    }
                }
            }
        }
        return returnme;
    }
}
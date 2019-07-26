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

public class SetIsUserResettableCommand extends BasicArmCommand {
    private final String rootCommand = "setisuserresettable";
    private final String regex = "(?i)setisuserresettable [^;\n ]+ (false|true)";
    private final String regex_massaction = "(?i)setisuserresettable rk:[^;\n ]+ (false|true)";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setisuserresettable [REGION] [true/false]", "setisuserresettable rk:[REGIONKIND] [true/false]"));

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
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if(!player.hasPermission(Permission.ADMIN_SET_IS_USERRESETTABLE)) {
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
            if(selectedRegionkind == RegionKind.SUBREGION) {
                throw new InputException(sender, Messages.SUB_REGION_IS_USER_RESETTABLE_ERROR);
            }
            regions = AdvancedRegionMarket.getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if(selectedRegion == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if(selectedRegion.isSubregion()) {
                throw new InputException(sender, Messages.SUB_REGION_IS_USER_RESETTABLE_ERROR);
            }
            regions.add(selectedRegion);
            selectedName = "&a" + selectedRegion.getRegion().getId();
        }

        Boolean boolsetting = Boolean.parseBoolean(args[2]);

        for(Region region : regions) {
            region.setIsUserResettable(boolsetting);
        }
        String sendmessage = Messages.PREFIX + "&6isUserResettable " + Messages.convertEnabledDisabled(boolsetting) + " &6for " + selectedName + "&6!";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', sendmessage));

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if(player.hasPermission(Permission.ADMIN_SET_IS_USERRESETTABLE)) {
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
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

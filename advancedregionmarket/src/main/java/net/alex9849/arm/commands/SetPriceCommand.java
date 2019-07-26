package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.exceptions.InputException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPriceCommand extends BasicArmCommand {
    private final String rootCommand = "setprice";
    private final String regex_massaction = "(?i)setprice rk:[^;\n]+";
    private final String regex_price = "(?i)setprice [^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private final String regex_price_autoprice = "(?i)setprice [^;\n ]+ [^;\n ]+";
    private final String regex_price_massaction = "(?i)setprice rk:[^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private final String regex_price_autoprice_massaction = "(?i)setprice rk:[^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("setprice [REGION] [AUTOPRICE]", "setprice [REGION] [PRICE] [EXTENDTIME] [MAXRENTTIME]"
            , "setprice rk:[REGION] [AUTOPRICE]", "setprice rk:[REGION] [PRICE] [EXTENDTIME] [MAXRENTTIME]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_price_autoprice_massaction) || command.matches(this.regex_price_massaction) || command.matches(this.regex_price_autoprice) || command.matches(this.regex_price);
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
        if(!(sender.hasPermission(Permission.ADMIN_SET_PRICE))){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        List<Region> selectedregions = new ArrayList<>();
        RentPrice price;
        String selectedName;

        if(allargs.matches(this.regex_massaction)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);
            RegionKind selectedRegionkind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if(selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            selectedregions = AdvancedRegionMarket.getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if(selectedRegion == null){
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
            selectedregions.add(selectedRegion);
            selectedName = "&a" + selectedRegion.getRegion().getId();
        }

        if(allargs.matches(this.regex_price) || allargs.matches(this.regex_price_massaction)) {
            int priceint = Integer.parseInt(args[2]);
            long extend = RentPrice.stringToTime(args[3]);
            long maxrenttime = RentPrice.stringToTime(args[4]);

            for(Region region : selectedregions) {
                price = new RentPrice(priceint, extend, maxrenttime);
                region.setPrice(price);
            }
        } else {
            AutoPrice selectedAutoprice = AutoPrice.getAutoprice(args[2]);
            if(selectedAutoprice == null) {
                throw new InputException(sender, ChatColor.RED + "Autoprice does not exist!");
            }
            for(Region region : selectedregions) {
                price = new RentPrice(selectedAutoprice);
                region.setPrice(price);
            }
        }

        String sendmessage = Messages.PREFIX + "&6Price has been set for " + selectedName + "&6!";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', sendmessage));

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_SET_PRICE)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true,true));
                        if("rk:".startsWith(args[1])) {
                            returnme.add("rk:");
                        }
                        if (args[1].matches("rk:([^;\n]+)?")) {
                            returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
                        }

                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AutoPrice.tabCompleteAutoPrice(args[2]));
                    } else if(args.length == 4 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if(args[3].matches("[0-9]+")) {
                            returnme.add(args[3] + "s");
                            returnme.add(args[3] + "m");
                            returnme.add(args[3] + "h");
                            returnme.add(args[3] + "d");
                        }
                    } else if(args.length == 5 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if(args[4].matches("[0-9]+")) {
                            returnme.add(args[4] + "s");
                            returnme.add(args[4] + "m");
                            returnme.add(args[4] + "h");
                            returnme.add(args[4] + "d");
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

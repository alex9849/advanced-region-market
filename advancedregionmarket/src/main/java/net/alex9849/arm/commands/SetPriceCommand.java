package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPriceCommand extends BasicArmCommand {
    private static final String regex_price = "(?i)setprice [^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private static final String regex_price_autoprice = "(?i)setprice [^;\n ]+ [^;\n ]+";
    private static final String regex_price_massaction = "(?i)setprice rk:[^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private static final String regex_price_autoprice_massaction = "(?i)setprice rk:[^;\n ]+ [^;\n ]+";

    public SetPriceCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "setprice",
                Arrays.asList(regex_price_autoprice, regex_price,
                        regex_price_autoprice_massaction, regex_price_massaction),
                Arrays.asList("setprice [REGION] [AUTOPRICE]", "setprice [REGION] [PRICE] [EXTENDTIME] [MAXEXTENDTIME]",
                        "setprice rk:[REGION] [AUTOPRICE]", "setprice rk:[REGION] [PRICE] [EXTENDTIME] [MAXEXTENDTIME]"),
                Arrays.asList(Permission.ADMIN_SET_PRICE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        String[] args = command.split(" ");

        List<Region> selectedregions = new ArrayList<>();
        RentPrice price;
        String selectedName;

        if (command.matches(regex_price_massaction) || command.matches(regex_price_autoprice_massaction)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);
            RegionKind selectedRegionkind = getPlugin().getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if (selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            selectedregions = getPlugin().getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.replaceVariables(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = getPlugin().getRegionManager()
                    .getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if (selectedRegion == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
            selectedregions.add(selectedRegion);
            selectedName = selectedRegion.getRegion().getId();
        }

        if (command.matches(regex_price) || command.matches(regex_price_massaction)) {
            int priceint = Integer.parseInt(args[2]);
            long extend = RentPrice.stringToTime(args[3]);
            long maxextendtime = RentPrice.stringToTime(args[4]);

            try {
                for (Region region : selectedregions) {
                    price = new RentPrice(priceint, extend, maxextendtime);
                    if(region instanceof SellRegion) {
                        ((SellRegion) region).setSellPrice(price);
                    } else if(region instanceof ContractRegion) {
                        ((ContractRegion) region).setContractPrice(price);
                    } else if(region instanceof RentRegion) {
                        ((RentRegion) region).setRentPrice(price);
                    } else {
                        throw new RuntimeException("This is a bug! SetPriceCommand doesn't know hot to set the price for " + region.getClass().getName());
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new InputException(sender, e.getMessage());
            }

        } else {
            AutoPrice selectedAutoprice = AutoPrice.getAutoprice(args[2]);
            if (selectedAutoprice == null) {
                throw new InputException(sender, ChatColor.RED + "Autoprice does not exist!");
            }
            for (Region region : selectedregions) {
                region.setAutoPrice(selectedAutoprice);
            }
        }

        String sendmessage = Messages.REGION_MODIFIED;
        sendmessage = sendmessage.replace("%option%", "Price");
        sendmessage = sendmessage.replace("%selectedregions%", selectedName);
        sender.sendMessage(Messages.PREFIX + sendmessage);

        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(getPlugin().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));
            if ("rk:".startsWith(args[1])) {
                returnme.add("rk:");
            }
            if (args[1].matches("rk:([^;\n]+)?")) {
                returnme.addAll(getPlugin().getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
            }

        } else if (args.length == 3) {
            returnme.addAll(AutoPrice.tabCompleteAutoPrice(args[2]));

        } else if (args.length == 4) {
            if (args[3].matches("[0-9]+")) {
                returnme.add(args[3] + "s");
                returnme.add(args[3] + "m");
                returnme.add(args[3] + "h");
                returnme.add(args[3] + "d");
            }
        } else if (args.length == 5) {
            if (args[4].matches("[0-9]+")) {
                returnme.add(args[4] + "s");
                returnme.add(args[4] + "m");
                returnme.add(args[4] + "h");
                returnme.add(args[4] + "d");
            }
        }
        return returnme;
    }
}

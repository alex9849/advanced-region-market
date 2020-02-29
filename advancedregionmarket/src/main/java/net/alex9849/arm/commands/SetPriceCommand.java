package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPriceCommand extends BasicArmCommand {
    private final String regex_price = "(?i)setprice [^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private final String regex_price_autoprice = "(?i)setprice [^;\n ]+ [^;\n ]+";
    private final String regex_price_massaction = "(?i)setprice rk:[^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)";
    private final String regex_price_autoprice_massaction = "(?i)setprice rk:[^;\n ]+ [^;\n ]+";

    public SetPriceCommand() {
        super(false, "setprice",
                Arrays.asList("(?i)setprice [^;\n ]+ [^;\n ]+", "(?i)setprice [^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)",
                        "(?i)setprice rk:[^;\n ]+ [^;\n ]+", "(?i)setprice rk:[^;\n ]+ [0-9]+ [0-9]+(s|m|h|d) [0-9]+(s|m|h|d)"),
                Arrays.asList("setprice [REGION] [AUTOPRICE]", "setprice [REGION] [PRICE] [EXTENDTIME] [MAXRENTTIME]",
                        "setprice rk:[REGION] [AUTOPRICE]", "setprice rk:[REGION] [PRICE] [EXTENDTIME] [MAXRENTTIME]"),
                Arrays.asList(Permission.ADMIN_SET_PRICE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        String[] args = command.split(" ");

        List<Region> selectedregions = new ArrayList<>();
        RentPrice price;
        String selectedName;

        if (command.matches(this.regex_price_massaction) || command.matches(this.regex_price_autoprice_massaction)) {
            String[] splittedRegionKindArg = args[1].split(":", 2);
            RegionKind selectedRegionkind = AdvancedRegionMarket.getInstance().getRegionKindManager().getRegionKind(splittedRegionKindArg[1]);
            if (selectedRegionkind == null) {
                throw new InputException(sender, Messages.REGIONKIND_DOES_NOT_EXIST);
            }
            selectedregions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByRegionKind(selectedRegionkind);
            selectedName = selectedRegionkind.getConvertedMessage(Messages.MASSACTION_SPLITTER);
        } else {
            Region selectedRegion = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
            if (selectedRegion == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
            selectedregions.add(selectedRegion);
            selectedName = selectedRegion.getRegion().getId();
        }

        if (command.matches(this.regex_price) || command.matches(this.regex_price_massaction)) {
            int priceint = Integer.parseInt(args[2]);
            long extend = RentPrice.stringToTime(args[3]);
            long maxrenttime = RentPrice.stringToTime(args[4]);

            for (Region region : selectedregions) {
                price = new RentPrice(priceint, extend, maxrenttime);
                region.setPrice(price);
            }
        } else {
            AutoPrice selectedAutoprice = AutoPrice.getAutoprice(args[2]);
            if (selectedAutoprice == null) {
                throw new InputException(sender, ChatColor.RED + "Autoprice does not exist!");
            }
            for (Region region : selectedregions) {
                price = new RentPrice(selectedAutoprice);
                region.setPrice(price);
            }
        }

        String sendmessage = Messages.REGION_MODIFIED;
        sendmessage = sendmessage.replace("%option%", "Price");
        sendmessage = sendmessage.replace("%selectedregions%", selectedName);
        sender.sendMessage(Messages.PREFIX + sendmessage);

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));
            if ("rk:".startsWith(args[1])) {
                returnme.add("rk:");
            }
            if (args[1].matches("rk:([^;\n]+)?")) {
                returnme.addAll(AdvancedRegionMarket.getInstance().getRegionKindManager().completeTabRegionKinds(args[1], "rk:"));
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

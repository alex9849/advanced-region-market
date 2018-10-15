package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OfferCommand extends BasicArmCommand {

    private final String rootCommand = "offer";
    private final String regex_new = "(?i)offer [^;\n ]+ [^;\n ]+ ([0-9]+[.])?[0-9]+";
    private final String regex_cancel = "(?i)offer (?i)cancel";
    private final String regex_reject = "(?i)offer (?i)reject";
    private final String regex_accept = "(?i)offer (?i)accept";
    private final String usage = "/arm offer [REGION] [BUYER] [PRICE]";

    @Override
    public boolean matchesRegex(String command) {
        return (command.matches(this.regex_new) || command.matches(this.regex_cancel) || command.matches(this.regex_accept) || command.matches(this.regex_reject));
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if (allargs.matches(regex_new)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                Region region = Region.searchRegionbyNameAndWorld(args[1], player.getLocation().getWorld().getName());
                if(region == null) {
                    throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
                }
                if(!AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player.getUniqueId(), region.getRegion())) {
                    throw new InputException(player, Messages.REGION_NOT_OWN);
                }
                if(!region.isSold()) {
                    throw new InputException(player, Messages.REGION_NOT_SOLD);
                }
                Player buyer = Bukkit.getPlayer(args[2]);
                if(buyer == null) {
                    throw new InputException(player, "The selected Player is not online");
                }
                double price = Double.parseDouble(args[3]);

                Offer.createOffer(region, price, player, buyer);

                player.sendMessage(Messages.PREFIX + "Your offer has been sent");
                buyer.sendMessage(Messages.PREFIX + player.getDisplayName() + " offers you his region " + region.getRegion().getId() + " in the world " + region.getRegionworld() + " for " + price + " " + Messages.CURRENCY + "!");
                buyer.sendMessage("You can accept his offer with /arm offer accept or reject it /arm offer reject");
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_accept)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                Offer offer = Offer.acceptOffer(player);
                player.sendMessage(Messages.PREFIX + "Offer accepted! You are now the owner of " + offer.getRegion().getRegion().getId());
                offer.getSeller().sendMessage(Messages.PREFIX + player.getDisplayName() + " accepted your offer");
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_cancel)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                Offer offer = Offer.cancelOffer(player);
                player.sendMessage(Messages.PREFIX + "Your offer has been cancelled!");
                offer.getBuyer().sendMessage(Messages.PREFIX + player.getDisplayName() + " cancelled his offer!");
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_reject)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                Offer offer = Offer.rejectOffer(player);
                player.sendMessage(Messages.PREFIX + "Offer rejected!");
                offer.getSeller().sendMessage(Messages.PREFIX + player.getDisplayName() + " rejected you offer!");
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<String>();
    }
}

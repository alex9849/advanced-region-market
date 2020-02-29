package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfferCommand extends BasicArmCommand {
    private final String regex_new = "(?i)offer [^;\n ]+ [^;\n ]+ ([0-9]+[.])?[0-9]+";
    private final String regex_cancel = "(?i)offer (?i)cancel";
    private final String regex_reject = "(?i)offer (?i)reject";
    private final String regex_accept = "(?i)offer (?i)accept";

    public OfferCommand() {
        super(false, "offer",
                Arrays.asList("(?i)offer [^;\n ]+ [^;\n ]+ ([0-9]+[.])?[0-9]+",
                        "(?i)offer (?i)cancel", "(?i)offer (?i)reject", "(?i)offer (?i)accept"),
                Arrays.asList("offer [BUYER] [REGION] [PRICE]", "offer accept", "offer reject", "offer cancel"),
                Arrays.asList(Permission.MEMBER_OFFER_CREATE, Permission.MEMBER_OFFER_ANSWER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        String[] args = command.split(" ");

        if (command.matches(regex_new)) {
            if (!player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                throw new InputException(player, Messages.NO_PERMISSION);
            }

            Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[2], player.getLocation().getWorld().getName());
            if (region == null) {
                throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
            }
            if (!region.getRegion().hasOwner(player.getUniqueId())) {
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
            if (!region.isSold()) {
                throw new InputException(player, Messages.REGION_NOT_SOLD);
            }
            Player buyer = Bukkit.getPlayer(args[1]);
            if (buyer == null) {
                throw new InputException(player, Messages.SELECTED_PLAYER_NOT_ONLINE);
            }
            double price = Double.parseDouble(args[3]);

            try {
                Offer offer = Offer.createOffer(region, price, player, buyer);
                player.sendMessage(Messages.PREFIX + Messages.OFFER_SENT);
                buyer.sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.INCOMING_OFFER));
            } catch (DublicateException | IllegalArgumentException e) {
                player.sendMessage(Messages.PREFIX + region.getConvertedMessage(e.getMessage()));
            }

            return true;


        } else if (command.matches(regex_accept)) {
            if (!player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                throw new InputException(player, Messages.NO_PERMISSION);
            }

            try {
                Offer offer = Offer.acceptOffer(player);
                player.sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_ACCEPTED_BUYER));
                offer.getSeller().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_ACCEPTED_SELLER));
            } catch (RegionNotOwnException | NoPermissionException | OutOfLimitExeption | NotEnoughMoneyException e) {
                if (e.hasMessage()) player.sendMessage(Messages.PREFIX + e.getMessage());
            }
            return true;


        } else if (command.matches(regex_cancel)) {
            if (player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                Offer offer = Offer.cancelOffer(player);
                player.sendMessage(Messages.PREFIX + Messages.OFFER_CANCELED);
                offer.getBuyer().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_HAS_BEEN_CANCELLED));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (command.matches(regex_reject)) {
            if (player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                Offer offer = Offer.rejectOffer(player);
                player.sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_REJECTED));
                offer.getSeller().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_HAS_BEEN_REJECTED));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }
        }
        return false;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (args.length == 2) {
            if (player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[1]));
                if ("cancel".startsWith(args[1])) {
                    returnme.add("cancel");
                }
            }
            if (player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                if ("reject".startsWith(args[1])) {
                    returnme.add("reject");
                }
                if ("accept".startsWith(args[1])) {
                    returnme.add("accept");
                }
            }
        } else if (args.length == 3) {
            if (player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                        .completeTabRegions(player, args[2], PlayerRegionRelationship.OWNER, true, true));
            }
        }

        return returnme;
    }
}

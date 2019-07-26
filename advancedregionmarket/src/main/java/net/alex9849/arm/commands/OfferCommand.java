package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.selloffer.Offer;
import net.alex9849.arm.regions.Region;
import net.alex9849.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfferCommand extends BasicArmCommand {

    private final String rootCommand = "offer";
    private final String regex_new = "(?i)offer [^;\n ]+ [^;\n ]+ ([0-9]+[.])?[0-9]+";
    private final String regex_cancel = "(?i)offer (?i)cancel";
    private final String regex_reject = "(?i)offer (?i)reject";
    private final String regex_accept = "(?i)offer (?i)accept";
    private final List<String> usage = new ArrayList<>(Arrays.asList("offer [BUYER] [REGION] [PRICE]", "offer accept", "offer reject", "offer cancel"));

    @Override
    public boolean matchesRegex(String command) {
        return (command.matches(this.regex_new) || command.matches(this.regex_cancel) || command.matches(this.regex_accept) || command.matches(this.regex_reject));
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

        if (allargs.matches(regex_new)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                Region region = AdvancedRegionMarket.getRegionManager().getRegionbyNameAndWorldCommands(args[2], player.getLocation().getWorld().getName());
                if(region == null) {
                    throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
                }
                if(!region.getRegion().hasOwner(player.getUniqueId())) {
                    throw new InputException(player, Messages.REGION_NOT_OWN);
                }
                if(!region.isSold()) {
                    throw new InputException(player, Messages.REGION_NOT_SOLD);
                }
                Player buyer = Bukkit.getPlayer(args[1]);
                if(buyer == null) {
                    throw new InputException(player, Messages.SELECTED_PLAYER_NOT_ONLINE);
                }
                double price = Double.parseDouble(args[3]);

                Offer offer = Offer.createOffer(region, price, player, buyer);

                player.sendMessage(Messages.PREFIX + Messages.OFFER_SENT);
                buyer.sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.INCOMING_OFFER));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_accept)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                Offer offer = Offer.acceptOffer(player);
                player.sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_ACCEPTED_BUYER));
                offer.getSeller().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_ACCEPTED_SELLER));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_cancel)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                Offer offer = Offer.cancelOffer(player);
                player.sendMessage(Messages.PREFIX + Messages.OFFER_CANCELED);
                offer.getBuyer().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_HAS_BEEN_CANCELLED));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }


        } else if (allargs.matches(regex_reject)) {
            if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                Offer offer = Offer.rejectOffer(player);
                player.sendMessage(Messages.PREFIX + Messages.OFFER_REJECTED);
                offer.getSeller().sendMessage(Messages.PREFIX + offer.getConvertedMessage(Messages.OFFER_HAS_BEEN_REJECTED));
                return true;
            } else {
                throw new InputException(player, Messages.NO_PERMISSION);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.MEMBER_OFFER_CREATE) || player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                            returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[1]));
                            if("cancel".startsWith(args[1])) {
                                returnme.add("cancel");
                            }
                        }
                        if(player.hasPermission(Permission.MEMBER_OFFER_ANSWER)) {
                            if("reject".startsWith(args[1])) {
                                returnme.add("reject");
                            }
                            if("accept".startsWith(args[1])) {
                                returnme.add("accept");
                            }
                        }
                    } else if(args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        if(player.hasPermission(Permission.MEMBER_OFFER_CREATE)) {
                            List<String> players = CommandHandler.tabCompleteOnlinePlayers(args[1]);
                            if(players.size() > 0) {
                                if(args[1].equalsIgnoreCase(players.get(0))) {
                                    returnme.addAll(AdvancedRegionMarket.getRegionManager().completeTabRegions(player, args[2], PlayerRegionRelationship.OWNER, true,true));
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

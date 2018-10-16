package net.alex9849.arm.minifeatures.selloffer;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Offer {
    private static List<Offer> offerList = new ArrayList<>();
    private Region region;
    private double price;
    private Player seller;
    private Player buyer;
    private OfferListener offerListener;

    private Offer(Region region, double price, Player seller, Player buyer) throws InputException {
        if(price < 0) {
            throw new InputException(seller, "Price can not be negative!");
        }
        this.region = region;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        this.offerListener = new OfferListener(seller, buyer, this);
    }

    public void accept() throws InputException {
        if(!region.isSold() || !AdvancedRegionMarket.getWorldGuardInterface().hasOwner(buyer.getUniqueId(), region.getRegion())) {
            this.reject();
            List<CommandSender> senders = new ArrayList<>(Arrays.asList(buyer, seller));
            List<String> messages = new ArrayList<>(Arrays.asList(this.getConvertedMessage(Messages.SELLER_DOES_NOT_LONGER_OWN_REGION), this.getConvertedMessage(Messages.OFFER_HAS_BEEN_REJECTED)));
            throw new InputException(senders, messages);
        }
        if(!LimitGroup.isCanBuyAnother(buyer, region)) {
            this.reject();
            List<CommandSender> senders = new ArrayList<>(Arrays.asList(buyer, seller));
            List<String> messages = new ArrayList<>(Arrays.asList(LimitGroup.getRegionBuyOutOfLimitMessage(buyer, region.getRegionKind()), this.getConvertedMessage(Messages.OFFER_HAS_BEEN_REJECTED)));
            throw new InputException(senders, messages);
        }

        Economy econ = AdvancedRegionMarket.getEcon();

        if(econ.getBalance(buyer) < price) {
            this.reject();
            List<CommandSender> senders = new ArrayList<>(Arrays.asList(buyer, seller));
            List<String> messages = new ArrayList<>(Arrays.asList(Messages.NOT_ENOUGHT_MONEY, this.getConvertedMessage(Messages.OFFER_HAS_BEEN_REJECTED)));
            throw new InputException(senders, messages);
        }
        econ.withdrawPlayer(seller, price);
        econ.depositPlayer(buyer, price);

        AdvancedRegionMarket.getWorldGuardInterface().setOwner(buyer, region.getRegion());

        this.unregister();

    }

    public void reject() {
        this.unregister();
    }

    private void unregister() {
        offerList.remove(this);
        this.offerListener.unregister();
    }

    public Region getRegion() {
        return this.region;
    }

    public Player getBuyer() {
        return this.buyer;
    }

    public Player getSeller() {
        return this.seller;
    }

    private void activateTimer(int ticks) {
        this.offerListener.activateCancelTimer(ticks);
    }

    public static Offer createOffer(Region region, double price, Player seller, Player buyer) throws InputException {
        for(int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getBuyer().getUniqueId() == buyer.getUniqueId()) {
                throw new InputException(seller, Messages.BUYER_ALREADY_GOT_AN_OFFER);
            }
            if(offerList.get(i).getSeller().getUniqueId() == seller.getUniqueId()) {
                throw new InputException(seller, Messages.SELLER_ALREADY_CREATED_AN_OFFER);
            }
        }

        for(int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getRegion() == region) {
                offerList.get(i).reject();
                i--;
            }
        }
        Offer offer = new Offer(region, price, seller, buyer);
        offerList.add(offer);
        int timer = AdvancedRegionMarket.getARM().getConfig().getInt("Reselling.Offers.OfferTimeOut");
        if(timer != 0) {
            offer.activateTimer(20 * timer);
        }
        return offer;
    }

    public static Offer acceptOffer(Player buyer) throws InputException {
        for(Offer offer : offerList) {
            if(offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.accept();
                return offer;
            }
        }
        throw new InputException(buyer, Messages.NO_OFFER_TO_ANSWER);
    }

    public static Offer cancelOffer(Player seller) throws InputException {
        for (Offer offer : offerList) {
            if(offer.getSeller().getUniqueId() == seller.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(seller, Messages.NO_OFFER_TO_CANCEL);
    }

    public static Offer rejectOffer(Player buyer) throws InputException {
        for (Offer offer : offerList) {
            if(offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(buyer, Messages.NO_OFFER_TO_REJECT);
    }

    public String getConvertedMessage(String message) {
        message = message.replace("%seller%", this.seller.getDisplayName());
        message = message.replace("%buyer%", this.buyer.getDisplayName());
        message = message.replace("%region%", this.region.getRegion().getId());
        message = message.replace("%world%", this.region.getRegionworld());
        message = message.replace("%price%", this.price + "");
        message = message.replace("%currency%", Messages.CURRENCY);
        return message;
    }
}

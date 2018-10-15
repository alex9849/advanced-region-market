package net.alex9849.arm.minifeatures.selloffer;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        if(region.isSold() && AdvancedRegionMarket.getWorldGuardInterface().hasOwner(buyer.getUniqueId(), region.getRegion())) {
            Economy econ = AdvancedRegionMarket.getEcon();

            if(econ.getBalance(buyer) < price) {
                throw new InputException(buyer, Messages.NOT_ENOUGHT_MONEY);
            }
            econ.withdrawPlayer(seller, price);
            econ.depositPlayer(buyer, price);

            AdvancedRegionMarket.getWorldGuardInterface().setOwner(buyer, region.getRegion());

            this.unregister();
        } else {
            this.reject();
            throw new InputException(buyer, seller.getDisplayName() + " does not longer own this region. The offer has been cancelled");
        }


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

    public static Offer createOffer(Region region, double price, Player seller, Player buyer) throws InputException {
        for(int i = 0; i < offerList.size(); i++) {
            if(offerList.get(i).getBuyer().getUniqueId() == buyer.getUniqueId()) {
                throw new InputException(seller, "The buyer already got an offer that he has to answer first!");
            }
            if(offerList.get(i).getSeller().getUniqueId() == seller.getUniqueId()) {
                throw new InputException(seller, "You have already created an offer! Please wait for an answer or cancel it first!");
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
        return offer;
    }

    public static Offer acceptOffer(Player buyer) throws InputException {
        for(Offer offer : offerList) {
            if(offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.accept();
                return offer;
            }
        }
        throw new InputException(buyer, "You dont have an offer to answer");
    }

    public static Offer cancelOffer(Player seller) throws InputException {
        for (Offer offer : offerList) {
            if(offer.getSeller().getUniqueId() == seller.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(seller, "You do not have an offer to cancel");
    }

    public static Offer rejectOffer(Player buyer) throws InputException {
        for (Offer offer : offerList) {
            if(offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(buyer, "You do not have an offer to reject");
    }
}

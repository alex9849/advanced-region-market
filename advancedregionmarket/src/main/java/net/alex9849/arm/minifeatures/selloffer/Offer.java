package net.alex9849.arm.minifeatures.selloffer;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.regionkind.RegionKind;
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

    private Offer(Region region, double price, Player seller, Player buyer) throws IllegalArgumentException {
        if (price < 0) {
            throw new IllegalArgumentException(Messages.PRICE_CAN_NOT_BE_NEGATIVE);
        }
        this.region = region;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
        this.offerListener = new OfferListener(seller, buyer, this);
    }

    public static Offer createOffer(Region region, double price, Player seller, Player buyer) throws DublicateException, IllegalArgumentException {
        for (Offer offer : offerList) {
            if (offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                throw new DublicateException(Messages.BUYER_ALREADY_GOT_AN_OFFER);
            }
            if (offer.getSeller().getUniqueId() == seller.getUniqueId()) {
                throw new DublicateException(Messages.SELLER_ALREADY_CREATED_AN_OFFER);
            }
        }

        for (int i = 0; i < offerList.size(); i++) {
            if (offerList.get(i).getRegion() == region) {
                offerList.get(i).reject();
                i--;
            }
        }
        Offer offer = new Offer(region, price, seller, buyer);
        offerList.add(offer);
        int timer = AdvancedRegionMarket.getInstance().getConfig().getInt("Reselling.Offers.OfferTimeOut");
        if (timer != 0) {
            offer.activateTimer(20 * timer);
        }
        return offer;
    }

    public static Offer acceptOffer(Player buyer) throws InputException, RegionNotOwnException, NoPermissionException, OutOfLimitExeption, NotEnoughMoneyException {
        for (Offer offer : offerList) {
            if (offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.accept();
                return offer;
            }
        }
        throw new InputException(buyer, Messages.NO_OFFER_TO_ANSWER);
    }

    public static Offer cancelOffer(Player seller) throws InputException {
        for (Offer offer : offerList) {
            if (offer.getSeller().getUniqueId() == seller.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(seller, Messages.NO_OFFER_TO_CANCEL);
    }

    public static Offer rejectOffer(Player buyer) throws InputException {
        for (Offer offer : offerList) {
            if (offer.getBuyer().getUniqueId() == buyer.getUniqueId()) {
                offer.reject();
                return offer;
            }
        }
        throw new InputException(buyer, Messages.NO_OFFER_TO_REJECT);
    }

    public static void reset() {
        Offer.offerList = new ArrayList<>();
    }

    public void accept() throws RegionNotOwnException, NoPermissionException, OutOfLimitExeption, NotEnoughMoneyException {
        if (!region.isSold() || !region.getRegion().hasOwner(seller.getUniqueId())) {
            this.reject();
            throw new RegionNotOwnException(this.getConvertedMessage(Messages.SELLER_DOES_NOT_LONGER_OWN_REGION));
        }

        if (!RegionKind.hasPermission(buyer, this.region.getRegionKind())) {
            this.reject();
            throw new NoPermissionException(this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
        }

        if (!LimitGroup.isCanBuyAnother(buyer, region)) {
            this.reject();
            throw new OutOfLimitExeption(LimitGroup.getRegionBuyOutOfLimitMessage(buyer, region.getRegionKind()));
        }

        Economy econ = AdvancedRegionMarket.getInstance().getEcon();

        if (econ.getBalance(buyer) < price) {
            this.reject();
            throw new NotEnoughMoneyException(this.getConvertedMessage(Messages.NOT_ENOUGHT_MONEY));
        }
        econ.depositPlayer(seller, price);
        econ.withdrawPlayer(buyer, price);

        region.getRegion().setOwner(buyer);

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

    public String getConvertedMessage(String message) {
        message = message.replace("%seller%", this.seller.getDisplayName());
        message = message.replace("%buyer%", this.buyer.getDisplayName());
        message = message.replace("%region%", this.region.getRegion().getId());
        message = message.replace("%regionid%", this.region.getRegion().getId());
        message = message.replace("%world%", this.region.getRegionworld().getName());
        message = message.replace("%price%", this.price + "");
        message = message.replace("%currency%", Messages.CURRENCY);
        if (this.region != null) {
            message = this.region.getConvertedMessage(message);
        }
        return message;
    }
}

package net.alex9849.arm.minifeatures.selloffer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OfferListener implements Listener {

    private Player seller;
    private Player buyer;
    private Offer offer;

    protected OfferListener(Player seller, Player buyer, Offer offer) {
        this.seller = seller;
        this.buyer = buyer;
        this.offer = offer;
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        if(event.getPlayer().getUniqueId() == buyer.getUniqueId() || event.getPlayer().getUniqueId() == seller.getUniqueId()) {
            offer.reject();
        }
    }

    protected void unregister() {
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}

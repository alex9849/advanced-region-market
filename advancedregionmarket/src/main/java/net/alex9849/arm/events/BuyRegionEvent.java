package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

/**
 * Will be called in the following cases:
 * Someone buys a SellRegion
 * Someone starts renting a RentRegion
 * Someone starts renting a ContractRegion
 */
public class BuyRegionEvent extends RegionEvent {
    private Player buyer;

    public BuyRegionEvent(Region region, Player buyer) {
        super(region);
        this.buyer = buyer;
    }

    public Player getBuyer() {
        return this.buyer;
    }
}

package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;
import org.bukkit.OfflinePlayer;

/**
 * Will be called in the following cases:
 * Someone buys a SellRegion
 * Someone starts renting a RentRegion
 * Someone starts renting a ContractRegion
 */
public class BuyRegionEvent extends RegionEvent {
    private OfflinePlayer buyer;

    public BuyRegionEvent(Region region, OfflinePlayer buyer) {
        super(region);
        this.buyer = buyer;
    }

    public OfflinePlayer getBuyer() {
        return this.buyer;
    }
}

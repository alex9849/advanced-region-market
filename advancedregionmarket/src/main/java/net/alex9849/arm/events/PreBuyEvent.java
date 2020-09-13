package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

public class PreBuyEvent extends RegionEvent {
    private boolean isPlayerInLimit;
    private Player buyer;
    private boolean isNoMoneyTransfer;

    public PreBuyEvent(Region region, Player buyer, boolean isPlayerInLimit) {
        super(region);
        this.buyer = buyer;
        this.isPlayerInLimit = isPlayerInLimit;
        this.isNoMoneyTransfer = false;
    }

    public boolean isPlayerInLimit() {
        return isPlayerInLimit;
    }

    public void setPlayerInLimit() {
        this.isPlayerInLimit = true;
    }

    public boolean isNoMoneyTransfer() {
        return isNoMoneyTransfer;
    }

    /**
     * If executed no balance checks will be done by the plugin
     * and no money will be transferred
     */
    public void setNoMoneyTransfer() {
        isNoMoneyTransfer = true;
    }

    public Player getBuyer() {
        return buyer;
    }
}

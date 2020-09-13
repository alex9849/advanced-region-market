package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

public class PreExtendEvent extends RegionEvent {
    private boolean isNoMoneyTransfer;

    public PreExtendEvent(Region region) {
        super(region);
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
}

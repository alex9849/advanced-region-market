package net.alex9849.arm.handler;

import net.alex9849.arm.AdvancedRegionMarket;

public class Scheduler implements Runnable {
    @Override
    public void run() {
        AdvancedRegionMarket.getARM().getRegionManager().updateRegions();
        AdvancedRegionMarket.getARM().getRegionManager().resetInactiveRegions();
    }
}

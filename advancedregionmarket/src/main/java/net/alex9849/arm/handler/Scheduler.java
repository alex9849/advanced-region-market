package net.alex9849.arm.handler;

import net.alex9849.arm.AdvancedRegionMarket;

public class Scheduler implements Runnable {
    @Override
    public void run() {
        AdvancedRegionMarket.getInstance().getRegionManager().updateRegions();
    }
}

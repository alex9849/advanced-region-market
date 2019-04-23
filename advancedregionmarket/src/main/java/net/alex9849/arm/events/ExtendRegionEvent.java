package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Will be called in the following cases:
 * Someone extends his RentRegion
 * A ContractRegion gets Extended automatically
 */
public class ExtendRegionEvent extends RegionEvent {

    public ExtendRegionEvent(Region region) {
        super(region);
    }
}

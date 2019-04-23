package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if a region gets unsold
 */
public class UnsellRegionEvent extends RegionEvent {
    public UnsellRegionEvent(Region region) {
        super(region);
    }
}

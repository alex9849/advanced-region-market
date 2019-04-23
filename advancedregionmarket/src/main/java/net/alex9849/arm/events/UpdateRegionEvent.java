package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if a region gets updated
 */
public class UpdateRegionEvent extends RegionEvent {
    public UpdateRegionEvent(Region region) {
        super(region);
    }
}

package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if a region is added to the RegionManager
 */
public class AddRegionEvent extends RegionEvent{
    public AddRegionEvent(Region region) {
        super(region);
    }
}

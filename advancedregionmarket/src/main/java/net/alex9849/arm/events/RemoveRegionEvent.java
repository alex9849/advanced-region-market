package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if a region gets removed from the RegionManager
 */
public class RemoveRegionEvent extends RegionEvent {
    public RemoveRegionEvent(Region region) {
        super(region);
    }
}

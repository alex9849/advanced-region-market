package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if the blocks of a region are getting resetted
 */
public class RestoreRegionEvent extends RegionEvent {

    public RestoreRegionEvent(Region region) {
        super(region);
    }

}

package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if the blocks of a region are getting resetted
 */
public class ResetBlocksEvent extends RegionEvent {

    public ResetBlocksEvent(Region region) {
        super(region);
    }

}

package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;

/**
 * Is called if a setting of a region has been changed and
 * the settings are about to be written to the config file
 * in the next few seconds
 */
public class UpdateRegionEvent extends RegionEvent {
    public UpdateRegionEvent(Region region) {
        super(region);
    }
}

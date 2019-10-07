package net.alex9849.arm.events;

import net.alex9849.arm.regions.Region;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private Region region;
    private boolean isCancelled;

    protected RegionEvent(Region region) {
        this.region = region;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return RegionEvent.handlerList;
    }

    public Region getRegion() {
        return this.region;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}

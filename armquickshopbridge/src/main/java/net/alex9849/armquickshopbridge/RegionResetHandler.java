package net.alex9849.armquickshopbridge;

import net.alex9849.arm.events.ResetBlocksEvent;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Shop.Shop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RegionResetHandler implements Listener {

    @EventHandler
    public void handleRegionReset(ResetBlocksEvent event) {
        Vector minPoint = event.getRegion().getRegion().getMinPoint();
        Vector maxPoint = event.getRegion().getRegion().getMinPoint();
        World world = event.getRegion().getRegionworld();
        Set<Chunk> chuckLocations = new HashSet<Chunk>();

        for(int x = minPoint.getBlockX(); x < maxPoint.getBlockX() + 16; x += 16) {
            for(int y = minPoint.getBlockY(); y < maxPoint.getBlockY() + 16; y += 16) {
                chuckLocations.add(world.getChunkAt(new Location(world, x, y, minPoint.getBlockZ())));
            }
        }

        HashMap<Location, Shop> shopMap = new HashMap<Location, Shop>();

        for(Chunk chunk : chuckLocations) {
            HashMap<Location, Shop> shopsInChunk = QuickShop.instance.getShopManager().getShops(chunk);
            if(shopsInChunk != null) {
                shopMap.putAll(shopsInChunk);
            }
        }
        for(Location shopLocation : shopMap.keySet()) {
            if(event.getRegion().getRegion().contains(shopLocation.getBlockX(), shopLocation.getBlockY(), shopLocation.getBlockZ())) {
                Shop shop = shopMap.get(shopLocation);
                if(shop != null) {
                    shop.delete(false);
                }
            }
        }
    }

}

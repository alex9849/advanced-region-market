package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.subregions.SubRegionCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinQuitEvent implements Listener {

    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByOwner(player.getUniqueId());

        for(Region region : regions) {
            region.setLastLogin();
        }

        if(ArmSettings.isEnableTakeOver()){
            Plugin plugin = AdvancedRegionMarket.getARM();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    PlayerJoinQuitEvent.doOvertakeCheck(player);
                }
            }, 40L);
        }

        if(RentRegion.isSendExpirationWarning()) {
            Plugin plugin = AdvancedRegionMarket.getARM();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    RentRegion.sendExpirationWarnings(player);
                }
            }, 40L);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ActivePresetManager.deletePreset(event.getPlayer());
        SubRegionCreator.removeSubRegioncreator(event.getPlayer());
    }

    public static void doOvertakeCheck(Player player) {
        List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByMember(player.getUniqueId());
        List<Region> takeoverableRegions = new ArrayList<>();
        for(Region region : regions) {
            if(region.isTakeOverReady()) {
                takeoverableRegions.add(region);
            }
        }

        if(takeoverableRegions.size() > 0) {
            Gui.openOvertakeGUI(player, takeoverableRegions);
        }
    }

}

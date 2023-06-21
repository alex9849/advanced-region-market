package net.alex9849.arm.handler.listener;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.CountdownRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.subregions.SubRegionCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class PlayerJoinQuitEvent implements Listener {

    public static void doTakeOverCheck(Player player) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByMember(player.getUniqueId());
        List<Region> takeoverableRegions = new ArrayList<>();
        for (Region region : regions) {
            if (region.isTakeOverReady()) {
                takeoverableRegions.add(region);
            }
        }

        if (takeoverableRegions.size() > 0) {
            Gui.openOvertakeGUI(player, takeoverableRegions, null);
        }
    }

    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();
        List<Region> regions = plugin.getRegionManager().getRegionsByOwner(player.getUniqueId());


        for (Region region : regions) {
            region.setLastLogin();
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                PlayerJoinQuitEvent.doTakeOverCheck(player);
            }
        }, 40L);

        if (plugin.getPluginSettings().isSendRentRegionExpirationWarning()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    sendExpirationWarnings(player);
                }
            }, 40L);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ActivePresetManager.deletePreset(event.getPlayer());
        SubRegionCreator.removeSubRegioncreator(event.getPlayer());
    }

    private static void sendExpirationWarnings(Player player) {
        AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();

        for (Region region : plugin.getRegionManager().getRegionsByOwner(player.getUniqueId())) {
            if (!(region instanceof CountdownRegion)) {
                continue;
            }
            CountdownRegion countdownRegion = (CountdownRegion) region;
            long remainingTime = countdownRegion.getPayedTill() - new GregorianCalendar().getTimeInMillis();
            if (countdownRegion.isSold() && remainingTime > plugin.getPluginSettings().getRentRegionExpirationWarningTime()) {
                continue;
            }

            if (countdownRegion instanceof ContractRegion) {
                double pBalance = AdvancedRegionMarket.getInstance().getEcon().getBalance(player);
                ContractRegion contractRegion = (ContractRegion) countdownRegion;
                if (contractRegion.isTerminated()) {
                    player.sendMessage(contractRegion.replaceVariables(Messages.PREFIX + Messages.CONTRACTREGION_EXPIRATION_WARNING_TERMINATED));
                } else if (!contractRegion.isTerminated() && contractRegion.getPricePerPeriod() > pBalance) {
                    player.sendMessage(contractRegion.replaceVariables(Messages.PREFIX + Messages.CONTRACTREGION_EXPIRATION_WARNING_LOW_BALANCE));
                }
            } else if (countdownRegion instanceof RentRegion) {
                player.sendMessage(countdownRegion.replaceVariables(Messages.PREFIX + Messages.RENTREGION_EXPIRATION_WARNING));
            }
        }
    }

}

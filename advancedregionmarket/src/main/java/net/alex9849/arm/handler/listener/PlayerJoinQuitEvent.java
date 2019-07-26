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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerJoinQuitEvent implements Listener {

    @EventHandler
    public void setLastLoginAndOpenOvertake(PlayerJoinEvent event) {
        if(ArmSettings.isEnableAutoReset() || ArmSettings.isEnableTakeOver()){
            try{
                ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");

                if(rs.next()){
                    ArmSettings.getStmt().executeUpdate("UPDATE `" + ArmSettings.getSqlPrefix() + "lastlogin` SET `lastlogin` = CURRENT_TIMESTAMP WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'");
                } else {
                    ArmSettings.getStmt().executeUpdate("INSERT INTO `" + ArmSettings.getSqlPrefix() + "lastlogin` (`uuid`, `lastlogin`) VALUES ('" + event.getPlayer().getUniqueId().toString() + "', CURRENT_TIMESTAMP)");
                }

            } catch (SQLException e) {
                AdvancedRegionMarket.reconnectSQL();
            } catch (NullPointerException e) {
                AdvancedRegionMarket.reconnectSQL();
            }
        }

        if(ArmSettings.isEnableTakeOver()){
            Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
            Player player = event.getPlayer();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    PlayerJoinQuitEvent.doOvertakeCheck(player);
                }
            }, 40L);
        }

        if(RentRegion.isSendExpirationWarning()) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
            Player player = event.getPlayer();
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
        GregorianCalendar comparedate = new GregorianCalendar();
        comparedate.add(Calendar.DAY_OF_MONTH, (-1 * ArmSettings.getTakeoverAfter()));
        Date convertdate = new Date();
        convertdate.setTime(comparedate.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String compareTime = sdf.format(convertdate);

        if(AdvancedRegionMarket.getRegionManager() == null) {
            return;
        }

        try {
            ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");

            List<Region> overtake = new LinkedList<>();
            while (rs.next()){
                List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByOwner(UUID.fromString(rs.getString("uuid")));

                for(int i = 0; i < regions.size(); i++){
                    if(regions.get(i).getAutoreset()){
                        if(regions.get(i).getRegion().hasMember(player.getUniqueId())){
                            overtake.add(regions.get(i));
                        }
                    }
                }
            }
            if(overtake.size() != 0){
                Gui.openOvertakeGUI(player, overtake);
            }

        } catch (SQLException e) {
            AdvancedRegionMarket.reconnectSQL();
        } catch (NullPointerException e) {
            AdvancedRegionMarket.reconnectSQL();
        }
    }

}

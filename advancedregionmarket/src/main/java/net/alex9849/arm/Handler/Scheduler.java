package net.alex9849.arm.Handler;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class Scheduler implements Runnable {
    @Override
    public void run() {
        Region.updateRegions();

        if(AdvancedRegionMarket.getEnableAutoReset()){

            GregorianCalendar comparedate = new GregorianCalendar();
            comparedate.add(Calendar.DAY_OF_MONTH, (-1 * AdvancedRegionMarket.getAutoResetAfter()));
            Date convertdate = new Date();
            convertdate.setTime(comparedate.getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String compareTime = sdf.format(convertdate);

            try {
                ResultSet rs = AdvancedRegionMarket.getStmt().executeQuery("SELECT * FROM `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");
                if (rs.next()){
                    Region.autoResetRegionsFromOwner(UUID.fromString(rs.getString("uuid")));
                    List<Region> regions = Region.getRegionsByMember(UUID.fromString(rs.getString("uuid")));
                    for (int i = 0; i < regions.size(); i++){
                        if(regions.get(i).getAutoreset()){
                            AdvancedRegionMarket.getWorldGuardInterface().removeMember(UUID.fromString(rs.getString("uuid")), regions.get(i).getRegion());
                        }
                    }
                    AdvancedRegionMarket.getStmt().executeUpdate("DELETE FROM `" + AdvancedRegionMarket.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + rs.getString("uuid") + "'");
                }
            } catch (SQLException e) {
                Bukkit.getServer().getLogger().log(Level.WARNING, "[AdvancedRegionMarket] SQL connection lost. Reconnecting...");
                AdvancedRegionMarket arm = AdvancedRegionMarket.getARM();
                if(arm != null) {
                    if(arm.connectSQL()) {
                        Bukkit.getLogger().log(Level.INFO, "SQL Login successful!");
                    } else {
                        Bukkit.getLogger().log(Level.INFO, "SQL Login failed!");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        }
    }
}

package net.alex9849.arm.handler;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.regions.Region;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scheduler implements Runnable {
    @Override
    public void run() {
        AdvancedRegionMarket.getRegionManager().updateRegions();

        if(ArmSettings.isEnableAutoReset()){

            GregorianCalendar comparedate = new GregorianCalendar();
            comparedate.add(Calendar.DAY_OF_MONTH, (-1 * ArmSettings.getAutoResetAfter()));
            Date convertdate = new Date();
            convertdate.setTime(comparedate.getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String compareTime = sdf.format(convertdate);

            try {
                ResultSet rs = ArmSettings.getStmt().executeQuery("SELECT * FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");
                if (rs.next()){
                    AdvancedRegionMarket.getRegionManager().autoResetRegionsFromOwner(UUID.fromString(rs.getString("uuid")));
                    List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByMember(UUID.fromString(rs.getString("uuid")));
                    for (int i = 0; i < regions.size(); i++){
                        if(regions.get(i).getAutoreset()){
                            regions.get(i).getRegion().removeMember(UUID.fromString(rs.getString("uuid")));
                        }
                    }
                    ArmSettings.getStmt().executeUpdate("DELETE FROM `" + ArmSettings.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + rs.getString("uuid") + "'");
                }
            } catch (SQLException e) {
                AdvancedRegionMarket.reconnectSQL();
            } catch (NullPointerException e) {
                AdvancedRegionMarket.reconnectSQL();
            }
        }
    }
}

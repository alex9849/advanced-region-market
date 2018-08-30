package net.liggesmeyer.arm;

import net.liggesmeyer.arm.regions.ContractRegion;
import net.liggesmeyer.arm.regions.Region;
import net.liggesmeyer.arm.regions.RentRegion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scheduler implements Runnable {
    @Override
    public void run() {
        RentRegion.doUpdates();
        ContractRegion.doUpdates();

        if(Main.getEnableAutoReset()){

            GregorianCalendar comparedate = new GregorianCalendar();
            comparedate.add(Calendar.DAY_OF_MONTH, (-1 * Main.getAutoResetAfter()));
            Date convertdate = new Date();
            convertdate.setTime(comparedate.getTimeInMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String compareTime = sdf.format(convertdate);

            try {
                ResultSet rs = Main.getStmt().executeQuery("SELECT * FROM `" + Main.getSqlPrefix() + "lastlogin` WHERE `lastlogin` < '" + compareTime + "'");
                if (rs.next()){
                    Region.autoResetRegionsFromOwner(UUID.fromString(rs.getString("uuid")));
                    List<Region> regions = Region.getRegionsByMember(UUID.fromString(rs.getString("uuid")));
                    for (int i = 0; i < regions.size(); i++){
                        if(regions.get(i).getAutoreset()){
                            Main.getWorldGuardInterface().removeMember(UUID.fromString(rs.getString("uuid")), regions.get(i).getRegion());
                        }
                    }
                    Main.getStmt().executeUpdate("DELETE FROM `" + Main.getSqlPrefix() + "lastlogin` WHERE `uuid` = '" + rs.getString("uuid") + "'");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

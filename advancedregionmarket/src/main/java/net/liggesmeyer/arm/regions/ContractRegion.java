package net.liggesmeyer.arm.regions;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.List;

public class ContractRegion extends Region {

    public ContractRegion(ProtectedRegion region, String regionworld, List<Sign> sellsign, double price, Boolean sold, Boolean autoreset, Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, Boolean newreg) {
        super(region, regionworld, sellsign, price, sold, autoreset, isHotel, doBlockReset, regionKind, teleportLoc, lastreset, newreg);
    }

    @Override
    protected void setSold(OfflinePlayer player) {

    }

    @Override
    protected void updateSignText(Sign mysign) {

    }

    @Override
    protected void buy(Player player) {

    }

    @Override
    public void userSell(Player player) {

    }

    @Override
    public double getPaybackMoney() {
        return 0;
    }
}

package net.liggesmeyer.arm.regions;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.arm.Main;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.GregorianCalendar;
import java.util.List;

public class ContractRegion extends Region {
    private long payedTill;
    private long extendTime;
    private boolean terminated;

    public ContractRegion(ProtectedRegion region, String regionworld, List<Sign> contractsign, double price, Boolean sold, Boolean autoreset, Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, long extendTime, long payedTill, Boolean terminated, Boolean newreg) {
        super(region, regionworld, contractsign, price, sold, autoreset, isHotel, doBlockReset, regionKind, teleportLoc, lastreset, newreg);
        this.payedTill = payedTill;
        this.extendTime = extendTime;
        this.terminated = terminated;

        if(newreg) {
            YamlConfiguration config = getRegionsConf();

            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".regiontype", "contractregion");
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", payedTill);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".extendTime", extendTime);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".terminated", terminated);
            saveRegionsConf(config);
            this.updateSignText(contractsign.get(0));
        }
    }

    @Override
    protected void setSold(OfflinePlayer player) {
        if(!this.sold) {
            GregorianCalendar actualtime = new GregorianCalendar();
            this.payedTill = actualtime.getTimeInMillis() + this.extendTime;
        }
        this.sold = true;
        this.terminated = false;
        Main.getWorldGuardInterface().deleteMembers(this.getRegion());
        Main.getWorldGuardInterface().setOwner(player, this.getRegion());

        this.updateSigns();

        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".sold", true);
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", this.payedTill);
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".terminated", terminated);
        saveRegionsConf(config);
    }

    @Override
    protected void updateSignText(Sign mysign) {

    }

    @Override
    public void buy(Player player) {

    }

    @Override
    public void userSell(Player player) {

    }

    @Override
    public double getPaybackMoney() {
        return 0;
    }
}

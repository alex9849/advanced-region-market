package net.liggesmeyer.arm.regions;

import net.liggesmeyer.arm.Permission;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.arm.Group.LimitGroup;
import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RentRegion extends Region {
    private long payedTill;
    private long maxRentTime;
    private long rentExtendPerClick;

    public RentRegion(ProtectedRegion region, String regionworld, List<Sign> rentsign, double price, Boolean sold, Boolean autoreset, Boolean allowOnlyNewBlocks,
                      Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, long payedTill, long maxRentTime, long rentExtendPerClick, Boolean newreg) {
        super(region, regionworld, rentsign, price, sold, autoreset, allowOnlyNewBlocks, doBlockReset, regionKind, teleportLoc, lastreset, newreg);

        this.payedTill = payedTill;
        this.maxRentTime = maxRentTime;
        this.rentExtendPerClick = rentExtendPerClick;

        if(newreg) {
            YamlConfiguration config = getRegionsConf();

            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".rentregion", true);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", payedTill);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".maxRentTime", maxRentTime);
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".rentExtendPerClick", rentExtendPerClick);
            saveRegionsConf(config);
            this.updateSignText(rentsign.get(0));
        }
    }

    @Override
    protected void updateSignText(Sign mysign){

        if (this.sold){

            LinkedList<UUID> ownerlist = new LinkedList<>(this.getRegion().getOwners().getUniqueIds());
            String ownername;
            if(ownerlist.size() < 1){
                ownername = "Unknown";
            } else {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerlist.get(0));
                ownername = owner.getName();
            }

            String line1 = Messages.RENTED_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());
            line1 = line1.replace("%owner%", ownername);
            line1 = line1.replace("%extendpercick%", this.getExtendPerClick());
            line1 = line1.replace("%maxrenttime%", this.getMaxRentTime());
            line1 = line1.replace("%remaining%", this.calcRemainingTime());

            String line2 = Messages.RENTED_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%owner%", ownername);
            line2 = line2.replace("%extendpercick%", this.getExtendPerClick());
            line2 = line2.replace("%maxrenttime%", this.getMaxRentTime());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());

            String line3 = Messages.RENTED_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%owner%", ownername);
            line3 = line3.replace("%extendpercick%", this.getExtendPerClick());
            line3 = line3.replace("%maxrenttime%", this.getMaxRentTime());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());

            String line4 = Messages.RENTED_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%owner%", ownername);
            line4 = line4.replace("%extendpercick%", this.getExtendPerClick());
            line4 = line4.replace("%maxrenttime%", this.getMaxRentTime());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();

        } else {
            String line1 = Messages.RENT_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());
            line1 = line1.replace("%extendpercick%", this.getExtendPerClick());
            line1 = line1.replace("%maxrenttime%", this.getMaxRentTime());
            line1 = line1.replace("%remaining%", this.calcRemainingTime());

            String line2 = Messages.RENT_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%extendpercick%", this.getExtendPerClick());
            line2 = line2.replace("%maxrenttime%", this.getMaxRentTime());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());

            String line3 = Messages.RENT_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%extendpercick%", this.getExtendPerClick());
            line3 = line3.replace("%maxrenttime%", this.getMaxRentTime());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());

            String line4 = Messages.RENT_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%extendpercick%", this.getExtendPerClick());
            line4 = line4.replace("%maxrenttime%", this.getMaxRentTime());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();
        }
    }

    @Override
    public void buy(Player player){

        if(!player.hasPermission(Permission.ARM_BUY_RENTREGION)) {
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return;
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!player.hasPermission(Permission.ARM_BUYKIND + this.regionKind.getName())){
                player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
                return;
            }
        }

        if(this.sold) {
            this.extendRegion(player);
            return;
        }

        if (this.regionKind != RegionKind.DEFAULT){
            if(!player.hasPermission(Permission.ARM_BUYKIND + this.regionKind.getName())){
                player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
            }
        }

        if(!LimitGroup.isCanBuyAnother(player, this)){
            int limittotal = LimitGroup.getLimit(player);
            int limitkind = LimitGroup.getLimit(player, this.regionKind);
            String limittotalS = "" + limittotal;
            String limitkindS = "" + limitkind;

            if(limitkind == Integer.MAX_VALUE) {
                limitkindS = Messages.UNLIMITED;
            }
            if(limittotal == Integer.MAX_VALUE) {
                limittotalS = Messages.UNLIMITED;
            }
            String message = Messages.REGION_BUY_OUT_OF_LIMIT;
            message = message.replace("%playerownedkind%", LimitGroup.getOwnedRegions(player, this.regionKind) + "");
            message = message.replace("%limitkind%", limitkindS);
            message = message.replace("%regionkind%", this.regionKind.getName());
            message = message.replace("%playerownedtotal%", LimitGroup.getOwnedRegions(player) + "");
            message = message.replace("%limittotal%", limittotalS);

            player.sendMessage(Messages.PREFIX + message);
            return;
        }

        GregorianCalendar actualtime = new GregorianCalendar();
        this.payedTill = actualtime.getTimeInMillis() + this.rentExtendPerClick;

        if(Main.getEcon().getBalance(player) < this.price) {
            player.sendMessage(Messages.PREFIX + Messages.NOT_ENOUGHT_MONEY);
            return;
        }
        Main.getEcon().withdrawPlayer(player, price);


        this.setSold(player);
        if(Main.isTeleportAfterRentRegionBought()){
            this.teleportToRegion(player);
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);
    }

    @Override
    protected void setSold(OfflinePlayer player){
        this.sold = true;
        Main.getWorldGuardInterface().deleteMembers(this.getRegion());
        Main.getWorldGuardInterface().setOwner(player, this.getRegion());

        for(int i = 0; i < this.sellsign.size(); i++){
            this.updateSignText(this.sellsign.get(i));
        }



        YamlConfiguration config = getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".sold", true);
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", this.payedTill);
        saveRegionsConf(config);

    }

    @Override
    public void userSell(Player player){
        List<UUID> defdomain = Main.getWorldGuardInterface().getOwners(this.getRegion());
        double amount = this.getPaybackMoney();

        if(amount > 0){
            for(int i = 0; i < defdomain.size(); i++) {
                Main.getEcon().depositPlayer(Bukkit.getOfflinePlayer(defdomain.get(i)), amount);
            }
        }

        this.resetRegion(player);
    }

    @Override
    public double getPaybackMoney() {
        double amount = (this.getPrice() * paybackPercentage)/100;
        GregorianCalendar acttime = new GregorianCalendar();
        long remaining = this.payedTill - acttime.getTimeInMillis();
        amount = amount * (remaining / rentExtendPerClick);
        return amount;
    }

    public static long stringToTime(String stringtime) throws IllegalArgumentException {
        long time = 0;
        if(stringtime.matches("[\\d]+d")){
            time = Long.parseLong(stringtime.split("d")[0]);
            time = time * 1000*60*60*24;
        } else if(stringtime.matches("[\\d]+h")){
            time = Long.parseLong(stringtime.split("h")[0]);
            time = time * 1000*60*60;
        } else if(stringtime.matches("[\\d]+m")){
            time = Long.parseLong(stringtime.split("m")[0]);
            time = time * 1000*60;
        } else if(stringtime.matches("[\\d]+s")){
            time = Long.parseLong(stringtime.split("s")[0]);
            time = time * 1000;
        } else {
            throw new IllegalArgumentException();
        }
        return time;
    }

    public String calcRemainingTime() {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(this.payedTill);

        long remainingMilliSeconds = payedTill.getTimeInMillis() - actualtime.getTimeInMillis();

        if(remainingMilliSeconds < 0){
            return "0s";
        }

        long remainingDays = TimeUnit.DAYS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    public String getExtendPerClick(){
        long time = this.rentExtendPerClick;

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    public String getMaxRentTime(){
        long time = this.maxRentTime;

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }

    public static void doSignUpdate(){
        for (int i = 0; i < getRegionList().size(); i++) {
            if(getRegionList().get(i) instanceof RentRegion) {
                RentRegion region = (RentRegion) getRegionList().get(i);
                if(region.isSold()){
                    GregorianCalendar actualtime = new GregorianCalendar();
                    if(region.payedTill < actualtime.getTimeInMillis()){
                        region.unsell();
                        if(region.isDoBlockReset()){
                            region.resetBlocks();
                        }
                    } else {
                        for (int y = 0; y < region.sellsign.size(); y++){
                            region.updateSignText(region.sellsign.get(y));
                        }
                    }
                }
            }
        }
    }

    public void setPayedTill(long payedTill) {
        this.payedTill = payedTill;
    }

    public long getRentExtendPerClick(){
        return this.rentExtendPerClick;
    }

    public long getPayedTill() {
        return this.payedTill;
    }

    public void extendRegion(Player player) {
        if(!player.hasPermission(Permission.ARM_BUY_RENTREGION)) {
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return;
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!player.hasPermission(Permission.ARM_BUYKIND + this.regionKind.getName())){
                player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
                return;
            }
        }

        if(!Main.getWorldGuardInterface().hasOwner(player, this.getRegion())) {
            if(!player.hasPermission(Permission.ADMIN_EXTEND)){
                player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_OWN);
                return;
            }
            if(!this.sold) {
                player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_SOLD);
                return;
            }
        }
        GregorianCalendar actualtime = new GregorianCalendar();
        if (this.maxRentTime < ((this.payedTill + this.rentExtendPerClick) - actualtime.getTimeInMillis())){
            String errormessage = Messages.RENT_EXTEND_ERROR;
            errormessage = errormessage.replace("%remaining%", this.calcRemainingTime());
            errormessage = errormessage.replace("%maxrenttime%", this.getMaxRentTime());
            errormessage = errormessage.replace("%extendpercick%", this.getExtendPerClick());
            errormessage = errormessage.replace("%price%", this.price + Messages.CURRENCY);
            player.sendMessage(Messages.PREFIX + errormessage);
            return;
        } else {
            if(Main.getEcon().getBalance(player) < this.price) {
                player.sendMessage(Messages.PREFIX + Messages.NOT_ENOUGHT_MONEY);
                return;
            }
            Main.getEcon().withdrawPlayer(player, price);
            this.payedTill = this.payedTill + this.rentExtendPerClick;
            YamlConfiguration config = getRegionsConf();
            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", this.payedTill);
            saveRegionsConf(config);

            String message = Messages.RENT_EXTEND_MESSAGE;
            message = message.replace("%remaining%", this.calcRemainingTime());
            message = message.replace("%maxrenttime%", this.getMaxRentTime());
            message = message.replace("%extendpercick%", this.getExtendPerClick());
            message = message.replace("%price%", this.price + "");
            message = message.replace("%currency%", Messages.CURRENCY);
            player.sendMessage(Messages.PREFIX + message);

            for(int i = 0; i < this.sellsign.size(); i++){
                this.updateSignText(this.sellsign.get(i));
            }

            if(Main.isTeleportAfterRentRegionExtend()) {
                this.teleportToRegion(player);
            }

            return;
        }
    }
}

package net.alex9849.arm.regions;

import net.alex9849.arm.Permission;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Main;
import net.alex9849.arm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RentRegion extends Region {
    private long payedTill;
    private long maxRentTime;
    private long rentExtendPerClick;
    private static long expirationWarningTime;
    private static Boolean sendExpirationWarning;

    public RentRegion(ProtectedRegion region, String regionworld, List<Sign> rentsign, double price, Boolean sold, Boolean autoreset, Boolean allowOnlyNewBlocks,
                      Boolean doBlockReset, RegionKind regionKind, Location teleportLoc, long lastreset, long payedTill, long maxRentTime, long rentExtendPerClick, Boolean newreg) {
        super(region, regionworld, rentsign, price, sold, autoreset, allowOnlyNewBlocks, doBlockReset, regionKind, teleportLoc, lastreset, newreg);

        this.payedTill = payedTill;
        this.maxRentTime = maxRentTime;
        this.rentExtendPerClick = rentExtendPerClick;

        if(newreg) {
            YamlConfiguration config = getRegionsConf();

            config.set("Regions." + this.regionworld + "." + this.region.getId() + ".regiontype", "rentregion");
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
    public void displayExtraInfo(CommandSender sender) {
        sender.sendMessage(Messages.REGION_INFO_REMAINING_TIME + this.calcRemainingTime());
        sender.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + this.getExtendPerClick());
        sender.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + this.getMaxRentTime());
    }

    @Override
    public void updateRegion() {
        if(this.isSold()){
            GregorianCalendar actualtime = new GregorianCalendar();
            if(this.payedTill < actualtime.getTimeInMillis()){
                this.unsell();
                if(this.isDoBlockReset()){
                    this.resetBlocks();
                }
            } else {
                this.updateSigns();
            }
        } else {
            this.updateSigns();
        }
    }

    @Override
    protected void setSold(OfflinePlayer player){
        if(!this.sold) {
            GregorianCalendar actualtime = new GregorianCalendar();
            this.payedTill = actualtime.getTimeInMillis() + this.rentExtendPerClick;
        }
        this.sold = true;
        Main.getWorldGuardInterface().deleteMembers(this.getRegion());
        Main.getWorldGuardInterface().setOwner(player, this.getRegion());

        this.updateSigns();

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
        String timetoString = Main.getRemainingTimeTimeformat();
        timetoString = timetoString.replace("%countdown%", this.getCountdown(Main.isUseShortCountdown()));
        timetoString = timetoString.replace("%date%", this.getDate(Main.getDateTimeformat()));


        return timetoString;
    }

    private String getDate(String regex) {
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(this.payedTill);

        SimpleDateFormat sdf = new SimpleDateFormat(regex);

        return sdf.format(payedTill.getTime());
    }

    private String getCountdown(Boolean mini){
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(this.payedTill);

        long remainingMilliSeconds = payedTill.getTimeInMillis() - actualtime.getTimeInMillis();

        String sec;
        String min;
        String hour;
        String days;
        if(mini) {
            sec = " " + Messages.TIME_SECONDS_SHORT;
            min = " " + Messages.TIME_MINUTES_SHORT;
            hour = " " + Messages.TIME_HOURS_SHORT;
            days = " " + Messages.TIME_DAYS_SHORT;
        } else {
            sec = Messages.TIME_SECONDS;
            min = Messages.TIME_MINUTES;
            hour = Messages.TIME_HOURS;
            days = Messages.TIME_DAYS;
        }

        if(remainingMilliSeconds < 0){
            return "0" + sec;
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
            timetoString = timetoString + remainingDays + days;
            if(mini){
                return timetoString;
            }
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + hour;
            if(mini){
                return timetoString;
            }
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + min;
            if(mini){
                return timetoString;
            }
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + sec;
            if(mini){
                return timetoString;
            }
        }
        if(remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0){
            timetoString = "0" + sec;
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
            timetoString = timetoString + remainingDays + Messages.TIME_DAYS;
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + Messages.TIME_HOURS;
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + Messages.TIME_MINUTES;
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + Messages.TIME_SECONDS;
        }
        if(remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0){
            timetoString = "0" + Messages.TIME_SECONDS;
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
            timetoString = timetoString + remainingDays + Messages.TIME_DAYS;
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + Messages.TIME_HOURS;
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + Messages.TIME_MINUTES;
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + Messages.TIME_SECONDS;
        }
        if(remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0){
            timetoString = "0" + Messages.TIME_SECONDS;
        }

        return timetoString;
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

        if(!this.sold) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_SOLD);
            return;
        }

        if(!Main.getWorldGuardInterface().hasOwner(player, this.getRegion())) {
            if(!player.hasPermission(Permission.ADMIN_EXTEND)){
                player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_OWN);
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

    public static void sendExpirationWarnings(Player player) {
        List<Region> regions = getRegionsByOwner(player.getUniqueId());
        List<RentRegion> rentRegions = new ArrayList<>();
        for(int i = 0; i < regions.size(); i++) {
            if(regions.get(i) instanceof RentRegion) {
                RentRegion rentRegion = (RentRegion) regions.get(i);
                if((rentRegion.getPayedTill() - (new GregorianCalendar().getTimeInMillis())) <= RentRegion.expirationWarningTime) {
                    rentRegions.add(rentRegion);
                }
            }
        }
        String regionWarnings = "";
        for(int i = 0; i < rentRegions.size() - 1; i++) {
            regionWarnings = regionWarnings + rentRegions.get(i).getRegion().getId() + ", ";
        }
        if(rentRegions.size() > 0) {
            regionWarnings = regionWarnings + rentRegions.get(rentRegions.size() - 1).getRegion().getId();
            player.sendMessage(Messages.PREFIX + Messages.RENTREGION_EXPIRATION_WARNING + regionWarnings);
        }
    }

    public static void setExpirationWarningTime(long time) {
        RentRegion.expirationWarningTime = time;
    }

    public static void setSendExpirationWarning(Boolean bool) {
        RentRegion.sendExpirationWarning = bool;
    }

    public static Boolean isSendExpirationWarning(){
        return RentRegion.sendExpirationWarning;
    }
}

package net.liggesmeyer.arm.regions;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.liggesmeyer.arm.Group.LimitGroup;
import net.liggesmeyer.arm.Main;
import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

        if (this.sold) {

            LinkedList<UUID> ownerlist = new LinkedList<>(this.getRegion().getOwners().getUniqueIds());
            String ownername;
            if (ownerlist.size() < 1) {
                ownername = "Unknown";
            } else {
                OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerlist.get(0));
                ownername = owner.getName();
            }

            String line1 = Messages.CONTRACT_SOLD_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());
            line1 = line1.replace("%owner%", ownername);
            line1 = line1.replace("%extend%", this.getExtendTimeString());
            line1 = line1.replace("%remaining%", this.calcRemainingTime());

            String line2 = Messages.CONTRACT_SOLD_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%owner%", ownername);
            line2 = line2.replace("%extend%", this.getExtendTimeString());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());

            String line3 = Messages.CONTRACT_SOLD_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%owner%", ownername);
            line3 = line3.replace("%extend%", this.getExtendTimeString());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());

            String line4 = Messages.CONTRACT_SOLD_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%owner%", ownername);
            line4 = line4.replace("%extend%", this.getExtendTimeString());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();

        } else {
            String line1 = Messages.CONTRACT_SIGN1.replace("%regionid%", this.getRegion().getId());
            line1 = line1.replace("%price%", this.price + "");
            line1 = line1.replace("%currency%", Messages.CURRENCY);
            line1 = line1.replace("%dimensions%", this.getDimensions());
            line1 = line1.replace("%extend%", this.getExtendTimeString());
            line1 = line1.replace("%remaining%", this.calcRemainingTime());

            String line2 = Messages.CONTRACT_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%extend%", this.getExtendTimeString());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());

            String line3 = Messages.CONTRACT_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%extend%", this.getExtendTimeString());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());

            String line4 = Messages.CONTRACT_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%extend%", this.getExtendTimeString());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());

            mysign.setLine(0, line1);
            mysign.setLine(1, line2);
            mysign.setLine(2, line3);
            mysign.setLine(3, line4);
            mysign.update();
        }
    }

    @Override
    public void buy(Player player) {

        if(!player.hasPermission(Permission.ARM_BUY_CONTRACTREGION)) {
            player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
            return;
        }
        if(this.sold) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_ALREADY_SOLD);
            return;
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!player.hasPermission(Permission.ARM_BUYKIND + this.regionKind.getName())){
                player.sendMessage(Messages.PREFIX + Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION);
                return;
            }
        }

        if(!LimitGroup.isCanBuyAnother(player, this)) {
            int limittotal = LimitGroup.getLimit(player);
            int limitkind = LimitGroup.getLimit(player, this.regionKind);
            String limittotalS = "" + limittotal;
            String limitkindS = "" + limitkind;

            if (limitkind == Integer.MAX_VALUE) {
                limitkindS = Messages.UNLIMITED;
            }
            if (limittotal == Integer.MAX_VALUE) {
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
     /*   if(Main.isTeleportAfterSellRegionBought()){
            this.teleportToRegion(player);
        } */
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);

    }

    @Override
    public void userSell(Player player) {

    }

    @Override
    public double getPaybackMoney() {
        return 0;
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

    public String getExtendTimeString(){
        long time = this.extendTime;

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

    public static void doUpdates(){
        for (int i = 0; i < getRegionList().size(); i++) {
            if(getRegionList().get(i) instanceof ContractRegion) {
                ContractRegion region = (ContractRegion) getRegionList().get(i);
                if(region.isSold()){
                    GregorianCalendar actualtime = new GregorianCalendar();
                    if((region.payedTill < actualtime.getTimeInMillis()) && region.terminated){
                        region.unsell();
                        if(region.isDoBlockReset()){
                            region.resetBlocks();
                        }
                    } else if(region.payedTill < actualtime.getTimeInMillis()) {
                        List<UUID> owners = Main.getWorldGuardInterface().getOwners(region.getRegion());
                        if(owners.size() == 0){
                            region.extend();
                            region.updateSigns();
                        } else {
                            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(owners.get(0));
                            if(oplayer == null) {
                                region.extend();
                                region.updateSigns();
                            } else {
                                if(Main.getEcon().getBalance(oplayer) < region.getPrice()) {
                                    region.unsell();
                                    if(region.isDoBlockReset()){
                                        region.resetBlocks();
                                    }
                                } else {
                                    Main.getEcon().withdrawPlayer(oplayer, region.getPrice());
                                    if(oplayer.isOnline()) {
                                        Player player = Bukkit.getPlayer(owners.get(0));
                                        region.extend(player);
                                        region.updateSigns();
                                    } else {
                                        region.extend();
                                        region.updateSigns();
                                    }
                                }
                            }
                        }
                    } else {
                        region.updateSigns();
                    }
                }
            }
        }
    }

    public void extend(){
        this.extend(null);
    }

    public void extend(Player player){
        GregorianCalendar actualtime = new GregorianCalendar();
        while (this.payedTill < actualtime.getTimeInMillis()) {
            this.payedTill = this.payedTill + this.extendTime;
        }
        YamlConfiguration config = Region.getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".payedTill", payedTill);
        Region.saveRegionsConf(config);
        if(player != null) {
            String sendmessage = Messages.CONTRACT_REGION_EXTENDED;
            sendmessage = sendmessage.replace("%price%", this.price + "");
            sendmessage = sendmessage.replace("%currency%", Messages.CURRENCY);
            sendmessage = sendmessage.replace("%extend%", getExtendTimeString());
            sendmessage = sendmessage.replace("%regionid%", getRegion().getId());
            player.sendMessage(sendmessage);
        }
    }
}

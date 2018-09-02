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
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
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
    public void displayExtraInfo(CommandSender sender) {
        sender.sendMessage(Messages.REGION_INFO_TERMINATED + this.terminated);
        sender.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + this.getExtendTimeString());
        sender.sendMessage(Messages.REGION_INFO_NEXT_EXTEND_REMAINING_TIME + this.calcRemainingTime());
    }

    @Override
    public void updateRegion() {
        if(this.isSold()){
            GregorianCalendar actualtime = new GregorianCalendar();
            if((this.payedTill < actualtime.getTimeInMillis()) && this.terminated){
                this.unsell();
                if(this.isDoBlockReset()){
                    this.resetBlocks();
                }
            } else if(this.payedTill < actualtime.getTimeInMillis()) {
                List<UUID> owners = Main.getWorldGuardInterface().getOwners(this.getRegion());
                if(owners.size() == 0){
                    this.extend();
                    this.updateSigns();
                } else {
                    OfflinePlayer oplayer = Bukkit.getOfflinePlayer(owners.get(0));
                    if(oplayer == null) {
                        this.extend();
                        this.updateSigns();
                    } else {
                        if(Main.getEcon().hasAccount(oplayer)) {
                            if(Main.getEcon().getBalance(oplayer) < this.getPrice()) {
                                this.unsell();
                                if(this.isDoBlockReset()){
                                    this.resetBlocks();
                                }
                            } else {
                                Main.getEcon().withdrawPlayer(oplayer, this.getPrice());
                                if(oplayer.isOnline()) {
                                    Player player = Bukkit.getPlayer(owners.get(0));
                                    this.extend(player);
                                    this.updateSigns();
                                } else {
                                    this.extend();
                                    this.updateSigns();
                                }
                            }
                        }
                    }
                }
            } else {
                this.updateSigns();
            }
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
            line1 = line1.replace("%status%", this.getTerminationString());

            String line2 = Messages.CONTRACT_SOLD_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%owner%", ownername);
            line2 = line2.replace("%extend%", this.getExtendTimeString());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());
            line2 = line2.replace("%status%", this.getTerminationString());

            String line3 = Messages.CONTRACT_SOLD_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%owner%", ownername);
            line3 = line3.replace("%extend%", this.getExtendTimeString());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());
            line3 = line3.replace("%status%", this.getTerminationString());

            String line4 = Messages.CONTRACT_SOLD_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%owner%", ownername);
            line4 = line4.replace("%extend%", this.getExtendTimeString());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());
            line4 = line4.replace("%status%", this.getTerminationString());

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
            line1 = line1.replace("%status%", this.getTerminationString());

            String line2 = Messages.CONTRACT_SIGN2.replace("%regionid%", this.getRegion().getId());
            line2 = line2.replace("%price%", this.price + "");
            line2 = line2.replace("%currency%", Messages.CURRENCY);
            line2 = line2.replace("%dimensions%", this.getDimensions());
            line2 = line2.replace("%extend%", this.getExtendTimeString());
            line2 = line2.replace("%remaining%", this.calcRemainingTime());
            line2 = line2.replace("%status%", this.getTerminationString());

            String line3 = Messages.CONTRACT_SIGN3.replace("%regionid%", this.getRegion().getId());
            line3 = line3.replace("%price%", this.price + "");
            line3 = line3.replace("%currency%", Messages.CURRENCY);
            line3 = line3.replace("%dimensions%", this.getDimensions());
            line3 = line3.replace("%extend%", this.getExtendTimeString());
            line3 = line3.replace("%remaining%", this.calcRemainingTime());
            line3 = line3.replace("%status%", this.getTerminationString());

            String line4 = Messages.CONTRACT_SIGN4.replace("%regionid%", this.getRegion().getId());
            line4 = line4.replace("%price%", this.price + "");
            line4 = line4.replace("%currency%", Messages.CURRENCY);
            line4 = line4.replace("%dimensions%", this.getDimensions());
            line4 = line4.replace("%extend%", this.getExtendTimeString());
            line4 = line4.replace("%remaining%", this.calcRemainingTime());
            line4 = line4.replace("%status%", this.getTerminationString());

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
            if(Main.getWorldGuardInterface().getOwners(getRegion()).contains(player.getUniqueId()) || player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                this.changeTerminated(player);
                return;
            } else {
                player.sendMessage(Messages.PREFIX + Messages.REGION_ALREADY_SOLD);
                return;
            }
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
        if(Main.isTeleportAfterContractRegionBought()){
            this.teleportToRegion(player);
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);

    }

    @Override
    public void userSell(Player player) {
        this.resetRegion(player);
    }

    @Override
    public double getPaybackMoney() {
        return 0;
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
        if((player != null) && Main.isSendContractRegionExtendMessage()) {
            String sendmessage = Messages.CONTRACT_REGION_EXTENDED;
            sendmessage = sendmessage.replace("%price%", this.price + "");
            sendmessage = sendmessage.replace("%currency%", Messages.CURRENCY);
            sendmessage = sendmessage.replace("%extend%", getExtendTimeString());
            sendmessage = sendmessage.replace("%regionid%", getRegion().getId());
            player.sendMessage(Messages.PREFIX + sendmessage);
        }
    }

    public void changeTerminated(){
        this.changeTerminated(null);
    }

    public void changeTerminated(Player player) {
        this.setTerminated(!this.terminated, player);
    }

    public void setTerminated(Boolean bool) {
        this.setTerminated(bool, null);
    }

    public void setTerminated(Boolean bool, Player player) {
        this.terminated = bool;
        YamlConfiguration config = Region.getRegionsConf();
        config.set("Regions." + this.regionworld + "." + this.region.getId() + ".terminated", terminated);
        Region.saveRegionsConf(config);
        if(player != null) {
            String sendmessage = Messages.CONTRACT_REGION_CHANGE_TERMINATED;
            sendmessage = sendmessage.replace("%regionid%", this.getRegion().getId());
            sendmessage = sendmessage.replace("%statuslong%", getTerminationStringLong());
            sendmessage = sendmessage.replace("%status%", getTerminationStringLong());
            player.sendMessage(Messages.PREFIX + sendmessage);
        }
    }

    public String getTerminationStringLong(){
        String retMessage;
        if(this.terminated) {
            retMessage = Messages.CONTRACT_REGION_STATUS_TERMINATED_LONG;
        } else {
            retMessage = Messages.CONTRACT_REGION_STATUS_ACTIVE_LONG;
        }
        retMessage = retMessage.replace("%remaining%", this.calcRemainingTime());
        retMessage = retMessage.replace("%regionid%", this.getRegion().getId());
        return retMessage;
    }

    public String getTerminationString(){
        if(this.terminated) {
            return Messages.CONTRACT_REGION_STATUS_TERMINATED;
        } else {
            return Messages.CONTRACT_REGION_STATUS_ACTIVE;
        }
    }

    public static boolean terminateCommand(CommandSender sender, String regionName, String setting){
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        Region region = Region.searchRegionbyNameAndWorld(regionName, player.getWorld().getName());
        if(region == null){
            player.sendMessage(Messages.PREFIX + Messages.REGION_DOES_NOT_EXIST);
            return true;
        }
        if(!(region instanceof ContractRegion)) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_IS_NOT_A_CONTRACT_REGION);
        }

        if(!region.sold) {
            player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_SOLD);
            return true;
        }

        if(!Main.getWorldGuardInterface().hasOwner(player, region.getRegion())) {
            if(!player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)){
                player.sendMessage(Messages.PREFIX + Messages.REGION_NOT_OWN);
                return true;
            }
        }

        ContractRegion contractRegion = (ContractRegion) region;
        contractRegion.setTerminated(Boolean.parseBoolean(setting), player);
        return true;
    }
}

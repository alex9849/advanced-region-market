package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.ArmSettings;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.BuyRegionEvent;
import net.alex9849.arm.events.ExtendRegionEvent;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.exceptions.InputException;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ContractRegion extends Region {
    private long payedTill;
    private long extendTime;
    private boolean terminated;

    public ContractRegion(WGRegion region, World regionworld, List<SignData> contractsign, ContractPrice contractPrice, Boolean sold, Boolean autoreset,
                          Boolean isHotel, Boolean doBlockReset, RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset, boolean isUserResettable,
                          long payedTill, Boolean terminated, List<Region> subregions, int allowedSubregions, EntityLimitGroup entityLimitGroup,
                          HashMap<EntityType, Integer> extraEntitys, int boughtExtraTotalEntitys) {
        super(region, regionworld, contractsign, contractPrice, sold, autoreset, isHotel, doBlockReset, regionKind, flagGroup, teleportLoc, lastreset, isUserResettable,
                subregions, allowedSubregions, entityLimitGroup, extraEntitys, boughtExtraTotalEntitys);
        this.payedTill = payedTill;
        this.extendTime = contractPrice.getExtendTime();
        this.terminated = terminated;

        this.updateSigns();
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if(sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_CONTRACTREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_CONTRACTREGION;
        }

        if(this.isSubregion()) {
            msg = Messages.REGION_INFO_CONTRACTREGION_SUBREGION;
        }

        for(String s : msg) {
            sender.sendMessage(this.getConvertedMessage(s));
        }
    }

    @Override
    public void updateRegion() {
        if(this.isSold()){
            GregorianCalendar actualtime = new GregorianCalendar();
            if((this.payedTill < actualtime.getTimeInMillis()) && this.terminated){
                this.automaticResetRegion();
            } else if(this.payedTill < actualtime.getTimeInMillis()) {
                List<UUID> owners = this.getRegion().getOwners();
                if(owners.size() == 0){
                    this.extend();
                    this.updateSigns();
                } else {
                    OfflinePlayer oplayer = Bukkit.getOfflinePlayer(owners.get(0));
                    if(oplayer == null) {
                        this.extend();
                        this.updateSigns();
                    } else {
                        if(AdvancedRegionMarket.getEcon().hasAccount(oplayer)) {
                            if(AdvancedRegionMarket.getEcon().getBalance(oplayer) < this.getPrice()) {
                                this.automaticResetRegion();
                            } else {
                                AdvancedRegionMarket.getEcon().withdrawPlayer(oplayer, this.getPrice());
                                if(this.isSubregion()) {
                                    this.giveParentRegionOwnerMoney(this.getPrice());
                                }
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
        } else {
            this.updateSigns();
        }
    }

    @Override
    public void setSold(OfflinePlayer player) {
        if(!this.sold) {
            GregorianCalendar actualtime = new GregorianCalendar();
            this.payedTill = actualtime.getTimeInMillis() + this.extendTime;
        }
        this.sold = true;
        this.terminated = false;
        this.getRegion().deleteMembers();
        this.getRegion().setOwner(player);
        this.updateSigns();
        this.flagGroup.applyToRegion(this, FlagGroup.ResetMode.COMPLETE);
        this.queueSave();
    }

    @Override
    public void unsell() {
        super.unsell();
        GregorianCalendar actualtime = new GregorianCalendar();
        if(this.getPayedTill() > actualtime.getTimeInMillis()){
            this.setPayedTill(actualtime.getTimeInMillis());
        }
        this.queueSave();
    }

    @Override
    protected void updateSignText(SignData signData) {
        if(this.sold){
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.CONTRACT_SOLD_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.CONTRACT_SOLD_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.CONTRACT_SOLD_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.CONTRACT_SOLD_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.CONTRACT_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.CONTRACT_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.CONTRACT_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.CONTRACT_SIGN4);
            signData.writeLines(lines);
        }
    }

    @Override
    public void buy(Player player) throws InputException {

        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(this.sold) {
            if(this.getRegion().hasOwner(player.getUniqueId()) || player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                this.changeTerminated(player);
                return;
            } else {
                throw new InputException(player, Messages.REGION_ALREADY_SOLD);
            }
        }
        if(!RegionKind.hasPermission(player, this.regionKind)){
            throw new InputException(player, this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
        }

        if(!LimitGroup.isCanBuyAnother(player, this)) {
            throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, this.regionKind));
        }
        if(AdvancedRegionMarket.getEcon().getBalance(player) < this.getPrice()) {
            throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
        }
        BuyRegionEvent buyRegionEvent = new BuyRegionEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(buyRegionEvent);
        if(buyRegionEvent.isCancelled()) {
            return;
        }
        AdvancedRegionMarket.getEcon().withdrawPlayer(player, this.getPrice());
        if(this.isSubregion()) {
            this.giveParentRegionOwnerMoney(this.getPrice());
        }
        this.setSold(player);
        this.resetBuiltBlocks();
        if(ArmSettings.isTeleportAfterContractRegionBought()){
            Teleporter.teleport(player, this, "", AdvancedRegionMarket.getARM().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);

    }

    @Override
    public void userSell(Player player){
        List<UUID> defdomain = this.getRegion().getOwners();
        double amount = this.getPaybackMoney();

        if(amount > 0){
            for(int i = 0; i < defdomain.size(); i++) {
                AdvancedRegionMarket.getEcon().depositPlayer(Bukkit.getOfflinePlayer(defdomain.get(i)), amount);
            }
        }

        this.automaticResetRegion(player);
    }

    @Override
    public double getPaybackMoney() {
        double amount = (this.getPrice() * this.getRegionKind().getPaybackPercentage())/100;
        GregorianCalendar acttime = new GregorianCalendar();
        long remaining = this.payedTill - acttime.getTimeInMillis();
        amount = amount * ((double)remaining / (double)extendTime);
        amount = amount * 10;
        amount = Math.round(amount);
        amount = amount / 10d;

        if(amount > 0) {
            return amount;
        } else {
            return 0;
        }
    }

    public String calcRemainingTime() {
        String timetoString = ArmSettings.getRemainingTimeTimeformat();
        timetoString = timetoString.replace("%countdown%", this.getCountdown(ArmSettings.isUseShortCountdown()));
        timetoString = timetoString.replace("%date%", this.getDate(ArmSettings.getDateTimeformat()));


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
            return Messages.REGION_INFO_EXPIRED;
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
        ExtendRegionEvent extendRegionEvent = new ExtendRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(extendRegionEvent);
        if(extendRegionEvent.isCancelled()) {
            return;
        }

        GregorianCalendar actualtime = new GregorianCalendar();
        while (this.payedTill < actualtime.getTimeInMillis()) {
            this.payedTill = this.payedTill + this.extendTime;
        }
        this.queueSave();
        if((player != null) && ArmSettings.isSendContractRegionExtendMessage()) {
            String sendmessage = this.getConvertedMessage(Messages.CONTRACT_REGION_EXTENDED);
            player.sendMessage(Messages.PREFIX + sendmessage);
        }
    }

    public void changeTerminated() throws InputException {
        this.changeTerminated(null);
    }

    public void changeTerminated(Player player) throws InputException {
        if(this.isTerminated()) {
            if(!LimitGroup.isInLimit(player, this)) {
                throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, this.getRegionKind()));
            } else {
                this.setTerminated(false, player);
            }
        } else {
            this.setTerminated(true, player);
        }
    }

    public void setTerminated(Boolean bool) {
        this.setTerminated(bool, null);
    }

    public void setTerminated(Boolean bool, Player player) {
        this.terminated = bool;
        this.queueSave();
        if(player != null) {
            String sendmessage = this.getConvertedMessage(Messages.CONTRACT_REGION_CHANGE_TERMINATED);
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
        return retMessage;
    }

    public String getTerminationString(){
        if(this.terminated) {
            return Messages.CONTRACT_REGION_STATUS_TERMINATED;
        } else {
            return Messages.CONTRACT_REGION_STATUS_ACTIVE;
        }
    }

    protected long getPayedTill() {
        return this.payedTill;
    }

    protected long getExtendTime() {
        return this.extendTime;
    }

    protected boolean isTerminated() {
        return this.terminated;
    }

    public double getPricePerM2PerWeek() {
        if(this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM2();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return  (msPerWeek / this.getExtendTime()) * pricePerM2;
    }

    public double getPricePerM3PerWeek() {
        if(this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM3();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return  (msPerWeek / this.getExtendTime()) * pricePerM2;
    }

    @Override
    public String getConvertedMessage(String message) {
        message = message.replace("%status%", this.getTerminationString());
        message = message.replace("%statuslong%", this.getTerminationStringLong());
        message = super.getConvertedMessage(message);
        message = message.replace("%extend%", this.getExtendTimeString());
        message = message.replace("%remaining%", this.calcRemainingTime());
        message = message.replace("%isterminated%", Messages.convertYesNo(this.isTerminated()));
        message = message.replace("%priceperm2perweek%", Price.formatPrice(this.getPricePerM2PerWeek()));
        message = message.replace("%priceperm3perweek%", Price.formatPrice(this.getPricePerM3PerWeek()));
        return message;
    }

    public SellType getSellType() {
        return SellType.CONTRACT;
    }

    @Override
    public void setPrice(Price price) {
        this.price = price;
        if(price instanceof ContractPrice) {
            this.extendTime = ((ContractPrice) price).getExtendTime();
        }
        this.updateSigns();
        this.queueSave();
    }
    public void setPayedTill(long payedTill) {
        this.payedTill = payedTill;
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection yamlConfiguration = super.toConfigurationSection();
        if(this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("extendTime", null);
        } else {
            yamlConfiguration.set("extendTime", this.getExtendTime());
        }
        yamlConfiguration.set("payedTill", this.getPayedTill());
        yamlConfiguration.set("terminated", this.isTerminated());
        return yamlConfiguration;
    }
}

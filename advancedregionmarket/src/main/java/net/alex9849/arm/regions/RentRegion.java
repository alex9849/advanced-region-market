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
import net.alex9849.arm.regions.price.RentPrice;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RentRegion extends Region {
    private long payedTill;
    private long maxRentTime;
    private long rentExtendPerClick;
    private static long expirationWarningTime;
    private static Boolean sendExpirationWarning;

    public RentRegion(WGRegion region, World regionworld, List<SignData> rentsign, RentPrice rentPrice, Boolean sold, Boolean autoreset, Boolean allowOnlyNewBlocks,
                      Boolean doBlockReset, RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset, boolean isUserResettable, long payedTill,
                      List<Region> subregions, int allowedSubregions, EntityLimitGroup entityLimitGroup, HashMap<EntityType, Integer> extraEntitys,
                      int boughtExtraTotalEntitys) {
        super(region, regionworld, rentsign, rentPrice, sold, autoreset, allowOnlyNewBlocks, doBlockReset, regionKind, flagGroup, teleportLoc, lastreset, isUserResettable,
                subregions, allowedSubregions, entityLimitGroup, extraEntitys, boughtExtraTotalEntitys);

        this.payedTill = payedTill;
        this.maxRentTime = rentPrice.getMaxRentTime();
        this.rentExtendPerClick = rentPrice.getExtendTime();
        this.updateSigns();
    }

    @Override
    protected void updateSignText(SignData signData){

        if(this.sold){
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.RENTED_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.RENTED_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.RENTED_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.RENTED_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.RENT_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.RENT_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.RENT_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.RENT_SIGN4);
            signData.writeLines(lines);
        }
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
    public void buy(Player player) throws InputException {

        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!RegionKind.hasPermission(player, regionKind)){
                throw new InputException(player, this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
            }
        }

        if(this.sold) {
            this.extendRegion(player);
            return;
        }

        if(!LimitGroup.isCanBuyAnother(player, this)){
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
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.setSold(player);
        this.resetBuiltBlocks();
        if(ArmSettings.isTeleportAfterRentRegionBought()){
            Teleporter.teleport(player, this, "", AdvancedRegionMarket.getARM().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if(sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_RENTREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_RENTREGION;
        }

        if(this.isSubregion()) {
            msg = Messages.REGION_INFO_RENTREGION_SUBREGION;
        }

        for(String s : msg) {
            sender.sendMessage(this.getConvertedMessage(s));
        }
    }

    @Override
    public void updateRegion() {
        if(this.isSold()){
            GregorianCalendar actualtime = new GregorianCalendar();
            if(this.payedTill < actualtime.getTimeInMillis()){
                this.automaticResetRegion();
            } else {
                this.updateSigns();
            }
        } else {
            this.updateSigns();
        }
    }

    @Override
    public void setSold(OfflinePlayer player){
        if(!this.sold) {
            GregorianCalendar actualtime = new GregorianCalendar();
            this.payedTill = actualtime.getTimeInMillis() + this.rentExtendPerClick;
        }
        this.sold = true;
        this.getRegion().deleteMembers();
        this.getRegion().setOwner(player);

        this.updateSigns();
        this.flagGroup.applyToRegion(this, FlagGroup.ResetMode.COMPLETE);
        this.queueSave();

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
        amount = amount * ((double)remaining / (double)rentExtendPerClick);
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
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(this.payedTill);

        if ((payedTill.getTimeInMillis() - actualtime.getTimeInMillis()) < 0) {
            return Messages.REGION_INFO_EXPIRED;
        }

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

    public String getMaxRentTimeString(){
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

    public void extendRegion(Player player) throws InputException {
        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if (this.regionKind != RegionKind.DEFAULT){
            if(!RegionKind.hasPermission(player, regionKind)){
                throw new InputException(player, this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
            }
        }

        if(!this.sold) {
            throw new InputException(player, Messages.REGION_NOT_SOLD);
        }

        if(!this.getRegion().hasOwner(player.getUniqueId())) {
            if(!player.hasPermission(Permission.ADMIN_EXTEND)){
                throw new InputException(player, Messages.REGION_NOT_OWN);
            }
        }

        if(!LimitGroup.isInLimit(player, this)) {
            throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, this.getRegionKind()));
        }

        GregorianCalendar actualtime = new GregorianCalendar();
        if (this.maxRentTime < ((this.payedTill + this.rentExtendPerClick) - actualtime.getTimeInMillis())){
            String errormessage = this.getConvertedMessage(Messages.RENT_EXTEND_ERROR);
            throw new InputException(player, errormessage);
        }
        ExtendRegionEvent extendRegionEvent = new ExtendRegionEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(extendRegionEvent);
        if(extendRegionEvent.isCancelled()) {
            return;
        }

        if(AdvancedRegionMarket.getEcon().getBalance(player) < this.getPrice()) {
            throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
        }
        AdvancedRegionMarket.getEcon().withdrawPlayer(player, this.getPrice());
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.payedTill = this.payedTill + this.rentExtendPerClick;

        this.queueSave();

        String message = this.getConvertedMessage(Messages.RENT_EXTEND_MESSAGE);
        player.sendMessage(Messages.PREFIX + message);

        this.updateSigns();

        if(ArmSettings.isTeleportAfterRentRegionExtend()) {
            Teleporter.teleport(player, this);
        }

        return;
    }

    public static void sendExpirationWarnings(Player player) {
        List<Region> regions = AdvancedRegionMarket.getRegionManager().getRegionsByOwner(player.getUniqueId());
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

    public double getPricePerM2PerWeek() {
        if(this.getRentExtendPerClick() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM2();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return (msPerWeek / this.getRentExtendPerClick()) * pricePerM2;
    }

    public double getPricePerM3PerWeek() {
        if(this.getRentExtendPerClick() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM3();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return (msPerWeek / this.getRentExtendPerClick()) * pricePerM2;
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

    public void setPrice(Price price) {
        this.price = price;

        if(price instanceof ContractPrice) {
            this.rentExtendPerClick = ((ContractPrice) price).getExtendTime();
        }

        if(price instanceof RentPrice) {
            this.maxRentTime = ((RentPrice) price).getMaxRentTime();
        }
        this.updateSigns();
        this.queueSave();
    }

    @Override
    public String getConvertedMessage(String message) {
        message = super.getConvertedMessage(message);
        message = message.replace("%maxrenttime%", this.getMaxRentTimeString());
        message = message.replace("%remaining%", this.calcRemainingTime());
        message = message.replace("%extendperclick%", this.getExtendPerClick());
        message = message.replace("%priceperm2perweek%", Price.formatPrice(this.getPricePerM2PerWeek()));
        message = message.replace("%priceperm3perweek%", Price.formatPrice(this.getPricePerM3PerWeek()));
        return message;
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }

    public SellType getSellType() {
        return SellType.RENT;
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection yamlConfiguration = super.toConfigurationSection();
        yamlConfiguration.set("payedTill", this.getPayedTill());
        if(this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("maxRentTime", null);
            yamlConfiguration.set("rentExtendPerClick", null);
        } else {
            yamlConfiguration.set("maxRentTime", this.getMaxRentTime());
            yamlConfiguration.set("rentExtendPerClick", this.getRentExtendPerClick());
        }
        return yamlConfiguration;
    }
}

package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.BuyRegionEvent;
import net.alex9849.arm.events.ExtendRegionEvent;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.exceptions.InputException;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class RentRegion extends CountdownRegion {
    private long maxRentTime;
    private static long expirationWarningTime;
    private static Boolean sendExpirationWarning;

    public RentRegion(WGRegion region, World regionworld, List<SignData> rentsign, RentPrice rentPrice, Boolean sold, Boolean inactivityReset, Boolean allowOnlyNewBlocks,
                      Boolean doBlockReset, RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset, long lastLogin, boolean isUserResettable, long payedTill,
                      List<Region> subregions, int allowedSubregions, EntityLimitGroup entityLimitGroup, HashMap<EntityLimit.LimitableEntityType, Integer> extraEntitys,
                      int boughtExtraTotalEntitys) {
        super(region, regionworld, rentsign, rentPrice, sold, inactivityReset, allowOnlyNewBlocks, doBlockReset, regionKind, flagGroup, teleportLoc, lastreset, lastLogin, isUserResettable,
                payedTill, subregions, allowedSubregions, entityLimitGroup, extraEntitys, boughtExtraTotalEntitys);
        this.maxRentTime = rentPrice.getMaxRentTime();
        this.updateSigns();
    }

    @Override
    protected void updateSignText(SignData signData){

        if(this.isSold()){
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
    public void buy(Player player) throws InputException {

        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if (this.getRegionKind() != RegionKind.DEFAULT){
            if(!RegionKind.hasPermission(player, this.getRegionKind())){
                throw new InputException(player, this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
            }
        }

        if(this.isSold()) {
            this.extendRegion(player);
            return;
        }

        if(!LimitGroup.isCanBuyAnother(player, this)){
            throw new InputException(player, LimitGroup.getRegionBuyOutOfLimitMessage(player, this.getRegionKind()));
        }
        if(AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPrice()) {
            throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
        }
        BuyRegionEvent buyRegionEvent = new BuyRegionEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(buyRegionEvent);
        if(buyRegionEvent.isCancelled()) {
            return;
        }

        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPrice());
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.setSold(player);
        this.resetBuiltBlocks();
        if(AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionBought()){
            Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
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
            if(this.getPayedTill() < actualtime.getTimeInMillis()){
                this.automaticResetRegion();
            }
        }
        super.updateRegion();
    }

    public void extendRegion(Player player) throws InputException {
        if(!Permission.hasAnyBuyPermission(player)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if (this.getRegionKind() != RegionKind.DEFAULT){
            if(!RegionKind.hasPermission(player, this.getRegionKind())){
                throw new InputException(player, this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
            }
        }

        if(!this.isSold()) {
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

        if(AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPrice()) {
            throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
        }
        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPrice());
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.payedTill = this.payedTill + this.rentExtendPerClick;

        this.queueSave();

        String message = this.getConvertedMessage(Messages.RENT_EXTEND_MESSAGE);
        player.sendMessage(Messages.PREFIX + message);

        this.updateSigns();

        if(AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionExtend()) {
            Teleporter.teleport(player, this);
        }

        return;
    }

    public static void sendExpirationWarnings(Player player) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId());
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

    public void setPrice(Price price) {
        super.setPrice(price);

        if(price instanceof RentPrice) {
            this.maxRentTime = ((RentPrice) price).getMaxRentTime();
        }
        this.updateSigns();
        this.queueSave();
    }

    public long getMaxRentTimeString() {
        return this.maxRentTime;
    }

    @Override
    public String getConvertedMessage(String message) {
        message = super.getConvertedMessage(message);
        message = message.replace("%maxrenttime%", CountdownRegion.timeInMsToString(this.getMaxRentTimeString()));
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
        if(this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("maxRentTime", null);
        } else {
            yamlConfiguration.set("maxRentTime", this.getMaxRentTime());
        }
        return yamlConfiguration;
    }
}

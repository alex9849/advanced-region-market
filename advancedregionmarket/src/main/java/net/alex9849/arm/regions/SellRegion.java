package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.events.BuyRegionEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class SellRegion extends Region {


    public SellRegion(WGRegion region, World regionworld, List<SignData> sellsign, Price price, Boolean sold, Boolean inactivityReset, Boolean allowOnlyNewBlocks,
                      Boolean doBlockReset, RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset, long lastLogin, boolean isUserRestorable, List<Region> subregions,
                      int allowedSubregions, EntityLimitGroup entityLimitGroup, HashMap<EntityLimit.LimitableEntityType, Integer> extraEntitys, int boughtExtraTotalEntitys, int maxMembers,
                      int paybackPercentage) {
        super(region, regionworld, sellsign, price, sold, inactivityReset, allowOnlyNewBlocks, doBlockReset, regionKind, flagGroup, teleportLoc, lastreset, lastLogin, isUserRestorable,
                subregions, allowedSubregions, entityLimitGroup, extraEntitys, boughtExtraTotalEntitys, maxMembers, paybackPercentage);
    }

    @Override
    protected void updateSignText(SignData signData) {
        if (this.isSold()) {
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.SOLD_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.SOLD_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.SOLD_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.SOLD_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.getConvertedMessage(Messages.SELL_SIGN1);
            lines[1] = this.getConvertedMessage(Messages.SELL_SIGN2);
            lines[2] = this.getConvertedMessage(Messages.SELL_SIGN3);
            lines[3] = this.getConvertedMessage(Messages.SELL_SIGN4);
            signData.writeLines(lines);
        }

    }

    @Override
    public void buy(Player player) throws NoPermissionException, OutOfLimitExeption, NotEnoughMoneyException, AlreadySoldException {

        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(this.getConvertedMessage(Messages.NO_PERMISSION));
        }
        if (this.isSold()) {
            throw new AlreadySoldException(this.getConvertedMessage(Messages.REGION_ALREADY_SOLD));
        }
        if (!RegionKind.hasPermission(player, this.getRegionKind())) {
            throw new NoPermissionException(this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
        }

        if (!LimitGroup.isCanBuyAnother(player, this)) {
            throw new OutOfLimitExeption(LimitGroup.getRegionBuyOutOfLimitMessage(player, this.getRegionKind()));
        }

        if (AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPrice()) {
            throw new NotEnoughMoneyException(this.getConvertedMessage(Messages.NOT_ENOUGHT_MONEY));
        }
        BuyRegionEvent buyRegionEvent = new BuyRegionEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(buyRegionEvent);
        if (buyRegionEvent.isCancelled()) {
            return;
        }

        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPrice());
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.setSold(player);
        if (AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterSellRegionBought()) {
            try {
                Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
            } catch (NoSaveLocationException e) {
                player.sendMessage(Messages.PREFIX + this.getConvertedMessage(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND));
            }
        }
        player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);
    }

    @Override
    public void setSold(OfflinePlayer player) {
        this.setSold(true);
        this.getRegion().deleteMembers();
        this.getRegion().setOwner(player);
        this.setLastLogin();

        this.updateSigns();
        this.queueSave();
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if (sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_SELLREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_SELLREGION;
        }

        if (this.isSubregion()) {
            msg = Messages.REGION_INFO_SELLREGION_SUBREGION;
        }

        for (String s : msg) {
            sender.sendMessage(this.getConvertedMessage(s));
        }
    }

    @Override
    public double getPaybackMoney() {
        double money = (this.getPrice() * this.getPaybackPercentage()) / 100;
        if (money > 0) {
            return money;
        } else {
            return 0;
        }
    }

    @Override
    public double getPricePerM2PerWeek() {
        return this.getPricePerM2();
    }

    public void setPrice(Price price) {
        super.setPrice(price);
        this.updateSigns();
        this.queueSave();
    }

    @Override
    public double getPricePerM3PerWeek() {
        return this.getPricePerM2();
    }

    public SellType getSellType() {
        return SellType.SELL;
    }
}

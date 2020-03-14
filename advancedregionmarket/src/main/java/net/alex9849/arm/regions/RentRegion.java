package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.events.BuyRegionEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.limitgroups.LimitGroup;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class RentRegion extends CountdownRegion {
    private static long expirationWarningTime;
    private static Boolean sendExpirationWarning;
    private long maxRentTime;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%maxrenttime-short%", () -> {
            return TimeUtil.timeInMsToString(this.getMaxRentTime(), false, false);
        });
        variableReplacements.put("%maxrenttime-writtenout%", () -> {
            return TimeUtil.timeInMsToString(this.getMaxRentTime(), true, false);
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 50);
    }

    public RentRegion(WGRegion region, List<SignData> sellsigns, RentPrice rentPrice, boolean sold, Region parentRegion) {
        super(region, sellsigns, rentPrice, sold, parentRegion);
        this.maxRentTime = rentPrice.getMaxRentTime();
    }

    public RentRegion(WGRegion region, World regionworld, List<SignData> sellsigns, RentPrice rentPrice, boolean sold) {
        super(region, regionworld, sellsigns, rentPrice, sold);
        this.maxRentTime = rentPrice.getMaxRentTime();
    }

    public static void sendExpirationWarnings(Player player) {
        List<Region> regions = AdvancedRegionMarket.getInstance().getRegionManager().getRegionsByOwner(player.getUniqueId());
        List<RentRegion> rentRegions = new ArrayList<>();
        for (int i = 0; i < regions.size(); i++) {
            if (regions.get(i) instanceof RentRegion) {
                RentRegion rentRegion = (RentRegion) regions.get(i);
                if ((rentRegion.getPayedTill() - (new GregorianCalendar().getTimeInMillis())) <= RentRegion.expirationWarningTime) {
                    rentRegions.add(rentRegion);
                }
            }
        }
        String regionWarnings = "";
        for (int i = 0; i < rentRegions.size() - 1; i++) {
            regionWarnings = regionWarnings + rentRegions.get(i).getRegion().getId() + ", ";
        }
        if (rentRegions.size() > 0) {
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

    public static Boolean isSendExpirationWarning() {
        return RentRegion.sendExpirationWarning;
    }

    @Override
    protected void updateSignText(SignData signData) {

        if (this.isSold()) {
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
    public void buy(Player player) throws NoPermissionException, AlreadySoldException, OutOfLimitExeption, NotEnoughMoneyException, MaxRentTimeExceededException {

        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if (this.isSold() && !this.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_EXTEND)) {
            throw new AlreadySoldException(Messages.REGION_ALREADY_SOLD);
        }

        if (!RegionKind.hasPermission(player, this.getRegionKind())) {
            throw new NoPermissionException(this.getConvertedMessage(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
        }

        if (!this.isSold() && !LimitGroup.isCanBuyAnother(player, this)) {
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

        if (this.isSold()) {
            this.extendNoteMaxRentTime();
            if (AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionExtend()) {
                try {
                    Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
                } catch (NoSaveLocationException e) {
                    if (e.hasMessage()) {
                        player.sendMessage(Messages.PREFIX + e.getMessage());
                    }
                }
            }

            player.sendMessage(Messages.PREFIX + this.getConvertedMessage(Messages.RENT_EXTEND_MESSAGE));
        } else {
            this.setSold(player);
            if (AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionBought()) {
                try {
                    Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
                } catch (NoSaveLocationException e) {
                    if (e.hasMessage()) {
                        player.sendMessage(Messages.PREFIX + e.getMessage());
                    }
                }
            }
            player.sendMessage(Messages.PREFIX + Messages.REGION_BUYMESSAGE);
        }

        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPrice());
        this.giveParentRegionOwnerMoney(this.getPrice());
        this.queueSave();
    }

    @Override
    public void regionInfo(CommandSender sender) {
        super.regionInfo(sender);
        List<String> msg;

        if (sender.hasPermission(Permission.ADMIN_INFO)) {
            msg = Messages.REGION_INFO_RENTREGION_ADMIN;
        } else {
            msg = Messages.REGION_INFO_RENTREGION;
        }

        if (this.isSubregion()) {
            msg = Messages.REGION_INFO_RENTREGION_SUBREGION;
        }

        for (String s : msg) {
            sender.sendMessage(this.getConvertedMessage(s));
        }
    }

    @Override
    public void updateRegion() {
        if (this.isSold()) {
            GregorianCalendar actualtime = new GregorianCalendar();
            if (this.getPayedTill() < actualtime.getTimeInMillis()) {
                try {
                    this.automaticResetRegion(ActionReason.EXPIRED, true);
                } catch (SchematicNotFoundException e) {
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, this.getConvertedMessage(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG)); }
            }
        }
        super.updateRegion();
    }

    public void extendNoteMaxRentTime() throws MaxRentTimeExceededException {
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if ((this.getPayedTill() + this.getExtendTime()) - actualTime > this.getMaxRentTime()) {
            throw new MaxRentTimeExceededException(this.getConvertedMessage(Messages.RENT_EXTEND_MAX_RENT_TIME_EXCEEDED));
        }
        this.extend();
    }

    public void setPrice(Price price) {
        super.setPrice(price);

        if (price instanceof RentPrice) {
            this.maxRentTime = ((RentPrice) price).getMaxRentTime();
        }
        this.updateSigns();
        this.queueSave();
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }


    public String getConvertedMessage(String message) {
        message = super.getConvertedMessage(message);
        return this.stringReplacer.replace(message).toString();
    }

    public SellType getSellType() {
        return SellType.RENT;
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection yamlConfiguration = super.toConfigurationSection();
        if (this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("maxRentTime", null);
        } else {
            yamlConfiguration.set("maxRentTime", this.getMaxRentTime());
        }
        return yamlConfiguration;
    }
}

package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.events.BuyRegionEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
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

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class RentRegion extends CountdownRegion {
    private RentPrice rentPrice;
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
        super(region, sellsigns, sold, parentRegion);
        this.rentPrice = rentPrice;
    }

    public RentRegion(WGRegion region, World regionworld, List<SignData> sellsigns, RentPrice rentPrice, boolean sold) {
        super(region, regionworld, sellsigns, sold);
        this.rentPrice = rentPrice;
    }

    @Override
    public Price getPriceObject() {
        return this.rentPrice;
    }

    public void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotSoldException, NoPermissionException, NotEnoughMoneyException, RegionNotOwnException, MaxRentTimeExceededException {
        if(this.isSold()) {
            this.extendNoteMaxRentTime(player);
        } else {
            this.buy(player);
        }
    }

    public void extendNoteMaxRentTime(Player player) throws MaxRentTimeExceededException, NotEnoughMoneyException, RegionNotOwnException, NotSoldException, NoPermissionException {
        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if(!this.isSold()) {
            throw new NotSoldException(Messages.REGION_NOT_SOLD);
        }

        if(!this.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_EXTEND)) {
            throw new RegionNotOwnException(Messages.REGION_NOT_OWN);
        }

        if (AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPricePerPeriod()) {
            throw new NotEnoughMoneyException(this.replaceVariables(Messages.NOT_ENOUGH_MONEY));
        }

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
        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPricePerPeriod());
        this.giveParentRegionOwnerMoney(this.getPricePerPeriod());

        player.sendMessage(Messages.PREFIX + this.replaceVariables(Messages.RENT_EXTEND_MESSAGE));
    }

    @Override
    public void buy(Player player) throws NoPermissionException, AlreadySoldException, OutOfLimitExeption, NotEnoughMoneyException, MaxRentTimeExceededException {

        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if (this.isSold()) {
            throw new AlreadySoldException(Messages.REGION_ALREADY_SOLD);
        }

        if (!RegionKind.hasPermission(player, this.getRegionKind())) {
            throw new NoPermissionException(this.replaceVariables(Messages.NO_PERMISSIONS_TO_BUY_THIS_KIND_OF_REGION));
        }

        if (!AdvancedRegionMarket.getInstance().getLimitGroupManager().isCanBuyAnother(player, this)) {
            throw new OutOfLimitExeption(AdvancedRegionMarket.getInstance().getLimitGroupManager()
                    .getRegionBuyOutOfLimitMessage(player, this.getRegionKind()));
        }

        if (AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getPricePerPeriod()) {
            throw new NotEnoughMoneyException(this.replaceVariables(Messages.NOT_ENOUGH_MONEY));
        }

        BuyRegionEvent buyRegionEvent = new BuyRegionEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(buyRegionEvent);
        if (buyRegionEvent.isCancelled()) {
            return;
        }

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

        AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, this.getPricePerPeriod());
        this.giveParentRegionOwnerMoney(this.getPricePerPeriod());
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
            sender.sendMessage(this.replaceVariables(s));
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
                    AdvancedRegionMarket.getInstance().getLogger().log(Level.WARNING, this.replaceVariables(Messages.COULD_NOT_FIND_OR_LOAD_SCHEMATIC_LOG)); }
            }
        }
        super.updateRegion();
    }

    public void extendNoteMaxRentTime() throws MaxRentTimeExceededException {
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if ((this.getPayedTill() + this.getExtendTime()) - actualTime > this.getMaxRentTime()) {
            throw new MaxRentTimeExceededException(this.replaceVariables(Messages.RENT_EXTEND_MAX_RENT_TIME_EXCEEDED));
        }
        this.extend();
    }

    public void setRentPrice(RentPrice rentPrice) {
        this.rentPrice = rentPrice;
        this.queueSave();
        this.updateSigns();
    }

    public long getMaxRentTime() {
        return this.rentPrice.getMaxRentTime();
    }

    @Override
    public long getExtendTime() {
        return this.rentPrice.getExtendTime();
    }

    public String replaceVariables(String message) {
        message = super.replaceVariables(message);
        return this.stringReplacer.replace(message).toString();
    }

    public SellType getSellType() {
        return SellType.RENT;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        this.rentPrice = new RentPrice(autoPrice);
    }

    @Override
    protected void updateSignText(SignData signData) {

        if (this.isSold()) {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.RENTED_SIGN1);
            lines[1] = this.replaceVariables(Messages.RENTED_SIGN2);
            lines[2] = this.replaceVariables(Messages.RENTED_SIGN3);
            lines[3] = this.replaceVariables(Messages.RENTED_SIGN4);
            signData.writeLines(lines);
        } else {
            String[] lines = new String[4];
            lines[0] = this.replaceVariables(Messages.RENT_SIGN1);
            lines[1] = this.replaceVariables(Messages.RENT_SIGN2);
            lines[2] = this.replaceVariables(Messages.RENT_SIGN3);
            lines[3] = this.replaceVariables(Messages.RENT_SIGN4);
            signData.writeLines(lines);
        }
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

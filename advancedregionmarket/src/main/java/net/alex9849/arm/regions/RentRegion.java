package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.events.PreExtendEvent;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
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
import java.util.function.Supplier;
import java.util.logging.Level;

public class RentRegion extends CountdownRegion {
    private RentPrice rentPrice;

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

    public void signClickAction(Player player) throws OutOfLimitExeption, AlreadySoldException, NotSoldException, NoPermissionException, NotEnoughMoneyException, RegionNotOwnException, ProtectionOfContinuanceException {
        if (this.isSold()) {
            this.extend(player);
        } else {
            this.buy(player);
        }
    }

    /**
     * The amount of time the region would be extended if a player would try to extend it now.
     * @return A number larger or equal to 0 and smaller or equal to the ExtendTime
     */
    public long getCurrentExtendTime() {
        long actualTime = new GregorianCalendar().getTimeInMillis();
        long remainingTime = this.getPayedTill() - actualTime;
        //Calculate the extend time. The minimal value will not exceed the MaxExtendTime
        return Math.min(this.getExtendTime(), Math.max(0, this.getMaxExtendTime() - remainingTime));
    }

    public double getCurrentExtendPrice() {
        return this.getPricePerPeriod() * this.getCurrentExtendTime() / this.getExtendTime();
    }

    public void extend() {
        this.extend(getCurrentExtendTime());
    }

    public void extend(Player player) throws NotEnoughMoneyException, RegionNotOwnException, NotSoldException, NoPermissionException {
        if (!player.hasPermission(Permission.MEMBER_BUY)) {
            throw new NoPermissionException(Messages.NO_PERMISSION);
        }

        if(!this.isSold()) {
            throw new NotSoldException(Messages.REGION_NOT_SOLD);
        }

        if(!this.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_EXTEND)) {
            throw new RegionNotOwnException(Messages.REGION_NOT_OWN);
        }

        PreExtendEvent preExtendEvent = new PreExtendEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(preExtendEvent);
        if(preExtendEvent.isCancelled()) {
            return;
        }
        boolean isNoMoneyTransfer = preExtendEvent.isNoMoneyTransfer();

        if (!isNoMoneyTransfer && AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < this.getCurrentExtendPrice()) {
            throw new NotEnoughMoneyException(this.replaceVariables(Messages.NOT_ENOUGH_MONEY));
        }

        // The local stringreplacer only replaces stuff like the current extend
        // time and the current price, but not the remaining time, that
        // we want to replace after the extension. So we first replace
        // with the local replacer and after successful extension we
        // replace with the normal replace method.
        double extensionCost = this.getCurrentExtendPrice();
        String successMessage = Messages.PREFIX + this.replaceVariables(Messages.RENT_EXTEND_MESSAGE);
        this.extend();
        successMessage = this.replaceVariables(successMessage);
        if (AdvancedRegionMarket.getInstance().getPluginSettings().isTeleportAfterRentRegionExtend()) {
            try {
                Teleporter.teleport(player, this, "", AdvancedRegionMarket.getInstance().getConfig().getBoolean("Other.TeleportAfterRegionBoughtCountdown"));
            } catch (NoSaveLocationException e) {
                if (e.hasMessage()) {
                    player.sendMessage(Messages.PREFIX + e.getMessage());
                }
            }
        }
        if(!isNoMoneyTransfer) {
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, extensionCost);
            this.giveLandlordMoney(extensionCost);
        }
        player.sendMessage(successMessage);
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

    public void setRentPrice(RentPrice rentPrice) {
        this.rentPrice = rentPrice;
        this.queueSave();
        this.updateSigns();
    }

    public long getMaxExtendTime() {
        return this.rentPrice.getMaxExtendTime();
    }

    @Override
    public long getExtendTime() {
        return this.rentPrice.getExtendTime();
    }

    public SellType getSellType() {
        return SellType.RENT;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        this.setRentPrice(new RentPrice(autoPrice));
    }

    @Override
    protected void updateSignText(SignData signData) {
        String[] lines = new String[4];

        if (this.isSold()) {
            lines[0] = this.replaceVariables(Messages.RENTED_SIGN1);
            lines[1] = this.replaceVariables(Messages.RENTED_SIGN2);
            lines[2] = this.replaceVariables(Messages.RENTED_SIGN3);
            lines[3] = this.replaceVariables(Messages.RENTED_SIGN4);

        } else if(this.isProtectionOfContinuance()) {
            lines[0] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN1);
            lines[1] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN2);
            lines[2] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN3);
            lines[3] = this.replaceVariables(Messages.PROTECTION_OF_CONTINUANCE_SIGN4);

        } else {
            lines[0] = this.replaceVariables(Messages.RENT_SIGN1);
            lines[1] = this.replaceVariables(Messages.RENT_SIGN2);
            lines[2] = this.replaceVariables(Messages.RENT_SIGN3);
            lines[3] = this.replaceVariables(Messages.RENT_SIGN4);
        }
        signData.writeLines(lines);
    }

    protected HashMap<String, Supplier<String>> getVariableReplacements() {
        HashMap<String, Supplier<String>> variableReplacements = super.getVariableReplacements();
        variableReplacements.put("%maxextendtime-short%", () -> {
            return TimeUtil.timeInMsToString(this.getMaxExtendTime(), false, false);
        });
        variableReplacements.put("%maxextendtime-writtenout%", () -> {
            return TimeUtil.timeInMsToString(this.getMaxExtendTime(), true, false);
        });
        variableReplacements.put("%extendtime-current-writtenout%", () -> {
            return TimeUtil.timeInMsToString(this.getCurrentExtendTime(), true, false);
        });
        variableReplacements.put("%extendtime-current-short%", () -> {
            return TimeUtil.timeInMsToString(this.getCurrentExtendTime(), false, false);
        });
        variableReplacements.put("%remainingtime-next-extend-countdown-writtenout%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill() + this.getCurrentExtendTime(), true, false, false, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%remainingtime-next-extend-countdown-short%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill() + this.getCurrentExtendTime(), false, false, false, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%price-current%", () -> {
            return Price.formatPrice(this.getCurrentExtendPrice());
        });
        return variableReplacements;
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection yamlConfiguration = super.toConfigurationSection();
        if (this.getPriceObject().isAutoPrice()) {
            yamlConfiguration.set("maxExtendTime", null);
        } else {
            yamlConfiguration.set("maxExtendTime", this.getMaxExtendTime());
        }
        return yamlConfiguration;
    }
}

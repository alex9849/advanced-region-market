package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class CountdownRegion extends Region {
    private static boolean staticSaveNeeded = false;
    private long payedTill;

    public CountdownRegion(WGRegion region, List<SignData> sellsigns, boolean sold, Region parentRegion) {
        super(region, sellsigns, sold, parentRegion);
    }

    public CountdownRegion(WGRegion region, World regionworld, List<SignData> sellsigns, boolean sold) {
        super(region, regionworld, sellsigns, sold);
    }

    public abstract long getExtendTime();

    public long getPayedTill() {
        return this.payedTill;
    }

    public void setPayedTill(long payedTill) {
        this.payedTill = payedTill;
    }

    /**
     * Sets the region to sold or not
     *
     * @param sold If 'true' the region will be set to sold
     *             If the region is already expired it will be extended
     *             to the actual_time + extend_time
     *             <p>
     *             if 'false' the region will be set to not_sold.
     *             The already payed time of the region will be set to the
     *             actual_time owners and members will not be removed!
     */
    @Override
    public void setSold(boolean sold) {
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if (sold) {
            if (this.getPayedTill() < actualTime) {
                this.payedTill = actualTime + this.getExtendTime();
            }
        } else {
            this.payedTill = actualTime;
        }
        super.setSold(sold);
        this.queueSave();
    }

    @Override
    public void unsell(ActionReason actionReason, boolean logToConsole, boolean preventBackupCreation) {
        super.unsell(actionReason, logToConsole, preventBackupCreation);
        GregorianCalendar actualtime = new GregorianCalendar();
        if (this.getPayedTill() > actualtime.getTimeInMillis()) {
            this.setPayedTill(actualtime.getTimeInMillis());
        }
        this.queueSave();
    }

    /**
     * Extends the region
     * If the region is already expired it will extend the region to
     * the actual_time + extend_time
     * Doesn't set the region to bought!
     */
    public void extend() {
        this.extend(this.getExtendTime());
    }

    public void extend(long time) {
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if (this.getPayedTill() < actualTime) {
            this.payedTill = actualTime;
        }
        this.payedTill += time;
        this.queueSave();
        this.updateSigns();
    }

    @Override
    public double getPaybackMoney() {
        double amount = (this.getPricePerPeriod() * this.getPaybackPercentage()) / 100;
        GregorianCalendar acttime = new GregorianCalendar();
        long remaining = this.payedTill - acttime.getTimeInMillis();
        amount = amount * ((double) remaining / (double) this.getExtendTime());
        amount = amount * 10;
        amount = Math.round(amount);
        amount = amount / 10d;

        if (amount < 0) {
            return 0;
        }
        return amount;
    }

    public double getPricePerM2PerWeek() {
        if (this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2 = this.getPricePerM2();
        double msPerWeek = 1000 * 60 * 60 * 24 * 7;
        return (msPerWeek / this.getExtendTime()) * pricePerM2;
    }

    public double getPricePerM3PerWeek() {
        if (this.getExtendTime() == 0) {
            return Integer.MAX_VALUE;
        }
        double pricePerM2PerWeek = this.getPricePerM2PerWeek();
        return pricePerM2PerWeek / (this.getRegion().getMaxPoint().getBlockY() - this.getRegion().getMinPoint().getBlockY());
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection cofSection = super.toConfigurationSection();
        if (this.getPriceObject().isAutoPrice()) {
            cofSection.set("extendTime", null);
        } else {
            cofSection.set("extendTime", this.getExtendTime());
        }
        cofSection.set("payedTill", this.payedTill);
        return cofSection;
    }

    /*
       Static methods
     */

    protected HashMap<String, Supplier<String>> getVariableReplacements() {
        HashMap<String, Supplier<String>> variableReplacements = super.getVariableReplacements();
        variableReplacements.put("%extendtime-short%", () -> {
            return TimeUtil.timeInMsToString(this.getExtendTime(), false, false);
        });
        variableReplacements.put("%extendtime-writtenout%", () -> {
            return TimeUtil.timeInMsToString(this.getExtendTime(), true, false);
        });
        variableReplacements.put("%remainingtime-date%", () -> {
            return TimeUtil.getDate(this.getPayedTill(), true, Messages.REGION_INFO_EXPIRED,
                    AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());
        });
        variableReplacements.put("%remainingtime-countdown-short%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill(), false, false, true, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%remainingtime-countdown-short-cutted%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill(), false, true, true, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%remainingtime-countdown-writtenout%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill(), true, false, true, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%remainingtime-countdown-writtenout-cutted%", () -> {
            return TimeUtil.getCountdown(this.getPayedTill(), true, true, true, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%priceperm2perweek%", () -> {
            return Price.formatPrice(this.getPricePerM2PerWeek());
        });
        variableReplacements.put("%priceperm3perweek%", () -> {
            return Price.formatPrice(this.getPricePerM3PerWeek());
        });
        return variableReplacements;
    }

    public static long stringToTime(String stringtime) throws IllegalArgumentException {
        long time = 0;
        if (stringtime.matches("[\\d]+d")) {
            time = Long.parseLong(stringtime.split("d")[0]);
            time = time * 1000 * 60 * 60 * 24;
        } else if (stringtime.matches("[\\d]+h")) {
            time = Long.parseLong(stringtime.split("h")[0]);
            time = time * 1000 * 60 * 60;
        } else if (stringtime.matches("[\\d]+m")) {
            time = Long.parseLong(stringtime.split("m")[0]);
            time = time * 1000 * 60;
        } else if (stringtime.matches("[\\d]+s")) {
            time = Long.parseLong(stringtime.split("s")[0]);
            time = time * 1000;
        } else if (stringtime.matches("[\\d]+")) {
            time = Long.parseLong(stringtime);
        } else {
            throw new IllegalArgumentException();
        }
        return time;
    }

    public static void setStaticSaved() {
        staticSaveNeeded = false;
    }

    public static void setStaticSaveNeeded() {
        staticSaveNeeded = true;
    }

    public static boolean isStaticSaveNeeded() {
        return staticSaveNeeded;
    }
}

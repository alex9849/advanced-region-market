package net.alex9849.arm.regions;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class CountdownRegion extends Region {
    private long payedTill;
    private long extendTime;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%extendtime%", () -> {
            return timeInMsToString(this.getExtendTime());
        });
        variableReplacements.put("%remaining%", () -> {
            return CountdownRegion.timeInMsToRemainingTimeString(this.getPayedTill(), false, Messages.REGION_INFO_EXPIRED);
        });
        variableReplacements.put("%priceperm2perweek%", () -> {
            return Price.formatPrice(this.getPricePerM2PerWeek());
        });
        variableReplacements.put("%priceperm3perweek%", () -> {
            return Price.formatPrice(this.getPricePerM3PerWeek());
        });

        this.stringReplacer = new StringReplacer(variableReplacements, 50);
    }

    public CountdownRegion(WGRegion region, World regionworld, List<SignData> contractsign, ContractPrice contractPrice,
                           Boolean sold, Boolean inactivityReset, Boolean isHotel, Boolean doBlockReset,
                           RegionKind regionKind, FlagGroup flagGroup, Location teleportLoc, long lastreset,
                           long lastLogin, boolean isUserResettable, long payedTill, List<Region> subregions,
                           int allowedSubregions, EntityLimitGroup entityLimitGroup,
                           HashMap<EntityLimit.LimitableEntityType, Integer> extraEntitys, int boughtExtraTotalEntitys) {
        super(region, regionworld, contractsign, contractPrice, sold, inactivityReset, isHotel, doBlockReset, regionKind,
                flagGroup, teleportLoc, lastreset, lastLogin, isUserResettable, subregions, allowedSubregions,
                entityLimitGroup, extraEntitys, boughtExtraTotalEntitys);
        this.payedTill = payedTill;
        this.extendTime = contractPrice.getExtendTime();
        if (this.extendTime < 1000) {
            this.extendTime = 1000;
        }
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

    public static String getCountdown(Boolean shortedCountdown, long endTimeInMs, boolean showZeroIfDateInThePast, String ifDateInPastReplacement) {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(endTimeInMs);

        long remainingMilliSeconds = payedTill.getTimeInMillis() - actualtime.getTimeInMillis();

        String sec;
        String min;
        String hour;
        String days;
        if (shortedCountdown) {
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

        if (remainingMilliSeconds < 0 && !showZeroIfDateInThePast) {
            return ifDateInPastReplacement;
        }

        long remainingDays = TimeUnit.DAYS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingDays * 1000 * 60 * 60 * 24);

        long remainingHours = TimeUnit.HOURS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);
        remainingMilliSeconds = remainingMilliSeconds - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(remainingMilliSeconds, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if (remainingDays != 0) {
            timetoString = timetoString + remainingDays + days;
            if (shortedCountdown) {
                return timetoString;
            }
        }
        if (remainingHours != 0) {
            timetoString = timetoString + remainingHours + hour;
            if (shortedCountdown) {
                return timetoString;
            }
        }
        if (remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + min;
            if (shortedCountdown) {
                return timetoString;
            }
        }
        if (remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + sec;
            if (shortedCountdown) {
                return timetoString;
            }
        }
        if (remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0) {
            timetoString = "0" + sec;
        }

        return timetoString;
    }

    public static String timeInMsToString(long timeInMs) {
        long time = timeInMs;

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 * 24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if (remainingDays != 0) {
            timetoString = timetoString + remainingDays + Messages.TIME_DAYS;
        }
        if (remainingHours != 0) {
            timetoString = timetoString + remainingHours + Messages.TIME_HOURS;
        }
        if (remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + Messages.TIME_MINUTES;
        }
        if (remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + Messages.TIME_SECONDS;
        }
        if (remainingSeconds == 0 && remainingMinutes == 0 && remainingHours == 0 && remainingDays == 0) {
            timetoString = "0" + Messages.TIME_SECONDS;
        }

        return timetoString;
    }

    public static String getDate(long dateInMs, boolean showDateIfDateInThePast, String ifDateInPastReplacement) {
        GregorianCalendar actualtime = new GregorianCalendar();
        GregorianCalendar payedTill = new GregorianCalendar();
        payedTill.setTimeInMillis(dateInMs);

        if ((payedTill.getTimeInMillis() - actualtime.getTimeInMillis()) < 0 && !showDateIfDateInThePast) {
            return ifDateInPastReplacement;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(AdvancedRegionMarket.getInstance().getPluginSettings().getDateTimeformat());

        return sdf.format(payedTill.getTime());
    }

    public static String timeInMsToRemainingTimeString(long endTime, boolean showTimeIfDateInThePast, String ifDateInPastReplacement) {
        String timetoString = AdvancedRegionMarket.getInstance().getPluginSettings().getRemainingTimeTimeformat();
        timetoString = timetoString.replace("%countdown%", getCountdown(AdvancedRegionMarket.getInstance().getPluginSettings().isUseShortCountdown(), endTime, showTimeIfDateInThePast, ifDateInPastReplacement));
        timetoString = timetoString.replace("%date%", getDate(endTime, showTimeIfDateInThePast, ifDateInPastReplacement));
        return timetoString;
    }

    public long getExtendTime() {
        return this.extendTime;
    }

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
        super.setSold(sold);
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if (sold) {
            if (this.getPayedTill() < actualTime) {
                this.payedTill = actualTime + this.getExtendTime();
            }
        } else {
            this.payedTill = actualTime;
        }
        this.queueSave();
    }

    @Override
    public void unsell(ActionReason actionReason, boolean logToConsole) {
        super.unsell(actionReason, logToConsole);
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
        long actualTime = new GregorianCalendar().getTimeInMillis();
        if (this.payedTill < actualTime) {
            this.payedTill = actualTime;
        }
        this.payedTill += this.getExtendTime();
        this.queueSave();
        this.updateSigns();
    }

    @Override
    public double getPaybackMoney() {
        double amount = (this.getPrice() * this.getRegionKind().getPaybackPercentage()) / 100;
        GregorianCalendar acttime = new GregorianCalendar();
        long remaining = this.payedTill - acttime.getTimeInMillis();
        amount = amount * ((double) remaining / (double) extendTime);
        amount = amount * 10;
        amount = Math.round(amount);
        amount = amount / 10d;

        if (amount < 0) {
            return 0;
        }
        return amount;
    }

    @Override
    public void setPrice(Price price) {
        super.setPrice(price);
        if (price instanceof ContractPrice) {
            this.extendTime = ((ContractPrice) price).getExtendTime();
        }
        this.updateSigns();
        this.queueSave();
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

    public String getConvertedMessage(String message) {
        message = super.getConvertedMessage(message);
        return this.stringReplacer.replace(message).toString();
    }

    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection cofSection = super.toConfigurationSection();
        if (this.getPriceObject().isAutoPrice()) {
            cofSection.set("extendTime", null);
        } else {
            cofSection.set("extendTime", this.getExtendTime());
        }
        cofSection.set("payedTill", this.getPayedTill());
        return cofSection;
    }
}

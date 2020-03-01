package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RentPreset extends ContractPreset {
    private boolean hasMaxRentTime = false;
    private long maxRentTime = 0;

    public RentPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup,
                      boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean hasMaxRentTime,
                      long maxRentTime, boolean hasExtendPerClick, long extendPerClick, boolean isUserRestorable,
                      int allowedSubregions, AutoPrice autoPrice, EntityLimitGroup entityLimitGroup,
                      List<String> setupCommands, int maxMembers, int paybackPercentage) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, hasExtendPerClick,
                extendPerClick, isUserRestorable, allowedSubregions, autoPrice, entityLimitGroup, setupCommands,
                maxMembers, paybackPercentage);
        this.hasMaxRentTime = hasMaxRentTime;
        this.maxRentTime = maxRentTime;
    }

    public RentPreset getCopy() {
        List<String> newsetupCommands = new ArrayList<>();
        for (String cmd : getCommands()) {
            newsetupCommands.add(cmd);
        }
        return new RentPreset(this.getName(), this.hasPrice(), this.getPrice(),
                this.getRegionKind(), this.getFlagGroup(), this.isInactivityReset(),
                this.isHotel(), this.isAutoRestore(), this.hasMaxRentTime(),
                this.getMaxRentTime(), this.hasExtendTime(), this.getExtendTime(),
                this.isUserRestorable(), this.getAllowedSubregions(),
                this.getAutoPrice(), this.getEntityLimitGroup(), newsetupCommands,
                this.getMaxMembers(), this.getPaybackPercentage());
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        this.removeMaxRentTime();
    }

    public long getMaxRentTime() {
        return this.maxRentTime;
    }

    public void setMaxRentTime(String string) {
        this.hasMaxRentTime = true;
        this.maxRentTime = RentPrice.stringToTime(string);
        this.removeAutoPrice();
    }

    public void setMaxRentTime(Long time) {
        this.hasMaxRentTime = true;
        this.maxRentTime = time;
        this.removeAutoPrice();
    }


    public boolean hasMaxRentTime() {
        return hasMaxRentTime;
    }

    public void removeMaxRentTime() {
        this.hasMaxRentTime = false;
        this.maxRentTime = 0;
    }

    private String longToTime(long time) {

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 * 24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if (remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if (remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if (remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if (remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }


    @Override
    public void getAdditionalInfo(CommandSender sender) {
        super.getAdditionalInfo(sender);
        String maxrenttime = "not defined";
        if (this.hasMaxRentTime()) {
            maxrenttime = longToTime(this.getMaxRentTime());
        }
        sender.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + maxrenttime);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (super.canPriceLineBeLetEmpty() && this.hasMaxRentTime()) || this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, List<SignData> signs) {

        RentRegion rentRegion = new RentRegion(wgRegion, world, signs, new RentPrice(AutoPrice.DEFAULT),
                false, this.isInactivityReset(), this.isHotel(), this.isAutoRestore(),
                this.getRegionKind(), this.getFlagGroup(), null, 0,
                new GregorianCalendar().getTimeInMillis(), this.isUserRestorable(),
                1, new ArrayList<>(), this.getAllowedSubregions(), this.getEntityLimitGroup(),
                new HashMap<>(), 0, this.getMaxMembers(), this.getPaybackPercentage());

        if(this.canPriceLineBeLetEmpty()) {
            if(this.hasAutoPrice()) {
                rentRegion.setPrice(new RentPrice(this.getAutoPrice()));
            } else {
                rentRegion.setPrice(new RentPrice(this.getPrice(), this.getExtendTime(), this.getMaxRentTime()));
            }
        }

        return rentRegion;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasMaxRentTime", this.hasMaxRentTime());
        section.set("maxRentTime", this.getMaxRentTime());
        return section;
    }

}

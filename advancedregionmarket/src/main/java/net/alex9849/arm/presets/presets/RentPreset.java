package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RentPreset extends CountdownPreset {
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


    @Override
    public void getAdditionalInfo(CommandSender sender) {
        super.getAdditionalInfo(sender);
        String maxrenttime = "not defined";
        if (this.hasMaxRentTime()) {
            maxrenttime = TimeUtil.timeInMsToString(this.getMaxRentTime(), false, false);
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
    protected RentRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new RentRegion(wgRegion, world, signs, new RentPrice(AutoPrice.DEFAULT), false, new ArrayList<>());
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (this.hasPrice() && this.hasExtendTime() && this.hasMaxRentTime()) {
            region.setPrice(new RentPrice(this.getPrice(), this.getExtendTime(), this.getMaxRentTime()));
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasMaxRentTime", this.hasMaxRentTime());
        section.set("maxRentTime", this.getMaxRentTime());
        return section;
    }

}

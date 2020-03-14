package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public abstract class CountdownPreset extends Preset {
    private boolean hasExtendTime = false;
    private long extendTime = 0;

    public CountdownPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup,
                          boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean hasExtend,
                          long extend, boolean isUserRestorable, int allowedSubregions, AutoPrice autoPrice,
                          EntityLimitGroup entityLimitGroup, List<String> setupCommands, int maxMembers,
                          int paybackPercentage) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, isUserRestorable,
                allowedSubregions, autoPrice, entityLimitGroup, setupCommands, maxMembers, paybackPercentage);
        this.hasExtendTime = hasExtend;
        this.extendTime = extend;
    }

    public boolean hasExtendTime() {
        return hasExtendTime;
    }

    public void removeExtendTime() {
        this.hasExtendTime = false;
        this.extendTime = 0;
    }

    public long getExtendTime() {
        return this.extendTime;
    }

    public void setExtendTime(String string) {
        this.hasExtendTime = true;
        this.extendTime = RentPrice.stringToTime(string);
        this.removeAutoPrice();
    }

    public void setExtend(long time) {
        this.hasExtendTime = true;
        this.extendTime = time;
        this.removeAutoPrice();
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        this.removeExtendTime();
    }

    @Override
    public void getAdditionalInfo(CommandSender sender) {
        String extendtime = "not defined";
        if (this.hasExtendTime()) {
            extendtime = TimeUtil.timeInMsToString(this.extendTime, false, false);
        }
        sender.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + extendtime);
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.hasPrice() && this.hasExtendTime()) || this.hasAutoPrice();
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (this.hasPrice() && this.hasExtendTime()) {
            region.setPrice(new ContractPrice(this.getPrice(), this.getExtendTime()));
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasExtendTime", this.hasExtendTime());
        section.set("extendTime", this.getExtendTime());
        return section;
    }
}

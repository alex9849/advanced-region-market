package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
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

public class ContractPreset extends Preset {
    private boolean hasExtendTime = false;
    private long extendTime = 0;

    public ContractPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup,
                          boolean inactivityReset, boolean isHotel, boolean doBlockReset, boolean hasExtend,
                          long extend, boolean isUserRestorable, int allowedSubregions, AutoPrice autoPrice,
                          EntityLimitGroup entityLimitGroup, List<String> setupCommands, int maxMembers,
                          int paybackPercentage) {
        super(name, hasPrice, price, regionKind, flagGroup, inactivityReset, isHotel, doBlockReset, isUserRestorable,
                allowedSubregions, autoPrice, entityLimitGroup, setupCommands, maxMembers, paybackPercentage);
        this.hasExtendTime = hasExtend;
        this.extendTime = extend;
    }

    public ContractPreset getCopy() {
        List<String> newsetupCommands = new ArrayList<>();
        for (String cmd : getCommands()) {
            newsetupCommands.add(cmd);
        }
        return new ContractPreset(this.getName(), this.hasPrice(), this.getPrice(), this.getRegionKind(),
                this.getFlagGroup(), this.isInactivityReset(), this.isHotel(), this.isAutoRestore(),
                this.hasExtendTime(), this.getExtendTime(), this.isUserRestorable(),
                this.getAllowedSubregions(), this.getAutoPrice(), this.getEntityLimitGroup(), newsetupCommands,
                this.getMaxMembers(), this.getPaybackPercentage());
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
        String extendtime = "not defined";
        if (this.hasExtendTime()) {
            extendtime = longToTime(this.extendTime);
        }
        sender.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + extendtime);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.CONTRACTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.hasPrice() && this.hasExtendTime()) || this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, List<SignData> signs) {

        ContractRegion contractRegion = new ContractRegion(wgRegion, world, signs,
                new ContractPrice(AutoPrice.DEFAULT), false, this.isInactivityReset(),
                this.isHotel(), this.isAutoRestore(), this.getRegionKind(), this.getFlagGroup(),
                null, 0, new GregorianCalendar().getTimeInMillis(),
                this.isUserRestorable(), 1, true, new ArrayList<>(),
                this.getAllowedSubregions(), this.getEntityLimitGroup(), new HashMap<>(), 0,
                this.getMaxMembers(), this.getPaybackPercentage());

        if (this.hasAutoPrice()) {
            contractRegion.setPrice(new ContractPrice(this.getAutoPrice()));
        } else if (this.hasPrice() && this.hasExtendTime()) {
            contractRegion.setPrice(new ContractPrice(this.getPrice(), this.getExtendTime()));
        }
        return contractRegion;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasExtendTime", this.hasExtendTime());
        section.set("extendTime", this.getExtendTime());
        return section;
    }

}

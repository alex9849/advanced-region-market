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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RentPreset extends Preset {
    private boolean hasMaxRentTime = false;
    private long maxRentTime = 0;
    private boolean hasExtendPerClick = false;
    private long extendPerClick = 0;

    public RentPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean hasMaxRentTime,
                      long maxRentTime, boolean hasExtendPerClick, long extendPerClick, boolean isUserResettable, int allowedSubregions, AutoPrice autoPrice, EntityLimitGroup entityLimitGroup, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, flagGroup, autoReset, isHotel, doBlockReset, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupCommands);
        this.hasMaxRentTime = hasMaxRentTime;
        this.maxRentTime = maxRentTime;
        this.hasExtendPerClick = hasExtendPerClick;
        this.extendPerClick = extendPerClick;
    }

    public RentPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new RentPreset(this.name, this.hasPrice, this.price, this.regionKind, this.flagGroup, this.autoReset, this.isHotel, this.doBlockReset, this.hasMaxRentTime, this.maxRentTime, this.hasExtendPerClick, this.extendPerClick, this.isUserResettable, this.allowedSubregions, this.autoPrice, this.entityLimitGroup, newsetupCommands);
    }

    public boolean hasExtendPerClick() {
        return hasExtendPerClick;
    }

    public void removeExtendPerClick(){
        this.hasExtendPerClick = false;
        this.extendPerClick = 0;
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        this.removeExtendPerClick();
        this.removeMaxRentTime();
    }

    public long getMaxRentTime(){
        return this.maxRentTime;
    }

    public long getExtendPerClick(){
        return this.extendPerClick;
    }

    public void setExtendPerClick(String string) {
        this.hasExtendPerClick = true;
        this.extendPerClick = RentPrice.stringToTime(string);
        this.removeAutoPrice();
    }

    public void setExtendPerClick(long time) {
        this.hasExtendPerClick = true;
        this.extendPerClick = time;
        this.removeAutoPrice();
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

    private String longToTime(long time){

        long remainingDays = TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingDays * 1000 * 60 * 60 *24);

        long remainingHours = TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingHours * 1000 * 60 * 60);

        long remainingMinutes = TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
        time = time - (remainingMinutes * 1000 * 60);

        long remainingSeconds = TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS);


        String timetoString = "";
        if(remainingDays != 0) {
            timetoString = timetoString + remainingDays + "d";
        }
        if(remainingHours != 0) {
            timetoString = timetoString + remainingHours + "h";
        }
        if(remainingMinutes != 0) {
            timetoString = timetoString + remainingMinutes + "m";
        }
        if(remainingSeconds != 0) {
            timetoString = timetoString + remainingSeconds + "s";
        }

        return timetoString;
    }


    @Override
    public void getAdditionalInfo(Player player) {
        String extendtime = "not defined";
        if(this.hasExtendPerClick()) {
            extendtime = longToTime(this.getExtendPerClick());
        }
        String maxrenttime = "not defined";
        if(this.hasMaxRentTime()) {
            maxrenttime = longToTime(this.getMaxRentTime());
        }
        player.sendMessage(Messages.REGION_INFO_EXTEND_PER_CLICK + extendtime);
        player.sendMessage(Messages.REGION_INFO_MAX_RENT_TIME + maxrenttime);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.hasPrice() && this.hasMaxRentTime() && this.hasExtendPerClick())|| this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, CommandSender sender, List<SignData> signs) {

        RentRegion rentRegion = new RentRegion(wgRegion, world, signs, new RentPrice(AutoPrice.DEFAULT), false, this.isAutoReset(), this.isHotel(), this.isDoBlockReset(), this.getRegionKind(), this.getFlagGroup(), null, 0, this.isUserResettable(), 1, new ArrayList<>(), this.getAllowedSubregions(), this.entityLimitGroup, new HashMap<>(), 0);
        if(this.hasAutoPrice()) {
            rentRegion.setPrice(new RentPrice(this.getAutoPrice()));
        } else if (this.hasPrice() && this.hasExtendPerClick() && this.hasMaxRentTime()) {
            rentRegion.setPrice(new RentPrice(this.getPrice(), this.getExtendPerClick(), this.getMaxRentTime()));
        }
        this.executeSavedCommands(sender, rentRegion);
        return rentRegion;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasMaxRentTime", this.hasMaxRentTime());
        section.set("maxRentTime", this.getMaxRentTime());
        section.set("hasExtendPerClick", this.hasExtendPerClick());
        section.set("extendPerClick", this.getExtendPerClick());
        return section;
    }

}

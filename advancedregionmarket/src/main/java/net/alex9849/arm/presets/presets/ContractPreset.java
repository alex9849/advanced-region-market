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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ContractPreset extends Preset {
    private boolean hasExtend = false;
    private long extend = 0;

    public ContractPreset(String name, boolean hasPrice, double price, RegionKind regionKind, FlagGroup flagGroup, boolean autoReset, boolean isHotel, boolean doBlockReset, boolean hasExtend, long extend, boolean isUserResettable, int allowedSubregions, AutoPrice autoPrice, EntityLimitGroup entityLimitGroup, List<String> setupCommands){
        super(name, hasPrice, price, regionKind, flagGroup, autoReset, isHotel, doBlockReset, isUserResettable, allowedSubregions, autoPrice, entityLimitGroup, setupCommands);
        this.hasExtend = hasExtend;
        this.extend = extend;
    }

    public ContractPreset getCopy(){
        List<String> newsetupCommands = new ArrayList<>();
        for(String cmd : setupCommands) {
            newsetupCommands.add(cmd);
        }
        return new ContractPreset(this.name, this.hasPrice, this.price, this.regionKind, this.flagGroup, this.autoReset, this.isHotel, this.doBlockReset, this.hasExtend, this.extend, this.isUserResettable, this.allowedSubregions, this.autoPrice, this.entityLimitGroup, newsetupCommands);
    }

    public boolean hasExtend() {
        return hasExtend;
    }

    public void removeExtend(){
        this.hasExtend = false;
        this.extend = 0;
    }

    public long getExtend(){
        return this.extend;
    }

    public void setExtend(String string) {
        this.hasExtend = true;
        this.extend = RentPrice.stringToTime(string);
        this.removeAutoPrice();
    }

    public void setExtend(long time) {
        this.hasExtend = true;
        this.extend = time;
        this.removeAutoPrice();
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        this.removeExtend();
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
        if(this.hasExtend()) {
            extendtime = longToTime(this.extend);
        }
        player.sendMessage(Messages.REGION_INFO_AUTO_EXTEND_TIME + extendtime);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.CONTRACTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.hasPrice() && this.hasExtend()) || this.hasAutoPrice();
    }

    @Override
    public Region generateRegion(WGRegion wgRegion, World world, CommandSender sender, List<SignData> signs) {

        ContractRegion contractRegion = new ContractRegion(wgRegion, world, signs, new ContractPrice(AutoPrice.DEFAULT), false, this.isAutoReset(), this.isHotel(), this.isDoBlockReset(), this.getRegionKind(), this.getFlagGroup(), null, 0, this.isUserResettable(), 1, true, new ArrayList<>(), this.getAllowedSubregions(), this.entityLimitGroup, new HashMap<>(), 0);
        if(this.hasAutoPrice()) {
            contractRegion.setPrice(new ContractPrice(this.getAutoPrice()));
        } else if (this.hasPrice() && this.hasExtend()) {
            contractRegion.setPrice(new ContractPrice(this.getPrice(), this.getExtend()));
        }
        this.executeSavedCommands(sender, contractRegion);

        return contractRegion;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("hasExtend", this.hasExtend());
        section.set("extendTime", this.getExtend());
        return section;
    }

}

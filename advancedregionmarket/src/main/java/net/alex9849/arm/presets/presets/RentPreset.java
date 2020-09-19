package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class RentPreset extends CountdownPreset {
    private Long maxRentTime;

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        if(autoPrice != null) {
            this.maxRentTime = null;
        }
    }

    @Override
    public void sendPresetInfo(CommandSender sender) {
        for(String msg : Messages.PRESET_INFO_RENTREGION) {
            sender.sendMessage(this.replaceVariables(msg));
        }
    }

    public Long getMaxRentTime() {
        return this.maxRentTime;
    }

    public void setMaxRentTime(String string) {
        this.setMaxRentTime(RentPrice.stringToTime(string));
    }

    public void setMaxRentTime(Long time) {
        if(time == null) {
            this.maxRentTime = null;
            return;
        }
        if (time < 1000) {
            throw new IllegalArgumentException("MaxRentTime needs to be at least one second!");
        }
        this.maxRentTime = time;
        this.setAutoPrice(null);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (super.canPriceLineBeLetEmpty() && this.maxRentTime != null) || this.getAutoPrice() != null;
    }

    @Override
    protected RentRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new RentRegion(wgRegion, world, signs, new RentPrice(AutoPrice.DEFAULT), false);
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (this.getPrice() != null && this.getExtendTime() != null
                && this.getMaxRentTime() != null && region instanceof RentRegion) {
            ((RentRegion) region).setRentPrice(new RentPrice(this.getPrice(), this.getExtendTime(), this.getMaxRentTime()));
        }
    }

    @Override
    public HashMap<String, StringCreator> getVariableReplacements() {
        HashMap<String, StringCreator> variableReplacements = super.getVariableReplacements();
        variableReplacements.put("%maxrenttime%", () -> Messages.getStringValue(this.getMaxRentTime(), x ->
                TimeUtil.timeInMsToString(x, false, false), Messages.NOT_DEFINED));
        return variableReplacements;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("maxRentTime", this.getMaxRentTime());
        return section;
    }

}

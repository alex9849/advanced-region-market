package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

public class RentPreset extends CountdownPreset {
    private Long maxRentTime;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%maxrenttime%", () -> Messages.getStringValue(this.getMaxRentTime(), x ->
                TimeUtil.timeInMsToString(x, false, false), Messages.NOT_DEFINED));
        this.stringReplacer = new StringReplacer(variableReplacements, 50);
    }

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
        this.maxRentTime = time;
        if(time != null) {
            this.setAutoPrice(null);
        }
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
        if (this.getPrice() != null && this.getExtendTime() != null && this.getMaxRentTime() != null) {
            region.setPrice(new RentPrice(this.getPrice(), this.getExtendTime(), this.getMaxRentTime()));
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("maxRentTime", this.getMaxRentTime());
        return section;
    }

    public String replaceVariables(String message) {
        String replacedMessge = super.replaceVariables(message);
        return this.stringReplacer.replace(replacedMessge).toString();
    }

}

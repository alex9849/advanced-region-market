package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RentRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.signs.SignData;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.inter.WGRegion;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class RentPreset extends CountdownPreset {
    private Long maxExtendTime;

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        if(autoPrice != null) {
            this.maxExtendTime = null;
        }
    }

    @Override
    public void sendPresetInfo(CommandSender sender) {
        for(String msg : Messages.PRESET_INFO_RENTREGION) {
            sender.sendMessage(this.replaceVariables(msg));
        }
    }

    public Long getMaxExtendTime() {
        return this.maxExtendTime;
    }

    public void setMaxExtendTime(String string) {
        this.setMaxExtendTime(RentPrice.stringToTime(string));
    }

    public void setMaxExtendTime(Long time) {
        if(time == null) {
            this.maxExtendTime = null;
            return;
        }
        if (time < 1000) {
            throw new IllegalArgumentException("MaxExtendTime needs to be at least one second!");
        }
        this.maxExtendTime = time;
        this.setAutoPrice(null);
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.RENTPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (super.canPriceLineBeLetEmpty() && this.maxExtendTime != null) || this.getAutoPrice() != null;
    }

    @Override
    protected RentRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new RentRegion(wgRegion, world, signs, new RentPrice(AutoPrice.DEFAULT), false);
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (region instanceof RentRegion && (this.getPrice() != null
                || this.getExtendTime() != null || this.getMaxExtendTime() != null)) {
            RentRegion rentRegion = (RentRegion) region;
            RentPrice oldPrice = (RentPrice) rentRegion.getPriceObject();
            double price = this.getPrice() != null ? this.getPrice() : oldPrice.calcPrice(region.getRegion());
            long extendTime = this.getExtendTime() != null ? this.getExtendTime() : oldPrice.getExtendTime();
            long maxExtendTime = this.getMaxExtendTime() != null ? this.getMaxExtendTime() : oldPrice.getMaxExtendTime();
            rentRegion.setRentPrice(new RentPrice(price, extendTime, maxExtendTime));
        }
    }

    @Override
    public HashMap<String, Supplier<String>> getVariableReplacements() {
        HashMap<String, Supplier<String>> variableReplacements = super.getVariableReplacements();
        variableReplacements.put("%maxextendtime%", () -> Messages.getStringValue(this.getMaxExtendTime(), x ->
                TimeUtil.timeInMsToString(x, false, false), Messages.NOT_DEFINED));
        return variableReplacements;
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("maxExtendTime", this.getMaxExtendTime());
        return section;
    }

}

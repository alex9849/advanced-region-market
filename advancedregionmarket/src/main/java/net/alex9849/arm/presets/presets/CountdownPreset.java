package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.regions.price.RentPrice;
import net.alex9849.arm.util.TimeUtil;
import net.alex9849.arm.util.stringreplacer.StringCreator;
import net.alex9849.arm.util.stringreplacer.StringReplacer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public abstract class CountdownPreset extends Preset {
    private Long extendTime;
    private StringReplacer stringReplacer;

    {
        HashMap<String, StringCreator> variableReplacements = new HashMap<>();
        variableReplacements.put("%extendtime%", () -> Messages.getStringValue(this.getExtendTime(), x ->
                TimeUtil.timeInMsToString(x, false, false), Messages.NOT_DEFINED));
        this.stringReplacer = new StringReplacer(variableReplacements, 50);
    }

    public Long getExtendTime() {
        return this.extendTime;
    }

    public void setExtendTime(String string) {
        this.setExtendTime(RentPrice.stringToTime(string));
    }

    public void setExtendTime(Long time) {
        this.extendTime = time;
        if(time != null) {
            this.setAutoPrice(null);
        }
    }

    @Override
    public void setAutoPrice(AutoPrice autoPrice) {
        super.setAutoPrice(autoPrice);
        if(autoPrice != null) {
            this.extendTime = null;
        }
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return (this.getPrice() != null && this.getExtendTime() != null) || this.getAutoPrice() != null;
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (this.getPrice() != null && this.getExtendTime() != null
                && region instanceof ContractRegion) {
            ((ContractRegion) region).setContractPrice(new ContractPrice(this.getPrice(), this.getExtendTime()));
        }
    }

    @Override
    public ConfigurationSection toConfigurationSection() {
        ConfigurationSection section = super.toConfigurationSection();
        section.set("extendTime", this.getExtendTime());
        return section;
    }

    public String replaceVariables(String message) {
        String replacedMessge = super.replaceVariables(message);
        return this.stringReplacer.replace(replacedMessge).toString();
    }

}

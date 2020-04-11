package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.Price;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SellPreset extends Preset {

    @Override
    public void sendPresetInfo(CommandSender sender) {
        for(String msg : Messages.PRESET_INFO_SELLREGION) {
            sender.sendMessage(this.replaceVariables(msg));
        }
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.SELLPRESET;
    }

    @Override
    public boolean canPriceLineBeLetEmpty() {
        return this.getPrice() != null || this.getAutoPrice() != null;
    }

    @Override
    protected SellRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new SellRegion(wgRegion, world, signs, new Price(AutoPrice.DEFAULT), false);
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if(this.getPrice() != null && region instanceof SellRegion)
            ((SellRegion) region).setSellPrice(new Price(this.getPrice()));
    }

}

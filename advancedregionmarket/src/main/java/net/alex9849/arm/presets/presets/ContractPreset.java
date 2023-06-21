package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.arm.signs.SignData;
import net.alex9849.inter.WGRegion;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ContractPreset extends CountdownPreset {

    @Override
    public void sendPresetInfo(CommandSender sender) {
        for(String msg : Messages.PRESET_INFO_CONTRACTREGION) {
            sender.sendMessage(this.replaceVariables(msg));
        }
    }

    @Override
    public PresetType getPresetType() {
        return PresetType.CONTRACTPRESET;
    }

    @Override
    public void applyToRegion(Region region) {
        super.applyToRegion(region);
        if (region instanceof ContractRegion && (this.getPrice() != null || this.getExtendTime() != null)) {
            ContractRegion contractRegion = (ContractRegion) region;
            ContractPrice oldPrice = (ContractPrice) contractRegion.getPriceObject();
            double price = this.getPrice() != null ? this.getPrice() : oldPrice.calcPrice(region.getRegion());
            long extendTime = this.getExtendTime() != null ? this.getExtendTime() : oldPrice.getExtendTime();
            contractRegion.setContractPrice(new ContractPrice(price, extendTime));
        }
    }

    @Override
    protected ContractRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new ContractRegion(wgRegion, world, signs, new ContractPrice(AutoPrice.DEFAULT), false);
    }
}

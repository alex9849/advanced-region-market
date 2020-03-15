package net.alex9849.arm.presets.presets;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.price.Autoprice.AutoPrice;
import net.alex9849.arm.regions.price.ContractPrice;
import net.alex9849.inter.WGRegion;
import net.alex9849.signs.SignData;
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
    protected ContractRegion generateBasicRegion(WGRegion wgRegion, World world, List<SignData> signs) {
        return new ContractRegion(wgRegion, world, signs, new ContractPrice(AutoPrice.DEFAULT), false);
    }
}

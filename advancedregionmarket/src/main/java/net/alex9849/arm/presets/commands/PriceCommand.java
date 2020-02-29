package net.alex9849.arm.presets.commands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriceCommand extends PresetOptionModifyCommand<Double> {

    public PriceCommand(PresetType presetType) {
        super("price", Arrays.asList(Permission.ADMIN_PRESET_SET_PRICE),
                "(([0-9]+[.])?[0-9]+|(?i)remove)", "[PRICE]", "", presetType);
    }

    @Override
    protected Double getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        String[] args = command.split(" ");
        if(args[1].equalsIgnoreCase("remove")) {
            return Double.NaN;
        }
        return Double.parseDouble(args[1]);
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Double setting) {
        if(setting == Double.NaN) {
            object.removePrice();
        } else {
            object.setPrice(setting);
        }
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 2) {
            if ("remove".startsWith(args[1])) {
                returnme.add("remove");
            }
        }
        return returnme;
    }
}

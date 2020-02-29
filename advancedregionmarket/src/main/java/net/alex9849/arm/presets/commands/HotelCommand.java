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

public class HotelCommand extends PresetOptionModifyCommand<Boolean> {

    public HotelCommand(PresetType presetType) {
        super("hotel", Arrays.asList(Permission.ADMIN_PRESET_SET_HOTEL),
                "(false|true)", "(true/false)", "", presetType);
    }

    @Override
    protected Boolean getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        return Boolean.parseBoolean(command.split(" ")[1]);
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, Boolean setting) {
        object.setHotel(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            if ("true".startsWith(args[1])) {
                returnme.add("true");
            }
            if ("false".startsWith(args[1])) {
                returnme.add("false");
            }
        }
        return returnme;
    }
}

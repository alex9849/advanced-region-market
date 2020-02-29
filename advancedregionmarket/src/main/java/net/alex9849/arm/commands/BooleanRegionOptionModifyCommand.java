package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.Tuple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BooleanRegionOptionModifyCommand extends RegionOptionModifyCommand<Boolean> {

    public BooleanRegionOptionModifyCommand(String rootCommand, List permissions, String optionName, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(rootCommand, permissions, optionName, "(false|true)",
                "[true/false]",allowSubregions, subregionModifyErrorMessage);
    }

    @Override
    protected Boolean getSettingFromString(Player player, String settingsString) {
        return Boolean.parseBoolean(settingsString);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, Tuple<String, List<Region>> obj, Boolean settingsObj) {
        String sendmessage = Messages.REGION_MODIFIED_BOOLEAN;
        sendmessage = sendmessage.replace("%option%", getOptionName());
        sendmessage = sendmessage.replace("%state%", Messages.convertEnabledDisabled(settingsObj));
        sendmessage = sendmessage.replace("%selectedregions%", obj.getValue1());
        sender.sendMessage(Messages.PREFIX + sendmessage);
    }

    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        List<String> returnme = new ArrayList<>();
        if ("true".startsWith(setting)) {
            returnme.add("true");
        }
        if ("false".startsWith(setting)) {
            returnme.add("false");
        }
        return returnme;
    }
}

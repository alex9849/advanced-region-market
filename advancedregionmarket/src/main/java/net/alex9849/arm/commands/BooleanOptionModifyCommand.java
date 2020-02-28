package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BooleanOptionModifyCommand extends OptionModifyCommand<Boolean> {

    public BooleanOptionModifyCommand(String rootCommand, List permissions, String optionName, boolean allowSubregions, String subregionModifyErrorMessage) {
        super(rootCommand, permissions, optionName, "(false|true)",
                "[true/false]",allowSubregions, subregionModifyErrorMessage);
    }

    @Override
    protected Boolean getSettingFromString(Player player, String settingsString) {
        return Boolean.parseBoolean(settingsString);
    }

    protected String getSuccessMessage(String selectedRegions, Boolean setting, String optionName) {
        String sendmessage = Messages.REGION_MODIFIED_BOOLEAN;
        sendmessage = sendmessage.replace("%option%", optionName);
        sendmessage = sendmessage.replace("%state%", Messages.convertEnabledDisabled(setting));
        sendmessage = sendmessage.replace("%selectedregions%", selectedRegions);
        return sendmessage;
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

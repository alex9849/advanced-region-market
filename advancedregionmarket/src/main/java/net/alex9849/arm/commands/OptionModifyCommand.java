package net.alex9849.arm.commands;

import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class OptionModifyCommand<Object, SettingsObj> extends BasicArmCommand {
    private String objectNotFoundMsg;
    private String settingNotFoundMsg;


    public OptionModifyCommand(boolean isConsoleCommand, String rootCommand, List<String> regexList, List<String> usage,
                               List<String> permissions, String objectNotFoundMsg, String settingNotFoundMsg) {
        super(isConsoleCommand, rootCommand, regexList, usage, permissions);
        this.objectNotFoundMsg = objectNotFoundMsg;
        this.settingNotFoundMsg = settingNotFoundMsg;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command) throws InputException, CmdSyntaxException {
        Object obj = getObjectFromCommand(sender, command);
        if(obj == null) {
            throw new InputException(sender, this.objectNotFoundMsg);
        }

        SettingsObj setting = getSettingsFromCommand(sender, command);
        if(setting == null) {
            throw new InputException(sender, this.settingNotFoundMsg);
        }

        applySetting(obj, setting);
        sendSuccessMessage(sender, obj, setting);
        return true;
    }

    protected abstract Object getObjectFromCommand(CommandSender sender, String command) throws InputException;

    protected abstract SettingsObj getSettingsFromCommand(CommandSender sender, String name) throws InputException;

    protected abstract void applySetting(Object object, SettingsObj setting);

    protected abstract void sendSuccessMessage(CommandSender sender, Object obj, SettingsObj settingsObj);

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        returnme.addAll(tabCompleteObject(player, args));
        returnme.addAll(tabCompleteSettingsObject(player, args));
        return returnme;
    }

    protected abstract List<String> tabCompleteObject(Player player, String[] args);

    protected abstract List<String> tabCompleteSettingsObject(Player player, String[] args);
}

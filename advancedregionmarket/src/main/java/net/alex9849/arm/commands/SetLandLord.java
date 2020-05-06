package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SetLandLord extends RegionOptionModifyCommand<UUID> {
    public SetLandLord() {
        super("setlandlord", Arrays.asList(Permission.ADMIN_SET_LANDLORD),
                false, "Landlord", "[^;\n ]+", "[PLAYER/SERVER]",
                false, Messages.SUBREGION_LANDLORD_ERROR, "");
    }

    @Override
    protected void applySetting(Region region, UUID setting) {
        region.setLandlord(setting);
    }

    @Override
    protected UUID getSettingFromString(Player player, String settingsString) {
        if(settingsString.equalsIgnoreCase("server")) {
            return null;
        }
        return Bukkit.getOfflinePlayer(settingsString).getUniqueId();
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        List<String> returnme = CommandHandler.tabCompleteOnlinePlayers(setting);
        if("server".startsWith(setting.toLowerCase())) {
            returnme.add("server");
        }
        return returnme;
    }
}

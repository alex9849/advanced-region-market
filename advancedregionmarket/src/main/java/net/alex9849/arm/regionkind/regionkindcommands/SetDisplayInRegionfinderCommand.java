package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetDisplayInRegionfinderCommand extends RegionKindOptionModifyCommand<Boolean> {


    public SetDisplayInRegionfinderCommand() {
        super("setdisplayinregionfinder",
                Arrays.asList(Permission.REGIONKIND_SET_DISPLAY_IN_REGIONFINDER), "(false|true)",
                "[true/false]", "");
    }

    @Override
    protected Boolean getSettingsFromString(CommandSender sender, String setting) {
        return Boolean.parseBoolean(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKind object, Boolean setting) {
        object.setDisplayInRegionfinder(setting);
    }

    @Override
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


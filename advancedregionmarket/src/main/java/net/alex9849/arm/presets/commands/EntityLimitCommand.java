package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityLimitCommand extends PresetOptionModifyCommand<EntityLimitGroup> {

    public EntityLimitCommand(PresetType presetType) {
        super("entitylimit", Arrays.asList(Permission.ADMIN_PRESET_SET_ENTITYLIMIT),
                true, "[^;\n ]+", "[ENTITYLIMIT/remove]",
                Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST, presetType);
    }

    @Override
    protected EntityLimitGroup getSettingsFromString(CommandSender sender, String setting) throws InputException {
        if(setting.equalsIgnoreCase("remove")) {
            return null;
        }
        EntityLimitGroup elg = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()
                .getEntityLimitGroup(setting);
        if(elg == EntityLimitGroup.SUBREGION) {
            throw new InputException(sender, Messages.ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        }
        return elg;
    }

    @Override
    protected void applySetting(CommandSender sender, Preset object, EntityLimitGroup setting) {
        object.setEntityLimitGroup(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String settings) {
        List<String> returnme = new ArrayList<>();
        returnme.addAll(AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()
                .tabCompleteEntityLimitGroups(settings));
        if ("remove".startsWith(settings)) {
            returnme.add("remove");
        }
        return returnme;
    }
}

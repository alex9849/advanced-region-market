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
                "[^;\n ]+", "[ENTITYLIMIT]",
                Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST, presetType);
    }

    @Override
    protected EntityLimitGroup getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        EntityLimitGroup elg = AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()
                .getEntityLimitGroup(command.split(" ")[1]);
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
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        if (args.length == 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getEntityLimitGroupManager()
                .tabCompleteEntityLimitGroups(args[1]);
    }
}

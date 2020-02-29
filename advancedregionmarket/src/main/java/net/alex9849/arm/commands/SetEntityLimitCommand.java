package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetEntityLimitCommand extends RegionOptionModifyCommand<EntityLimitGroup> {

    public SetEntityLimitCommand() {
        super("setentitylimit", Arrays.asList(Permission.ADMIN_SET_ENTITYLIMIT),
                "EntityLimitGroup", "[^;\n ]+", "[ENTITYTYPE]",
                false, Messages.SUB_REGION_ENTITYLIMITGROUP_ERROR);
    }

    @Override
    protected void applySetting(Region region, EntityLimitGroup setting) {
        region.setEntityLimitGroup(setting);
    }

    @Override
    protected EntityLimitGroup getSettingFromString(Player player, String settingsString) throws InputException {
        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getInstance()
                .getEntityLimitGroupManager().getEntityLimitGroup(settingsString);

        if (entityLimitGroup == null) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
        }

        if (entityLimitGroup == EntityLimitGroup.SUBREGION) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        }
        return entityLimitGroup;
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        return AdvancedRegionMarket.getInstance().getEntityLimitGroupManager().tabCompleteEntityLimitGroups(setting);
    }
}
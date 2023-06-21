package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetItemCommand extends RegionKindOptionModifyCommand<Material> {

    public SetItemCommand(AdvancedRegionMarket plugin) {
        super("setitem", Arrays.asList(Permission.REGIONKIND_SET_ITEM),
                "[^;\n ]+", "[ITEM]", Messages.MATERIAL_NOT_FOUND, plugin);
    }

    @Override
    protected Material getSettingsFromString(CommandSender sender, String setting) {
        return getPlugin().getMaterialFinder().getMaterial(setting);
    }

    @Override
    protected void applySetting(CommandSender sender, RegionKind object, Material setting) {
        object.setItem(setting);
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String setting) {
        List<String> returnme = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.toString().toLowerCase().startsWith(setting)) {
                returnme.add(material.toString());
            }
        }
        return returnme;
    }
}

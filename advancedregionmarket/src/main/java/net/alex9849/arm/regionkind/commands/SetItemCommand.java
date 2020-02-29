package net.alex9849.arm.regionkind.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.util.MaterialFinder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetItemCommand extends RegionKindOptionModifyCommand<Material> {

    public SetItemCommand() {
        super("setitem", Arrays.asList(Permission.REGIONKIND_SET_ITEM),
                "[^;\n ]+", "[ITEM]", Messages.MATERIAL_NOT_FOUND);
    }

    @Override
    protected Material getSettingsFromString(CommandSender sender, String setting) {
        return MaterialFinder.getMaterial(setting);
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

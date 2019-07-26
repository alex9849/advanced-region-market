package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimitGroup;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityLimitCommand extends BasicArmCommand {
    private final String rootCommand = "entitylimit";
    private final String regex_set = "(?i)entitylimit [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("entitylimit ([ENTITYLIMIT])"));
    private PresetType presetType;

    public EntityLimitCommand(PresetType presetType) {
        this.presetType = presetType;
    }

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_set);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_ENTITYLIMIT)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        if(this.presetType == null) {
            return false;
        }

        Preset preset = ActivePresetManager.getPreset(player, this.presetType);

        if(preset == null) {
            preset = this.presetType.create();
            ActivePresetManager.add(new PresetPlayerPair(player, preset));
        }

        EntityLimitGroup entityLimitGroup = AdvancedRegionMarket.getEntityLimitGroupManager().getEntityLimitGroup(args[1]);
        if(entityLimitGroup == null) {
            player.sendMessage(Messages.PREFIX + Messages.ENTITYLIMITGROUP_DOES_NOT_EXIST);
            return true;
        }
        if(entityLimitGroup == EntityLimitGroup.SUBREGION) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_SUBREGION_GROUP_ONLY_FOR_SUBREGIONS);
        }

        preset.setEntityLimitGroup(entityLimitGroup);
        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_ENTITYLIMIT)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getEntityLimitGroupManager().tabCompleteEntityLimitGroups(args[1]));
                }
            }
        }
        return returnme;
    }
}

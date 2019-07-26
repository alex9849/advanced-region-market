package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.PresetPlayerPair;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionKindCommand extends BasicArmCommand {

    private final String rootCommand = "regionkind";
    private final String regex_set = "(?i)regionkind [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("regionkind ([REGIONKIND])"));
    private PresetType presetType;

    public RegionKindCommand(PresetType presetType) {
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

        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
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

        RegionKind regkind = AdvancedRegionMarket.getRegionKindManager().getRegionKind(args[1]);
        if(regkind == null) {
            player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
            return true;
        }
        if(regkind == RegionKind.SUBREGION) {
            throw new InputException(player, Messages.SUB_REGION_REGIONKIND_ONLY_FOR_SUB_REGIONS);
        }
        preset.setRegionKind(regkind);
        player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getRegionKindManager().completeTabRegionKinds(args[1], ""));
                }
            }
        }
        return returnme;
    }
}

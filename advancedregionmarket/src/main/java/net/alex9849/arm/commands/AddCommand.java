package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoPermissionException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.Region;
import net.alex9849.inter.WGRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCommand extends BasicArmCommand {
    public AddCommand() {
        super(false, "add", Arrays.asList("(?i)add [^;\n]+ (?i)(SELL|RENT|CONTRACT)"),
                Arrays.asList("add [REGIONID] [SELL/RENT/CONTRACT]"),
                Arrays.asList(Permission.ADMIN_CREATE_SELL, Permission.ADMIN_CREATE_RENT, Permission.ADMIN_CREATE_CONTRACT));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException, NoPermissionException {
        Player player = (Player) sender;
        String[] args = command.split(" ");
        PresetType presetType;
        if(args[2].equalsIgnoreCase("sell")) {
            if(!player.hasPermission(Permission.ADMIN_CREATE_SELL)) {
                throw new NoPermissionException(Messages.NO_PERMISSION);
            }
            presetType = PresetType.SELLPRESET;
        } else if(args[2].equalsIgnoreCase("rent")) {
            if(!player.hasPermission(Permission.ADMIN_CREATE_RENT)) {
                throw new NoPermissionException(Messages.NO_PERMISSION);
            }
            presetType = PresetType.RENTPRESET;
        } else {
            //CONTRACT
            if(!player.hasPermission(Permission.ADMIN_CREATE_CONTRACT)) {
                throw new NoPermissionException(Messages.NO_PERMISSION);
            }
            presetType = PresetType.CONTRACTPRESET;
        }
        WGRegion wgRegion = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getRegion(player.getWorld(), args[1]);
        if(wgRegion == null) {
            throw new InputException(player, Messages.WGREGION_NOT_FOUND);
        }
        if(AdvancedRegionMarket.getInstance().getRegionManager().getRegion(wgRegion) != null) {
            throw new InputException(player, Messages.REGION_ALREADY_REGISTERED);
        }
        Preset preset = ActivePresetManager.getPreset(player, presetType);
        if(preset == null) {
            preset = presetType.create();
        }
        Region region = preset.generateRegion(wgRegion, player.getWorld(), sender, false, new ArrayList<>());
        region.createSchematic();
        try {
            region.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
        } catch (FeatureDisabledException e) {
            //Ignore
        }
        AdvancedRegionMarket.getInstance().getRegionManager().add(region);
        preset.executeSetupCommands(player, region);
        region.updateSigns();
        player.sendMessage(Messages.REGION_ADDED_TO_ARM);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length < 2) {
            return new ArrayList<>();
        }
        List<String> returnMe = new ArrayList<>();
        if(args.length == 2) {
            returnMe.addAll(AdvancedRegionMarket.getInstance().getWorldGuardInterface().tabCompleteRegions(args[1], player.getWorld()));
        }
        else if(args.length == 3) {
            if("rent".startsWith(args[2].toLowerCase())) {
                returnMe.add("rent");
            }
            if("sell".startsWith(args[2].toLowerCase())) {
                returnMe.add("sell");
            }
            if("contract".startsWith(args[2].toLowerCase())) {
                returnMe.add("contract");
            }
        }
        return returnMe;
    }
}

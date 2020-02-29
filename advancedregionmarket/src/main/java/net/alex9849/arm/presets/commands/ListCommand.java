package net.alex9849.arm.presets.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListCommand extends BasicArmCommand {
    private PresetType presetType;

    public ListCommand(PresetType presetType) {
        super(false, "list",
                Arrays.asList("(?i)list"),
                Arrays.asList("list"),
                Arrays.asList(Permission.ADMIN_PRESET_LIST));
        this.presetType = presetType;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        List<Preset> patterns = AdvancedRegionMarket.getInstance().getPresetPatternManager().getPresets(this.presetType);

        sender.sendMessage(Messages.PREFIX + ChatColor.GOLD + this.presetType.getMajorityName() + ":");
        for (int i = 0; i < patterns.size(); i++) {
            sender.sendMessage(ChatColor.GOLD + " - " + patterns.get(i).getName());
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}

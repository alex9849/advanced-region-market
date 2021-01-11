package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCommand extends BasicArmCommand {
    private static final String GUI_REGEX = "(?i)gui";
    private static final String REGION_GUI_REGEX = "(?i)gui [^;\n ]+";

    public GuiCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "gui",
                Arrays.asList(GUI_REGEX, REGION_GUI_REGEX),
                Arrays.asList("gui", "gui [REGION]"),
                Arrays.asList(Permission.MEMBER_GUI));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        if(command.matches(GUI_REGEX)) {
            Gui.openMainMenu((Player) sender, null);
            return true;
        }
        String regionName = command.split(" ", 2)[1];
        Region region = getPlugin().getRegionManager()
                .getRegionbyNameAndWorldCommands(regionName, player.getWorld().getName());
        if(region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }
        boolean isOwner = region.getRegion().hasOwner(player.getUniqueId());
        boolean isMember = region.getRegion().hasMember(player.getUniqueId());
        if(isOwner) {
            Gui.openRegionOwnerManager(player, region, null);
            return true;
        } else if(isMember) {
            Gui.openRegionMemberManager(player, region);
            return true;
        } else {
            throw new InputException(player, Messages.NOT_A_MEMBER_OR_OWNER);
        }
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return getPlugin().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.MEMBER_OR_OWNER,
                        true, true);
    }
}

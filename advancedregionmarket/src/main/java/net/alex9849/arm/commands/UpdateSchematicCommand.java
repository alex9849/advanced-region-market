package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSchematicCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)updateschematic [^;\n ]+";

    public UpdateSchematicCommand() {
        super(false, "updateschematic",
                Arrays.asList("(?i)updateschematic [^;\n ]+", "(?i)updateschematic"),
                Arrays.asList("updateschematic [REGION]", "updateschematic"),
                Arrays.asList(Permission.ADMIN_UPDATESCHEMATIC));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;

        Region region;
        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        player.sendMessage(Messages.PREFIX + Messages.UPDATING_SCHEMATIC);
        region.createSchematic();
        player.sendMessage(Messages.PREFIX + Messages.SCHEMATIC_UPDATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length == 2) {
            return AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
        }
        return new ArrayList<>();
    }
}

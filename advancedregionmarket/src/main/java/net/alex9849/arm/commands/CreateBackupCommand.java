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

public class CreateBackupCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)createbackup [^;\n ]+";

    public CreateBackupCommand() {
        super(false, "createbackup",
                Arrays.asList("(?i)createbackup [^;\n ]+", "(?i)createbackup"),
                Arrays.asList("createbackup [REGION]", "createbackup"),
                Arrays.asList(Permission.ADMIN_CREATE_BACKUP));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        //No console command sender must be a player
        Player player = (Player) sender;

        Region region;
        if (command.matches(this.regex_with_args)) {
            String[] args = command.split(" ");
            region = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, args[1]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, "");
        }

        region.createBackup();
        player.sendMessage(Messages.PREFIX + Messages.BACKUP_CREATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return AdvancedRegionMarket.getInstance().getRegionManager()
                .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true);
    }
}

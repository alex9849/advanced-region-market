package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RestoreBackupCommand extends BasicArmCommand {

    public RestoreBackupCommand() {
        super(false, "restorebackup",
                Arrays.asList("(?i)restorebackup [^;\n ]+ [^;\n ]+"),
                Arrays.asList("restorebackup [REGION] [ID]"),
                Arrays.asList(Permission.ADMIN_RESTORE_BACKUP));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        String[] args = command.split(" ");

        Region region = AdvancedRegionMarket.getInstance().getRegionManager()
                .getRegionAtPositionOrNameCommand(player, args[1]);

        try {
            region.loadBackup(args[2]);
            player.sendMessage(Messages.PREFIX + Messages.BACKUP_RESTORED);
        } catch (SchematicNotFoundException e) {
            player.sendMessage(Messages.PREFIX + Messages.COULD_NOT_LOAD_BACKUP
                    .replace("%regionid%", e.getRegion().getId()));
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));

        } else if (args.length == 3) {
            String regionname = args[1];
            Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(regionname, player.getWorld().getName());
            if (region != null) {
                File backupDirectory = new File(AdvancedRegionMarket.getInstance().getDataFolder()
                        + "/schematics/"
                        + region.getRegionworld().getName() + "/"
                        + region.getRegion().getId() + "/Backups");
                if (backupDirectory.exists()) {
                    File[] backupDirectoryContent = backupDirectory.listFiles();
                    for (File backupFile : backupDirectoryContent) {
                        if (backupFile.isFile()) {
                            String[] backupFileParts = backupFile.getName().split(Pattern.quote("."));
                            if (backupFileParts.length < 2) {
                                continue;
                            }
                            String backupID = backupFile.getName().replace("." + backupFileParts[backupFileParts.length - 1], "");
                            if(backupID.startsWith(args[2])) {
                                returnme.add(backupID);
                            }
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

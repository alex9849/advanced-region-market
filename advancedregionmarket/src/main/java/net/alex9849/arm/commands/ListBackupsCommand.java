package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ListBackupsCommand extends BasicArmCommand {
    private String regex_with_args = "(?i)listbackups [^;\n ]+";

    public ListBackupsCommand() {
        super(false, "listbackups",
                Arrays.asList("(?i)listbackups [^;\n ]+", "(?i)listbackups"),
                Arrays.asList("listbackups [REGION]", "listbackups"),
                Arrays.asList(Permission.ADMIN_LIST_BACKUPS));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;
        if (command.matches(this.regex_with_args)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        }

        File backupDirectory = new File(AdvancedRegionMarket.getInstance().getDataFolder()
                + "/schematics/"
                + region.getRegionworld().getName() + "/"
                + region.getRegion().getId() + "/Backups");

        List<String> backupNames = new ArrayList<>();

        if (backupDirectory.exists()) {
            File[] backupDirectoryContent = backupDirectory.listFiles();
            for (File backupFile : backupDirectoryContent) {
                if (backupFile.isFile()) {
                    String[] backupFileParts = backupFile.getName().split(Pattern.quote("."));
                    if (backupFileParts.length < 2) {
                        continue;
                    }
                    String backupID = backupFile.getName().replace("." + backupFileParts[backupFileParts.length - 1], "");
                    backupNames.add(backupID);
                }
            }
        }

        player.sendMessage(region.replaceVariables(Messages.BACKUP_LIST_HEADER));
        for(String backupID : backupNames) {
            player.sendMessage("- " + backupID);
        }

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

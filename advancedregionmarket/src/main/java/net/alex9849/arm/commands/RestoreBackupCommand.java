package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.SchematicNotFoundException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RestoreBackupCommand implements BasicArmCommand {
    private final String rootCommand = "restorebackup";
    private final String regex_with_args = "(?i)restorebackup [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("restorebackup [REGION] [ID]"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex_with_args);
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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;

        if (!player.hasPermission(Permission.ADMIN_RESTORE_BACKUP)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);

        try {
            region.loadBackup(args[2]);
            player.sendMessage(Messages.PREFIX + Messages.BACKUP_RESTORED);
        } catch (SchematicNotFoundException e) {
            player.sendMessage(Messages.PREFIX + Messages.COULD_NOT_LOAD_BACKUP.replace("%regionid%", e.getRegion().getId()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_RESTORE_BACKUP)) {
                    if (args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));
                    } else if (args.length == 3 && (args[0].equalsIgnoreCase(this.rootCommand))) {
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
                }
            }
        }
        return returnme;
    }
}

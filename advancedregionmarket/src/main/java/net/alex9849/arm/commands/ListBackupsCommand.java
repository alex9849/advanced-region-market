package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
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

public class ListBackupsCommand implements BasicArmCommand {
    private final String rootCommand = "listbackups";
    private final String regex_with_args = "(?i)listbackups [^;\n ]+";
    private final String regex = "(?i)listbackups";
    private final List<String> usage = new ArrayList<>(Arrays.asList("listbackups [REGION]", "listbackups"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex) || command.matches(this.regex_with_args);
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

        //TODO change permissions and stuff
        if (!player.hasPermission(Permission.ADMIN_LIST_BACKUPS)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }

        Region region;
        if (allargs.matches(this.regex)) {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, "");
        } else {
            region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionAtPositionOrNameCommand(player, args[1]);
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

        //TODO make beautiful
        for(String backupID : backupNames) {
            player.sendMessage(backupID);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if (args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_LIST_BACKUPS)) {
                    if (args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if (args.length == 2 && (args[0].equalsIgnoreCase(this.rootCommand))) {
                        returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.ALL, true, true));
                    }
                }
            }
        }
        return returnme;
    }
}

package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetRegionKind extends BasicArmCommand {
    private final String rootCommand = "setregionkind";
    private final String regex = "(?i)setregionkind [^;\n ]+ [^;\n ]+";
    private final String usage = "/arm setregionkind [REGIONKIND] [REGION]";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(sender instanceof Player){
            if(!sender.hasPermission(Permission.ADMIN_SETREGIONKIND)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            Player player = (Player) sender;
            for(int i = 0; i < Region.getRegionList().size(); i++){
                if(Region.getRegionList().get(i).getRegion().getId().equalsIgnoreCase(args[2]) && Region.getRegionList().get(i).getRegionworld().equals(player.getWorld().getName())){
                    if(Region.getRegionList().get(i).setKind(args[1])){
                        sender.sendMessage(Messages.PREFIX + Messages.REGION_KIND_SET);
                        return true;
                    } else {
                        throw new InputException(sender, Messages.REGION_KIND_NOT_EXIST);
                    }
                }
            }
            throw new InputException(sender, Messages.REGION_KIND_REGION_NOT_EXIST);
        } else {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_SETREGIONKIND))
                    if (args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if (args.length == 2) {
                        returnme.addAll(RegionKind.completeTabRegionKinds(args[1]));
                    } else if (args.length == 3) {
                        returnme.addAll(Region.completeTabRegions(player, args[2], PlayerRegionRelationship.ALL));
                    }
            }
        }
        return returnme;
    }
}

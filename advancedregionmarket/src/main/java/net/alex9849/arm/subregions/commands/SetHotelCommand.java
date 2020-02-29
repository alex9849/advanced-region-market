package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.OptionModifyCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetHotelCommand extends OptionModifyCommand<Region, Boolean> {

    public SetHotelCommand() {
        super(false, true, "sethotel",
                Arrays.asList("(?i)sethotel [^;\n ]+ (false|true)"),
                Arrays.asList("sethotel [REGION] [true/false]"),
                Arrays.asList(Permission.SUBREGION_SET_IS_HOTEL), Messages.REGION_DOES_NOT_EXIST, "");
    }

    @Override
    protected Region getObjectFromCommand(CommandSender sender, String command) throws InputException {
        Player player = (Player) sender;
        Region region = AdvancedRegionMarket.getInstance().getRegionManager()
                .getRegionbyNameAndWorldCommands(command.split(" ")[1], player.getWorld().getName());
        if (region == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }
        if (!region.isSubregion()) {
            throw new InputException(player, Messages.REGION_NOT_A_SUBREGION);
        }
        if (!region.getParentRegion().getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(player, Messages.PARENT_REGION_NOT_OWN);
        }
        return region;
    }

    @Override
    protected Boolean getSettingsFromCommand(CommandSender sender, String command) throws InputException {
        return Boolean.parseBoolean(command.split(" ")[2]);
    }

    @Override
    protected void applySetting(CommandSender sender, Region object, Boolean setting) {
        object.setHotel(setting);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, Region obj, Boolean aBoolean) {
        String sendmessage = Messages.REGION_MODIFIED_BOOLEAN;
        sendmessage = sendmessage.replace("%option%", "Hotel-function");
        sendmessage = sendmessage.replace("%state%", Messages.convertEnabledDisabled(aBoolean));
        sendmessage = sendmessage.replace("%selectedregions%", obj.getRegion().getId());
        sender.sendMessage(Messages.PREFIX + sendmessage);
    }

    @Override
    protected List<String> tabCompleteObject(Player player, String[] args) {
        return new ArrayList<>();
    }

    @Override
    protected List<String> tabCompleteSettingsObject(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.PARENTREGION_OWNER, false, true));

        } else if (args.length == 3) {
            if ("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if ("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return returnme;
    }
}

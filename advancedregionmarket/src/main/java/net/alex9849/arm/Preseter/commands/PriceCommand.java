package net.alex9849.arm.Preseter.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.*;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PriceCommand extends BasicPresetCommand {

    private final String rootCommand = "price";
    private final String regex_set = "(?i)price [+-]?([0-9]+[.])?[0-9]+";
    private final String regex_remove = "(?i)price (?i)remove";
    private final String usage = "price ([PRICE]/remove)";

    @Override
    public boolean matchesRegex(String command) {
        if(command.matches(this.regex_set)) {
            return true;
        } else {
            return command.matches(this.regex_remove);
        }
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
    public boolean runCommand(Player player, String[] args, String allargs, PresetType presetType) throws InputException {
        if(!player.hasPermission(Permission.ADMIN_PRESET_SET_PRICE)) {
            throw new InputException(player, Messages.NO_PERMISSION);
        }
        if(presetType == null) {
            return false;
        }

        Preset preset = Preset.getPreset(presetType, player);

        if(preset == null) {
            preset = PresetType.create(presetType, player);
        }

        if(allargs.matches(this.regex_set)) {
            preset.setPrice(Double.parseDouble(args[1]));
            player.sendMessage(Messages.PREFIX + Messages.PRESET_SET);
            if(priceLineCanBeLetEmpty(preset)) {
                player.sendMessage(Messages.PREFIX + "You can leave the price-line on signs empty now");
            }
            return true;
        } else {
            preset.removePrice();
            player.sendMessage(Messages.PREFIX + Messages.PRESET_REMOVED);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args, PresetType presetType) {
        List<String> returnme = new ArrayList<>();
        if(player.hasPermission(Permission.ADMIN_PRESET_SET_PRICE)) {
            if(args.length >= 1) {
                if(args.length == 1) {
                    if(this.rootCommand.startsWith(args[0])) {
                        returnme.add(this.rootCommand);
                    }
                }
                if(args.length == 2 && this.rootCommand.equalsIgnoreCase(args[0])) {
                    if("remove".startsWith(args[1])) {
                        returnme.add("remove");
                    }
                    if(args[1].matches("[0-9]+")) {
                        returnme.add(args[1]);
                        returnme.add(args[1]);
                        returnme.add(args[1]);
                        returnme.add(args[1]);
                    }
                }
            }
        }
        return returnme;
    }

    private boolean priceLineCanBeLetEmpty(Preset preset) {
        if (preset instanceof SellPreset) {
            return preset.hasPrice();
        } else if (preset instanceof ContractPreset) {
            ContractPreset contractPreset = (ContractPreset) preset;
            return contractPreset.hasPrice() && contractPreset.hasExtend();
        } else if (preset instanceof RentPreset) {
            RentPreset rentPreset = (RentPreset) preset;
            return rentPreset.hasPrice() && rentPreset.hasExtendPerClick() && rentPreset.hasMaxRentTime();
        }
        return false;
    }
}

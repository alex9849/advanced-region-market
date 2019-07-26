package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.SellType;
import net.alex9849.exceptions.InputException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignLinkModeCommand extends BasicArmCommand {
    private final String rootCommand = "signlinkmode";
    private final String regex_start = "(?i)signlinkmode [^;\n ]+";
    private final String regex_disable = "(?i)signlinkmode (?i)disable";
    private final List<String> usage = new ArrayList<>(Arrays.asList("signlinkmode [selltype]", "signlinkmode disable"));

    @Override
    public boolean matchesRegex(String command) {
        return (command.matches(this.regex_start) || command.matches(this.regex_disable));
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
        if(!sender.hasPermission(Permission.ADMIN_SIGN_LINK_MODE)){
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        if(allargs.matches(this.regex_disable)) {

            SignLinkMode slm = SignLinkMode.getSignLinkMode(player);
            if(slm == null) {
                throw new InputException(player, Messages.SIGN_LINK_MODE_ALREADY_DEACTIVATED);
            }
            slm.unregister();
            player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_DEACTIVATED);
            return true;

        } else {

            SellType sellType = SellType.getSelltype(args[1]);
            if(sellType == null) {
                throw new InputException(player, Messages.SELLTYPE_NOT_EXIST);
            }
            Preset preset = null;
            if(sellType == SellType.SELL) {
                if(!player.hasPermission(Permission.ADMIN_CREATE_SELL)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.SELLPRESET);
            } else if (sellType == SellType.RENT) {
                if(!player.hasPermission(Permission.ADMIN_CREATE_RENT)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.RENTPRESET);
            } else if (sellType == SellType.CONTRACT) {
                if(!player.hasPermission(Permission.ADMIN_CREATE_CONTRACT)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.CONTRACTPRESET);
            }
            if(preset == null) {
                throw new InputException(player, Messages.SIGN_LINK_MODE_NO_PRESET_SELECTED);
            }
            if(!preset.canPriceLineBeLetEmpty()) {
                player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_PRESET_NOT_PRICEREADY);
            }
            SignLinkMode.register(new SignLinkMode(player, preset));
            player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_ACTIVATED);

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if(this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_SIGN_LINK_MODE)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    }
                    if(args.length == 2) {
                        if(SellType.SELL.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_SELL)) {
                            returnme.add(SellType.SELL.getInternalName());
                        }
                        if(SellType.RENT.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_RENT)) {
                            returnme.add(SellType.RENT.getInternalName());
                        }
                        if(SellType.CONTRACT.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_CONTRACT)) {
                            returnme.add(SellType.CONTRACT.getInternalName());
                        }
                        if("disable".startsWith(args[1])) {
                            returnme.add("disable");
                        }
                    }
                }
            }
        }
        return returnme;
    }
}

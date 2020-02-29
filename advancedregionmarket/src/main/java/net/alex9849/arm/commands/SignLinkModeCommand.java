package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.presets.ActivePresetManager;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.presets.presets.PresetType;
import net.alex9849.arm.regions.SellType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignLinkModeCommand extends BasicArmCommand {
    private final String regex_start = "(?i)signlinkmode [^;\n ]+";
    private final String regex_disable = "(?i)signlinkmode (?i)disable";

    public SignLinkModeCommand() {
        super(false, "signlinkmode",
                Arrays.asList("(?i)signlinkmode [^;\n ]+", "(?i)signlinkmode (?i)disable"),
                Arrays.asList("signlinkmode [selltype]", "signlinkmode disable"),
                Arrays.asList(Permission.ADMIN_SIGN_LINK_MODE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        if (!sender.hasPermission(Permission.ADMIN_SIGN_LINK_MODE)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        if (command.matches(this.regex_disable)) {

            SignLinkMode slm = SignLinkMode.getSignLinkMode(player);
            if (slm == null) {
                throw new InputException(player, Messages.SIGN_LINK_MODE_ALREADY_DEACTIVATED);
            }
            slm.unregister();
            player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_DEACTIVATED);
            return true;

        } else {

            String[] args = command.split(" ");
            SellType sellType = SellType.getSelltype(args[1]);
            if (sellType == null) {
                throw new InputException(player, Messages.SELLTYPE_NOT_EXIST);
            }
            Preset preset = null;
            if (sellType == SellType.SELL) {
                if (!player.hasPermission(Permission.ADMIN_CREATE_SELL)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.SELLPRESET);
            } else if (sellType == SellType.RENT) {
                if (!player.hasPermission(Permission.ADMIN_CREATE_RENT)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.RENTPRESET);
            } else if (sellType == SellType.CONTRACT) {
                if (!player.hasPermission(Permission.ADMIN_CREATE_CONTRACT)) {
                    throw new InputException(player, Messages.NO_PERMISSION);
                }
                preset = ActivePresetManager.getPreset(player, PresetType.CONTRACTPRESET);
            }
            if (preset == null) {
                throw new InputException(player, Messages.SIGN_LINK_MODE_NO_PRESET_SELECTED);
            }
            if (!preset.canPriceLineBeLetEmpty()) {
                player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_PRESET_NOT_PRICEREADY);
            }
            SignLinkMode.register(new SignLinkMode(player, preset));
            player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_ACTIVATED);

        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        List<String> returnme = new ArrayList<>();
        if (SellType.SELL.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_SELL)) {
            returnme.add(SellType.SELL.getInternalName());
        }
        if (SellType.RENT.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_RENT)) {
            returnme.add(SellType.RENT.getInternalName());
        }
        if (SellType.CONTRACT.getInternalName().toLowerCase().startsWith(args[1]) && player.hasPermission(Permission.ADMIN_CREATE_CONTRACT)) {
            returnme.add(SellType.CONTRACT.getInternalName());
        }
        if ("disable".startsWith(args[1])) {
            returnme.add("disable");
        }
        return returnme;
    }
}

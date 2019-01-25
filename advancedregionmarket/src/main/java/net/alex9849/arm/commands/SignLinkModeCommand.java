package net.alex9849.arm.commands;

import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.ActivePresetManager;
import net.alex9849.arm.Preseter.presets.Preset;
import net.alex9849.arm.Preseter.presets.PresetType;
import net.alex9849.arm.Preseter.presets.SellPreset;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.SignLinkMode;
import net.alex9849.arm.regions.RegionManager;
import net.alex9849.arm.regions.SellType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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

        //TODO change message
        if(allargs.matches(this.regex_disable)) {

            SignLinkMode slm = SignLinkMode.getSignLinkMode(player);
            if(slm == null) {
                throw new InputException(player, "Sign-Link-Mode is already deactivated!");
            }
            slm.unregister();
            player.sendMessage(Messages.PREFIX + "Sign-Link-Mode deactivated!");
            return true;

        } else {

            SellType sellType = SellType.getSelltype(args[1]);
            if(sellType == null) {
                throw new InputException(player, "The selected selltype does not exist!");
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
                String err = "&cYou dont have a preset loaded! Please load or create a preset first! " +
                        "&cYou can create a preset by using the &6/arm sellpreset/rentpreset/contractpreset &ccommands!\nFor more " +
                        "&cinformation about presets click here:\n&6https://goo.gl/3upfAA (Gitlab Wiki)";
                throw new InputException(player, ChatColor.translateAlternateColorCodes('&', err));
            }
            if(!preset.canPriceLineBeLetEmpty()) {
                player.sendMessage(Messages.PREFIX + "The selected preset is not price-ready! All regions you will create now will be created with the default autoprice");
            }
            SignLinkMode.register(new SignLinkMode(player, preset));
            player.sendMessage(Messages.PREFIX + "Sign-Link-Mode activated!");

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
                }
            }
        }
        return returnme;
    }
}

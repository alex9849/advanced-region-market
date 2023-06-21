package net.alex9849.arm.regionkind.regionkindcommands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regionkind.RegionKind;
import net.alex9849.arm.util.MaterialFinder112;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommand extends BasicArmCommand {

    public CreateCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "create",
                Arrays.asList("(?i)create [^;\n\\. ]+"),
                Arrays.asList("create [REGIONKINDNAME]"),
                Arrays.asList(Permission.REGIONKIND_CREATE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        String[] args = command.split(" ");
        if (getPlugin().getRegionKindManager().getRegionKind(args[1]) != null) {
            throw new InputException(sender, Messages.REGIONKIND_ALREADY_EXISTS);
        }

        Material item = MaterialFinder112.getRedBed();
        String internalName = args[1];
        String displayName = args[1];
        List<String> lore = new ArrayList<>(Arrays.asList("Default Lore"));
        boolean displayInGui = true;
        boolean displayInLimits = true;

        getPlugin().getRegionKindManager().add(new RegionKind(internalName, item, lore, displayName, displayInGui, displayInLimits));
        sender.sendMessage(Messages.PREFIX + Messages.REGIONKIND_CREATED);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}

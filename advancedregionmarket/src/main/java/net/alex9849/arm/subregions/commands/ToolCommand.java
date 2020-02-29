package net.alex9849.arm.subregions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolCommand extends BasicArmCommand {

    public ToolCommand() {
        super(false, "tool",
                Arrays.asList("(?i)tool"),
                Arrays.asList("tool"),
                Arrays.asList(Permission.SUBREGION_TOOL));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        Player player = (Player) sender;
        for (String msg : Messages.SUBREGION_TOOL_INSTRUCTION) {
            player.sendMessage(msg);
        }
        // Check if Tool is already in Inventory -> Prevent duplicating Feathers
        if (checkFeather(player)) {
            throw new InputException(sender, Messages.SUBREGION_TOOL_ALREADY_OWNED);
        } else {
            ItemStack subRegionTool = new ItemStack(Material.FEATHER, 1);
            ItemMeta itemMeta = subRegionTool.getItemMeta();
            itemMeta.setDisplayName("Subregion Tool");
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            subRegionTool.setItemMeta(itemMeta);
            player.getInventory().addItem(subRegionTool);
        }
        return true;
    }

    /**
     * Removes the Subregion-Tool from the players inventory to prevent duplicating feathers a bit
     *
     * @param player The player which gets checked
     * @return boolean True: Feder ist vorhanden<br>False: Spieler hat keine Feder
     */
    private boolean checkFeather(Player player) {
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals("Subregion Tool")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<String> onTabCompleteLogic(Player player, String[] args) {
        return new ArrayList<>();
    }
}

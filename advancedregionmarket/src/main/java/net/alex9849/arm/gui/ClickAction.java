package net.alex9849.arm.gui;

import net.alex9849.arm.exceptions.InputException;
import org.bukkit.entity.Player;

public interface ClickAction {

    void execute(Player player) throws InputException;

}

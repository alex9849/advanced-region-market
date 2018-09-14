package net.alex9849.arm.exceptions;

import net.alex9849.arm.Messages;
import org.bukkit.entity.Player;

import java.io.IOException;

public class InputException extends IOException {
    private String message;
    private Player player;

    public InputException (String message, Player player) {
        this.message = message;
        this.player = player;
    }

    public InputException() {
    }

    public void sendMessage(){
        this.player.sendMessage(Messages.PREFIX + this.message);
    }


}

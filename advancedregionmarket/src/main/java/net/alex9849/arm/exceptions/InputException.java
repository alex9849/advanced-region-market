package net.alex9849.arm.exceptions;

import net.alex9849.arm.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class InputException extends IOException {
    private String message;
    private CommandSender sender;

    public InputException (CommandSender sender, String message) {
        this.message = message;
        this.sender = sender;
    }

    public InputException() {
    }

    public void sendMessage(){
        this.sender.sendMessage(Messages.PREFIX + this.message);
    }


}

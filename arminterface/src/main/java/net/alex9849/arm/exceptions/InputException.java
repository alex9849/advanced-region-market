package net.alex9849.arm.exceptions;

import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputException extends IOException {
    private ArrayList<CommandSender> senders = new ArrayList<CommandSender>();
    private boolean sendToLogger;
    private Logger logger;
    private ArrayList<String> messages = new ArrayList<String>();

    public InputException(CommandSender sender, String message) {
        this.sendToLogger = false;
        this.messages.add(message);
        this.senders.add(sender);
    }

    public void sendMessages(String prefix) {
        if (sendToLogger) {
            this.logger.log(Level.WARNING, messages.get(0));
        } else {
            for (int i = 0; i < this.senders.size(); i++) {
                this.senders.get(i).sendMessage(prefix + this.messages.get(i));
            }
        }
    }


}

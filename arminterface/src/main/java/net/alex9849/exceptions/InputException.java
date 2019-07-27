package net.alex9849.exceptions;

import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputException extends IOException {
    private ArrayList<CommandSender> senders = new ArrayList<CommandSender>();
    private boolean sendToLogger;
    private Logger logger;
    private ArrayList<String> messages = new ArrayList<String>();

    public InputException (CommandSender sender, String message) {
        this.sendToLogger = false;
        this.messages.add(message);
        this.senders.add(sender);
    }

    public InputException (Logger logger, String message) {
        this.sendToLogger = true;
        this.logger = logger;
        this.messages.add(message);
    }

    public InputException (Collection<CommandSender> senders, Collection<String> messages) {
        this.sendToLogger = false;
        if(senders.size() != senders.size()) {
            throw new IllegalArgumentException("The size of CommandSenders and Strings needs to be the same by creating an InputException!");
        }
        this.senders.addAll(senders);
        this.messages.addAll(messages);
    }

    public InputException() {
    }

    public void sendMessages(String prefix){
        if(sendToLogger) {
            this.logger.log(Level.WARNING, messages.get(0));
        } else {
            for(int i = 0; i < this.senders.size(); i++) {
                this.senders.get(i).sendMessage(prefix + this.messages.get(i));
            }
        }
    }


}

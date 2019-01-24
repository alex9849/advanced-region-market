package net.alex9849.arm.exceptions;

import net.alex9849.arm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputException extends IOException {
    private ArrayList<CommandSender> senders = new ArrayList<>();
    private boolean sendToLogger;
    private Logger logger;
    private ArrayList<String> messages = new ArrayList<>();

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

    public void sendMessages(){
        if(sendToLogger) {
            this.logger.log(Level.WARNING, messages.get(0));
        } else {
            for(int i = 0; i < this.senders.size(); i++) {
                this.senders.get(i).sendMessage(Messages.PREFIX + this.messages.get(i));
            }
        }
    }


}

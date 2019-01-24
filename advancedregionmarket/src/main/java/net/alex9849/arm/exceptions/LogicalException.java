package net.alex9849.arm.exceptions;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class LogicalException extends Exception {
    String message;

    public LogicalException(String message) {
        this.message = message;
    }

    public void sendMessage() {
        Bukkit.getLogger().log(Level.ALL, "[ARM] " + message);
    }
}

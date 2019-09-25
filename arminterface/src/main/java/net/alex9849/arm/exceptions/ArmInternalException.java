package net.alex9849.arm.exceptions;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ArmInternalException extends Exception {
    String message;

    public ArmInternalException(String message) {
        this.message = message;
    }

    public void logMessage() {
        Bukkit.getLogger().log(Level.ALL, "[ARM] " + message);
    }

    public String getMessage() {
        return this.message;
    }
}

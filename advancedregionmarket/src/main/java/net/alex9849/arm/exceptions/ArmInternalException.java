package net.alex9849.arm.exceptions;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ArmInternalException extends Exception {
    String message;

    public ArmInternalException(String message) {
        this.message = message;
    }

    public void sendMessage() {
        Bukkit.getLogger().log(Level.ALL, "[ARM] " + message);
    }
}

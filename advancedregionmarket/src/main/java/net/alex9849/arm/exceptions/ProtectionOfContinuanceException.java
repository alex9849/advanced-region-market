package net.alex9849.arm.exceptions;

public class ProtectionOfContinuanceException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public ProtectionOfContinuanceException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public ProtectionOfContinuanceException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

package net.alex9849.arm.exceptions;

public class NoBuyPermissionException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public NoBuyPermissionException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public NoBuyPermissionException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

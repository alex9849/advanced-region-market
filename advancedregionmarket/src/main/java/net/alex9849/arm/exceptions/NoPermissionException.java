package net.alex9849.arm.exceptions;

public class NoPermissionException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public NoPermissionException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public NoPermissionException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

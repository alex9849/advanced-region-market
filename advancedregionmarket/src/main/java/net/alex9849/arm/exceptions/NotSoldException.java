package net.alex9849.arm.exceptions;

public class NotSoldException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public NotSoldException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public NotSoldException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

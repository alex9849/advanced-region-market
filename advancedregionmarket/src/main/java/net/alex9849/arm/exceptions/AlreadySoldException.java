package net.alex9849.arm.exceptions;

public class AlreadySoldException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public AlreadySoldException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public AlreadySoldException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

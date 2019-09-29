package net.alex9849.arm.exceptions;

public class RegionNotOwnException extends IllegalAccessException implements MayHaveMessage {
    private boolean hasMessage;

    public RegionNotOwnException(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public RegionNotOwnException() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}

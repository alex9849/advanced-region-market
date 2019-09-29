package net.alex9849.arm.exceptions;

public class OutOfLimitExeption extends IllegalAccessException implements MayHaveMessage {

    private boolean hasMessage;

    public OutOfLimitExeption(String message) {
        super(message);
        this.hasMessage = message != null;
    }

    public OutOfLimitExeption() {
        super();
        this.hasMessage = false;
    }

    public boolean hasMessage() {
        return hasMessage;
    }

}

package net.alex9849.arm.exceptions;

public class ArmInternalException extends Exception implements MayHaveMessage {
    String message;

    public ArmInternalException(String message) {
        this.message = message;
    }

    public ArmInternalException() {

    }

    public boolean hasMessage() {
        return message != null;
    }

    public String getMessage() {
        return this.message;
    }
}

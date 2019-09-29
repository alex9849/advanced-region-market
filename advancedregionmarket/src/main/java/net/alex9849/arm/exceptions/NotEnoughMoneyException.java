package net.alex9849.arm.exceptions;

public class NotEnoughMoneyException extends ArmInternalException {

    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException() {
        super();
    }
}

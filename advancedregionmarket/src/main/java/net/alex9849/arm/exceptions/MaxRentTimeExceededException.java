package net.alex9849.arm.exceptions;

public class MaxRentTimeExceededException extends ArmInternalException {
    public MaxRentTimeExceededException(String message) {
        super(message);
    }

    public MaxRentTimeExceededException() {

    }
}

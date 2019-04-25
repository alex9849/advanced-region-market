package net.alex9849.exceptions;

import java.io.IOException;

public class SyntaxException extends IOException {
    private String message;

    public SyntaxException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

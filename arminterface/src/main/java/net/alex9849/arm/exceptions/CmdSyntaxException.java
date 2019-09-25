package net.alex9849.arm.exceptions;

import java.io.IOException;
import java.util.List;

public class CmdSyntaxException extends IOException {
    private List<String> syntax;

    public CmdSyntaxException(List<String> syntax) {
        this.syntax = syntax;
    }

    public List<String> getSyntax() {
        return this.syntax;
    }
}

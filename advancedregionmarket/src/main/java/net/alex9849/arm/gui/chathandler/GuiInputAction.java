package net.alex9849.arm.gui.chathandler;

import net.alex9849.arm.exceptions.InputException;

public interface GuiInputAction {

    void runAction(String s) throws InputException;

}

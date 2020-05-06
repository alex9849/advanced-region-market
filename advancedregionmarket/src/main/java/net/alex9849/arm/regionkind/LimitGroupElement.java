package net.alex9849.arm.regionkind;

public interface LimitGroupElement {

    String getName();

    String getDisplayName();

    void setDisplayName(String displayName);

    boolean isDisplayInLimits();

    void setDisplayInLimits(boolean displayInLimits);

    String replaceVariables(String message);
}

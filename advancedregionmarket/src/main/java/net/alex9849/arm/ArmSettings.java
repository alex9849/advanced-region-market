package net.alex9849.arm;

public class ArmSettings {
    private boolean isAllowSubRegionUserReset = false;
    private boolean isSubregionBlockReset = false;
    private boolean isSubregionInactivityReset = false;
    private boolean isTeleportAfterSellRegionBought = false;
    private boolean teleportAfterRentRegionBought = false;
    private boolean isTeleportAfterRentRegionExtend = false;
    private boolean isTeleportAfterContractRegionBought = false;
    private boolean isSendContractRegionExtendMessage = false;
    private boolean isRegionInfoParticleBorder = true;
    private boolean useShortCountdown = false;
    private boolean deleteSubregionsOnParentRegionUnsell = false;
    private boolean deleteSubregionsOnParentRegionBlockReset = false;
    private boolean allowParentRegionOwnersBuildOnSubregions = true;
    private boolean removeEntitiesOnRegionBlockReset = true;
    private boolean activateRegionKindPermissions = false;

    private String signRightClickSneakCommand = "buyaction";
    private String signRightClickNotSneakCommand = "buyaction";
    private String signLeftClickSneakCommand = "buyaction";
    private String signLeftClickNotSneakCommand = "buyaction";

    private boolean isAllowTeleportToBuySign = true;

    private String REMAINING_TIME_TIMEFORMAT = "%date%";
    private String DATE_TIMEFORMAT = "dd.MM.yyyy hh:mm";

    public boolean isActivateRegionKindPermissions() {
        return this.activateRegionKindPermissions;
    }

    public void setActivateRegionKindPermissions(boolean activateRegionKindPermissions) {
        this.activateRegionKindPermissions = activateRegionKindPermissions;
    }

    protected void setRemoveEntitiesOnRegionBlockReset(boolean removeEntitiesOnRegionReset) {
        this.removeEntitiesOnRegionBlockReset = removeEntitiesOnRegionReset;
    }

    protected void setIsAllowTeleportToBuySign(boolean isAllowTeleportToBuySign) {
        this.isAllowTeleportToBuySign = isAllowTeleportToBuySign;
    }

    protected void setDeleteSubregionsOnParentRegionUnsell(boolean deleteSubregionsOnParentRegionUnsell) {
        this.deleteSubregionsOnParentRegionUnsell = deleteSubregionsOnParentRegionUnsell;
    }

    protected void setDeleteSubregionsOnParentRegionBlockReset(boolean deleteSubregionsOnParentRegionBlockReset) {
        this.deleteSubregionsOnParentRegionBlockReset = deleteSubregionsOnParentRegionBlockReset;
    }

    protected void setIsAllowSubRegionUserReset(boolean isAllowSubRegionUserReset) {
        this.isAllowSubRegionUserReset = isAllowSubRegionUserReset;
    }

    protected void setIsRegionInfoParticleBorder(boolean isRegionInfoParticleBorder) {
        this.isRegionInfoParticleBorder = isRegionInfoParticleBorder;
    }

    protected void setIsSubregionBlockReset(boolean isSubregionBlockReset) {
        this.isSubregionBlockReset = isSubregionBlockReset;
    }

    protected void setIsSubregionInactivityReset(boolean isSubregionInactivityReset) {
        this.isSubregionInactivityReset = isSubregionInactivityReset;
    }

    protected void setIsTeleportAfterSellRegionBought(boolean isTeleportAfterSellRegionBought) {
        this.isTeleportAfterSellRegionBought = isTeleportAfterSellRegionBought;
    }

    protected void setIsTeleportAfterRentRegionBought(boolean teleportAfterRentRegionBought) {
        this.teleportAfterRentRegionBought = teleportAfterRentRegionBought;
    }

    protected void setIsTeleportAfterRentRegionExtend(boolean isTeleportAfterRentRegionExtend) {
        this.isTeleportAfterRentRegionExtend = isTeleportAfterRentRegionExtend;
    }

    protected void setIsTeleportAfterContractRegionBought(boolean isTeleportAfterContractRegionBought) {
        this.isTeleportAfterContractRegionBought = isTeleportAfterContractRegionBought;
    }

    protected void setIsSendContractRegionExtendMessage(boolean isSendContractRegionExtendMessage) {
        this.isSendContractRegionExtendMessage = isSendContractRegionExtendMessage;
    }

    protected void setUseShortCountdown(boolean useShortCountdown) {
        this.useShortCountdown = useShortCountdown;
    }

    protected void setRemainingTimeTimeformat(String remainingTimeTimeformat) {
        REMAINING_TIME_TIMEFORMAT = remainingTimeTimeformat;
    }

    protected void setSignRightClickSneakCommand(String signRightClickSneakCommand) {
        this.signRightClickSneakCommand = signRightClickSneakCommand;
    }

    protected void setSignRightClickNotSneakCommand(String signRightClickNotSneakCommand) {
        this.signRightClickNotSneakCommand = signRightClickNotSneakCommand;
    }

    protected void setSignLeftClickSneakCommand(String signLeftClickSneakCommand) {
        this.signLeftClickSneakCommand = signLeftClickSneakCommand;
    }

    protected void setSignLeftClickNotSneakCommand(String signLeftClickNotSneakCommand) {
        this.signLeftClickNotSneakCommand = signLeftClickNotSneakCommand;
    }

    protected void setDateTimeformat(String dateTimeformat) {
        DATE_TIMEFORMAT = dateTimeformat;
    }

    public boolean isAllowSubRegionUserReset() {
        return isAllowSubRegionUserReset;
    }

    public boolean isSubregionBlockReset() {
        return isSubregionBlockReset;
    }

    public boolean isSubregionInactivityReset() {
        return isSubregionInactivityReset;
    }

    public boolean isTeleportAfterSellRegionBought() {
        return isTeleportAfterSellRegionBought;
    }

    public boolean isTeleportAfterRentRegionBought() {
        return teleportAfterRentRegionBought;
    }

    public boolean isTeleportAfterRentRegionExtend() {
        return isTeleportAfterRentRegionExtend;
    }

    public boolean isTeleportAfterContractRegionBought() {
        return isTeleportAfterContractRegionBought;
    }

    public boolean isSendContractRegionExtendMessage() {
        return isSendContractRegionExtendMessage;
    }

    public boolean isUseShortCountdown() {
        return useShortCountdown;
    }

    public String getRemainingTimeTimeformat() {
        return REMAINING_TIME_TIMEFORMAT;
    }

    public String getDateTimeformat() {
        return DATE_TIMEFORMAT;
    }

    public boolean isRegionInfoParticleBorder() {
        return isRegionInfoParticleBorder;
    }

    public boolean isDeleteSubregionsOnParentRegionUnsell() {
        return deleteSubregionsOnParentRegionUnsell;
    }

    public boolean isDeleteSubregionsOnParentRegionBlockReset() {
        return deleteSubregionsOnParentRegionBlockReset;
    }

    public boolean isAllowTeleportToBuySign() {
        return isAllowTeleportToBuySign;
    }

    public boolean isAllowParentRegionOwnersBuildOnSubregions() {
        return allowParentRegionOwnersBuildOnSubregions;
    }

    public void setAllowParentRegionOwnersBuildOnSubregions(boolean allowParentRegionOwnersBuildOnSubregions) {
        this.allowParentRegionOwnersBuildOnSubregions = allowParentRegionOwnersBuildOnSubregions;
    }

    public boolean isRemoveEntitiesOnRegionBlockReset() {
        return this.removeEntitiesOnRegionBlockReset;
    }

    public String getSignRightClickSneakCommand() {
        return signRightClickSneakCommand;
    }

    public String getSignRightClickNotSneakCommand() {
        return signRightClickNotSneakCommand;
    }

    public String getSignLeftClickSneakCommand() {
        return signLeftClickSneakCommand;
    }

    public String getSignLeftClickNotSneakCommand() {
        return signLeftClickNotSneakCommand;
    }

}

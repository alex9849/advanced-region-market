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
    private long userResetCooldown = 604800000;

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

    void setActivateRegionKindPermissions(boolean activateRegionKindPermissions) {
        this.activateRegionKindPermissions = activateRegionKindPermissions;
    }

    void setRemoveEntitiesOnRegionBlockReset(boolean removeEntitiesOnRegionReset) {
        this.removeEntitiesOnRegionBlockReset = removeEntitiesOnRegionReset;
    }

    void setIsAllowTeleportToBuySign(boolean isAllowTeleportToBuySign) {
        this.isAllowTeleportToBuySign = isAllowTeleportToBuySign;
    }

    void setDeleteSubregionsOnParentRegionUnsell(boolean deleteSubregionsOnParentRegionUnsell) {
        this.deleteSubregionsOnParentRegionUnsell = deleteSubregionsOnParentRegionUnsell;
    }

    void setDeleteSubregionsOnParentRegionBlockReset(boolean deleteSubregionsOnParentRegionBlockReset) {
        this.deleteSubregionsOnParentRegionBlockReset = deleteSubregionsOnParentRegionBlockReset;
    }

    void setIsAllowSubRegionUserReset(boolean isAllowSubRegionUserReset) {
        this.isAllowSubRegionUserReset = isAllowSubRegionUserReset;
    }

    void setIsRegionInfoParticleBorder(boolean isRegionInfoParticleBorder) {
        this.isRegionInfoParticleBorder = isRegionInfoParticleBorder;
    }

    void setIsSubregionBlockReset(boolean isSubregionBlockReset) {
        this.isSubregionBlockReset = isSubregionBlockReset;
    }

    void setIsSubregionInactivityReset(boolean isSubregionInactivityReset) {
        this.isSubregionInactivityReset = isSubregionInactivityReset;
    }

    void setIsTeleportAfterSellRegionBought(boolean isTeleportAfterSellRegionBought) {
        this.isTeleportAfterSellRegionBought = isTeleportAfterSellRegionBought;
    }

    void setIsTeleportAfterRentRegionBought(boolean teleportAfterRentRegionBought) {
        this.teleportAfterRentRegionBought = teleportAfterRentRegionBought;
    }

    void setIsTeleportAfterRentRegionExtend(boolean isTeleportAfterRentRegionExtend) {
        this.isTeleportAfterRentRegionExtend = isTeleportAfterRentRegionExtend;
    }

    void setIsTeleportAfterContractRegionBought(boolean isTeleportAfterContractRegionBought) {
        this.isTeleportAfterContractRegionBought = isTeleportAfterContractRegionBought;
    }

    void setIsSendContractRegionExtendMessage(boolean isSendContractRegionExtendMessage) {
        this.isSendContractRegionExtendMessage = isSendContractRegionExtendMessage;
    }

    void setUseShortCountdown(boolean useShortCountdown) {
        this.useShortCountdown = useShortCountdown;
    }

    void setRemainingTimeTimeformat(String remainingTimeTimeformat) {
        REMAINING_TIME_TIMEFORMAT = remainingTimeTimeformat;
    }

    void setSignRightClickSneakCommand(String signRightClickSneakCommand) {
        this.signRightClickSneakCommand = signRightClickSneakCommand;
    }

    void setSignRightClickNotSneakCommand(String signRightClickNotSneakCommand) {
        this.signRightClickNotSneakCommand = signRightClickNotSneakCommand;
    }

    void setSignLeftClickSneakCommand(String signLeftClickSneakCommand) {
        this.signLeftClickSneakCommand = signLeftClickSneakCommand;
    }

    void setSignLeftClickNotSneakCommand(String signLeftClickNotSneakCommand) {
        this.signLeftClickNotSneakCommand = signLeftClickNotSneakCommand;
    }

    void setDateTimeformat(String dateTimeformat) {
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

    public long getUserResetCooldown() {
        return userResetCooldown;
    }

    void setUserResetCooldown(long userResetCooldown) {
        this.userResetCooldown = userResetCooldown;
    }

}

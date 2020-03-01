package net.alex9849.arm;

public class ArmSettings {
    private boolean isAllowSubRegionUserRestore = false;
    private boolean isSubregionAutoRestore = false;
    private int maxSubRegionMembers = -1;
    private boolean isSubregionInactivityReset = false;
    private boolean isTeleportAfterSellRegionBought = false;
    private boolean teleportAfterRentRegionBought = false;
    private boolean isTeleportAfterRentRegionExtend = false;
    private boolean isTeleportAfterContractRegionBought = false;
    private boolean isSendContractRegionExtendMessage = false;
    private boolean isRegionInfoParticleBorder = true;
    private boolean deleteSubregionsOnParentRegionUnsell = false;
    private boolean deleteSubregionsOnParentRegionBlockReset = false;
    private boolean allowParentRegionOwnersBuildOnSubregions = true;
    private boolean removeEntitiesOnRegionBlockReset = true;
    private boolean activateRegionKindPermissions = false;
    private boolean createBackupOnRegionUnsell = true;
    private int paybackPercentage = 100;


    public int getPaybackPercentage() {
        return paybackPercentage;
    }

    public void setPaybackPercentage(int paybackPercentage) {
        this.paybackPercentage = paybackPercentage;
    }


    public boolean isCreateBackupOnRegionUnsell() {
        return createBackupOnRegionUnsell;
    }

    public void setCreateBackupOnRegionUnsell(boolean createBackupOnRegionUnsell) {
        this.createBackupOnRegionUnsell = createBackupOnRegionUnsell;
    }

    public int getMaxSubRegionMembers() {
        return maxSubRegionMembers;
    }

    public void setMaxSubRegionMembers(int maxSubRegionMembers) {
        this.maxSubRegionMembers = maxSubRegionMembers;
    }

    public boolean isCreateBackupOnRegionRestore() {
        return createBackupOnRegionRestore;
    }

    public void setCreateBackupOnRegionRestore(boolean createBackupOnRegionRestore) {
        this.createBackupOnRegionRestore = createBackupOnRegionRestore;
    }

    private boolean createBackupOnRegionRestore = true;
    private long userResetCooldown = 604800000;

    private String signRightClickSneakCommand = "buyaction";
    private String signRightClickNotSneakCommand = "buyaction";
    private String signLeftClickSneakCommand = "buyaction";
    private String signLeftClickNotSneakCommand = "buyaction";

    private boolean isAllowTeleportToBuySign = true;

    private String DATE_TIMEFORMAT = "dd.MM.yyyy hh:mm";

    public boolean isActivateRegionKindPermissions() {
        return this.activateRegionKindPermissions;
    }

    void setActivateRegionKindPermissions(boolean activateRegionKindPermissions) {
        this.activateRegionKindPermissions = activateRegionKindPermissions;
    }

    void setIsAllowTeleportToBuySign(boolean isAllowTeleportToBuySign) {
        this.isAllowTeleportToBuySign = isAllowTeleportToBuySign;
    }

    void setIsAllowSubregionUserRestore(boolean isAllowSubRegionUserRestore) {
        this.isAllowSubRegionUserRestore = isAllowSubRegionUserRestore;
    }

    void setIsRegionInfoParticleBorder(boolean isRegionInfoParticleBorder) {
        this.isRegionInfoParticleBorder = isRegionInfoParticleBorder;
    }

    void setIsSubregionAutoRestore(boolean isSubregionAutoRestore) {
        this.isSubregionAutoRestore = isSubregionAutoRestore;
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

    public boolean isAllowSubRegionUserRestore() {
        return isAllowSubRegionUserRestore;
    }

    public boolean isSubregionAutoRestore() {
        return isSubregionAutoRestore;
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

    public String getDateTimeformat() {
        return DATE_TIMEFORMAT;
    }

    void setDateTimeformat(String dateTimeformat) {
        DATE_TIMEFORMAT = dateTimeformat;
    }

    public boolean isRegionInfoParticleBorder() {
        return isRegionInfoParticleBorder;
    }

    public boolean isDeleteSubregionsOnParentRegionUnsell() {
        return deleteSubregionsOnParentRegionUnsell;
    }

    void setDeleteSubregionsOnParentRegionUnsell(boolean deleteSubregionsOnParentRegionUnsell) {
        this.deleteSubregionsOnParentRegionUnsell = deleteSubregionsOnParentRegionUnsell;
    }

    public boolean isDeleteSubregionsOnParentRegionBlockReset() {
        return deleteSubregionsOnParentRegionBlockReset;
    }

    void setDeleteSubregionsOnParentRegionBlockReset(boolean deleteSubregionsOnParentRegionBlockReset) {
        this.deleteSubregionsOnParentRegionBlockReset = deleteSubregionsOnParentRegionBlockReset;
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

    void setRemoveEntitiesOnRegionBlockReset(boolean removeEntitiesOnRegionReset) {
        this.removeEntitiesOnRegionBlockReset = removeEntitiesOnRegionReset;
    }

    public String getSignRightClickSneakCommand() {
        return signRightClickSneakCommand;
    }

    void setSignRightClickSneakCommand(String signRightClickSneakCommand) {
        this.signRightClickSneakCommand = signRightClickSneakCommand;
    }

    public String getSignRightClickNotSneakCommand() {
        return signRightClickNotSneakCommand;
    }

    void setSignRightClickNotSneakCommand(String signRightClickNotSneakCommand) {
        this.signRightClickNotSneakCommand = signRightClickNotSneakCommand;
    }

    public String getSignLeftClickSneakCommand() {
        return signLeftClickSneakCommand;
    }

    void setSignLeftClickSneakCommand(String signLeftClickSneakCommand) {
        this.signLeftClickSneakCommand = signLeftClickSneakCommand;
    }

    public String getSignLeftClickNotSneakCommand() {
        return signLeftClickNotSneakCommand;
    }

    void setSignLeftClickNotSneakCommand(String signLeftClickNotSneakCommand) {
        this.signLeftClickNotSneakCommand = signLeftClickNotSneakCommand;
    }

    public long getUserResetCooldown() {
        return userResetCooldown;
    }

    void setUserResetCooldown(long userResetCooldown) {
        this.userResetCooldown = userResetCooldown;
    }

}

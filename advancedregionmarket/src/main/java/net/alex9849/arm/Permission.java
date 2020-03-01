package net.alex9849.arm;

import org.bukkit.command.CommandSender;

public class Permission {
    public static final String ADMIN_SET_INACTIVITYRESET = "arm.admin.setinactivityreset";
    public static final String ADMIN_CREATE_SELL = "arm.admin.create.sellregion";
    public static final String ADMIN_CREATE_RENT = "arm.admin.create.rentregion";
    public static final String ADMIN_CREATE_CONTRACT = "arm.admin.create.contractregion";
    public static final String ADMIN_REMOVE_SIGN = "arm.admin.remove";
    public static final String ADMIN_BUILDEVERYWHERE = "arm.admin.buildeverywhere";
    public static final String ADMIN_UPDATESCHEMATIC = "arm.admin.updateschematic";
    public static final String ADMIN_SET_IS_HOTEL = "arm.admin.setishotel";
    public static final String ADMIN_SET_MAX_MEMBERS = "arm.admin.setmaxmembers";
    public static final String ADMIN_SET_AUTORESTORE = "arm.admin.setautorestore";
    public static final String ADMIN_SETREGIONKIND = "arm.admin.setregionkind";
    public static final String ADMIN_SET_PAYBACKPERCENTAGE = "arm.admin.setpaybackpercentage";
    public static final String ADMIN_LISTREGIONS = "arm.admin.listregions";
    public static final String ADMIN_RESETREGION = "arm.admin.resetregion";
    public static final String ADMIN_RESTORE = "arm.admin.restore";
    public static final String ADMIN_UNSELL = "arm.admin.unsell";
    public static final String ADMIN_INFO = "arm.admin.info";
    public static final String ADMIN_ADDMEMBER = "arm.admin.addmember";
    public static final String ADMIN_REMOVEMEMBER = "arm.admin.removemember";
    public static final String ADMIN_TP = "arm.admin.tp";
    public static final String ADMIN_SETOWNER = "arm.admin.setowner";
    public static final String ADMIN_LIMIT_BYPASS = "arm.admin.bypasslimit";
    public static final String ADMIN_SETWARP = "arm.admin.setwarp";
    public static final String ADMIN_DELETEREGION = "arm.admin.deleteregion";
    public static final String ADMIN_EXTEND = "arm.admin.extend";
    public static final String ADMIN_RELOAD = "arm.admin.reload";
    public static final String ADMIN_LIST_BACKUPS = "arm.admin.listbackups";
    public static final String ADMIN_RESTORE_BACKUP = "arm.admin.restorebackup";
    public static final String ADMIN_CREATE_BACKUP = "arm.admin.createbackup";
    public static final String ADMIN_PRESET_SET_PRICE = "arm.admin.preset.setprice";
    public static final String ADMIN_PRESET_SET_EXTENDTIME = "arm.admin.preset.setextendtime";
    public static final String ADMIN_PRESET_SET_MAXRENTTIME = "arm.admin.preset.setmaxrenttime";
    public static final String ADMIN_PRESET_SET_REGIONKIND = "arm.admin.preset.setregionkind";
    public static final String ADMIN_PRESET_SET_INACTIVITYRESET = "arm.admin.preset.setinactivityreset";
    public static final String ADMIN_PRESET_SET_HOTEL = "arm.admin.preset.sethotel";
    public static final String ADMIN_PRESET_SET_PAYBACKPERCENTAGE = "arm.admin.preset.setpaybackpercentage";
    public static final String ADMIN_PRESET_SET_MAX_MEMBERS = "arm.admin.preset.setmaxmembers";
    public static final String ADMIN_PRESET_SET_AUTORESTORE = "arm.admin.preset.setautorestore";
    public static final String ADMIN_PRESET_SET_AUTOPRICE = "arm.admin.preset.setautoprice";
    public static final String ADMIN_PRESET_SET_ENTITYLIMIT = "arm.admin.setentitylimit";
    public static final String ADMIN_PRESET_INFO = "arm.admin.preset.info";
    public static final String ADMIN_PRESET_RESET = "arm.admin.preset.reset";
    public static final String ADMIN_PRESET_HELP = "arm.admin.preset.help";
    public static final String ADMIN_PRESET_LOAD = "arm.admin.preset.load";
    public static final String ADMIN_PRESET_DELETE = "arm.admin.preset.delete";
    public static final String ADMIN_PRESET_SAVE = "arm.admin.preset.save";
    public static final String ADMIN_PRESET_LIST = "arm.admin.preset.list";
    public static final String ADMIN_PRESET_ADDCOMMAND = "arm.admin.preset.addcommand";
    public static final String ADMIN_PRESET_USERRESTORABLE = "arm.admin.preset.userrestorable";
    public static final String ADMIN_PRESET_ALLOWEDSUBREGIONS = "arm.admin.preset.allowedsubregions";
    public static final String ADMIN_PRESET_REMOVECOMMAND = "arm.admin.preset.removecommand";

    public static final String ADMIN_TERMINATE_CONTRACT = "arm.admin.terminatecontract";
    public static final String ADMIN_REGION_STATS = "arm.admin.regionstatus";
    public static final String ADMIN_BYPASS_TELEPORTER_COOLDOWN = "arm.admin.bypassteleportercooldown";
    public static final String ADMIN_SET_SUBREGION_LIMIT = "arm.admin.setsubregionlimit";
    public static final String ADMIN_SET_IS_USERRESTORABLE = "arm.admin.setisuserrestorable";
    public static final String ADMIN_LISTAUTOPRICES = "arm.admin.listautoprices";
    public static final String ADMIN_SET_PRICE = "arm.admin.setprice";
    public static final String ADMIN_SIGN_LINK_MODE = "arm.admin.signlinkmode";
    public static final String ADMIN_FLAGEDITOR = "arm.admin.flageditor";
    public static final String ADMIN_SET_FLAGGROUP = "arm.admin.setflaggroup";
    public static final String ADMIN_PRESET_SET_FLAGGROUP = "arm.admin.preset.flaggroup";
    public static final String ADMIN_SUBREGION_CREATE_ON_UNOWNED_REGIONS = "arm.admin.subregion.createonunowned";

    public static final String ADMIN_ENTITYLIMIT_CREATE = "arm.admin.entitylimit.create";
    public static final String ADMIN_ENTITYLIMIT_DELETE = "arm.admin.entitylimit.delete";
    public static final String ADMIN_ENTITYLIMIT_ADD_LIMIT = "arm.admin.entitylimit.addlimit";
    public static final String ADMIN_ENTITYLIMIT_REMOVE_LIMIT = "arm.admin.entitylimit.removelimit";
    public static final String ADMIN_ENTITYLIMIT_LIST = "arm.admin.entitylimit.list";
    public static final String ADMIN_SET_ENTITYLIMIT = "arm.admin.setentitylimit";
    public static final String ADMIN_ENTITYLIMIT_CHECK = "arm.admin.entitylimit.check";
    public static final String ADMIN_ENTITYLIMIT_SET_EXTRA = "arm.admin.entitylimit.setextra";
    public static final String ADMIN_ENTITYLIMIT_HELP = "arm.admin.entitylimit.help";


    public static final String SUBREGION_TOOL = "arm.subregion.tool";
    public static final String SUBREGION_CREATE_SELL = "arm.subregion.create.sellregion";
    public static final String SUBREGION_CREATE_RENT = "arm.subregion.create.rentregion";
    public static final String SUBREGION_CREATE_CONTRACT = "arm.subregion.create.contractregion";
    public static final String SUBREGION_DELETE_AVAILABLE = "arm.subregion.delete.available";
    public static final String SUBREGION_DELETE_SOLD = "arm.subregion.delete.sold";
    public static final String SUBREGION_SET_IS_HOTEL = "arm.subregion.setishotel";
    public static final String SUBREGION_TP = "arm.subregion.tp";
    public static final String SUBREGION_UNSELL = "arm.subregion.unsell";
    public static final String SUBREGION_RESTORE = "arm.subregion.restore";
    public static final String SUBREGION_HELP = "arm.subregion.help";

    public static final String REGIONKIND_CREATE = "arm.admin.regionkind.create";
    public static final String REGIONKIND_DELETE = "arm.admin.regionkind.delete";
    public static final String REGIONKIND_LIST = "arm.admin.regionkind.list";
    public static final String REGIONKIND_SET_DISPLAY_IN_REGIONFINDER = "arm.admin.regionkind.setdisplayinregionfinder";
    public static final String REGIONKIND_SET_DISPLAY_IN_LIMITS = "arm.admin.regionkind.setdisplayinlimits";
    public static final String REGIONKIND_SET_ITEM = "arm.admin.regionkind.setitem";
    public static final String REGIONKIND_SET_DISPLAYNAME = "arm.admin.regionkind.setdisplayname";
    public static final String REGIONKIND_REMOVE_LORE_LINE = "arm.admin.regionkind.removeloreline";
    public static final String REGIONKIND_ADD_LORE_LINE = "arm.admin.regionkind.addloreline";
    public static final String REGIONKIND_INFO = "arm.admin.regionkind.info";
    public static final String REGIONKIND_HELP = "arm.admin.regionkind.help";

    public static final String MEMBER_RESTORE = "arm.member.restore";
    public static final String MEMBER_SELLBACK = "arm.member.sellregion";
    public static final String MEMBER_INFO = "arm.member.info";
    public static final String MEMBER_ADDMEMBER = "arm.member.addmember";
    public static final String MEMBER_REMOVEMEMBER = "arm.member.removemember";
    public static final String MEMBER_LISTREGIONS = "arm.member.listregions";
    public static final String MEMBER_GUI = "arm.member.gui";
    public static final String MEMBER_TP = "arm.member.tp";
    public static final String MEMBER_LIMIT = "arm.member.limit";
    public static final String MEMBER_OFFER_CREATE = "arm.member.offer.create";
    public static final String MEMBER_OFFER_ANSWER = "arm.member.offer.answer";
    public static final String MEMBER_PROMOTE = "arm.member.promote";
    public static final String MEMBER_BUY = "arm.member.buy";
    public static final String MEMBER_REGIONFINDER = "arm.member.regionfinder";
    public static final String MEMBER_ENTITYLIMIT_INFO = "arm.member.entitylimit.info";
    public static final String MEMBER_ENTITYLIMIT_CHECK = "arm.member.entitylimit.check";
    public static final String MEMBER_ENTITYLIMIT_BUY_EXTRA = "arm.member.entitylimit.buyextra";
    //TODO ADD TO WIKI
    public static final String MEMBER_TP_TO_FREE_REGION = "arm.member.tptofreeregion";
    public static final String MEMBER_FLAGEDITOR = "arm.member.flageditor";

    public static final String ARM_HELP = "arm.help";
    public static final String ARM_BUYKIND = "arm.buykind.";
    public static final String ARM_LIMIT = "arm.limit.";
    public static final String ARM_INACTIVITY_EXPIRATION = "arm.inactivityexpiration.";

    public static boolean hasAnySubregionCreatePermission(CommandSender sender) {
        return (sender.hasPermission(SUBREGION_CREATE_SELL)) || (sender.hasPermission(SUBREGION_CREATE_RENT)) || (sender.hasPermission(SUBREGION_CREATE_CONTRACT));
    }

    public static boolean hasAnySubregionPermission(CommandSender sender) {
        return hasAnySubregionCreatePermission(sender) || (sender.hasPermission(SUBREGION_TOOL)) || (sender.hasPermission(SUBREGION_DELETE_SOLD)) ||
                (sender.hasPermission(SUBREGION_SET_IS_HOTEL)) || (sender.hasPermission(SUBREGION_DELETE_AVAILABLE)) || (sender.hasPermission(SUBREGION_TP) ||
                (sender.hasPermission(SUBREGION_UNSELL)) || (sender.hasPermission(SUBREGION_RESTORE)));
    }
}
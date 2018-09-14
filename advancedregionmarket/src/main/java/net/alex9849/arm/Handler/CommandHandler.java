package net.alex9849.arm.Handler;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.ContractPreset;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements TabCompleter {

    private static final String SET_REGION_KIND = " (?i)setregionkind [^;\n ]+ [^;\n ]+";
    private static final String LIST_REGION_KIND = " (?i)listregionkinds";
    private static final String FIND_REGION_BY_TYPE = " (?i)findfreeregion [^;\n ]+";
    private static final String RESET_REGION_BLOCKS = " (?i)resetblocks [^;\n ]+";
    private static final String RESET_REGION = " (?i)reset [^;\n ]+";
    private static final String REGION_INFO_WITHOUT_REGIONNAME = " (?i)info";
    private static final String REGION_INFO_WITH_REGIONNAME = " (?i)info [^;\n ]+";
    private static final String ADDMEMBER_WITH_REGIONNAME = " (?i)addmember [^;\n ]+ [^;\n ]+";
    private static final String REMOVEMEMBER_WITH_REGIONNAME = " (?i)removemember [^;\n ]+ [^;\n ]+";
    private static final String CHANGE_AUTORESET = " (?i)autoreset [^;\n ]+ (false|true)";
    private static final String LIST_REGIONS_OTHER = " (?i)listregions [^;\n ]+";
    private static final String LIST_REGIONS = " (?i)listregions";
    private static final String SET_OWNER = " (?i)setowner [^;\n ]+ [^;\n ]+";
    private static final String SET_ALLOW_ONLY_NEW_BLOCKS = " (?i)hotel [^;\n ]+ (false|true)";
    private static final String CREATE_NEW_SCHEMATIC = " (?i)updateschematic [^;\n ]+";
    private static final String TELEPORT = " (?i)tp [^;\n ]+";
    private static final String SET_WARP = " (?i)setwarp [^;\n ]+";
    private static final String UNSELL = " (?i)unsell [^;\n ]+";
    private static final String EXTEND = " (?i)extend [^;\n ]+";
    private static final String DELETE = " (?i)delete [^;\n ]+";
    private static final String SET_DO_BLOCK_RESET = " (?i)doblockreset [^;\n ]+ (false|true)";
    private static final String TERMINATE = " (?i)terminate [^;\n ]+ (false|true)";
    private static final String HELP = " (?i)help";
    private static final String RELOAD = " (?i)reload";
    private static final String SELLPRESET = " (?i)sellpreset [^;\n]+";
    private static final String RENTPRESET = " (?i)rentpreset [^;\n]+";
    private static final String CONTRACTPRESET = " (?i)contractpreset [^;\n]+";

    public boolean executeCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args){
        String allargs = "";
        for (int i = 0; i < args.length; i++) {
            allargs = allargs + " " + args[i];
        }

        if (cmd.getName().equalsIgnoreCase("arm")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("setregionkind")) {
                    if (allargs.matches(SET_REGION_KIND)) {                         //DONE
                        return Region.setRegionKindCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setregionkind [REGIONKIND] [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregionkinds")) {
                    if (allargs.matches(LIST_REGION_KIND)) {                  //Done
                        return Region.listRegionKindsCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregionkinds");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("findfreeregion")) {
                    if (allargs.matches(FIND_REGION_BY_TYPE)) {              //DONE
                        if (sender instanceof Player) {
                            return Region.teleportToFreeRegionCommand(args[1], ((Player) sender).getPlayer());
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm findfreeregion [REGIONKIND]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (allargs.matches(HELP)) {                            //DONE
                        return AdvancedRegionMarket.help(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("resetblocks")) {
                    if (allargs.matches(RESET_REGION_BLOCKS)) {             //DONE
                        return Region.resetRegionBlocksCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm resetblocks [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (allargs.matches(RESET_REGION)) {                    //DONE
                        return Region.resetRegionCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm reset [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (allargs.matches(REGION_INFO_WITHOUT_REGIONNAME)) {   //DONE
                        return Region.regionInfoCommand(sender);
                    } else if (allargs.matches(REGION_INFO_WITH_REGIONNAME)) {      //DONE
                        return Region.regionInfoCommand(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm info [REGION] or /arm info");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("addmember")) {
                    if (allargs.matches(ADDMEMBER_WITH_REGIONNAME)) {        //DONE
                        return Region.addMemberCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm addmember [REGION] [NEWMEMBER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("removemember")) {
                    if (allargs.matches(REMOVEMEMBER_WITH_REGIONNAME)) {      //DONE
                        return Region.removeMemberCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm removemember [REGION] [OLDMEMBER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("gui")) {
                    if (allargs.matches(" gui")) {
                        if (sender instanceof Player) {
                            if (sender.hasPermission(Permission.MEMBER_GUI)) {
                                Gui.openARMGui((Player) sender);
                            } else {
                                sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                            }
                            return true;
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm gui");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("autoreset")) {
                    if (allargs.matches(CHANGE_AUTORESET)) {
                        return Region.changeAutoresetCommand(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm autoreset [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregions")) {
                    if (allargs.matches(LIST_REGIONS_OTHER)) {
                        return Region.listRegionsCommand(sender, args[1]);
                    } else if (allargs.matches(LIST_REGIONS)) {
                        return Region.listRegionsCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregions or /arm listregions [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("hotel")) {
                    if (allargs.matches(SET_ALLOW_ONLY_NEW_BLOCKS)) {
                        return Region.setHotel(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm hotel [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("updateschematic")) {
                    if (allargs.matches(CREATE_NEW_SCHEMATIC)) {
                        return Region.createNewSchematic(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm updateschematic [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setowner")) {
                    if (allargs.matches(SET_OWNER)) {
                        return Region.setRegionOwner(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setowner [REGION] [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("tp")) {
                    if (allargs.matches(TELEPORT)) {
                        return Region.teleport(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm tp [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("limit")) {
                    if (allargs.matches(" limit")) {
                        if (sender.hasPermission(Permission.MEMBER_LIMIT)) {
                            LimitGroup.getLimitCommand(sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm limit");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setwarp")) {
                    if (allargs.matches(SET_WARP)) {
                        if (sender.hasPermission(Permission.ADMIN_SETWARP)) {
                            Region.setTeleportLocation(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setwarp [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (allargs.matches(RELOAD)) {
                        if (sender.hasPermission(Permission.ADMIN_RELOAD)) {
                            sender.sendMessage(Messages.PREFIX + "Reloading...");
                            AdvancedRegionMarket.getARM().onDisable();
                            Bukkit.getServer().getPluginManager().getPlugin("AdvancedRegionMarket").reloadConfig();
                            AdvancedRegionMarket.getARM().onEnable();
                            sender.sendMessage(Messages.PREFIX + "Complete!");
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm reload");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("unsell")) {
                    if (allargs.matches(UNSELL)) {
                        if (sender.hasPermission(Permission.ADMIN_UNSELL)) {
                            Region.unsellCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm unsell [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("extend")) {
                    if (allargs.matches(EXTEND)) {
                        if (sender.hasPermission(Permission.ARM_BUY_RENTREGION)) {
                            Region.extendCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm extend [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (allargs.matches(DELETE)) {
                        if (sender.hasPermission(Permission.ADMIN_DELETEREGION)) {
                            Region.deleteCommand(args[1], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm delete [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("doblockreset")) {
                    if (allargs.matches(SET_DO_BLOCK_RESET)) {
                        if (sender.hasPermission(Permission.ADMIN_CHANGE_DO_BLOCK_RESET)) {
                            Region.setDoBlockResetCommand(args[1], args[2], sender);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm doblockreset [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("sellpreset")) {
                    if (allargs.matches(SELLPRESET)) {
                        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
                            SellPreset.onCommand(sender, allargs, args);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm sellpreset [SETTING]");
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "or (for help): /arm sellpreset help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("rentpreset")) {
                    if (allargs.matches(RENTPRESET)) {
                        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
                            RentPreset.onCommand(sender, allargs, args);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm rentpreset [SETTING]");
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "or (for help): /arm rentpreset help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("contractpreset")) {
                    if (allargs.matches(CONTRACTPRESET)) {
                        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
                            ContractPreset.onCommand(sender, allargs, args);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm contractpreset [SETTING]");
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "or (for help): /arm contractpreset help");
                        return true;
                    }
                }else if (args[0].equalsIgnoreCase("terminate")) {
                    if (allargs.matches(TERMINATE)) {
                        if (sender.hasPermission(Permission.ARM_BUY_CONTRACTREGION)) {
                            ContractRegion.terminateCommand(sender, args[1], args[2]);
                        } else {
                            sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
                        }
                        return true;
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm terminate [REGION] [true/false]");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandsLabel, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(!(commandSender instanceof Player)) {
            return returnme;
        }

        Player player = (Player) commandSender;

        if(command.getName().equalsIgnoreCase("arm")) {
            if(args.length == 1) {
                returnme.add("Test1");
                returnme.add("Test2");
            }
        }
        return returnme;
    }
}

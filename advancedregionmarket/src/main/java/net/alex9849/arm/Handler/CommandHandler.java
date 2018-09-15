package net.alex9849.arm.Handler;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.ContractPreset;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.Diagram;
import net.alex9849.arm.regions.ContractRegion;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHandler implements TabCompleter {

    private static final String REGEX_SET_REGION_KIND = " (?i)setregionkind [^;\n ]+ [^;\n ]+";
    private static final String REGEX_LIST_REGION_KIND = " (?i)listregionkinds";
    private static final String REGEX_FIND_REGION_BY_TYPE = " (?i)findfreeregion [^;\n ]+";
    private static final String REGEX_RESET_REGION_BLOCKS = " (?i)resetblocks [^;\n ]+";
    private static final String REGEX_RESET_REGION = " (?i)reset [^;\n ]+";
    private static final String REGEX_REGION_INFO_WITHOUT_REGIONNAME = " (?i)info";
    private static final String REGEX_REGION_INFO_WITH_REGIONNAME = " (?i)info [^;\n ]+";
    private static final String REGEX_ADDMEMBER_WITH_REGIONNAME = " (?i)addmember [^;\n ]+ [^;\n ]+";
    private static final String REGEX_REMOVEMEMBER_WITH_REGIONNAME = " (?i)removemember [^;\n ]+ [^;\n ]+";
    private static final String REGEX_CHANGE_AUTORESET = " (?i)autoreset [^;\n ]+ (false|true)";
    private static final String REGEX_LIST_REGIONS_OTHER = " (?i)listregions [^;\n ]+";
    private static final String REGEX_LIST_REGIONS = " (?i)listregions";
    private static final String REGEX_SET_OWNER = " (?i)setowner [^;\n ]+ [^;\n ]+";
    private static final String REGEX_SET_ALLOW_ONLY_NEW_BLOCKS = " (?i)hotel [^;\n ]+ (false|true)";
    private static final String REGEX_CREATE_NEW_SCHEMATIC = " (?i)updateschematic [^;\n ]+";
    private static final String REGEX_TELEPORT = " (?i)tp [^;\n ]+";
    private static final String REGEX_SET_WARP = " (?i)setwarp [^;\n ]+";
    private static final String REGEX_UNSELL = " (?i)unsell [^;\n ]+";
    private static final String REGEX_EXTEND = " (?i)extend [^;\n ]+";
    private static final String REGEX_DELETE = " (?i)delete [^;\n ]+";
    private static final String REGEX_SET_DO_BLOCK_RESET = " (?i)doblockreset [^;\n ]+ (false|true)";
    private static final String REGEX_TERMINATE = " (?i)terminate [^;\n ]+ (false|true)";
    private static final String REGEX_HELP = " (?i)help";
    private static final String REGEX_RELOAD = " (?i)reload";
    private static final String REGEX_SELLPRESET = " (?i)sellpreset [^;\n]+";
    private static final String REGEX_RENTPRESET = " (?i)rentpreset [^;\n]+";
    private static final String REGEX_CONTRACTPRESET = " (?i)contractpreset [^;\n]+";
    private static final String REGEX_REGION_STATS_WITH_ARGS = " (?i)regionstats [^;\n]+";
    private static final String REGEX_REGION_STATS = " (?i)regionstats";
    private Boolean completeRegions = false;

    private enum PlayerRegionStatus {
            ALL, MEMBER, OWNER, MEMBER_OR_OWNER;
    }

    public CommandHandler(Boolean completeRegions) {
        this.completeRegions = completeRegions;
    }

    public boolean executeCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) throws InputException {
        String allargs = "";
        for (int i = 0; i < args.length; i++) {
            allargs = allargs + " " + args[i];
        }

        if (cmd.getName().equalsIgnoreCase("arm")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("setregionkind")) {
                    if (allargs.matches(REGEX_SET_REGION_KIND)) {                         //DONE
                        return Region.setRegionKindCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setregionkind [REGIONKIND] [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregionkinds")) {
                    if (allargs.matches(REGEX_LIST_REGION_KIND)) {                  //Done
                        return Region.listRegionKindsCommand(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregionkinds");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("findfreeregion")) {
                    if (allargs.matches(REGEX_FIND_REGION_BY_TYPE)) {              //DONE
                        if (sender instanceof Player) {
                            return Region.teleportToFreeRegionCommand(args[1], ((Player) sender).getPlayer());
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm findfreeregion [REGIONKIND]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (allargs.matches(REGEX_HELP)) {                            //DONE
                        return AdvancedRegionMarket.help(sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm help");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("resetblocks")) {
                    if (allargs.matches(REGEX_RESET_REGION_BLOCKS)) {             //DONE
                        return Region.resetRegionBlocksCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm resetblocks [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (allargs.matches(REGEX_RESET_REGION)) {                    //DONE
                        return Region.resetRegionCommand(args[1], sender);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm reset [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (allargs.matches(REGEX_REGION_INFO_WITHOUT_REGIONNAME)) {   //DONE
                        return Region.regionInfoCommand(sender);
                    } else if (allargs.matches(REGEX_REGION_INFO_WITH_REGIONNAME)) {      //DONE
                        return Region.regionInfoCommand(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm info [REGION] or /arm info");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("addmember")) {
                    if (allargs.matches(REGEX_ADDMEMBER_WITH_REGIONNAME)) {        //DONE
                        return Region.addMemberCommand(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm addmember [REGION] [NEWMEMBER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("removemember")) {
                    if (allargs.matches(REGEX_REMOVEMEMBER_WITH_REGIONNAME)) {      //DONE
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
                    if (allargs.matches(REGEX_CHANGE_AUTORESET)) {
                        return Region.changeAutoresetCommand(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm autoreset [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("listregions")) {
                    if (allargs.matches(REGEX_LIST_REGIONS)) {
                        return Region.listRegionsCommand(sender);
                    } else if (allargs.matches(REGEX_LIST_REGIONS_OTHER)) {
                        return Region.listRegionsCommand(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm listregions or /arm listregions [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("hotel")) {
                    if (allargs.matches(REGEX_SET_ALLOW_ONLY_NEW_BLOCKS)) {
                        return Region.setHotel(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm hotel [REGION] [true/false]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("updateschematic")) {
                    if (allargs.matches(REGEX_CREATE_NEW_SCHEMATIC)) {
                        return Region.createNewSchematic(sender, args[1]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm updateschematic [REGION]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setowner")) {
                    if (allargs.matches(REGEX_SET_OWNER)) {
                        return Region.setRegionOwner(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm setowner [REGION] [PLAYER]");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("tp")) {
                    if (allargs.matches(REGEX_TELEPORT)) {
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
                    if (allargs.matches(REGEX_SET_WARP)) {
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
                    if (allargs.matches(REGEX_RELOAD)) {
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
                    if (allargs.matches(REGEX_UNSELL)) {
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
                    if (allargs.matches(REGEX_EXTEND)) {
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
                    if (allargs.matches(REGEX_DELETE)) {
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
                    if (allargs.matches(REGEX_SET_DO_BLOCK_RESET)) {
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
                    if (allargs.matches(REGEX_SELLPRESET)) {
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
                    if (allargs.matches(REGEX_RENTPRESET)) {
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
                    if (allargs.matches(REGEX_CONTRACTPRESET)) {
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
                } else if (args[0].equalsIgnoreCase("terminate")) {
                    if (allargs.matches(REGEX_TERMINATE)) {
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
                } else if (args[0].equalsIgnoreCase("regionstats")) {
                    if (sender.hasPermission(Permission.ADMIN_REGION_STATS)) {
                        if(allargs.matches(REGEX_REGION_STATS)) {
                            return Diagram.sendRegionStats(sender);
                        } else if (allargs.matches(REGEX_REGION_STATS_WITH_ARGS)) {
                            return Diagram.sendRegionStats(sender, args[1]);
                        } else {
                            sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use: /arm regionstats [RegionKind/Nothing]");
                            return true;
                        }
                    } else {
                        sender.sendMessage(Messages.PREFIX + Messages.NO_PERMISSION);
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

        for(int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        if(command.getName().equalsIgnoreCase("arm")) {
            if (args.length >= 1) {
                if ("setregionkind".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_SETREGIONKIND))
                        returnme.addAll(onTabCompleteSetRegionKind(player, args));
                }
                if ("findfreeregion".startsWith(args[0])) {
                    returnme.addAll(onTabCompleteFindFreeRegion(player, args));
                }
                if ("autoreset".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_CHANGEAUTORESET)) {
                        returnme.addAll(onTabompleteAutoreset(player, args));
                    }
                }
                if ("hotel".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_CHANGE_IS_HOTEL)) {
                        returnme.addAll(onTabompleteHotel(player, args));
                    }
                }
                if ("doblockreset".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_CHANGE_DO_BLOCK_RESET)) {
                        returnme.addAll(onTabompleteDoBlockReset(player, args));
                    }
                }
                if ("terminate".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT) || player.hasPermission(Permission.ARM_BUY_CONTRACTREGION)) {
                        returnme.addAll(onTabompleteTerminate(player, args));
                    }
                }
                if ("delete".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_DELETEREGION)) {
                        returnme.addAll(onTabompleteDelete(player, args));
                    }
                }
                if ("extend".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_EXTEND) || player.hasPermission(Permission.ARM_BUY_RENTREGION)) {
                        returnme.addAll(onTabompleteExtend(player, args));
                    }
                }
                if ("unsell".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_UNSELL)) {
                        returnme.addAll(onTabompleteUnsell(player, args));
                    }
                }
                if ("setwarp".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_SETWARP)) {
                        returnme.addAll(onTabompleteSetWarp(player, args));
                    }
                }
                if ("tp".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_TP) || player.hasPermission(Permission.MEMBER_TP)) {
                        returnme.addAll(onTabompleteTP(player, args));
                    }
                }
                if ("updateschematic".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_UPDATESCHEMATIC)) {
                        returnme.addAll(onTabompleteUpdateSchematic(player, args));
                    }
                }
                if ("resetblocks".startsWith(args[0])) {
                    if (player.hasPermission(Permission.MEMBER_RESETREGIONBLOCKS) || player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
                        returnme.addAll(onTabompleteResetBlocks(player, args));
                    }
                }
                if("reset".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_RESETREGION)) {
                        returnme.addAll(onTabompleteReset(player, args));
                    }
                }
                if ("addmember".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_ADDMEMBER) || player.hasPermission(Permission.MEMBER_ADDMEMBER)) {
                        returnme.addAll(onTabompleteAddMember(player, args));
                    }
                }
                if ("setowner".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_SETOWNER)) {
                        returnme.addAll(onTabompleteSetOwner(player, args));
                    }
                }
                if ("removemember".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_REMOVEMEMBER) || player.hasPermission(Permission.MEMBER_REMOVEMEMBER)) {
                        returnme.addAll(onTabompleteRemoveMember(player, args));
                    }
                }
                if("info".startsWith(args[0])) {
                    if(player.hasPermission(Permission.ADMIN_INFO) || player.hasPermission(Permission.MEMBER_INFO)) {
                        returnme.addAll(onTabompleteInfo(player, args));
                    }
                }
                if ("listregions".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_LISTREGIONS) || player.hasPermission(Permission.MEMBER_LISTREGIONS)) {
                        returnme.addAll(onTabompleteListRegions(player, args));
                    }
                }
                if ("sellpreset".startsWith(args[0]) || "rentpreset".startsWith(args[0]) || "contractpreset".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_PRESET)) {
                        returnme.addAll(onTabCompletePreset(player, args));
                    }
                }
                if ("regionstats".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_REGION_STATS)) {
                        returnme.addAll(onTabompleteRegionStats(player, args));
                    }
                }
            }
            if(args.length == 1) {
                if ("listregionkinds".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_LISTREGIONKINDS)) {
                        returnme.add("listregionkinds");
                    }
                }
                if ("help".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ARM_HELP)) {
                        returnme.add("help");
                    }
                }
                if ("reload".startsWith(args[0])) {
                    if (player.hasPermission(Permission.ADMIN_RELOAD)) {
                        returnme.add("reload");
                    }
                }
                if ("gui".startsWith(args[0])) {
                    if (player.hasPermission(Permission.MEMBER_GUI)) {
                        returnme.add("gui");
                    }
                }
                if ("limit".startsWith(args[0])) {
                    if (player.hasPermission(Permission.MEMBER_LIMIT)) {
                        returnme.add("limit");
                    }
                }
            }
        }
        return returnme;
    }

    private List<String> onTabCompletePreset(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            if("sellpreset".startsWith(args[0])) {
                returnme.add("sellpreset");
            } else if("rentpreset".startsWith(args[0])) {
                returnme.add("rentpreset");
            } else if("contractpreset".startsWith(args[0])) {
                returnme.add("contractpreset");
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("sellpreset") || args[0].equalsIgnoreCase("rentpreset") || args[0].equalsIgnoreCase("contractpreset")) {
                if(player.hasPermission(Permission.ADMIN_PRESET_LIST)) {
                    if("list".startsWith(args[1])) {
                        returnme.add("list");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_INFO)) {
                    if("info".startsWith(args[1])) {
                        returnme.add("info");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_RESET)) {
                    if("reset".startsWith(args[1])) {
                        returnme.add("reset");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
                    if("load".startsWith(args[1])) {
                        returnme.add("load");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SAVE)) {
                    if("save".startsWith(args[1])) {
                        returnme.add("save");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_PRICE)) {
                    if("price".startsWith(args[1])) {
                        returnme.add("price");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
                    if("delete".startsWith(args[1])) {
                        returnme.add("delete");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
                    if("regionkind".startsWith(args[1])) {
                        returnme.add("regionkind");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_AUTORESET)) {
                    if("autoreset".startsWith(args[1])) {
                        returnme.add("autoreset");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_HOTEL)) {
                    if("hotel".startsWith(args[1])) {
                        returnme.add("hotel");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_DOBLOCKRESET)) {
                    if("doblockreset".startsWith(args[1])) {
                        returnme.add("doblockreset");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("rentpreset")) {
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_MAXRENTTIME)) {
                    if("maxrenttime".startsWith(args[1])) {
                        returnme.add("maxrenttime");
                    }
                }
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_EXTENDPERCLICK)) {
                    if("extendperclick".startsWith(args[1])) {
                        returnme.add("extendperclick");
                    }
                }
            }
            if(args[0].equalsIgnoreCase("contractpreset")) {
                if(player.hasPermission(Permission.ADMIN_PRESET_SET_EXTEND)) {
                    if("extend".startsWith(args[1])) {
                        returnme.add("extend");
                    }
                }
            }
        }
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("sellpreset") || args[0].equalsIgnoreCase("rentpreset") || args[0].equalsIgnoreCase("contractpreset")) {
                if(args[1].equalsIgnoreCase("load")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_LOAD)) {
                        returnme.addAll(onTabCompleteCompleteSavedPresets(args));
                    }
                }
                if(args[1].equalsIgnoreCase("delete")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
                        returnme.addAll(onTabCompleteCompleteSavedPresets(args));
                    }
                }
                if(args[1].equalsIgnoreCase("price")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_PRICE)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("regionkind")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_REGIONKIND)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                            if(regionkind.getName().startsWith(args[2])) {
                                returnme.add(regionkind.getName());
                            }
                        }
                        if("default".startsWith(args[2])) {
                            returnme.add("default");
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("autoreset")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_AUTORESET)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("hotel")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_HOTEL)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("doblockreset")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_DOBLOCKRESET)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if("true".startsWith(args[2])) {
                            returnme.add("true");
                        }
                        if("false".startsWith(args[2])) {
                            returnme.add("false");
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("rentpreset")) {
                if(args[1].equalsIgnoreCase("maxrenttime")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_MAXRENTTIME)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if(args[2].matches("[0-9]+")) {
                            returnme.add(args[2] + "s");
                            returnme.add(args[2] + "m");
                            returnme.add(args[2] + "h");
                            returnme.add(args[2] + "d");
                        }
                    }
                }
                if(args[1].equalsIgnoreCase("extendperclick")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_EXTENDPERCLICK)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if(args[2].matches("[0-9]+")) {
                            returnme.add(args[2] + "s");
                            returnme.add(args[2] + "m");
                            returnme.add(args[2] + "h");
                            returnme.add(args[2] + "d");
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("contractpreset")) {
                if(args[1].equalsIgnoreCase("extend")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_SET_EXTEND)) {
                        if("remove".startsWith(args[2])) {
                            returnme.add("remove");
                        }
                        if(args[2].matches("[0-9]+")) {
                            returnme.add(args[2] + "s");
                            returnme.add(args[2] + "m");
                            returnme.add(args[2] + "h");
                            returnme.add(args[2] + "d");
                        }
                    }
                }
            }
        }

        return returnme;
    }

    private List<String> onTabCompleteCompleteSavedPresets(String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args[0].equalsIgnoreCase("sellpreset")) {
            for(SellPreset preset: SellPreset.getPatterns()) {
                if(preset.getName().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(args[0].equalsIgnoreCase("rentpreset")) {
            for(RentPreset preset: RentPreset.getPatterns()) {
                if(preset.getName().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(args[0].equalsIgnoreCase("contractpreset")) {
            for(ContractPreset preset: ContractPreset.getPatterns()) {
                if(preset.getName().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        return returnme;
    }

    private List<String> onTabCompleteSetRegionKind(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("setregionkind");
        } else if (args.length == 2) {
            for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                if(regionkind.getName().startsWith(args[1])) {
                    returnme.add(regionkind.getName());
                }
            }
            if("default".startsWith(args[1])) {
                returnme.add("default");
            }
        } else if(args.length == 3) {
            returnme.addAll(this.completeRegions(player, args[2], PlayerRegionStatus.ALL));
        }
        return returnme;
    }

    private List<String> onTabCompleteFindFreeRegion(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();

        if(args.length == 1) {
            Boolean addffr = false;
            for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                if (player.hasPermission(Permission.ARM_BUYKIND + regionkind.getName())) {
                    addffr = true;
                }
            }
            if(player.hasPermission(Permission.ARM_BUYKIND + "default")) {
                addffr = true;
            }
            if(addffr) {
                returnme.add("findfreeregion");
            }
        } else if (args.length == 2) {
            for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                if(regionkind.getName().startsWith(args[1])) {
                    if(player.hasPermission(Permission.ARM_BUYKIND + regionkind.getName())) {
                        returnme.add(regionkind.getName());
                    }
                }
            }
            if("default".startsWith(args[1])) {
                if(player.hasPermission(Permission.ARM_BUYKIND + "default")) {
                    returnme.add("default");
                }
            }
        }
        return returnme;
    }

    private List<String> onTabompleteAutoreset(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("autoreset");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        } else if(args.length == 3) {
            if("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return returnme;
    }

    private List<String> onTabompleteHotel(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("hotel");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        } else if(args.length == 3) {
            if("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return  returnme;
    }

    private List<String> onTabompleteDelete(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("delete");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteReset(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("reset");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteUnsell(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("unsell");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteInfo(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("info");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteListRegions(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("listregions");
        } else if(args.length == 2) {
            if(player.hasPermission(Permission.ADMIN_LISTREGIONS)) {
                returnme.addAll(this.completeOnlinePlayers(args[1]));
            }
        }
        return  returnme;
    }

    private List<String> onTabompleteSetWarp(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("setwarp");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteUpdateSchematic(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("updateschematic");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        }
        return  returnme;
    }

    private List<String> onTabompleteExtend(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("extend");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_EXTEND)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        }
        return  returnme;
    }

    private List<String> onTabompleteAddMember(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("addmember");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        } else if(args.length == 3) {
            returnme.addAll(this.completeOnlinePlayers(args[2]));
        }
        return  returnme;
    }

    private List<String> onTabompleteRemoveMember(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("removemember");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_REMOVEMEMBER)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        } else if(args.length == 3) {
            if(this.completeRegions) {
                Region region = Region.searchRegionbyNameAndWorld(args[1], player.getWorld().getName());
                if(region != null) {
                    if(AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, region.getRegion()) || player.hasPermission(Permission.ADMIN_REMOVEMEMBER)) {
                        returnme.addAll(completeRegionMembers(args[2], region.getRegion()));
                    }
                }
            }
        }
        return  returnme;
    }

    private List<String> onTabompleteSetOwner(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("setowner");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_SETOWNER)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        } else if(args.length == 3) {
            returnme.addAll(this.completeOnlinePlayers(args[2]));
        }
        return  returnme;
    }

    private List<String> onTabompleteResetBlocks(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("resetblocks");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_RESETREGIONBLOCKS)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        }
        return  returnme;
    }

    private List<String> onTabompleteTP(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("tp");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_TP)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.MEMBER_OR_OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        }
        return  returnme;
    }

    private List<String> onTabompleteDoBlockReset(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("doblockreset");
        } else if(args.length == 2) {
            returnme.addAll(this.completeRegions(player, args[1], PlayerRegionStatus.ALL));
        } else if(args.length == 3) {
            if("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return  returnme;
    }

    private List<String> onTabompleteRegionStats(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("regionstats");
        } else if(args.length == 2) {
            for(RegionKind regionkind : RegionKind.getRegionKindList()) {
                if(regionkind.getName().startsWith(args[1])) {
                    returnme.add(regionkind.getName());
                }
            }
            if("default".startsWith(args[1])) {
                returnme.add("default");
            }
            if("rentregion".startsWith(args[1])) {
                returnme.add("rentregion");
            }
            if("sellregion".startsWith(args[1])) {
                returnme.add("sellregion");
            }
            if("contractregion".startsWith(args[1])) {
                returnme.add("contractregion");
            }
        }
        return  returnme;
    }

    private List<String> onTabompleteTerminate(Player player, String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 1) {
            returnme.add("terminate");
        } else if(args.length == 2) {
            PlayerRegionStatus playerRegionStatus = null;
            if(player.hasPermission(Permission.ADMIN_TERMINATE_CONTRACT)) {
                playerRegionStatus = PlayerRegionStatus.ALL;
            } else {
                playerRegionStatus = PlayerRegionStatus.OWNER;
            }
            returnme.addAll(this.completeRegions(player, args[1], playerRegionStatus));
        } else if(args.length == 3) {
            if("true".startsWith(args[2])) {
                returnme.add("true");
            }
            if("false".startsWith(args[2])) {
                returnme.add("false");
            }
        }
        return  returnme;
    }

    private List<String> completeRegions(Player player, String arg, PlayerRegionStatus playerstatus) {
        List<String> returnme = new ArrayList<>();

        if(this.completeRegions) {
            for(Region region : Region.getRegionList()) {
                if(region.getRegion().getId().startsWith(arg)) {
                    if(playerstatus == PlayerRegionStatus.OWNER) {
                        if(AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, region.getRegion())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerstatus == PlayerRegionStatus.MEMBER) {
                        if(AdvancedRegionMarket.getWorldGuardInterface().hasMember(player, region.getRegion())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerstatus == PlayerRegionStatus.MEMBER_OR_OWNER) {
                        if(AdvancedRegionMarket.getWorldGuardInterface().hasMember(player, region.getRegion()) || AdvancedRegionMarket.getWorldGuardInterface().hasOwner(player, region.getRegion())) {
                            returnme.add(region.getRegion().getId());
                        }
                    } else if (playerstatus == PlayerRegionStatus.ALL) {
                        returnme.add(region.getRegion().getId());
                    }
                }
            }
        }

        return returnme;
    }

    private List<String> completeOnlinePlayers(String args) {
        List<String> returnme = new ArrayList<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(player.getName().startsWith(args)) {
                returnme.add(player.getName());
            }
        }
        return returnme;
    }

    private List<String> completeRegionMembers(String args, ProtectedRegion region) {
        List<String> returnme = new ArrayList<>();

        List<UUID> uuidList = AdvancedRegionMarket.getWorldGuardInterface().getMembers(region);
        for(UUID uuids: uuidList) {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuids);
            if(oplayer != null) {
                if (oplayer.getName().startsWith(args)) {
                    returnme.add(oplayer.getName());
                }
            }
        }
        return  returnme;
    }

}

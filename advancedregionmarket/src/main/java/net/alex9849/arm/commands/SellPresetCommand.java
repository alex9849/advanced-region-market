package net.alex9849.arm.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.Preset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SellPresetCommand extends BasicArmCommand {

    private final String rootCommand = "sellpreset";
    private final String regex = "(?i)sellpreset [^;\n]+";
    private final String usage = "/arm sellpreset [SETTING] or (for help): /arm sellpreset help";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (sender.hasPermission(Permission.ADMIN_PRESET)) {
            return SellPreset.onCommand(sender, allargs, args);
        } else {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_PRESET)) {
                    returnme.addAll(onTabCompletePreset(player, args));
                }
            }
        }
        return returnme;
    }

    protected List<String> onTabCompletePreset(Player player, String args[]) {
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
                        returnme.addAll(Preset.onTabCompleteCompleteSavedPresets(args));
                    }
                }
                if(args[1].equalsIgnoreCase("delete")) {
                    if(player.hasPermission(Permission.ADMIN_PRESET_DELETE)) {
                        returnme.addAll(Preset.onTabCompleteCompleteSavedPresets(args));
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
                            if(regionkind.getName().toLowerCase().startsWith(args[2])) {
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

}

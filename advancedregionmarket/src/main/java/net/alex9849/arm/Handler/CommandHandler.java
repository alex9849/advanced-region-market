package net.alex9849.arm.Handler;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Group.LimitGroup;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.Preseter.ContractPreset;
import net.alex9849.arm.Preseter.RentPreset;
import net.alex9849.arm.Preseter.SellPreset;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.Diagram;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class CommandHandler implements TabCompleter {

    private List<BasicArmCommand> commands = new ArrayList<>();
    private static CommandHandler latestHandler;

    public CommandHandler() {
        this.loadCommands();
        CommandHandler.latestHandler = this;
    }

    public static CommandHandler getLatestHandler() {
        return latestHandler;
    }

    public List<BasicArmCommand> getCommands() {
        return this.commands;
    }

    private void loadCommands(){

        this.commands = new ArrayList<>();

        try {
            JarFile jarFile = new JarFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();

            while(jarEntryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                String jarEntryClassPath = jarEntry.getName().replace("/", ".");
                if(jarEntryClassPath.startsWith("net.alex9849.arm.commands") && jarEntryClassPath.endsWith(".class")) {
                    String loadClassPath = jarEntryClassPath.substring(0, jarEntryClassPath.length() - 6);
                    Class<?> commandClass = Class.forName(loadClassPath);
                    if(BasicArmCommand.class.isAssignableFrom(commandClass) && !Modifier.isAbstract(commandClass.getModifiers())) {
                        this.commands.add((BasicArmCommand) commandClass.newInstance());
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public boolean executeCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args) throws InputException {
        String allargs = "";

        for (int i = 0; i < args.length; i++) {
            if(i == 0) {
                allargs = args[i];
            } else {
                allargs = allargs + " " + args[i];
            }
        }

        if(cmd.getName().equalsIgnoreCase("arm") && (args.length >= 1)) {
            for(int i = 0; i < this.commands.size(); i++) {
                if(this.commands.get(i).getRootCommand().equalsIgnoreCase(args[0])) {
                    if(this.commands.get(i).matchesRegex(allargs)) {
                        return this.commands.get(i).runCommand(sender, cmd, commandsLabel, args, allargs);
                    } else {
                        List<String> syntax = this.commands.get(i).getUsage();
                        if(syntax.size() >= 1) {
                            String message = Messages.BAD_SYNTAX;

                            message = message.replace("%command%", "/" + commandsLabel + " " + syntax.get(0));
                            for(int x = 1; x < syntax.size(); x++) {
                                message = message + " " + Messages.BAD_SYNTAX_SPLITTER.replace("%command%", "/" + commandsLabel + " " + syntax.get(x));
                            }
                            sender.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + message);
                        }
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

        for(int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        if(command.getName().equalsIgnoreCase("arm")) {
            for(int i = 0; i < this.commands.size(); i++) {
                returnme.addAll(this.commands.get(i).onTabComplete(player, args));
            }
        }

        return returnme;
    }

    public static List<String> tabCompleteOnlinePlayers(String args) {
        List<String> returnme = new ArrayList<>();
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(player.getName().toLowerCase().startsWith(args)) {
                returnme.add(player.getName());
            }
        }
        return returnme;
    }

    public static List<String> tabCompleteRegionMembers(String args, ProtectedRegion region) {
        List<String> returnme = new ArrayList<>();

        List<UUID> uuidList = AdvancedRegionMarket.getWorldGuardInterface().getMembers(region);
        for(UUID uuids: uuidList) {
            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuids);
            if(oplayer != null) {
                if (oplayer.getName().toLowerCase().startsWith(args)) {
                    returnme.add(oplayer.getName());
                }
            }
        }
        return  returnme;
    }

}

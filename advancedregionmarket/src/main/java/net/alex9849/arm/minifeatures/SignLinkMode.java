package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Preseter.presets.Preset;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.RegionManager;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class SignLinkMode implements Listener {
    private static List<SignLinkMode> signLinkModeList = new ArrayList<>();
    private Player player;
    private Preset preset;
    private Sign sign;
    private WGRegion wgRegion;
    private World world;

    public SignLinkMode(Player player, Preset preset) throws InputException {
        if(preset == null) {
            //TODO Change message
            throw new InputException(player, "You have not selected a preset");
        }
        this.player = player;
        this.preset = preset;
    }

    @EventHandler
    private void playerLeave(PlayerQuitEvent event) {
        if(event.getPlayer().getUniqueId() == this.player.getUniqueId()) {
            this.unregister();
        }
    }

    @EventHandler
    private void playerClick(PlayerInteractEvent event) {
        try {
            if(!(event.getAction() == Action.LEFT_CLICK_BLOCK) && !(event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            if((event.getClickedBlock().getType() == Material.SIGN) || (event.getClickedBlock().getType() == Material.WALL_SIGN)) {
                sign = (Sign) event.getClickedBlock().getBlockData();
                player.sendMessage("Sign selected!");
            } else {
                Location clicklocation = event.getClickedBlock().getLocation();
                if(clicklocation == null) {
                    return;
                }
                List<WGRegion> regions = AdvancedRegionMarket.getWorldGuardInterface().getApplicableRegions(clicklocation.getWorld(), clicklocation, AdvancedRegionMarket.getWorldGuard());
                if(regions.size() > 1) {
                    throw new InputException(event.getPlayer(), "Could not select WorldGuard-Region! There are more then one regions available!");
                }
                if(regions.size() < 1) {
                    throw new InputException(event.getPlayer(), "Could not select WorldGuard-Region! There is no region at this position!");
                }
                this.wgRegion = regions.get(0);
                this.world = clicklocation.getWorld();
            }
            if((this.sign != null) && (this.wgRegion != null) && (this.world != null)) {
                this.registerRegion();
            }
        } catch (InputException e) {
            e.sendMessages();
        }


    }

    private void registerRegion() throws InputException {
        if(this.sign == null) {
            //TODO Change message
            throw new InputException(player, "You have not selected any signs");
        }
        if(this.wgRegion == null) {
            //TODO Change message
            throw new InputException(player, "You have not selected a WorldGuard-Region");
        }
        if(this.world == null) {
            //TODO Change message
            throw new InputException(player, "Could not identify world! Please select the WorldGuard-Region again!");
        }
        List<Sign> signs = new ArrayList<>();
        signs.add(this.sign);
        Region existingRegion = RegionManager.getRegion(wgRegion);
        if(existingRegion != null) {
            existingRegion.addSign(this.sign.getLocation());
            this.player.sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
        } else {
            Region newRegion = this.preset.generateRegion(this.wgRegion, this.world, signs);
            RegionManager.addRegion(newRegion);
            this.player.sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
        }
    }

    public void unregister() {
        SignLinkMode.signLinkModeList.remove(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
    }

    private static void reset() {
        SignLinkMode.signLinkModeList = new ArrayList<>();
    }

    public Player getPlayer() {
        return this.player;
    }

    public static SignLinkMode getSignLinkMode(Player player) {
        for(SignLinkMode slm : signLinkModeList) {
            if(slm.getPlayer().getUniqueId() == player.getUniqueId()) {
                return slm;
            }
        }
        return null;
    }

    public static boolean hasSignLinkMode(Player player) {
        return getSignLinkMode(player) != null;
    }

    private void register() {
        Bukkit.getServer().getPluginManager().registerEvents(this, AdvancedRegionMarket.getARM());
    }

    public static void register(SignLinkMode slm) {
        if(SignLinkMode.hasSignLinkMode(slm.getPlayer())) {
            SignLinkMode.getSignLinkMode(slm.getPlayer()).unregister();
        }
        signLinkModeList.add(slm);
        slm.register();
    }
}

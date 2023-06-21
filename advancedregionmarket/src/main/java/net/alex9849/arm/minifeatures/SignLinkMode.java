package net.alex9849.arm.minifeatures;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.exceptions.FeatureDisabledException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.presets.presets.Preset;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.signs.SignData;
import net.alex9849.inter.WGRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SignLinkMode implements Listener {
    private static List<SignLinkMode> signLinkModeList = new ArrayList<>();
    //by Hashed world and regionId
    private static HashMap<String, Set<String>> blacklistedRegions = new HashMap<>();
    private Player player;
    private Preset preset;
    private Sign sign;
    private WGRegion wgRegion;
    private World world;

    public SignLinkMode(Player player, Preset preset) throws InputException {
        if (preset == null) {
            throw new InputException(player, Messages.SIGN_LINK_MODE_NO_PRESET_SELECTED);
        }
        this.player = player;
        this.preset = preset;
    }

    public static void reset() {
        SignLinkMode.signLinkModeList = new ArrayList<>();
        SignLinkMode.blacklistedRegions = new HashMap<>();
    }

    public static SignLinkMode getSignLinkMode(Player player) {
        for (SignLinkMode slm : signLinkModeList) {
            if (slm.getPlayer().getUniqueId() == player.getUniqueId()) {
                return slm;
            }
        }
        return null;
    }

    public static boolean hasSignLinkMode(Player player) {
        return getSignLinkMode(player) != null;
    }

    public static void register(SignLinkMode slm) {
        if (SignLinkMode.hasSignLinkMode(slm.getPlayer())) {
            SignLinkMode.getSignLinkMode(slm.getPlayer()).unregister();
        }
        signLinkModeList.add(slm);
        slm.register();
    }

    public static void setBlacklistedRegions(HashMap<String, Set<String>> regions) {
        if (regions == null) {
            return;
        }
        SignLinkMode.blacklistedRegions = regions;
    }

    @EventHandler
    private void playerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId() == this.player.getUniqueId()) {
            this.unregister();
        }
    }

    @EventHandler
    private void playerClick(PlayerInteractEvent event) {
        if (event.getPlayer().getUniqueId() != this.player.getUniqueId()) {
            return;
        }
        AdvancedRegionMarket plugin = AdvancedRegionMarket.getInstance();
        try {
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            if ((!(event.getAction() == Action.LEFT_CLICK_BLOCK)) && (!(event.getAction() == Action.RIGHT_CLICK_BLOCK))) {
                return;
            }
            List<Material> signMaterials = plugin.getMaterialFinder().getSignMaterials();
            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
            Material clickedBlock = event.getClickedBlock().getType();

            if (event.getAction() == Action.LEFT_CLICK_BLOCK && signMaterials.contains(clickedBlock)) {
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && itemInHand != null) {
                if (signMaterials.contains(itemInHand.getType()) && !signMaterials.contains(clickedBlock)) {
                    return;
                }
            }
            event.setCancelled(true);
            if (plugin.getMaterialFinder().getSignMaterials().contains(event.getClickedBlock().getType())) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (plugin.getRegionManager().getRegion(sign) != null) {
                    throw new InputException(event.getPlayer(), Messages.SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION);
                }
                this.sign = sign;
                player.sendMessage(Messages.SIGN_LINK_MODE_SIGN_SELECTED);
            } else {
                Location clicklocation = event.getClickedBlock().getLocation();
                this.wgRegion = getRegionFromClickedLocation(player, clicklocation, event.getBlockFace());
                this.world = clicklocation.getWorld();
                this.player.sendMessage(Messages.PREFIX + Messages.SIGN_LINK_MODE_REGION_SELECTED.replace("%regionid%", this.wgRegion.getId()));
            }
            if ((this.sign != null) && (this.wgRegion != null) && (this.world != null)) {
                this.registerRegion();
            }
        } catch (InputException e) {
            e.sendMessages(Messages.PREFIX);
        }
    }

    private WGRegion getRegionFromClickedLocation(Player player, Location location, BlockFace blockFace) throws InputException {
        List<WGRegion> regions = AdvancedRegionMarket.getInstance().getWorldGuardInterface().getApplicableRegions(location.getWorld(), location);
        Set<String> worldBlacklisted = SignLinkMode.blacklistedRegions.getOrDefault(location.getWorld().getName(), new HashSet<>());
        regions.removeIf(x -> worldBlacklisted.contains(x.getId()));
        if (regions.size() > 1) {
            throw new InputException(player, Messages.SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_MULTIPLE_WG_REGIONS);
        }
        if (regions.size() < 1) {
            if (blockFace == null) {
                throw new InputException(player, Messages.SIGN_LINK_MODE_COULD_NOT_SELECT_REGION_NO_WG_REGION);
            }
            return getRegionFromClickedLocation(player, location.add(blockFace.getDirection()), null);
        }
        return regions.get(0);
    }

    private void registerRegion() throws InputException {
        if (this.sign == null) {
            throw new InputException(player, Messages.SIGN_LINK_MODE_NO_SIGN_SELECTED);
        }
        if (this.wgRegion == null) {
            throw new InputException(player, Messages.SIGN_LINK_MODE_NO_WG_REGION_SELECTED);
        }
        if (this.world == null) {
            throw new InputException(player, Messages.SIGN_LINK_MODE_COULD_NOT_IDENTIFY_WORLD);
        }
        if (AdvancedRegionMarket.getInstance().getRegionManager().getRegion(sign) != null) {
            throw new InputException(this.player, Messages.SIGN_LINK_MODE_SIGN_BELONGS_TO_ANOTHER_REGION);
        }
        List<SignData> signs = new ArrayList<>();
        SignData signData = AdvancedRegionMarket.getInstance().getSignDataFactory().generateSignData(this.sign.getLocation());
        if (signData == null) {
            throw new InputException(this.player, "Could not import sign!");
        }
        signs.add(signData);
        Region existingRegion = AdvancedRegionMarket.getInstance().getRegionManager().getRegion(wgRegion);
        if (existingRegion != null) {
            existingRegion.addSign(signData);
            existingRegion.queueSave();
            this.player.sendMessage(Messages.PREFIX + Messages.SIGN_ADDED_TO_REGION);
        } else {
            Region newRegion = this.preset.generateRegion(this.wgRegion, this.world, this.player, false, signs);
            newRegion.createSchematic();
            try {
                newRegion.applyFlagGroup(FlagGroup.ResetMode.COMPLETE, false);
            } catch (FeatureDisabledException e) {
                //Ignore
            }
            AdvancedRegionMarket.getInstance().getRegionManager().add(newRegion);
            this.preset.executeSetupCommands(this.player, newRegion);
            this.player.sendMessage(Messages.PREFIX + Messages.REGION_ADDED_TO_ARM);
        }
        this.sign = null;
    }

    public void unregister() {
        SignLinkMode.signLinkModeList.remove(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    private void register() {
        Bukkit.getServer().getPluginManager().registerEvents(this, AdvancedRegionMarket.getInstance());
    }
}

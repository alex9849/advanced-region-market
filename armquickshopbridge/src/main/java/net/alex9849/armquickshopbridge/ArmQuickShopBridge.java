package net.alex9849.armquickshopbridge;

import net.alex9849.arm.events.ResetBlocksEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmQuickShopBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new RegionResetHandler(), this);
    }

    @Override
    public void onDisable() {
        ResetBlocksEvent.getHandlerList().unregister(this);
    }
}

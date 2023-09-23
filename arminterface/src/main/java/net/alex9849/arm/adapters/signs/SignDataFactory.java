package net.alex9849.arm.adapters.signs;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public abstract class SignDataFactory {

    public abstract SignData generateSignData(Location loc);

    public abstract SignData generateSignData(Location loc, SignAttachment signAttachment, BlockFace blockFace);
}

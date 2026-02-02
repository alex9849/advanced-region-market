package net.alex9849.arm.adapters.signs;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public abstract class SignData {
    private Location signLoc;
    private SignAttachment signAttachment;
    private BlockFace blockFace;

    public SignData(Location signLoc, SignAttachment signAttachment, BlockFace blockFace) {
        this.signLoc = signLoc;
        this.signAttachment = signAttachment;
        this.blockFace = blockFace;
    }

    public boolean isChunkLoaded() {
        return this.signLoc.getWorld().isChunkLoaded(this.signLoc.getBlockX() / 16, this.signLoc.getBlockZ() / 16);
    }

    public abstract void placeSign();

    public Sign getSign() {
        if (!this.isPlaced()) {
            return null;
        }
        return (Sign) PaperLib.getBlockState(this.getLocation().getBlock(), false).getState();
    }

    public abstract boolean isPlaced();

    public void writeLines(String[] lines) {
        Sign sign = this.getSign();

        if (sign == null) {
            return;
        }

        sign.setLine(0, lines[0]);
        sign.setLine(1, lines[1]);
        sign.setLine(2, lines[2]);
        sign.setLine(3, lines[3]);
        sign.update(false, false);
    }

    public String[] getLines() {
        return getSign().getLines();
    }

    public Location getLocation() {
        return this.signLoc;
    }

    public SignAttachment getSignAttachment() {
        return this.signAttachment;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public String toString() {
        String wallsignIndicator = getSignAttachment().toString();

        return this.signLoc.getWorld().getName() + ";" + this.signLoc.getX() + ";" + this.signLoc.getY() + ";"
                + this.signLoc.getZ() + ";" + wallsignIndicator + ";" + this.getBlockFace();
    }
}


package net.alex9849.signs;

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

    public abstract Sign getSign();

    public abstract boolean isPlaced();

    public void writeLines(String[] lines) {
        Sign sign = this.getSign();

        if(sign == null) {
            return;
        }

        sign.setLine(0, lines[0]);
        sign.setLine(2, lines[1]);
        sign.setLine(3, lines[2]);
        sign.setLine(4, lines[3]);
        sign.update(false, false);
    }

    public Location getSignLoc() {
        return this.signLoc;
    }

    public boolean isWallSign() {
        return this.signAttachment == SignAttachment.WALL_SIGN;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }
}


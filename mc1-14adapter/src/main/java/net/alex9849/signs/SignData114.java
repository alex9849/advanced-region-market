package net.alex9849.signs;

import net.alex9849.arm.util.MaterialFinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;

public class SignData114 extends SignData {

    public SignData114(Location signLoc, SignAttachment signAttachment, BlockFace blockFace) {
        super(signLoc, signAttachment, blockFace);
    }

    public void placeSign() {
        Material signMaterial;
        Location signLoc = this.getLocation();

        if (this.isWallSign()) {
            signMaterial = MaterialFinder.getWallSign();
            signLoc.getBlock().setType(signMaterial, false);
            WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
            wallSign.setFacing(this.getBlockFace());
            signLoc.getBlock().setBlockData(wallSign, false);
        } else {
            signMaterial = MaterialFinder.getSign();
            signLoc.getBlock().setType(signMaterial, false);
            Sign sign = (Sign) signLoc.getBlock().getBlockData();
            sign.setRotation(this.getBlockFace());
            signLoc.getBlock().setBlockData(sign, false);
        }

        signLoc.getBlock().getState().update(false, false);
    }
}

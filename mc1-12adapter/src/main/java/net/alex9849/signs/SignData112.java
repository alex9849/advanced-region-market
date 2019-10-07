package net.alex9849.signs;

import net.alex9849.arm.util.MaterialFinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public class SignData112 extends SignData {
    public SignData112(Location signLoc, SignAttachment signAttachment, BlockFace blockFace) {
        super(signLoc, signAttachment, blockFace);
    }

    @Override
    public void placeSign() {
        Material signMaterial;
        Location signLoc = this.getLocation();

        if (this.isWallSign()) {
            signMaterial = MaterialFinder.getWallSign();
        } else {
            signMaterial = MaterialFinder.getSign();
        }

        signLoc.getBlock().setType(signMaterial, false);
        BlockState signState = getLocation().getBlock().getState();
        org.bukkit.material.Sign sign = (org.bukkit.material.Sign) signState.getData();
        sign.setFacingDirection(this.getBlockFace());
        signState.setData(sign);
        signState.update(false, false);
    }
}

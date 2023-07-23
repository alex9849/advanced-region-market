package net.alex9849.arm.adapters.signs;

import net.alex9849.arm.adapters.util.MaterialFinder113;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;

public class SignData113 extends SignData {
    public SignData113(Location signLoc, SignAttachment signAttachment, BlockFace blockFace) {
        super(signLoc, signAttachment, blockFace);
    }

    @Override
    public void placeSign() {
        Material signMaterial;
        Location signLoc = this.getLocation();

        if (this.getSignAttachment() == SignAttachment.WALL) {
            signMaterial = MaterialFinder113.getInstance().getWallSign();
            signLoc.getBlock().setType(signMaterial, false);
            WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
            wallSign.setFacing(this.getBlockFace());
            signLoc.getBlock().setBlockData(wallSign, false);
        } else {
            signMaterial = MaterialFinder113.getInstance().getSign();
            signLoc.getBlock().setType(signMaterial, false);
            Sign sign = (Sign) signLoc.getBlock().getBlockData();
            sign.setRotation(this.getBlockFace());
            signLoc.getBlock().setBlockData(sign, false);
        }

        signLoc.getBlock().getState().update(false, false);
    }

    @Override
    public boolean isPlaced() {
        return MaterialFinder113.getInstance().getSignMaterials().contains(this.getLocation().getBlock().getType());
    }
}

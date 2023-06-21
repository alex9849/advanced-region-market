package net.alex9849.arm.signs;

import net.alex9849.arm.util.MaterialFinder120;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;

public class SignData120 extends SignData {

    public SignData120(Location signLoc, SignAttachment signAttachment, BlockFace blockFace) {
        super(signLoc, signAttachment, blockFace);
    }

    public void placeSign() {
        Material signMaterial;
        Location signLoc = this.getLocation();


        switch (this.getSignAttachment()) {
            case WALL:
                signMaterial = MaterialFinder120.getInstance().getWallSign();
                signLoc.getBlock().setType(signMaterial, false);
                WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
                wallSign.setFacing(this.getBlockFace());
                signLoc.getBlock().setBlockData(wallSign, false);
                break;
            case GROUND:
                signMaterial = MaterialFinder120.getInstance().getSign();
                signLoc.getBlock().setType(signMaterial, false);
                Sign sign = (Sign) signLoc.getBlock().getBlockData();
                sign.setRotation(this.getBlockFace());
                signLoc.getBlock().setBlockData(sign, false);
                break;
            case HANGING:
                signMaterial = MaterialFinder120.getInstance().getHangingSign();
                signLoc.getBlock().setType(signMaterial, false);
                HangingSign hangingSign = (HangingSign) signLoc.getBlock().getBlockData();
                hangingSign.setRotation(this.getBlockFace());
                signLoc.getBlock().setBlockData(hangingSign, false);
                break;
            case HANGING_WALL:
                signMaterial = MaterialFinder120.getInstance().getWallHangingSign();
                signLoc.getBlock().setType(signMaterial, false);
                WallHangingSign wallHangingSign = (WallHangingSign) signLoc.getBlock().getBlockData();
                wallHangingSign.setFacing(this.getBlockFace());
                signLoc.getBlock().setBlockData(wallHangingSign, false);
                break;
        }

        signLoc.getBlock().getState().update(false, false);
    }

    @Override
    public boolean isPlaced() {
        return MaterialFinder120.getInstance().getSignMaterials().contains(this.getLocation().getBlock().getType());
    }
}

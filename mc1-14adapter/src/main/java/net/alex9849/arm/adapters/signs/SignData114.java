package net.alex9849.arm.adapters.signs;

import io.papermc.lib.PaperLib;
import net.alex9849.arm.adapters.util.MaterialFinder114;
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

        if (this.getSignAttachment() == SignAttachment.WALL) {
            signMaterial = MaterialFinder114.getInstance().getWallSign();
            signLoc.getBlock().setType(signMaterial, false);
            WallSign wallSign = (WallSign) signLoc.getBlock().getBlockData();
            wallSign.setFacing(this.getBlockFace());
            signLoc.getBlock().setBlockData(wallSign, false);
        } else {
            signMaterial = MaterialFinder114.getInstance().getSign();
            signLoc.getBlock().setType(signMaterial, false);
            Sign sign = (Sign) signLoc.getBlock().getBlockData();
            sign.setRotation(this.getBlockFace());
            signLoc.getBlock().setBlockData(sign, false);
        }

        PaperLib.getBlockState(this.getLocation().getBlock(), false).getState().update(false, false);
    }

    @Override
    public boolean isPlaced() {
        return MaterialFinder114.getInstance().getSignMaterials().contains(this.getLocation().getBlock().getType());
    }
}

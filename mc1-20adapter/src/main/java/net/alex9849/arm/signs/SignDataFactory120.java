package net.alex9849.arm.signs;

import net.alex9849.arm.util.MaterialFinder120;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;

public class SignDataFactory120 extends SignDataFactory {
    public SignData generateSignData(Location loc) {
        if (!MaterialFinder120.getInstance().getSignMaterials().contains(loc.getBlock().getType())) {
            return null;
        }

        BlockFace blockFace;
        SignAttachment signAttachment;
        BlockState blockState = loc.getBlock().getState();
        BlockData blockData = loc.getBlock().getBlockData();

        if (blockData instanceof Sign) {
            Sign sign = (Sign) blockData;
            blockFace = sign.getRotation();
            signAttachment = SignAttachment.GROUND;
        } else if (blockData instanceof HangingSign) {
            HangingSign hangingSign = (HangingSign) blockData;
            blockFace = hangingSign.getRotation();
            signAttachment = SignAttachment.HANGING;
        } else if (blockData instanceof WallHangingSign) {
            WallHangingSign hangingSign = (WallHangingSign) blockData;
            blockFace = hangingSign.getFacing();
            signAttachment = SignAttachment.HANGING;
        } else {
            WallSign wallSign = (WallSign) blockData;
            blockFace = wallSign.getFacing();
            signAttachment = SignAttachment.WALL;
        }

        return new SignData120(loc, signAttachment, blockFace);
    }

    @Override
    public SignData generateSignData(Location loc, SignAttachment signAttachment, BlockFace blockFace) {
        return new SignData120(loc, signAttachment, blockFace);
    }
}

package net.alex9849.arm.signs;

import net.alex9849.arm.util.MaterialFinder112;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;

public class SignDataFactory112 extends SignDataFactory {
    @Override
    public SignData generateSignData(Location loc) {
        if (!MaterialFinder112.getSignMaterials().contains(loc.getBlock().getType())) {
            return null;
        }

        Sign sign = (Sign) loc.getBlock().getState().getData();
        SignAttachment signAttachment;

        if (sign.isWallSign()) {
            signAttachment = SignAttachment.WALL;
        } else {
            signAttachment = SignAttachment.GROUND;
        }

        return new SignData112(loc, signAttachment, sign.getFacing());
    }

    @Override
    public SignData generateSignData(Location loc, SignAttachment signAttachment, BlockFace blockFace) {
        return new SignData112(loc, signAttachment, blockFace);
    }
}

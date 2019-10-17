package net.alex9849.arm.exceptions;

import net.alex9849.inter.WGRegion;

import java.io.IOException;

public class SchematicNotFoundException extends IOException {
    private WGRegion wgRegion;

    public SchematicNotFoundException(WGRegion wgregion) {
        this.wgRegion = wgregion;
    }

    public WGRegion getRegion() {
        return this.wgRegion;
    }
}

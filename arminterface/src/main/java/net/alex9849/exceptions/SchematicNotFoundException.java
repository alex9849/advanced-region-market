package net.alex9849.exceptions;

import net.alex9849.inter.WGRegion;

import java.io.FileNotFoundException;

public class SchematicNotFoundException extends FileNotFoundException {
    WGRegion wgRegion;

    public SchematicNotFoundException(WGRegion wgregion) {
        this.wgRegion = wgregion;
    }

    public WGRegion getRegion() {
        return this.wgRegion;
    }
}

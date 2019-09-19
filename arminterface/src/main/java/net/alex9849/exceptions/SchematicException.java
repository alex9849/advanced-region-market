package net.alex9849.exceptions;

import net.alex9849.inter.WGRegion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SchematicException extends IOException {
    private WGRegion wgRegion;

    public SchematicException(WGRegion wgregion) {
        this.wgRegion = wgregion;
    }

    public WGRegion getRegion() {
        return this.wgRegion;
    }
}

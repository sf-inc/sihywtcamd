package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "cosmetics")
public class CosmeticsConfig implements ConfigData {
    public boolean translucentAllay = true;
    public boolean translucentGhast = true;
    public boolean translucentPhantom = true;
    public boolean translucentVex = true;
}

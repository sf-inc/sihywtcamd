package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "nether")
public class NetherConfig implements ConfigData {
    public boolean blazeFireCollision = true;
    public boolean ghastIncreasedHealth = true;
    public boolean babyWitherSkeleton = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
    public int piglinGoldenArmor = 4;
    public boolean piglinRideHoglin = true;
}

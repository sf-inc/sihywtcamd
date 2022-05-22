package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "nether")
public class NetherConfig implements ConfigData {
    public boolean blazeFireCollision = true;
    public boolean ghastIncreasedHealth = true;

    @ConfigEntry.Gui.CollapsibleObject
    public Piglin piglin = new Piglin();

    public static class Piglin {
        @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
        public int goldenArmor = 4;
        public boolean rideHoglin = true;
    }
}

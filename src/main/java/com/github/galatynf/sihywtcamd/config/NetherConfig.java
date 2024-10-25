package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "nether")
public class NetherConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Blaze blaze = new Blaze();

    public static class Blaze {
        public boolean fireAttack = true;
        public boolean reducedFollowRange = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Ghast ghast = new Ghast();

    public static class Ghast {
        public boolean increasedHealth = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public MagmaCube magmaCube = new MagmaCube();

    public static class MagmaCube {
        public boolean fireCollision = true;
        public boolean slimeConversion = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Piglin piglin = new Piglin();

    public static class Piglin {
        @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
        public int goldenArmor = 4;
        public boolean rideHoglin = true;
    }
}

package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "overworld")
public class OverworldConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Creeper creeper = new Creeper();

    public static class Creeper {
        @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
        public int explosionFatigueMaxDuration = 60;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 300)
        public int explosionWeaknessMaxDuration = 60;
        public boolean chainExplosions = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Slime slime = new Slime();

    public static class Slime {
        public boolean biggerSize = true;
        public boolean canMerge = true;
        public boolean magmaConversion = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Vex vex = new Vex();

    public static class Vex {
        public boolean dieWithEvoker = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean naturalSpawnDarkForest = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Guardian guardian = new Guardian();

    public static class Guardian {
        @ConfigEntry.Gui.RequiresRestart
        public boolean naturalSpawn = true;
        public boolean silentKill = true;
    }
}

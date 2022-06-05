package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "skeleton")
public class SkeletonConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    public static class General {
        public boolean fleeGoal = true;
        public boolean swimGoal = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Skeleton skeleton = new Skeleton();

    public static class Skeleton {
        public boolean spectralArrow = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Stray stray = new Stray();

    public static class Stray {
        public boolean betterSlowness = true;
        public boolean frozenArrows = true;
    }
    @ConfigEntry.Gui.CollapsibleObject
    public WitherSkeleton witherSkeleton = new WitherSkeleton();

    public static class WitherSkeleton {
        public boolean baby = true;
        public boolean fireResistant = true;
    }
}

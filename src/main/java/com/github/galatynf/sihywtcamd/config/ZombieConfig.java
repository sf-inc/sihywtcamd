package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "zombie")
public class ZombieConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    public static class General {
        public boolean attackHeal = true;
        public boolean knockbackResistance = true;
        public boolean brainless = true;
        public boolean leaderAndSiege = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Husk husk = new Husk();

    public static class Husk {
        public boolean fireResistant = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Drowned drowned = new Drowned();

    public static class Drowned {
        public boolean tridentSpawn = true;
        public boolean highVelocity = true;
        public boolean guardianJockeySpawn = true;
    }
}

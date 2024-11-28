package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "zombies")
public class ZombiesConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    public static class General {
        public boolean moreKnockbackResistance = true;
        public boolean spawnMoreReinforcement = true;
        public boolean attributeVariations = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 9)
        public int babyTowerHeight = 4;
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

    @ConfigEntry.Gui.CollapsibleObject
    public ZombifiedPiglin zombifiedPiglin = new ZombifiedPiglin();

    public static class ZombifiedPiglin {
        public boolean bruteVariant = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public ZombieHorse zombieHorse = new ZombieHorse();

    public static class ZombieHorse {
        public boolean zombieHorseTrap = true;
        public boolean increasedSpeed = true;
    }
}

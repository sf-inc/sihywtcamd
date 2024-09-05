package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "arthropods")
public class ArthropodsConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    public static class General {
        public boolean noFallDamage = true;
        public boolean larvaeSpeedBonus = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Silverfish silverfish = new Silverfish();

    public static class Silverfish {
        public boolean infestedEverywhere = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public GeneralSpiders generalSpiders = new GeneralSpiders();

    public static class GeneralSpiders {
        @ConfigEntry.Gui.RequiresRestart
        public boolean webProjectileGoal = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Spider spider = new Spider();

    public static class Spider {
        public boolean babyOnDeath = true;
        public boolean babySpawnGroup = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public CaveSpider caveSpider = new CaveSpider();

    public static class CaveSpider {
        public boolean babyOnDeath = true;
        public boolean babySpawnGroup = true;

        public boolean jockey = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean naturalSpawn = true;
    }
}

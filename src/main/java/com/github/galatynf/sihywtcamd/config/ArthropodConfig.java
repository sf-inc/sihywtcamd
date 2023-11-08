package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "arthropod")
public class ArthropodConfig implements ConfigData {
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
    public Spider spider = new Spider();

    public static class Spider {
        public boolean baby = true;
        public boolean babyCaveSpider = true;
        public boolean webProjectileGoal = true;

        @ConfigEntry.Gui.Tooltip
        public boolean caveSpiderJockey = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean caveSpiderNaturalSpawn = true;
    }
}

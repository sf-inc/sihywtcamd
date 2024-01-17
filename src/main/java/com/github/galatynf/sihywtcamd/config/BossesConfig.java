package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "bosses")
public class BossesConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public EnderDragon enderDragon = new EnderDragon();

    public static class EnderDragon {
        public boolean crystalDestructionSpawnsPhantom = true;
        public boolean controlEndermanWhenLanding = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Wither wither = new Wither();

    public static class Wither {
        public boolean increasedHealth = true;
        public boolean stormyWeather = true;
        public boolean explosion = true;
        public boolean skeletonsSpawn = true;
    }
}

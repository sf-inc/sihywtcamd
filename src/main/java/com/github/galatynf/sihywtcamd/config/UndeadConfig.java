package com.github.galatynf.sihywtcamd.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "undead")
public class UndeadConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Phantom phantom = new Phantom();

    public static class Phantom {
        public boolean throughBlocks = true;
        public boolean lightFear = true;
        public boolean spawnInEndCities = true;
    }
}

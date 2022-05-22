package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "zombie")
public class ZombieConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    public static class General {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ZombieType typesBuffed = ZombieType.ALL;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int playersAttack = 20;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int mobsAttack = 50;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Husk husk = new Husk();

    public static class Husk {
        public boolean fireProtection = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Drowned drowned = new Drowned();

    public static class Drowned {
        public boolean tridentSpawn = true;
        public boolean highVelocity = true;
    }
}

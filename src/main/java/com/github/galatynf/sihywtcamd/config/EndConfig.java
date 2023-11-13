package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "end")
public class EndConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Enderman enderman = new Enderman();

    public static class Enderman {
        public boolean blindnessAttack = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Shulker shulker = new Shulker();

    public static class Shulker {
        public boolean cancelLevitation = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Endermite endermite = new Endermite();

    public static class Endermite {
        public boolean infestedChorus = true;
        public boolean teleportAttack = true;
    }
}

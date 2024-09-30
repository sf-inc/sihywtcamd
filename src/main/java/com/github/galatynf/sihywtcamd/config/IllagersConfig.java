package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "illagers")
public class IllagersConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Pillager pillager = new Pillager();

    public static class Pillager {
        public boolean moreEnchants = true;
        public boolean pillatrooper = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Vindicator vindicator = new Vindicator();

    public static class Vindicator {
        public boolean spawnInPatrols = true;
        public boolean speedBonus = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Evoker evoker = new Evoker();

    public static class Evoker {
        public boolean stopArrows = true;
        public boolean increasedHealth = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Illusioner illusioner = new Illusioner();

    public static class Illusioner {
        public boolean spawnInMansions = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Ravager ravager = new Ravager();

    public static class Ravager {
        public boolean spawnInPatrols = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Witch witch = new Witch();

    public static class Witch {
        public boolean fleeGoal = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean moreSpawn = true;
    }
}

package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "overworld")
public class OverworldConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public Mobs mobs = new Mobs();

    public static class Mobs {
        public boolean mobsLessFear = true;
        public boolean merchantHostility = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Zombies zombies = new Zombies();

    public static class Zombies {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ZombieType zombieTypeBuffed = ZombieType.ALL;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int zombiePlayersAttack = 20;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int zombieMobsAttack = 50;

        public boolean huskFireProtection = true;

        public boolean drownedTridentSpawn = true;
        public boolean drownedHighVelocity = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Skeletons skeletons = new Skeletons();

    public static class Skeletons {
        public boolean skeletonFleeGoal = true;
        public boolean strayBetterSlowness = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Spiders spiders = new Spiders();

    public static class Spiders {
        public boolean babySpiders = true;
        public boolean webProjectileGoal = true;

        @ConfigEntry.Gui.Tooltip
        public boolean caveSpiderJockey = true;
        public boolean caveSpiderNaturalSpawn = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Creepers creepers = new Creepers();

    public static class Creepers {
        public boolean explosionBlindness = true;
        public boolean chainExplosions = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Slimes slimes = new Slimes();

    public static class Slimes {
        public boolean slimeBiggerSize = true;
        public boolean slimeCanMerge = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Illagers illagers = new Illagers();

    public static class Illagers {
        public boolean pillagerMoreEnchants = true;
        public boolean pillagerSpeedBonus = true;
        public boolean vindicatorInPatrols = true;
        public boolean vindicatorSpeedBonus = true;
        public boolean ravagerInPatrols = true;
        public boolean evokerStopArrows = true;
        public boolean evokerIncreasedHealth = true;

        public boolean witchFleeGoal = true;
        public boolean witchMoreSpawn = true;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Phantoms phantoms = new Phantoms();

    public static class Phantoms {
        public boolean phantomThroughBlocks = true;
        public boolean phantomLightFear = true;
        public boolean phantomTranslucent = true;
    }

    public boolean guardianNaturalSpawn = true;
}

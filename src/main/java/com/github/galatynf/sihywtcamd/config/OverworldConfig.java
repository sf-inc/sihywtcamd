package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "overworld")
public class OverworldConfig implements ConfigData {
    public boolean mobsLessFear = true;

    @ConfigEntry.Gui.Tooltip
    public boolean zombieBuffedProtection = true;
    public boolean huskFireProtection = true;
    public boolean drownedTridentSpawn = true;
    public boolean drownedHighVelocity = true;

    public boolean skeletonFleeGoal = true;
    public boolean strayBetterSlowness = true;

    public boolean babySpiders = true;
    @ConfigEntry.Gui.Tooltip
    public boolean caveSpiderJockey = true;
    public boolean caveSpiderNaturalSpawn = true;

    public boolean slimeBiggerSize = true;
    public boolean slimeCanMerge = true;

    public boolean pillagerMoreEnchants = true;
    public boolean pillagerSpeedBonus = true;
    public boolean vindicatorInPatrols = true;
    public boolean vindicatorSpeedBonus = true;
    public boolean ravagerInPatrols = true;
    public boolean evokerStopArrows = true;
    public boolean evokerIncreasedHealth = true;

    public boolean witchFleeGoal = true;
    public boolean witchMoreSpawn = true;

    public boolean phantomThroughBlocks = true;
    public boolean phantomLightFear = true;
    public boolean phantomTranslucent = true;

    public boolean guardianNaturalSpawn = true;
}

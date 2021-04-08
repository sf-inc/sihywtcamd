package com.github.galatynf.sihywtcamd.config;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;

@Config(name = "sihywtcamd")
public class ModConfig implements ConfigData {
    public boolean mobsLessFear = true;

    @ConfigEntry.Gui.Tooltip
    public boolean zombieBuffedProtection = true;
    public boolean huskFireProtection = true;
    public boolean drownedTridentSpawn = true;
    public boolean drownedHighVelocity = true;

    public boolean skeletonFleeGoal = true;
    public boolean strayBetterSlowness = true;

    public boolean endermanBlindness = true;
    public boolean shulkerBlindness = true;

    public boolean babySpiders = true;

    public boolean slimeBiggerSize = true;
    public boolean slimeCanMerge = true;

    // public boolean guardianNaturalSpawn = true;

    public boolean phantomThroughBlocks = true;
    public boolean phantomLightFear = true;
    public boolean phantomTranslucent = true;

    public boolean babyWitherSkeleton = true;
    public boolean blazeFireCollision = true;
    public boolean ghastIncreasedHealth = true;
    public boolean piglinFullGold = true;
    public boolean piglinRideHoglin = true;

    public boolean pillagerMoreEnchants = true;
    public boolean pillagerSpeedBonus = true;
    public boolean vindicatorSpeedBonus = true;
    public boolean evokerStopArrows = true;
    public boolean evokerIncreasedHealth = true;

    public boolean witchFleeGoal = true;
    // public boolean witchMoreSpawn = true;

    public boolean witherIncreasedHealth = true;
    public boolean witherSpawnSkeletons = true;


    public static ModConfig get() {
        if (!Sihywtcamd.areConfigsInit) {
            AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
            Sihywtcamd.areConfigsInit = true;
        }
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}

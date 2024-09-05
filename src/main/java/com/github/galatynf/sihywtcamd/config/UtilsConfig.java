package com.github.galatynf.sihywtcamd.config;

public class UtilsConfig {
    public static boolean babySpidersEnabled() {
        return ModConfig.get().arthropods.spider.babyOnDeath
                || ModConfig.get().arthropods.spider.babySpawnGroup;
    }

    public static boolean babyCaveSpidersEnabled() {
        return ModConfig.get().arthropods.caveSpider.babyOnDeath
                || ModConfig.get().arthropods.caveSpider.babySpawnGroup;
    }
}

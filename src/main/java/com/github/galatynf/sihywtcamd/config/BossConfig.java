package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "boss")
public class BossConfig implements ConfigData {
    public boolean witherIncreasedHealth = true;
    public boolean witherSpawnSkeletons = true;
}

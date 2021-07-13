package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "end")
public class EndConfig implements ConfigData {
    public boolean endermanBlindness = true;
    public boolean shulkerBlindness = true;
}

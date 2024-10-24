package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "general")
public class GeneralConfig implements ConfigData {
    public boolean increasedFollowRange = true;
    public boolean mobsLessFear = true;
    public boolean merchantHostility = true;
    public boolean pathPassengerAware = true;
}

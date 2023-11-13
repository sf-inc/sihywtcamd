package com.github.galatynf.sihywtcamd.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "end")
public class EndConfig implements ConfigData {
    public boolean endermanBlindness = true;
    public boolean shulkerCancelLevitation = true;
    public boolean endermiteInChorus = true;

    @ConfigEntry.Gui.CollapsibleObject
    public Endermite endermite = new Endermite();

    public static class Endermite {
        public boolean teleportAttack = true;
    }
}

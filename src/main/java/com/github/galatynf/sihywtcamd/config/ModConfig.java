package com.github.galatynf.sihywtcamd.config;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = "sihywtcamd")
public class ModConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("overworld")
    @ConfigEntry.Gui.TransitiveObject
    public OverworldConfig overworld = new OverworldConfig();

    @ConfigEntry.Category("undead")
    @ConfigEntry.Gui.TransitiveObject
    public UndeadConfig undead = new UndeadConfig();

    @ConfigEntry.Category("zombies")
    @ConfigEntry.Gui.TransitiveObject
    public ZombiesConfig zombies = new ZombiesConfig();

    @ConfigEntry.Category("skeletons")
    @ConfigEntry.Gui.TransitiveObject
    public SkeletonsConfig skeletons = new SkeletonsConfig();

    @ConfigEntry.Category("arthropods")
    @ConfigEntry.Gui.TransitiveObject
    public ArthropodsConfig arthropods = new ArthropodsConfig();

    @ConfigEntry.Category("illagers")
    @ConfigEntry.Gui.TransitiveObject
    public IllagersConfig illagers = new IllagersConfig();

    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.TransitiveObject
    public NetherConfig nether = new NetherConfig();

    @ConfigEntry.Category("end")
    @ConfigEntry.Gui.TransitiveObject
    public EndConfig end = new EndConfig();

    @ConfigEntry.Category("bosses")
    @ConfigEntry.Gui.TransitiveObject
    public BossesConfig bosses = new BossesConfig();

    @ConfigEntry.Category("cosmetics")
    @ConfigEntry.Gui.TransitiveObject
    public CosmeticsConfig cosmetics = new CosmeticsConfig();

    public static ModConfig get() {
        if (!Sihywtcamd.areConfigsInit) {
            AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
            Sihywtcamd.areConfigsInit = true;
        }
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}

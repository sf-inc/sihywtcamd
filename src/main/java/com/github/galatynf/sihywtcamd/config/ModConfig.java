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

    @ConfigEntry.Category("zombie")
    @ConfigEntry.Gui.TransitiveObject
    public ZombieConfig zombie = new ZombieConfig();

    @ConfigEntry.Category("skeleton")
    @ConfigEntry.Gui.TransitiveObject
    public SkeletonConfig skeleton = new SkeletonConfig();

    @ConfigEntry.Category("arthropod")
    @ConfigEntry.Gui.TransitiveObject
    public ArthropodConfig arthropod = new ArthropodConfig();

    @ConfigEntry.Category("illager")
    @ConfigEntry.Gui.TransitiveObject
    public IllagerConfig illager = new IllagerConfig();

    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.TransitiveObject
    public NetherConfig nether = new NetherConfig();

    @ConfigEntry.Category("end")
    @ConfigEntry.Gui.TransitiveObject
    public EndConfig end = new EndConfig();

    @ConfigEntry.Category("boss")
    @ConfigEntry.Gui.TransitiveObject
    public BossConfig boss = new BossConfig();

    @ConfigEntry.Category("cosmetic")
    @ConfigEntry.Gui.TransitiveObject
    public CosmeticConfig cosmetic = new CosmeticConfig();

    public static ModConfig get() {
        if (!Sihywtcamd.areConfigsInit) {
            AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
            Sihywtcamd.areConfigsInit = true;
        }
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}

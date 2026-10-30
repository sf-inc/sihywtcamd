package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.init.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class Sihywtcamd implements ModInitializer {
    public static boolean areConfigsInit = false;
    public static final String MOD_ID = "sihywtcamd";

    @Override
    public void onInitialize() {
        if (!Sihywtcamd.areConfigsInit) {
            AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
            Sihywtcamd.areConfigsInit = true;
        }

        AdvancementRegistry.init();
        BiomeFeatures.init();
        BiomeSpawn.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

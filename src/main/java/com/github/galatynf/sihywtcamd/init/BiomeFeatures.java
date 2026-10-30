package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BiomeFeatures {
    public static void init() {
        if (ModConfig.get().arthropods.silverfish.infestedEverywhere) {
            BiomeModifications.addFeature(
                    BiomeSelectors.foundInOverworld(),
                    GenerationStep.Decoration.UNDERGROUND_ORES,
                    OrePlacements.ORE_INFESTED);
        }
    }
}

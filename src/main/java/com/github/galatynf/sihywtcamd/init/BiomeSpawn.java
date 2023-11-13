package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.BiomeKeys;

public class BiomeSpawn {
    public static void init() {
        if (ModConfig.get().arthropods.spider.caveSpiderNaturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.spawnsOneOf(EntityType.SPIDER),
                    SpawnGroup.MONSTER,
                    EntityType.CAVE_SPIDER,
                    80, 1, 3);
        }
        if (ModConfig.get().illagers.witch.moreSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.spawnsOneOf(EntityType.WITCH),
                    SpawnGroup.MONSTER,
                    EntityType.WITCH,
                    3, 2, 2);
        }
        if (ModConfig.get().overworld.guardian.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN),
                    SpawnGroup.MONSTER,
                    EntityType.GUARDIAN,
                    2, 1, 2);
        }
        if (ModConfig.get().overworld.guardian.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.tag(BiomeTags.IS_OCEAN),
                    SpawnGroup.MONSTER,
                    EntityType.GUARDIAN,
                    1, 1, 2);
        }
        if (ModConfig.get().overworld.vex.naturalSpawnDarkForest) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST),
                    SpawnGroup.MONSTER,
                    EntityType.VEX,
                    25, 2, 3);
        }
    }
}

package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

import java.util.function.Predicate;

public class BiomeSpawn {
    public static void init() {
        if (ModConfig.get().arthropods.caveSpider.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.spawnsOneOf(EntityType.SPIDER),
                    SpawnGroup.MONSTER,
                    EntityType.CAVE_SPIDER,
                    80, 1, 3);
        }
        if (ModConfig.get().skeletons.stray.coldBiomeSpawn) {
            Predicate<BiomeSelectionContext> coldPredicate = context -> context.getBiome().getTemperature() < 0.15f;
            Predicate<BiomeSelectionContext> biomePredicate = coldPredicate.and(BiomeSelectors.spawnsOneOf(EntityType.STRAY).negate());

            removeSpawn(biomePredicate, EntityType.SKELETON);
            BiomeModifications.addSpawn(
                    biomePredicate,
                    SpawnGroup.MONSTER,
                    EntityType.SKELETON,
                    20, 4, 4);
            BiomeModifications.addSpawn(
                    biomePredicate,
                    SpawnGroup.MONSTER,
                    EntityType.STRAY,
                    80, 4, 4);
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

    private static void removeSpawn(Predicate<BiomeSelectionContext> biomeSelector, EntityType<?> entityType) {
        // We need the entity type to be registered, or we cannot deduce an ID otherwise
        Identifier id = Registries.ENTITY_TYPE.getId(entityType);
        Preconditions.checkState(Registries.ENTITY_TYPE.getKey(entityType).isPresent(), "Unregistered entity type: %s", entityType);

        BiomeModifications.create(id).add(ModificationPhase.ADDITIONS, biomeSelector,
                context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
    }
}

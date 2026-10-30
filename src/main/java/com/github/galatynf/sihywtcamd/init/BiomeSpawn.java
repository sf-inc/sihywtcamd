package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import java.util.function.Predicate;

public class BiomeSpawn {
    public static void init() {
        if (ModConfig.get().arthropods.caveSpider.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.spawnsOneOf(EntityType.SPIDER),
                    MobCategory.MONSTER,
                    EntityType.CAVE_SPIDER,
                    80, 1, 3);
        }
        if (ModConfig.get().skeletons.stray.coldBiomeSpawn) {
            Predicate<BiomeSelectionContext> coldPredicate = context -> context.getBiome().getBaseTemperature() < 0.15f;
            Predicate<BiomeSelectionContext> biomePredicate = coldPredicate
                    .and(BiomeSelectors.spawnsOneOf(EntityType.SKELETON))
                    .and(BiomeSelectors.spawnsOneOf(EntityType.STRAY).negate())
                    .and(BiomeSelectors.foundInOverworld());

            removeSpawn(biomePredicate, EntityType.SKELETON);
            BiomeModifications.addSpawn(
                    biomePredicate,
                    MobCategory.MONSTER,
                    EntityType.SKELETON,
                    20, 4, 4);
            BiomeModifications.addSpawn(
                    biomePredicate,
                    MobCategory.MONSTER,
                    EntityType.STRAY,
                    80, 4, 4);
        }
        if (ModConfig.get().illagers.witch.moreSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.spawnsOneOf(EntityType.WITCH),
                    MobCategory.MONSTER,
                    EntityType.WITCH,
                    3, 2, 2);
        }
        if (ModConfig.get().overworld.guardian.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN),
                    MobCategory.MONSTER,
                    EntityType.GUARDIAN,
                    2, 1, 2);
        }
        if (ModConfig.get().overworld.guardian.naturalSpawn) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.tag(BiomeTags.IS_OCEAN),
                    MobCategory.MONSTER,
                    EntityType.GUARDIAN,
                    1, 1, 2);
        }
        if (ModConfig.get().overworld.vex.naturalSpawnDarkForest) {
            BiomeModifications.addSpawn(
                    BiomeSelectors.includeByKey(Biomes.DARK_FOREST),
                    MobCategory.MONSTER,
                    EntityType.VEX,
                    25, 2, 3);
        }
    }

    private static void removeSpawn(Predicate<BiomeSelectionContext> biomeSelector, EntityType<?> entityType) {
        // We need the entity type to be registered, or we cannot deduce an ID otherwise
        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        Preconditions.checkState(BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).isPresent(), "Unregistered entity type: %s", entityType);

        BiomeModifications.create(id).add(ModificationPhase.ADDITIONS, biomeSelector,
                context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
    }
}

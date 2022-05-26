package com.github.galatynf.sihywtcamd.mixin.biome;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DefaultBiomeFeatures.class)
public abstract class BiomeFeatMixin {
    private static final PlacedFeature ORE_DEEPER_INFESTED = PlacedFeatures.register("ore_less_infested", OreConfiguredFeatures.ORE_INFESTED.withPlacement(List.of(CountPlacementModifier.of(12), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(0)), BiomePlacementModifier.of())));

    @Inject(method = "addMonsters", at = @At("TAIL"))
    private static void changeWitchSpawnrate(SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight,
                                             int skeletonWeight, boolean drowned, CallbackInfo ci) {
        if (ModConfig.get().illager.witch.moreSpawn) {
            builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 3, 1, 1));
        }
    }

    @Inject(method = "addMonsters", at = @At("TAIL"))
    private static void spawnCaveSpiderNaturally(SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight,
                                                 int skeletonWeight, boolean drowned, CallbackInfo ci) {
        if (ModConfig.get().arthropod.spider.caveSpiderNaturalSpawn) {
            builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.CAVE_SPIDER, 80, 1, 3));
        }
    }

    @Inject(method = "addOceanMobs", at = @At("TAIL"))
    private static void spawnGuardianNaturally(SpawnSettings.Builder builder, int squidWeight, int squidMaxGroupSize,
                                               int codWeight, CallbackInfo ci) {
        if (ModConfig.get().overworld.guardianNaturalSpawn) {
            builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 2, 1, 2));
        }
    }

    @Inject(method = "addDefaultOres(Lnet/minecraft/world/biome/GenerationSettings$Builder;Z)V", at = @At("TAIL"))
    private static void generateInfestedEverywhere(GenerationSettings.Builder builder, boolean largeCopperOreBlob, CallbackInfo ci) {
        if (ModConfig.get().arthropod.silverfish.infestedEverywhere) {
            builder.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ORE_DEEPER_INFESTED);
        }
    }
}

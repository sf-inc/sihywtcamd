package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class BiomeFeatMixin {
    @Inject(method = "addMonsters", at = @At("TAIL"))
    private static void changeWitchSpawnrate(SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight,
                                             int skeletonWeight, CallbackInfo ci) {
        if (ModConfig.get().overworld.illagers.witchMoreSpawn) {
            builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 3, 1, 1));
        }
    }

    @Inject(method = "addMonsters", at = @At("TAIL"))
    private static void spawnCaveSpiderNaturally(SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight,
                                             int skeletonWeight, CallbackInfo ci) {
        if (ModConfig.get().overworld.spiders.caveSpiderNaturalSpawn) {
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
}

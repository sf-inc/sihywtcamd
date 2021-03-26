package com.github.galatynf.sihywtcamd.mixin;

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
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.WITCH, 3, 1, 1));
    }

    @Inject(method = "addOceanMobs", at = @At("TAIL"))
    private static void spawnGuardianNaturally(SpawnSettings.Builder builder, int squidWeight, int squidMaxGroupSize,
                                               int codWeight, CallbackInfo ci) {
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 2, 1, 2));
    }
}

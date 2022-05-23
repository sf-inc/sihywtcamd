package com.github.galatynf.sihywtcamd.mixin.biome;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(OverworldBiomeCreator.class)
public class OverworldBiomeMixin {
    @ModifyVariable(method = "createDarkForest", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBatsAndMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V"))
    private static SpawnSettings.Builder addVexes(SpawnSettings.Builder builder) {
        if (ModConfig.get().overworld.vex.naturalSpawnDarkForest) {
            builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.VEX, 25, 2, 3));
        }
        return builder;
    }
}

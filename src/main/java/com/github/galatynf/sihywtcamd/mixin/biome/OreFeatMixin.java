package com.github.galatynf.sihywtcamd.mixin.biome;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.world.gen.feature.OrePlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OrePlacedFeatures.class)
public class OreFeatMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;"))
    private static int increaseInfestedSpawn(int count) {
        return ModConfig.get().arthropod.silverfish.infestedEverywhere ? 24 : count;
    }
}

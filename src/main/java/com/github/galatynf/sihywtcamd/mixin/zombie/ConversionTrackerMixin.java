package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.entity.ZombieAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionTracker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConversionTracker.class)
public class ConversionTrackerMixin {
    @WrapOperation(method = "doConversion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;convertTo(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/ConversionParams;Lnet/minecraft/world/entity/ConversionParams$AfterConversion;)Lnet/minecraft/world/entity/Mob;"))
    private Mob keepZombieAttributesOnConversion(Mob instance, EntityType<?> entityType, ConversionParams conversionParams,
                                                 ConversionParams.AfterConversion<Mob> finalizer, Operation<Mob> original) {
        if (!(instance instanceof Zombie zombie)) {
            return original.call(instance, entityType, conversionParams, finalizer);
        }

        return original.call(instance, entityType, conversionParams,
                (ConversionParams.AfterConversion<Mob>) converted -> {
            if (converted instanceof Zombie convertedZombie) {
                ZombieAttributes.copyOnConversion(zombie, convertedZombie);
            }
            finalizer.finalizeConversion(converted);
        });
    }
}

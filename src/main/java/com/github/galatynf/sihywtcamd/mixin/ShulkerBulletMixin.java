package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {
    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean cancelLevitation(LivingEntity livingEntity, MobEffectInstance effect, Entity source, Operation<Boolean> original) {
        if (ModConfig.get().end.shulker.cancelLevitation
                && livingEntity.hasEffect(MobEffects.LEVITATION)
                && livingEntity.getRandom().nextFloat() < 0.33f) {
            return livingEntity.removeEffect(MobEffects.LEVITATION);
        }
        return original.call(livingEntity, effect, source);
    }
}

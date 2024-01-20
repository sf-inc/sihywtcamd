package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletMixin {
    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean cancelLevitation(LivingEntity livingEntity, StatusEffectInstance effect, Entity source) {
        if (ModConfig.get().end.shulker.cancelLevitation
                && livingEntity.hasStatusEffect(StatusEffects.LEVITATION)
                && livingEntity.getRandom().nextFloat() < 0.33f) {
            return livingEntity.removeStatusEffect(StatusEffects.LEVITATION);
        }
        return livingEntity.addStatusEffect(effect, source);
    }
}

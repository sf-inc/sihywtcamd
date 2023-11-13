package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletMixin {
    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private void cancelLevitation(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (ModConfig.get().end.shulker.cancelLevitation
                && entityHitResult.getEntity() instanceof PlayerEntity playerEntity
                && playerEntity.hasStatusEffect(StatusEffects.LEVITATION)
                && playerEntity.getRandom().nextFloat() < 0.33f) {
            playerEntity.removeStatusEffect(StatusEffects.LEVITATION);
        }
    }
}

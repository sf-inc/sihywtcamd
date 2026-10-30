package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ServerExplosion;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends EntityMixin {
    @Shadow private int explosionRadius;

    @Shadow public abstract boolean isPowered();

    @Inject(method = "explodeCreeper", at = @At("HEAD"))
    private void effectToEntities(CallbackInfo ci) {
        if (this.level().isClientSide()) return;

        final int fatigueDuration = ModConfig.get().overworld.creeper.explosionFatigueMaxDuration * 20;
        final int weaknessDuration = ModConfig.get().overworld.creeper.explosionWeaknessMaxDuration * 20;
        if (fatigueDuration <= 0 && weaknessDuration <= 0) return;

        Entity thisEntity = (Entity)(Object) this;
        final int explosionRadius = this.explosionRadius * (this.isPowered() ? 3 : 2);
        List<Entity> entityList = this.level().getEntities(thisEntity,
                this.getBoundingBox().inflate(explosionRadius),
                entity -> entity instanceof LivingEntity && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                        && this.distanceTo(entity) < explosionRadius);

        for (Entity entity : entityList) {
            final double multiplier = Math.sqrt(1.0 - (this.distanceTo(entity) / explosionRadius))
                    * ServerExplosion.getSeenPercent(thisEntity.position(), entity);
            if (multiplier <= 0.0) continue;
            LivingEntity livingEntity = (LivingEntity) entity;
            if (fatigueDuration > 0) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MINING_FATIGUE, (int) (fatigueDuration * multiplier)), thisEntity);
            }
            if (weaknessDuration > 0) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, (int) (weaknessDuration * multiplier)), thisEntity);
            }
        }
    }
}

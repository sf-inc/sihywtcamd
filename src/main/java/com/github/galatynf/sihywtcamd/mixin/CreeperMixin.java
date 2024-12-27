package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends EntityMixin {
    @Shadow private int explosionRadius;

    @Shadow public abstract boolean isCharged();

    @Shadow public abstract void ignite();

    @Inject(method = "explode", at = @At("HEAD"))
    private void effectToEntities(CallbackInfo ci) {
        if (this.getWorld().isClient()) return;

        final int fatigueDuration = ModConfig.get().overworld.creeper.explosionFatigueMaxDuration * 20;
        final int weaknessDuration = ModConfig.get().overworld.creeper.explosionWeaknessMaxDuration * 20;
        if (fatigueDuration <= 0 && weaknessDuration <= 0) return;

        Entity thisEntity = (Entity)(Object) this;
        final int explosionRadius = this.explosionRadius * (this.isCharged() ? 3 : 2);
        List<Entity> entityList = this.getWorld().getOtherEntities(thisEntity,
                this.getBoundingBox().expand(explosionRadius),
                entity -> entity instanceof LivingEntity && this.distanceTo(entity) < explosionRadius);

        for (Entity entity : entityList) {
            final double multiplier = Math.sqrt(1.0 - (this.distanceTo(entity) / explosionRadius));
            LivingEntity livingEntity = (LivingEntity) entity;
            if (fatigueDuration > 0) {
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.MINING_FATIGUE, (int) (fatigueDuration * multiplier)), thisEntity);
            }
            if (weaknessDuration > 0) {
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WEAKNESS, (int) (weaknessDuration * multiplier)), thisEntity);
            }
        }
    }

    @Override
    public boolean updateImmunityToExplosion(boolean original, Explosion explosion) {
        if (ModConfig.get().overworld.creeper.chainExplosions
                && (!(explosion.getCausingEntity() instanceof CreeperEntity creeper)
                || !creeper.isCharged())) {
            this.ignite();
            return true;
        } else {
            return original;
        }
    }
}

package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends LivingEntityMixin {
    @Shadow private int explosionRadius;

    @Shadow public abstract boolean isCharged();

    @Shadow public abstract void ignite();

    public CreeperMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "explode", at = @At("HEAD"))
    private void effectToEntities(CallbackInfo ci) {
        if (this.getWorld().isClient()) return;

        final int fatigueDuration = ModConfig.get().overworld.creeper.explosionFatigueMaxDuration * 20;
        final int weaknessDuration = ModConfig.get().overworld.creeper.explosionWeaknessMaxDuration * 20;
        if (fatigueDuration <= 0 && weaknessDuration <= 0) return;

        final int explosionRadius = this.explosionRadius * (this.isCharged() ? 3 : 2);
        List<Entity> entityList = this.getWorld().getOtherEntities(this,
                this.getBoundingBox().expand(explosionRadius),
                entity -> entity instanceof LivingEntity && this.distanceTo(entity) < explosionRadius);

        for (Entity entity : entityList) {
            final double multiplier = Math.sqrt(1.0 - (this.distanceTo(entity) / explosionRadius));
            LivingEntity livingEntity = (LivingEntity) entity;
            if (fatigueDuration > 0) {
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.MINING_FATIGUE, (int) (fatigueDuration * multiplier)), this);
            }
            if (weaknessDuration > 0) {
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WEAKNESS, (int) (weaknessDuration * multiplier)), this);
            }
        }
    }

    @Override
    public boolean updateDamage(boolean original, ServerWorld world, DamageSource source, float amount) {
        if (!original) return false;

        if (ModConfig.get().overworld.creeper.chainExplosions
                && source.isIn(DamageTypeTags.IS_EXPLOSION)
                && (!(source.getAttacker() instanceof CreeperEntity creeper)
                || !creeper.isCharged())) {
            this.ignite();
            return false;
        } else {
            return true;
        }
    }
}

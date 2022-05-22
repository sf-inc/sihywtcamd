package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends HostileEntity {
    @Shadow private int explosionRadius;

    @Shadow public abstract boolean shouldRenderOverlay();

    @Shadow public abstract void ignite();

    protected CreeperMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "explode", at = @At("HEAD"))
    private void effectToEntities(CallbackInfo ci) {
        final int explosionRadius = this.explosionRadius * (this.shouldRenderOverlay() ? 3 : 2);
        final int duration = 150;
        Vec3d explosionRadiuses = new Vec3d(explosionRadius, explosionRadius, explosionRadius);
        List<Entity> entityList = this.world.getOtherEntities(this,
                new Box(this.getPos().subtract(explosionRadiuses), this.getPos().add(explosionRadiuses)),
                entity -> entity instanceof PlayerEntity && this.distanceTo(entity) < explosionRadius);

        for (Entity entity : entityList) {
            final double multiplier = Math.sqrt(1.0 - (this.distanceTo(entity) / explosionRadius));
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (ModConfig.get().overworld.creeper.explosionFatigue) {
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.MINING_FATIGUE, (int) (duration * multiplier)), this);
            }
            if (ModConfig.get().overworld.creeper.explosionWeakness) {
                playerEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.WEAKNESS, (int) (duration * multiplier)), this);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (ModConfig.get().overworld.creeper.chainExplosions && source.isExplosive()) {
            this.ignite();
            return false;
        } else {
            return super.damage(source, amount);
        }
    }
}
